package com.pillrem.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.models.DoseWithMedicine
import com.pillrem.app.domain.AdherenceCalculator
import com.pillrem.app.domain.DoseEngine
import com.pillrem.app.ui.components.PermissionBanner
import com.pillrem.app.ui.navigation.NavItem
import com.pillrem.app.ui.screens.HistoryScreen
import com.pillrem.app.ui.screens.MedicinesScreen
import com.pillrem.app.ui.screens.SettingsScreen
import com.pillrem.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(this)
        val doseEngine = DoseEngine(db)

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            PillRemTheme(darkTheme = isDarkTheme) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppHost(
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
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
                            "PillRem",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        activeProfile?.let {
                            Text(
                                "Profile: ${it.name}",
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
                            contentDescription = "Toggle Theme"
                        )
                    }

                    Box {
                        IconButton(onClick = { showProfileMenu = true }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Switch Profile")
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
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(profile.avatarColor)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    profile.name.take(1),
                                                    color = Color.White,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            Text(profile.name)
                                            if (profile.isActive) {
                                                Spacer(Modifier.width(8.dp))
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = "Active",
                                                    tint = LightPrimary,
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
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentNav == item,
                        onClick = { currentNav = item },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = activeColor,
                            selectedTextColor = activeColor,
                            indicatorColor = activeColor.copy(alpha = 0.15f)
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
                    isDarkTheme = isDarkTheme
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
                    isDark = isDarkTheme,
                    onToggleTheme = onToggleTheme
                )
            }
        }
    }
}

@Composable
fun TodayTabContent(
    activeId: Long,
    db: AppDatabase,
    doseEngine: DoseEngine,
    isDarkTheme: Boolean
) {
    val coroutineScope = rememberCoroutineScope()

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
                    "Today's Medication Doses",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${doses.size} scheduled",
                    fontSize = 13.sp,
                    color = if (isDarkTheme) DarkTextSecondary else LightTextSecondary
                )
            }
        }

        if (doses.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No doses scheduled for today.",
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
                    onSnooze = {
                        coroutineScope.launch { doseEngine.snoozeDose(item.dose.id, 15) }
                    },
                    onSkip = {
                        coroutineScope.launch { doseEngine.skipDose(item.dose.id, "Patient skipped") }
                    }
                )
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "PillRem Native Android • Kotlin & Compose • SQLite Room",
                    fontSize = 11.sp,
                    color = if (isDarkTheme) DarkTextSecondary else LightTextSecondary
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdherenceBanner(stats: com.pillrem.app.domain.AdherenceStats, isDark: Boolean) {
    val successColor = if (isDark) DarkSuccess else LightSuccess
    val dangerColor = if (isDark) DarkDanger else LightDanger
    val neutralColor = if (isDark) DarkNeutral else LightNeutral
    val warningColor = if (isDark) DarkWarning else LightWarning

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Adherence Score", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            (if (stats.adherenceRate >= 80f) successColor else warningColor).copy(alpha = 0.15f)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        "${stats.adherenceRate.toInt()}%",
                        fontWeight = FontWeight.Bold,
                        color = if (stats.adherenceRate >= 80f) successColor else warningColor
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatColumn(label = "Taken", count = stats.takenCount.toString(), color = successColor)
                StatColumn(label = "Missed", count = stats.missedCount.toString(), color = dangerColor)
                StatColumn(label = "Skipped", count = stats.skippedCount.toString(), color = neutralColor)
            }
        }
    }
}

@Composable
fun StatColumn(label: String, count: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun DoseCard(
    item: DoseWithMedicine,
    isDark: Boolean,
    onTake: () -> Unit,
    onSnooze: () -> Unit,
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
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Medication,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(med.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        "${med.dosageAmount.toInt()} ${med.dosageUnit} • ${med.instructions ?: ""}",
                        fontSize = 13.sp,
                        color = if (isDark) DarkTextSecondary else LightTextSecondary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(timeStr, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        "Stock: ${med.stockCount}",
                        fontSize = 11.sp,
                        color = if (med.stockCount <= 7) LightWarning else Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        dose.status.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }

                if (dose.snoozeCount > 0) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Snoozed ${dose.snoozeCount}x",
                        fontSize = 11.sp,
                        color = LightWarning
                    )
                }

                Spacer(Modifier.weight(1f))

                if (dose.status != "taken" && dose.status != "skipped") {
                    TextButton(onClick = onSnooze) {
                        Text("Snooze", fontSize = 13.sp)
                    }
                    TextButton(onClick = onSkip) {
                        Text("Skip", fontSize = 13.sp)
                    }
                    Button(
                        onClick = onTake,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) DarkPrimary else LightPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Take", fontSize = 13.sp)
                    }
                } else if (dose.status == "taken") {
                    val takenStr = dose.takenAt?.let { timeFormat.format(Date(it)) } ?: ""
                    Text(
                        "Taken at $takenStr",
                        fontSize = 12.sp,
                        color = if (isDark) DarkSuccess else LightSuccess,
                        fontWeight = FontWeight.Medium
                    )
                } else if (dose.status == "skipped") {
                    Text(
                        "Skipped: ${dose.skipReason ?: "Confirmed"}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
