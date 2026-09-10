package com.droidkit.registry.blocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.droidkit.registry.theme.AppTheme

@Preview(showBackground = true, name = "settings light")
@Composable
internal fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(
            state =
                SettingsState(
                    notifications = true,
                    theme = ThemeOption.System,
                ),
            onNotificationsChanged = {},
            onLogout = {},
        )
    }
}

@Preview(showBackground = true, name = "settings dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SettingsScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        SettingsScreen(
            state =
                SettingsState(
                    notifications = false,
                    theme = ThemeOption.Dark,
                ),
            onNotificationsChanged = {},
            onLogout = {},
        )
    }
}
