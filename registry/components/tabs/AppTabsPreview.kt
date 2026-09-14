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
private fun TabsPreviewSurface(
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

@Preview(showBackground = true, name = "tabs default")
@Composable
internal fun TabsDefaultPreview() {
    TabsPreviewSurface {
        AppTabs(titles = listOf("Inbox", "Sent", "Drafts"), selectedIndex = 0, onSelect = {})
    }
}

@Preview(showBackground = true, name = "tabs selected-end")
@Composable
internal fun TabsSelectedEndPreview() {
    TabsPreviewSurface {
        AppTabs(titles = listOf("Inbox", "Sent", "Drafts"), selectedIndex = 2, onSelect = {})
    }
}

@Preview(showBackground = true, name = "tabs rtl")
@Composable
internal fun TabsRtlPreview() {
    TabsPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppTabs(titles = listOf("Inbox", "Sent", "Drafts"), selectedIndex = 0, onSelect = {})
        }
    }
}

@Preview(showBackground = true, name = "tabs dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TabsDarkPreview() {
    TabsPreviewSurface(darkTheme = true) {
        AppTabs(titles = listOf("Inbox", "Sent", "Drafts"), selectedIndex = 0, onSelect = {})
    }
}

@Preview(showBackground = true, name = "tabs large-font", fontScale = 2f)
@Composable
internal fun TabsLargeFontPreview() {
    TabsPreviewSurface {
        AppTabs(titles = listOf("Inbox", "Sent", "Drafts"), selectedIndex = 0, onSelect = {})
    }
}
