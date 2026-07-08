package com.example.patienthealthmonitoring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patienthealthmonitoring.data.local.entity.PatientEntity
import com.example.patienthealthmonitoring.data.repository.PatientRepository
import com.example.patienthealthmonitoring.util.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientFormViewModel(
    private val patientRepository: PatientRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PatientFormUiState())
    val uiState: StateFlow<PatientFormUiState> = _uiState.asStateFlow()

    fun prepareNewPatientId() {
        val state = _uiState.value
        if (state.patientId.isNotBlank() && !state.isEditMode) return

        viewModelScope.launch {
            val patientId = patientRepository.generateNextPatientId()
            _uiState.update {
                PatientFormUiState(patientId = patientId, title = "Add Patient")
            }
        }
    }

    fun loadPatient(patientId: String) {
        if (_uiState.value.loadedPatientId == patientId) return

        viewModelScope.launch {
            val patient = patientRepository.getPatient(patientId) ?: return@launch
            _uiState.update {
                PatientFormUiState(
                    patientId = patient.patientId,
                    patientName = patient.patientName,
                    gender = patient.gender,
                    age = patient.age.toString(),
                    bloodGroup = patient.bloodGroup,
                    height = patient.height,
                    weight = patient.weight,
                    phoneNumber = patient.phoneNumber,
                    emergencyContact = patient.emergencyContact,
                    address = patient.address,
                    dateOfBirth = patient.dateOfBirth,
                    aadhaarNumber = patient.aadhaarNumber,
                    allergies = patient.allergies,
                    ongoingMedication = patient.ongoingMedication,
                    currentDiseases = patient.currentDiseases,
                    previousMedicalHistory = patient.previousMedicalHistory,
                    previousTreatments = patient.previousTreatments,
                    surgeries = patient.surgeries,
                    doctorName = patient.doctorName,
                    hospitalName = patient.hospitalName,
                    department = patient.department,
                    dateOfAdmission = patient.dateOfAdmission,
                    dateOfVisit = patient.dateOfVisit,
                    bloodPressure = patient.bloodPressure,
                    heartRate = patient.heartRate,
                    bodyTemperature = patient.bodyTemperature,
                    oxygenSaturation = patient.oxygenSaturation,
                    bloodSugar = patient.bloodSugar,
                    notes = patient.notes,
                    isEditMode = true,
                    loadedPatientId = patient.patientId,
                    createdAt = patient.createdAt,
                    title = "Edit Patient"
                )
            }
        }
    }

    fun updateField(field: PatientFormField, value: String) {
        _uiState.update { state ->
            val cleanErrors = state.errors - field
            when (field) {
                PatientFormField.PATIENT_ID -> state.copy(patientId = value, errors = cleanErrors)
                PatientFormField.PATIENT_NAME -> state.copy(patientName = value, errors = cleanErrors)
                PatientFormField.GENDER -> state.copy(gender = value, errors = cleanErrors)
                PatientFormField.AGE -> state.copy(age = value, errors = cleanErrors)
                PatientFormField.BLOOD_GROUP -> state.copy(bloodGroup = value, errors = cleanErrors)
                PatientFormField.HEIGHT -> state.copy(height = value, errors = cleanErrors)
                PatientFormField.WEIGHT -> state.copy(weight = value, errors = cleanErrors)
                PatientFormField.PHONE_NUMBER -> state.copy(phoneNumber = value, errors = cleanErrors)
                PatientFormField.EMERGENCY_CONTACT -> state.copy(emergencyContact = value, errors = cleanErrors)
                PatientFormField.ADDRESS -> state.copy(address = value, errors = cleanErrors)
                PatientFormField.DATE_OF_BIRTH -> state.copy(dateOfBirth = value, errors = cleanErrors)
                PatientFormField.AADHAAR_NUMBER -> state.copy(aadhaarNumber = value, errors = cleanErrors)
                PatientFormField.ALLERGIES -> state.copy(allergies = value, errors = cleanErrors)
                PatientFormField.ONGOING_MEDICATION -> state.copy(ongoingMedication = value, errors = cleanErrors)
                PatientFormField.CURRENT_DISEASES -> state.copy(currentDiseases = value, errors = cleanErrors)
                PatientFormField.PREVIOUS_MEDICAL_HISTORY -> state.copy(previousMedicalHistory = value, errors = cleanErrors)
                PatientFormField.PREVIOUS_TREATMENTS -> state.copy(previousTreatments = value, errors = cleanErrors)
                PatientFormField.SURGERIES -> state.copy(surgeries = value, errors = cleanErrors)
                PatientFormField.DOCTOR_NAME -> state.copy(doctorName = value, errors = cleanErrors)
                PatientFormField.HOSPITAL_NAME -> state.copy(hospitalName = value, errors = cleanErrors)
                PatientFormField.DEPARTMENT -> state.copy(department = value, errors = cleanErrors)
                PatientFormField.DATE_OF_ADMISSION -> state.copy(dateOfAdmission = value, errors = cleanErrors)
                PatientFormField.DATE_OF_VISIT -> state.copy(dateOfVisit = value, errors = cleanErrors)
                PatientFormField.BLOOD_PRESSURE -> state.copy(bloodPressure = value, errors = cleanErrors)
                PatientFormField.HEART_RATE -> state.copy(heartRate = value, errors = cleanErrors)
                PatientFormField.BODY_TEMPERATURE -> state.copy(bodyTemperature = value, errors = cleanErrors)
                PatientFormField.OXYGEN_SATURATION -> state.copy(oxygenSaturation = value, errors = cleanErrors)
                PatientFormField.BLOOD_SUGAR -> state.copy(bloodSugar = value, errors = cleanErrors)
                PatientFormField.NOTES -> state.copy(notes = value, errors = cleanErrors)
            }
        }
    }

    fun savePatient() {
        val state = _uiState.value
        val validationErrors = validate(state)
        if (validationErrors.isNotEmpty()) {
            _uiState.update { it.copy(errors = validationErrors) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            if (!state.isEditMode && patientRepository.isPatientIdTaken(state.patientId)) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errors = mapOf(PatientFormField.PATIENT_ID to "Duplicate Patient ID")
                    )
                }
                return@launch
            }

            val entity = state.toEntity()
            if (state.isEditMode) {
                patientRepository.updatePatient(entity)
            } else {
                patientRepository.addPatient(entity)
            }

            _uiState.update {
                it.copy(
                    isSaving = false,
                    successMessage = if (state.isEditMode) "Patient Updated Successfully" else "Patient Saved Successfully",
                    shouldNavigateBack = true
                )
            }
        }
    }

    fun consumeNavigation() {
        _uiState.update { it.copy(shouldNavigateBack = false, successMessage = null) }
    }

    private fun validate(state: PatientFormUiState): Map<PatientFormField, String> {
        val errors = mutableMapOf<PatientFormField, String>()

        fun required(field: PatientFormField, value: String, label: String) {
            if (value.isBlank()) errors[field] = "$label is required"
        }

        required(PatientFormField.PATIENT_NAME, state.patientName, "Patient Name")
        required(PatientFormField.GENDER, state.gender, "Gender")
        required(PatientFormField.AGE, state.age, "Age")
        required(PatientFormField.PHONE_NUMBER, state.phoneNumber, "Phone Number")
        required(PatientFormField.EMERGENCY_CONTACT, state.emergencyContact, "Emergency Contact")
        required(PatientFormField.ADDRESS, state.address, "Address")
        required(PatientFormField.DATE_OF_BIRTH, state.dateOfBirth, "Date of Birth")
        required(PatientFormField.DOCTOR_NAME, state.doctorName, "Doctor Name")
        required(PatientFormField.HOSPITAL_NAME, state.hospitalName, "Hospital Name")
        required(PatientFormField.DEPARTMENT, state.department, "Department")
        required(PatientFormField.DATE_OF_VISIT, state.dateOfVisit, "Date of Visit")
        required(PatientFormField.BLOOD_PRESSURE, state.bloodPressure, "Blood Pressure")
        required(PatientFormField.HEART_RATE, state.heartRate, "Heart Rate")
        required(PatientFormField.BODY_TEMPERATURE, state.bodyTemperature, "Body Temperature")
        required(PatientFormField.OXYGEN_SATURATION, state.oxygenSaturation, "SpO2")

        if (state.age.isNotBlank() && !Validation.isValidAge(state.age)) {
            errors[PatientFormField.AGE] = "Enter a valid age"
        }
        if (state.phoneNumber.isNotBlank() && !Validation.isValidPhone(state.phoneNumber)) {
            errors[PatientFormField.PHONE_NUMBER] = "Enter a valid phone number"
        }
        if (state.emergencyContact.isNotBlank() && !Validation.isValidPhone(state.emergencyContact)) {
            errors[PatientFormField.EMERGENCY_CONTACT] = "Enter a valid emergency contact"
        }
        if (!Validation.isValidAadhaar(state.aadhaarNumber)) {
            errors[PatientFormField.AADHAAR_NUMBER] = "Aadhaar must contain 12 digits"
        }
        if (!Validation.isPositiveDecimal(state.height)) {
            errors[PatientFormField.HEIGHT] = "Enter a valid height"
        }
        if (!Validation.isPositiveDecimal(state.weight)) {
            errors[PatientFormField.WEIGHT] = "Enter a valid weight"
        }
        if (state.heartRate.isNotBlank() && state.heartRate.toIntOrNull() == null) {
            errors[PatientFormField.HEART_RATE] = "Enter a valid heart rate"
        }
        if (state.bodyTemperature.isNotBlank() && state.bodyTemperature.toDoubleOrNull() == null) {
            errors[PatientFormField.BODY_TEMPERATURE] = "Enter a valid temperature"
        }
        if (state.oxygenSaturation.isNotBlank() && !Validation.isValidPercentage(state.oxygenSaturation)) {
            errors[PatientFormField.OXYGEN_SATURATION] = "SpO2 must be between 0 and 100"
        }
        if (state.bloodSugar.isNotBlank() && state.bloodSugar.toDoubleOrNull() == null) {
            errors[PatientFormField.BLOOD_SUGAR] = "Enter a valid blood sugar value"
        }

        return errors
    }

    private fun PatientFormUiState.toEntity(): PatientEntity {
        return PatientEntity(
            patientId = patientId.trim(),
            patientName = patientName.trim(),
            gender = gender.trim(),
            age = age.toInt(),
            bloodGroup = bloodGroup.trim(),
            height = height.trim(),
            weight = weight.trim(),
            phoneNumber = phoneNumber.trim(),
            emergencyContact = emergencyContact.trim(),
            address = address.trim(),
            dateOfBirth = dateOfBirth.trim(),
            aadhaarNumber = aadhaarNumber.trim(),
            allergies = allergies.trim(),
            ongoingMedication = ongoingMedication.trim(),
            currentDiseases = currentDiseases.trim(),
            previousMedicalHistory = previousMedicalHistory.trim(),
            previousTreatments = previousTreatments.trim(),
            surgeries = surgeries.trim(),
            doctorName = doctorName.trim(),
            hospitalName = hospitalName.trim(),
            department = department.trim(),
            dateOfAdmission = dateOfAdmission.trim(),
            dateOfVisit = dateOfVisit.trim(),
            bloodPressure = bloodPressure.trim(),
            heartRate = heartRate.trim(),
            bodyTemperature = bodyTemperature.trim(),
            oxygenSaturation = oxygenSaturation.trim(),
            bloodSugar = bloodSugar.trim(),
            notes = notes.trim(),
            createdAt = createdAt,
            updatedAt = System.currentTimeMillis()
        )
    }
}

data class PatientFormUiState(
    val patientId: String = "",
    val patientName: String = "",
    val gender: String = "",
    val age: String = "",
    val bloodGroup: String = "",
    val height: String = "",
    val weight: String = "",
    val phoneNumber: String = "",
    val emergencyContact: String = "",
    val address: String = "",
    val dateOfBirth: String = "",
    val aadhaarNumber: String = "",
    val allergies: String = "",
    val ongoingMedication: String = "",
    val currentDiseases: String = "",
    val previousMedicalHistory: String = "",
    val previousTreatments: String = "",
    val surgeries: String = "",
    val doctorName: String = "",
    val hospitalName: String = "",
    val department: String = "",
    val dateOfAdmission: String = "",
    val dateOfVisit: String = "",
    val bloodPressure: String = "",
    val heartRate: String = "",
    val bodyTemperature: String = "",
    val oxygenSaturation: String = "",
    val bloodSugar: String = "",
    val notes: String = "",
    val errors: Map<PatientFormField, String> = emptyMap(),
    val isSaving: Boolean = false,
    val successMessage: String? = null,
    val shouldNavigateBack: Boolean = false,
    val isEditMode: Boolean = false,
    val loadedPatientId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val title: String = "Add Patient"
)

enum class PatientFormField {
    PATIENT_ID,
    PATIENT_NAME,
    GENDER,
    AGE,
    BLOOD_GROUP,
    HEIGHT,
    WEIGHT,
    PHONE_NUMBER,
    EMERGENCY_CONTACT,
    ADDRESS,
    DATE_OF_BIRTH,
    AADHAAR_NUMBER,
    ALLERGIES,
    ONGOING_MEDICATION,
    CURRENT_DISEASES,
    PREVIOUS_MEDICAL_HISTORY,
    PREVIOUS_TREATMENTS,
    SURGERIES,
    DOCTOR_NAME,
    HOSPITAL_NAME,
    DEPARTMENT,
    DATE_OF_ADMISSION,
    DATE_OF_VISIT,
    BLOOD_PRESSURE,
    HEART_RATE,
    BODY_TEMPERATURE,
    OXYGEN_SATURATION,
    BLOOD_SUGAR,
    NOTES
}
