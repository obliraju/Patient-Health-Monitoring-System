package com.example.patienthealthmonitoring.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = MedicalBlue,
    onPrimary = Color.White,
    primaryContainer = SoftBlue,
    onPrimaryContainer = MedicalBlueDark,
    secondary = Color.White,
    onSecondary = MedicalBlueDark,
    tertiary = AccentGreen,
    onTertiary = Color.White,
    background = HospitalBackground,
    onBackground = TextPrimary,
    surface = Color.White,
    onSurface = TextPrimary,
    surfaceVariant = SoftBlue,
    onSurfaceVariant = TextSecondary,
    error = Color(0xFFB3261E)
)

@Composable
fun PatientHealthMonitoringTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
