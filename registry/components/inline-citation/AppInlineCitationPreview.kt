package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.droidkit.registry.theme.AppTheme

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

private const val SampleParagraph =
    "Modern web development has evolved significantly over the past few years. " +
        "New frameworks like Svelte offer better performance and developer experience {cite} " +
        "compared to traditional approaches."

private val SampleSources =
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
            description = "The official application framework for Svelte.",
        ),
    )

@Composable
private fun InlineCitationPreviewSurface(
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

@Preview(showBackground = true, name = "inline-citation default")
@Composable
internal fun AppInlineCitationDefaultPreview() {
    InlineCitationPreviewSurface {
        AppInlineCitation(text = SampleParagraph, sources = SampleSources)
    }
}

@Preview(showBackground = true, name = "inline-citation open")
@Composable
internal fun AppInlineCitationOpenPreview() {
    InlineCitationPreviewSurface {
        AppInlineCitation(text = SampleParagraph, sources = SampleSources, expanded = true)
    }
}

@Preview(showBackground = true, name = "inline-citation single")
@Composable
internal fun AppInlineCitationSinglePreview() {
    InlineCitationPreviewSurface {
        AppInlineCitation(
            text = SampleParagraph,
            sources = listOf(SampleSources.first()),
        )
    }
}

@Preview(showBackground = true, name = "inline-citation quote")
@Composable
internal fun AppInlineCitationQuotePreview() {
    InlineCitationPreviewSurface {
        AppInlineCitation(
            text = SampleParagraph,
            sources =
                listOf(
                    AppCitation(
                        url = "https://svelte.dev",
                        title = "Svelte",
                        description = "A compiler that turns declarative components into efficient JavaScript.",
                        quote = "Write less code, and let the compiler do the rest.",
                    ),
                ),
            expanded = true,
        )
    }
}

@Preview(showBackground = true, name = "inline-citation two-claims")
@Composable
internal fun AppInlineCitationTwoClaimsPreview() {
    InlineCitationPreviewSurface {
        AppInlineCitation(
            text = "Compose owns the UI [1] and Kotlin owns the language [2].",
            citations =
                mapOf(
                    1 to listOf(AppCitation(url = "https://developer.android.com", title = "Compose")),
                    2 to listOf(AppCitation(url = "https://kotlinlang.org", title = "Kotlin")),
                ),
        )
    }
}

@Preview(showBackground = true, name = "inline-citation rtl")
@Composable
internal fun AppInlineCitationRtlPreview() {
    InlineCitationPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppInlineCitation(
                text =
                    "تطور تطوير الويب الحديث كثيرا خلال السنوات القليلة الماضية. " +
                        "أطر عمل مثل Svelte تقدم أداء أفضل {cite} مقارنة بالأساليب التقليدية.",
                sources =
                    listOf(
                        AppCitation(
                            url = "https://example.com",
                            title = "تطوير الويب الحديث",
                            description = "مسح لتغير أطر عمل الواجهة منذ ٢٠٢٠.",
                        ),
                        SampleSources[1],
                        SampleSources[2],
                    ),
                expanded = true,
            )
        }
    }
}

@Preview(showBackground = true, name = "inline-citation dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppInlineCitationDarkPreview() {
    InlineCitationPreviewSurface(darkTheme = true) {
        AppInlineCitation(text = SampleParagraph, sources = SampleSources, expanded = true)
    }
}

@Preview(showBackground = true, name = "inline-citation large-font", fontScale = 2f)
@Composable
internal fun AppInlineCitationLargeFontPreview() {
    InlineCitationPreviewSurface {
        AppInlineCitation(text = SampleParagraph, sources = SampleSources, expanded = true)
    }
}
