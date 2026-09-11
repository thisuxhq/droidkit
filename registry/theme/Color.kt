package com.droidkit.registry.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val LightBackground = Color(0xFFF7F4EF)
private val LightSurface = Color(0xFFFFFCF8)
private val LightAccent = Color(0xFF1C2B24)
private val LightOnAccent = Color(0xFFF7F4EF)
private val LightOnBackground = Color(0xFF1A1916)
private val LightMuted = Color(0xFF6B6560)
private val LightDanger = Color(0xFFB42318)

private val DarkBackground = Color(0xFF141311)
private val DarkSurface = Color(0xFF1E1C1A)
private val DarkAccent = Color(0xFFC8E6C9)
private val DarkOnAccent = Color(0xFF1C2B24)
private val DarkOnBackground = Color(0xFFF4F0EA)
private val DarkMuted = Color(0xFFA39E98)
private val DarkDanger = Color(0xFFFF8A7A)

// Every ColorScheme role is mapped from the tokens above so Material defaults
// (purple primary, etc.) never leak through an unset parameter.
internal val LightColors: ColorScheme =
    lightColorScheme(
        primary = LightAccent,
        onPrimary = LightOnAccent,
        primaryContainer = LightSurface,
        onPrimaryContainer = LightOnBackground,
        inversePrimary = DarkAccent,
        secondary = LightMuted,
        onSecondary = LightOnAccent,
        secondaryContainer = LightSurface,
        onSecondaryContainer = LightOnBackground,
        tertiary = LightMuted,
        onTertiary = LightOnAccent,
        tertiaryContainer = LightSurface,
        onTertiaryContainer = LightOnBackground,
        background = LightBackground,
        onBackground = LightOnBackground,
        surface = LightSurface,
        onSurface = LightOnBackground,
        surfaceVariant = LightSurface,
        onSurfaceVariant = LightMuted,
        surfaceTint = LightAccent,
        inverseSurface = DarkSurface,
        inverseOnSurface = DarkOnBackground,
        error = LightDanger,
        onError = LightOnAccent,
        errorContainer = LightSurface,
        onErrorContainer = LightDanger,
        outline = LightMuted,
        outlineVariant = LightMuted,
        scrim = DarkBackground,
        surfaceBright = LightSurface,
        surfaceDim = LightBackground,
        surfaceContainer = LightSurface,
        surfaceContainerHigh = LightSurface,
        surfaceContainerHighest = LightSurface,
        surfaceContainerLow = LightSurface,
        surfaceContainerLowest = LightBackground,
        primaryFixed = LightAccent,
        primaryFixedDim = LightAccent,
        onPrimaryFixed = LightOnAccent,
        onPrimaryFixedVariant = LightOnAccent,
        secondaryFixed = LightMuted,
        secondaryFixedDim = LightMuted,
        onSecondaryFixed = LightOnAccent,
        onSecondaryFixedVariant = LightOnAccent,
        tertiaryFixed = LightMuted,
        tertiaryFixedDim = LightMuted,
        onTertiaryFixed = LightOnAccent,
        onTertiaryFixedVariant = LightOnAccent,
    )

internal val DarkColors: ColorScheme =
    darkColorScheme(
        primary = DarkAccent,
        onPrimary = DarkOnAccent,
        primaryContainer = DarkSurface,
        onPrimaryContainer = DarkOnBackground,
        inversePrimary = LightAccent,
        secondary = DarkMuted,
        onSecondary = DarkOnAccent,
        secondaryContainer = DarkSurface,
        onSecondaryContainer = DarkOnBackground,
        tertiary = DarkMuted,
        onTertiary = DarkOnAccent,
        tertiaryContainer = DarkSurface,
        onTertiaryContainer = DarkOnBackground,
        background = DarkBackground,
        onBackground = DarkOnBackground,
        surface = DarkSurface,
        onSurface = DarkOnBackground,
        surfaceVariant = DarkSurface,
        onSurfaceVariant = DarkMuted,
        surfaceTint = DarkAccent,
        inverseSurface = LightSurface,
        inverseOnSurface = LightOnBackground,
        error = DarkDanger,
        onError = DarkOnAccent,
        errorContainer = DarkSurface,
        onErrorContainer = DarkDanger,
        outline = DarkMuted,
        outlineVariant = DarkMuted,
        scrim = DarkBackground,
        surfaceBright = DarkSurface,
        surfaceDim = DarkBackground,
        surfaceContainer = DarkSurface,
        surfaceContainerHigh = DarkSurface,
        surfaceContainerHighest = DarkSurface,
        surfaceContainerLow = DarkSurface,
        surfaceContainerLowest = DarkBackground,
        primaryFixed = DarkAccent,
        primaryFixedDim = DarkAccent,
        onPrimaryFixed = DarkOnAccent,
        onPrimaryFixedVariant = DarkOnAccent,
        secondaryFixed = DarkMuted,
        secondaryFixedDim = DarkMuted,
        onSecondaryFixed = DarkOnAccent,
        onSecondaryFixedVariant = DarkOnAccent,
        tertiaryFixed = DarkMuted,
        tertiaryFixedDim = DarkMuted,
        onTertiaryFixed = DarkOnAccent,
        onTertiaryFixedVariant = DarkOnAccent,
    )
