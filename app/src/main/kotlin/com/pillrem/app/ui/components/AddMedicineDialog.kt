package com.pillrem.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.MedicineEntity
import com.pillrem.app.data.local.entities.ScheduleEntity
import com.pillrem.app.data.models.ScheduleType
import com.pillrem.app.domain.DoseEngine
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineDialog(
    profileId: Long,
    db: AppDatabase,
    doseEngine: DoseEngine,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var dosageAmount by remember { mutableStateOf("1") }
    var dosageUnit by remember { mutableStateOf("tablet") }
    var form by remember { mutableStateOf("tablet") }
    var instructions by remember { mutableStateOf("With food") }
    var stockCount by remember { mutableStateOf("30") }
    var timeHour by remember { mutableStateOf("08") }
    var timeMinute by remember { mutableStateOf("00") }

    val forms = listOf("tablet", "capsule", "liquid", "injection", "inhaler", "drops")
    val units = listOf("tablet", "mg", "ml", "capsule", "drops", "puffs")
    val instructionOptions = listOf("Before food", "With food", "After food", "At bedtime", "Any time")

    var formExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }
    var instructionExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medication") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medicine Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = dosageAmount,
                        onValueChange = { dosageAmount = it },
                        label = { Text("Dosage") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    ExposedDropdownMenuBox(
                        expanded = unitExpanded,
                        onExpandedChange = { unitExpanded = !unitExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = dosageUnit,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Unit") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = unitExpanded,
                            onDismissRequest = { unitExpanded = false }
                        ) {
                            units.forEach { u ->
                                DropdownMenuItem(
                                    text = { Text(u) },
                                    onClick = {
                                        dosageUnit = u
                                        unitExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = formExpanded,
                    onExpandedChange = { formExpanded = !formExpanded }
                ) {
                    OutlinedTextField(
                        value = form.replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Form") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = formExpanded,
                        onDismissRequest = { formExpanded = false }
                    ) {
                        forms.forEach { f ->
                            DropdownMenuItem(
                                text = { Text(f.replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    form = f
                                    formExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = instructionExpanded,
                    onExpandedChange = { instructionExpanded = !instructionExpanded }
                ) {
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Clinical Instructions") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = instructionExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = instructionExpanded,
                        onDismissRequest = { instructionExpanded = false }
                    ) {
                        instructionOptions.forEach { inst ->
                            DropdownMenuItem(
                                text = { Text(inst) },
                                onClick = {
                                    instructions = inst
                                    instructionExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = stockCount,
                    onValueChange = { stockCount = it },
                    label = { Text("Starting Stock Count") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Daily Scheduled Time", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = timeHour,
                        onValueChange = { if (it.length <= 2) timeHour = it },
                        label = { Text("Hour (0-23)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = timeMinute,
                        onValueChange = { if (it.length <= 2) timeMinute = it },
                        label = { Text("Minute (0-59)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            val med = MedicineEntity(
                                profileId = profileId,
                                name = name.trim(),
                                dosageAmount = dosageAmount.toDoubleOrNull() ?: 1.0,
                                dosageUnit = dosageUnit,
                                form = form,
                                instructions = instructions,
                                stockCount = stockCount.toIntOrNull() ?: 0
                            )
                            val medId = db.medicineDao().insertMedicine(med)

                            val hour = (timeHour.toIntOrNull() ?: 8).coerceIn(0, 23)
                            val minute = (timeMinute.toIntOrNull() ?: 0).coerceIn(0, 59)

                            val schedule = ScheduleEntity(
                                medicineId = medId,
                                profileId = profileId,
                                scheduleType = ScheduleType.DAILY.value,
                                timeHour = hour,
                                timeMinute = minute,
                                startDate = System.currentTimeMillis()
                            )
                            db.scheduleDao().insertSchedule(schedule)

                            // Pre-generate rolling doses
                            doseEngine.generateRollingDoses(daysAhead = 7)
                            onDismiss()
                        }
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
