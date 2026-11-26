package com.pillrem.app

import android.app.Application
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.services.NotificationHelper
import com.pillrem.app.workers.AutoMissedWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PillRemApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        try {
            NotificationHelper.createNotificationChannel(this)
            AutoMissedWorker.schedulePeriodic(this)
            com.pillrem.app.utils.LocaleHelper.init(this)

            val db = AppDatabase.getInstance(this)
            val doseEngine = DoseEngine(db)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Reconcile and schedule alarms on app start for existing profiles
                    val profiles = db.profileDao().getAllProfilesSync()
                    if (profiles.isNotEmpty()) {
                        doseEngine.reconcileAndScheduleAlarms(this@PillRemApplication)
                    }
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }
}
