package com.example.patienthealthmonitoring.ui.screens.auth

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.patienthealthmonitoring.ui.components.MedicalLogo
import com.example.patienthealthmonitoring.ui.components.PasswordTextField
import com.example.patienthealthmonitoring.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackClick: () -> Unit,
    onRegistrationComplete: () -> Unit
) {
    val state by viewModel.registrationUiState.collectAsState()

    LaunchedEffect(state.registrationSuccessful) {
        if (state.registrationSuccessful) {
            delay(900)
            viewModel.consumeRegistrationSuccess()
            onRegistrationComplete()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Register Staff",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            MedicalLogo(size = 72.dp)
            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = state.fullName,
                onValueChange = viewModel::updateFullName,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.fullNameError != null,
                label = { Text("Full Name") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null) },
                supportingText = state.fullNameError?.let { message -> { Text(message) } }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.staffId,
                onValueChange = viewModel::updateStaffId,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.staffIdError != null,
                label = { Text("Staff ID") },
                leadingIcon = { Icon(imageVector = Icons.Filled.Badge, contentDescription = null) },
                supportingText = state.staffIdError?.let { message -> { Text(message) } }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordTextField(
                value = state.password,
                onValueChange = viewModel::updateRegistrationPassword,
                label = "Password",
                isVisible = state.passwordVisible,
                onToggleVisibility = viewModel::toggleRegistrationPasswordVisibility,
                modifier = Modifier.fillMaxWidth(),
                errorMessage = state.passwordError
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordTextField(
                value = state.confirmPassword,
                onValueChange = viewModel::updateConfirmPassword,
                label = "Confirm Password",
                isVisible = state.confirmPasswordVisible,
                onToggleVisibility = viewModel::toggleConfirmPasswordVisibility,
                modifier = Modifier.fillMaxWidth(),
                errorMessage = state.confirmPasswordError
            )

            state.message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = if (state.registrationSuccessful) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = viewModel::register,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isLoading && !state.registrationSuccessful
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Create Account")
                }
            }
        }
    }
}
