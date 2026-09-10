package com.droidkit.registry.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

const val THEME_VERSION = 1

private val LightColors: ColorScheme =
    lightColorScheme(
        primary = LightAccent,
        onPrimary = LightOnAccent,
        background = LightBackground,
        onBackground = LightOnBackground,
        surface = LightSurface,
        onSurface = LightOnBackground,
        surfaceVariant = LightSurface,
        onSurfaceVariant = LightMuted,
        error = LightDanger,
        onError = LightOnAccent,
        outline = LightMuted,
    )

private val DarkColors: ColorScheme =
    darkColorScheme(
        primary = DarkAccent,
        onPrimary = DarkOnAccent,
        background = DarkBackground,
        onBackground = DarkOnBackground,
        surface = DarkSurface,
        onSurface = DarkOnBackground,
        surfaceVariant = DarkSurface,
        onSurfaceVariant = DarkMuted,
        error = DarkDanger,
        onError = DarkOnAccent,
        outline = DarkMuted,
    )

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
