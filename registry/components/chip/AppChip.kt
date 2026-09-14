package com.droidkit.registry.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics

private val ChipMinHeight = 48.dp

/**
 * A filter you turn on and off. Not an action — that is a button.
 *
 * reach:    pressScale + click; the chip is 48 dp tall.
 * act:      selecting fires a tick haptic; TalkBack reads selected / not selected.
 * see:      selected is a filled chip, not a different widget.
 */
@Composable
fun AppChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberAppHaptics()

    FilterChip(
        selected = selected,
        onClick = {
            haptics.tick()
            onClick()
        },
        label = { Text(text = text) },
        modifier =
            modifier
                .heightIn(min = ChipMinHeight)
                .pressScale(interactionSource = interactionSource, enabled = enabled),
        enabled = enabled,
        leadingIcon = leadingIcon,
        interactionSource = interactionSource,
    )
}
