package com.droidkit.registry.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics

private val SwitchRowMinHeight = 48.dp

/**
 * A labeled on/off row. The words are the control; the switch is the picture.
 *
 * reach:    the whole row is 48 dp and pressScale + click.
 * act:      toggling fires a tick haptic; TalkBack reads one Switch, not a row plus a switch.
 * see:      [description] sits under the title when the rule needs a sentence.
 */
@Composable
fun AppSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberAppHaptics()

    ListItem(
        headlineContent = {
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        },
        supportingContent =
            description?.let {
                {
                    Text(text = it, style = MaterialTheme.typography.bodyMedium)
                }
            },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = null,
                enabled = enabled,
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = SwitchRowMinHeight)
                .pressScale(interactionSource = interactionSource, enabled = enabled)
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Switch,
                    interactionSource = interactionSource,
                    indication = null,
                    onValueChange = { next ->
                        haptics.tick()
                        onCheckedChange(next)
                    },
                ),
    )
}
