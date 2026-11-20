import 'package:drift/drift.dart';
import '../../data/local/database/app_database.dart';
import '../../domain/value_objects/dose_status.dart';
import '../../domain/value_objects/medication_enums.dart';

class DoseEngineService {
  final AppDatabase db;

  DoseEngineService(this.db);

  /// Pre-generates dose records for the next [daysAhead] days for all active schedules.
  /// Idempotent: existing instances are ignored thanks to unique index (schedule_id, scheduled_at).
  Future<int> generateRollingDoses({DateTime? startDate, int daysAhead = 7}) async {
    final now = startDate ?? DateTime.now();
    final startOfToday = DateTime(now.year, now.month, now.day);
    final activeSchedules = await db.scheduleDao.getAllActiveSchedules();
    int generatedCount = 0;

    for (final schedule in activeSchedules) {
      final profileSettings = await db.profileDao.getSettingsForProfile(schedule.profileId);
      final graceMinutes = profileSettings?.gracePeriodMinutes ?? 60;

      for (int i = 0; i < daysAhead; i++) {
        final targetDate = startOfToday.add(Duration(days: i));
        
        // Check schedule start and end dates
        if (targetDate.isBefore(DateTime(schedule.startDate.year, schedule.startDate.month, schedule.startDate.day))) {
          continue;
        }
        if (schedule.endDate != null &&
            targetDate.isAfter(DateTime(schedule.endDate!.year, schedule.endDate!.month, schedule.endDate!.day))) {
          continue;
        }

        // Match recurrence rule
        final shouldSchedule = _matchesSchedule(schedule, targetDate);
        if (!shouldSchedule) continue;

        final scheduledTime = DateTime(
          targetDate.year,
          targetDate.month,
          targetDate.day,
          schedule.timeHour,
          schedule.timeMinute,
        );

        final graceDeadline = scheduledTime.add(Duration(minutes: graceMinutes));
        final notificationId = _generateNotificationId(schedule.id, scheduledTime);

        final insertedId = await db.doseRecordDao.insertDoseIdempotent(
          DoseRecordsCompanion.insert(
            profileId: schedule.profileId,
            medicineId: schedule.medicineId,
            scheduleId: Value(schedule.id),
            scheduledAt: scheduledTime,
            reminderAt: scheduledTime,
            status: const Value('pending'),
            gracePeriodDeadline: graceDeadline,
            notificationId: notificationId,
          ),
        );

        if (insertedId > 0) {
          generatedCount++;
        }
      }
    }

    return generatedCount;
  }

  bool _matchesSchedule(ScheduleEntry schedule, DateTime targetDate) {
    final type = ScheduleType.fromString(schedule.scheduleType);

    switch (type) {
      case ScheduleType.daily:
        return true;

      case ScheduleType.specificDays:
        if (schedule.daysOfWeek == null || schedule.daysOfWeek!.isEmpty) return false;
        final selectedDays = schedule.daysOfWeek!.split(',').map((e) => int.tryParse(e.trim()) ?? 0).toSet();
        // Dart DateTime.weekday: 1=Mon, 7=Sun
        return selectedDays.contains(targetDate.weekday);

      case ScheduleType.intervalDays:
        final interval = schedule.intervalDays ?? 1;
        if (interval <= 1) return true;
        final diffDays = targetDate.difference(DateTime(schedule.startDate.year, schedule.startDate.month, schedule.startDate.day)).inDays;
        return diffDays >= 0 && (diffDays % interval == 0);

      case ScheduleType.asNeeded:
        return false; // As-needed doses are created on demand, not pre-scheduled
    }
  }

  /// Take Dose Action
  Future<void> takeDose(int doseId, {DateTime? takenAt}) async {
    return db.transaction(() async {
      final doseWithMed = await db.doseRecordDao.getDoseWithMedicine(doseId);
      if (doseWithMed == null) return;
      if (doseWithMed.dose.status == DoseStatus.taken.value) return; // Prevent duplicate

      final confirmationTime = takenAt ?? DateTime.now();

      await db.doseRecordDao.updateDoseStatus(
        doseId: doseId,
        status: DoseStatus.taken.value,
        takenAt: confirmationTime,
      );

      // Decrement stock if stock tracking is active for this medicine
      if (doseWithMed.medicine.stockCount > 0) {
        await db.medicineDao.decrementStock(doseWithMed.medicine.id);
      }
    });
  }

  /// Snooze Dose Action
  /// Requirement: Keeps dose pending, schedules new reminder, preserves original grace-period deadline
  Future<void> snoozeDose(int doseId, {int? snoozeMinutes}) async {
    final doseWithMed = await db.doseRecordDao.getDoseWithMedicine(doseId);
    if (doseWithMed == null) return;
    if (doseWithMed.dose.status == DoseStatus.taken.value) return;

    final profileSettings = await db.profileDao.getSettingsForProfile(doseWithMed.dose.profileId);
    final duration = snoozeMinutes ?? profileSettings?.defaultSnoozeMinutes ?? 15;

    final newReminderAt = DateTime.now().add(Duration(minutes: duration));

    await db.doseRecordDao.updateDoseStatus(
      doseId: doseId,
      status: DoseStatus.pending.value, // Requirement: keep the dose pending
      reminderAt: newReminderAt,
      snoozeCount: doseWithMed.dose.snoozeCount + 1,
    );
  }

  /// Skip Dose Action
  /// Requirement: Explicit confirmation, optional reason, never count as missed in adherence
  Future<void> skipDose(int doseId, {String? reason, DateTime? skippedAt}) async {
    final doseWithMed = await db.doseRecordDao.getDoseWithMedicine(doseId);
    if (doseWithMed == null) return;
    if (doseWithMed.dose.status == DoseStatus.taken.value) return;

    await db.doseRecordDao.updateDoseStatus(
      doseId: doseId,
      status: DoseStatus.skipped.value,
      skippedAt: skippedAt ?? DateTime.now(),
      skipReason: reason,
    );
  }

  /// Auto-Missed Evaluation
  /// Automatically transitions unresolved doses past their grace-period deadline to missed
  Future<int> evaluateMissedDoses({DateTime? currentTime}) async {
    final now = currentTime ?? DateTime.now();
    final expiredDoses = await db.doseRecordDao.getUnresolvedExpiredDoses(now);

    for (final dose in expiredDoses) {
      await db.doseRecordDao.updateDoseStatus(
        doseId: dose.id,
        status: DoseStatus.missed.value,
      );
    }

    return expiredDoses.length;
  }

  /// Correct Missed Dose
  /// Requirement: Allow correction of mistakenly recorded missed doses
  Future<void> correctMissedDose(int doseId, {DateTime? actualTakenAt}) async {
    return db.transaction(() async {
      final doseWithMed = await db.doseRecordDao.getDoseWithMedicine(doseId);
      if (doseWithMed == null) return;

      final confirmationTime = actualTakenAt ?? doseWithMed.dose.scheduledAt;

      await db.doseRecordDao.updateDoseStatus(
        doseId: doseId,
        status: DoseStatus.taken.value,
        takenAt: confirmationTime,
      );

      if (doseWithMed.medicine.stockCount > 0) {
        await db.medicineDao.decrementStock(doseWithMed.medicine.id);
      }
    });
  }

  /// Deterministic 31-bit positive integer notification ID
  int _generateNotificationId(int scheduleId, DateTime scheduledTime) {
    final hash = (scheduleId * 31 + scheduledTime.millisecondsSinceEpoch ~/ 60000) & 0x7FFFFFFF;
    return hash;
  }
}
