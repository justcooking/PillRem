package com.pillrem.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.pillrem.app.R

enum class NavItem(@StringRes val titleRes: Int, val icon: ImageVector) {
    TODAY(R.string.nav_today, Icons.Default.CheckCircle),
    MEDICINES(R.string.nav_medicines, Icons.Default.Medication),
    HISTORY(R.string.nav_history, Icons.Default.History),
    SETTINGS(R.string.nav_settings, Icons.Default.Settings)
}
