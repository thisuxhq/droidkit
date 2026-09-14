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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

@Composable
private fun IconButtonPreviewSurface(
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

@Preview(showBackground = true, name = "icon-button default")
@Composable
internal fun IconButtonDefaultPreview() {
    IconButtonPreviewSurface {
        AppIconButton(contentDescription = "More", onClick = {}) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }
    }
}

@Preview(showBackground = true, name = "icon-button loading")
@Composable
internal fun IconButtonLoadingPreview() {
    IconButtonPreviewSurface {
        AppIconButton(contentDescription = "More", onClick = {}, loading = true) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }
    }
}

@Preview(showBackground = true, name = "icon-button disabled")
@Composable
internal fun IconButtonDisabledPreview() {
    IconButtonPreviewSurface {
        AppIconButton(contentDescription = "More", onClick = {}, enabled = false) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }
    }
}

@Preview(showBackground = true, name = "icon-button rtl")
@Composable
internal fun IconButtonRtlPreview() {
    IconButtonPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppIconButton(contentDescription = "More", onClick = {}) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }
        }
    }
}

@Preview(showBackground = true, name = "icon-button dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun IconButtonDarkPreview() {
    IconButtonPreviewSurface(darkTheme = true) {
        AppIconButton(contentDescription = "More", onClick = {}) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }
    }
}

@Preview(showBackground = true, name = "icon-button large-font", fontScale = 2f)
@Composable
internal fun IconButtonLargeFontPreview() {
    IconButtonPreviewSurface {
        AppIconButton(contentDescription = "More", onClick = {}) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }
    }
}
