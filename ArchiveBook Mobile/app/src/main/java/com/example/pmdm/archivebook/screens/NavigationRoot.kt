package com.example.pmdm.archivebook.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable
data class LoginScreenKey(val id: String = "Login") : NavKey

@Serializable
data class HomeScreenKey(val id: String = "Home") : NavKey


@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {

    val backStack = rememberNavBackStack(LoginScreenKey())

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {
            entry<LoginScreenKey> { navElement ->
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(HomeScreenKey())
                    }
                )
            }

            entry<HomeScreenKey> { navElement ->
                HomeScreen()
            }
        }
    )
}
