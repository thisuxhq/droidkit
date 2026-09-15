package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.droidkit.registry.theme.AppTheme

private val OpenAi =
    AppModelProvider(id = "openai", name = "OpenAI", mark = Icons.Filled.Star)
private val Anthropic = AppModelProvider(id = "anthropic", name = "Anthropic")
private val Google = AppModelProvider(id = "google", name = "Google")

private val ShortCatalog =
    listOf(
        AppModelOption("gpt-4o", "GPT-4o", OpenAi, "Answers fast"),
        AppModelOption("o3", "o3", OpenAi, "Thinks first"),
        AppModelOption("sonnet", "Sonnet 4", Anthropic, "Balanced"),
        AppModelOption("opus", "Opus 4", Anthropic, "Deep reasoning"),
        AppModelOption("flash", "Gemini Flash", Google, "Everyday"),
        AppModelOption("pro", "Gemini Pro", Google, "Long documents"),
    )

private val LongCatalog =
    ShortCatalog +
        listOf(
            AppModelOption("gpt-4o-mini", "GPT-4o mini", OpenAi, "Cheaper and faster"),
            AppModelOption("haiku", "Haiku", Anthropic, "Quick drafts"),
            AppModelOption("nano", "Gemini Nano", Google, "On device"),
        )

private val SingleProviderCatalog = ShortCatalog.filter { it.provider.id == "openai" }

@Composable
private fun ModelSelectorPreviewSurface(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.padding(AppTheme.spacing.lg)) {
                content()
            }
        }
    }
}

@Preview(showBackground = true, name = "model-selector default")
@Composable
internal fun ModelSelectorDefaultPreview() {
    ModelSelectorPreviewSurface {
        AppModelSelector(
            models = ShortCatalog,
            selectedId = "gpt-4o",
            onSelect = {},
        )
    }
}

@Preview(showBackground = true, name = "model-selector short-list")
@Composable
internal fun ModelSelectorShortListPreview() {
    ModelSelectorPreviewSurface {
        AppBottomSheetChrome(title = "Choose a model") {
            AppModelSelectorSheetPreview(
                models = ShortCatalog,
                selectedId = "gpt-4o",
                query = "",
            )
        }
    }
}

@Preview(showBackground = true, name = "model-selector sheet-open")
@Composable
internal fun ModelSelectorSheetOpenPreview() {
    ModelSelectorPreviewSurface {
        AppBottomSheetChrome(title = "Choose a model") {
            AppModelSelectorSheetPreview(
                models = LongCatalog,
                selectedId = "gpt-4o",
                query = "",
            )
        }
    }
}

@Preview(showBackground = true, name = "model-selector search-results")
@Composable
internal fun ModelSelectorSearchResultsPreview() {
    ModelSelectorPreviewSurface {
        AppBottomSheetChrome(title = "Choose a model") {
            AppModelSelectorSheetPreview(
                models = LongCatalog,
                selectedId = "gpt-4o",
                query = "gpt4",
            )
        }
    }
}

@Preview(showBackground = true, name = "model-selector no-results")
@Composable
internal fun ModelSelectorNoResultsPreview() {
    ModelSelectorPreviewSurface {
        AppBottomSheetChrome(title = "Choose a model") {
            AppModelSelectorSheetPreview(
                models = LongCatalog,
                selectedId = "gpt-4o",
                query = "llama",
            )
        }
    }
}

@Preview(showBackground = true, name = "model-selector disabled")
@Composable
internal fun ModelSelectorDisabledPreview() {
    ModelSelectorPreviewSurface {
        AppModelSelector(
            models = ShortCatalog,
            selectedId = "gpt-4o",
            onSelect = {},
            enabled = false,
        )
    }
}

@Preview(showBackground = true, name = "model-selector rtl")
@Composable
internal fun ModelSelectorRtlPreview() {
    ModelSelectorPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppBottomSheetChrome(title = "Choose a model") {
                AppModelSelectorSheetPreview(
                    models = LongCatalog,
                    selectedId = "gpt-4o",
                    query = "",
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "model-selector dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ModelSelectorDarkPreview() {
    ModelSelectorPreviewSurface(darkTheme = true) {
        AppBottomSheetChrome(title = "Choose a model") {
            AppModelSelectorSheetPreview(
                models = LongCatalog,
                selectedId = "gpt-4o",
                query = "gpt4",
            )
        }
    }
}

@Preview(showBackground = true, name = "model-selector large-font", fontScale = 2f)
@Composable
internal fun ModelSelectorLargeFontPreview() {
    ModelSelectorPreviewSurface {
        AppModelSelector(
            models =
                listOf(
                    AppModelOption(
                        id = "long",
                        name = "GPT-4o mini high reasoning",
                        provider = OpenAi,
                        capability = "Answers fast",
                    ),
                ),
            selectedId = "long",
            onSelect = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "model-selector single-provider")
@Composable
internal fun ModelSelectorSingleProviderPreview() {
    ModelSelectorPreviewSurface {
        AppBottomSheetChrome(title = "Choose a model") {
            AppModelSelectorSheetPreview(
                models = SingleProviderCatalog,
                selectedId = "gpt-4o",
                query = "",
            )
        }
    }
}
