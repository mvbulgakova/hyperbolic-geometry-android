package com.hyperbolic.geometry.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.material3.tokens.ColorSchemeKeyTokens.Primary.value,
    secondary = androidx.compose.material3.tokens.ColorSchemeKeyTokens.Secondary.value,
    tertiary = androidx.compose.material3.tokens.ColorSchemeKeyTokens.Tertiary.value
)

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.material3.tokens.ColorSchemeKeyTokens.Primary.value,
    secondary = androidx.compose.material3.tokens.ColorSchemeKeyTokens.Secondary.value,
    tertiary = androidx.compose.material3.tokens.ColorSchemeKeyTokens.Tertiary.value
)

@Composable
fun HyperbolicGeometryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}