package com.pillrem.app.ui.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.services.PermissionHelper
import com.pillrem.app.ui.theme.DarkWarning
import com.pillrem.app.ui.theme.LightWarning

@Composable
fun PermissionBanner(
    isDark: Boolean,
    onPermissionChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    var hasNotifPermission by remember { mutableStateOf(PermissionHelper.hasNotificationPermission(context)) }
    var canExactAlarm by remember { mutableStateOf(PermissionHelper.canScheduleExactAlarms(context)) }

    val warningColor = if (isDark) DarkWarning else LightWarning

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotifPermission = isGranted
        onPermissionChanged()
    }

    // Refresh state when composable recomposes
    LaunchedEffect(Unit) {
        hasNotifPermission = PermissionHelper.hasNotificationPermission(context)
        canExactAlarm = PermissionHelper.canScheduleExactAlarms(context)
    }

    if (!hasNotifPermission) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = warningColor.copy(alpha = 0.12f)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = warningColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Notification Permission Needed",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Enable notifications so PillRem can alert you when doses are due.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            PermissionHelper.openAppNotificationSettings(context)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = warningColor),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Enable", fontSize = 12.sp)
                }
            }
        }
    } else if (!canExactAlarm) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = warningColor.copy(alpha = 0.12f)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Alarm,
                    contentDescription = null,
                    tint = warningColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Exact Alarms Restricted",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Allow exact alarms in Android settings to ensure on-time medication alerts.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { PermissionHelper.openExactAlarmSettings(context) },
                    colors = ButtonDefaults.buttonColors(containerColor = warningColor),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Allow", fontSize = 12.sp)
                }
            }
        }
    }
}
