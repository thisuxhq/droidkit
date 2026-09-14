package com.droidkit.registry.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics

private val RadioRowMinHeight = 48.dp

data class AppRadioOption(
    val id: String,
    val label: String,
    val description: String? = null,
)

/**
 * Exclusive labeled choices. Picking one unpicks the others.
 *
 * see:      the selected row is the one with the filled radio.
 * reach:    each row is 48 dp and pressScale + click.
 * act:      selecting fires a tick haptic; TalkBack reads one RadioButton per row in a group.
 */
@Composable
fun AppRadioGroup(
    options: List<AppRadioOption>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val haptics = rememberAppHaptics()

    Column(modifier = modifier.selectableGroup()) {
        options.forEach { option ->
            val selected = option.id == selectedId
            val interactionSource = remember(option.id) { MutableInteractionSource() }
            ListItem(
                headlineContent = {
                    Text(text = option.label, style = MaterialTheme.typography.titleMedium)
                },
                supportingContent =
                    option.description?.let {
                        {
                            Text(text = it, style = MaterialTheme.typography.bodyMedium)
                        }
                    },
                leadingContent = {
                    RadioButton(
                        selected = selected,
                        onClick = null,
                        enabled = enabled,
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = RadioRowMinHeight)
                        .pressScale(interactionSource = interactionSource, enabled = enabled)
                        .selectable(
                            selected = selected,
                            enabled = enabled,
                            role = Role.RadioButton,
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = {
                                if (!selected) {
                                    haptics.tick()
                                    onSelect(option.id)
                                }
                            },
                        ),
            )
        }
    }
}
