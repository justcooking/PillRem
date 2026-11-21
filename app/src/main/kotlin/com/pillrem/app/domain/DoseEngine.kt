package com.pillrem.app.domain

import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.DoseRecordEntity
import com.pillrem.app.data.local.entities.ScheduleEntity
import com.pillrem.app.data.models.DoseStatus
import com.pillrem.app.data.models.ScheduleType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DoseEngine(private val db: AppDatabase) {

    /**
     * Pre-generates dose records for the next [daysAhead] days for all active schedules.
     * Idempotent: existing doses are ignored by Room thanks to unique index (scheduleId, scheduledAt).
     */
    suspend fun generateRollingDoses(daysAhead: Int = 7): Int {
        val today = LocalDate.now()
        val activeSchedules = db.scheduleDao().getAllActiveSchedulesSync()
        var generatedCount = 0

        for (schedule in activeSchedules) {
            val settings = db.profileDao().getSettingsForProfileSync(schedule.profileId)
            val graceMinutes = settings?.gracePeriodMinutes ?: 60

            val scheduleStartDate = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(schedule.startDate),
                ZoneId.systemDefault()
            ).toLocalDate()

            val scheduleEndDate = schedule.endDate?.let {
                LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(it), ZoneId.systemDefault()).toLocalDate()
            }

            for (i in 0 until daysAhead) {
                val targetDate = today.plusDays(i.toLong())

                if (targetDate.isBefore(scheduleStartDate)) continue
                if (scheduleEndDate != null && targetDate.isAfter(scheduleEndDate)) continue

                if (!_matchesSchedule(schedule, targetDate, scheduleStartDate)) continue

                val scheduledDateTime = targetDate.atTime(schedule.timeHour, schedule.timeMinute)
                val scheduledEpoch = scheduledDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val graceDeadline = scheduledDateTime.plusMinutes(graceMinutes.toLong())
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

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

    private fun _matchesSchedule(
        schedule: ScheduleEntity,
        targetDate: LocalDate,
        startDate: LocalDate
    ): Boolean {
        return when (ScheduleType.fromString(schedule.scheduleType)) {
            ScheduleType.DAILY -> true
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
            ScheduleType.AS_NEEDED -> false
        }
    }

    /**
     * Mark Dose Taken
     */
    suspend fun takeDose(doseId: Long, takenAt: Long = System.currentTimeMillis()) {
        val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return
        if (doseWithMed.dose.status == DoseStatus.TAKEN.value) return

        db.doseRecordDao().markDoseTaken(
            doseId = doseId,
            status = DoseStatus.TAKEN.value,
            takenAt = takenAt
        )

        if (doseWithMed.medicine.stockCount > 0) {
            db.medicineDao().decrementStock(doseWithMed.medicine.id, 1)
        }
    }

    /**
     * Snooze Dose
     * Strict Invariant: Preserves original gracePeriodDeadline, advances reminderAt, keeps status PENDING
     */
    suspend fun snoozeDose(doseId: Long, snoozeMinutes: Int? = null) {
        val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return
        if (doseWithMed.dose.status == DoseStatus.TAKEN.value) return

        val profileSettings = db.profileDao().getSettingsForProfileSync(doseWithMed.dose.profileId)
        val durationMinutes = snoozeMinutes ?: profileSettings?.defaultSnoozeMinutes ?: 15

        val newReminderAt = System.currentTimeMillis() + (durationMinutes * 60 * 1000L)

        db.doseRecordDao().snoozeDose(
            doseId = doseId,
            status = DoseStatus.PENDING.value,
            reminderAt = newReminderAt
        )
    }

    /**
     * Skip Dose
     */
    suspend fun skipDose(doseId: Long, reason: String? = null, skippedAt: Long = System.currentTimeMillis()) {
        val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return
        if (doseWithMed.dose.status == DoseStatus.TAKEN.value) return

        db.doseRecordDao().skipDose(
            doseId = doseId,
            status = DoseStatus.SKIPPED.value,
            skippedAt = skippedAt,
            skipReason = reason
        )
    }

    /**
     * Auto-mark expired doses as missed
     */
    suspend fun autoMarkMissedDoses(cutoffTime: Long = System.currentTimeMillis()): Int {
        return db.doseRecordDao().autoMarkMissedDoses(cutoffTime)
    }

    /**
     * Correct mistakenly recorded missed dose back to taken
     */
    suspend fun correctMissedDose(doseId: Long, actualTime: Long? = null) {
        val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId) ?: return
        val confirmedTime = actualTime ?: doseWithMed.dose.scheduledAt

        db.doseRecordDao().markDoseTaken(
            doseId = doseId,
            status = DoseStatus.TAKEN.value,
            takenAt = confirmedTime
        )

        if (doseWithMed.medicine.stockCount > 0) {
            db.medicineDao().decrementStock(doseWithMed.medicine.id, 1)
        }
    }

    companion object {
        fun generateNotificationId(scheduleId: Long, scheduledEpoch: Long): Int {
            return ((scheduleId * 31 + scheduledEpoch / 60000) and 0x7FFFFFFF).toInt()
        }
    }
}
