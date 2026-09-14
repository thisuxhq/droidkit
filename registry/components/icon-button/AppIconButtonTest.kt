package com.droidkit.registry.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppIconButtonTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clickFires() {
        var clicked = false
        composeRule.setContent {
            AppTheme {
                AppIconButton(contentDescription = "More", onClick = { clicked = true }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }
            }
        }
        composeRule.onNodeWithContentDescription("More").performClick()
        assertTrue(clicked)
    }

    @Test
    fun loadingDoesNotClick() {
        var clicked = false
        composeRule.setContent {
            AppTheme {
                AppIconButton(contentDescription = "More", onClick = { clicked = true }, loading = true) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }
            }
        }
        val node = composeRule.onNodeWithContentDescription("More")
        node.assertIsEnabled()
        node.performClick()
        assertFalse(clicked)
    }

    @Test
    fun disabledDoesNotClick() {
        var clicked = false
        composeRule.setContent {
            AppTheme {
                AppIconButton(contentDescription = "More", onClick = { clicked = true }, enabled = false) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }
            }
        }
        composeRule.onNodeWithContentDescription("More").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("More").performClick()
        assertFalse(clicked)
    }
}
