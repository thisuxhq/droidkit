package com.droidkit.registry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.droidkit.registry.theme.AppTheme

@Preview(showBackground = true, name = "button light")
@Composable
internal fun AppButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            AppButton(text = "Continue", onClick = {})
            AppButton(text = "Loading", loading = true, onClick = {})
            AppButton(text = "Disabled", enabled = false, onClick = {})
            AppButton(text = "Continue with a much longer label", onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "button dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppButtonDarkPreview() {
    AppTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            AppButton(text = "Continue", onClick = {})
            AppButton(text = "Loading", loading = true, onClick = {})
            AppButton(text = "Disabled", enabled = false, onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "button large font", fontScale = 1.5f)
@Composable
internal fun AppButtonLargeFontPreview() {
    AppTheme {
        AppButton(text = "Continue", onClick = {})
    }
}
