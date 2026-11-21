package com.pillrem.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.data.models.DoseWithMedicine
import com.pillrem.app.domain.DoseEngine
import kotlinx.coroutines.launch

@Composable
fun SkipDoseDialog(
    item: DoseWithMedicine,
    doseEngine: DoseEngine,
    onDismiss: () -> Unit,
    onSkipped: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val commonReasons = listOf(
        "Fasting for medical test",
        "Doctor advised to pause",
        "Nausea / feeling unwell",
        "Medication out of stock",
        "Other"
    )
    var selectedReason by remember { mutableStateOf(commonReasons[0]) }
    var customReason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Skip Dose Confirmation") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Are you sure you want to skip ${item.medicine.name} (${com.pillrem.app.utils.DosageFormatter.formatWithUnit(item.medicine.dosageAmount, item.medicine.dosageUnit)})?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Skipped doses are excluded from adherence penalty calculations.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text("Reason for skipping (optional):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

                commonReasons.forEach { reason ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = selectedReason == reason,
                            onClick = { selectedReason = reason }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            reason,
                            modifier = Modifier.padding(top = 12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (selectedReason == "Other") {
                    OutlinedTextField(
                        value = customReason,
                        onValueChange = { customReason = it },
                        label = { Text("Specify reason") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                onClick = {
                    coroutineScope.launch {
                        val finalReason = if (selectedReason == "Other") customReason.ifBlank { "Other" } else selectedReason
                        doseEngine.skipDose(item.dose.id, reason = finalReason)
                        onSkipped()
                        onDismiss()
                    }
                }
            ) {
                Text("Confirm Skip")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
