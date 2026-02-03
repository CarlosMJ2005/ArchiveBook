package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.pmdm.archivebook.presentation.screens.LibraryScreen
import com.example.pmdm.archivebook.presentation.screens.LoginScreen
import com.example.pmdm.archivebook.presentation.screens.RegisterScreen
import androidx.compose.ui.platform.LocalContext
import com.example.pmdm.archivebook.data.LoginRepositoryImpl
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.di.LoginViewModelFactory
import com.example.pmdm.archivebook.di.RegisterViewModelFactory
import io.ktor.client.HttpClient
import org.koin.compose.koinInject


@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // 1. Inyectamos el servicio configurado de Koin
    val authApiService: AuthApiService = koinInject()

    // 2. Definimos el repositorio con el servicio REAL
    val repository = remember {
        LoginRepositoryImpl(
            context = context,
            authApiService = authApiService
        )
    }

    // 3. Definimos las factories
    val loginFactory = remember { LoginViewModelFactory(repository) }
    val registerFactory = remember { RegisterViewModelFactory(repository) }

    val backStack = rememberNavBackStack(LoginScreenKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {

            entry<LoginScreenKey> {
                LoginScreen(
                    factory = loginFactory, // Ahora 'loginFactory' es visible
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(LibraryScreenKey)
                    },
                    onRegisterClick = {
                        backStack.add(RegisterScreenKey)
                    }
                )
            }

            entry<RegisterScreenKey> {
                RegisterScreen(
                    // USAMOS 'registerFactory' que definimos arriba
                    factory = registerFactory,
                    onRegisterSuccess = {
                        backStack.clear()
                        backStack.add(LibraryScreenKey)
                    },
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginScreenKey)
                    }
                )
            }

            entry<LibraryScreenKey> {
                LibraryScreen(
                    onLogout = {
                        backStack.clear()
                        backStack.add(LoginScreenKey)
                    }
                )
            }
        }
    )
}