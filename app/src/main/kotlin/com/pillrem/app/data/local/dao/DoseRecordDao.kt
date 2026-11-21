package com.pillrem.app.data.local.dao

import androidx.room.*
import com.pillrem.app.data.local.entities.DoseRecordEntity
import com.pillrem.app.data.models.DoseWithMedicine
import kotlinx.coroutines.flow.Flow

@Dao
interface DoseRecordDao {
    @Transaction
    @Query("SELECT * FROM dose_records WHERE profileId = :profileId AND scheduledAt >= :start AND scheduledAt <= :end ORDER BY scheduledAt ASC")
    fun getDosesForProfileRange(profileId: Long, start: Long, end: Long): Flow<List<DoseWithMedicine>>

    @Transaction
    @Query("SELECT * FROM dose_records WHERE profileId = :profileId AND scheduledAt >= :start AND scheduledAt <= :end ORDER BY scheduledAt ASC")
    suspend fun getDosesForProfileRangeSync(profileId: Long, start: Long, end: Long): List<DoseWithMedicine>

    @Transaction
    @Query("SELECT * FROM dose_records WHERE id = :doseId LIMIT 1")
    suspend fun getDoseWithMedicineById(doseId: Long): DoseWithMedicine?

    @Query("SELECT * FROM dose_records WHERE id = :doseId LIMIT 1")
    suspend fun getDoseById(doseId: Long): DoseRecordEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDoseIdempotent(dose: DoseRecordEntity): Long

    @Update
    suspend fun updateDose(dose: DoseRecordEntity)

    @Query("UPDATE dose_records SET status = :status, takenAt = :takenAt, updatedAt = :updatedAt WHERE id = :doseId")
    suspend fun markDoseTaken(doseId: Long, status: String, takenAt: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dose_records SET status = :status, reminderAt = :reminderAt, snoozeCount = snoozeCount + 1, updatedAt = :updatedAt WHERE id = :doseId")
    suspend fun snoozeDose(doseId: Long, status: String, reminderAt: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dose_records SET status = :status, skippedAt = :skippedAt, skipReason = :skipReason, updatedAt = :updatedAt WHERE id = :doseId")
    suspend fun skipDose(doseId: Long, status: String, skippedAt: Long, skipReason: String?, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE dose_records SET status = 'missed', updatedAt = :updatedAt WHERE (status = 'pending' OR status = 'snoozed') AND gracePeriodDeadline <= :cutoff")
    suspend fun autoMarkMissedDoses(cutoff: Long, updatedAt: Long = System.currentTimeMillis()): Int

    @Query("SELECT * FROM dose_records WHERE (status = 'pending' OR status = 'snoozed') AND gracePeriodDeadline <= :cutoff")
    suspend fun getUnresolvedExpiredDoses(cutoff: Long): List<DoseRecordEntity>

    @Query("SELECT * FROM dose_records WHERE profileId = :profileId AND scheduledAt >= :since ORDER BY scheduledAt DESC")
    suspend fun getDoseHistoryForProfile(profileId: Long, since: Long): List<DoseRecordEntity>

    @Query("SELECT * FROM dose_records")
    suspend fun getAllDosesSync(): List<DoseRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllDoses(doses: List<DoseRecordEntity>)

    @Query("SELECT * FROM dose_records WHERE profileId = :profileId ORDER BY scheduledAt DESC")
    suspend fun getAllDoseHistoryForProfile(profileId: Long): List<DoseRecordEntity>
}
