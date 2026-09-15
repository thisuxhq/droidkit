package com.droidkit.registry.components

import androidx.compose.material3.Text
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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

    @Test
    fun chromeShowsFooter() {
        composeRule.setContent {
            AppTheme {
                AppBottomSheetChrome(
                    title = "Appearance",
                    footer = { Text("Save") },
                ) {
                    Text("System")
                }
            }
        }
        composeRule.onNodeWithText("Save").assertIsDisplayed()
    }

    @Test
    fun handleAnnouncesExpanded() {
        composeRule.setContent {
            AppTheme {
                AppBottomSheetChrome(
                    title = "Appearance",
                    detent = AppSheetDetent.Expanded,
                ) {
                    Text("System")
                }
            }
        }
        composeRule
            .onNode(
                SemanticsMatcher.expectValue(SemanticsProperties.ContentDescription, listOf("Drag handle")) and
                    SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Expanded"),
            )
            .assertExists()
    }

    @Test
    fun handleAnnouncesPartial() {
        composeRule.setContent {
            AppTheme {
                AppBottomSheetChrome(
                    title = "Appearance",
                    detent = AppSheetDetent.Partial,
                ) {
                    Text("System")
                }
            }
        }
        composeRule
            .onNode(
                SemanticsMatcher.expectValue(SemanticsProperties.ContentDescription, listOf("Drag handle")) and
                    SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Half expanded"),
            )
            .assertExists()
    }

    @Test
    fun partialBodyHidesExpandedContent() {
        composeRule.setContent {
            AppTheme {
                AppBottomSheetChrome(
                    title = "Appearance",
                    detent = AppSheetDetent.Partial,
                ) {
                    val detent = LocalAppSheetDetent.current
                    if (detent == AppSheetDetent.Partial) {
                        Text("System")
                    } else {
                        Text("Light")
                    }
                }
            }
        }
        composeRule.onNodeWithText("System").assertIsDisplayed()
        composeRule.onNodeWithText("Light").assertDoesNotExist()
    }

    @Test
    fun expandedBodyShowsFullContent() {
        composeRule.setContent {
            AppTheme {
                AppBottomSheetChrome(
                    title = "Appearance",
                    detent = AppSheetDetent.Expanded,
                ) {
                    val detent = LocalAppSheetDetent.current
                    if (detent == AppSheetDetent.Partial) {
                        Text("System")
                    } else {
                        Text("Light")
                    }
                }
            }
        }
        composeRule.onNodeWithText("Light").assertIsDisplayed()
        composeRule.onNodeWithText("System").assertDoesNotExist()
    }

    @Test
    fun chromeHasNoCloseControl() {
        composeRule.setContent {
            AppTheme {
                AppBottomSheetChrome(title = "Appearance") {
                    Text("System")
                }
            }
        }
        composeRule.onNodeWithContentDescription("Close").assertDoesNotExist()
        composeRule.onNodeWithText("Close").assertDoesNotExist()
    }
}
