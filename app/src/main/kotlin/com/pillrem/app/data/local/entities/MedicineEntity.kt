package com.pillrem.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medicines",
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
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val name: String,
    val dosageAmount: Double = 1.0,
    val dosageUnit: String = "tablet",
    val form: String = "tablet",
    val instructions: String? = null,
    val colorTag: Int = 0xFF2B6CB0.toInt(),
    val stockCount: Int = 0,
    val lowStockThreshold: Int? = null,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
