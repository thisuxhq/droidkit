package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.droidkit.registry.theme.AppTheme

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

@Composable
private fun TextFieldPreviewSurface(
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

@Preview(showBackground = true, name = "text-field default")
@Composable
internal fun AppTextFieldDefaultPreview() {
    TextFieldPreviewSurface {
        AppTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            placeholder = "you@example.com",
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
        )
    }
}

@Preview(showBackground = true, name = "text-field filled")
@Composable
internal fun AppTextFieldFilledPreview() {
    TextFieldPreviewSurface {
        AppTextField(
            value = "you@example.com",
            onValueChange = {},
            label = "Email",
        )
    }
}

@Preview(showBackground = true, name = "text-field supporting-text")
@Composable
internal fun AppTextFieldSupportingTextPreview() {
    TextFieldPreviewSurface {
        AppTextField(
            value = "",
            onValueChange = {},
            label = "Username",
            supportingText = "Letters, numbers, and underscores only",
        )
    }
}

@Preview(showBackground = true, name = "text-field error")
@Composable
internal fun AppTextFieldErrorPreview() {
    TextFieldPreviewSurface {
        AppTextField(
            value = "not-an-email",
            onValueChange = {},
            label = "Email",
            supportingText = "Enter a valid email",
            isError = true,
        )
    }
}

@Preview(showBackground = true, name = "text-field disabled")
@Composable
internal fun AppTextFieldDisabledPreview() {
    TextFieldPreviewSurface {
        AppTextField(
            value = "Ada Lovelace",
            onValueChange = {},
            label = "Name",
            enabled = false,
        )
    }
}

@Preview(showBackground = true, name = "text-field rtl")
@Composable
internal fun AppTextFieldRtlPreview() {
    TextFieldPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppTextField(
                value = "you@example.com",
                onValueChange = {},
                label = "Email",
                supportingText = "Enter a valid email",
                isError = true,
            )
        }
    }
}

@Preview(showBackground = true, name = "text-field dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppTextFieldDarkPreview() {
    TextFieldPreviewSurface(darkTheme = true) {
        AppTextField(
            value = "you@example.com",
            onValueChange = {},
            label = "Email",
        )
    }
}

@Preview(showBackground = true, name = "text-field large-font", fontScale = 2f)
@Composable
internal fun AppTextFieldLargeFontPreview() {
    TextFieldPreviewSurface {
        AppTextField(
            value = "you@example.com",
            onValueChange = {},
            label = "Email",
            supportingText = "Enter a valid email",
        )
    }
}
