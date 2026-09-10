package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.droidkit.registry.patterns.EmptyState

@Composable
fun EmptyStateScreen(onBack: () -> Unit) {
    ShowcaseScaffold(title = "empty-state", onBack = onBack) { padding ->
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
            icon = Icons.Filled.Info,
            action = "Create project",
            onAction = {},
        )
    }
}
