package com.droidkit.registry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.droidkit.registry.theme.AppTheme

@Preview(showBackground = true, name = "text field light")
@Composable
internal fun AppTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
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
            AppTextField(
                value = "you@example.com",
                onValueChange = {},
                label = "Email",
            )
            AppTextField(
                value = "not-an-email",
                onValueChange = {},
                label = "Email",
                supportingText = "Enter a valid email",
                isError = true,
            )
            AppTextField(
                value = "Disabled",
                onValueChange = {},
                label = "Name",
                enabled = false,
            )
        }
    }
}

@Preview(showBackground = true, name = "text field dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppTextFieldDarkPreview() {
    AppTheme(darkTheme = true) {
        AppTextField(
            value = "you@example.com",
            onValueChange = {},
            label = "Email",
        )
    }
}
