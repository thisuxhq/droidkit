package com.droidkit.registry.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppButtonTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun buttonCallsClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Continue", onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("Continue").performClick()
        assertTrue(clicked)
    }

    @Test
    fun disabledDoesNotClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Continue", enabled = false, onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("Continue").assertIsNotEnabled()
        composeRule.onNodeWithText("Continue").performClick()
        assertFalse(clicked)
    }

    @Test
    fun loadingDoesNotClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Continue", loading = true, onClick = { clicked = true })
            }
        }

        composeRule.onAllNodesWithText("Continue").assertCountEquals(0)
        assertFalse(clicked)
    }
}
