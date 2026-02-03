package com.example.pmdm.archivebook.di

import android.util.Log
import com.example.pmdm.archivebook.BuildConfig
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.auth.repository.AuthRepositoryImpl
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.LibraryRepositoryImpl
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import com.example.pmdm.archivebook.presentation.LibraryViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // 1. Persistencia local
    single { AuthManager(androidContext()) }

    // 2. Cliente de Red (Ktor)
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            // AGREGAR ESTO:
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HTTP_KTOR", message)
                    }
                }
            }
            defaultRequest {
                url(BuildConfig.BASE_URL) // Esto toma el valor del build.gradle
            }
        }
    }

    // 3. Servicios de API
    single<AuthApiService> {
        AuthApiService(
            get()
        ) }


    single<AuthRepository> {
        AuthRepositoryImpl(
            apiService = get(),
            authManager = get()
        )
    }

    single<LibraryRepository> { LibraryRepositoryImpl(get()) }

    // 5. ViewModels
    viewModelOf(::LibraryViewModel)
}