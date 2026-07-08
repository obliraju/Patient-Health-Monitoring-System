package com.example.patienthealthmonitoring.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey
    @ColumnInfo(name = "patient_id")
    val patientId: String,
    @ColumnInfo(name = "patient_name")
    val patientName: String,
    val gender: String,
    val age: Int,
    @ColumnInfo(name = "blood_group")
    val bloodGroup: String,
    val height: String,
    val weight: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "emergency_contact")
    val emergencyContact: String,
    val address: String,
    @ColumnInfo(name = "date_of_birth")
    val dateOfBirth: String,
    @ColumnInfo(name = "aadhaar_number")
    val aadhaarNumber: String,
    val allergies: String,
    @ColumnInfo(name = "ongoing_medication")
    val ongoingMedication: String,
    @ColumnInfo(name = "current_diseases")
    val currentDiseases: String,
    @ColumnInfo(name = "previous_medical_history")
    val previousMedicalHistory: String,
    @ColumnInfo(name = "previous_treatments")
    val previousTreatments: String,
    val surgeries: String,
    @ColumnInfo(name = "doctor_name")
    val doctorName: String,
    @ColumnInfo(name = "hospital_name")
    val hospitalName: String,
    val department: String,
    @ColumnInfo(name = "date_of_admission")
    val dateOfAdmission: String,
    @ColumnInfo(name = "date_of_visit")
    val dateOfVisit: String,
    @ColumnInfo(name = "blood_pressure")
    val bloodPressure: String,
    @ColumnInfo(name = "heart_rate")
    val heartRate: String,
    @ColumnInfo(name = "body_temperature")
    val bodyTemperature: String,
    @ColumnInfo(name = "oxygen_saturation")
    val oxygenSaturation: String,
    @ColumnInfo(name = "blood_sugar")
    val bloodSugar: String,
    val notes: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)
