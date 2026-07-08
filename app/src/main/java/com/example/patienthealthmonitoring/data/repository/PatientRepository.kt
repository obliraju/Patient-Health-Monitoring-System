package com.example.patienthealthmonitoring.data.repository

import com.example.patienthealthmonitoring.data.local.dao.PatientDao
import com.example.patienthealthmonitoring.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

class PatientRepository(private val patientDao: PatientDao) {
    fun observePatients(): Flow<List<PatientEntity>> = patientDao.observeAllPatients()

    fun observePatient(patientId: String): Flow<PatientEntity?> = patientDao.observePatient(patientId)

    suspend fun getPatient(patientId: String): PatientEntity? = patientDao.getPatient(patientId)

    suspend fun isPatientIdTaken(patientId: String): Boolean = patientDao.countByPatientId(patientId) > 0

    suspend fun generateNextPatientId(): String {
        val lastId = patientDao.getLastPatientId()
        val lastNumber = lastId
            ?.removePrefix("PAT-")
            ?.toIntOrNull()
            ?: 0

        var nextNumber = lastNumber + 1
        var nextId = formatPatientId(nextNumber)
        while (isPatientIdTaken(nextId)) {
            nextNumber += 1
            nextId = formatPatientId(nextNumber)
        }
        return nextId
    }

    suspend fun addPatient(patient: PatientEntity) = patientDao.insertPatient(patient)

    suspend fun updatePatient(patient: PatientEntity) = patientDao.updatePatient(patient.copy(updatedAt = System.currentTimeMillis()))

    suspend fun deletePatient(patient: PatientEntity) = patientDao.deletePatient(patient)

    private fun formatPatientId(number: Int): String = "PAT-${number.toString().padStart(6, '0')}"
}
