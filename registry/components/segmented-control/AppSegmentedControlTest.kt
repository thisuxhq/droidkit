package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
class AppSegmentedControlTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectingChangesIndex() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableIntStateOf(0) }
                AppSegmentedControl(
                    options = listOf("Day", "Week", "Month"),
                    selectedIndex = selected,
                    onSelect = { selected = it },
                )
            }
        }
        composeRule.onNodeWithText("Month").performClick()
        composeRule.onNodeWithText("Month").assertExists()
    }

    @Test
    fun disabledDoesNotSelect() {
        var selected = 0
        composeRule.setContent {
            AppTheme {
                AppSegmentedControl(
                    options = listOf("Day", "Week", "Month"),
                    selectedIndex = selected,
                    onSelect = { selected = it },
                    enabled = false,
                )
            }
        }
        composeRule.onNodeWithText("Month").performClick()
        assertEquals(0, selected)
    }
}
