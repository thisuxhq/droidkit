package com.droidkit.registry.components

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTextFieldTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val hasError = SemanticsMatcher.keyIsDefined(SemanticsProperties.Error)
    private val hasNoError = SemanticsMatcher.keyNotDefined(SemanticsProperties.Error)

    private class RecordingView(context: Context) : View(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }

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
    fun showsErrorSupportingTextAndSemantics() {
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
        composeRule.onNode(hasSetTextAction()).assert(hasError)
    }

    @Test
    fun clearButtonAppearsWithTextAndClearsKeepingFocus() {
        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("") }
                AppTextField(value = value, onValueChange = { value = it }, label = "Email")
            }
        }

        composeRule.onNodeWithContentDescription("Clear email").assertDoesNotExist()

        composeRule.onNodeWithText("Email").performTextInput("hello")
        composeRule.onNodeWithContentDescription("Clear email").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Clear email").performClick()
        composeRule.onNodeWithText("hello").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Clear email").assertDoesNotExist()
        composeRule.onNode(hasSetTextAction()).assertIsFocused()
    }

    @Test
    fun errorGoesQuietOnFirstKeystrokeWhileEditing() {
        var isError by mutableStateOf(false)

        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("bad") }
                AppTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = "Email",
                    supportingText = "Enter a valid email",
                    isError = isError,
                )
            }
        }

        // Focus first, then the caller flags the value on submit.
        composeRule.onNodeWithText("Email").performTextInput("")
        isError = true
        composeRule.waitForIdle()
        composeRule.onNode(hasSetTextAction()).assert(hasError)

        composeRule.onNodeWithText("Email").performTextInput("x")
        composeRule.onNode(hasSetTextAction()).assert(hasNoError)
        composeRule.onNodeWithText("Enter a valid email").assertIsDisplayed()
    }

    @Test
    fun errorFiresRejectHapticOnce() {
        var isError by mutableStateOf(false)
        lateinit var recorder: RecordingView

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    AppTextField(
                        value = "bad",
                        onValueChange = {},
                        label = "Email",
                        supportingText = "Enter a valid email",
                        isError = isError,
                    )
                }
            }
        }

        composeRule.waitForIdle()
        assertEquals(emptyList<Int>(), recorder.constants)

        isError = true
        composeRule.waitForIdle()
        composeRule.mainClock.advanceTimeBy(1000)
        assertEquals(listOf(HapticFeedbackConstants.REJECT), recorder.constants)
    }

    @Test
    fun counterTurnsToErrorPastTheLimitWithoutBlocking() {
        composeRule.setContent {
            AppTheme {
                AppTextField(
                    value = "abcdefg",
                    onValueChange = {},
                    label = "Handle",
                    maxLength = 5,
                )
            }
        }

        composeRule.onNodeWithText("7 / 5").assertIsDisplayed()
        composeRule.onNodeWithText("abcdefg").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).assert(hasError)
    }
}
