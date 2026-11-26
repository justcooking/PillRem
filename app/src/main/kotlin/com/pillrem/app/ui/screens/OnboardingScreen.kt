package com.pillrem.app.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.*
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
import com.pillrem.app.services.PermissionHelper
import com.pillrem.app.ui.theme.*
import com.pillrem.app.utils.AppLanguage
import com.pillrem.app.utils.LocaleHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDarkTheme: Boolean,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentStep by remember { mutableIntStateOf(0) }

    // Step 2 (Profile) State
    var profileName by remember { mutableStateOf("") }
    var profileNameError by remember { mutableStateOf<String?>(null) }
    val avatarColors = listOf(
        0xFF2B6CB0.toInt(), // Clinical Blue
        0xFF388E67.toInt(), // Medical Green
        0xFFD97706.toInt(), // Amber
        0xFF0D9488.toInt(), // Teal
        0xFF4F46E5.toInt(), // Indigo
        0xFFE11D48.toInt()  // Rose
    )
    var selectedColor by remember { mutableIntStateOf(avatarColors[0]) }
    var gracePeriodMinutes by remember { mutableIntStateOf(60) }
    var defaultSnoozeMinutes by remember { mutableIntStateOf(15) }

    // Step 3 (First Medicine) State
    var medName by remember { mutableStateOf("") }
    var medDosage by remember { mutableStateOf("1") }
    var medDosageUnit by remember { mutableStateOf("tablet") }
    var medForm by remember { mutableStateOf("tablet") }
    var medInstructions by remember { mutableStateOf("With Food") }
    var medFrequency by remember { mutableStateOf("Day(s)") }
    var medInterval by remember { mutableStateOf("1") }
    val todayDow = remember { java.time.LocalDate.now().dayOfWeek.value }
    var medDaysOfWeek by remember { mutableStateOf(setOf(todayDow)) }
    var medReminderTimes by remember { mutableStateOf(listOf(Pair(8, 0))) }

    var medDosageError by remember { mutableStateOf<String?>(null) }
    var medTimingError by remember { mutableStateOf<String?>(null) }

    val forms = listOf("tablet", "capsule", "syrup", "injection", "drops", "inhaler", "patch", "cream")
    val units = listOf("tablet", "capsule", "mg", "ml", "drops", "puffs", "g")
    val instructionsList = listOf("With Food", "Before Food", "After Food", "Empty Stomach", "Before Bed")

    // Finish Setup Helper
    fun finishSetup(addMedication: Boolean) {
        val trimmedName = profileName.trim().ifEmpty { "My Profile" }
        coroutineScope.launch {
            val profileId = db.profileDao().createProfileWithSettings(
                name = trimmedName,
                avatarColor = selectedColor,
                avatarIcon = "person",
                gracePeriodMinutes = gracePeriodMinutes,
                defaultSnoozeMinutes = defaultSnoozeMinutes
            )

            if (addMedication && medName.trim().isNotEmpty()) {
                val dosage = medDosage.toDoubleOrNull() ?: 1.0
                val interval = medInterval.toIntOrNull() ?: 1

                val med = MedicineEntity(
                    profileId = profileId,
                    name = medName.trim(),
                    dosageAmount = dosage,
                    dosageUnit = medDosageUnit,
                    form = medForm,
                    instructions = medInstructions,
                    stockCount = 0
                )
                val medId = db.medicineDao().insertMedicine(med)

                val schedType = when (medFrequency) {
                    "Day(s)" -> ScheduleType.DAILY.value
                    "Week(s)" -> ScheduleType.WEEKLY.value
                    else -> ScheduleType.MONTHLY.value
                }
                val daysStr = if (medFrequency == "Week(s)") medDaysOfWeek.sorted().joinToString(",") else null
                val now = System.currentTimeMillis()

                for (timePair in medReminderTimes) {
                    val schedule = ScheduleEntity(
                        medicineId = medId,
                        profileId = profileId,
                        scheduleType = schedType,
                        timeHour = timePair.first,
                        timeMinute = timePair.second,
                        daysOfWeek = daysStr,
                        intervalDays = interval,
                        startDate = now
                    )
                    db.scheduleDao().insertSchedule(schedule)
                }
            }

            // Reconcile and schedule alarms
            doseEngine.reconcileAndScheduleAlarms(context)
            onComplete()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AnimatedContent(
                targetState = currentStep,
                label = "OnboardingStepAnimation"
            ) { step ->
                when (step) {
                    0 -> WelcomeStep(
                        isDark = isDarkTheme,
                        onNext = { currentStep = 1 }
                    )
                    1 -> PermissionsStep(
                        isDark = isDarkTheme,
                        onBack = { currentStep = 0 },
                        onNext = { currentStep = 2 }
                    )
                    2 -> ProfileStep(
                        isDark = isDarkTheme,
                        name = profileName,
                        nameError = profileNameError,
                        onNameChange = {
                            profileName = it
                            if (profileNameError != null) profileNameError = null
                        },
                        avatarColors = avatarColors,
                        selectedColor = selectedColor,
                        onSelectColor = { selectedColor = it },
                        gracePeriod = gracePeriodMinutes,
                        onGraceChange = { gracePeriodMinutes = it },
                        snoozeDuration = defaultSnoozeMinutes,
                        onSnoozeChange = { defaultSnoozeMinutes = it },
                        onBack = { currentStep = 1 },
                        onNext = {
                            if (profileName.trim().isEmpty()) {
                                profileNameError = "Please enter your name"
                            } else {
                                currentStep = 3
                            }
                        }
                    )
                    3 -> FirstMedicineStep(
                        isDark = isDarkTheme,
                        name = medName,
                        onNameChange = { medName = it },
                        dosage = medDosage,
                        dosageError = medDosageError,
                        onDosageChange = {
                            medDosage = it
                            if (medDosageError != null) medDosageError = null
                        },
                        dosageUnit = medDosageUnit,
                        units = units,
                        onUnitChange = { medDosageUnit = it },
                        form = medForm,
                        forms = forms,
                        onFormChange = { medForm = it },
                        instructions = medInstructions,
                        instructionOptions = instructionsList,
                        onInstructionsChange = { medInstructions = it },
                        frequency = medFrequency,
                        onFrequencyChange = { medFrequency = it },
                        interval = medInterval,
                        onIntervalChange = { medInterval = it },
                        selectedDaysOfWeek = medDaysOfWeek,
                        onDaysOfWeekChange = { medDaysOfWeek = it },
                        reminderTimes = medReminderTimes,
                        onReminderTimesChange = { medReminderTimes = it },
                        timingError = medTimingError,
                        onBack = { currentStep = 2 },
                        onSkip = { finishSetup(addMedication = false) },
                        onFinish = {
                            var hasError = false
                            if (medName.trim().isNotEmpty()) {
                                if (medDosage.toDoubleOrNull() == null || medDosage.toDoubleOrNull()!! <= 0.0) {
                                    medDosageError = "Valid dosage > 0 required"
                                    hasError = true
                                }
                                val intVal = medInterval.toIntOrNull()
                                if (intVal == null || intVal < 1) {
                                    medTimingError = "Interval must be >= 1"
                                    hasError = true
                                }
                                if (medReminderTimes.isEmpty()) {
                                    medTimingError = "At least one dose time required"
                                    hasError = true
                                }
                            }
                            if (!hasError) {
                                finishSetup(addMedication = medName.trim().isNotEmpty())
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeStep(
    isDark: Boolean,
    onNext: () -> Unit
) {
    val currentLanguage by LocaleHelper.currentLanguage.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language selector chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
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
                        label = { Text(lang.nativeName, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isDark) DarkRaised else LightRaised),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = if (isDark) DarkPrimary else LightPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                stringResource(R.string.onboarding_welcome_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                stringResource(R.string.onboarding_welcome_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) DarkTextSecondary else LightTextSecondary,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(8.dp))

            ValuePropositionCard(
                icon = Icons.Default.Lock,
                title = stringResource(R.string.prop_offline_title),
                description = stringResource(R.string.prop_offline_desc),
                isDark = isDark
            )

            ValuePropositionCard(
                icon = Icons.Default.Alarm,
                title = stringResource(R.string.prop_exact_title),
                description = stringResource(R.string.prop_exact_desc),
                isDark = isDark
            )

            ValuePropositionCard(
                icon = Icons.Default.CheckCircle,
                title = stringResource(R.string.prop_adherence_title),
                description = stringResource(R.string.prop_adherence_desc),
                isDark = isDark
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) DarkPrimary else LightPrimary
                )
            ) {
                Text(
                    stringResource(R.string.btn_get_started),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) DarkBackground else Color.White
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun PermissionsStep(
    isDark: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    var hasNotification by remember { mutableStateOf(PermissionHelper.hasNotificationPermission(context)) }
    var hasExactAlarm by remember { mutableStateOf(PermissionHelper.canScheduleExactAlarms(context)) }
    var isBatteryOptIgnored by remember { mutableStateOf(PermissionHelper.isIgnoringBatteryOptimizations(context)) }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotification = granted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(stringResource(R.string.step_permissions_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Text(
                stringResource(R.string.step_permissions_headline),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(R.string.step_permissions_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) DarkTextSecondary else LightTextSecondary
            )

            Spacer(Modifier.height(8.dp))

            // Notification Card
            PermissionItemCard(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.perm_notif_title),
                description = stringResource(R.string.perm_card_notif_desc),
                isGranted = hasNotification,
                buttonText = stringResource(R.string.btn_grant),
                onAction = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        hasNotification = true
                    }
                },
                isDark = isDark
            )

            // Exact Alarm Card
            PermissionItemCard(
                icon = Icons.Default.Alarm,
                title = stringResource(R.string.perm_exact_title),
                description = stringResource(R.string.perm_card_exact_desc),
                isGranted = hasExactAlarm,
                buttonText = stringResource(R.string.btn_allow),
                onAction = {
                    PermissionHelper.openExactAlarmSettings(context)
                },
                isDark = isDark
            )

            // Battery Optimization Card
            PermissionItemCard(
                icon = Icons.Default.BatteryChargingFull,
                title = stringResource(R.string.perm_battery_title),
                description = stringResource(R.string.perm_card_battery_desc),
                isGranted = isBatteryOptIgnored,
                buttonText = stringResource(R.string.btn_unrestrict),
                onAction = {
                    PermissionHelper.openBatteryOptimizationSettings(context)
                },
                isDark = isDark
            )
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDark) DarkPrimary else LightPrimary
            )
        ) {
            Text(stringResource(R.string.btn_next_profile), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
private fun ProfileStep(
    isDark: Boolean,
    name: String,
    nameError: String?,
    onNameChange: (String) -> Unit,
    avatarColors: List<Int>,
    selectedColor: Int,
    onSelectColor: (Int) -> Unit,
    gracePeriod: Int,
    onGraceChange: (Int) -> Unit,
    snoozeDuration: Int,
    onSnoozeChange: (Int) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(stringResource(R.string.step_profile_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Text(
                stringResource(R.string.step_profile_headline),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(R.string.step_profile_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) DarkTextSecondary else LightTextSecondary
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.profile_name_hint)) },
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(stringResource(R.string.avatar_badge_color), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                avatarColors.forEach { colorInt ->
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(colorInt))
                            .border(
                                width = if (selectedColor == colorInt) 3.dp else 0.dp,
                                color = if (selectedColor == colorInt) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { onSelectColor(colorInt) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedColor == colorInt) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.clinical_timings), fontWeight = FontWeight.Bold, fontSize = 15.sp)

                    Text(
                        "Grace Period: $gracePeriod minutes",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "How long a dose stays actionable before being automatically marked as missed.",
                        fontSize = 11.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                    Slider(
                        value = gracePeriod.toFloat(),
                        onValueChange = { onGraceChange(it.toInt()) },
                        valueRange = 15f..180f,
                        steps = 10
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "Default Snooze: $snoozeDuration minutes",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "How long lock screen snooze notifications pause before reminding you again.",
                        fontSize = 11.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                    Slider(
                        value = snoozeDuration.toFloat(),
                        onValueChange = { onSnoozeChange(it.toInt()) },
                        valueRange = 5f..60f,
                        steps = 10
                    )
                }
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDark) DarkPrimary else LightPrimary
            )
        ) {
            Text(stringResource(R.string.btn_next_medication), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FirstMedicineStep(
    isDark: Boolean,
    name: String,
    onNameChange: (String) -> Unit,
    dosage: String,
    dosageError: String?,
    onDosageChange: (String) -> Unit,
    dosageUnit: String,
    units: List<String>,
    onUnitChange: (String) -> Unit,
    form: String,
    forms: List<String>,
    onFormChange: (String) -> Unit,
    instructions: String,
    instructionOptions: List<String>,
    onInstructionsChange: (String) -> Unit,
    frequency: String,
    onFrequencyChange: (String) -> Unit,
    interval: String,
    onIntervalChange: (String) -> Unit,
    selectedDaysOfWeek: Set<Int>,
    onDaysOfWeekChange: (Set<Int>) -> Unit,
    reminderTimes: List<Pair<Int, Int>>,
    onReminderTimesChange: (List<Pair<Int, Int>>) -> Unit,
    timingError: String?,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onFinish: () -> Unit
) {
    var unitExpanded by remember { mutableStateOf(false) }
    var formExpanded by remember { mutableStateOf(false) }
    var instExpanded by remember { mutableStateOf(false) }
    var freqExpanded by remember { mutableStateOf(false) }

    val weekdays = remember {
        (1..7).map { dayNum ->
            val display = try {
                java.time.DayOfWeek.of(dayNum).getDisplayName(java.time.format.TextStyle.NARROW, java.util.Locale.getDefault())
            } catch (_: Exception) {
                when (dayNum) {
                    1 -> "M"; 2 -> "T"; 3 -> "W"; 4 -> "T"; 5 -> "F"; 6 -> "S"; else -> "S"
                }
            }
            Pair(dayNum, display)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(stringResource(R.string.step_med_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            Text(
                stringResource(R.string.step_med_headline),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(R.string.step_med_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isDark) DarkTextSecondary else LightTextSecondary
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(stringResource(R.string.med_name_optional)) },
                placeholder = { Text("e.g. Metformin, Vitamin D") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = dosage,
                    onValueChange = onDosageChange,
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
                                    onUnitChange(u)
                                    unitExpanded = false
                                }
                            )
                        }
                    }
                }
            }

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
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = formExpanded,
                        onDismissRequest = { formExpanded = false }
                    ) {
                        forms.forEach { f ->
                            DropdownMenuItem(
                                text = { Text(f.replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    onFormChange(f)
                                    formExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = instExpanded,
                    onExpandedChange = { instExpanded = !instExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.med_instructions_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = instExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = instExpanded,
                        onDismissRequest = { instExpanded = false }
                    ) {
                        instructionOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    onInstructionsChange(opt)
                                    instExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // Cadence
            Text(stringResource(R.string.repeating_cadence), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = interval,
                    onValueChange = {
                        if (it.length <= 3 && (it.isEmpty() || it.all { c -> c.isDigit() })) {
                            onIntervalChange(it)
                        }
                    },
                    label = { Text(stringResource(R.string.cadence_every)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(0.8f)
                )

                ExposedDropdownMenuBox(
                    expanded = freqExpanded,
                    onExpandedChange = { freqExpanded = !freqExpanded },
                    modifier = Modifier.weight(1.2f)
                ) {
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.cadence_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = freqExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = freqExpanded,
                        onDismissRequest = { freqExpanded = false }
                    ) {
                        listOf("Day(s)", "Week(s)", "Month(s)").forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    onFrequencyChange(opt)
                                    freqExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (frequency == "Week(s)") {
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
                                    val updated = if (isSelected) {
                                        if (selectedDaysOfWeek.size > 1) selectedDaysOfWeek - dayNum else selectedDaysOfWeek
                                    } else {
                                        selectedDaysOfWeek + dayNum
                                    }
                                    onDaysOfWeekChange(updated)
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

            // N Times per Day
            Text(
                stringResource(R.string.dose_times_header, reminderTimes.size),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

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
                        onClick = { onReminderTimesChange(presetTimes) },
                        label = { Text(label, fontSize = 11.sp) }
                    )
                }
            }

            // List of Times
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

                        OutlinedTextField(
                            value = String.format(java.util.Locale.getDefault(), "%02d", timePair.first),
                            onValueChange = { newVal ->
                                val h = newVal.toIntOrNull()
                                if (newVal.isEmpty() || (h != null && h in 0..23)) {
                                    val updated = reminderTimes.toMutableList()
                                    updated[index] = Pair(h ?: 0, timePair.second)
                                    onReminderTimesChange(updated)
                                }
                            },
                            label = { Text(stringResource(R.string.hour_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        Text(":", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        OutlinedTextField(
                            value = String.format(java.util.Locale.getDefault(), "%02d", timePair.second),
                            onValueChange = { newVal ->
                                val m = newVal.toIntOrNull()
                                if (newVal.isEmpty() || (m != null && m in 0..59)) {
                                    val updated = reminderTimes.toMutableList()
                                    updated[index] = Pair(timePair.first, m ?: 0)
                                    onReminderTimesChange(updated)
                                }
                            },
                            label = { Text(stringResource(R.string.min_label)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        if (reminderTimes.size > 1) {
                            IconButton(
                                onClick = {
                                    onReminderTimesChange(reminderTimes.filterIndexed { i, _ -> i != index })
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

                TextButton(
                    onClick = {
                        val last = reminderTimes.lastOrNull() ?: Pair(8, 0)
                        val nextHour = (last.first + 4) % 24
                        onReminderTimesChange(reminderTimes + Pair(nextHour, 0))
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
                    timingError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) DarkPrimary else LightPrimary
                )
            ) {
                Text(
                    if (name.trim().isEmpty()) stringResource(R.string.btn_finish_setup) else stringResource(R.string.btn_save_and_start),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = onSkip,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_skip_med_later))
            }
        }
    }
}

@Composable
private fun ValuePropositionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isDark: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDark) DarkPrimary else LightPrimary,
                modifier = Modifier.size(20.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(description, fontSize = 12.sp, color = if (isDark) DarkTextSecondary else LightTextSecondary)
            }
        }
    }
}

@Composable
private fun PermissionItemCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    buttonText: String,
    onAction: () -> Unit,
    isDark: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isGranted) (if (isDark) DarkSuccess else LightSuccess) else (if (isDark) DarkPrimary else LightPrimary),
                    modifier = Modifier.size(20.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text(description, fontSize = 12.sp, color = if (isDark) DarkTextSecondary else LightTextSecondary)
                }
            }

            Spacer(Modifier.width(8.dp))

            if (isGranted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Granted",
                    tint = if (isDark) DarkSuccess else LightSuccess,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                OutlinedButton(
                    onClick = onAction,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder)
                ) {
                    Text(buttonText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
