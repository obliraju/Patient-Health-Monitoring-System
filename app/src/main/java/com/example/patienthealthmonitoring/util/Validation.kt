package com.example.patienthealthmonitoring.util

object Validation {
    fun isValidAge(value: String): Boolean {
        val age = value.toIntOrNull() ?: return false
        return age in 0..130
    }

    fun isValidPhone(value: String): Boolean {
        val digits = value.filter { it.isDigit() }
        return digits.length in 10..15
    }

    fun isValidAadhaar(value: String): Boolean {
        if (value.isBlank()) return true
        return value.filter { it.isDigit() }.length == 12
    }

    fun isPositiveDecimal(value: String): Boolean {
        if (value.isBlank()) return true
        return value.toDoubleOrNull()?.let { it > 0 } == true
    }

    fun isValidPercentage(value: String): Boolean {
        val percentage = value.toDoubleOrNull() ?: return false
        return percentage in 0.0..100.0
    }
}
