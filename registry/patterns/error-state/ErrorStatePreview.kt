package com.droidkit.registry.patterns

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

@Composable
private fun ErrorStatePreviewSurface(
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

@Preview(showBackground = true, name = "error-state default")
@Composable
internal fun ErrorStateDefaultPreview() {
    ErrorStatePreviewSurface {
        ErrorState(
            title = "Could not load projects",
            description = "Check the connection and try again.",
            action = "Try again",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "error-state with-icon")
@Composable
internal fun ErrorStateWithIconPreview() {
    ErrorStatePreviewSurface {
        ErrorState(
            title = "Could not load projects",
            description = "Check the connection and try again.",
            action = "Try again",
            onAction = {},
            icon = Icons.Filled.Info,
        )
    }
}

@Preview(showBackground = true, name = "error-state long-text", widthDp = 320)
@Composable
internal fun ErrorStateLongTextPreview() {
    ErrorStatePreviewSurface {
        ErrorState(
            title = "Could not reach the project archive for this workspace",
            description = "The last sync stopped halfway. Check the connection, then try again to finish the download.",
            action = "Try again",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "error-state rtl")
@Composable
internal fun ErrorStateRtlPreview() {
    ErrorStatePreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ErrorState(
            title = "Could not load projects",
            description = "Check the connection and try again.",
            action = "Try again",
            onAction = {},
        )
        }
    }
}

@Preview(showBackground = true, name = "error-state dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ErrorStateDarkPreview() {
    ErrorStatePreviewSurface(darkTheme = true) {
        ErrorState(
            title = "Could not load projects",
            description = "Check the connection and try again.",
            action = "Try again",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "error-state large-font", fontScale = 2f)
@Composable
internal fun ErrorStateLargeFontPreview() {
    ErrorStatePreviewSurface {
        ErrorState(
            title = "Could not load projects",
            description = "Check the connection and try again.",
            action = "Try again",
            onAction = {},
        )
    }
}
