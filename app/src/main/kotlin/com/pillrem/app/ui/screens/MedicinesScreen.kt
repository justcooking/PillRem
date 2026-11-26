package com.pillrem.app.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.R
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.MedicineEntity
import com.pillrem.app.data.local.entities.ScheduleEntity
import com.pillrem.app.data.models.ScheduleType
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.ui.components.AddMedicineDialog
import com.pillrem.app.ui.theme.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MedicinesScreen(
    profileId: Long,
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDark: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0: Active, 1: Archived
    var showAddDialog by remember { mutableStateOf(false) }

    val activeMedicines by db.medicineDao().getActiveMedicines(profileId).collectAsState(initial = emptyList())
    val archivedMedicines by db.medicineDao().getArchivedMedicines(profileId).collectAsState(initial = emptyList())
    val allSchedules by db.scheduleDao().getActiveSchedulesForProfile(profileId).collectAsState(initial = emptyList())
    val schedulesByMed = remember(allSchedules) { allSchedules.groupBy { it.medicineId } }

    val displayedList = if (selectedTab == 0) activeMedicines else archivedMedicines

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = if (isDark) DarkPrimary else LightPrimary,
                    contentColor = if (isDark) DarkBackground else Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_medicine), modifier = Modifier.size(20.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = if (isDark) DarkPrimary else LightPrimary,
                divider = {
                    HorizontalDivider(color = if (isDark) DarkBorder else LightBorder, thickness = 1.dp)
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(stringResource(R.string.tab_active, activeMedicines.size), fontSize = 13.sp, fontWeight = if (selectedTab == 0) FontWeight.SemiBold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(stringResource(R.string.tab_archived, archivedMedicines.size), fontSize = 13.sp, fontWeight = if (selectedTab == 1) FontWeight.SemiBold else FontWeight.Normal) }
                )
            }

            Spacer(Modifier.height(14.dp))

            if (displayedList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (selectedTab == 0) stringResource(R.string.no_active_medicines)
                        else stringResource(R.string.no_archived_medicines),
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedList, key = { it.id }) { med ->
                        MedicineCard(
                            medicine = med,
                            schedules = schedulesByMed[med.id] ?: emptyList(),
                            isDark = isDark,
                            isArchived = selectedTab == 1,
                            onToggleArchive = {
                                coroutineScope.launch {
                                    if (selectedTab == 0) {
                                        doseEngine.archiveMedicine(context, med.id)
                                    } else {
                                        doseEngine.unarchiveMedicine(context, med.id)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMedicineDialog(
            profileId = profileId,
            db = db,
            doseEngine = doseEngine,
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun MedicineCard(
    medicine: MedicineEntity,
    schedules: List<ScheduleEntity>,
    isDark: Boolean,
    isArchived: Boolean,
    onToggleArchive: () -> Unit
) {
    val context = LocalContext.current
    val scheduleSummary = remember(schedules, context) { formatScheduleSummary(context, schedules) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Medication,
                contentDescription = null,
                tint = if (isDark) DarkTextSecondary else LightTextSecondary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    medicine.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${com.pillrem.app.utils.DosageFormatter.formatWithUnit(medicine.dosageAmount, medicine.dosageUnit)} • ${medicine.form.replaceFirstChar { it.uppercase() }}",
                    fontSize = 13.sp,
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
                if (!medicine.instructions.isNullOrBlank()) {
                    Text(
                        medicine.instructions,
                        fontSize = 12.sp,
                        color = if (isDark) DarkNeutral else LightNeutral
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = if (isDark) DarkNeutral else LightNeutral
                    )
                    Text(
                        scheduleSummary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (isDark) DarkNeutral else LightNeutral
                    )
                }
            }

            IconButton(onClick = onToggleArchive) {
                Icon(
                    imageVector = if (isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                    contentDescription = stringResource(if (isArchived) R.string.action_restore else R.string.action_archive),
                    modifier = Modifier.size(18.dp),
                    tint = if (isDark) DarkNeutral else LightNeutral
                )
            }
        }
    }
}

fun formatScheduleSummary(context: Context, schedules: List<ScheduleEntity>): String {
    if (schedules.isEmpty()) return context.getString(R.string.schedule_no_reminders)
    val sortedTimes = schedules.sortedWith(compareBy({ it.timeHour }, { it.timeMinute }))
        .joinToString(", ") { String.format(Locale.getDefault(), "%02d:%02d", it.timeHour, it.timeMinute) }

    val first = schedules.first()
    val type = ScheduleType.fromString(first.scheduleType)
    val interval = first.intervalDays ?: 1

    return when (type) {
        ScheduleType.DAILY, ScheduleType.INTERVAL_DAYS -> {
            if (interval <= 1) context.getString(R.string.schedule_daily_at, sortedTimes)
            else context.getString(R.string.schedule_every_days_at, interval, sortedTimes)
        }
        ScheduleType.WEEKLY -> {
            val dayNames = first.daysOfWeek?.split(",")?.mapNotNull { it.trim().toIntOrNull() }
                ?.mapNotNull { dayNum ->
                    try {
                        DayOfWeek.of(dayNum).getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    } catch (_: Exception) {
                        null
                    }
                }?.joinToString(", ") ?: ""
            if (interval <= 1) {
                context.getString(R.string.schedule_weekly_at, dayNames, sortedTimes)
            } else {
                context.getString(R.string.schedule_weekly_interval, interval, dayNames, sortedTimes)
            }
        }
        ScheduleType.MONTHLY -> {
            if (interval <= 1) context.getString(R.string.schedule_monthly_at, sortedTimes)
            else context.getString(R.string.schedule_every_months_at, interval, sortedTimes)
        }
        ScheduleType.SPECIFIC_DAYS -> {
            val dayNames = first.daysOfWeek?.split(",")?.mapNotNull { it.trim().toIntOrNull() }
                ?.mapNotNull { dayNum ->
                    try {
                        DayOfWeek.of(dayNum).getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    } catch (_: Exception) {
                        null
                    }
                }?.joinToString(", ") ?: ""
            context.getString(R.string.schedule_weekly_at, dayNames, sortedTimes)
        }
        ScheduleType.AS_NEEDED -> context.getString(R.string.schedule_as_needed)
        else -> context.getString(R.string.schedule_as_needed)
    }
}
