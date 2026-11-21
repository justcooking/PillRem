package com.pillrem.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var dosageAmount by remember { mutableStateOf("1") }
    var dosageUnit by remember { mutableStateOf("tablet") }
    var form by remember { mutableStateOf("tablet") }
    var instructions by remember { mutableStateOf("With Food") }
    var stockCount by remember { mutableStateOf("30") }
    var timeHour by remember { mutableStateOf("08") }
    var timeMinute by remember { mutableStateOf("00") }

    // Validation error states
    var nameError by remember { mutableStateOf<String?>(null) }
    var dosageError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }
    var timeHourError by remember { mutableStateOf<String?>(null) }
    var timeMinuteError by remember { mutableStateOf<String?>(null) }

    val forms = listOf("tablet", "capsule", "syrup", "injection", "drops", "inhaler", "patch", "cream")
    val units = listOf("tablet", "capsule", "mg", "ml", "drops", "puffs", "g")
    val instructionOptions = listOf("With Food", "Before Food", "After Food", "Empty Stomach", "Before Bed")

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
                    onValueChange = {
                        name = it
                        if (nameError != null) nameError = null
                    },
                    label = { Text("Medicine Name *") },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = dosageAmount,
                        onValueChange = {
                            dosageAmount = it
                            if (dosageError != null) dosageError = null
                        },
                        label = { Text("Dosage *") },
                        isError = dosageError != null,
                        supportingText = dosageError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
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
                    onValueChange = {
                        stockCount = it
                        if (stockError != null) stockError = null
                    },
                    label = { Text("Current Stock Count") },
                    isError = stockError != null,
                    supportingText = stockError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Daily Scheduled Time:", style = MaterialTheme.typography.labelLarge)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = timeHour,
                        onValueChange = {
                            if (it.length <= 2) {
                                timeHour = it
                                if (timeHourError != null) timeHourError = null
                            }
                        },
                        label = { Text("Hour (0-23)") },
                        isError = timeHourError != null,
                        supportingText = timeHourError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = timeMinute,
                        onValueChange = {
                            if (it.length <= 2) {
                                timeMinute = it
                                if (timeMinuteError != null) timeMinuteError = null
                            }
                        },
                        label = { Text("Minute (0-59)") },
                        isError = timeMinuteError != null,
                        supportingText = timeMinuteError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val trimmedName = name.trim()
                    var hasError = false

                    if (trimmedName.isEmpty()) {
                        nameError = "Medicine name is required"
                        hasError = true
                    }

                    val parsedDosage = dosageAmount.toDoubleOrNull()
                    if (parsedDosage == null || parsedDosage <= 0.0) {
                        dosageError = "Enter valid amount > 0 (e.g. 1 or 0.5)"
                        hasError = true
                    }

                    val parsedStock = stockCount.toIntOrNull()
                    if (parsedStock == null || parsedStock < 0) {
                        stockError = "Enter valid stock >= 0"
                        hasError = true
                    }

                    val parsedHour = timeHour.toIntOrNull()
                    if (parsedHour == null || parsedHour !in 0..23) {
                        timeHourError = "0-23"
                        hasError = true
                    }

                    val parsedMinute = timeMinute.toIntOrNull()
                    if (parsedMinute == null || parsedMinute !in 0..59) {
                        timeMinuteError = "0-59"
                        hasError = true
                    }

                    if (hasError) return@Button

                    coroutineScope.launch {
                        val med = MedicineEntity(
                            profileId = profileId,
                            name = trimmedName,
                            dosageAmount = parsedDosage!!,
                            dosageUnit = dosageUnit,
                            form = form,
                            instructions = instructions,
                            stockCount = parsedStock!!
                        )
                        val medId = db.medicineDao().insertMedicine(med)

                        val schedule = ScheduleEntity(
                            medicineId = medId,
                            profileId = profileId,
                            scheduleType = ScheduleType.DAILY.value,
                            timeHour = parsedHour!!,
                            timeMinute = parsedMinute!!,
                            startDate = System.currentTimeMillis()
                        )
                        db.scheduleDao().insertSchedule(schedule)

                        // Shared reconciliation: pre-generates rolling doses AND immediately schedules alarms
                        doseEngine.reconcileAndScheduleAlarms(context)
                        onDismiss()
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
