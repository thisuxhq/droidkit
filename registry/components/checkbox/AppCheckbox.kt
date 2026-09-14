package com.droidkit.registry.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics

private val CheckboxRowMinHeight = 48.dp

/**
 * A labeled checkbox row. The words are the control.
 *
 * reach:    the whole row is 48 dp and pressScale + click.
 * act:      checking fires a tick haptic; TalkBack reads one Checkbox.
 * see:      [description] sits under the title when the choice needs a sentence.
 */
@Composable
fun AppCheckbox(
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
        leadingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = null,
                enabled = enabled,
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = CheckboxRowMinHeight)
                .pressScale(interactionSource = interactionSource, enabled = enabled)
                .toggleable(
                    value = checked,
                    enabled = enabled,
                    role = Role.Checkbox,
                    interactionSource = interactionSource,
                    indication = null,
                    onValueChange = { next ->
                        haptics.tick()
                        onCheckedChange(next)
                    },
                ),
    )
}
