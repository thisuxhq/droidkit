package com.droidkit.registry.blocks

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.droidkit.registry.theme.AppTheme

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.
// The block is a full Scaffold, so previews pin a device size instead of wrapping content.

private val previewState = SettingsState(notifications = true, theme = ThemeOption.System)

@Composable
private fun SettingsPreviewSurface(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(darkTheme = darkTheme) {
        content()
    }
}

@Preview(showBackground = true, name = "settings default", widthDp = 412, heightDp = 640)
@Composable
internal fun SettingsScreenDefaultPreview() {
    SettingsPreviewSurface {
        SettingsScreen(
            state = previewState,
            onNotificationsChanged = {},
            onThemeClick = {},
            onLogout = {},
        )
    }
}

@Preview(showBackground = true, name = "settings rtl", widthDp = 412, heightDp = 640)
@Composable
internal fun SettingsScreenRtlPreview() {
    SettingsPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            SettingsScreen(
                state = previewState,
                onNotificationsChanged = {},
                onThemeClick = {},
                onLogout = {},
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "settings dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 412,
    heightDp = 640,
)
@Composable
internal fun SettingsScreenDarkPreview() {
    SettingsPreviewSurface(darkTheme = true) {
        SettingsScreen(
            state = SettingsState(notifications = false, theme = ThemeOption.Dark),
            onNotificationsChanged = {},
            onThemeClick = {},
            onLogout = {},
        )
    }
}

@Preview(showBackground = true, name = "settings large-font", fontScale = 2f, widthDp = 412, heightDp = 640)
@Composable
internal fun SettingsScreenLargeFontPreview() {
    SettingsPreviewSurface {
        SettingsScreen(
            state = previewState,
            onNotificationsChanged = {},
            onThemeClick = {},
            onLogout = {},
        )
    }
}

@Preview(showBackground = true, name = "settings tablet", widthDp = 840, heightDp = 600)
@Composable
internal fun SettingsScreenTabletPreview() {
    SettingsPreviewSurface {
        SettingsScreen(
            state = previewState,
            onNotificationsChanged = {},
            onThemeClick = {},
            onLogout = {},
        )
    }
}
