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
private fun SearchFieldPreviewSurface(
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

@Preview(showBackground = true, name = "search-field default")
@Composable
internal fun SearchFieldDefaultPreview() {
    SearchFieldPreviewSurface {
        AppSearchField(value = "", onValueChange = {}, onSearch = {})
    }
}

@Preview(showBackground = true, name = "search-field filled")
@Composable
internal fun SearchFieldFilledPreview() {
    SearchFieldPreviewSurface {
        AppSearchField(value = "projects", onValueChange = {}, onSearch = {})
    }
}

@Preview(showBackground = true, name = "search-field disabled")
@Composable
internal fun SearchFieldDisabledPreview() {
    SearchFieldPreviewSurface {
        AppSearchField(value = "projects", onValueChange = {}, onSearch = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "search-field rtl")
@Composable
internal fun SearchFieldRtlPreview() {
    SearchFieldPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppSearchField(value = "projects", onValueChange = {}, onSearch = {})
        }
    }
}

@Preview(showBackground = true, name = "search-field dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SearchFieldDarkPreview() {
    SearchFieldPreviewSurface(darkTheme = true) {
        AppSearchField(value = "projects", onValueChange = {}, onSearch = {})
    }
}

@Preview(showBackground = true, name = "search-field large-font", fontScale = 2f)
@Composable
internal fun SearchFieldLargeFontPreview() {
    SearchFieldPreviewSurface {
        AppSearchField(value = "projects", onValueChange = {}, onSearch = {})
    }
}
