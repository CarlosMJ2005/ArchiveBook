package com.example.pmdm.archivebook.utils

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(10, password.toCharArray())
    }
}