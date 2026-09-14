package com.droidkit.registry.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppBottomSheetTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun chromeShowsTitleAndContent() {
        composeRule.setContent {
            AppTheme {
                AppBottomSheetChrome(title = "Appearance") {
                    Text("System")
                }
            }
        }
        composeRule.onNodeWithText("Appearance").assertIsDisplayed()
        composeRule.onNodeWithText("System").assertIsDisplayed()
    }
}
