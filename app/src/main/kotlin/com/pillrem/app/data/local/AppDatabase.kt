package com.pillrem.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pillrem.app.data.local.dao.DoseRecordDao
import com.pillrem.app.data.local.dao.MedicineDao
import com.pillrem.app.data.local.dao.ProfileDao
import com.pillrem.app.data.local.dao.ScheduleDao
import com.pillrem.app.data.local.entities.*

@Database(
    entities = [
        ProfileEntity::class,
        ProfileSettingsEntity::class,
        AppSettingEntity::class,
        MedicineEntity::class,
        ScheduleEntity::class,
        DoseRecordEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun medicineDao(): MedicineDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun doseRecordDao(): DoseRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pillrem_database.db"
                )
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
