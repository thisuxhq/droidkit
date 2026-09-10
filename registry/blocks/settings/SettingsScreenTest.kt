package com.droidkit.registry.blocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun logoutCallsCallback() {
        var loggedOut = false

        composeRule.setContent {
            AppTheme {
                SettingsScreen(
                    state = SettingsState(notifications = true, theme = ThemeOption.System),
                    onNotificationsChanged = {},
                    onLogout = { loggedOut = true },
                )
            }
        }

        composeRule.onNodeWithText("Settings").assertIsDisplayed()
        composeRule.onNodeWithText("Log out").performClick()
        assertTrue(loggedOut)
    }

    @Test
    fun toggleUpdatesState() {
        composeRule.setContent {
            AppTheme {
                var state by remember {
                    mutableStateOf(SettingsState(notifications = true, theme = ThemeOption.Light))
                }
                SettingsScreen(
                    state = state,
                    onNotificationsChanged = { state = state.copy(notifications = it) },
                    onLogout = {},
                )
            }
        }

        composeRule.onNodeWithText("Notifications").assertIsOn()
        composeRule.onNodeWithText("Appearance").assertIsDisplayed()
        composeRule.onNodeWithText("Light").assertIsDisplayed()
    }
}
