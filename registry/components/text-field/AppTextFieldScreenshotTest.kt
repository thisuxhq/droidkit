package com.droidkit.registry.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTextFieldScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun lightPreviewStates() {
        composeRule.setContent { AppTextFieldPreview() }
        composeRule.onNodeWithText("Enter a valid email").assertIsDisplayed()
        composeRule.onNodeWithText("Disabled").assertIsDisplayed()
    }

    @Test
    fun darkPreviewStates() {
        composeRule.setContent { AppTextFieldDarkPreview() }
        composeRule.onNodeWithText("you@example.com").assertIsDisplayed()
    }
}
