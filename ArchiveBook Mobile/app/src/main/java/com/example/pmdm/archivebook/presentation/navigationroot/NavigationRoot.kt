package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.pmdm.archivebook.data.LoginRepositoryImpl
import com.example.pmdm.archivebook.di.RegisterViewModelFactory
import com.example.pmdm.archivebook.presentation.RegisterViewModel
import com.example.pmdm.archivebook.presentation.screens.LibraryScreen
import com.example.pmdm.archivebook.presentation.screens.LoginScreen
import com.example.pmdm.archivebook.presentation.screens.RegisterScreen

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(LoginKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {

            entry<LoginKey> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(LibraryKey)
                    },
                    onRegisterClick = {
                        backStack.add(RegisterKey)
                    }
                )
            }

            // Caso: PANTALLA DE REGISTRO
            entry<RegisterKey> {
                RegisterScreen(
                    onRegisterSuccess = {
                        backStack.clear()
                        backStack.add(LibraryKey)
                    },
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginKey)
                    }
                )
            }

            // Caso: PANTALLA PRINCIPAL (Biblioteca, buscador y menú lateral)
            entry<LibraryKey> {
                LibraryScreen(
                    onLogout = {
                        backStack.clear()
                        backStack.add(LoginKey)
                    }
                )
            }
        }
    )
}
