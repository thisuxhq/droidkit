package com.droidkit.registry.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.theme.AppTheme

private val CardMinHeight = 48.dp
private val CardHairline = 1.dp

/**
 * A titled surface that is one thing, not a box with elevation.
 *
 * see:      title + one supporting line; TalkBack reads them as one item.
 * act:      the clickable overload is the whole card; the action overload keeps the
 *           button outside the merged copy. Never both.
 */
@Composable
fun AppCard(
    title: String,
    supporting: String,
    modifier: Modifier = Modifier,
) {
    AppCardContent(
        title = title,
        supporting = supporting,
        onClick = null,
        action = null,
        onAction = null,
        modifier = modifier,
    )
}

@Composable
fun AppCard(
    title: String,
    supporting: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCardContent(
        title = title,
        supporting = supporting,
        onClick = onClick,
        action = null,
        onAction = null,
        modifier = modifier,
    )
}

@Composable
fun AppCard(
    title: String,
    supporting: String,
    action: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCardContent(
        title = title,
        supporting = supporting,
        onClick = null,
        action = action,
        onAction = onAction,
        modifier = modifier,
    )
}

@Composable
private fun AppCardContent(
    title: String,
    supporting: String,
    onClick: (() -> Unit)?,
    action: String?,
    onAction: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberAppHaptics()
    val colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    val elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.elevation.flat)
    val border = BorderStroke(CardHairline, MaterialTheme.colorScheme.outline)
    val body: @Composable () -> Unit = {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
        ) {
            Column(
                modifier = Modifier.semantics(mergeDescendants = true) {},
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = supporting,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (action != null && onAction != null) {
                AppButton(text = action, onClick = onAction)
            }
        }
    }

    if (onClick != null) {
        Card(
            onClick = {
                haptics.click()
                onClick()
            },
            modifier =
                modifier
                    .fillMaxWidth()
                    .heightIn(min = CardMinHeight)
                    .pressScale(interactionSource = interactionSource),
            interactionSource = interactionSource,
            colors = colors,
            elevation = elevation,
            border = border,
            content = { body() },
        )
    } else {
        Card(
            modifier =
                modifier
                    .fillMaxWidth()
                    .heightIn(min = CardMinHeight),
            colors = colors,
            elevation = elevation,
            border = border,
            content = { body() },
        )
    }
}
