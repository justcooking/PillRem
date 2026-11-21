package com.pillrem.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val avatarColor: Int = 0xFF2B6CB0.toInt(),
    val avatarIcon: String = "person",
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false
)

@Entity(
    tableName = "profile_settings",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["profileId"])]
)
data class ProfileSettingsEntity(
    @PrimaryKey
    val profileId: Long,
    val gracePeriodMinutes: Int = 60,
    val defaultSnoozeMinutes: Int = 15,
    val notificationSound: String = "clinical_chime",
    val vibrate: Boolean = true
)
