package com.example.patienthealthmonitoring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patienthealthmonitoring.data.local.entity.PatientEntity
import com.example.patienthealthmonitoring.data.repository.PatientRepository
import com.example.patienthealthmonitoring.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    patientRepository: PatientRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val sortOption = MutableStateFlow(PatientSortOption.RECENTLY_ADDED)

    val uiState: StateFlow<HomeUiState> = combine(
        patientRepository.observePatients(),
        searchQuery,
        sortOption
    ) { patients, query, sort ->
        val filteredPatients = patients.filter { patient ->
            patient.patientName.contains(query, ignoreCase = true) ||
                patient.patientId.contains(query, ignoreCase = true)
        }

        HomeUiState(
            patients = sortPatients(filteredPatients, sort),
            searchQuery = query,
            sortOption = sort,
            staffName = sessionManager.getStaffName()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(staffName = sessionManager.getStaffName())
    )

    fun updateSearchQuery(value: String) {
        searchQuery.update { value }
    }

    fun updateSortOption(option: PatientSortOption) {
        sortOption.update { option }
    }

    fun logout() {
        sessionManager.clearSession()
    }

    private fun sortPatients(
        patients: List<PatientEntity>,
        sort: PatientSortOption
    ): List<PatientEntity> {
        return when (sort) {
            PatientSortOption.PATIENT_NAME -> patients.sortedBy { it.patientName.lowercase() }
            PatientSortOption.RECENTLY_ADDED -> patients.sortedByDescending { it.createdAt }
            PatientSortOption.OLDEST -> patients.sortedBy { it.createdAt }
            PatientSortOption.AGE -> patients.sortedBy { it.age }
        }
    }
}

data class HomeUiState(
    val patients: List<PatientEntity> = emptyList(),
    val searchQuery: String = "",
    val sortOption: PatientSortOption = PatientSortOption.RECENTLY_ADDED,
    val staffName: String = ""
)

enum class PatientSortOption(val label: String) {
    PATIENT_NAME("Patient Name"),
    RECENTLY_ADDED("Recently Added"),
    OLDEST("Oldest"),
    AGE("Age")
}
