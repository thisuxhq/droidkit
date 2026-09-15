package com.droidkit.registry.components

import android.content.Context
import android.view.HapticFeedbackConstants
import android.widget.FrameLayout
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
        composeRule.onNodeWithContentDescription("Citation, example.com and 2 more sources").assertExists()
        composeRule.onNodeWithText("example.com", useUnmergedTree = true).assertExists()
        composeRule.onNodeWithText(" +2", useUnmergedTree = true).assertExists()
    }

    @Test
    fun tapOpensSourceAndSecondTapClosesIt() {
        composeRule.setContent {
            AppTheme {
                AppInlineCitation(
                    text = "Svelte is fast {cite} on the web.",
                    sources = sources,
                )
            }
        }

        composeRule.onNodeWithText("Modern web development").assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Citation, example.com and 2 more sources").performClick()
        composeRule.onNodeWithText("Modern web development").assertExists()
        composeRule.onNodeWithText("1 of 3").assertExists()
        composeRule.onNodeWithContentDescription("Citation, example.com and 2 more sources").performClick()
        composeRule.onNodeWithText("Modern web development").assertDoesNotExist()
    }

    @Test
    fun extraSourcesPageOneAtATime() {
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

        composeRule.onNodeWithText("Modern web development").assertExists()
        composeRule.onNodeWithContentDescription("Previous source").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("Next source").performClick()
        composeRule.onNodeWithText("Svelte").assertExists()
        composeRule.onNodeWithText("2 of 3").assertExists()
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

        composeRule.onNodeWithContentDescription("Citation, example.com").assertExists()
        composeRule.onNodeWithText(" +2", useUnmergedTree = true).assertDoesNotExist()
        composeRule.onNodeWithText("1 of 3").assertDoesNotExist()
    }

    private class RecordingView(context: Context) : FrameLayout(context) {
        val constants = mutableListOf<Int>()

        override fun performHapticFeedback(feedbackConstant: Int): Boolean {
            constants += feedbackConstant
            return true
        }
    }
}
