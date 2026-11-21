package com.pillrem.app.services

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.*
import com.pillrem.app.domain.DoseEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

data class PillRemBackupData(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val profiles: List<ProfileEntity>,
    val profileSettings: List<ProfileSettingsEntity>,
    val medicines: List<MedicineEntity>,
    val schedules: List<ScheduleEntity>,
    val doseRecords: List<DoseRecordEntity>
)

object BackupHelper {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    suspend fun exportBackup(context: Context, uri: Uri, db: AppDatabase): Boolean = withContext(Dispatchers.IO) {
        try {
            val profiles = db.profileDao().getAllProfilesSync()
            val settings = db.profileDao().getAllSettingsSync()
            val medicines = db.medicineDao().getAllMedicinesSync()
            val schedules = db.scheduleDao().getAllSchedulesSync()
            val doses = db.doseRecordDao().getAllDosesSync()

            val backup = PillRemBackupData(
                profiles = profiles,
                profileSettings = settings,
                medicines = medicines,
                schedules = schedules,
                doseRecords = doses
            )

            val jsonString = gson.toJson(backup)
            context.contentResolver.openOutputStream(uri)?.use { os ->
                os.write(jsonString.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun importBackup(
        context: Context,
        uri: Uri,
        db: AppDatabase,
        doseEngine: DoseEngine
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).readText()
            } ?: return@withContext false

            val backup = gson.fromJson(jsonString, PillRemBackupData::class.java) ?: return@withContext false

            // Restore tables
            if (backup.profiles.isNotEmpty()) {
                db.profileDao().insertAllProfiles(backup.profiles)
            }
            if (backup.profileSettings.isNotEmpty()) {
                db.profileDao().insertAllSettings(backup.profileSettings)
            }
            if (backup.medicines.isNotEmpty()) {
                db.medicineDao().insertAllMedicines(backup.medicines)
            }
            if (backup.schedules.isNotEmpty()) {
                db.scheduleDao().insertAllSchedules(backup.schedules)
            }
            if (backup.doseRecords.isNotEmpty()) {
                db.doseRecordDao().insertAllDoses(backup.doseRecords)
            }

            // Pre-generate rolling doses
            doseEngine.generateRollingDoses(daysAhead = 7)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
