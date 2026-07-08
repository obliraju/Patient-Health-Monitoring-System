package com.example.patienthealthmonitoring.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.sp
import com.example.patienthealthmonitoring.ui.components.MedicalLogo
import com.example.patienthealthmonitoring.ui.components.PasswordTextField
import com.example.patienthealthmonitoring.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val state by viewModel.loginUiState.collectAsState()

    LaunchedEffect(state.loginSuccessful) {
        if (state.loginSuccessful) {
            viewModel.consumeLoginSuccess()
            onLoginSuccess()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MedicalLogo()
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Patient Health Monitoring System",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Secure staff login",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = state.identifier,
                onValueChange = viewModel::updateLoginIdentifier,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.identifierError != null,
                label = { Text("Staff ID or Name") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Badge, contentDescription = null)
                },
                supportingText = state.identifierError?.let { message -> { Text(message) } }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordTextField(
                value = state.password,
                onValueChange = viewModel::updateLoginPassword,
                label = "Password",
                isVisible = state.passwordVisible,
                onToggleVisibility = viewModel::toggleLoginPasswordVisibility,
                modifier = Modifier.fillMaxWidth(),
                errorMessage = state.passwordError
            )

            state.message?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = viewModel::login,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Register")
            }
        }
    }
}
