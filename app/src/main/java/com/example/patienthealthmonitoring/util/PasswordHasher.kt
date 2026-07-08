package com.example.patienthealthmonitoring.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PasswordHasher {
    private const val HASH_ALGORITHM = "SHA-256"
    private const val SALT_BYTES = 16

    fun hash(password: String): String {
        val salt = ByteArray(SALT_BYTES)
        SecureRandom().nextBytes(salt)

        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        digest.update(salt)
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))

        return "${salt.toBase64()}:${hash.toBase64()}"
    }

    fun verify(password: String, storedHash: String): Boolean {
        val parts = storedHash.split(":")
        if (parts.size != 2) return false

        return try {
            val salt = Base64.decode(parts[0], Base64.NO_WRAP)
            val expectedHash = Base64.decode(parts[1], Base64.NO_WRAP)

            val digest = MessageDigest.getInstance(HASH_ALGORITHM)
            digest.update(salt)
            val actualHash = digest.digest(password.toByteArray(Charsets.UTF_8))

            MessageDigest.isEqual(expectedHash, actualHash)
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    private fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.NO_WRAP)
}
