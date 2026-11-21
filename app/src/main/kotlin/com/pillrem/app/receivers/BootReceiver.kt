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
                    // Shared generation and alarm reconciliation on reboot or timezone shift
                    doseEngine.reconcileAndScheduleAlarms(context)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
