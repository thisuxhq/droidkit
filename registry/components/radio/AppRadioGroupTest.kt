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
class AppRadioGroupTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val options = listOf(
        AppRadioOption("system", "System"),
        AppRadioOption("light", "Light"),
        AppRadioOption("dark", "Dark"),
    )

    @Test
    fun selectingChangesId() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableStateOf("system") }
                AppRadioGroup(options = options, selectedId = selected, onSelect = { selected = it })
            }
        }
        composeRule.onNodeWithText("Dark").performClick()
        composeRule.onNodeWithText("Dark").assertExists()
    }

    @Test
    fun disabledDoesNotSelect() {
        var selected = "system"
        composeRule.setContent {
            AppTheme {
                AppRadioGroup(options = options, selectedId = selected, onSelect = { selected = it }, enabled = false)
            }
        }
        composeRule.onNodeWithText("Dark").performClick()
        assertEquals("system", selected)
    }
}
