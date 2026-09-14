package com.droidkit.registry.foundation

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private val ShakeFrameSize = 80.dp
private val ShakeBoxSize = 40.dp

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AppFoundationTest {
    // A TestDispatcher in the effect context puts delay() under mainClock.
    @get:Rule
    val composeRule = createComposeRule(effectContext = UnconfinedTestDispatcher())

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

        set { loading = true }
        tick(100)
        assertFalse("spinner must not show within the first 150 ms", visible)

        tick(150)
        assertTrue("spinner shows after 150 ms", visible)
    }

    /** A state write from the test thread is only seen by the next frame after an idle flush. */
    private fun set(write: () -> Unit) {
        write()
        composeRule.runOnIdle { }
    }

    /** Advance the clock, then flush and give writes made by effects one frame to compose. */
    private fun tick(millis: Long) {
        composeRule.mainClock.advanceTimeBy(millis)
        composeRule.runOnIdle { }
        composeRule.mainClock.advanceTimeByFrame()
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

        set { loading = true }
        tick(48)
        set { loading = false }
        tick(1000)

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

        set { loading = true }
        tick(250)
        assertTrue("spinner is showing", visible)

        set { loading = false }
        tick(200)
        assertTrue("spinner stays for at least 500 ms once shown", visible)

        tick(600)
        assertFalse("spinner hides once the minimum has passed", visible)
    }

    @Test
    fun shakeSettlesBackToRest() {
        composeRule.mainClock.autoAdvance = false
        var trigger by mutableStateOf(false)

        composeRule.setContent {
            AppTheme {
                Box(modifier = Modifier.size(ShakeFrameSize), contentAlignment = Alignment.Center) {
                    Box(
                        modifier =
                            Modifier
                                .shake(trigger)
                                .testTag("shaken")
                                .size(ShakeBoxSize),
                    )
                }
            }
        }

        val rest = composeRule.onNodeWithTag("shaken").getBoundsInRoot().left

        set { trigger = true }
        var displaced = false
        repeat(16) {
            composeRule.mainClock.advanceTimeByFrame()
            if (composeRule.onNodeWithTag("shaken").getBoundsInRoot().left != rest) displaced = true
        }
        assertTrue("content is displaced during the shake", displaced)

        tick(1000)
        val settled = composeRule.onNodeWithTag("shaken").getBoundsInRoot().left
        assertEquals("content settles back where it started", rest, settled)
    }
}
