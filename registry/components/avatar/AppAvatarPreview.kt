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
private fun AvatarPreviewSurface(
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

@Preview(showBackground = true, name = "avatar default")
@Composable
internal fun AvatarDefaultPreview() {
    AvatarPreviewSurface {
        AppAvatar(name = "Ada Lovelace")
    }
}

@Preview(showBackground = true, name = "avatar single-name")
@Composable
internal fun AvatarSingleNamePreview() {
    AvatarPreviewSurface {
        AppAvatar(name = "Ada")
    }
}

@Preview(showBackground = true, name = "avatar long-name")
@Composable
internal fun AvatarLongNamePreview() {
    AvatarPreviewSurface {
        AppAvatar(name = "Ada King Countess of Lovelace")
    }
}

@Preview(showBackground = true, name = "avatar rtl")
@Composable
internal fun AvatarRtlPreview() {
    AvatarPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppAvatar(name = "Ada Lovelace")
        }
    }
}

@Preview(showBackground = true, name = "avatar dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AvatarDarkPreview() {
    AvatarPreviewSurface(darkTheme = true) {
        AppAvatar(name = "Ada Lovelace")
    }
}

@Preview(showBackground = true, name = "avatar large-font", fontScale = 2f)
@Composable
internal fun AvatarLargeFontPreview() {
    AvatarPreviewSurface {
        AppAvatar(name = "Ada Lovelace")
    }
}
