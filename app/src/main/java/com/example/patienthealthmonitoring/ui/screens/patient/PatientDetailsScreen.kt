package com.example.patienthealthmonitoring.ui.screens.patient

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.patienthealthmonitoring.data.local.entity.PatientEntity
import com.example.patienthealthmonitoring.ui.components.DetailRow
import com.example.patienthealthmonitoring.ui.components.InfoSection
import com.example.patienthealthmonitoring.viewmodel.PatientDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    patientId: String,
    viewModel: PatientDetailsViewModel,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    onDeleted: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(patientId) {
        viewModel.loadPatient(patientId)
    }

    LaunchedEffect(state.deleted) {
        if (state.deleted) {
            Toast.makeText(context, "Patient deleted", Toast.LENGTH_SHORT).show()
            onDeleted()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.patient == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(state.error ?: "Patient not found")
                }
            }
            else -> {
                PatientDetailsContent(
                    patient = state.patient!!,
                    modifier = Modifier.padding(paddingValues),
                    onEdit = onEdit,
                    onDelete = viewModel::showDeleteDialog
                )
            }
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideDeleteDialog,
            title = { Text("Delete Patient") },
            text = { Text("Are you sure you want to delete this patient?") },
            confirmButton = {
                TextButton(onClick = viewModel::deletePatient) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideDeleteDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PatientDetailsContent(
    patient: PatientEntity,
    modifier: Modifier = Modifier,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        InfoSection(title = patient.patientName, icon = Icons.Filled.Person) {
            Text(
                text = patient.patientId,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        InfoSection(title = "Patient Information", icon = Icons.Filled.Info) {
            DetailRow("Patient Name", patient.patientName)
            DetailRow("Patient ID", patient.patientId)
            DetailRow("Age", patient.age.toString())
            DetailRow("Gender", patient.gender)
            DetailRow("Blood Group", patient.bloodGroup)
            DetailRow("Height", patient.height)
            DetailRow("Weight", patient.weight)
            DetailRow("Phone Number", patient.phoneNumber)
            DetailRow("Emergency Contact", patient.emergencyContact)
            DetailRow("Address", patient.address)
            DetailRow("Date of Birth", patient.dateOfBirth)
            DetailRow("Aadhaar Number", patient.aadhaarNumber)
        }

        InfoSection(title = "Medical Information", icon = Icons.Filled.HealthAndSafety) {
            DetailRow("Ongoing Medication", patient.ongoingMedication)
            DetailRow("Allergies", patient.allergies)
            DetailRow("Diseases", patient.currentDiseases)
            DetailRow("Medical History", patient.previousMedicalHistory)
            DetailRow("Previous Treatments", patient.previousTreatments)
            DetailRow("Surgeries", patient.surgeries)
        }

        InfoSection(title = "Doctor Information", icon = Icons.Filled.LocalHospital) {
            DetailRow("Doctor Name", patient.doctorName)
            DetailRow("Hospital Name", patient.hospitalName)
            DetailRow("Department", patient.department)
            DetailRow("Date of Admission", patient.dateOfAdmission)
            DetailRow("Date of Visit", patient.dateOfVisit)
        }

        InfoSection(title = "Health Parameters", icon = Icons.Filled.MonitorHeart) {
            DetailRow("Blood Pressure", patient.bloodPressure)
            DetailRow("Heart Rate", patient.heartRate)
            DetailRow("Temperature", patient.bodyTemperature)
            DetailRow("SpO2", patient.oxygenSaturation)
            DetailRow("Blood Sugar", patient.bloodSugar)
        }

        InfoSection(title = "Additional Notes", icon = Icons.Filled.MedicalInformation) {
            Text(patient.notes.ifBlank { "No additional notes" })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { onEdit(patient.patientId) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                Text(text = "Edit Patient", modifier = Modifier.padding(start = 8.dp))
            }
            Button(
                onClick = onDelete,
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                Text(text = "Delete Patient", modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
