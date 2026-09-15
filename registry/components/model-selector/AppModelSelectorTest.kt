package com.droidkit.registry.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.and
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppModelSelectorTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val openAi = AppModelProvider(id = "openai", name = "OpenAI")
    private val anthropic = AppModelProvider(id = "anthropic", name = "Anthropic")

    private val shortCatalog =
        listOf(
            AppModelOption("gpt-4o", "GPT-4o", openAi, "Answers fast"),
            AppModelOption("o3", "o3", openAi, "Thinks first"),
            AppModelOption("sonnet", "Sonnet 4", anthropic, "Balanced"),
        )

    private val longCatalog =
        shortCatalog +
            listOf(
                AppModelOption("gpt-4o-mini", "GPT-4o mini", openAi, "Cheaper and faster"),
                AppModelOption("haiku", "Haiku", anthropic, "Quick drafts"),
                AppModelOption("opus", "Opus 4", anthropic, "Deep reasoning"),
                AppModelOption("gpt-5", "GPT-5", openAi, "Best reasoning"),
                AppModelOption("sonnet-3", "Sonnet 3.5", anthropic, "Older balanced"),
            )

    @Test
    fun triggerNamesSelectedModel() {
        composeRule.setContent {
            AppTheme {
                AppModelSelector(models = shortCatalog, selectedId = "sonnet", onSelect = {})
            }
        }
        composeRule.onNodeWithText("Sonnet 4").assertIsDisplayed()
    }

    @Test
    fun triggerExposesCollapsedAndExpandedState() {
        composeRule.setContent {
            InspectionTheme {
                AppModelSelector(models = shortCatalog, selectedId = "gpt-4o", onSelect = {})
            }
        }
        composeRule.onNode(hasText("GPT-4o")).assert(stateDescription("Collapsed"))
        composeRule.onNodeWithText("GPT-4o").performClick()
        composeRule.onNodeWithText("Choose a model").assertIsDisplayed()
        composeRule.onNode(hasText("GPT-4o") and stateDescription("Expanded")).assertIsDisplayed()
    }

    @Test
    fun disabledTriggerDoesNotOpenSheet() {
        composeRule.setContent {
            AppTheme {
                AppModelSelector(
                    models = shortCatalog,
                    selectedId = "gpt-4o",
                    onSelect = {},
                    enabled = false,
                )
            }
        }
        composeRule.onNodeWithText("GPT-4o").assertIsNotEnabled()
        composeRule.onNodeWithText("GPT-4o").performClick()
        composeRule.onNodeWithText("Choose a model").assertDoesNotExist()
    }

    @Test
    fun pickingADifferentRowEmitsOnceAndDismisses() {
        var selected by mutableStateOf("gpt-4o")
        var emissions = 0
        composeRule.setContent {
            InspectionTheme {
                AppModelSelector(
                    models = shortCatalog,
                    selectedId = selected,
                    onSelect = {
                        selected = it
                        emissions++
                    },
                )
            }
        }
        composeRule.onNodeWithText("GPT-4o").performClick()
        composeRule.onNodeWithText("Choose a model").assertIsDisplayed()
        composeRule.onNodeWithText("Sonnet 4").performClick()
        composeRule.waitForIdle()
        assertEquals("sonnet", selected)
        assertEquals(1, emissions)
        composeRule.onNodeWithText("Choose a model").assertDoesNotExist()
        composeRule.onNodeWithText("Sonnet 4").assertIsDisplayed()
    }

    @Test
    fun pickingTheCurrentRowClosesWithoutEmitting() {
        var selected by mutableStateOf("gpt-4o")
        var emissions = 0
        composeRule.setContent {
            InspectionTheme {
                AppModelSelector(
                    models = shortCatalog,
                    selectedId = selected,
                    onSelect = {
                        selected = it
                        emissions++
                    },
                )
            }
        }
        composeRule.onNodeWithText("GPT-4o").performClick()
        composeRule.onNodeWithText("Choose a model").assertIsDisplayed()
        composeRule.onNodeWithText("Answers fast").performClick()
        composeRule.waitForIdle()
        assertEquals("gpt-4o", selected)
        assertEquals(0, emissions)
        composeRule.onNodeWithText("Choose a model").assertDoesNotExist()
    }

    @Test
    fun disablingWhileOpenDismissesTheSheet() {
        val enabled = mutableStateOf(true)
        composeRule.setContent {
            InspectionTheme {
                AppModelSelector(
                    models = shortCatalog,
                    selectedId = "gpt-4o",
                    onSelect = {},
                    enabled = enabled.value,
                )
            }
        }
        composeRule.onNodeWithText("GPT-4o").performClick()
        composeRule.onNodeWithText("Choose a model").assertIsDisplayed()
        enabled.value = false
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Choose a model").assertDoesNotExist()
    }

    @Test
    fun typingFiltersNameAndProviderLive() {
        composeRule.setContent {
            AppTheme {
                var query by remember { mutableStateOf("") }
                AppModelSelectorSheetPreview(
                    models = longCatalog,
                    selectedId = "gpt-4o",
                    query = query,
                    onQueryChange = { query = it },
                )
            }
        }
        composeRule.onNodeWithText("Search models").performTextInput("anthropic")
        composeRule.onNodeWithText("Sonnet 4").assertIsDisplayed()
        composeRule.onNodeWithText("Haiku").assertIsDisplayed()
        composeRule.onNodeWithText("GPT-4o mini").assertDoesNotExist()
    }

    @Test
    fun typingAfterNoResultsRestoresMatches() {
        composeRule.setContent {
            AppTheme {
                var query by remember { mutableStateOf("") }
                AppModelSelectorSheetPreview(
                    models = longCatalog,
                    selectedId = "gpt-4o",
                    query = query,
                    onQueryChange = { query = it },
                )
            }
        }
        composeRule.onNodeWithText("Search models").performTextInput("llama")
        composeRule.onNodeWithText("No models match").assertIsDisplayed()
        composeRule.onNodeWithText("llama").performTextReplacement("sonnet")
        composeRule.onNodeWithText("No models match").assertDoesNotExist()
        composeRule.onNodeWithText("Sonnet 4").assertIsDisplayed()
        composeRule.onNodeWithText("Search models").assertDoesNotExist()
        composeRule.onNodeWithText("sonnet").assertIsDisplayed()
    }

    @Test
    fun clearAfterNoResultsRestoresTheCatalog() {
        composeRule.setContent {
            AppTheme {
                var query by remember { mutableStateOf("") }
                AppModelSelectorSheetPreview(
                    models = longCatalog,
                    selectedId = "gpt-4o",
                    query = query,
                    onQueryChange = { query = it },
                )
            }
        }
        composeRule.onNodeWithText("Search models").performTextInput("llama")
        composeRule.onNodeWithText("No models match").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Clear").performClick()
        composeRule.onNodeWithText("No models match").assertDoesNotExist()
        composeRule.onNodeWithText("GPT-4o mini").assertIsDisplayed()
        composeRule.onNodeWithText("Haiku").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Clear").assertDoesNotExist()
    }

    @Test
    fun foldedQueryMatchesHyphenatedName() {
        assertTrue(longCatalog.first { it.id == "gpt-4o" }.matchesQuery("gpt4"))
        assertTrue(longCatalog.first { it.id == "gpt-4o-mini" }.matchesQuery("gpt4 mini"))
        assertFalse(longCatalog.first { it.id == "sonnet" }.matchesQuery("gpt4"))
    }

    @Test
    fun everyTokenMustMatchProviderOrName() {
        val mini = longCatalog.first { it.id == "gpt-4o-mini" }
        assertTrue(mini.matchesQuery("open mini"))
        assertFalse(mini.matchesQuery("open claude"))
    }

    @Test
    fun capabilityIsNotASearchTarget() {
        val o3 = longCatalog.first { it.id == "o3" }
        assertFalse(o3.matchesQuery("thinks"))
    }

    @Test
    fun headersAppearOnlyForMultipleProviders() {
        val headed = groupedModels(shortCatalog, "")
        assertEquals(2, headed.size)
        assertTrue(headed[0].showHeader)
        assertEquals("OpenAI", headed[0].provider.name)
        assertEquals("Anthropic", headed[1].provider.name)

        val flat = groupedModels(shortCatalog.filter { it.provider.id == "openai" }, "")
        assertEquals(1, flat.size)
        assertFalse(flat[0].showHeader)
    }

    @Test
    fun filterDropsEmptyGroupsAndKeepsHeaderPolicy() {
        val groups = groupedModels(shortCatalog, "sonnet")
        assertEquals(1, groups.size)
        assertEquals("Anthropic", groups[0].provider.name)
        assertEquals(listOf("sonnet"), groups[0].models.map { it.id })
    }

    @Test
    fun noMatchesIsEmptyGroups() {
        assertTrue(groupedModels(shortCatalog, "llama").isEmpty())
    }

    @Test
    fun foldStripsAccentsSpacesAndPunctuation() {
        assertEquals("gpt4o", foldForSearch("GPT-4o"))
        assertEquals("openai", foldForSearch("OpenAI"))
        assertEquals("claude", foldForSearch("Claudé"))
    }

    private fun stateDescription(value: String): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, value)
}

@Composable
private fun InspectionTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        AppTheme(content = content)
    }
}
