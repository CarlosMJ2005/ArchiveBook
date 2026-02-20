package com.example.pmdm.archivebook.di

import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.auth.repository.AuthRepositoryImpl
import com.example.pmdm.archivebook.auth.usecase.LogOut
import com.example.pmdm.archivebook.data.LibraryRepositoryImpl
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.data.remote.LibraryApiService
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import com.example.pmdm.archivebook.presentation.BookDetailViewModel
import com.example.pmdm.archivebook.presentation.LibraryViewModel
import com.example.pmdm.archivebook.presentation.LoginViewModel
import com.example.pmdm.archivebook.presentation.RegisterViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // 1. Auth & HTTP
    single { AuthManager(androidContext()) }

    // Definimos el Provider una sola vez
    single { HttpClientProvider(authManager = get()) }

    // ESTA LÍNEA ES VITAL: Extrae el HttpClient del Provider para que otros servicios lo usen
    factory<HttpClient> { get<HttpClientProvider>().get() }

    // 2. Servicios de API
    single { AuthApiService(client = get()) } // Asegúrate de que AuthApiService reciba el cliente
    single { LibraryApiService(client = get(), authManager = get()) }

    // 3. Repositorios
    single<AuthRepository> {
        AuthRepositoryImpl(apiService = get(), authManager = get())
    }
    single<LibraryRepository> { LibraryRepositoryImpl(apiService = get(), authManager = get()) }

    // 4. Casos de Uso
    single { LogOut(authRepository = get(), libraryRepository = get(), httpClientProvider = get()) }

    // 5. ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LibraryViewModel)

    viewModel { params ->
        BookDetailViewModel(
            bookId = params.get<Int>(),
            libraryRepository = get(),
            token = params.get<String>()
        )
    }
}