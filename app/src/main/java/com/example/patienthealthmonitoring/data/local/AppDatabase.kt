package com.example.patienthealthmonitoring.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.patienthealthmonitoring.data.local.dao.PatientDao
import com.example.patienthealthmonitoring.data.local.dao.StaffDao
import com.example.patienthealthmonitoring.data.local.entity.PatientEntity
import com.example.patienthealthmonitoring.data.local.entity.StaffEntity

@Database(
    entities = [StaffEntity::class, PatientEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun staffDao(): StaffDao
    abstract fun patientDao(): PatientDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "patient_health_monitoring.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
