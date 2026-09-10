package com.droidkit.showcase.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.droidkit.registry.blocks.SettingsScreen
import com.droidkit.registry.blocks.SettingsState
import com.droidkit.registry.blocks.ThemeOption

@Composable
fun SettingsDemoScreen(onBack: () -> Unit) {
    var state by remember {
        mutableStateOf(SettingsState(notifications = true, theme = ThemeOption.System))
    }
    var loggedOut by remember { mutableStateOf(false) }

    BackHandler(onBack = onBack)

    SettingsScreen(
        state = state.copy(theme = if (loggedOut) ThemeOption.Light else state.theme),
        onNotificationsChanged = { state = state.copy(notifications = it) },
        onLogout = {
            loggedOut = true
            onBack()
        },
        onThemeClick = {
            state =
                state.copy(
                    theme =
                        when (state.theme) {
                            ThemeOption.System -> ThemeOption.Light
                            ThemeOption.Light -> ThemeOption.Dark
                            ThemeOption.Dark -> ThemeOption.System
                        },
                )
        },
    )
}
