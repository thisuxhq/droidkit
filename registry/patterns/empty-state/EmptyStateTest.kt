package com.droidkit.registry.patterns

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmptyStateTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun actionCallsCallback() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                EmptyState(
                    title = "No projects yet",
                    description = "Create your first project to get started.",
                    action = "Create project",
                    onAction = { clicked = true },
                )
            }
        }

        composeRule.onNodeWithText("No projects yet").assertIsDisplayed()
        composeRule.onNodeWithText("Create project").performClick()
        assertTrue(clicked)
    }

    @Test
    fun hidesActionWhenMissing() {
        composeRule.setContent {
            AppTheme {
                EmptyState(
                    title = "No projects yet",
                    description = "Create your first project to get started.",
                    action = "Create project",
                    onAction = null,
                )
            }
        }

        composeRule.onAllNodesWithText("Create project").assertCountEquals(0)
    }
}
