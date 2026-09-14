package com.droidkit.registry.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppProgressTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun announcesPercent() {
        composeRule.setContent {
            AppTheme { AppProgress(label = "Uploading photo", progress = 0.45f) }
        }
        val node = composeRule.onNodeWithText("Uploading photo").fetchSemanticsNode()
        assertEquals("45 percent", node.config[SemanticsProperties.StateDescription])
    }

    @Test
    fun indeterminateReadsLoading() {
        composeRule.setContent {
            AppTheme { AppProgress(label = "Uploading photo") }
        }
        val node = composeRule.onNodeWithText("Uploading photo").fetchSemanticsNode()
        assertEquals("Loading", node.config[SemanticsProperties.StateDescription])
    }
}
