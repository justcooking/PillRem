package com.pillrem.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavItem(val title: String, val icon: ImageVector) {
    TODAY("Today", Icons.Default.CheckCircle),
    MEDICINES("Cabinet", Icons.Default.Medication),
    HISTORY("History", Icons.Default.History),
    SETTINGS("Settings", Icons.Default.Settings)
}
