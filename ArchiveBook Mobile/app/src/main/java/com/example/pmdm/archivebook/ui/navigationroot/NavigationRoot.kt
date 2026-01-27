package com.example.pmdm.archivebook.ui.navigationroot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.pmdm.archivebook.ui.screens.LibraryScreen
import com.example.pmdm.archivebook.ui.screens.LoginScreen
import com.example.pmdm.archivebook.ui.screens.RegisterScreen

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {

    // Definimos LoginKey como la pantalla de inicio
    val backStack = rememberNavBackStack(LoginKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {

            // Caso: PANTALLA DE LOGIN
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