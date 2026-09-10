package com.droidkit.registry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.droidkit.registry.theme.AppTheme

@Preview(showBackground = true, name = "password field light")
@Composable
internal fun AppPasswordFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            AppPasswordField(value = "", onValueChange = {}, label = "Password")
            AppPasswordField(value = "hunter2", onValueChange = {})
            AppPasswordField(
                value = "short",
                onValueChange = {},
                supportingText = "Use at least 8 characters",
                isError = true,
            )
            AppPasswordField(value = "secret", onValueChange = {}, enabled = false)
        }
    }
}

@Preview(showBackground = true, name = "password field dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppPasswordFieldDarkPreview() {
    AppTheme(darkTheme = true) {
        AppPasswordField(value = "hunter2", onValueChange = {})
    }
}
