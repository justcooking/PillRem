package com.pillrem.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                label = { Text("All (${doses.size})") }
            )
            FilterChip(
                selected = selectedFilter == "taken",
                onClick = { selectedFilter = "taken" },
                label = { Text("Taken (${adherenceStats.takenCount})") }
            )
            FilterChip(
                selected = selectedFilter == "missed",
                onClick = { selectedFilter = "missed" },
                label = { Text("Missed (${adherenceStats.missedCount})") }
            )
            FilterChip(
                selected = selectedFilter == "skipped",
                onClick = { selectedFilter = "skipped" },
                label = { Text("Skipped (${adherenceStats.skippedCount})") }
            )
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No dose records found.",
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
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
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    item.medicine.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    "${com.pillrem.app.utils.DosageFormatter.formatWithUnit(item.medicine.dosageAmount, item.medicine.dosageUnit)} • ${timeFormat.format(Date(dose.scheduledAt))}",
                                    fontSize = 13.sp,
                                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                                )
                                if (dose.status == "missed") {
                                    Text(
                                        "Tap to mark as taken",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isDark) DarkPrimary else LightPrimary
                                    )
                                } else if (dose.status == "skipped" && !dose.skipReason.isNullOrBlank()) {
                                    Text(
                                        "Reason: ${dose.skipReason}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(statusColor.copy(alpha = 0.12f), shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    dose.status.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
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
