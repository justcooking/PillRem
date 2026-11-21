package com.pillrem.app.data.local.dao

import androidx.room.*
import com.pillrem.app.data.local.entities.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("""
        SELECT s.* FROM schedules s
        INNER JOIN medicines m ON s.medicineId = m.id
        WHERE s.profileId = :profileId AND s.isActive = 1 AND m.isArchived = 0
    """)
    fun getActiveSchedulesForProfile(profileId: Long): Flow<List<ScheduleEntity>>

    @Query("""
        SELECT s.* FROM schedules s
        INNER JOIN medicines m ON s.medicineId = m.id
        WHERE s.isActive = 1 AND m.isArchived = 0
    """)
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
