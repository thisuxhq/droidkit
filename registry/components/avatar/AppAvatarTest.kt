package com.droidkit.registry.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppAvatarTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun initialsFromTwoWords() {
        assertEquals("AL", initialsFor("Ada Lovelace"))
        composeRule.setContent {
            AppTheme { AppAvatar(name = "Ada Lovelace") }
        }
        composeRule.onNodeWithContentDescription("Ada Lovelace").assertIsDisplayed()
        composeRule.onNodeWithText("AL").assertIsDisplayed()
    }

    @Test
    fun initialsFromOneWord() {
        assertEquals("A", initialsFor("Ada"))
    }
}
