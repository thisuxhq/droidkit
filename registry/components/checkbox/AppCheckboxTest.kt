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
class AppCheckboxTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rowChecks() {
        var checked = false
        composeRule.setContent {
            AppTheme {
                var on by remember { mutableStateOf(false) }
                AppCheckbox(text = "Remember this device", checked = on, onCheckedChange = {
                    on = it
                    checked = it
                })
            }
        }
        composeRule.onNodeWithText("Remember this device").performClick()
        assertTrue(checked)
    }

    @Test
    fun disabledDoesNotCheck() {
        var changed = false
        composeRule.setContent {
            AppTheme {
                AppCheckbox(text = "Remember this device", checked = false, onCheckedChange = { changed = true }, enabled = false)
            }
        }
        composeRule.onNodeWithText("Remember this device").performClick()
        assertFalse(changed)
    }
}
