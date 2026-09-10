package com.droidkit.registry.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppPasswordFieldScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun lightPreviewStates() {
        composeRule.setContent { AppPasswordFieldPreview() }
        composeRule.onNodeWithText("Use at least 8 characters").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Show password").assertIsDisplayed()
    }

    @Test
    fun darkPreviewStates() {
        composeRule.setContent { AppPasswordFieldDarkPreview() }
        composeRule.onNodeWithContentDescription("Show password").assertIsDisplayed()
    }
}
