package com.pillrem.app

import android.app.Application
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.MedicineEntity
import com.pillrem.app.data.local.entities.ScheduleEntity
import com.pillrem.app.data.models.ScheduleType
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.services.NotificationHelper
import com.pillrem.app.workers.AutoMissedWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PillRemApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
        AutoMissedWorker.schedulePeriodic(this)

        val db = AppDatabase.getInstance(this)
        val doseEngine = DoseEngine(db)

        CoroutineScope(Dispatchers.IO).launch {
            val profiles = db.profileDao().getAllProfilesSync()
            if (profiles.isEmpty()) {
                seedInitialData(db, doseEngine)
            }
            // Shared generation and alarm reconciliation on app start
            doseEngine.reconcileAndScheduleAlarms(this@PillRemApplication)
        }
    }

    private suspend fun seedInitialData(db: AppDatabase, doseEngine: DoseEngine) {
        val sarahId = db.profileDao().createProfileWithSettings(
            name = "Sarah Connor",
            avatarColor = 0xFF2B6CB0.toInt(),
            avatarIcon = "person",
            gracePeriodMinutes = 60,
            defaultSnoozeMinutes = 15
        )

        val metId = db.medicineDao().insertMedicine(
            MedicineEntity(
                profileId = sarahId,
                name = "Metformin",
                dosageAmount = 500.0,
                dosageUnit = "mg",
                form = "tablet",
                instructions = "With dinner",
                stockCount = 28
            )
        )

        val lisId = db.medicineDao().insertMedicine(
            MedicineEntity(
                profileId = sarahId,
                name = "Lisinopril",
                dosageAmount = 10.0,
                dosageUnit = "mg",
                form = "tablet",
                instructions = "In the morning",
                stockCount = 14
            )
        )

        val now = System.currentTimeMillis()
        db.scheduleDao().insertSchedule(
            ScheduleEntity(
                medicineId = lisId,
                profileId = sarahId,
                scheduleType = ScheduleType.DAILY.value,
                timeHour = 8,
                timeMinute = 0,
                startDate = now
            )
        )

        db.scheduleDao().insertSchedule(
            ScheduleEntity(
                medicineId = metId,
                profileId = sarahId,
                scheduleType = ScheduleType.DAILY.value,
                timeHour = 20,
                timeMinute = 0,
                startDate = now
            )
        )

        // Grandpa Joe profile
        db.profileDao().createProfileWithSettings(
            name = "Grandpa Joe",
            avatarColor = 0xFF388E67.toInt(),
            avatarIcon = "face",
            gracePeriodMinutes = 90,
            defaultSnoozeMinutes = 20
        )

        doseEngine.generateRollingDoses(daysAhead = 7)
    }
}
