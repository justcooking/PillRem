import 'package:flutter_test/flutter_test.dart';
import 'package:drift/native.dart';
import 'package:drift/drift.dart' hide isNotNull, isNull;
import 'package:pillrem/data/local/database/app_database.dart';
import 'package:pillrem/domain/value_objects/dose_status.dart';
import 'package:pillrem/domain/value_objects/medication_enums.dart';
import 'package:pillrem/services/dose_engine/dose_engine_service.dart';
import 'package:pillrem/services/adherence/adherence_service.dart';

void main() {
  late AppDatabase db;
  late DoseEngineService doseEngine;
  late AdherenceService adherenceService;

  setUp(() {
    // In-memory isolated database for rapid, hermetic testing
    db = AppDatabase(NativeDatabase.memory());
    doseEngine = DoseEngineService(db);
    adherenceService = AdherenceService(db);
  });

  tearDown(() async {
    await db.close();
  });

  group('Profiles & Multi-profile Isolation Tests', () {
    test('Creating first profile automatically sets it as active with default settings', () async {
      final profileId = await db.profileDao.createProfile(
        name: 'Sarah',
        avatarColor: 0xFF2B6CB0,
        avatarIcon: 'person',
      );

      final active = await db.profileDao.getActiveProfile();
      expect(active, isNotNull);
      expect(active!.id, equals(profileId));
      expect(active.name, equals('Sarah'));
      expect(active.isActive, isTrue);

      final settings = await db.profileDao.getSettingsForProfile(profileId);
      expect(settings, isNotNull);
      expect(settings!.gracePeriodMinutes, equals(60));
      expect(settings.defaultSnoozeMinutes, equals(15));
    });

    test('Creating a second profile does not displace active profile until switched', () async {
      final p1 = await db.profileDao.createProfile(name: 'Sarah', avatarColor: 0xFF2B6CB0, avatarIcon: 'person');
      final p2 = await db.profileDao.createProfile(name: 'Grandpa Joe', avatarColor: 0xFF388E67, avatarIcon: 'face');

      var active = await db.profileDao.getActiveProfile();
      expect(active!.id, equals(p1));

      // Switch to p2
      await db.profileDao.setActiveProfile(p2);
      active = await db.profileDao.getActiveProfile();
      expect(active!.id, equals(p2));
      expect(active.name, equals('Grandpa Joe'));
    });

    test('Deleting profile cascades to its medicines, schedules and dose records', () async {
      final p1 = await db.profileDao.createProfile(name: 'Sarah', avatarColor: 0xFF2B6CB0, avatarIcon: 'person');
      final medId = await db.medicineDao.insertMedicine(
        MedicinesCompanion.insert(
          profileId: p1,
          name: 'Amoxicillin',
          dosageAmount: const Value(500.0),
          dosageUnit: const Value('mg'),
        ),
      );

      final scheduleId = await db.scheduleDao.insertSchedule(
        SchedulesCompanion.insert(
          profileId: p1,
          medicineId: medId,
          scheduleType: ScheduleType.daily.value,
          timeHour: 8,
          timeMinute: 0,
          startDate: DateTime(2026, 1, 1),
        ),
      );

      expect(scheduleId, isPositive);

      // Verify records exist
      var meds = await db.medicineDao.getActiveMedicines(p1);
      expect(meds.length, equals(1));

      // Delete profile
      await db.profileDao.deleteProfile(p1);

      // Verify cascade deletion
      meds = await db.medicineDao.getActiveMedicines(p1);
      expect(meds, isEmpty);
      final schedules = await db.scheduleDao.getActiveSchedulesForProfile(p1);
      expect(schedules, isEmpty);
    });
  });

  group('Medicine & Inventory Tracking Tests', () {
    test('Taking dose decrements medicine inventory stock', () async {
      final p1 = await db.profileDao.createProfile(name: 'Sarah', avatarColor: 0xFF2B6CB0, avatarIcon: 'person');
      final medId = await db.medicineDao.insertMedicine(
        MedicinesCompanion.insert(
          profileId: p1,
          name: 'Metformin',
          dosageAmount: const Value(500.0),
          dosageUnit: const Value('mg'),
          stockCount: const Value(30),
        ),
      );

      final schedId = await db.scheduleDao.insertSchedule(
        SchedulesCompanion.insert(
          profileId: p1,
          medicineId: medId,
          scheduleType: ScheduleType.daily.value,
          timeHour: 9,
          timeMinute: 0,
          startDate: DateTime(2026, 1, 1),
        ),
      );

      await doseEngine.generateRollingDoses(startDate: DateTime(2026, 1, 1), daysAhead: 1);
      final doses = await db.doseRecordDao.getDosesForProfileRange(
        p1,
        DateTime(2026, 1, 1, 0, 0),
        DateTime(2026, 1, 1, 23, 59),
      );

      expect(doses.length, equals(1));
      final doseId = doses.first.dose.id;

      // Take the dose
      await doseEngine.takeDose(doseId);

      final updatedDose = await db.doseRecordDao.getDoseWithMedicine(doseId);
      expect(updatedDose!.dose.status, equals(DoseStatus.taken.value));
      expect(updatedDose.dose.takenAt, isNotNull);

      final updatedMed = await db.medicineDao.getMedicineById(medId);
      expect(updatedMed!.stockCount, equals(29)); // Decremented by 1
    });

    test('Soft-delete archiving preserves medicine history', () async {
      final p1 = await db.profileDao.createProfile(name: 'Sarah', avatarColor: 0xFF2B6CB0, avatarIcon: 'person');
      final medId = await db.medicineDao.insertMedicine(
        MedicinesCompanion.insert(
          profileId: p1,
          name: 'Ibuprofen',
          dosageAmount: const Value(200.0),
          dosageUnit: const Value('mg'),
        ),
      );

      await db.medicineDao.archiveMedicine(medId);

      final activeMeds = await db.medicineDao.getActiveMedicines(p1);
      expect(activeMeds, isEmpty);

      final archivedMeds = await db.medicineDao.watchArchivedMedicines(p1).first;
      expect(archivedMeds.length, equals(1));
      expect(archivedMeds.first.name, equals('Ibuprofen'));
    });
  });

  group('Dose State Machine & Snooze Invariant Tests', () {
    test('Snoozing advances reminderAt, increments count, BUT strictly preserves grace_period_deadline', () async {
      final p1 = await db.profileDao.createProfile(
        name: 'Sarah',
        avatarColor: 0xFF2B6CB0,
        avatarIcon: 'person',
        gracePeriodMinutes: 60,
        defaultSnoozeMinutes: 15,
      );

      final medId = await db.medicineDao.insertMedicine(
        MedicinesCompanion.insert(profileId: p1, name: 'Thyroxine'),
      );

      await db.scheduleDao.insertSchedule(
        SchedulesCompanion.insert(
          profileId: p1,
          medicineId: medId,
          scheduleType: ScheduleType.daily.value,
          timeHour: 8,
          timeMinute: 0,
          startDate: DateTime(2026, 1, 1),
        ),
      );

      await doseEngine.generateRollingDoses(startDate: DateTime(2026, 1, 1), daysAhead: 1);
      final doses = await db.doseRecordDao.getDosesForProfileRange(
        p1,
        DateTime(2026, 1, 1, 0, 0),
        DateTime(2026, 1, 1, 23, 59),
      );

      final originalDose = doses.first.dose;
      final originalGraceDeadline = originalDose.gracePeriodDeadline;
      expect(originalGraceDeadline, equals(DateTime(2026, 1, 1, 9, 0))); // 8:00 + 60m

      // Snooze dose
      await doseEngine.snoozeDose(originalDose.id, snoozeMinutes: 15);

      final snoozedDose = (await db.doseRecordDao.getDoseWithMedicine(originalDose.id))!.dose;
      expect(snoozedDose.status, equals(DoseStatus.pending.value)); // Requirement: keeps pending
      expect(snoozedDose.snoozeCount, equals(1));
      // CRITICAL INVARIANT: grace deadline must not change
      expect(snoozedDose.gracePeriodDeadline, equals(originalGraceDeadline));
    });

    test('Unresolved dose is automatically marked as missed after grace period expires', () async {
      final p1 = await db.profileDao.createProfile(
        name: 'Sarah',
        avatarColor: 0xFF2B6CB0,
        avatarIcon: 'person',
        gracePeriodMinutes: 60,
      );

      final medId = await db.medicineDao.insertMedicine(
        MedicinesCompanion.insert(profileId: p1, name: 'Vitamin D'),
      );

      await db.scheduleDao.insertSchedule(
        SchedulesCompanion.insert(
          profileId: p1,
          medicineId: medId,
          scheduleType: ScheduleType.daily.value,
          timeHour: 8,
          timeMinute: 0,
          startDate: DateTime(2026, 1, 1),
        ),
      );

      await doseEngine.generateRollingDoses(startDate: DateTime(2026, 1, 1), daysAhead: 1);
      final dose = (await db.doseRecordDao.getDosesForProfileRange(
        p1,
        DateTime(2026, 1, 1, 0, 0),
        DateTime(2026, 1, 1, 23, 59),
      )).first.dose;

      // Cutoff before deadline (e.g. 8:30)
      int missed = await doseEngine.evaluateMissedDoses(currentTime: DateTime(2026, 1, 1, 8, 30));
      expect(missed, equals(0));

      // Cutoff after deadline (e.g. 9:01, past 60 min grace)
      missed = await doseEngine.evaluateMissedDoses(currentTime: DateTime(2026, 1, 1, 9, 1));
      expect(missed, equals(1));

      final updated = (await db.doseRecordDao.getDoseWithMedicine(dose.id))!.dose;
      expect(updated.status, equals(DoseStatus.missed.value));
    });

    test('User can correct a mistakenly recorded missed dose', () async {
      final p1 = await db.profileDao.createProfile(name: 'Sarah', avatarColor: 0xFF2B6CB0, avatarIcon: 'person');
      final medId = await db.medicineDao.insertMedicine(
        MedicinesCompanion.insert(profileId: p1, name: 'Lisinopril', stockCount: const Value(10)),
      );

      await db.scheduleDao.insertSchedule(
        SchedulesCompanion.insert(
          profileId: p1,
          medicineId: medId,
          scheduleType: ScheduleType.daily.value,
          timeHour: 8,
          timeMinute: 0,
          startDate: DateTime(2026, 1, 1),
        ),
      );

      await doseEngine.generateRollingDoses(startDate: DateTime(2026, 1, 1), daysAhead: 1);
      final dose = (await db.doseRecordDao.getDosesForProfileRange(
        p1,
        DateTime(2026, 1, 1, 0, 0),
        DateTime(2026, 1, 1, 23, 59),
      )).first.dose;

      // Transition to missed
      await doseEngine.evaluateMissedDoses(currentTime: DateTime(2026, 1, 1, 10, 0));

      // Correct to taken
      final realTime = DateTime(2026, 1, 1, 8, 15);
      await doseEngine.correctMissedDose(dose.id, actualTakenAt: realTime);

      final corrected = (await db.doseRecordDao.getDoseWithMedicine(dose.id))!.dose;
      expect(corrected.status, equals(DoseStatus.taken.value));
      expect(corrected.takenAt, equals(realTime));
    });
  });

  group('Skip Functionality & Adherence Math Tests', () {
    test('Skipping a dose records reason and excludes it from adherence penalty', () async {
      final p1 = await db.profileDao.createProfile(name: 'Sarah', avatarColor: 0xFF2B6CB0, avatarIcon: 'person');
      final medId = await db.medicineDao.insertMedicine(
        MedicinesCompanion.insert(profileId: p1, name: 'Aspirin'),
      );

      // Create 3 doses
      await db.scheduleDao.insertSchedule(
        SchedulesCompanion.insert(
          profileId: p1,
          medicineId: medId,
          scheduleType: ScheduleType.daily.value,
          timeHour: 8,
          timeMinute: 0,
          startDate: DateTime(2026, 1, 1),
          endDate: Value(DateTime(2026, 1, 3)),
        ),
      );

      await doseEngine.generateRollingDoses(startDate: DateTime(2026, 1, 1), daysAhead: 3);
      final doses = await db.doseRecordDao.getDoseHistoryForProfile(p1);
      expect(doses.length, equals(3));

      // Dose 1: Taken
      await doseEngine.takeDose(doses[0].id);
      // Dose 2: Skipped (e.g. fasting before lab test)
      await doseEngine.skipDose(doses[1].id, reason: 'Fasting for blood work');
      // Dose 3: Missed
      await doseEngine.evaluateMissedDoses(currentTime: DateTime(2026, 1, 10));

      final stats = await adherenceService.getAdherenceStats(p1);
      expect(stats.totalScheduled, equals(3));
      expect(stats.takenCount, equals(1));
      expect(stats.skippedCount, equals(1));
      expect(stats.missedCount, equals(1));

      // Formula: 1 taken / (1 taken + 1 missed) = 50.0%
      // Skipped is NOT in denominator, avoiding unfair penalty!
      expect(stats.adherenceRate, equals(50.0));
    });
  });
}
