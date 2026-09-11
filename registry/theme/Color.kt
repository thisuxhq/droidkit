package com.droidkit.registry.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Ink on paper. Primary is the dark pill, not a colourful brand.
// Tertiary is a reserved signal (progress, location) — do not promote it to actions.

private val LightInk = Color(0xFF0A0A0A)
private val LightOnInk = Color(0xFFF7F7F7)
private val LightPaper = Color(0xFFFFFFFF)
private val LightWash = Color(0xFFF4F4F4)
private val LightMuted = Color(0xFF6B6B6B)
private val LightHairline = Color(0xFFE6E6E6)
private val LightSignal = Color(0xFF3A5F8A)
private val LightDanger = Color(0xFFB42318)

private val DarkInk = Color(0xFFF2F2F2)
private val DarkOnInk = Color(0xFF0A0A0A)
private val DarkPaper = Color(0xFF161616)
private val DarkWash = Color(0xFF0B0B0B)
private val DarkMuted = Color(0xFF9A9A9A)
private val DarkHairline = Color(0xFF2E2E2E)
private val DarkSignal = Color(0xFF7FA3C9)
private val DarkDanger = Color(0xFFFF8A7A)

// Every ColorScheme role is mapped from the tokens above so Material defaults
// (purple primary, etc.) never leak through an unset parameter.
internal val LightColors: ColorScheme =
    lightColorScheme(
        primary = LightInk,
        onPrimary = LightOnInk,
        primaryContainer = LightWash,
        onPrimaryContainer = LightInk,
        inversePrimary = DarkInk,
        secondary = LightMuted,
        onSecondary = LightOnInk,
        secondaryContainer = LightWash,
        onSecondaryContainer = LightInk,
        tertiary = LightSignal,
        onTertiary = LightOnInk,
        tertiaryContainer = LightWash,
        onTertiaryContainer = LightInk,
        background = LightPaper,
        onBackground = LightInk,
        surface = LightPaper,
        onSurface = LightInk,
        surfaceVariant = LightWash,
        onSurfaceVariant = LightMuted,
        surfaceTint = Color.Transparent,
        inverseSurface = DarkPaper,
        inverseOnSurface = DarkInk,
        error = LightDanger,
        onError = LightOnInk,
        errorContainer = LightPaper,
        onErrorContainer = LightDanger,
        outline = LightHairline,
        outlineVariant = LightHairline,
        scrim = DarkWash,
        surfaceBright = LightPaper,
        surfaceDim = LightWash,
        surfaceContainer = LightPaper,
        surfaceContainerHigh = LightPaper,
        surfaceContainerHighest = LightPaper,
        surfaceContainerLow = LightPaper,
        surfaceContainerLowest = LightPaper,
        primaryFixed = LightInk,
        primaryFixedDim = LightInk,
        onPrimaryFixed = LightOnInk,
        onPrimaryFixedVariant = LightOnInk,
        secondaryFixed = LightMuted,
        secondaryFixedDim = LightMuted,
        onSecondaryFixed = LightOnInk,
        onSecondaryFixedVariant = LightOnInk,
        tertiaryFixed = LightSignal,
        tertiaryFixedDim = LightSignal,
        onTertiaryFixed = LightOnInk,
        onTertiaryFixedVariant = LightOnInk,
    )

internal val DarkColors: ColorScheme =
    darkColorScheme(
        primary = DarkInk,
        onPrimary = DarkOnInk,
        primaryContainer = DarkPaper,
        onPrimaryContainer = DarkInk,
        inversePrimary = LightInk,
        secondary = DarkMuted,
        onSecondary = DarkOnInk,
        secondaryContainer = DarkPaper,
        onSecondaryContainer = DarkInk,
        tertiary = DarkSignal,
        onTertiary = DarkOnInk,
        tertiaryContainer = DarkPaper,
        onTertiaryContainer = DarkInk,
        background = DarkWash,
        onBackground = DarkInk,
        surface = DarkPaper,
        onSurface = DarkInk,
        surfaceVariant = DarkPaper,
        onSurfaceVariant = DarkMuted,
        surfaceTint = Color.Transparent,
        inverseSurface = LightPaper,
        inverseOnSurface = LightInk,
        error = DarkDanger,
        onError = DarkOnInk,
        errorContainer = DarkPaper,
        onErrorContainer = DarkDanger,
        outline = DarkHairline,
        outlineVariant = DarkHairline,
        scrim = DarkWash,
        surfaceBright = DarkPaper,
        surfaceDim = DarkWash,
        surfaceContainer = DarkPaper,
        surfaceContainerHigh = DarkPaper,
        surfaceContainerHighest = DarkPaper,
        surfaceContainerLow = DarkPaper,
        surfaceContainerLowest = DarkWash,
        primaryFixed = DarkInk,
        primaryFixedDim = DarkInk,
        onPrimaryFixed = DarkOnInk,
        onPrimaryFixedVariant = DarkOnInk,
        secondaryFixed = DarkMuted,
        secondaryFixedDim = DarkMuted,
        onSecondaryFixed = DarkOnInk,
        onSecondaryFixedVariant = DarkOnInk,
        tertiaryFixed = DarkSignal,
        tertiaryFixedDim = DarkSignal,
        onTertiaryFixed = DarkOnInk,
        onTertiaryFixedVariant = DarkOnInk,
    )
