package com.example.patienthealthmonitoring.session

import android.content.Context

class SessionManager(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(
        "patient_health_session",
        Context.MODE_PRIVATE
    )

    val isLoggedIn: Boolean
        get() = preferences.getString(KEY_STAFF_ID, null) != null

    fun saveStaffSession(staffId: String, staffName: String) {
        preferences.edit()
            .putString(KEY_STAFF_ID, staffId)
            .putString(KEY_STAFF_NAME, staffName)
            .apply()
    }

    fun getStaffId(): String? = preferences.getString(KEY_STAFF_ID, null)

    fun getStaffName(): String = preferences.getString(KEY_STAFF_NAME, "") ?: ""

    fun clearSession() {
        preferences.edit().clear().apply()
    }

    private companion object {
        const val KEY_STAFF_ID = "staff_id"
        const val KEY_STAFF_NAME = "staff_name"
    }
}
