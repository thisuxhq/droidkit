package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppCitation
import com.droidkit.registry.components.AppInlineCitation
import com.droidkit.registry.theme.AppTheme

@Composable
fun InlineCitationScreen(onBack: () -> Unit) {
    var lastOpened by rememberSaveable { mutableStateOf("") }
    ShowcaseScaffold(title = "inline-citation", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.lg),
        ) {
            Text(
                text = "Tap the pill. The source opens under the sentence, not over it.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AppInlineCitation(
                text =
                    "Modern web development has evolved significantly over the past few years. " +
                        "New frameworks like Svelte offer better performance and developer experience {cite} " +
                        "compared to traditional approaches.",
                sources =
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
                    ),
                onOpenUrl = { lastOpened = it },
            )
            AppInlineCitation(
                text = "A single source does not grow a +N {cite} because there is nothing waiting.",
                sources =
                    listOf(
                        AppCitation(
                            url = "https://kotlinlang.org",
                            title = "Kotlin",
                            quote = "A modern programming language that makes developers happier.",
                        ),
                    ),
                onOpenUrl = { lastOpened = it },
            )
            if (lastOpened.isNotEmpty()) {
                Text(
                    text = "Opened $lastOpened",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
