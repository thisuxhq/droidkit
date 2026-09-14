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
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppPreferencePickerTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val options = listOf(
        AppPreferenceOption("system", "System"),
        AppPreferenceOption("light", "Light"),
        AppPreferenceOption("dark", "Dark"),
    )

    @Test
    fun selectingChangesId() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableStateOf("system") }
                AppPreferencePicker(title = "Appearance", options = options, selectedId = selected, onSelect = { selected = it })
            }
        }
        composeRule.onNodeWithText("Dark").performClick()
        composeRule.onNodeWithText("Appearance").assertExists()
    }

    @Test
    fun disabledDoesNotSelect() {
        var selected = "system"
        composeRule.setContent {
            AppTheme {
                AppPreferencePicker(title = "Appearance", options = options, selectedId = selected, onSelect = { selected = it }, enabled = false)
            }
        }
        composeRule.onNodeWithText("Dark").performClick()
        assertEquals("system", selected)
    }
}
