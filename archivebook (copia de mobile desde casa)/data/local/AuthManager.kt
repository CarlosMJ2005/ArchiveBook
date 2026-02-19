package com.example.pmdm.archivebook.data.local

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    // SharedPreferences es la forma estándar de guardar strings pequeños como tokens
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String, email: String) {
        prefs.edit()
            .putString("jwt_token", token)
            .putString("user_email", email)
            .apply()
    }

    fun getToken(): String? = prefs.getString("jwt_token", null)
    fun getEmail(): String? = prefs.getString("user_email", null)

    fun clearToken() {
        prefs.edit()
            .remove("jwt_token")
            .remove("user_email")
            .apply()
    }
}