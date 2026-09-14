package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppBeadTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val options = listOf("Never", "Periodic", "Always")

    @Test
    fun selectsOption() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableIntStateOf(1) }
                AppBead(
                    options = options,
                    selectedIndex = selected,
                    onSelect = { selected = it },
                )
            }
        }

        composeRule.onNodeWithText("Periodic").assertIsSelected()
        composeRule.onNodeWithText("Never").assertIsNotSelected()
        composeRule.onNodeWithText("Never").performClick()
        composeRule.onNodeWithText("Never").assertIsSelected()
        composeRule.onNodeWithText("Periodic").assertIsNotSelected()
    }

    @Test
    fun disabledDoesNotSelect() {
        var selected = 1
        var called = false

        composeRule.setContent {
            AppTheme {
                AppBead(
                    options = options,
                    selectedIndex = selected,
                    onSelect = {
                        called = true
                        selected = it
                    },
                    enabled = false,
                )
            }
        }

        composeRule.onNodeWithText("Always").performClick()
        assertFalse(called)
        assertEquals(1, selected)
    }
}
