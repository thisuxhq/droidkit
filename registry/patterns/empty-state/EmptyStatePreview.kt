package com.droidkit.registry.patterns

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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

@Composable
private fun EmptyStatePreviewSurface(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Preview(showBackground = true, name = "empty-state default")
@Composable
internal fun EmptyStateDefaultPreview() {
    EmptyStatePreviewSurface {
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
            action = "Create project",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "empty-state with-icon")
@Composable
internal fun EmptyStateWithIconPreview() {
    EmptyStatePreviewSurface {
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
            action = "Create project",
            onAction = {},
            icon = Icons.Filled.Info,
        )
    }
}

@Preview(showBackground = true, name = "empty-state no-action")
@Composable
internal fun EmptyStateNoActionPreview() {
    EmptyStatePreviewSurface {
        EmptyState(
            title = "No results",
            description = "Try a different search term.",
        )
    }
}

@Preview(showBackground = true, name = "empty-state long-text", widthDp = 320)
@Composable
internal fun EmptyStateLongTextPreview() {
    EmptyStatePreviewSurface {
        EmptyState(
            title = "You have not saved any articles to read later",
            description =
                "Saved articles stay available offline and sync across your devices. " +
                    "Tap the bookmark on any article to keep it here.",
            action = "Browse featured articles",
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.Info,
        )
    }
}

@Preview(showBackground = true, name = "empty-state rtl")
@Composable
internal fun EmptyStateRtlPreview() {
    EmptyStatePreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            EmptyState(
                title = "No projects yet",
                description = "Create your first project to get started.",
                action = "Create project",
                onAction = {},
                icon = Icons.Filled.Info,
            )
        }
    }
}

@Preview(showBackground = true, name = "empty-state dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun EmptyStateDarkPreview() {
    EmptyStatePreviewSurface(darkTheme = true) {
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
            action = "Create project",
            onAction = {},
            icon = Icons.Filled.Info,
        )
    }
}

@Preview(showBackground = true, name = "empty-state large-font", fontScale = 2f, widthDp = 360)
@Composable
internal fun EmptyStateLargeFontPreview() {
    EmptyStatePreviewSurface {
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
            action = "Create project",
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.Info,
        )
    }
}
