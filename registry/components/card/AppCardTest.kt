package com.droidkit.registry.components

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHasNoClickAction
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
class AppCardTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clickableFires() {
        var clicks = 0
        composeRule.setContent {
            AppTheme {
                AppCard(title = "Weekly report", supporting = "Ready to review.", onClick = { clicks += 1 })
            }
        }
        composeRule.onNodeWithText("Weekly report").performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun actionStaysSeparate() {
        var clicks = 0
        composeRule.setContent {
            AppTheme {
                AppCard(
                    title = "Weekly report",
                    supporting = "Ready to review.",
                    action = "Review",
                    onAction = { clicks += 1 },
                )
            }
        }
        composeRule.onNodeWithText("Review").assertHasClickAction()
        composeRule.onNodeWithText("Review").performClick()
        assertEquals(1, clicks)
    }
}
