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

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

@Composable
private fun ConfirmDialogPreviewSurface(
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

@Preview(showBackground = true, name = "confirm-dialog default")
@Composable
internal fun ConfirmDialogDefaultPreview() {
    ConfirmDialogPreviewSurface {
        ConfirmationDialog(
            title = "Discard draft",
            description = "This draft will be gone. You can start a new one later.",
            confirm = "Discard",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true, name = "confirm-dialog destructive")
@Composable
internal fun ConfirmDialogDestructivePreview() {
    ConfirmDialogPreviewSurface {
        ConfirmationDialog(
            title = "Delete project",
            description = "This project and its files will be removed from this device.",
            confirm = "Delete",
            onConfirm = {},
            onDismiss = {},
            destructive = true,
        )
    }
}

@Preview(showBackground = true, name = "confirm-dialog rtl")
@Composable
internal fun ConfirmDialogRtlPreview() {
    ConfirmDialogPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ConfirmationDialog(
            title = "Discard draft",
            description = "This draft will be gone. You can start a new one later.",
            confirm = "Discard",
            onConfirm = {},
            onDismiss = {},
        )
        }
    }
}

@Preview(showBackground = true, name = "confirm-dialog dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ConfirmDialogDarkPreview() {
    ConfirmDialogPreviewSurface(darkTheme = true) {
        ConfirmationDialog(
            title = "Discard draft",
            description = "This draft will be gone. You can start a new one later.",
            confirm = "Discard",
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview(showBackground = true, name = "confirm-dialog large-font", fontScale = 2f)
@Composable
internal fun ConfirmDialogLargeFontPreview() {
    ConfirmDialogPreviewSurface {
        ConfirmationDialog(
            title = "Discard draft",
            description = "This draft will be gone. You can start a new one later.",
            confirm = "Discard",
            onConfirm = {},
            onDismiss = {},
        )
    }
}
