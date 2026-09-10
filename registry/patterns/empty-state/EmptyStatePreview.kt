package com.droidkit.registry.patterns

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.droidkit.registry.theme.AppTheme

@Preview(showBackground = true, name = "empty state light")
@Composable
internal fun EmptyStatePreview() {
    AppTheme {
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
            icon = Icons.Filled.Info,
            action = "Create project",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "empty state no action")
@Composable
internal fun EmptyStateNoActionPreview() {
    AppTheme {
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
        )
    }
}

@Preview(showBackground = true, name = "empty state dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun EmptyStateDarkPreview() {
    AppTheme(darkTheme = true) {
        EmptyState(
            title = "No projects yet",
            description = "Create your first project to get started.",
            icon = Icons.Filled.Info,
            action = "Create project",
            onAction = {},
        )
    }
}
