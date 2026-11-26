package com.pillrem.app.data.models

enum class ScheduleType(val value: String, val label: String) {
    DAILY("daily", "Every Day"),
    SPECIFIC_DAYS("specific_days", "Specific Days"),
    INTERVAL_DAYS("interval_days", "Day Interval"),
    WEEKLY("weekly", "Weekly"),
    MONTHLY("monthly", "Monthly"),
    AS_NEEDED("as_needed", "As Needed");

    companion object {
        fun fromString(value: String): ScheduleType {
            return entries.firstOrNull { it.value.equals(value, ignoreCase = true) } ?: DAILY
        }
    }
}
