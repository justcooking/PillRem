package com.pillrem.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pillrem.app.services.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        const val EXTRA_DOSE_ID = "extra_dose_id"
        const val EXTRA_PROFILE_NAME = "extra_profile_name"
        const val EXTRA_MEDICINE_NAME = "extra_medicine_name"
        const val EXTRA_DOSAGE_INFO = "extra_dosage_info"
        const val EXTRA_SNOOZE_MINUTES = "extra_snooze_minutes"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        val doseId = intent.getLongExtra(EXTRA_DOSE_ID, 0L)
        val profileName = intent.getStringExtra(EXTRA_PROFILE_NAME) ?: "PillRem"
        val medicineName = intent.getStringExtra(EXTRA_MEDICINE_NAME) ?: "Medication"
        val dosageInfo = intent.getStringExtra(EXTRA_DOSAGE_INFO) ?: "Time for your dose"
        val snoozeMinutes = intent.getIntExtra(EXTRA_SNOOZE_MINUTES, 15)

        if (notificationId != 0 && doseId != 0L) {
            NotificationHelper.showDoseReminder(
                context = context,
                notificationId = notificationId,
                doseId = doseId,
                profileName = profileName,
                medicineName = medicineName,
                dosageInfo = dosageInfo,
                snoozeMinutes = snoozeMinutes
            )
        }
    }
}
