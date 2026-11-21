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
        const val EXTRA_SNOOZE_MINUTES = "extra_snooze_minutes"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val doseId = intent.getLongExtra(EXTRA_DOSE_ID, 0L)
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        val passedSnoozeMinutes = intent.getIntExtra(EXTRA_SNOOZE_MINUTES, 0)

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
                        val doseWithMed = db.doseRecordDao().getDoseWithMedicineById(doseId)
                        val effectiveSnoozeMinutes = if (passedSnoozeMinutes > 0) {
                            passedSnoozeMinutes
                        } else if (doseWithMed != null) {
                            db.profileDao().getSettingsForProfileSync(doseWithMed.dose.profileId)?.defaultSnoozeMinutes ?: 15
                        } else {
                            15
                        }

                        doseEngine.snoozeDose(doseId, effectiveSnoozeMinutes)
                        NotificationHelper.cancelNotification(context, notificationId)

                        if (doseWithMed != null) {
                            val profile = db.profileDao().getAllProfilesSync().firstOrNull { it.id == doseWithMed.dose.profileId }
                            val nextTrigger = System.currentTimeMillis() + (effectiveSnoozeMinutes * 60 * 1000L)

                            AlarmScheduler.scheduleExactAlarm(
                                context = context,
                                triggerEpochMs = nextTrigger,
                                notificationId = notificationId,
                                doseId = doseId,
                                profileName = profile?.name ?: "Patient",
                                medicineName = doseWithMed.medicine.name,
                                dosageInfo = com.pillrem.app.utils.DosageFormatter.formatWithUnit(
                                    doseWithMed.medicine.dosageAmount,
                                    doseWithMed.medicine.dosageUnit
                                ),
                                snoozeMinutes = effectiveSnoozeMinutes
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
