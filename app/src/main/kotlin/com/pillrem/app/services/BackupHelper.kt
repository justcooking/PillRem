package com.pillrem.app.services

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pillrem.app.data.local.AppDatabase
import com.pillrem.app.data.local.entities.*
import com.pillrem.app.domain.DoseEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

data class PillRemBackupData(
    val version: Int = 1,
    val exportedAt: Long = System.currentTimeMillis(),
    val appVersion: String = "1.1.0",
    val profiles: List<ProfileEntity> = emptyList(),
    val profileSettings: List<ProfileSettingsEntity> = emptyList(),
    val medicines: List<MedicineEntity> = emptyList(),
    val schedules: List<ScheduleEntity> = emptyList(),
    val doseRecords: List<DoseRecordEntity> = emptyList()
)

object BackupHelper {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    suspend fun exportBackupJson(
        uri: Uri,
        context: Context,
        db: AppDatabase
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val profiles = db.profileDao().getAllProfilesSync()
            val profileSettings = db.profileDao().getAllSettingsSync()
            val medicines = db.medicineDao().getAllMedicinesSync()
            val schedules = db.scheduleDao().getAllSchedulesSync()
            val doseRecords = db.doseRecordDao().getAllDosesSync()

            val backup = PillRemBackupData(
                version = 1,
                exportedAt = System.currentTimeMillis(),
                appVersion = "1.1.0",
                profiles = profiles,
                profileSettings = profileSettings,
                medicines = medicines,
                schedules = schedules,
                doseRecords = doseRecords
            )

            val jsonString = gson.toJson(backup)
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream, Charsets.UTF_8).use { writer ->
                    writer.write(jsonString)
                }
            } ?: return@withContext false

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun importBackupJson(
        uri: Uri,
        context: Context,
        db: AppDatabase,
        doseEngine: DoseEngine
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).readText()
            } ?: return@withContext false

            val backup = gson.fromJson(jsonString, PillRemBackupData::class.java) ?: return@withContext false

            // Schema version validation: only format version 1 is supported in v1.x
            if (backup.version != 1) {
                return@withContext false
            }

            // Transactional atomic restore
            db.withTransaction {
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
            }

            // Shared reconciliation: pre-generates rolling doses AND rebuilds exact alarms
            doseEngine.reconcileAndScheduleAlarms(context)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun exportBackup(context: Context, uri: Uri, db: AppDatabase): Boolean =
        exportBackupJson(uri, context, db)

    suspend fun importBackup(context: Context, uri: Uri, db: AppDatabase, doseEngine: DoseEngine): Boolean =
        importBackupJson(uri, context, db, doseEngine)
}
