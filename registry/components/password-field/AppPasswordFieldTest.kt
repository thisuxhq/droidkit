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
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppPasswordFieldTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun typesPassword() {
        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("") }
                AppPasswordField(value = value, onValueChange = { value = it })
            }
        }

        composeRule.onNodeWithText("Password").performTextInput("secret")
        composeRule.onNodeWithContentDescription("Show password").assertIsDisplayed()
    }

    @Test
    fun togglesVisibility() {
        composeRule.setContent {
            AppTheme {
                AppPasswordField(value = "secret", onValueChange = {})
            }
        }

        composeRule.onNodeWithContentDescription("Show password").performClick()
        composeRule.onNodeWithContentDescription("Hide password").assertIsDisplayed()
        composeRule.onNodeWithText("secret").assertIsDisplayed()
    }
}
