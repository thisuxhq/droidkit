package com.droidkit.registry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.droidkit.registry.theme.AppTheme

data class AppPreferenceOption(
    val id: String,
    val label: String,
    val description: String? = null,
)

/**
 * A titled exclusive list. The current value is the checked row.
 *
 * see:      [title] names the preference; the checked row is the current value.
 * act:      picking a row ticks and calls [onSelect]; the caller dismisses the host.
 * leave:    this does not own a sheet or a dialog — drop it in one.
 */
@Composable
fun AppPreferencePicker(
    title: String,
    options: List<AppPreferenceOption>,
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        AppRadioGroup(
            options =
                options.map { option ->
                    AppRadioOption(
                        id = option.id,
                        label = option.label,
                        description = option.description,
                    )
                },
            selectedId = selectedId,
            onSelect = onSelect,
            enabled = enabled,
        )
    }
}
