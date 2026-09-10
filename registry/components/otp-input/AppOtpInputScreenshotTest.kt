package com.droidkit.registry.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppOtpInputScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun lightPreviewStates() {
        composeRule.setContent { AppOtpInputPreview() }
        composeRule.onAllNodesWithContentDescription("One-time code").assertCountEquals(4)
    }

    @Test
    fun darkPreviewStates() {
        composeRule.setContent { AppOtpInputDarkPreview() }
        composeRule.onAllNodesWithContentDescription("One-time code").assertCountEquals(1)
    }
}
