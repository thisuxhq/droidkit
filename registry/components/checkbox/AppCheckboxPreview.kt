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
private fun CheckboxPreviewSurface(
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

@Preview(showBackground = true, name = "checkbox default")
@Composable
internal fun CheckboxDefaultPreview() {
    CheckboxPreviewSurface {
        AppCheckbox(text = "Remember this device", checked = true, onCheckedChange = {})
    }
}

@Preview(showBackground = true, name = "checkbox unchecked")
@Composable
internal fun CheckboxUncheckedPreview() {
    CheckboxPreviewSurface {
        AppCheckbox(text = "Remember this device", checked = false, onCheckedChange = {})
    }
}

@Preview(showBackground = true, name = "checkbox disabled")
@Composable
internal fun CheckboxDisabledPreview() {
    CheckboxPreviewSurface {
        AppCheckbox(text = "Remember this device", checked = true, onCheckedChange = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "checkbox rtl")
@Composable
internal fun CheckboxRtlPreview() {
    CheckboxPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppCheckbox(text = "Remember this device", checked = true, onCheckedChange = {})
        }
    }
}

@Preview(showBackground = true, name = "checkbox dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun CheckboxDarkPreview() {
    CheckboxPreviewSurface(darkTheme = true) {
        AppCheckbox(text = "Remember this device", checked = true, onCheckedChange = {})
    }
}

@Preview(showBackground = true, name = "checkbox large-font", fontScale = 2f)
@Composable
internal fun CheckboxLargeFontPreview() {
    CheckboxPreviewSurface {
        AppCheckbox(text = "Remember this device", checked = true, onCheckedChange = {})
    }
}
