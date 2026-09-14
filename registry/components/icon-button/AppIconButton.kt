package com.droidkit.registry.components

import android.os.SystemClock
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberLoadingVisibility

private val IconButtonSize = 48.dp
private val IconButtonSpinnerSize = 18.dp
private val IconButtonSpinnerStroke = 2.dp
private const val DefaultLoadingStateDescription = "Loading"
private const val DoubleTapGuardMillis = 400L
private const val InspectionSpinnerProgress = 0.75f

/**
 * Icon-only action that must say its name.
 *
 * reach:    pressScale + click haptic; ripple and scale share one interactionSource.
 * act:      a second tap within 400 ms is ignored; taps are ignored while loading.
 * loading:  the spinner appears after 150 ms and stays at least 500 ms; the icon stays
 *           in the tree at alpha 0 so the hit target never shrinks. Loading is not disabled.
 * see:      [contentDescription] is required — there is no silent icon.
 */
@Composable
fun AppIconButton(
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = DefaultLoadingStateDescription,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberAppHaptics()
    val showSpinner = rememberLoadingVisibility(loading)
    var lastClickAt by remember { mutableLongStateOf(0L) }

    IconButton(
        onClick = {
            val now = SystemClock.uptimeMillis()
            val guarded = lastClickAt != 0L && now - lastClickAt < DoubleTapGuardMillis
            if (!loading && !guarded) {
                lastClickAt = now
                haptics.click()
                onClick()
            }
        },
        modifier =
            modifier
                .size(IconButtonSize)
                .pressScale(interactionSource = interactionSource, enabled = enabled && !loading)
                .semantics {
                    this.contentDescription = contentDescription
                    if (loading) stateDescription = loadingStateDescription
                },
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.alpha(if (showSpinner) 0f else 1f)) {
                content()
            }
            if (showSpinner) {
                IconButtonSpinner(color = LocalContentColor.current)
            }
        }
    }
}

@Composable
private fun IconButtonSpinner(color: Color) {
    val spinnerModifier =
        Modifier
            .size(IconButtonSpinnerSize)
            .clearAndSetSemantics { }
    if (LocalInspectionMode.current) {
        CircularProgressIndicator(
            progress = { InspectionSpinnerProgress },
            modifier = spinnerModifier,
            color = color,
            strokeWidth = IconButtonSpinnerStroke,
            trackColor = Color.Transparent,
        )
    } else {
        CircularProgressIndicator(
            modifier = spinnerModifier,
            color = color,
            strokeWidth = IconButtonSpinnerStroke,
            trackColor = Color.Transparent,
        )
    }
}
