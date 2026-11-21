package com.pillrem.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pillrem.app.data.models.DoseWithMedicine
import com.pillrem.app.domain.DoseEngine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CorrectMissedDialog(
    item: DoseWithMedicine,
    doseEngine: DoseEngine,
    onDismiss: () -> Unit,
    onCorrected: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val timeFormat = SimpleDateFormat("HH:mm, dd MMM", Locale.getDefault())
    val scheduledTimeStr = timeFormat.format(Date(item.dose.scheduledAt))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Correct Missed Dose") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Did you take ${item.medicine.name} (${item.medicine.dosageAmount.toInt()} ${item.medicine.dosageUnit})?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Scheduled for: $scheduledTimeStr",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "This will change the status from Missed to Taken and adjust your adherence score accordingly.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        doseEngine.correctMissedDose(item.dose.id, System.currentTimeMillis())
                        onCorrected()
                        onDismiss()
                    }
                }
            ) {
                Text("Mark as Taken")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
