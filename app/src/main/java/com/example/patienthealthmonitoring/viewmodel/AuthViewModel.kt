package com.example.patienthealthmonitoring.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.patienthealthmonitoring.data.repository.AuthRepository
import com.example.patienthealthmonitoring.data.repository.LoginResult
import com.example.patienthealthmonitoring.data.repository.RegistrationResult
import com.example.patienthealthmonitoring.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _registrationUiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState: StateFlow<RegistrationUiState> = _registrationUiState.asStateFlow()

    fun updateLoginIdentifier(value: String) {
        _loginUiState.update { it.copy(identifier = value, identifierError = null, message = null) }
    }

    fun updateLoginPassword(value: String) {
        _loginUiState.update { it.copy(password = value, passwordError = null, message = null) }
    }

    fun toggleLoginPasswordVisibility() {
        _loginUiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun login() {
        val state = _loginUiState.value
        var hasError = false
        var identifierError: String? = null
        var passwordError: String? = null

        if (state.identifier.isBlank()) {
            identifierError = "Staff ID or Name is required"
            hasError = true
        }
        if (state.password.isBlank()) {
            passwordError = "Password is required"
            hasError = true
        }
        if (hasError) {
            _loginUiState.update {
                it.copy(identifierError = identifierError, passwordError = passwordError)
            }
            return
        }

        viewModelScope.launch {
            _loginUiState.update { it.copy(isLoading = true, message = null) }
            when (val result = authRepository.login(state.identifier, state.password)) {
                is LoginResult.Success -> {
                    sessionManager.saveStaffSession(result.staff.staffId, result.staff.name)
                    _loginUiState.update {
                        it.copy(isLoading = false, loginSuccessful = true, message = null)
                    }
                }
                LoginResult.InvalidIdentifier -> {
                    _loginUiState.update {
                        it.copy(
                            isLoading = false,
                            identifierError = "Invalid Staff ID/Name",
                            message = "Invalid Staff ID/Name"
                        )
                    }
                }
                LoginResult.IncorrectPassword -> {
                    _loginUiState.update {
                        it.copy(
                            isLoading = false,
                            passwordError = "Incorrect Password",
                            message = "Incorrect Password"
                        )
                    }
                }
            }
        }
    }

    fun consumeLoginSuccess() {
        _loginUiState.update { it.copy(loginSuccessful = false) }
    }

    fun updateFullName(value: String) {
        _registrationUiState.update { it.copy(fullName = value, fullNameError = null, message = null) }
    }

    fun updateStaffId(value: String) {
        _registrationUiState.update { it.copy(staffId = value, staffIdError = null, message = null) }
    }

    fun updateRegistrationPassword(value: String) {
        _registrationUiState.update { it.copy(password = value, passwordError = null, message = null) }
    }

    fun updateConfirmPassword(value: String) {
        _registrationUiState.update { it.copy(confirmPassword = value, confirmPasswordError = null, message = null) }
    }

    fun toggleRegistrationPasswordVisibility() {
        _registrationUiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _registrationUiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun register() {
        val state = _registrationUiState.value
        var hasError = false
        var fullNameError: String? = null
        var staffIdError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        if (state.fullName.isBlank()) {
            fullNameError = "Full Name is required"
            hasError = true
        }
        if (state.staffId.isBlank()) {
            staffIdError = "Staff ID is required"
            hasError = true
        }
        if (state.password.isBlank()) {
            passwordError = "Password is required"
            hasError = true
        }
        if (state.confirmPassword.isBlank()) {
            confirmPasswordError = "Confirm Password is required"
            hasError = true
        }
        if (state.password.isNotBlank() && state.confirmPassword.isNotBlank() && state.password != state.confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            hasError = true
        }

        if (hasError) {
            _registrationUiState.update {
                it.copy(
                    fullNameError = fullNameError,
                    staffIdError = staffIdError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _registrationUiState.update { it.copy(isLoading = true, message = null) }
            when (authRepository.register(state.fullName, state.staffId, state.password)) {
                RegistrationResult.Success -> {
                    _registrationUiState.update {
                        RegistrationUiState(
                            registrationSuccessful = true,
                            message = "Registration Successful"
                        )
                    }
                }
                RegistrationResult.DuplicateStaffId -> {
                    _registrationUiState.update {
                        it.copy(
                            isLoading = false,
                            staffIdError = "Staff ID already exists",
                            message = "Staff ID already exists"
                        )
                    }
                }
            }
        }
    }

    fun consumeRegistrationSuccess() {
        _registrationUiState.update { it.copy(registrationSuccessful = false) }
    }
}

data class LoginUiState(
    val identifier: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val identifierError: String? = null,
    val passwordError: String? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val loginSuccessful: Boolean = false
)

data class RegistrationUiState(
    val fullName: String = "",
    val staffId: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val fullNameError: String? = null,
    val staffIdError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val message: String? = null,
    val isLoading: Boolean = false,
    val registrationSuccessful: Boolean = false
)
