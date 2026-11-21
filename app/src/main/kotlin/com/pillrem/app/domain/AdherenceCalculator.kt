package com.pillrem.app.domain

import com.pillrem.app.data.local.entities.DoseRecordEntity
import com.pillrem.app.data.models.DoseStatus

data class AdherenceStats(
    val totalScheduled: Int,
    val takenCount: Int,
    val missedCount: Int,
    val skippedCount: Int,
    val pendingCount: Int,
    val adherenceRate: Float // 0.0f .. 100.0f
)

object AdherenceCalculator {
    fun calculate(doses: List<DoseRecordEntity>): AdherenceStats {
        var taken = 0
        var missed = 0
        var skipped = 0
        var pending = 0

        for (dose in doses) {
            when (DoseStatus.fromString(dose.status)) {
                DoseStatus.TAKEN -> taken++
                DoseStatus.MISSED -> missed++
                DoseStatus.SKIPPED -> skipped++
                DoseStatus.PENDING, DoseStatus.SNOOZED -> pending++
            }
        }

        val evaluable = taken + missed
        val rate = if (evaluable > 0) {
            (taken.toFloat() / evaluable.toFloat()) * 100.0f
        } else {
            100.0f
        }

        return AdherenceStats(
            totalScheduled = doses.size,
            takenCount = taken,
            missedCount = missed,
            skippedCount = skipped,
            pendingCount = pending,
            adherenceRate = rate
        )
    }
}
