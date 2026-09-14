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
import androidx.compose.ui.unit.dp
import com.droidkit.registry.theme.AppTheme

private val SkeletonPreviewTall = 72.dp

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

@Composable
private fun SkeletonPreviewSurface(
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

@Preview(showBackground = true, name = "skeleton default")
@Composable
internal fun SkeletonDefaultPreview() {
    SkeletonPreviewSurface {
        AppSkeleton()
    }
}

@Preview(showBackground = true, name = "skeleton tall")
@Composable
internal fun SkeletonTallPreview() {
    SkeletonPreviewSurface {
        AppSkeleton(height = SkeletonPreviewTall)
    }
}

@Preview(showBackground = true, name = "skeleton rtl")
@Composable
internal fun SkeletonRtlPreview() {
    SkeletonPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppSkeleton()
        }
    }
}

@Preview(showBackground = true, name = "skeleton dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SkeletonDarkPreview() {
    SkeletonPreviewSurface(darkTheme = true) {
        AppSkeleton()
    }
}

@Preview(showBackground = true, name = "skeleton large-font", fontScale = 2f)
@Composable
internal fun SkeletonLargeFontPreview() {
    SkeletonPreviewSurface {
        AppSkeleton()
    }
}
