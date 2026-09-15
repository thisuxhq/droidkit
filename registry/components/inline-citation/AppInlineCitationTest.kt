package com.droidkit.registry.components

import android.content.Context
import android.view.HapticFeedbackConstants
import android.widget.FrameLayout
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.click
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppInlineCitationTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val sources =
        listOf(
            AppCitation(
                url = "https://example.com",
                title = "Modern web development",
                description = "A survey of how frontend frameworks have changed since 2020.",
            ),
            AppCitation(
                url = "https://svelte.dev",
                title = "Svelte",
                description = "A compiler that turns declarative components into efficient JavaScript.",
            ),
            AppCitation(
                url = "https://kit.svelte.dev",
                title = "SvelteKit",
            ),
        )

    @Test
    fun hostnameIsParsedFromUrl() {
        assertEquals("example.com", citationHostname("https://www.example.com/path"))
        assertEquals("svelte.dev", citationHostname("https://svelte.dev"))
    }

    @Test
    fun citePlaceholderBecomesGroupOne() {
        val spans = splitCitedText(normalizeCitationText("Hello {cite} there."))
        assertEquals(
            listOf(
                CitedSpan.Words("Hello "),
                CitedSpan.Mark(1),
                CitedSpan.Words(" there."),
            ),
            spans,
        )
    }

    @Test
    fun pillShowsHostnameAndExtraCount() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources,
                )
            }
        }

        composeRule.onNodeWithText("Svelte is fast ", substring = true).assertExists()
        composeRule.onNodeWithText("on the web.", substring = true).assertExists()
        composeRule.onNodeWithContentDescription("example.com and 2 more sources").assertExists()
    }

    @Test
    fun pillTouchTargetIsAtLeast48Dp() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources,
                )
            }
        }

        val node = composeRule.onNodeWithContentDescription("example.com and 2 more sources")
        val clipped = node.getBoundsInRoot()
        val unclipped = node.getUnclippedBoundsInRoot()
        assertTrue("clipped width ${clipped.width}", clipped.width >= 48.dp)
        assertTrue("clipped height ${clipped.height}", clipped.height >= 48.dp)
        assertTrue(
            "overlay should cover example.com +2, not a 48 dp square; width was ${clipped.width}",
            clipped.width > 48.dp,
        )
        assertTrue("unclipped width ${unclipped.width}", unclipped.width >= 48.dp)
        assertTrue("unclipped height ${unclipped.height}", unclipped.height >= 48.dp)
    }

    @Test
    fun tappingTheVisibleHostOpensTheCard() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources,
                )
            }
        }

        composeRule.onNodeWithText("example.com", useUnmergedTree = true).performTouchInput { click() }
        composeRule.onNodeWithText("Modern web development").assertExists()
    }

    @Test
    fun tappingTheExtraCountOpensTheCard() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources,
                )
            }
        }

        composeRule.onNodeWithText(" +2", useUnmergedTree = true).performTouchInput { click() }
        composeRule.onNodeWithText("Modern web development").assertExists()
    }

    @Test
    fun tapOpensSourceAndSecondTapClosesIt() {
        lateinit var recorder: RecordingView
        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    AppInlineCitation(
                        text = "Svelte is fast {cite} on the web.",
                        sources = sources,
                    )
                }
            }
        }

        composeRule.onNodeWithText("Modern web development").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("example.com and 2 more sources").performClick()
        composeRule.onNodeWithText("Modern web development").assertExists()
        composeRule.onNodeWithText("1 of 3").assertExists()
        assertTrue(recorder.constants.contains(HapticFeedbackConstants.CONTEXT_CLICK))
        composeRule.onNodeWithContentDescription("example.com and 2 more sources").performClick()
        composeRule.onNodeWithText("Modern web development").assertDoesNotExist()
    }

    @Test
    fun extraSourcesPageOneAtATimeAndUpdateSpokenTitle() {
        lateinit var recorder: RecordingView
        composeRule.setContent {
            recorder = RecordingView(LocalView.current.context)
            CompositionLocalProvider(LocalView provides recorder) {
                AppTheme {
                    AppInlineCitation(
                        text = "Svelte is fast {cite} on the web.",
                        sources = sources,
                        expanded = true,
                    )
                }
            }
        }

        val pill = composeRule.onNodeWithContentDescription("example.com and 2 more sources")
        pill.assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                "Showing Modern web development",
            ),
        )
        composeRule.onNodeWithText("Modern web development").assertExists()
        composeRule.onNodeWithContentDescription("Previous source").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("Next source").performClick()
        composeRule.onNodeWithText("Svelte").assertExists()
        composeRule.onNodeWithText("2 of 3").assertExists()
        pill.assert(
            SemanticsMatcher.expectValue(
                SemanticsProperties.StateDescription,
                "Showing Svelte",
            ),
        )
        assertTrue(recorder.constants.contains(HapticFeedbackConstants.CLOCK_TICK))
    }

    @Test
    fun openSourceFiresCallback() {
        var opened: String? = null
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources.take(1),
                    expanded = true,
                    onOpenUrl = { opened = it },
                )
            }
        }

        composeRule.onNodeWithText("Open source").performClick()
        assertEquals("https://example.com", opened)
    }

    @Test
    fun singleSourceHasNoExtraCount() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources.take(1),
                )
            }
        }

        composeRule.onNodeWithContentDescription("example.com").assertExists()
        composeRule.onNodeWithContentDescription("example.com and 2 more sources").assertDoesNotExist()
    }

    @Test
    fun unknownMarkerStaysAsText() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Hello [9] there [1] done.",
                    citations = mapOf(1 to sources.take(1)),
                )
            }
        }

        composeRule.onNodeWithText("Hello [9] there ", substring = true).assertExists()
        composeRule.onNodeWithContentDescription("example.com").assertExists()
    }

    @Test
    fun twoMarkersOpenTheirOwnSources() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "First [1] and second [2].",
                    citations =
                        mapOf(
                            1 to listOf(AppCitation(url = "https://example.com", title = "One")),
                            2 to listOf(AppCitation(url = "https://svelte.dev", title = "Two")),
                        ),
                )
            }
        }

        composeRule.onNodeWithContentDescription("example.com").assertExists()
        composeRule.onNodeWithContentDescription("svelte.dev").assertExists()
        composeRule.onNodeWithContentDescription("example.com").performClick()
        composeRule.onNodeWithText("One").assertExists()
        composeRule.onNodeWithContentDescription("svelte.dev").performClick()
        composeRule.onNodeWithText("One").assertDoesNotExist()
        composeRule.onNodeWithText("Two").assertExists()
    }

    @Test
    fun openPageSurvivesRestoration() {
        val restorationTester = StateRestorationTester(composeRule)
        restorationTester.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources,
                    expanded = true,
                )
            }
        }

        composeRule.onNodeWithContentDescription("Next source").performClick()
        composeRule.onNodeWithText("Svelte").assertExists()
        restorationTester.emulateSavedInstanceStateRestore()
        composeRule.onNodeWithText("Svelte").assertExists()
        composeRule.onNodeWithText("2 of 3").assertExists()
    }

    private class RecordingView(context: Context) : FrameLayout(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }
}
