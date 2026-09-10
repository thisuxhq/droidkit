package com.droidkit.registry.blocks

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsScreenScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun lightPreviewStates() {
        composeRule.setContent { SettingsScreenPreview() }
        composeRule.onNodeWithText("Notifications").assertIsDisplayed()
        composeRule.onNodeWithText("Log out").assertIsDisplayed()
    }

    @Test
    fun darkPreviewStates() {
        composeRule.setContent { SettingsScreenDarkPreview() }
        composeRule.onNodeWithText("Dark").assertIsDisplayed()
    }
}
