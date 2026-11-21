package com.pillrem.app

import com.pillrem.app.data.local.entities.DoseRecordEntity
import com.pillrem.app.data.models.DoseStatus
import com.pillrem.app.data.models.ScheduleType
import com.pillrem.app.domain.AdherenceCalculator
import org.junit.Assert.*
import org.junit.Test

class AdherenceAndDomainTest {

    @Test
    fun testDoseStatus_resolution() {
        assertTrue(DoseStatus.TAKEN.isResolved)
        assertTrue(DoseStatus.MISSED.isResolved)
        assertTrue(DoseStatus.SKIPPED.isResolved)
        assertFalse(DoseStatus.PENDING.isResolved)
        assertFalse(DoseStatus.SNOOZED.isResolved)
    }

    @Test
    fun testScheduleType_fromString() {
        assertEquals(ScheduleType.DAILY, ScheduleType.fromString("daily"))
        assertEquals(ScheduleType.SPECIFIC_DAYS, ScheduleType.fromString("specific_days"))
        assertEquals(ScheduleType.INTERVAL_DAYS, ScheduleType.fromString("interval_days"))
        assertEquals(ScheduleType.AS_NEEDED, ScheduleType.fromString("as_needed"))
    }

    @Test
    fun testAdherence_calculation_excludesSkippedDoses() {
        val now = System.currentTimeMillis()
        val doses = listOf(
            DoseRecordEntity(
                id = 1,
                profileId = 1,
                medicineId = 1,
                scheduledAt = now,
                reminderAt = now,
                status = DoseStatus.TAKEN.value,
                gracePeriodDeadline = now + 3600000,
                notificationId = 101
            ),
            DoseRecordEntity(
                id = 2,
                profileId = 1,
                medicineId = 1,
                scheduledAt = now + 1000,
                reminderAt = now + 1000,
                status = DoseStatus.SKIPPED.value,
                gracePeriodDeadline = now + 3600000,
                notificationId = 102
            ),
            DoseRecordEntity(
                id = 3,
                profileId = 1,
                medicineId = 1,
                scheduledAt = now + 2000,
                reminderAt = now + 2000,
                status = DoseStatus.MISSED.value,
                gracePeriodDeadline = now + 3600000,
                notificationId = 103
            )
        )

        val stats = AdherenceCalculator.calculate(doses)
        assertEquals(3, stats.totalScheduled)
        assertEquals(1, stats.takenCount)
        assertEquals(1, stats.skippedCount)
        assertEquals(1, stats.missedCount)

        // Adherence Rate = 1 taken / (1 taken + 1 missed) = 50.0%
        // Skipped doses are NOT penalized!
        assertEquals(50.0f, stats.adherenceRate, 0.01f)
    }

    @Test
    fun testAdherence_perfectScoreWhenAllTaken() {
        val now = System.currentTimeMillis()
        val doses = listOf(
            DoseRecordEntity(
                id = 1,
                profileId = 1,
                medicineId = 1,
                scheduledAt = now,
                reminderAt = now,
                status = DoseStatus.TAKEN.value,
                gracePeriodDeadline = now + 3600000,
                notificationId = 101
            )
        )

        val stats = AdherenceCalculator.calculate(doses)
        assertEquals(100.0f, stats.adherenceRate, 0.01f)
    }

    @Test
    fun testBackupData_jsonSerializationAndDeserialization() {
        val gson = com.google.gson.Gson()
        val backup = com.pillrem.app.services.PillRemBackupData(
            version = 1,
            exportedAt = 123456789L,
            profiles = listOf(
                com.pillrem.app.data.local.entities.ProfileEntity(id = 1, name = "Sarah Connor")
            ),
            profileSettings = listOf(
                com.pillrem.app.data.local.entities.ProfileSettingsEntity(profileId = 1, gracePeriodMinutes = 60)
            ),
            medicines = listOf(
                com.pillrem.app.data.local.entities.MedicineEntity(id = 1, profileId = 1, name = "Metformin", stockCount = 28)
            ),
            schedules = emptyList(),
            doseRecords = emptyList()
        )

        val json = gson.toJson(backup)
        assertNotNull(json)
        assertTrue(json.contains("Sarah Connor"))
        assertTrue(json.contains("Metformin"))

        val restored = gson.fromJson(json, com.pillrem.app.services.PillRemBackupData::class.java)
        assertEquals(backup.version, restored.version)
        assertEquals(1, restored.profiles.size)
        assertEquals("Sarah Connor", restored.profiles[0].name)
        assertEquals(1, restored.medicines.size)
        assertEquals("Metformin", restored.medicines[0].name)
    }
}
