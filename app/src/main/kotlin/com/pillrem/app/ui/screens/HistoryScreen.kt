package com.pillrem.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.R
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.models.DoseWithMedicine
import com.pillrem.app.domain.AdherenceCalculator
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.ui.components.CorrectMissedDialog
import com.pillrem.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    profileId: Long,
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDark: Boolean
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedFilter by remember { mutableStateOf("all") } // all, taken, missed, skipped
    var selectedMissedDose by remember { mutableStateOf<DoseWithMedicine?>(null) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    // Fetch past 30 days doses
    val now = System.currentTimeMillis()
    val past30Days = now - (30L * 24 * 60 * 60 * 1000)

    val doses by db.doseRecordDao().getDosesForProfileRange(profileId, past30Days, now + (24 * 60 * 60 * 1000))
        .collectAsState(initial = emptyList())

    val filteredList = remember(doses, selectedFilter) {
        when (selectedFilter) {
            "taken" -> doses.filter { it.dose.status == "taken" }
            "missed" -> doses.filter { it.dose.status == "missed" }
            "skipped" -> doses.filter { it.dose.status == "skipped" }
            else -> doses
        }.sortedByDescending { it.dose.scheduledAt }
    }

    val adherenceStats = remember(doses) {
        AdherenceCalculator.calculate(doses.map { it.dose })
    }

    val timeFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                label = { Text(stringResource(R.string.filter_all, doses.size), fontSize = 12.sp) }
            )
            FilterChip(
                selected = selectedFilter == "taken",
                onClick = { selectedFilter = "taken" },
                label = { Text(stringResource(R.string.filter_taken, adherenceStats.takenCount), fontSize = 12.sp) }
            )
            FilterChip(
                selected = selectedFilter == "missed",
                onClick = { selectedFilter = "missed" },
                label = { Text(stringResource(R.string.filter_missed, adherenceStats.missedCount), fontSize = 12.sp) }
            )
            FilterChip(
                selected = selectedFilter == "skipped",
                onClick = { selectedFilter = "skipped" },
                label = { Text(stringResource(R.string.filter_skipped, adherenceStats.skippedCount), fontSize = 12.sp) }
            )
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.no_dose_records),
                    fontSize = 13.sp,
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, key = { it.dose.id }) { item ->
                    val dose = item.dose
                    val statusColor = when (dose.status) {
                        "taken" -> if (isDark) DarkSuccess else LightSuccess
                        "missed" -> if (isDark) DarkDanger else LightDanger
                        "skipped" -> if (isDark) DarkNeutral else LightNeutral
                        else -> if (isDark) DarkPrimary else LightPrimary
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (dose.status == "missed") Modifier.clickable { selectedMissedDose = item }
                                else Modifier
                            ),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Quiet status dot
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    item.medicine.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "${com.pillrem.app.utils.DosageFormatter.formatWithUnit(item.medicine.dosageAmount, item.medicine.dosageUnit)} • ${timeFormat.format(Date(dose.scheduledAt))}",
                                    fontSize = 13.sp,
                                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                                )
                                if (dose.status == "missed") {
                                    Text(
                                        stringResource(R.string.tap_to_mark_taken),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isDark) DarkPrimary else LightPrimary
                                    )
                                } else if (dose.status == "skipped" && !dose.skipReason.isNullOrBlank()) {
                                    Text(
                                        "Reason: ${dose.skipReason}",
                                        fontSize = 11.sp,
                                        color = if (isDark) DarkNeutral else LightNeutral
                                    )
                                }
                            }

                            val statusText = when (dose.status) {
                                "taken" -> stringResource(R.string.stat_taken)
                                "missed" -> stringResource(R.string.stat_missed)
                                "skipped" -> stringResource(R.string.stat_skipped)
                                else -> "Scheduled"
                            }
                            Text(
                                statusText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = statusColor
                            )
                        }
                    }
                }
            }
        }
    }

    selectedMissedDose?.let { doseItem ->
        CorrectMissedDialog(
            item = doseItem,
            doseEngine = doseEngine,
            onDismiss = { selectedMissedDose = null },
            onCorrected = {
                refreshTrigger++
            }
        )
    }
}
