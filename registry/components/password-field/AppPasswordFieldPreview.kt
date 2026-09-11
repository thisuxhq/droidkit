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
private fun PasswordFieldPreviewSurface(
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

@Preview(showBackground = true, name = "password-field default")
@Composable
internal fun AppPasswordFieldDefaultPreview() {
    PasswordFieldPreviewSurface {
        AppPasswordField(value = "", onValueChange = {})
    }
}

@Preview(showBackground = true, name = "password-field filled")
@Composable
internal fun AppPasswordFieldFilledPreview() {
    PasswordFieldPreviewSurface {
        AppPasswordField(value = "hunter2", onValueChange = {})
    }
}

@Preview(showBackground = true, name = "password-field revealed")
@Composable
internal fun AppPasswordFieldRevealedPreview() {
    PasswordFieldPreviewSurface {
        AppPasswordField(value = "hunter2", onValueChange = {}, initialVisible = true)
    }
}

@Preview(showBackground = true, name = "password-field error")
@Composable
internal fun AppPasswordFieldErrorPreview() {
    PasswordFieldPreviewSurface {
        AppPasswordField(
            value = "short",
            onValueChange = {},
            supportingText = "Use at least 8 characters",
            isError = true,
        )
    }
}

@Preview(showBackground = true, name = "password-field disabled")
@Composable
internal fun AppPasswordFieldDisabledPreview() {
    PasswordFieldPreviewSurface {
        AppPasswordField(value = "hunter2", onValueChange = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "password-field rtl")
@Composable
internal fun AppPasswordFieldRtlPreview() {
    PasswordFieldPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppPasswordField(
                value = "short",
                onValueChange = {},
                supportingText = "Use at least 8 characters",
                isError = true,
            )
        }
    }
}

@Preview(showBackground = true, name = "password-field dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppPasswordFieldDarkPreview() {
    PasswordFieldPreviewSurface(darkTheme = true) {
        AppPasswordField(value = "hunter2", onValueChange = {})
    }
}

@Preview(showBackground = true, name = "password-field large-font", fontScale = 2f)
@Composable
internal fun AppPasswordFieldLargeFontPreview() {
    PasswordFieldPreviewSurface {
        AppPasswordField(
            value = "hunter2",
            onValueChange = {},
            supportingText = "Use at least 8 characters",
        )
    }
}
