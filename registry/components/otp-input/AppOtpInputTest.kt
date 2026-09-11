package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
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

        composeRule.onNode(hasSetTextAction()).performTextInput("12ab34")
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

        composeRule.onNode(hasSetTextAction()).performTextInput("847291")
        assertEquals("8472", value)
    }

    @Test
    fun disabledRejectsInput() {
        var value = "847291"

        composeRule.setContent {
            AppTheme {
                AppOtpInput(
                    value = value,
                    onValueChange = { value = it },
                    enabled = false,
                )
            }
        }

        composeRule.onNode(hasStateDescription("Disabled")).assertIsNotEnabled()
        composeRule.onNode(hasSetTextAction()).assertDoesNotExist()
        assertEquals("847291", value)
    }

    @Test
    fun errorAnnouncesSupportingText() {
        composeRule.setContent {
            AppTheme {
                AppOtpInput(
                    value = "847291",
                    onValueChange = {},
                    isError = true,
                    supportingText = "Code did not match",
                )
            }
        }

        composeRule.onNodeWithText("Code did not match").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).assert(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.Error),
        )
    }
}
