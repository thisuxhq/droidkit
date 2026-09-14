package com.droidkit.registry.components

import android.os.SystemClock
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberLoadingVisibility
import com.droidkit.registry.foundation.rememberReducedMotion
import com.droidkit.registry.theme.AppTheme

private val ButtonHeight = 52.dp
private val ButtonCorner = 14.dp
private val ButtonHorizontalPadding = 20.dp
private val TextButtonHorizontalPadding = 12.dp
private val ButtonVerticalPadding = 12.dp
private val ButtonSpinnerSize = 18.dp
private val ButtonSpinnerStroke = 2.dp
private const val DefaultLoadingStateDescription = "Loading"
private const val DefaultSuccessStateDescription = "Done"
private const val DoubleTapGuardMillis = 400L
private const val InspectionSpinnerProgress = 0.75f

/**
 * The primary action. The ink pill that scales to 0.97 under your finger with a click.
 *
 * press:    pressScale + click haptic; the ripple and the scale share one interactionSource.
 * tap:      a second tap within 400 ms of the first is ignored, covering the gap before the
 *           caller flips [loading]. Clicks are also ignored while loading or success.
 * loading:  the spinner appears after 150 ms and stays at least 500 ms (a fast call never flashes
 *           one); the label stays in the tree at alpha 0 so the width never jumps. Loading is
 *           not disabled: colours stay primary and TalkBack reads "Loading".
 * success:  [success] swaps the label for a check with a confirm haptic and reads "Done". The
 *           caller keeps it true for about 800 ms, then navigates.
 * disabled: Material disabled colours; nothing fires.
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    success: Boolean = false,
    loadingStateDescription: String = DefaultLoadingStateDescription,
    successStateDescription: String = DefaultSuccessStateDescription,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(ButtonCorner),
    contentPadding: PaddingValues = ButtonDefaults.appButtonPadding(),
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberAppHaptics()
    val reducedMotion = rememberReducedMotion()
    val showSpinner = rememberLoadingVisibility(loading)
    var lastClickAt by remember { mutableLongStateOf(0L) }

    LaunchedEffect(success) {
        if (success) haptics.confirm()
    }

    val labelAlpha by animateFloatAsState(
        targetValue = if (showSpinner || success) 0f else 1f,
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else AppTheme.motion.quick),
        label = "button-label",
    )

    Button(
        onClick = {
            val now = SystemClock.uptimeMillis()
            val guarded = lastClickAt != 0L && now - lastClickAt < DoubleTapGuardMillis
            if (!loading && !success && !guarded) {
                lastClickAt = now
                haptics.click()
                onClick()
            }
        },
        modifier =
            modifier
                .heightIn(min = ButtonHeight)
                .pressScale(interactionSource = interactionSource, enabled = enabled && !loading)
                .semantics {
                    when {
                        success -> stateDescription = successStateDescription
                        loading -> stateDescription = loadingStateDescription
                    }
                },
        enabled = enabled,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.alpha(labelAlpha),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    Box(
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        contentAlignment = Alignment.Center,
                    ) {
                        leadingIcon()
                    }
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                )
            }
            when {
                success ->
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                showSpinner -> ButtonSpinner(color = LocalContentColor.current)
            }
        }
    }
}

/** Quiet gray fill for the second action beside an [AppButton]: "Not now", "Skip", "Edit". */
@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    AppButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        leadingIcon = leadingIcon,
    )
}

/** Text-only action for low-emphasis choices inside a section or dialog. */
@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    AppButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(),
        contentPadding = ButtonDefaults.appTextButtonPadding(),
        leadingIcon = leadingIcon,
    )
}

/**
 * Destructive action: red text, never a red fill (decisions #18). Put it beside a quiet
 * cancel, and let the confirmation pattern own the "are you sure".
 */
@Composable
fun AppDestructiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    AppButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        loading = loading,
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
        contentPadding = ButtonDefaults.appTextButtonPadding(),
        leadingIcon = leadingIcon,
    )
}

fun ButtonDefaults.appButtonPadding(): PaddingValues =
    PaddingValues(horizontal = ButtonHorizontalPadding, vertical = ButtonVerticalPadding)

fun ButtonDefaults.appTextButtonPadding(): PaddingValues =
    PaddingValues(horizontal = TextButtonHorizontalPadding, vertical = ButtonVerticalPadding)

@Composable
private fun ButtonSpinner(color: Color) {
    val spinnerModifier =
        Modifier
            .size(ButtonSpinnerSize)
            .clearAndSetSemantics { }
    if (LocalInspectionMode.current) {
        CircularProgressIndicator(
            progress = { InspectionSpinnerProgress },
            modifier = spinnerModifier,
            color = color,
            strokeWidth = ButtonSpinnerStroke,
            trackColor = Color.Transparent,
        )
    } else {
        CircularProgressIndicator(
            modifier = spinnerModifier,
            color = color,
            strokeWidth = ButtonSpinnerStroke,
            trackColor = Color.Transparent,
        )
    }
}
