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
class AppTextFieldTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun typesIntoField() {
        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("") }
                AppTextField(value = value, onValueChange = { value = it }, label = "Email")
            }
        }

        composeRule.onNodeWithText("Email").performTextInput("hi@droidkit.dev")
        composeRule.onNodeWithText("hi@droidkit.dev").assertIsDisplayed()
    }

    @Test
    fun showsErrorSupportingText() {
        composeRule.setContent {
            AppTheme {
                AppTextField(
                    value = "bad",
                    onValueChange = {},
                    label = "Email",
                    supportingText = "Enter a valid email",
                    isError = true,
                )
            }
        }

        composeRule.onNodeWithText("Enter a valid email").assertIsDisplayed()
    }
}
