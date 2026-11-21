package com.pillrem.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.services.AlarmScheduler
import com.pillrem.app.services.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DoseActionReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_TAKE = "com.pillrem.app.ACTION_TAKE"
        const val ACTION_SNOOZE = "com.pillrem.app.ACTION_SNOOZE"
        const val EXTRA_DOSE_ID = "extra_dose_id"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val doseId = intent.getLongExtra(EXTRA_DOSE_ID, 0L)
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)

        if (doseId == 0L) return

        val pendingResult = goAsync()
        val db = AppDatabase.getInstance(context)
        val doseEngine = DoseEngine(db)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (action) {
                    ACTION_TAKE -> {
                        doseEngine.takeDose(doseId)
                        NotificationHelper.cancelNotification(context, notificationId)
                    }
                    ACTION_SNOOZE -> {
                        doseEngine.snoozeDose(doseId, 15)
                        NotificationHelper.cancelNotification(context, notificationId)

                        val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId)
                        if (doseWithMed != null) {
                            val profile = db.profileDao().getAllProfilesSync().firstOrNull { it.id == doseWithMed.dose.profileId }
                            val nextTrigger = System.currentTimeMillis() + (15 * 60 * 1000L)

                            AlarmScheduler.scheduleExactAlarm(
                                context = context,
                                triggerEpochMs = nextTrigger,
                                notificationId = notificationId,
                                doseId = doseId,
                                profileName = profile?.name ?: "Patient",
                                medicineName = doseWithMed.medicine.name,
                                dosageInfo = "${doseWithMed.medicine.dosageAmount.toInt()} ${doseWithMed.medicine.dosageUnit}"
                            )
                        }
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
