package com.example.patienthealthmonitoring.ui.screens.patient

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.patienthealthmonitoring.ui.components.DetailRow
import com.example.patienthealthmonitoring.ui.components.InfoSection
import com.example.patienthealthmonitoring.viewmodel.PatientFormField
import com.example.patienthealthmonitoring.viewmodel.PatientFormUiState
import com.example.patienthealthmonitoring.viewmodel.PatientFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientFormScreen(
    patientId: String?,
    viewModel: PatientFormViewModel,
    onCancel: () -> Unit,
    onSaved: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(patientId) {
        if (patientId == null) {
            viewModel.prepareNewPatientId()
        } else {
            viewModel.loadPatient(patientId)
        }
    }

    LaunchedEffect(state.shouldNavigateBack) {
        if (state.shouldNavigateBack) {
            Toast.makeText(context, state.successMessage.orEmpty(), Toast.LENGTH_SHORT).show()
            viewModel.consumeNavigation()
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.title) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            InfoSection(title = "Patient ID", icon = Icons.Filled.Badge) {
                DetailRow(label = "Auto-generated ID", value = state.patientId)
                state.errors[PatientFormField.PATIENT_ID]?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }

            InfoSection(title = "Personal Information", icon = Icons.Filled.Person) {
                PatientTextField(state, PatientFormField.PATIENT_NAME, "Patient Name", viewModel::updateField)
                PatientTextField(state, PatientFormField.GENDER, "Gender", viewModel::updateField)
                PatientTextField(state, PatientFormField.AGE, "Age", viewModel::updateField, KeyboardType.Number)
                PatientTextField(state, PatientFormField.BLOOD_GROUP, "Blood Group", viewModel::updateField)
                PatientTextField(state, PatientFormField.HEIGHT, "Height", viewModel::updateField, KeyboardType.Decimal)
                PatientTextField(state, PatientFormField.WEIGHT, "Weight", viewModel::updateField, KeyboardType.Decimal)
                PatientTextField(state, PatientFormField.PHONE_NUMBER, "Phone Number", viewModel::updateField, KeyboardType.Phone)
                PatientTextField(state, PatientFormField.EMERGENCY_CONTACT, "Emergency Contact", viewModel::updateField, KeyboardType.Phone)
                PatientTextField(state, PatientFormField.ADDRESS, "Address", viewModel::updateField, singleLine = false)
                PatientTextField(state, PatientFormField.DATE_OF_BIRTH, "Date of Birth", viewModel::updateField, placeholder = "YYYY-MM-DD")
                PatientTextField(state, PatientFormField.AADHAAR_NUMBER, "Aadhaar Number (optional)", viewModel::updateField, KeyboardType.Number)
            }

            InfoSection(title = "Medical Information", icon = Icons.Filled.HealthAndSafety) {
                PatientTextField(state, PatientFormField.ALLERGIES, "Allergies", viewModel::updateField, singleLine = false)
                PatientTextField(state, PatientFormField.ONGOING_MEDICATION, "Ongoing Medication", viewModel::updateField, singleLine = false)
                PatientTextField(state, PatientFormField.CURRENT_DISEASES, "Current Diseases", viewModel::updateField, singleLine = false)
                PatientTextField(state, PatientFormField.PREVIOUS_MEDICAL_HISTORY, "Previous Medical History", viewModel::updateField, singleLine = false)
                PatientTextField(state, PatientFormField.PREVIOUS_TREATMENTS, "Previous Treatments", viewModel::updateField, singleLine = false)
                PatientTextField(state, PatientFormField.SURGERIES, "Surgeries", viewModel::updateField, singleLine = false)
            }

            InfoSection(title = "Doctor Information", icon = Icons.Filled.MedicalServices) {
                PatientTextField(state, PatientFormField.DOCTOR_NAME, "Doctor Name", viewModel::updateField)
                PatientTextField(state, PatientFormField.HOSPITAL_NAME, "Hospital Name", viewModel::updateField)
                PatientTextField(state, PatientFormField.DEPARTMENT, "Department", viewModel::updateField)
                PatientTextField(state, PatientFormField.DATE_OF_ADMISSION, "Date of Admission", viewModel::updateField, placeholder = "YYYY-MM-DD")
                PatientTextField(state, PatientFormField.DATE_OF_VISIT, "Date of Visit", viewModel::updateField, placeholder = "YYYY-MM-DD")
            }

            InfoSection(title = "Health Parameters", icon = Icons.Filled.MonitorHeart) {
                PatientTextField(state, PatientFormField.BLOOD_PRESSURE, "Blood Pressure", viewModel::updateField, placeholder = "120/80")
                PatientTextField(state, PatientFormField.HEART_RATE, "Heart Rate", viewModel::updateField, KeyboardType.Number)
                PatientTextField(state, PatientFormField.BODY_TEMPERATURE, "Body Temperature", viewModel::updateField, KeyboardType.Decimal)
                PatientTextField(state, PatientFormField.OXYGEN_SATURATION, "Oxygen Saturation (SpO2)", viewModel::updateField, KeyboardType.Decimal)
                PatientTextField(state, PatientFormField.BLOOD_SUGAR, "Blood Sugar", viewModel::updateField, KeyboardType.Decimal)
            }

            InfoSection(title = "Additional Notes", icon = Icons.Filled.Bloodtype) {
                PatientTextField(state, PatientFormField.NOTES, "Notes", viewModel::updateField, singleLine = false)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = viewModel::savePatient,
                    modifier = Modifier.weight(1f),
                    enabled = !state.isSaving
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(if (state.isEditMode) "Update Patient" else "Save Patient")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun PatientTextField(
    state: PatientFormUiState,
    field: PatientFormField,
    label: String,
    onValueChange: (PatientFormField, String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    placeholder: String? = null
) {
    OutlinedTextField(
        value = state.valueFor(field),
        onValueChange = { onValueChange(field, it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = placeholder?.let { hint -> { Text(hint) } },
        isError = state.errors[field] != null,
        supportingText = state.errors[field]?.let { message -> { Text(message) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3
    )
}

private fun PatientFormUiState.valueFor(field: PatientFormField): String {
    return when (field) {
        PatientFormField.PATIENT_ID -> patientId
        PatientFormField.PATIENT_NAME -> patientName
        PatientFormField.GENDER -> gender
        PatientFormField.AGE -> age
        PatientFormField.BLOOD_GROUP -> bloodGroup
        PatientFormField.HEIGHT -> height
        PatientFormField.WEIGHT -> weight
        PatientFormField.PHONE_NUMBER -> phoneNumber
        PatientFormField.EMERGENCY_CONTACT -> emergencyContact
        PatientFormField.ADDRESS -> address
        PatientFormField.DATE_OF_BIRTH -> dateOfBirth
        PatientFormField.AADHAAR_NUMBER -> aadhaarNumber
        PatientFormField.ALLERGIES -> allergies
        PatientFormField.ONGOING_MEDICATION -> ongoingMedication
        PatientFormField.CURRENT_DISEASES -> currentDiseases
        PatientFormField.PREVIOUS_MEDICAL_HISTORY -> previousMedicalHistory
        PatientFormField.PREVIOUS_TREATMENTS -> previousTreatments
        PatientFormField.SURGERIES -> surgeries
        PatientFormField.DOCTOR_NAME -> doctorName
        PatientFormField.HOSPITAL_NAME -> hospitalName
        PatientFormField.DEPARTMENT -> department
        PatientFormField.DATE_OF_ADMISSION -> dateOfAdmission
        PatientFormField.DATE_OF_VISIT -> dateOfVisit
        PatientFormField.BLOOD_PRESSURE -> bloodPressure
        PatientFormField.HEART_RATE -> heartRate
        PatientFormField.BODY_TEMPERATURE -> bodyTemperature
        PatientFormField.OXYGEN_SATURATION -> oxygenSaturation
        PatientFormField.BLOOD_SUGAR -> bloodSugar
        PatientFormField.NOTES -> notes
    }
}
