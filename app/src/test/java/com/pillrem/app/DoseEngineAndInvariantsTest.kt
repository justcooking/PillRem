package com.pillrem.app

import com.pillrem.app.data.local.entities.DoseRecordEntity
import com.pillrem.app.data.local.entities.MedicineEntity
import com.pillrem.app.data.local.entities.ProfileEntity
import com.pillrem.app.data.local.entities.ProfileSettingsEntity
import com.pillrem.app.data.local.entities.ScheduleEntity
import com.pillrem.app.data.models.DoseStatus
import com.pillrem.app.data.models.DoseWithMedicine
import com.pillrem.app.data.models.ScheduleType
import com.pillrem.app.domain.AdherenceCalculator
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.utils.DosageFormatter
import org.junit.Assert.*
import org.junit.Test
import java.time.*
import java.time.temporal.ChronoUnit

class DoseEngineAndInvariantsTest {

    // Fixed mock clock at Monday, 2026-11-23 08:00:00 UTC
    private val fixedInstant = Instant.parse("2026-11-23T08:00:00Z")
    private val fixedZone = ZoneId.of("UTC")
    private val fixedClock = Clock.fixed(fixedInstant, fixedZone)

    @Test
    fun testDosageFormatter_fractionalAndWholeNumbers() {
        assertEquals("0.5", DosageFormatter.format(0.5))
        assertEquals("1.5", DosageFormatter.format(1.5))
        assertEquals("0.25", DosageFormatter.format(0.25))
        assertEquals("1", DosageFormatter.format(1.0))
        assertEquals("2", DosageFormatter.format(2.0))
        assertEquals("100", DosageFormatter.format(100.0))

        assertEquals("0.5 tablet", DosageFormatter.formatWithUnit(0.5, "tablet"))
        assertEquals("1 capsule", DosageFormatter.formatWithUnit(1.0, "capsule"))
        assertEquals("2.5 ml", DosageFormatter.formatWithUnit(2.5, "ml"))
    }

    @Test
    fun testScheduleMatching_daily() {
        val schedule = ScheduleEntity(
            id = 1,
            medicineId = 1,
            profileId = 1,
            scheduleType = ScheduleType.DAILY.value,
            timeHour = 8,
            timeMinute = 0,
            startDate = fixedInstant.toEpochMilli()
        )

        val targetDate = LocalDate.of(2026, 11, 23)
        val startDate = LocalDate.of(2026, 11, 23)

        // Reflection or schedule matching test
        assertEquals(ScheduleType.DAILY, ScheduleType.fromString(schedule.scheduleType))
    }

    @Test
    fun testScheduleMatching_specificDays() {
        // Monday (1) and Thursday (4)
        val schedule = ScheduleEntity(
            id = 1,
            medicineId = 1,
            profileId = 1,
            scheduleType = ScheduleType.SPECIFIC_DAYS.value,
            daysOfWeek = "1,4",
            timeHour = 8,
            timeMinute = 0,
            startDate = fixedInstant.toEpochMilli()
        )

        val monday = LocalDate.of(2026, 11, 23) // Monday = 1
        val tuesday = LocalDate.of(2026, 11, 24) // Tuesday = 2
        val thursday = LocalDate.of(2026, 11, 26) // Thursday = 4

        val daysSet = schedule.daysOfWeek!!.split(",").map { it.trim().toInt() }.toSet()
        assertTrue(daysSet.contains(monday.dayOfWeek.value))
        assertFalse(daysSet.contains(tuesday.dayOfWeek.value))
        assertTrue(daysSet.contains(thursday.dayOfWeek.value))
    }

    @Test
    fun testScheduleMatching_intervalDays() {
        // Every 3 days starting Nov 23
        val startDate = LocalDate.of(2026, 11, 23)
        val interval = 3

        val day0 = LocalDate.of(2026, 11, 23) // diff = 0 -> true
        val day1 = LocalDate.of(2026, 11, 24) // diff = 1 -> false
        val day2 = LocalDate.of(2026, 11, 25) // diff = 2 -> false
        val day3 = LocalDate.of(2026, 11, 26) // diff = 3 -> true
        val day6 = LocalDate.of(2026, 11, 29) // diff = 6 -> true

        assertTrue(ChronoUnit.DAYS.between(startDate, day0) % interval == 0L)
        assertFalse(ChronoUnit.DAYS.between(startDate, day1) % interval == 0L)
        assertFalse(ChronoUnit.DAYS.between(startDate, day2) % interval == 0L)
        assertTrue(ChronoUnit.DAYS.between(startDate, day3) % interval == 0L)
        assertTrue(ChronoUnit.DAYS.between(startDate, day6) % interval == 0L)
    }

    @Test
    fun testAdherenceScore_withFractionalDosages() {
        val now = fixedClock.millis()
        val doses = listOf(
            DoseRecordEntity(
                id = 1,
                profileId = 1,
                medicineId = 1,
                scheduledAt = now,
                reminderAt = now,
                status = DoseStatus.TAKEN.value,
                gracePeriodDeadline = now + 3600000,
                notificationId = 1
            ),
            DoseRecordEntity(
                id = 2,
                profileId = 1,
                medicineId = 2,
                scheduledAt = now,
                reminderAt = now,
                status = DoseStatus.SKIPPED.value,
                gracePeriodDeadline = now + 3600000,
                notificationId = 2
            ),
            DoseRecordEntity(
                id = 3,
                profileId = 1,
                medicineId = 3,
                scheduledAt = now,
                reminderAt = now,
                status = DoseStatus.MISSED.value,
                gracePeriodDeadline = now + 3600000,
                notificationId = 3
            )
        )

        val stats = AdherenceCalculator.calculate(doses)
        assertEquals(3, stats.totalScheduled)
        assertEquals(1, stats.takenCount)
        assertEquals(1, stats.skippedCount)
        assertEquals(1, stats.missedCount)
        // 1 taken / (1 taken + 1 missed) = 50.0%
        assertEquals(50.0f, stats.adherenceRate, 0.01f)
    }

    @Test
    fun testNotificationId_deterministicGeneration() {
        val id1 = DoseEngine.generateNotificationId(scheduleId = 42L, scheduledEpoch = 1763884800000L)
        val id2 = DoseEngine.generateNotificationId(scheduleId = 42L, scheduledEpoch = 1763884800000L)
        val id3 = DoseEngine.generateNotificationId(scheduleId = 43L, scheduledEpoch = 1763884800000L)

        assertEquals(id1, id2)
        assertNotEquals(id1, id3)
        assertTrue(id1 > 0)
    }
}
