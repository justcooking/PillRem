package com.pillrem.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.R
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.MedicineEntity
import com.pillrem.app.data.local.entities.ScheduleEntity
import com.pillrem.app.data.models.ScheduleType
import com.pillrem.app.domain.DoseEngine
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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

    // Medicine Basic Info
    var name by remember { mutableStateOf("") }
    var dosageAmount by remember { mutableStateOf("1") }
    var dosageUnit by remember { mutableStateOf("tablet") }
    var form by remember { mutableStateOf("tablet") }
    var instructions by remember { mutableStateOf("With Food") }

    // Validation error states
    var nameError by remember { mutableStateOf<String?>(null) }
    var dosageError by remember { mutableStateOf<String?>(null) }
    var timingError by remember { mutableStateOf<String?>(null) }

    val forms = listOf("tablet", "capsule", "syrup", "injection", "drops", "inhaler", "patch", "cream")
    val units = listOf("tablet", "capsule", "mg", "ml", "drops", "puffs", "g")
    val instructionOptions = listOf("With Food", "Before Food", "After Food", "Empty Stomach", "Before Bed")

    var formExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }
    var instructionExpanded by remember { mutableStateOf(false) }

    // Recurrence State (Google Tasks / Google Calendar style)
    val frequencyOptions = listOf("Day(s)", "Week(s)", "Month(s)")
    var selectedFrequency by remember { mutableStateOf("Day(s)") }
    var frequencyExpanded by remember { mutableStateOf(false) }
    var intervalStr by remember { mutableStateOf("1") }

    val todayDayOfWeek = remember { LocalDate.now().dayOfWeek.value } // 1=Mon .. 7=Sun
    var selectedDaysOfWeek by remember { mutableStateOf(setOf(todayDayOfWeek)) }

    // N times per day: List of (Hour, Minute)
    var reminderTimes by remember { mutableStateOf(listOf(Pair(8, 0))) }

    val weekdays = remember {
        (1..7).map { dayNum ->
            val display = try {
                DayOfWeek.of(dayNum).getDisplayName(TextStyle.NARROW, Locale.getDefault())
            } catch (_: Exception) {
                when (dayNum) {
                    1 -> "M"; 2 -> "T"; 3 -> "W"; 4 -> "T"; 5 -> "F"; 6 -> "S"; else -> "S"
                }
            }
            Pair(dayNum, display)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(14.dp),
        title = { Text(stringResource(R.string.dialog_add_medicine_title), fontWeight = FontWeight.SemiBold, fontSize = 16.sp) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (nameError != null) nameError = null
                    },
                    label = { Text(stringResource(R.string.med_name_label)) },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Dosage & Unit
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
                        label = { Text(stringResource(R.string.med_dosage_label)) },
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
                            label = { Text(stringResource(R.string.med_unit_label)) },
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

                // Form & Instructions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = formExpanded,
                        onExpandedChange = { formExpanded = !formExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = form.replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.med_form_label)) },
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
                        onExpandedChange = { instructionExpanded = !instructionExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = instructions,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.med_instructions_label)) },
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
                }

                HorizontalDivider()

                // Recurrence Section (Google Calendar / Tasks style)
                Text(
                    stringResource(R.string.repeating_cadence),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = intervalStr,
                        onValueChange = {
                            if (it.length <= 3 && (it.isEmpty() || it.all { char -> char.isDigit() })) {
                                intervalStr = it
                            }
                        },
                        label = { Text(stringResource(R.string.cadence_every)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.8f)
                    )

                    ExposedDropdownMenuBox(
                        expanded = frequencyExpanded,
                        onExpandedChange = { frequencyExpanded = !frequencyExpanded },
                        modifier = Modifier.weight(1.2f)
                    ) {
                        OutlinedTextField(
                            value = selectedFrequency,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.cadence_label)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = frequencyExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = frequencyExpanded,
                            onDismissRequest = { frequencyExpanded = false }
                        ) {
                            frequencyOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        selectedFrequency = opt
                                        frequencyExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // If Weekly, show Weekday selection pills
                if (selectedFrequency == "Week(s)") {
                    Text(stringResource(R.string.select_days_of_week), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        weekdays.forEach { (dayNum, label) ->
                            val isSelected = selectedDaysOfWeek.contains(dayNum)
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .clickable {
                                        selectedDaysOfWeek = if (isSelected) {
                                            if (selectedDaysOfWeek.size > 1) selectedDaysOfWeek - dayNum else selectedDaysOfWeek
                                        } else {
                                            selectedDaysOfWeek + dayNum
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                // N Times Per Day Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.dose_times_header, reminderTimes.size),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Preset Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "1× (08:00)" to listOf(Pair(8, 0)),
                        "2× (08, 20)" to listOf(Pair(8, 0), Pair(20, 0)),
                        "3× (08, 14, 20)" to listOf(Pair(8, 0), Pair(14, 0), Pair(20, 0))
                    ).forEach { (label, presetTimes) ->
                        FilterChip(
                            selected = reminderTimes == presetTimes,
                            onClick = { reminderTimes = presetTimes },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }

                // List of times with inputs
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    reminderTimes.forEachIndexed { index, timePair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )

                            // Hour input
                            OutlinedTextField(
                                value = String.format(Locale.getDefault(), "%02d", timePair.first),
                                onValueChange = { newVal ->
                                    val h = newVal.toIntOrNull()
                                    if (newVal.isEmpty() || (h != null && h in 0..23)) {
                                        val updated = reminderTimes.toMutableList()
                                        updated[index] = Pair(h ?: 0, timePair.second)
                                        reminderTimes = updated
                                    }
                                },
                                label = { Text(stringResource(R.string.hour_label)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            Text(":", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                            // Minute input
                            OutlinedTextField(
                                value = String.format(Locale.getDefault(), "%02d", timePair.second),
                                onValueChange = { newVal ->
                                    val m = newVal.toIntOrNull()
                                    if (newVal.isEmpty() || (m != null && m in 0..59)) {
                                        val updated = reminderTimes.toMutableList()
                                        updated[index] = Pair(timePair.first, m ?: 0)
                                        reminderTimes = updated
                                    }
                                },
                                label = { Text(stringResource(R.string.min_label)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            if (reminderTimes.size > 1) {
                                IconButton(
                                    onClick = {
                                        reminderTimes = reminderTimes.filterIndexed { i, _ -> i != index }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove Time",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    // Add Time Button
                    TextButton(
                        onClick = {
                            val last = reminderTimes.lastOrNull() ?: Pair(8, 0)
                            val nextHour = (last.first + 4) % 24
                            reminderTimes = reminderTimes + Pair(nextHour, 0)
                        },
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(R.string.add_another_dose_time))
                    }
                }

                if (timingError != null) {
                    Text(
                        timingError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
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
                        nameError = context.getString(R.string.med_name_error)
                        hasError = true
                    }

                    val parsedDosage = dosageAmount.toDoubleOrNull()
                    if (parsedDosage == null || parsedDosage <= 0.0) {
                        dosageError = context.getString(R.string.med_dosage_error)
                        hasError = true
                    }

                    val parsedInterval = intervalStr.toIntOrNull()
                    if (parsedInterval == null || parsedInterval < 1) {
                        timingError = context.getString(R.string.error_interval_min)
                        hasError = true
                    }

                    if (reminderTimes.isEmpty()) {
                        timingError = context.getString(R.string.error_at_least_one_dose)
                        hasError = true
                    }

                    if (hasError) return@Button

                    val interval = parsedInterval ?: 1
                    val scheduleType = when (selectedFrequency) {
                        "Day(s)" -> ScheduleType.DAILY.value
                        "Week(s)" -> ScheduleType.WEEKLY.value
                        else -> ScheduleType.MONTHLY.value
                    }
                    val daysOfWeekStr = if (selectedFrequency == "Week(s)") {
                        selectedDaysOfWeek.sorted().joinToString(",")
                    } else null

                    coroutineScope.launch {
                        val med = MedicineEntity(
                            profileId = profileId,
                            name = trimmedName,
                            dosageAmount = parsedDosage!!,
                            dosageUnit = dosageUnit,
                            form = form,
                            instructions = instructions,
                            stockCount = 0
                        )
                        val medId = db.medicineDao().insertMedicine(med)

                        val now = System.currentTimeMillis()
                        // Insert a schedule record for each N-time per day
                        for (timePair in reminderTimes) {
                            val schedule = ScheduleEntity(
                                medicineId = medId,
                                profileId = profileId,
                                scheduleType = scheduleType,
                                timeHour = timePair.first,
                                timeMinute = timePair.second,
                                daysOfWeek = daysOfWeekStr,
                                intervalDays = interval,
                                startDate = now
                            )
                            db.scheduleDao().insertSchedule(schedule)
                        }

                        // Reconcile and schedule alarms immediately
                        doseEngine.reconcileAndScheduleAlarms(context)
                        onDismiss()
                    }
                },
                modifier = Modifier.height(36.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.btn_save), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(36.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.btn_cancel), fontSize = 13.sp)
            }
        }
    )
}
