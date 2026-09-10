package com.droidkit.registry.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppMotion(
    val quick: Int = 150,
    val normal: Int = 300,
    val slow: Int = 450,
)

data class AppElevation(
    val flat: Dp = 0.dp,
    val floating: Dp = 4.dp,
    val overlay: Dp = 8.dp,
)

val LocalMotion = staticCompositionLocalOf { AppMotion() }

val LocalElevation = staticCompositionLocalOf { AppElevation() }
