package com.pillrem.app.utils

import java.util.Locale

object DosageFormatter {

    /**
     * Formats a dosage amount cleanly:
     * - Whole numbers like 1.0 or 2.0 -> "1", "2"
     * - Fractional numbers like 0.5 or 1.5 -> "0.5", "1.5"
     * - Fractions like 0.25 -> "0.25"
     */
    fun format(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            amount.toLong().toString()
        } else {
            String.format(Locale.US, "%.2f", amount)
                .trimEnd('0')
                .trimEnd('.')
        }
    }

    /**
     * Formats dosage amount with unit:
     * e.g. "0.5 tablet", "1 capsule", "2.5 ml"
     */
    fun formatWithUnit(amount: Double, unit: String): String {
        val formattedAmount = format(amount)
        return "$formattedAmount $unit".trim()
    }
}
