package com.pillrem.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dose_records",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicineEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicineId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ScheduleEntity::class,
            parentColumns = ["id"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["scheduleId", "scheduledAt"], unique = true),
        Index(value = ["profileId", "scheduledAt", "status"]),
        Index(value = ["medicineId"])
    ]
)
data class DoseRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val medicineId: Long,
    val scheduleId: Long? = null,
    val scheduledAt: Long,
    val reminderAt: Long,
    val status: String = "pending",
    val takenAt: Long? = null,
    val skippedAt: Long? = null,
    val skipReason: String? = null,
    val snoozeCount: Int = 0,
    val gracePeriodDeadline: Long,
    val notificationId: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
