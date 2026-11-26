package com.pillrem.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Upload
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.BuildConfig
import com.pillrem.app.R
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.ProfileEntity
import com.pillrem.app.data.update.UpdateManager
import com.pillrem.app.data.update.UpdateState
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.services.BackupHelper
import com.pillrem.app.services.PermissionHelper
import com.pillrem.app.ui.components.AddProfileDialog
import com.pillrem.app.ui.theme.*
import com.pillrem.app.utils.AppLanguage
import com.pillrem.app.utils.LocaleHelper
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    updateManager: UpdateManager
) {
    val coroutineScope = rememberCoroutineScope()
    val profiles by db.profileDao().getAllProfiles().collectAsState(initial = emptyList())
    val activeProfile by db.profileDao().getActiveProfile().collectAsState(initial = null)
    val activeId = activeProfile?.id ?: 0L

    val settings by db.profileDao().getSettingsForProfile(activeId).collectAsState(initial = null)
    var showAddProfileDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(Modifier.height(4.dp))
            Text(stringResource(R.string.section_profiles), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    profiles.forEach { p ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(p.avatarColor)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    p.name.take(1),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(p.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)

                            if (p.isActive) {
                                Icon(Icons.Default.Check, contentDescription = "Active", tint = if (isDark) DarkPrimary else LightPrimary)
                            } else {
                                TextButton(onClick = {
                                    coroutineScope.launch { db.profileDao().setActiveProfile(p.id) }
                                }) {
                                    Text(stringResource(R.string.btn_switch))
                                }
                            }

                            if (profiles.size > 1) {
                                IconButton(onClick = {
                                    coroutineScope.launch { db.profileDao().deleteProfile(p.id) }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Profile", tint = Color.Gray, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { showAddProfileDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDark) DarkPrimary else LightPrimary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(R.string.btn_add_profile), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        item {
            Text(stringResource(R.string.section_notifications), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val currentGrace = settings?.gracePeriodMinutes ?: 60
                    val currentSnooze = settings?.defaultSnoozeMinutes ?: 15

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.grace_period_title), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(stringResource(R.string.minutes_format, currentGrace), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = if (isDark) DarkPrimary else LightPrimary)
                        }
                        Slider(
                            value = currentGrace.toFloat(),
                            onValueChange = { newValue ->
                                coroutineScope.launch {
                                    settings?.let { s ->
                                        db.profileDao().updateSettings(s.copy(gracePeriodMinutes = newValue.toInt()))
                                    }
                                }
                            },
                            valueRange = 15f..180f,
                            steps = 10
                        )
                        Text(
                            stringResource(R.string.grace_period_desc),
                            fontSize = 12.sp,
                            color = if (isDark) DarkTextSecondary else LightTextSecondary
                        )
                    }

                    HorizontalDivider()

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(stringResource(R.string.snooze_duration_title), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(stringResource(R.string.minutes_format, currentSnooze), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = if (isDark) DarkPrimary else LightPrimary)
                        }
                        Slider(
                            value = currentSnooze.toFloat(),
                            onValueChange = { newValue ->
                                coroutineScope.launch {
                                    settings?.let { s ->
                                        db.profileDao().updateSettings(s.copy(defaultSnoozeMinutes = newValue.toInt()))
                                    }
                                }
                            },
                            valueRange = 5f..60f,
                            steps = 10
                        )
                        Text(
                            stringResource(R.string.snooze_duration_desc),
                            fontSize = 12.sp,
                            color = if (isDark) DarkTextSecondary else LightTextSecondary
                        )
                    }
                }
            }
        }

        item {
            Text(stringResource(R.string.section_permissions), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }

        item {
            val context = LocalContext.current
            val hasNotif = remember { PermissionHelper.hasNotificationPermission(context) }
            val canExact = remember { PermissionHelper.canScheduleExactAlarms(context) }
            val isBatteryIgnored = remember { PermissionHelper.isIgnoringBatteryOptimizations(context) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.perm_notif_title), fontWeight = FontWeight.Medium)
                            Text(
                                if (hasNotif) stringResource(R.string.perm_notif_granted) else stringResource(R.string.perm_notif_denied),
                                fontSize = 12.sp,
                                color = if (hasNotif) (if (isDark) DarkSuccess else LightSuccess) else (if (isDark) DarkDanger else LightDanger)
                            )
                        }
                        if (!hasNotif) {
                            TextButton(onClick = { PermissionHelper.openAppNotificationSettings(context) }) {
                                Text(stringResource(R.string.btn_fix))
                            }
                        }
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.perm_exact_title), fontWeight = FontWeight.Medium)
                            Text(
                                if (canExact) stringResource(R.string.perm_exact_allowed) else stringResource(R.string.perm_exact_denied),
                                fontSize = 12.sp,
                                color = if (canExact) (if (isDark) DarkSuccess else LightSuccess) else (if (isDark) DarkWarning else LightWarning)
                            )
                        }
                        if (!canExact) {
                            TextButton(onClick = { PermissionHelper.openExactAlarmSettings(context) }) {
                                Text(stringResource(R.string.btn_allow))
                            }
                        }
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.perm_battery_title), fontWeight = FontWeight.Medium)
                            Text(
                                if (isBatteryIgnored) stringResource(R.string.perm_battery_unrestricted) else stringResource(R.string.perm_battery_optimized),
                                fontSize = 12.sp,
                                color = if (isBatteryIgnored) (if (isDark) DarkSuccess else LightSuccess) else (if (isDark) DarkWarning else LightWarning)
                            )
                        }
                        if (!isBatteryIgnored) {
                            TextButton(onClick = { PermissionHelper.openBatteryOptimizationSettings(context) }) {
                                Text(stringResource(R.string.btn_unrestrict))
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(stringResource(R.string.section_language), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }

        item {
            val currentLanguage by LocaleHelper.currentLanguage.collectAsState()
            val context = LocalContext.current

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDark) DarkRaised else LightRaised),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = null,
                                tint = if (isDark) DarkPrimary else LightPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(currentLanguage.nativeName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(
                                stringResource(R.string.language_description),
                                fontSize = 12.sp,
                                color = if (isDark) DarkTextSecondary else LightTextSecondary
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.entries.forEach { lang ->
                            val isSelected = currentLanguage == lang
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (!isSelected) {
                                        LocaleHelper.setLanguage(context, lang)
                                    }
                                },
                                label = {
                                    Text(
                                        "${lang.nativeName} (${lang.displayName})",
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                } else null
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(stringResource(R.string.section_appearance), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(stringResource(R.string.dark_theme_title), fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        Text(
                            stringResource(R.string.dark_theme_desc),
                            fontSize = 12.sp,
                            color = if (isDark) DarkTextSecondary else LightTextSecondary
                        )
                    }
                    Switch(
                        checked = isDark,
                        onCheckedChange = { onToggleTheme() }
                    )
                }
            }
        }

        item {
            Text(stringResource(R.string.section_backup), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }

        item {
            val context = LocalContext.current
            val exportLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument("application/json")
            ) { uri ->
                if (uri != null) {
                    coroutineScope.launch {
                        val ok = BackupHelper.exportBackup(context, uri, db)
                        Toast.makeText(
                            context,
                            if (ok) "Backup exported successfully!" else "Failed to export backup",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            val importLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument()
            ) { uri ->
                if (uri != null) {
                    coroutineScope.launch {
                        val ok = BackupHelper.importBackup(context, uri, db, doseEngine)
                        Toast.makeText(
                            context,
                            if (ok) "Backup restored successfully!" else "Failed to import backup",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        stringResource(R.string.backup_desc),
                        fontSize = 12.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                exportLauncher.launch("pillrem_backup_${System.currentTimeMillis() / 1000}.json")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.btn_export_json), fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                importLauncher.launch(arrayOf("application/json", "text/*", "*/*"))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) DarkPrimary else LightPrimary
                            )
                        ) {
                            Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.btn_import_json), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(4.dp))
            Text(stringResource(R.string.section_updates), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }

        item {
            val updateState by updateManager.updateState.collectAsState()
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDark) DarkRaised else LightRaised),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = if (isDark) DarkPrimary else LightPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.version_format, BuildConfig.VERSION_NAME),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                stringResource(R.string.build_format, BuildConfig.VERSION_CODE),
                                fontSize = 12.sp,
                                color = if (isDark) DarkTextSecondary else LightTextSecondary
                            )
                        }
                    }

                    Text(
                        stringResource(R.string.updates_desc),
                        fontSize = 12.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    updateManager.checkForUpdates(silent = false)
                                }
                            },
                            enabled = updateState !is UpdateState.Checking && updateState !is UpdateState.Downloading,
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDark) DarkPrimary else LightPrimary
                            )
                        ) {
                            if (updateState is UpdateState.Checking) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(stringResource(R.string.checking_updates), fontSize = 12.sp)
                            } else {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(stringResource(R.string.btn_check_updates), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.offline_privacy_footer),
                    fontSize = 11.sp,
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
            }
        }
    }

    if (showAddProfileDialog) {
        AddProfileDialog(
            db = db,
            onDismiss = { showAddProfileDialog = false },
            onProfileAdded = {}
        )
    }
}
