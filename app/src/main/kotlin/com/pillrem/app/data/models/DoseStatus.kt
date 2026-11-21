package com.pillrem.app.data.models

enum class DoseStatus(val value: String, val label: String) {
    PENDING("pending", "Pending"),
    TAKEN("taken", "Taken"),
    SNOOZED("snoozed", "Snoozed"),
    MISSED("missed", "Missed"),
    SKIPPED("skipped", "Skipped");

    val isPending: Boolean get() = this == PENDING
    val isTaken: Boolean get() = this == TAKEN
    val isSnoozed: Boolean get() = this == SNOOZED
    val isMissed: Boolean get() = this == MISSED
    val isSkipped: Boolean get() = this == SKIPPED
    val isResolved: Boolean get() = isTaken || isMissed || isSkipped

    companion object {
        fun fromString(value: String): DoseStatus {
            return entries.firstOrNull { it.value.equals(value, ignoreCase = true) } ?: PENDING
        }
    }
}
