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
private fun SegmentedPreviewSurface(
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

@Preview(showBackground = true, name = "segmented-control default")
@Composable
internal fun SegmentedControlDefaultPreview() {
    SegmentedPreviewSurface {
        AppSegmentedControl(options = listOf("Day", "Week", "Month"), selectedIndex = 0, onSelect = {})
    }
}

@Preview(showBackground = true, name = "segmented-control selected-end")
@Composable
internal fun SegmentedControlSelectedEndPreview() {
    SegmentedPreviewSurface {
        AppSegmentedControl(options = listOf("Day", "Week", "Month"), selectedIndex = 2, onSelect = {})
    }
}

@Preview(showBackground = true, name = "segmented-control disabled")
@Composable
internal fun SegmentedControlDisabledPreview() {
    SegmentedPreviewSurface {
        AppSegmentedControl(options = listOf("Day", "Week", "Month"), selectedIndex = 0, onSelect = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "segmented-control rtl")
@Composable
internal fun SegmentedControlRtlPreview() {
    SegmentedPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppSegmentedControl(options = listOf("Day", "Week", "Month"), selectedIndex = 0, onSelect = {})
        }
    }
}

@Preview(showBackground = true, name = "segmented-control dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SegmentedControlDarkPreview() {
    SegmentedPreviewSurface(darkTheme = true) {
        AppSegmentedControl(options = listOf("Day", "Week", "Month"), selectedIndex = 0, onSelect = {})
    }
}

@Preview(showBackground = true, name = "segmented-control large-font", fontScale = 2f)
@Composable
internal fun SegmentedControlLargeFontPreview() {
    SegmentedPreviewSurface {
        AppSegmentedControl(options = listOf("Day", "Week", "Month"), selectedIndex = 0, onSelect = {})
    }
}
