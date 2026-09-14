package com.droidkit.registry.components

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.KeyEvent as AndroidKeyEvent
import android.view.View
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AppOtpInputTest {
    // A TestDispatcher in the effect context puts the complete delay() under mainClock.
    @get:Rule
    val composeRule = createComposeRule(effectContext = UnconfinedTestDispatcher())

    private class RecordingView(context: Context) : View(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }

    /** Advance the clock, then flush and give writes made by effects one frame to compose. */
    private fun tick(millis: Long) {
        composeRule.runOnIdle { }
        composeRule.mainClock.advanceTimeBy(millis)
        composeRule.runOnIdle { }
        composeRule.mainClock.advanceTimeByFrame()
    }

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
    fun pasteStripsNonDigits() {
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

        composeRule.onNode(hasSetTextAction()).performTextInput("12 34-56")
        assertEquals("123456", value)
    }

    @Test
    fun backspaceDeletesLastDigit() {
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

        val field = composeRule.onNode(hasSetTextAction())
        field.performTextInput("1234")
        assertEquals("1234", value)
        field.performKeyPress(
            KeyEvent(AndroidKeyEvent(AndroidKeyEvent.ACTION_DOWN, AndroidKeyEvent.KEYCODE_DEL)),
        )
        field.performKeyPress(
            KeyEvent(AndroidKeyEvent(AndroidKeyEvent.ACTION_UP, AndroidKeyEvent.KEYCODE_DEL)),
        )
        assertEquals("123", value)
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

    @Test
    fun errorWithoutSupportingTextShowsDefault() {
        composeRule.setContent {
            AppTheme {
                AppOtpInput(
                    value = "847291",
                    onValueChange = {},
                    isError = true,
                )
            }
        }

        composeRule.onNodeWithText("Invalid code").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).assert(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.Error),
        )
    }

    @Test
    fun fieldIsNamedByLabel() {
        composeRule.setContent {
            AppTheme {
                AppOtpInput(value = "", onValueChange = {})
            }
        }

        composeRule.onNode(hasContentDescription("Code")).assert(hasSetTextAction())
    }

    @Test
    fun lastDigitCompletesAfterAShortPauseWithConfirm() {
        composeRule.mainClock.autoAdvance = false
        var completed: String? = null
        lateinit var recorder: RecordingView

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    var code by remember { mutableStateOf("") }
                    AppOtpInput(
                        value = code,
                        onValueChange = { code = it },
                        onComplete = { completed = it },
                        length = 4,
                    )
                }
            }
        }

        for (digit in listOf("8", "4", "7")) {
            composeRule.onNode(hasSetTextAction()).performTextInput(digit)
            tick(50)
        }
        assertEquals(3, recorder.constants.count { it == HapticFeedbackConstants.CLOCK_TICK })

        composeRule.onNode(hasSetTextAction()).performTextInput("2")
        tick(50)
        assertNull("the code is shown whole before it submits", completed)

        tick(200)
        assertEquals("8472", completed)
        assertEquals(HapticFeedbackConstants.CONFIRM, recorder.constants.last())
    }

    @Test
    fun wrongCodeStaysVisibleAndNextDigitStartsOver() {
        var isError by mutableStateOf(false)
        var value = "8472"
        lateinit var recorder: RecordingView

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    var code by remember { mutableStateOf(value) }
                    AppOtpInput(
                        value = code,
                        onValueChange = {
                            code = it
                            value = it
                        },
                        length = 4,
                        isError = isError,
                        supportingText = "Code did not match",
                    )
                }
            }
        }

        isError = true
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Code did not match").assertIsDisplayed()
        assertEquals("8472", value)
        assertEquals(listOf(HapticFeedbackConstants.REJECT), recorder.constants)
        composeRule.onNode(hasSetTextAction()).assertIsFocused()

        isError = false
        composeRule.onNode(hasSetTextAction()).performTextInput("5")
        assertEquals("5", value)
    }

    @Test
    fun imeDoneInvokesOnDone() {
        var done = false

        composeRule.setContent {
            AppTheme {
                AppOtpInput(
                    value = "847291",
                    onValueChange = {},
                    onDone = { done = true },
                )
            }
        }

        composeRule.onNode(hasSetTextAction()).performImeAction()
        assertTrue(done)
    }
}
