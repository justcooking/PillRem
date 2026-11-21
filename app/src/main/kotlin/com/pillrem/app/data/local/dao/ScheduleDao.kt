package com.pillrem.app.data.local.dao

import androidx.room.*
import com.pillrem.app.data.local.entities.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE profileId = :profileId AND isActive = 1")
    fun getActiveSchedulesForProfile(profileId: Long): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE isActive = 1")
    suspend fun getAllActiveSchedulesSync(): List<ScheduleEntity>

    @Query("SELECT * FROM schedules WHERE medicineId = :medicineId")
    fun getSchedulesForMedicine(medicineId: Long): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE id = :id LIMIT 1")
    suspend fun getScheduleById(id: Long): ScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Update
    suspend fun updateSchedule(schedule: ScheduleEntity)

    @Query("UPDATE schedules SET isActive = :isActive WHERE id = :scheduleId")
    suspend fun toggleScheduleActive(scheduleId: Long, isActive: Boolean)

    @Query("SELECT * FROM schedules")
    suspend fun getAllSchedulesSync(): List<ScheduleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSchedules(schedules: List<ScheduleEntity>)

    @Query("DELETE FROM schedules WHERE id = :scheduleId")
    suspend fun deleteSchedule(scheduleId: Long)
}
