package com.droidkit.registry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.droidkit.registry.theme.AppTheme

@Preview(showBackground = true, name = "otp light")
@Composable
internal fun AppOtpInputPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            AppOtpInput(value = "", onValueChange = {})
            AppOtpInput(value = "12", onValueChange = {})
            AppOtpInput(value = "847291", onValueChange = {})
            AppOtpInput(value = "847291", onValueChange = {}, enabled = false)
        }
    }
}

@Preview(showBackground = true, name = "otp dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppOtpInputDarkPreview() {
    AppTheme(darkTheme = true) {
        AppOtpInput(value = "847291", onValueChange = {})
    }
}
