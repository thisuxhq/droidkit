package com.droidkit.registry.foundation

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppFoundationTest {
    @get:Rule
    val composeRule = createComposeRule()

    private class RecordingView(context: Context) : View(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }

    @Test
    fun hapticsMapToPlatformConstants() {
        lateinit var recorder: RecordingView
        lateinit var haptics: AppHaptics

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                haptics = rememberAppHaptics()
            }
        }

        haptics.click()
        haptics.tick()
        haptics.confirm()
        haptics.reject()

        assertEquals(
            listOf(
                HapticFeedbackConstants.CONTEXT_CLICK,
                HapticFeedbackConstants.CLOCK_TICK,
                HapticFeedbackConstants.CONFIRM,
                HapticFeedbackConstants.REJECT,
            ),
            recorder.constants,
        )
    }

    @Test
    fun loadingVisibilityWaitsBeforeShowing() {
        composeRule.mainClock.autoAdvance = false
        var loading by mutableStateOf(false)
        var visible = false

        composeRule.setContent {
            AppTheme {
                visible = rememberLoadingVisibility(loading)
            }
        }

        loading = true
        composeRule.mainClock.advanceTimeBy(100)
        assertFalse("spinner must not show within the first 150 ms", visible)

        composeRule.mainClock.advanceTimeBy(100)
        assertTrue("spinner shows after 150 ms", visible)
    }

    @Test
    fun loadingVisibilityNeverFlashesForAFastCall() {
        composeRule.mainClock.autoAdvance = false
        var loading by mutableStateOf(false)
        var visible = false

        composeRule.setContent {
            AppTheme {
                visible = rememberLoadingVisibility(loading)
            }
        }

        loading = true
        composeRule.mainClock.advanceTimeBy(80)
        loading = false
        composeRule.mainClock.advanceTimeBy(1000)

        assertFalse("an 80 ms call never shows a spinner", visible)
    }

    @Test
    fun loadingVisibilityHoldsForMinimumTime() {
        composeRule.mainClock.autoAdvance = false
        var loading by mutableStateOf(false)
        var visible = false

        composeRule.setContent {
            AppTheme {
                visible = rememberLoadingVisibility(loading)
            }
        }

        loading = true
        composeRule.mainClock.advanceTimeBy(200)
        assertTrue(visible)

        loading = false
        composeRule.mainClock.advanceTimeBy(200)
        assertTrue("spinner stays for at least 500 ms once shown", visible)

        composeRule.mainClock.advanceTimeBy(400)
        assertFalse("spinner hides once the minimum has passed", visible)
    }

    @Test
    fun shakeSettlesBackToRest() {
        composeRule.mainClock.autoAdvance = false
        var trigger by mutableStateOf(false)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier =
                        Modifier
                            .testTag("shaken")
                            .shake(trigger),
                )
            }
        }

        val rest = composeRule.onNodeWithTag("shaken").getBoundsInRoot().left

        trigger = true
        composeRule.mainClock.advanceTimeBy(50)
        val moving = composeRule.onNodeWithTag("shaken").getBoundsInRoot().left
        assertTrue("content is displaced mid-shake", moving != rest)

        composeRule.mainClock.advanceTimeBy(1000)
        val settled = composeRule.onNodeWithTag("shaken").getBoundsInRoot().left
        assertEquals("content settles back where it started", rest, settled)
    }
}
