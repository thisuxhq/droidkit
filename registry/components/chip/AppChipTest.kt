package com.droidkit.registry.components

import androidx.compose.ui.test.junit4.createComposeRule
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
class AppChipTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clickFires() {
        var clicked = false
        composeRule.setContent {
            AppTheme {
                AppChip(text = "On sale", selected = false, onClick = { clicked = true })
            }
        }
        composeRule.onNodeWithText("On sale").performClick()
        assertTrue(clicked)
    }

    @Test
    fun disabledDoesNotClick() {
        var clicked = false
        composeRule.setContent {
            AppTheme {
                AppChip(text = "On sale", selected = false, onClick = { clicked = true }, enabled = false)
            }
        }
        composeRule.onNodeWithText("On sale").performClick()
        assertFalse(clicked)
    }
}
