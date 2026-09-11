package com.droidkit.registry.patterns

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EmptyStateTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun copyMergesAndActionStaysSeparate() {
        var clicks = 0

        composeRule.setContent {
            AppTheme {
                EmptyState(
                    title = "No projects yet",
                    description = "Create your first project to get started.",
                    action = "Create project",
                    onAction = { clicks += 1 },
                )
            }
        }

        val copy = hasText("No projects yet") and hasText("Create your first project to get started.")
        composeRule.onNode(copy).assertIsDisplayed()
        composeRule.onNode(copy).assertHasNoClickAction()
        composeRule.onNode(copy).performClick()
        assertEquals(0, clicks)

        composeRule.onNodeWithText("Create project").assertHasClickAction()
        composeRule.onNodeWithText("Create project").performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun noActionOverloadOmitsButton() {
        composeRule.setContent {
            AppTheme {
                EmptyState(
                    title = "No results",
                    description = "Try a different search term.",
                )
            }
        }

        composeRule
            .onNode(hasText("No results") and hasText("Try a different search term."))
            .assertIsDisplayed()
        composeRule.onAllNodes(hasClickAction()).assertCountEquals(0)
    }
}
