package com.example.pmdm.archivebook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Use the variables we defined in Color.kt
private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,        // Brown Buttons
    onPrimary = LightOnPrimary,    // Cream text on Brown buttons
    secondary = LightSecondary,
    background = LightBackground,  // Cream Screen background
    surface = LightBackground,     // Matches background for seamless TextFields
    onBackground = LightPrimary,   // Brown text on the Cream background
    onSurface = LightPrimary       // Brown text inside fields
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,         // Cream Buttons
    onPrimary = DarkOnPrimary,     // Brown text on Cream buttons
    secondary = DarkSecondary,
    background = DarkBackground,   // Brown Screen background
    surface = DarkBackground,      // Matches background for seamless TextFields
    onBackground = DarkPrimary,    // Cream text on the Brown background
    onSurface = DarkPrimary,       // Cream text inside fields
    error = Color(0xFFF2B8B5)      // Light pinkish-red for errors on dark brown
)

@Composable
fun ArchiveBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        // If Typography causes errors, leave it out for now
        content = content
    )
}