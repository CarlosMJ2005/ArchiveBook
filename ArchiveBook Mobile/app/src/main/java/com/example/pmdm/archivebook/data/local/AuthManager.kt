package com.example.pmdm.archivebook.data.local

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    // SharedPreferences es la forma estándar de guardar strings pequeños como tokens
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("jwt_token", null)

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }
}