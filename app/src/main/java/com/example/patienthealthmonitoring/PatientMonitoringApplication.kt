package com.example.patienthealthmonitoring

import android.app.Application
import com.example.patienthealthmonitoring.data.AppContainer

class PatientMonitoringApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
