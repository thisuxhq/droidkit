package com.droidkit.registry.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp

private val ButtonHeight = 52.dp
private val ButtonCorner = 14.dp
private val ButtonHorizontalPadding = 20.dp
private val ButtonVerticalPadding = 12.dp
private val ButtonSpinnerSize = 18.dp
private val ButtonSpinnerStroke = 2.dp

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .height(ButtonHeight)
                .semantics {
                    if (loading) {
                        stateDescription = "Loading"
                        liveRegion = LiveRegionMode.Polite
                    }
                },
        enabled = enabled && !loading,
        shape = RoundedCornerShape(ButtonCorner),
        contentPadding =
            PaddingValues(
                horizontal = ButtonHorizontalPadding,
                vertical = ButtonVerticalPadding,
            ),
    ) {
        AnimatedContent(
            targetState = loading,
            label = "button-loading",
        ) { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(ButtonSpinnerSize),
                    strokeWidth = ButtonSpinnerStroke,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
