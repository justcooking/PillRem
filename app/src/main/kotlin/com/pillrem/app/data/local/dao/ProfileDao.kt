package com.pillrem.app.data.local.dao

import androidx.room.*
import com.pillrem.app.data.local.entities.ProfileEntity
import com.pillrem.app.data.local.entities.ProfileSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY createdAt ASC")
    fun getAllProfiles(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profiles ORDER BY createdAt ASC")
    suspend fun getAllProfilesSync(): List<ProfileEntity>

    @Query("SELECT * FROM profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveProfile(): Flow<ProfileEntity?>

    @Query("SELECT * FROM profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveProfileSync(): ProfileEntity?

    @Query("SELECT * FROM profile_settings WHERE profileId = :profileId LIMIT 1")
    fun getSettingsForProfile(profileId: Long): Flow<ProfileSettingsEntity?>

    @Query("SELECT * FROM profile_settings WHERE profileId = :profileId LIMIT 1")
    suspend fun getSettingsForProfileSync(profileId: Long): ProfileSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: ProfileSettingsEntity)

    @Update
    suspend fun updateProfile(profile: ProfileEntity)

    @Update
    suspend fun updateSettings(settings: ProfileSettingsEntity)

    @Query("UPDATE profiles SET isActive = 0")
    suspend fun deactivateAllProfiles()

    @Query("UPDATE profiles SET isActive = 1 WHERE id = :profileId")
    suspend fun activateProfile(profileId: Long)

    @Transaction
    suspend fun createProfileWithSettings(
        name: String,
        avatarColor: Int,
        avatarIcon: String = "person",
        gracePeriodMinutes: Int = 60,
        defaultSnoozeMinutes: Int = 15
    ): Long {
        val existing = getAllProfilesSync()
        val isFirst = existing.isEmpty()
        val profileId = insertProfile(
            ProfileEntity(
                name = name,
                avatarColor = avatarColor,
                avatarIcon = avatarIcon,
                isActive = isFirst
            )
        )
        insertSettings(
            ProfileSettingsEntity(
                profileId = profileId,
                gracePeriodMinutes = gracePeriodMinutes,
                defaultSnoozeMinutes = defaultSnoozeMinutes
            )
        )
        return profileId
    }

    @Transaction
    suspend fun setActiveProfile(profileId: Long) {
        deactivateAllProfiles()
        activateProfile(profileId)
    }

    @Query("DELETE FROM profiles WHERE id = :profileId")
    suspend fun deleteProfile(profileId: Long)
}
