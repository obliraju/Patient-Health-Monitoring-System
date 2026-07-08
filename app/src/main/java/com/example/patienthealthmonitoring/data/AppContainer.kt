package com.example.patienthealthmonitoring.data

import android.content.Context
import com.example.patienthealthmonitoring.data.local.AppDatabase
import com.example.patienthealthmonitoring.data.repository.AuthRepository
import com.example.patienthealthmonitoring.data.repository.PatientRepository
import com.example.patienthealthmonitoring.session.SessionManager

class AppContainer(context: Context) {
    private val database = AppDatabase.getDatabase(context)

    val sessionManager = SessionManager(context)
    val authRepository = AuthRepository(database.staffDao())
    val patientRepository = PatientRepository(database.patientDao())
}
