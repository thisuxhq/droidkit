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

@Composable
private fun SnackbarPreviewSurface(
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

@Preview(showBackground = true, name = "snackbar default")
@Composable
internal fun SnackbarDefaultPreview() {
    SnackbarPreviewSurface {
        AppSnackbar(message = "Draft saved")
    }
}

@Preview(showBackground = true, name = "snackbar with-action")
@Composable
internal fun SnackbarWithActionPreview() {
    SnackbarPreviewSurface {
        AppSnackbar(message = "Draft saved", actionLabel = "Undo", onAction = {})
    }
}

@Preview(showBackground = true, name = "snackbar rtl")
@Composable
internal fun SnackbarRtlPreview() {
    SnackbarPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppSnackbar(message = "Draft saved", actionLabel = "Undo", onAction = {})
        }
    }
}

@Preview(showBackground = true, name = "snackbar dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SnackbarDarkPreview() {
    SnackbarPreviewSurface(darkTheme = true) {
        AppSnackbar(message = "Draft saved", actionLabel = "Undo", onAction = {})
    }
}

@Preview(showBackground = true, name = "snackbar large-font", fontScale = 2f)
@Composable
internal fun SnackbarLargeFontPreview() {
    SnackbarPreviewSurface {
        AppSnackbar(message = "Draft saved", actionLabel = "Undo", onAction = {})
    }
}
