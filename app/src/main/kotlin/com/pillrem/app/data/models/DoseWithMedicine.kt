package com.pillrem.app.data.models

import androidx.room.Embedded
import androidx.room.Relation
import com.pillrem.app.data.local.entities.DoseRecordEntity
import com.pillrem.app.data.local.entities.MedicineEntity

data class DoseWithMedicine(
    @Embedded
    val dose: DoseRecordEntity,
    @Relation(
        parentColumn = "medicineId",
        entityColumn = "id"
    )
    val medicine: MedicineEntity
)
