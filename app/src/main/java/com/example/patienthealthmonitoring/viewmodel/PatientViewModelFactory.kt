package com.example.patienthealthmonitoring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.patienthealthmonitoring.data.AppContainer

class PatientViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(container.authRepository, container.sessionManager) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(container.patientRepository, container.sessionManager) as T
            }
            modelClass.isAssignableFrom(PatientFormViewModel::class.java) -> {
                PatientFormViewModel(container.patientRepository) as T
            }
            modelClass.isAssignableFrom(PatientDetailsViewModel::class.java) -> {
                PatientDetailsViewModel(container.patientRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
