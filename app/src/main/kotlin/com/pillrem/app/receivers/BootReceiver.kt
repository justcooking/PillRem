package com.pillrem.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.services.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED
        ) {
            val pendingResult = goAsync()
            val db = AppDatabase.getInstance(context)
            val doseEngine = DoseEngine(db)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Pre-generate rolling doses
                    doseEngine.generateRollingDoses(daysAhead = 7)

                    // Reschedule pending alarms for today and future
                    val now = System.currentTimeMillis()
                    val profiles = db.profileDao().getAllProfilesSync()

                    for (profile in profiles) {
                        val endOfRange = now + (7 * 24 * 60 * 60 * 1000L)
                        val doses = db.doseRecordDao().getDosesForProfileRangeSync(profile.id, now, endOfRange)

                        for (d in doses) {
                            if (d.dose.status == "pending" && d.dose.reminderAt > now) {
                                AlarmScheduler.scheduleExactAlarm(
                                    context = context,
                                    triggerEpochMs = d.dose.reminderAt,
                                    notificationId = d.dose.notificationId,
                                    doseId = d.dose.id,
                                    profileName = profile.name,
                                    medicineName = d.medicine.name,
                                    dosageInfo = "${d.medicine.dosageAmount.toInt()} ${d.medicine.dosageUnit}"
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
}
