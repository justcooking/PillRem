package com.pillrem.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pillrem.app.R
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
    val context = LocalContext.current
    val reasonResList = listOf(
        R.string.reason_fasting,
        R.string.reason_doctor,
        R.string.reason_nausea,
        R.string.reason_out_of_stock,
        R.string.reason_other
    )
    var selectedReasonRes by remember { mutableStateOf(reasonResList[0]) }
    var customReason by remember { mutableStateOf("") }

    val formattedDosage = com.pillrem.app.utils.DosageFormatter.formatWithUnit(
        item.medicine.dosageAmount,
        item.medicine.dosageUnit
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(14.dp),
        title = { Text(stringResource(R.string.dialog_skip_title), fontWeight = FontWeight.SemiBold, fontSize = 16.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    stringResource(R.string.dialog_skip_question, item.medicine.name, formattedDosage),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    stringResource(R.string.dialog_skip_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    stringResource(R.string.skip_reason_label),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                reasonResList.forEach { resId ->
                    val reasonText = stringResource(resId)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = selectedReasonRes == resId,
                            onClick = { selectedReasonRes = resId }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            reasonText,
                            modifier = Modifier.padding(top = 12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (selectedReasonRes == R.string.reason_other) {
                    OutlinedTextField(
                        value = customReason,
                        onValueChange = { customReason = it },
                        label = { Text(stringResource(R.string.specify_reason)) },
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
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp),
                onClick = {
                    coroutineScope.launch {
                        val finalReason = if (selectedReasonRes == R.string.reason_other) {
                            customReason.ifBlank { context.getString(R.string.reason_other) }
                        } else {
                            context.getString(selectedReasonRes)
                        }
                        doseEngine.skipDose(item.dose.id, reason = finalReason)
                        onSkipped()
                        onDismiss()
                    }
                }
            ) {
                Text(stringResource(R.string.btn_confirm_skip), fontSize = 13.sp, fontWeight = FontWeight.Medium)
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
