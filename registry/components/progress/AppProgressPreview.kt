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
private fun ProgressPreviewSurface(
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

@Preview(showBackground = true, name = "progress default")
@Composable
internal fun ProgressDefaultPreview() {
    ProgressPreviewSurface {
        AppProgress(label = "Uploading photo", progress = 0.45f)
    }
}

@Preview(showBackground = true, name = "progress indeterminate")
@Composable
internal fun ProgressIndeterminatePreview() {
    ProgressPreviewSurface {
        AppProgress(label = "Uploading photo")
    }
}

@Preview(showBackground = true, name = "progress complete")
@Composable
internal fun ProgressCompletePreview() {
    ProgressPreviewSurface {
        AppProgress(label = "Uploading photo", progress = 1f)
    }
}

@Preview(showBackground = true, name = "progress rtl")
@Composable
internal fun ProgressRtlPreview() {
    ProgressPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppProgress(label = "Uploading photo", progress = 0.45f)
        }
    }
}

@Preview(showBackground = true, name = "progress dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ProgressDarkPreview() {
    ProgressPreviewSurface(darkTheme = true) {
        AppProgress(label = "Uploading photo", progress = 0.45f)
    }
}

@Preview(showBackground = true, name = "progress large-font", fontScale = 2f)
@Composable
internal fun ProgressLargeFontPreview() {
    ProgressPreviewSurface {
        AppProgress(label = "Uploading photo", progress = 0.45f)
    }
}
