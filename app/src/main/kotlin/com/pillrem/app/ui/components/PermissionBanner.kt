package com.pillrem.app.ui.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import com.pillrem.app.ui.theme.*

@Composable
fun PermissionBanner(
    isDark: Boolean,
    onPermissionChanged: () -> Unit = {}
) {
    val context = LocalContext.current
    var hasNotifPermission by remember { mutableStateOf(PermissionHelper.hasNotificationPermission(context)) }
    var canExactAlarm by remember { mutableStateOf(PermissionHelper.canScheduleExactAlarms(context)) }

    val warningColor = if (isDark) DarkWarning else LightWarning
    val borderColor = if (isDark) DarkBorder else LightBorder

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotifPermission = isGranted
        onPermissionChanged()
    }

    LaunchedEffect(Unit) {
        hasNotifPermission = PermissionHelper.hasNotificationPermission(context)
        canExactAlarm = PermissionHelper.canScheduleExactAlarms(context)
    }

    if (!hasNotifPermission) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = warningColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Notification Permission Needed",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Enable notifications so PillRem can alert you when doses are due.",
                        fontSize = 12.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
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
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) DarkRaised else LightRaised,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text("Enable", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    } else if (!canExactAlarm) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Alarm,
                    contentDescription = null,
                    tint = warningColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Exact Alarms Restricted",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Allow exact alarms in Android settings to ensure on-time medication alerts.",
                        fontSize = 12.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { PermissionHelper.openExactAlarmSettings(context) },
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) DarkRaised else LightRaised,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text("Allow", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
