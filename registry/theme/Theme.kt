package com.droidkit.registry.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

const val THEME_VERSION = 1

// light ↔ dark: only the Material ColorScheme flips (`darkTheme` / system setting).
// spacing, motion, and elevation stay the same. Do not wrap content in a second MaterialTheme.

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalSpacing provides AppSpacing(),
        LocalMotion provides AppMotion(),
        LocalElevation provides AppElevation(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

object AppTheme {
    val spacing: AppSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val motion: AppMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalMotion.current

    val elevation: AppElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalElevation.current
}
