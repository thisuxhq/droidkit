package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.droidkit.registry.theme.AppTheme

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

@Composable
private fun PreferencePickerPreviewSurface(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(AppTheme.spacing.lg)) {
                content()
            }
        }
    }
}

@Preview(showBackground = true, name = "preference-picker default")
@Composable
internal fun PreferencePickerDefaultPreview() {
    PreferencePickerPreviewSurface {
        AppPreferencePicker(
            title = "Appearance",
            options = listOf(
                AppPreferenceOption("system", "System"),
                AppPreferenceOption("light", "Light"),
                AppPreferenceOption("dark", "Dark"),
            ),
            selectedId = "system",
            onSelect = {},
        )
    }
}

@Preview(showBackground = true, name = "preference-picker selected-end")
@Composable
internal fun PreferencePickerSelectedEndPreview() {
    PreferencePickerPreviewSurface {
        AppPreferencePicker(
            title = "Appearance",
            options = listOf(
                AppPreferenceOption("system", "System"),
                AppPreferenceOption("light", "Light"),
                AppPreferenceOption("dark", "Dark"),
            ),
            selectedId = "dark",
            onSelect = {},
        )
    }
}

@Preview(showBackground = true, name = "preference-picker disabled")
@Composable
internal fun PreferencePickerDisabledPreview() {
    PreferencePickerPreviewSurface {
        AppPreferencePicker(
            title = "Appearance",
            options = listOf(
                AppPreferenceOption("system", "System"),
                AppPreferenceOption("light", "Light"),
                AppPreferenceOption("dark", "Dark"),
            ),
            selectedId = "system",
            onSelect = {}, enabled = false,
        )
    }
}

@Preview(showBackground = true, name = "preference-picker rtl")
@Composable
internal fun PreferencePickerRtlPreview() {
    PreferencePickerPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppPreferencePicker(
            title = "Appearance",
            options = listOf(
                AppPreferenceOption("system", "System"),
                AppPreferenceOption("light", "Light"),
                AppPreferenceOption("dark", "Dark"),
            ),
            selectedId = "system",
            onSelect = {},
        )
        }
    }
}

@Preview(showBackground = true, name = "preference-picker dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun PreferencePickerDarkPreview() {
    PreferencePickerPreviewSurface(darkTheme = true) {
        AppPreferencePicker(
            title = "Appearance",
            options = listOf(
                AppPreferenceOption("system", "System"),
                AppPreferenceOption("light", "Light"),
                AppPreferenceOption("dark", "Dark"),
            ),
            selectedId = "system",
            onSelect = {},
        )
    }
}

@Preview(showBackground = true, name = "preference-picker large-font", fontScale = 2f)
@Composable
internal fun PreferencePickerLargeFontPreview() {
    PreferencePickerPreviewSurface {
        AppPreferencePicker(
            title = "Appearance",
            options = listOf(
                AppPreferenceOption("system", "System"),
                AppPreferenceOption("light", "Light"),
                AppPreferenceOption("dark", "Dark"),
            ),
            selectedId = "system",
            onSelect = {},
        )
    }
}
