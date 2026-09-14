package com.droidkit.registry.components

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AppPasswordFieldTest {
    // A TestDispatcher in the effect context puts the reveal timer's delay() under mainClock.
    @get:Rule
    val composeRule = createComposeRule(effectContext = UnconfinedTestDispatcher())

    private val rules =
        listOf(
            PasswordRule.minLength(8),
            PasswordRule.number(),
        )

    private fun stateDescription(value: String) =
        SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, value)

    private class RecordingView(context: Context) : View(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }

    @Test
    fun typesPasswordThenRevealsValue() {
        composeRule.setContent {
            AppTheme {
                var value by remember { mutableStateOf("") }
                AppPasswordField(value = value, onValueChange = { value = it })
            }
        }

        composeRule.onNodeWithText("Password").performTextInput("secret")
        composeRule.onNodeWithContentDescription("Show password").performClick()
        composeRule.onNodeWithText("secret").assertIsDisplayed()
    }

    @Test
    fun toggleFlipsContentDescription() {
        composeRule.setContent {
            AppTheme {
                AppPasswordField(value = "secret", onValueChange = {})
            }
        }

        composeRule.onNodeWithContentDescription("Show password").performClick()
        composeRule.onNodeWithContentDescription("Hide password").assertIsDisplayed()
        composeRule.onNodeWithText("secret").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Hide password").performClick()
        composeRule.onNodeWithContentDescription("Show password").assertIsDisplayed()
    }

    @Test
    fun disabledToggleDoesNotReveal() {
        composeRule.setContent {
            AppTheme {
                AppPasswordField(value = "secret", onValueChange = {}, enabled = false)
            }
        }

        composeRule.onNodeWithContentDescription("Show password").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("Show password").performClick()
        composeRule.onNodeWithContentDescription("Show password").assertIsDisplayed()
    }

    @Test
    fun errorShowsSupportingText() {
        composeRule.setContent {
            AppTheme {
                AppPasswordField(
                    value = "short",
                    onValueChange = {},
                    supportingText = "Use at least 8 characters",
                    isError = true,
                )
            }
        }

        composeRule.onNodeWithText("Use at least 8 characters").assertIsDisplayed()
    }

    @Test
    fun revealHidesAgainAfterTenSecondsIdle() {
        composeRule.mainClock.autoAdvance = false

        composeRule.setContent {
            AppTheme {
                AppPasswordField(value = "secret", onValueChange = {})
            }
        }

        composeRule.onNodeWithContentDescription("Show password").performClick()
        tick(500)
        composeRule.onNodeWithContentDescription("Hide password").assertExists()

        tick(9_000)
        composeRule.onNodeWithContentDescription("Hide password").assertExists()

        tick(1_000)
        composeRule.onNodeWithContentDescription("Show password").assertExists()
    }

    /** Advance the clock, then flush and give writes made by effects one frame to compose. */
    private fun tick(millis: Long) {
        composeRule.runOnIdle { }
        composeRule.mainClock.advanceTimeBy(millis)
        composeRule.runOnIdle { }
        composeRule.mainClock.advanceTimeByFrame()
    }

    @Test
    fun revealHidesWhenFocusLeavesTheField() {
        composeRule.setContent {
            AppTheme {
                Column {
                    var password by remember { mutableStateOf("secret") }
                    var other by remember { mutableStateOf("") }
                    AppPasswordField(value = password, onValueChange = { password = it })
                    AppTextField(value = other, onValueChange = { other = it }, label = "Name")
                }
            }
        }

        composeRule.onNodeWithText("Password").performTextInput("")
        composeRule.onNodeWithContentDescription("Show password").performClick()
        composeRule.onNodeWithContentDescription("Hide password").assertExists()

        composeRule.onNodeWithText("Name").performTextInput("a")
        composeRule.onNodeWithContentDescription("Show password").assertExists()
    }

    @Test
    fun pasteIsTrimmedButTypingIsNot() {
        var value by mutableStateOf("")

        composeRule.setContent {
            AppTheme {
                AppPasswordField(value = value, onValueChange = { value = it })
            }
        }

        composeRule.onNodeWithText("Password").performTextInput("  hunter2  ")
        assertEquals("hunter2", value)

        composeRule.onNodeWithText("Password").performTextInput(" ")
        assertEquals("hunter2 ", value)
    }

    @Test
    fun rulesTickLiveAndReadTheirState() {
        var value by mutableStateOf("")

        composeRule.setContent {
            AppTheme {
                AppPasswordField(value = value, onValueChange = { value = it }, rules = rules)
            }
        }

        composeRule.onNode(hasText("At least 8 characters")).assert(stateDescription("Not met"))
        composeRule.onNode(hasText("A number")).assert(stateDescription("Not met"))
        composeRule.onNodeWithText("Weak password").assertDoesNotExist()

        value = "hunter"
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Weak password").assertIsDisplayed()

        value = "hunter2"
        composeRule.waitForIdle()
        composeRule.onNode(hasText("A number")).assert(stateDescription("Met"))
        composeRule.onNodeWithText("Good password").assertIsDisplayed()

        value = "hunter2000"
        composeRule.waitForIdle()
        composeRule.onNode(hasText("At least 8 characters")).assert(stateDescription("Met"))
        composeRule.onNodeWithText("Strong password").assertIsDisplayed()
    }

    @Test
    fun ruleMetTicksAndAllMetConfirms() {
        var value by mutableStateOf("hunter")
        lateinit var recorder: RecordingView

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    AppPasswordField(value = value, onValueChange = { value = it }, rules = rules)
                }
            }
        }

        composeRule.waitForIdle()
        assertEquals(emptyList<Int>(), recorder.constants)

        value = "hunter2"
        composeRule.waitForIdle()
        assertEquals(listOf(HapticFeedbackConstants.CLOCK_TICK), recorder.constants)

        value = "hunter2000"
        composeRule.waitForIdle()
        assertEquals(
            listOf(HapticFeedbackConstants.CLOCK_TICK, HapticFeedbackConstants.CONFIRM),
            recorder.constants,
        )
    }
}
