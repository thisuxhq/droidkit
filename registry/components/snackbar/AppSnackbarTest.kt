package com.droidkit.registry.components

import androidx.compose.ui.test.assertHasClickAction
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
class AppSnackbarTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun actionFires() {
        var undone = false
        composeRule.setContent {
            AppTheme {
                AppSnackbar(message = "Draft saved", actionLabel = "Undo", onAction = { undone = true })
            }
        }
        composeRule.onNodeWithText("Undo").assertHasClickAction()
        composeRule.onNodeWithText("Undo").performClick()
        assertTrue(undone)
    }

    @Test
    fun messageIsShown() {
        composeRule.setContent {
            AppTheme { AppSnackbar(message = "Draft saved") }
        }
        composeRule.onNodeWithText("Draft saved").assertExists()
    }
}
