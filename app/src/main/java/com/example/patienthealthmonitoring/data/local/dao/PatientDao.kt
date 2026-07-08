package com.example.patienthealthmonitoring.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.patienthealthmonitoring.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients ORDER BY created_at DESC")
    fun observeAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE patient_id = :patientId LIMIT 1")
    fun observePatient(patientId: String): Flow<PatientEntity?>

    @Query("SELECT * FROM patients WHERE patient_id = :patientId LIMIT 1")
    suspend fun getPatient(patientId: String): PatientEntity?

    @Query("SELECT patient_id FROM patients WHERE patient_id LIKE 'PAT-%' ORDER BY patient_id DESC LIMIT 1")
    suspend fun getLastPatientId(): String?

    @Query("SELECT COUNT(*) FROM patients WHERE patient_id = :patientId")
    suspend fun countByPatientId(patientId: String): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPatient(patient: PatientEntity)

    @Update
    suspend fun updatePatient(patient: PatientEntity)

    @Delete
    suspend fun deletePatient(patient: PatientEntity)
}
