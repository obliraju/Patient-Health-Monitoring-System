package com.example.patienthealthmonitoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.patienthealthmonitoring.navigation.AppNavHost
import com.example.patienthealthmonitoring.ui.theme.PatientHealthMonitoringTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as PatientMonitoringApplication).container
        setContent {
            PatientHealthMonitoringTheme {
                AppNavHost(container = appContainer)
            }
        }
    }
}
