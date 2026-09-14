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
private fun SettingRowPreviewSurface(
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

@Preview(showBackground = true, name = "setting-row default")
@Composable
internal fun SettingRowDefaultPreview() {
    SettingRowPreviewSurface {
        AppSettingRow(title = "Appearance", value = "System", onClick = {})
    }
}

@Preview(showBackground = true, name = "setting-row with-description")
@Composable
internal fun SettingRowWithDescriptionPreview() {
    SettingRowPreviewSurface {
        AppSettingRow(title = "Appearance", value = "System", description = "Matches the device setting", onClick = {})
    }
}

@Preview(showBackground = true, name = "setting-row toggle")
@Composable
internal fun SettingRowTogglePreview() {
    SettingRowPreviewSurface {
        AppSettingToggle(title = "Notifications", checked = true, onCheckedChange = {}, description = "Receive important updates")
    }
}

@Preview(showBackground = true, name = "setting-row disabled")
@Composable
internal fun SettingRowDisabledPreview() {
    SettingRowPreviewSurface {
        AppSettingRow(title = "Appearance", value = "System", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "setting-row rtl")
@Composable
internal fun SettingRowRtlPreview() {
    SettingRowPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppSettingRow(title = "Appearance", value = "System", onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "setting-row dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SettingRowDarkPreview() {
    SettingRowPreviewSurface(darkTheme = true) {
        AppSettingRow(title = "Appearance", value = "System", onClick = {})
    }
}

@Preview(showBackground = true, name = "setting-row large-font", fontScale = 2f)
@Composable
internal fun SettingRowLargeFontPreview() {
    SettingRowPreviewSurface {
        AppSettingRow(title = "Appearance", value = "System", onClick = {})
    }
}
