package com.pillrem.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.R
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.models.DoseWithMedicine
import com.pillrem.app.data.update.UpdateManager
import com.pillrem.app.data.update.UpdateState
import com.pillrem.app.domain.AdherenceCalculator
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.ui.components.PermissionBanner
import com.pillrem.app.ui.components.SkipDoseDialog
import com.pillrem.app.ui.components.UpdateDialog
import com.pillrem.app.ui.navigation.NavItem
import com.pillrem.app.ui.screens.HistoryScreen
import com.pillrem.app.ui.screens.MedicinesScreen
import com.pillrem.app.ui.screens.OnboardingScreen
import com.pillrem.app.ui.screens.SettingsScreen
import com.pillrem.app.ui.theme.*
import com.pillrem.app.utils.LocaleHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val language = LocaleHelper.getSavedLanguage(newBase)
        super.attachBaseContext(LocaleHelper.createLocalizedContext(newBase, language))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(this)
        val doseEngine = DoseEngine(db)

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            val profiles by db.profileDao().getAllProfiles().collectAsState(initial = null)

            PillRemTheme(darkTheme = isDarkTheme) {
                when {
                    profiles == null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = if (isDarkTheme) DarkPrimary else LightPrimary
                            )
                        }
                    }
                    profiles!!.isEmpty() -> {
                        OnboardingScreen(
                            db = db,
                            doseEngine = doseEngine,
                            isDarkTheme = isDarkTheme,
                            onComplete = {
                                // Flow emission from profile creation triggers transition to MainAppHost
                            }
                        )
                    }
                    else -> {
                        MainAppHost(
                            db = db,
                            doseEngine = doseEngine,
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = { isDarkTheme = !isDarkTheme }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppHost(
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val updateManager = remember { UpdateManager(context.applicationContext) }
    val updateState by updateManager.updateState.collectAsState()
    var showUpdateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        updateManager.checkForUpdates(silent = true)
    }

    var currentNav by remember { mutableStateOf(NavItem.TODAY) }

    val profiles by db.profileDao().getAllProfiles().collectAsState(initial = emptyList())
    val activeProfile by db.profileDao().getActiveProfile().collectAsState(initial = null)
    var showProfileMenu by remember { mutableStateOf(false) }

    val activeId = activeProfile?.id ?: 0L
    val activeColor = if (isDarkTheme) DarkPrimary else LightPrimary

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            stringResource(R.string.app_name),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        activeProfile?.let {
                            Text(
                                stringResource(R.string.profile_prefix, it.name),
                                fontSize = 12.sp,
                                color = if (isDarkTheme) DarkTextSecondary else LightTextSecondary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = stringResource(R.string.toggle_theme),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box {
                        IconButton(onClick = { showProfileMenu = true }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = stringResource(R.string.switch_profile), modifier = Modifier.size(22.dp))
                        }

                        DropdownMenu(
                            expanded = showProfileMenu,
                            onDismissRequest = { showProfileMenu = false }
                        ) {
                            profiles.forEach { profile ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(profile.avatarColor)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    profile.name.take(1),
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            Text(profile.name, fontSize = 14.sp)
                                            if (profile.isActive) {
                                                Spacer(Modifier.width(8.dp))
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = "Active",
                                                    tint = if (isDarkTheme) DarkPrimary else LightPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        showProfileMenu = false
                                        coroutineScope.launch {
                                            db.profileDao().setActiveProfile(profile.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = activeColor
            ) {
                NavItem.entries.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = stringResource(item.titleRes), modifier = Modifier.size(20.dp)) },
                        label = { Text(stringResource(item.titleRes), fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        selected = currentNav == item,
                        onClick = { currentNav = item },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = activeColor,
                            selectedTextColor = activeColor,
                            indicatorColor = activeColor.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentNav) {
                NavItem.TODAY -> TodayTabContent(
                    activeId = activeId,
                    db = db,
                    doseEngine = doseEngine,
                    isDarkTheme = isDarkTheme,
                    updateState = updateState,
                    onShowUpdate = { showUpdateDialog = true }
                )
                NavItem.MEDICINES -> MedicinesScreen(
                    profileId = activeId,
                    db = db,
                    doseEngine = doseEngine,
                    isDark = isDarkTheme
                )
                NavItem.HISTORY -> HistoryScreen(
                    profileId = activeId,
                    db = db,
                    doseEngine = doseEngine,
                    isDark = isDarkTheme
                )
                NavItem.SETTINGS -> SettingsScreen(
                    db = db,
                    doseEngine = doseEngine,
                    isDark = isDarkTheme,
                    onToggleTheme = onToggleTheme,
                    updateManager = updateManager
                )
            }
        }

        if (showUpdateDialog || updateState is UpdateState.Downloading || updateState is UpdateState.ReadyToInstall || updateState is UpdateState.UpToDate || updateState is UpdateState.Error) {
            UpdateDialog(
                updateState = updateState,
                onDismiss = {
                    showUpdateDialog = false
                    updateManager.resetState()
                },
                onStartDownload = { releaseInfo ->
                    coroutineScope.launch {
                        updateManager.downloadAndPrepareUpdate(releaseInfo)
                    }
                },
                onInstall = { apkPath ->
                    updateManager.launchPackageInstaller(apkPath)
                }
            )
        }
    }
}

@Composable
fun TodayTabContent(
    activeId: Long,
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDarkTheme: Boolean,
    updateState: UpdateState,
    onShowUpdate: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var doseToSkip by remember { mutableStateOf<DoseWithMedicine?>(null) }

    // Start & End of today
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startOfToday = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val endOfToday = calendar.timeInMillis - 1

    val doses by db.doseRecordDao().getDosesForProfileRange(activeId, startOfToday, endOfToday)
        .collectAsState(initial = emptyList())

    val allHistory by produceState<List<com.pillrem.app.data.local.entities.DoseRecordEntity>>(initialValue = emptyList(), activeId, doses) {
        value = if (activeId != 0L) db.doseRecordDao().getAllDoseHistoryForProfile(activeId) else emptyList()
    }
    val adherenceStats = remember(allHistory) {
        AdherenceCalculator.calculate(allHistory)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (updateState is UpdateState.UpdateAvailable) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) DarkSurface else LightSurface
                    ),
                    border = BorderStroke(1.dp, if (isDarkTheme) DarkBorder else LightBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.SystemUpdate,
                            contentDescription = null,
                            tint = if (isDarkTheme) DarkPrimary else LightPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.update_available_title, updateState.releaseInfo.tagName),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                stringResource(R.string.update_available_subtitle),
                                fontSize = 12.sp,
                                color = if (isDarkTheme) DarkTextSecondary else LightTextSecondary
                            )
                        }
                        Button(
                            onClick = onShowUpdate,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            modifier = Modifier.height(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDarkTheme) DarkPrimary else LightPrimary
                            )
                        ) {
                            Text(stringResource(R.string.btn_update), fontSize = 12.sp, color = if (isDarkTheme) DarkBackground else Color.White)
                        }
                    }
                }
            }
        }

        item {
            PermissionBanner(isDark = isDarkTheme)
        }

        item {
            AdherenceBanner(stats = adherenceStats, isDark = isDarkTheme)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.todays_medications),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    stringResource(R.string.doses_scheduled_count, doses.size),
                    fontSize = 12.sp,
                    color = if (isDarkTheme) DarkTextSecondary else LightTextSecondary
                )
            }
        }

        if (doses.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, if (isDarkTheme) DarkBorder else LightBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(R.string.no_doses_today),
                            fontSize = 13.sp,
                            color = if (isDarkTheme) DarkTextSecondary else LightTextSecondary
                        )
                    }
                }
            }
        } else {
            items(doses, key = { it.dose.id }) { item ->
                DoseCard(
                    item = item,
                    isDark = isDarkTheme,
                    onTake = {
                        coroutineScope.launch { doseEngine.takeDose(item.dose.id) }
                    },
                    onSkip = {
                        doseToSkip = item
                    }
                )
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
        }
    }

    doseToSkip?.let { targetDose ->
        SkipDoseDialog(
            item = targetDose,
            doseEngine = doseEngine,
            onDismiss = { doseToSkip = null },
            onSkipped = { doseToSkip = null }
        )
    }
}

@Composable
fun AdherenceBanner(stats: com.pillrem.app.domain.AdherenceStats, isDark: Boolean) {
    val successColor = if (isDark) DarkSuccess else LightSuccess
    val dangerColor = if (isDark) DarkDanger else LightDanger
    val neutralColor = if (isDark) DarkNeutral else LightNeutral
    val warningColor = if (isDark) DarkWarning else LightWarning
    val borderColor = if (isDark) DarkBorder else LightBorder

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.adherence_score),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${stats.adherenceRate.toInt()}%",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = if (stats.adherenceRate >= 80f) successColor else warningColor
                )
            }

            HorizontalDivider(color = borderColor, thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatColumn(label = stringResource(R.string.stat_taken), count = stats.takenCount.toString(), color = successColor)
                StatColumn(label = stringResource(R.string.stat_missed), count = stats.missedCount.toString(), color = dangerColor)
                StatColumn(label = stringResource(R.string.stat_skipped), count = stats.skippedCount.toString(), color = neutralColor)
            }
        }
    }
}

@Composable
fun StatColumn(label: String, count: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(count, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = color)
        Text(label, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun DoseCard(
    item: DoseWithMedicine,
    isDark: Boolean,
    onTake: () -> Unit,
    onSkip: () -> Unit
) {
    val dose = item.dose
    val med = item.medicine
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeStr = timeFormat.format(Date(dose.scheduledAt))

    val statusColor = when (dose.status) {
        "taken" -> if (isDark) DarkSuccess else LightSuccess
        "missed" -> if (isDark) DarkDanger else LightDanger
        "skipped" -> if (isDark) DarkNeutral else LightNeutral
        else -> if (isDark) DarkPrimary else LightPrimary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Quiet status dot instead of colored circular wash
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        med.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${com.pillrem.app.utils.DosageFormatter.formatWithUnit(med.dosageAmount, med.dosageUnit)}${if (!med.instructions.isNullOrBlank()) " • " + med.instructions else ""}",
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                }

                Text(
                    timeStr,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val statusText = when (dose.status) {
                    "taken" -> stringResource(R.string.stat_taken)
                    "missed" -> stringResource(R.string.stat_missed)
                    "skipped" -> stringResource(R.string.stat_skipped)
                    else -> "Pending"
                }

                Text(
                    statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = statusColor
                )

                if (dose.snoozeCount > 0) {
                    Text(
                        " • ${stringResource(R.string.status_snoozed_count, dose.snoozeCount)}",
                        fontSize = 12.sp,
                        color = LightWarning
                    )
                }

                Spacer(Modifier.weight(1f))

                if (dose.status != "taken" && dose.status != "skipped") {
                    TextButton(
                        onClick = onSkip,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            stringResource(R.string.action_skip),
                            fontSize = 12.sp,
                            color = if (isDark) DarkTextSecondary else LightTextSecondary
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Button(
                        onClick = onTake,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) DarkPrimary else LightPrimary
                        )
                    ) {
                        Text(
                            stringResource(R.string.action_take),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) DarkBackground else Color.White
                        )
                    }
                } else if (dose.status == "taken") {
                    val takenStr = dose.takenAt?.let { timeFormat.format(Date(it)) } ?: ""
                    Text(
                        stringResource(R.string.status_taken_at, takenStr),
                        fontSize = 12.sp,
                        color = if (isDark) DarkSuccess else LightSuccess,
                        fontWeight = FontWeight.Normal
                    )
                } else if (dose.status == "skipped") {
                    Text(
                        stringResource(R.string.status_skipped_reason, dose.skipReason ?: ""),
                        fontSize = 12.sp,
                        color = if (isDark) DarkNeutral else LightNeutral
                    )
                }
            }
        }
    }
}
