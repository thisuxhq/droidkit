package com.droidkit.registry.patterns

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmptyStateScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun lightPreviewStates() {
        composeRule.setContent { EmptyStatePreview() }
        composeRule.onNodeWithText("No projects yet").assertIsDisplayed()
        composeRule.onNodeWithText("Create project").assertIsDisplayed()
    }

    @Test
    fun darkPreviewStates() {
        composeRule.setContent { EmptyStateDarkPreview() }
        composeRule.onNodeWithText("No projects yet").assertIsDisplayed()
    }
}
