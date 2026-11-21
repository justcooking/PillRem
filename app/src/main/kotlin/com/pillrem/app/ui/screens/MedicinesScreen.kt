package com.pillrem.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.MedicineEntity
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.ui.components.AddMedicineDialog
import com.pillrem.app.ui.theme.*
import kotlinx.coroutines.launch

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

    val displayedList = if (selectedTab == 0) activeMedicines else archivedMedicines

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = if (isDark) DarkPrimary else LightPrimary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Medicine")
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
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = if (isDark) DarkPrimary else LightPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Active (${activeMedicines.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Archived (${archivedMedicines.size})") }
                )
            }

            Spacer(Modifier.height(16.dp))

            if (displayedList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (selectedTab == 0) "No active medicines. Tap + to add one."
                        else "No archived medicines.",
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedList, key = { it.id }) { med ->
                        MedicineCard(
                            medicine = med,
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
    isDark: Boolean,
    isArchived: Boolean,
    onToggleArchive: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background((if (isDark) DarkPrimary else LightPrimary).copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Medication,
                    contentDescription = null,
                    tint = if (isDark) DarkPrimary else LightPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    medicine.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
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
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Remaining Stock: ${medicine.stockCount}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (medicine.stockCount <= 7) LightWarning else Color.Gray
                )
            }

            IconButton(onClick = onToggleArchive) {
                Icon(
                    imageVector = if (isArchived) Icons.Default.Unarchive else Icons.Default.Archive,
                    contentDescription = if (isArchived) "Restore" else "Archive",
                    tint = Color.Gray
                )
            }
        }
    }
}
