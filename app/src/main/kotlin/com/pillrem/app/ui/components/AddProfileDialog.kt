package com.pillrem.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pillrem.app.data.local.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun AddProfileDialog(
    db: AppDatabase,
    onDismiss: () -> Unit,
    onProfileAdded: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(0xFF2B6CB0.toInt()) }
    var gracePeriodMinutes by remember { mutableStateOf("60") }
    var snoozeMinutes by remember { mutableStateOf("15") }

    val colors = listOf(
        0xFF2B6CB0.toInt(), // Clinical Steel Blue
        0xFF388E67.toInt(), // Clinical Green
        0xFF7C3AED.toInt(), // Purple
        0xFFD97706.toInt(), // Amber
        0xFFDB2777.toInt(), // Rose Pink
        0xFF0284C7.toInt()  // Cyan
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Profile") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Profile Name (e.g. Grandma)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Profile Avatar Color", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    colors.forEach { colorInt ->
                        val isSelected = selectedColor == colorInt
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(colorInt))
                                .clickable { selectedColor = colorInt }
                                .then(
                                    if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    else Modifier
                                )
                        )
                    }
                }

                OutlinedTextField(
                    value = gracePeriodMinutes,
                    onValueChange = { gracePeriodMinutes = it },
                    label = { Text("Grace Period (Minutes)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = snoozeMinutes,
                    onValueChange = { snoozeMinutes = it },
                    label = { Text("Default Snooze (Minutes)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        coroutineScope.launch {
                            val grace = gracePeriodMinutes.toIntOrNull() ?: 60
                            val snooze = snoozeMinutes.toIntOrNull() ?: 15
                            db.profileDao().createProfileWithSettings(
                                name = name.trim(),
                                avatarColor = selectedColor,
                                gracePeriodMinutes = grace,
                                defaultSnoozeMinutes = snooze
                            )
                            onProfileAdded()
                            onDismiss()
                        }
                    }
                }
            ) {
                Text("Create Profile")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
