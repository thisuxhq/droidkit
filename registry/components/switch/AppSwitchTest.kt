package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
class AppSwitchTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rowToggles() {
        composeRule.setContent {
            AppTheme {
                var on by remember { mutableStateOf(false) }
                AppSwitch(text = "Notifications", checked = on, onCheckedChange = { on = it })
            }
        }
        composeRule.onNodeWithText("Notifications").performClick()
        composeRule.onNodeWithText("Notifications").performClick()
    }

    @Test
    fun disabledDoesNotToggle() {
        var changed = false
        composeRule.setContent {
            AppTheme {
                AppSwitch(text = "Notifications", checked = true, onCheckedChange = { changed = true }, enabled = false)
            }
        }
        composeRule.onNodeWithText("Notifications").performClick()
        assertFalse(changed)
    }
}
