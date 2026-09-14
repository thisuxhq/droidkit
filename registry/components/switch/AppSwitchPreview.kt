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
private fun SwitchPreviewSurface(
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

@Preview(showBackground = true, name = "switch default")
@Composable
internal fun SwitchDefaultPreview() {
    SwitchPreviewSurface {
        AppSwitch(text = "Notifications", checked = true, onCheckedChange = {{}}, description = "Receive important updates")
    }
}

@Preview(showBackground = true, name = "switch off")
@Composable
internal fun SwitchOffPreview() {
    SwitchPreviewSurface {
        AppSwitch(text = "Notifications", checked = false, onCheckedChange = {})
    }
}

@Preview(showBackground = true, name = "switch disabled")
@Composable
internal fun SwitchDisabledPreview() {
    SwitchPreviewSurface {
        AppSwitch(text = "Notifications", checked = true, onCheckedChange = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "switch rtl")
@Composable
internal fun SwitchRtlPreview() {
    SwitchPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppSwitch(text = "Notifications", checked = true, onCheckedChange = {{}}, description = "Receive important updates")
        }
    }
}

@Preview(showBackground = true, name = "switch dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SwitchDarkPreview() {
    SwitchPreviewSurface(darkTheme = true) {
        AppSwitch(text = "Notifications", checked = true, onCheckedChange = {{}}, description = "Receive important updates")
    }
}

@Preview(showBackground = true, name = "switch large-font", fontScale = 2f)
@Composable
internal fun SwitchLargeFontPreview() {
    SwitchPreviewSurface {
        AppSwitch(text = "Notifications", checked = true, onCheckedChange = {{}}, description = "Receive important updates")
    }
}
