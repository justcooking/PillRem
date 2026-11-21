package com.pillrem.app.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.ProfileEntity
import com.pillrem.app.ui.components.AddProfileDialog
import com.pillrem.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    db: AppDatabase,
    isDark: Boolean,
    onToggleTheme: () -> Unit
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
            Text("Profiles & Accounts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
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
                                    Text("Switch")
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDark) DarkPrimary else LightPrimary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add New Profile")
                    }
                }
            }
        }

        item {
            Text("Notification Preferences", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val currentGrace = settings?.gracePeriodMinutes ?: 60
                    val currentSnooze = settings?.defaultSnoozeMinutes ?: 15

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Missed Dose Grace Period")
                            Text("$currentGrace minutes", fontWeight = FontWeight.Bold, color = if (isDark) DarkPrimary else LightPrimary)
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
                            "Unresolved doses past this deadline are automatically marked as missed.",
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
                            Text("Default Snooze Duration")
                            Text("$currentSnooze minutes", fontWeight = FontWeight.Bold, color = if (isDark) DarkPrimary else LightPrimary)
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
                            "Snoozing advances the alarm reminder while strictly preserving the original grace period deadline.",
                            fontSize = 12.sp,
                            color = if (isDark) DarkTextSecondary else LightTextSecondary
                        )
                    }
                }
            }
        }

        item {
            Text("Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Dark Theme", fontWeight = FontWeight.Medium)
                        Text(
                            "Clinical high-contrast dark palette",
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "PillRem • 100% Offline & Private • Zero Trackers",
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
