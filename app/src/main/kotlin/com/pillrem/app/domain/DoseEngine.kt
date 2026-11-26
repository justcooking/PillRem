package com.pillrem.app.domain

import android.content.Context
import androidx.room.withTransaction
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.DoseRecordEntity
import com.pillrem.app.data.local.entities.ScheduleEntity
import com.pillrem.app.data.models.DoseStatus
import com.pillrem.app.data.models.ScheduleType
import com.pillrem.app.services.AlarmScheduler
import com.pillrem.app.utils.DosageFormatter
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DoseEngine(
    private val db: AppDatabase,
    private val clock: Clock = Clock.systemDefaultZone()
) {

    /**
     * Pre-generates dose records for the next [daysAhead] days for all active schedules.
     * Idempotent: existing doses are ignored by Room thanks to unique index (scheduleId, scheduledAt).
     */
    suspend fun generateRollingDoses(daysAhead: Int = 7): Int {
        val today = LocalDate.now(clock)
        val zoneId = clock.zone
        val activeSchedules = db.scheduleDao().getAllActiveSchedulesSync()
        var generatedCount = 0

        for (schedule in activeSchedules) {
            val settings = db.profileDao().getSettingsForProfileSync(schedule.profileId)
            val graceMinutes = settings?.gracePeriodMinutes ?: 60

            val scheduleStartDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(schedule.startDate),
                zoneId
            ).toLocalDate()

            val scheduleEndDate = schedule.endDate?.let {
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it), zoneId).toLocalDate()
            }

            for (i in 0 until daysAhead) {
                val targetDate = today.plusDays(i.toLong())

                if (targetDate.isBefore(scheduleStartDate)) continue
                if (scheduleEndDate != null && targetDate.isAfter(scheduleEndDate)) continue

                if (!_matchesSchedule(schedule, targetDate, scheduleStartDate)) continue

                val scheduledDateTime = targetDate.atTime(schedule.timeHour, schedule.timeMinute)
                val scheduledEpoch = scheduledDateTime.atZone(zoneId).toInstant().toEpochMilli()
                val graceDeadline = scheduledDateTime.plusMinutes(graceMinutes.toLong())
                    .atZone(zoneId).toInstant().toEpochMilli()

                val notificationId = generateNotificationId(schedule.id, scheduledEpoch)

                val dose = DoseRecordEntity(
                    profileId = schedule.profileId,
                    medicineId = schedule.medicineId,
                    scheduleId = schedule.id,
                    scheduledAt = scheduledEpoch,
                    reminderAt = scheduledEpoch,
                    status = DoseStatus.PENDING.value,
                    gracePeriodDeadline = graceDeadline,
                    notificationId = notificationId
                )

                val insertedId = db.doseRecordDao().insertDoseIdempotent(dose)
                if (insertedId > 0) {
                    generatedCount++
                }
            }
        }

        return generatedCount
    }

    /**
     * Reconciles the 7-day rolling dose records and schedules exact alarms
     * for all upcoming active pending doses.
     * This is the single shared reconciliation path called on:
     * - Medicine save / update
     * - App startup
     * - System boot / timezone change
     * - Periodic AutoMissedWorker replenishment
     * - Backup restore
     */
    suspend fun reconcileAndScheduleAlarms(context: Context): Int {
        // 1. Replenish the 7-day rolling window
        generateRollingDoses(daysAhead = 7)

        val now = clock.millis()
        // 2. Query all upcoming active pending doses (archived medicines excluded)
        val upcomingDoses = db.doseRecordDao().getUpcomingActivePendingDoses(now)

        val allProfiles = db.profileDao().getAllProfilesSync().associateBy { it.id }
        val profileSettings = db.profileDao().getAllSettingsSync().associateBy { it.profileId }

        var scheduledCount = 0
        for (item in upcomingDoses) {
            val profile = allProfiles[item.dose.profileId]
            val settings = profileSettings[item.dose.profileId]
            val snoozeMinutes = settings?.defaultSnoozeMinutes ?: 15

            AlarmScheduler.scheduleExactAlarm(
                context = context,
                triggerEpochMs = item.dose.reminderAt,
                notificationId = item.dose.notificationId,
                doseId = item.dose.id,
                profileName = profile?.name ?: "Patient",
                medicineName = item.medicine.name,
                dosageInfo = DosageFormatter.formatWithUnit(
                    item.medicine.dosageAmount,
                    item.medicine.dosageUnit
                ),
                snoozeMinutes = snoozeMinutes
            )
            scheduledCount++
        }
        return scheduledCount
    }

    private fun _matchesSchedule(
        schedule: ScheduleEntity,
        targetDate: LocalDate,
        startDate: LocalDate
    ): Boolean {
        return when (ScheduleType.fromString(schedule.scheduleType)) {
            ScheduleType.DAILY -> {
                val interval = schedule.intervalDays ?: 1
                if (interval <= 1) true
                else {
                    val daysDiff = ChronoUnit.DAYS.between(startDate, targetDate)
                    daysDiff >= 0 && (daysDiff % interval == 0L)
                }
            }
            ScheduleType.SPECIFIC_DAYS -> {
                if (schedule.daysOfWeek.isNullOrBlank()) return false
                val days = schedule.daysOfWeek.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
                // DayOfWeek.value: 1=Monday .. 7=Sunday
                days.contains(targetDate.dayOfWeek.value)
            }
            ScheduleType.INTERVAL_DAYS -> {
                val interval = schedule.intervalDays ?: 1
                if (interval <= 1) return true
                val daysDiff = ChronoUnit.DAYS.between(startDate, targetDate)
                daysDiff >= 0 && (daysDiff % interval == 0L)
            }
            ScheduleType.WEEKLY -> {
                val intervalWeeks = schedule.intervalDays ?: 1
                val weeksDiff = ChronoUnit.WEEKS.between(startDate, targetDate)
                if (weeksDiff < 0 || (weeksDiff % intervalWeeks != 0L)) return false
                if (!schedule.daysOfWeek.isNullOrBlank()) {
                    val days = schedule.daysOfWeek.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
                    days.contains(targetDate.dayOfWeek.value)
                } else {
                    targetDate.dayOfWeek == startDate.dayOfWeek
                }
            }
            ScheduleType.MONTHLY -> {
                val intervalMonths = schedule.intervalDays ?: 1
                val monthsDiff = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), targetDate.withDayOfMonth(1))
                if (monthsDiff < 0 || (monthsDiff % intervalMonths != 0L)) return false
                val targetDayOfMonth = startDate.dayOfMonth.coerceAtMost(targetDate.lengthOfMonth())
                targetDate.dayOfMonth == targetDayOfMonth
            }
            ScheduleType.AS_NEEDED -> false
        }
    }

    /**
     * Mark Dose Taken (Transactional and concurrency-guarded).
     * Prevents double decrement of stock or re-taking an already taken dose.
     */
    suspend fun takeDose(doseId: Long, takenAt: Long = clock.millis()): Boolean {
        return db.withTransaction {
            val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return@withTransaction false
            if (doseWithMed.dose.status == DoseStatus.TAKEN.value) {
                return@withTransaction false
            }

            db.doseRecordDao().markDoseTaken(
                doseId = doseId,
                status = DoseStatus.TAKEN.value,
                takenAt = takenAt
            )
            true
        }
    }

    /**
     * Snooze Dose
     * Strict Invariant: Preserves original gracePeriodDeadline, advances reminderAt, keeps status PENDING.
     * Guard: If the new reminder exceeds the grace deadline, auto-marks missed.
     */
    suspend fun snoozeDose(doseId: Long, snoozeMinutes: Int? = null): Boolean {
        return db.withTransaction {
            val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return@withTransaction false
            if (doseWithMed.dose.status == DoseStatus.TAKEN.value || doseWithMed.dose.status == DoseStatus.SKIPPED.value) {
                return@withTransaction false
            }

            val profileSettings = db.profileDao().getSettingsForProfileSync(doseWithMed.dose.profileId)
            val durationMinutes = snoozeMinutes ?: profileSettings?.defaultSnoozeMinutes ?: 15

            val now = clock.millis()
            val newReminderAt = now + (durationMinutes * 60 * 1000L)

            // Strict invariant: Snooze cannot push past gracePeriodDeadline
            if (newReminderAt >= doseWithMed.dose.gracePeriodDeadline) {
                db.doseRecordDao().autoMarkMissedDoses(doseWithMed.dose.gracePeriodDeadline)
                return@withTransaction false
            }

            db.doseRecordDao().snoozeDose(
                doseId = doseId,
                status = DoseStatus.PENDING.value,
                reminderAt = newReminderAt
            )
            true
        }
    }

    /**
     * Skip Dose
     */
    suspend fun skipDose(doseId: Long, reason: String? = null, skippedAt: Long = clock.millis()): Boolean {
        return db.withTransaction {
            val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return@withTransaction false
            if (doseWithMed.dose.status == DoseStatus.TAKEN.value) return@withTransaction false

            db.doseRecordDao().skipDose(
                doseId = doseId,
                status = DoseStatus.SKIPPED.value,
                skippedAt = skippedAt,
                skipReason = reason
            )
            true
        }
    }

    /**
     * Auto-mark expired doses as missed
     */
    suspend fun autoMarkMissedDoses(cutoffTime: Long = clock.millis()): Int {
        return db.doseRecordDao().autoMarkMissedDoses(cutoffTime)
    }

    /**
     * Correct mistakenly recorded missed dose back to taken.
     * Strict Guard: ONLY missed doses can be corrected to taken!
     */
    suspend fun correctMissedDose(doseId: Long, actualTime: Long? = null): Boolean {
        return db.withTransaction {
            val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return@withTransaction false
            if (doseWithMed.dose.status != DoseStatus.MISSED.value) {
                return@withTransaction false
            }

            val confirmedTime = actualTime ?: doseWithMed.dose.scheduledAt

            db.doseRecordDao().markDoseTaken(
                doseId = doseId,
                status = DoseStatus.TAKEN.value,
                takenAt = confirmedTime
            )
            true
        }
    }

    /**
     * Archive medicine: Cancels all future alarms, deletes future pending records, and sets isArchived=1.
     */
    suspend fun archiveMedicine(context: Context, medicineId: Long) {
        db.withTransaction {
            db.medicineDao().archiveMedicine(medicineId)
            val now = clock.millis()
            val pendingDoses = db.doseRecordDao().getFuturePendingDosesForMedicine(medicineId, now)
            for (dose in pendingDoses) {
                AlarmScheduler.cancelAlarm(context, dose.notificationId)
            }
            db.doseRecordDao().deleteFuturePendingDosesForMedicine(medicineId, now)
        }
    }

    /**
     * Unarchive medicine: Restores medicine and reconciles rolling doses and alarms.
     */
    suspend fun unarchiveMedicine(context: Context, medicineId: Long) {
        db.medicineDao().restoreMedicine(medicineId)
        reconcileAndScheduleAlarms(context)
    }

    companion object {
        fun generateNotificationId(scheduleId: Long, scheduledEpoch: Long): Int {
            return ((scheduleId * 31 + scheduledEpoch / 60000) and 0x7FFFFFFF).toInt()
        }
    }
}
