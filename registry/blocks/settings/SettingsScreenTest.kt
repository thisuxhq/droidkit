package com.droidkit.registry.blocks

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
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
                    onThemeClick = {},
                    onLogout = { loggedOut = true },
                )
            }
        }

        composeRule.onNodeWithText("Settings").assertIsDisplayed()
        composeRule.onNodeWithText("Log out").performClick()
        assertTrue(loggedOut)
    }

    @Test
    fun notificationsToggleCallsCallback() {
        var checked: Boolean? = null

        composeRule.setContent {
            AppTheme {
                SettingsScreen(
                    state = SettingsState(notifications = true, theme = ThemeOption.Light),
                    onNotificationsChanged = { checked = it },
                    onThemeClick = {},
                    onLogout = {},
                )
            }
        }

        composeRule.onNodeWithText("Notifications").performClick()
        assertEquals(false, checked)
    }

    @Test
    fun appearanceOpensDetailAndThemeClickFires() {
        var themeClicked = false

        composeRule.setContent {
            AppTheme {
                SettingsScreen(
                    state = SettingsState(notifications = true, theme = ThemeOption.Light),
                    onNotificationsChanged = {},
                    onThemeClick = { themeClicked = true },
                    onLogout = {},
                )
            }
        }

        composeRule.onNodeWithText("Appearance").performClick()
        composeRule.onNodeWithText("Change theme").assertIsDisplayed()
        composeRule.onNodeWithText("Change theme").performClick()
        assertTrue(themeClicked)
    }
}
