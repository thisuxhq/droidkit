package com.droidkit.registry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.droidkit.registry.theme.AppTheme

private val ButtonHeight = 52.dp
private val ButtonCorner = 14.dp
private val ButtonHorizontalPadding = 20.dp
private val ButtonVerticalPadding = 12.dp
private val ButtonSpinnerSize = 18.dp
private val ButtonSpinnerStroke = 2.dp
private const val DefaultLoadingStateDescription = "Loading"
private const val InspectionSpinnerProgress = 0.75f

// idle → loading: clicks ignored; label stays in the tree (invisible) so width holds;
// spinner overlays; enabled stays the caller's value so colours stay primary.
// loading → idle: label visible again; spinner gone; clicks fire.
// disabled: Material disabled colours; clicks do not fire. Loading never forces disabled.

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    loadingStateDescription: String = DefaultLoadingStateDescription,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(ButtonCorner),
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    Button(
        onClick = { if (!loading) onClick() },
        modifier =
            modifier
                .heightIn(min = ButtonHeight)
                .semantics {
                    if (loading) {
                        stateDescription = loadingStateDescription
                    }
                },
        enabled = enabled,
        shape = shape,
        colors = colors,
        contentPadding =
            PaddingValues(
                horizontal = ButtonHorizontalPadding,
                vertical = ButtonVerticalPadding,
            ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.alpha(if (loading) 0f else 1f),
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
            if (loading) {
                ButtonSpinner(color = LocalContentColor.current)
            }
        }
    }
}

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
