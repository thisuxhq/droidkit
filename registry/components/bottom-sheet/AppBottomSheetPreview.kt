package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

private val AppearanceOptions =
    listOf(
        AppRadioOption("system", "System"),
        AppRadioOption("light", "Light"),
        AppRadioOption("dark", "Dark"),
    )

@Composable
private fun AppearanceChoices() {
    AppRadioGroup(
        options = AppearanceOptions,
        selectedId = "system",
        onSelect = {},
    )
}

@Composable
private fun AppearancePeekOrFull() {
    val detent = LocalAppSheetDetent.current
    if (detent == AppSheetDetent.Partial) {
        Text(
            text = "System",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        AppearanceChoices()
    }
}

@Preview(showBackground = true, name = "bottom-sheet default")
@Composable
internal fun BottomSheetDefaultPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(title = "Appearance") {
            AppearanceChoices()
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet floating")
@Composable
internal fun BottomSheetFloatingPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(
            title = "Appearance",
            chrome = AppSheetChrome.Float,
        ) {
            AppearanceChoices()
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet partial")
@Composable
internal fun BottomSheetPartialPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(
            title = "Appearance",
            detent = AppSheetDetent.Partial,
        ) {
            AppearancePeekOrFull()
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet expanded")
@Composable
internal fun BottomSheetExpandedPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(
            title = "Appearance",
            detent = AppSheetDetent.Expanded,
        ) {
            AppearancePeekOrFull()
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet with-footer")
@Composable
internal fun BottomSheetWithFooterPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(
            title = "Appearance",
            footer = { AppButton(text = "Save", onClick = {}) },
        ) {
            AppearanceChoices()
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet rtl")
@Composable
internal fun BottomSheetRtlPreview() {
    BottomSheetPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppBottomSheetChrome(title = "Appearance") {
                AppearanceChoices()
            }
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun BottomSheetDarkPreview() {
    BottomSheetPreviewSurface(darkTheme = true) {
        AppBottomSheetChrome(title = "Appearance") {
            AppearanceChoices()
        }
    }
}

@Preview(showBackground = true, name = "bottom-sheet large-font", fontScale = 2f)
@Composable
internal fun BottomSheetLargeFontPreview() {
    BottomSheetPreviewSurface {
        AppBottomSheetChrome(title = "Appearance") {
            AppearanceChoices()
        }
    }
}
