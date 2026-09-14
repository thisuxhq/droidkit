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
private fun BottomSheetPreviewSurface(
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

@Preview(showBackground = true, name = "bottom-sheet default")
@Composable
internal fun BottomSheetDefaultPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(title = "Appearance") {
            AppRadioGroup(
                options = listOf(
                    AppRadioOption("system", "System"),
                    AppRadioOption("light", "Light"),
                    AppRadioOption("dark", "Dark"),
                ),
                selectedId = "system",
                onSelect = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet long-title")
@Composable
internal fun BottomSheetLongTitlePreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(title = "Choose how this project should look on a large screen") {
            androidx.compose.material3.Text(text = "System, light, or dark.")
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet rtl")
@Composable
internal fun BottomSheetRtlPreview() {
    BottomSheetPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppBottomSheetChrome(title = "Appearance") {
            AppRadioGroup(
                options = listOf(
                    AppRadioOption("system", "System"),
                    AppRadioOption("light", "Light"),
                    AppRadioOption("dark", "Dark"),
                ),
                selectedId = "system",
                onSelect = {},
            )
        }
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun BottomSheetDarkPreview() {
    BottomSheetPreviewSurface(darkTheme = true) {
        AppBottomSheetChrome(title = "Appearance") {
            AppRadioGroup(
                options = listOf(
                    AppRadioOption("system", "System"),
                    AppRadioOption("light", "Light"),
                    AppRadioOption("dark", "Dark"),
                ),
                selectedId = "system",
                onSelect = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet large-font", fontScale = 2f)
@Composable
internal fun BottomSheetLargeFontPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(title = "Appearance") {
            AppRadioGroup(
                options = listOf(
                    AppRadioOption("system", "System"),
                    AppRadioOption("light", "Light"),
                    AppRadioOption("dark", "Dark"),
                ),
                selectedId = "system",
                onSelect = {},
            )
        }
    }
}
