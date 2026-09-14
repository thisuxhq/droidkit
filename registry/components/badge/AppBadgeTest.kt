package com.droidkit.registry.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppBadgeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun capsAt99() {
        composeRule.setContent {
            AppTheme {
                AppBadge(count = 140) {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
                }
            }
        }
        composeRule.onNodeWithText("99+").assertIsDisplayed()
    }

    @Test
    fun hidesZero() {
        composeRule.setContent {
            AppTheme {
                AppBadge(count = 0) {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
                }
            }
        }
        composeRule.onNodeWithText("0").assertDoesNotExist()
    }
}
