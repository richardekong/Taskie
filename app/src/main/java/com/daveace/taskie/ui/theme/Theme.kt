package com.daveace.taskie.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = light,
    onPrimary = dark,
    background = dark,
    onBackground = light,
    surface = dark,
    onSurface = light
)

private val LightColorScheme = lightColorScheme(
    primary = dark,
    onPrimary = light,
    background = light,
    onBackground = dark,
    surface = light,
    onSurface = dark
)

@Composable
fun TaskieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}