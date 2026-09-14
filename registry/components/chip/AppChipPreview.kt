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
private fun ChipPreviewSurface(
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

@Preview(showBackground = true, name = "chip default")
@Composable
internal fun ChipDefaultPreview() {
    ChipPreviewSurface {
        AppChip(text = "On sale", selected = false, onClick = {})
    }
}

@Preview(showBackground = true, name = "chip selected")
@Composable
internal fun ChipSelectedPreview() {
    ChipPreviewSurface {
        AppChip(text = "On sale", selected = true, onClick = {})
    }
}

@Preview(showBackground = true, name = "chip disabled")
@Composable
internal fun ChipDisabledPreview() {
    ChipPreviewSurface {
        AppChip(text = "On sale", selected = true, onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "chip rtl")
@Composable
internal fun ChipRtlPreview() {
    ChipPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppChip(text = "On sale", selected = true, onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "chip dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ChipDarkPreview() {
    ChipPreviewSurface(darkTheme = true) {
        AppChip(text = "On sale", selected = true, onClick = {})
    }
}

@Preview(showBackground = true, name = "chip large-font", fontScale = 2f)
@Composable
internal fun ChipLargeFontPreview() {
    ChipPreviewSurface {
        AppChip(text = "On sale", selected = true, onClick = {})
    }
}
