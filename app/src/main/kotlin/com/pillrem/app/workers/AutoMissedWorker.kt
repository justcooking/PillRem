package com.pillrem.app.workers

import android.content.Context
import androidx.work.*
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.services.NotificationHelper
import java.util.concurrent.TimeUnit

class AutoMissedWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getInstance(applicationContext)
        val doseEngine = DoseEngine(db)

        val now = System.currentTimeMillis()
        val expiredDoses = db.doseRecordDao().getUnresolvedExpiredDoses(now)

        if (expiredDoses.isNotEmpty()) {
            doseEngine.autoMarkMissedDoses(now)
            // Dismiss notification if it's still lingering on the lock screen past the grace deadline
            for (dose in expiredDoses) {
                NotificationHelper.cancelNotification(applicationContext, dose.notificationId)
            }
        }

        return Result.success()
    }

    companion object {
        const val WORK_NAME_PERIODIC = "auto_missed_dose_periodic"

        fun schedulePeriodic(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<AutoMissedWorker>(
                15, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME_PERIODIC,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        fun scheduleOneTimeGraceCheck(context: Context, delayMillis: Long) {
            val workRequest = OneTimeWorkRequestBuilder<AutoMissedWorker>()
                .setInitialDelay(delayMillis.coerceAtLeast(0), TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
