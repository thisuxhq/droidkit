package com.droidkit.registry.components

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.LayoutDirection
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppReelTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val moments =
        listOf(
            ReelMoment(id = "arrive", label = "Arrive", icon = Icons.Filled.Place),
            ReelMoment(id = "stay", label = "Stay", icon = Icons.Filled.Home),
            ReelMoment(id = "taste", label = "Taste", icon = Icons.Filled.Favorite),
        )

    private class RecordingView(context: Context) : View(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }

    @Test
    fun tapMarkerSelectsAndShowsPane() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableIntStateOf(1) }
                AppReel(
                    moments = moments,
                    selectedIndex = selected,
                    onSelect = { selected = it },
                ) { index ->
                    Text(text = moments[index].label)
                }
            }
        }

        composeRule.onNode(hasContentDescription("Stay")).assertIsSelected()
        composeRule.onNode(hasContentDescription("Arrive")).assertIsNotSelected()
        composeRule.onNode(hasStateDescription("Stay")).assertExists()

        composeRule.onNode(hasContentDescription("Arrive")).performClick()

        composeRule.onNode(hasContentDescription("Arrive")).assertIsSelected()
        composeRule.onNode(hasContentDescription("Stay")).assertIsNotSelected()
        composeRule.onNodeWithText("Arrive").assertExists()
        composeRule.onNode(hasStateDescription("Arrive")).assertExists()
    }

    @Test
    fun disabledDoesNotSelect() {
        var selected = 1
        var called = false

        composeRule.setContent {
            AppTheme {
                AppReel(
                    moments = moments,
                    selectedIndex = selected,
                    onSelect = {
                        called = true
                        selected = it
                    },
                    enabled = false,
                ) { index ->
                    Text(text = moments[index].label)
                }
            }
        }

        composeRule.onNode(hasContentDescription("Taste")).performClick()
        assertFalse(called)
        assertEquals(1, selected)
    }

    @Test
    fun tapFiresTickWhenIndexChanges() {
        lateinit var recorder: RecordingView

        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    var selected by remember { mutableIntStateOf(0) }
                    AppReel(
                        moments = moments,
                        selectedIndex = selected,
                        onSelect = { selected = it },
                    ) { index ->
                        Text(text = moments[index].label)
                    }
                }
            }
        }

        composeRule.onNode(hasContentDescription("Stay")).performClick()
        composeRule.waitForIdle()
        assertTrue(recorder.constants.contains(HapticFeedbackConstants.CLOCK_TICK))
    }

    @Test
    fun rtlStillSelectsByLabel() {
        composeRule.setContent {
            AppTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    var selected by remember { mutableIntStateOf(0) }
                    AppReel(
                        moments = moments,
                        selectedIndex = selected,
                        onSelect = { selected = it },
                    ) { index ->
                        Text(text = moments[index].label)
                    }
                }
            }
        }

        composeRule.onNode(hasContentDescription("Taste")).performClick()
        composeRule.onNode(hasContentDescription("Taste")).assertIsSelected()
        composeRule.onNode(hasContentDescription("Arrive")).assertIsNotSelected()
        composeRule.onNode(hasStateDescription("Taste")).assertExists()
    }
}
