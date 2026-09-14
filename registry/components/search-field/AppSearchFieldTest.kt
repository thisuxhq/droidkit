package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppSearchFieldTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun typesAndClears() {
        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("") }
                AppSearchField(value = value, onValueChange = { value = it }, onSearch = {})
            }
        }
        composeRule.onNodeWithText("Search").performTextInput("maps")
        composeRule.onNodeWithText("maps").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Clear").performClick()
        composeRule.onNodeWithText("Search").assertIsDisplayed()
    }

    @Test
    fun searchImeFires() {
        var searched = ""
        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("maps") }
                AppSearchField(value = value, onValueChange = { value = it }, onSearch = { searched = it })
            }
        }
        composeRule.onNodeWithText("maps").performImeAction()
        assertEquals("maps", searched)
    }
}
