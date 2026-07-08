package com.example.patienthealthmonitoring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patienthealthmonitoring.data.local.entity.PatientEntity
import com.example.patienthealthmonitoring.data.repository.PatientRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientDetailsViewModel(
    private val patientRepository: PatientRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PatientDetailsUiState())
    val uiState: StateFlow<PatientDetailsUiState> = _uiState.asStateFlow()

    private var patientJob: Job? = null
    private var loadedPatientId: String? = null

    fun loadPatient(patientId: String) {
        if (loadedPatientId == patientId) return
        loadedPatientId = patientId
        patientJob?.cancel()
        patientJob = viewModelScope.launch {
            patientRepository.observePatient(patientId).collect { patient ->
                _uiState.update {
                    it.copy(patient = patient, isLoading = false, error = if (patient == null) "Patient not found" else null)
                }
            }
        }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun hideDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun deletePatient() {
        val patient = _uiState.value.patient ?: return
        viewModelScope.launch {
            patientRepository.deletePatient(patient)
            _uiState.update { it.copy(showDeleteDialog = false, deleted = true) }
        }
    }
}

data class PatientDetailsUiState(
    val patient: PatientEntity? = null,
    val isLoading: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val deleted: Boolean = false,
    val error: String? = null
)
