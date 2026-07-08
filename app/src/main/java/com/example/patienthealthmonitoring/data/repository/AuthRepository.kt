package com.example.patienthealthmonitoring.data.repository

import com.example.patienthealthmonitoring.data.local.dao.StaffDao
import com.example.patienthealthmonitoring.data.local.entity.StaffEntity
import com.example.patienthealthmonitoring.util.PasswordHasher

class AuthRepository(private val staffDao: StaffDao) {
    suspend fun login(identifier: String, password: String): LoginResult {
        val staff = staffDao.getStaffByIdentifier(identifier.trim())
            ?: return LoginResult.InvalidIdentifier

        return if (PasswordHasher.verify(password, staff.passwordHash)) {
            LoginResult.Success(staff)
        } else {
            LoginResult.IncorrectPassword
        }
    }

    suspend fun register(fullName: String, staffId: String, password: String): RegistrationResult {
        if (staffDao.countByStaffId(staffId.trim()) > 0) {
            return RegistrationResult.DuplicateStaffId
        }

        val staff = StaffEntity(
            staffId = staffId.trim(),
            name = fullName.trim(),
            passwordHash = PasswordHasher.hash(password)
        )
        staffDao.insertStaff(staff)
        return RegistrationResult.Success
    }
}

sealed class LoginResult {
    data class Success(val staff: StaffEntity) : LoginResult()
    object InvalidIdentifier : LoginResult()
    object IncorrectPassword : LoginResult()
}

sealed class RegistrationResult {
    object Success : RegistrationResult()
    object DuplicateStaffId : RegistrationResult()
}
