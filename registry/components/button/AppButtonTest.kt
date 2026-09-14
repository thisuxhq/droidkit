package com.droidkit.registry.components

import android.content.Context
import android.view.HapticFeedbackConstants
import android.widget.FrameLayout
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowSystemClock
import java.time.Duration

@RunWith(AndroidJUnit4::class)
class AppButtonTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun loadingButton(): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button) and
            SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Loading")

    @Test
    fun buttonCallsClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Continue", onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("Continue").performClick()
        assertTrue(clicked)
    }

    @Test
    fun disabledDoesNotClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Continue", enabled = false, onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("Continue").assertIsNotEnabled()
        composeRule.onNodeWithText("Continue").performClick()
        assertFalse(clicked)
    }

    @Test
    fun loadingDoesNotClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Continue", loading = true, onClick = { clicked = true })
            }
        }

        val node = composeRule.onNode(loadingButton())
        node.assertExists()
        node.assertIsEnabled()
        node.performClick()
        assertFalse(clicked)
        node.assertExists()
        composeRule.onNodeWithText("Continue").assertExists()
    }

    @Test
    fun secondTapWithinGuardIsIgnored() {
        var clicks = 0

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Pay", onClick = { clicks++ })
            }
        }

        composeRule.onNodeWithText("Pay").performClick()
        composeRule.onNodeWithText("Pay").performClick()
        assertEquals(1, clicks)

        ShadowSystemClock.advanceBy(Duration.ofMillis(500))
        composeRule.onNodeWithText("Pay").performClick()
        assertEquals(2, clicks)
    }

    @Test
    fun clickFiresClickHapticOnlyWhenTheClickFires() {
        lateinit var recorder: RecordingView

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    AppButton(text = "Pay", onClick = {})
                }
            }
        }

        composeRule.onNodeWithText("Pay").performClick()
        composeRule.onNodeWithText("Pay").performClick()
        assertEquals(listOf(HapticFeedbackConstants.CONTEXT_CLICK), recorder.constants)
    }

    @Test
    fun successReadsDoneConfirmsAndDoesNotClick() {
        var clicked = false
        var success by mutableStateOf(false)
        lateinit var recorder: RecordingView

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    AppButton(text = "Pay", success = success, onClick = { clicked = true })
                }
            }
        }

        success = true
        composeRule.waitForIdle()

        val node = composeRule.onNode(stateDescription("Done"))
        node.assertExists()
        node.performClick()
        assertFalse(clicked)
        assertEquals(listOf(HapticFeedbackConstants.CONFIRM), recorder.constants)
    }

    @Test
    fun destructiveButtonFiresClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppDestructiveButton(text = "Delete account", onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("Delete account").performClick()
        assertTrue(clicked)
    }

    private fun stateDescription(value: String): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, value)

    /** A ViewGroup so the Material ripple can host its RippleContainer in it. */
    private class RecordingView(context: Context) : FrameLayout(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }
}
