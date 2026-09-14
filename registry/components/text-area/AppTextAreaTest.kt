package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTextAreaTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun typesMultiline() {
        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("") }
                AppTextArea(value = value, onValueChange = { value = it }, label = "Notes")
            }
        }
        composeRule.onNodeWithText("Notes").performTextInput("line one")
        composeRule.onNodeWithText("line one").assertIsDisplayed()
    }

    @Test
    fun showsErrorText() {
        composeRule.setContent {
            AppTheme {
                AppTextArea(
                    value = "",
                    onValueChange = {},
                    label = "Notes",
                    supportingText = "Add a note before you send",
                    isError = true,
                )
            }
        }
        composeRule.onNodeWithText("Add a note before you send").assertIsDisplayed()
    }
}
