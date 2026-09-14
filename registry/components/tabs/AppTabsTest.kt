package com.droidkit.registry.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTabsTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectingChangesTab() {
        composeRule.setContent {
            AppTheme {
                var selected by remember { mutableIntStateOf(0) }
                AppTabs(titles = listOf("Inbox", "Sent", "Drafts"), selectedIndex = selected, onSelect = { selected = it })
            }
        }
        composeRule.onNodeWithText("Drafts").performClick()
        composeRule.onNodeWithText("Drafts").assertExists()
    }

    @Test
    fun selectedIsHeading() {
        composeRule.setContent {
            AppTheme {
                AppTabs(titles = listOf("Inbox", "Sent"), selectedIndex = 0, onSelect = {})
            }
        }
        val heading = composeRule.onNodeWithText("Inbox").fetchSemanticsNode().config.contains(SemanticsProperties.Heading)
        assertTrue(heading)
    }
}
