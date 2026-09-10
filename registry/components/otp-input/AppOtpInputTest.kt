package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppOtpInputTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun acceptsDigitsOnly() {
        var value = ""

        composeRule.setContent {
            AppTheme {
                var code by remember { mutableStateOf("") }
                AppOtpInput(
                    value = code,
                    onValueChange = {
                        code = it
                        value = it
                    },
                )
            }
        }

        composeRule.onNodeWithContentDescription("One-time code").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("One-time code").performTextInput("12ab34")
        assertEquals("1234", value)
    }

    @Test
    fun capsAtLength() {
        var value = ""

        composeRule.setContent {
            AppTheme {
                var code by remember { mutableStateOf("") }
                AppOtpInput(
                    value = code,
                    onValueChange = {
                        code = it
                        value = it
                    },
                    length = 4,
                )
            }
        }

        composeRule.onNodeWithContentDescription("One-time code").performTextInput("847291")
        assertEquals("8472", value)
    }
}
