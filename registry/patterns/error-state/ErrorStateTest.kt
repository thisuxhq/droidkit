package com.droidkit.registry.patterns

import androidx.compose.ui.test.assertHasClickAction
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
class ErrorStateTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun retryFires() {
        var clicks = 0
        composeRule.setContent {
            AppTheme {
                ErrorState(
                    title = "Could not load projects",
                    description = "Check the connection and try again.",
                    action = "Try again",
                    onAction = { clicks += 1 },
                )
            }
        }
        composeRule.onNodeWithText("Try again").assertHasClickAction()
        composeRule.onNodeWithText("Try again").performClick()
        assertEquals(1, clicks)
    }
}
