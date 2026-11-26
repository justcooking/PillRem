package com.pillrem.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.R
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
    val formattedDosage = com.pillrem.app.utils.DosageFormatter.formatWithUnit(
        item.medicine.dosageAmount,
        item.medicine.dosageUnit
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(14.dp),
        title = { Text(stringResource(R.string.dialog_correct_title), fontWeight = FontWeight.SemiBold, fontSize = 16.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    stringResource(R.string.dialog_correct_question, item.medicine.name, formattedDosage),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    stringResource(R.string.scheduled_for, scheduledTimeStr),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    stringResource(R.string.dialog_correct_desc),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Button(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp),
                onClick = {
                    coroutineScope.launch {
                        doseEngine.correctMissedDose(item.dose.id, System.currentTimeMillis())
                        onCorrected()
                        onDismiss()
                    }
                }
            ) {
                Text(stringResource(R.string.btn_mark_as_taken), fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(stringResource(R.string.btn_cancel), fontSize = 13.sp)
            }
        }
    )
}
