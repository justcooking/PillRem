package com.pillrem.app.data.local.dao

import androidx.room.*
import com.pillrem.app.data.local.entities.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines WHERE profileId = :profileId AND isArchived = 0 ORDER BY name ASC")
    fun getActiveMedicines(profileId: Long): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE profileId = :profileId AND isArchived = 0 ORDER BY name ASC")
    suspend fun getActiveMedicinesSync(profileId: Long): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE profileId = :profileId AND isArchived = 1 ORDER BY name ASC")
    fun getArchivedMedicines(profileId: Long): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE id = :id LIMIT 1")
    suspend fun getMedicineById(id: Long): MedicineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: MedicineEntity): Long

    @Update
    suspend fun updateMedicine(medicine: MedicineEntity)

    @Query("UPDATE medicines SET isArchived = 1 WHERE id = :medicineId")
    suspend fun archiveMedicine(medicineId: Long)

    @Query("UPDATE medicines SET isArchived = 0 WHERE id = :medicineId")
    suspend fun restoreMedicine(medicineId: Long)

    @Query("UPDATE medicines SET stockCount = CASE WHEN stockCount >= :amount THEN stockCount - :amount ELSE 0 END WHERE id = :medicineId")
    suspend fun decrementStock(medicineId: Long, amount: Int = 1)

    @Query("UPDATE medicines SET stockCount = :newStock WHERE id = :medicineId")
    suspend fun updateStock(medicineId: Long, newStock: Int)

    @Delete
    suspend fun deleteMedicine(medicine: MedicineEntity)
}
