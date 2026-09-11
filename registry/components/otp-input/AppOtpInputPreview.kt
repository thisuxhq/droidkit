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
private fun OtpInputPreviewSurface(
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

@Preview(showBackground = true, name = "otp-input default")
@Composable
internal fun AppOtpInputDefaultPreview() {
    OtpInputPreviewSurface {
        AppOtpInput(value = "", onValueChange = {})
    }
}

@Preview(showBackground = true, name = "otp-input partial")
@Composable
internal fun AppOtpInputPartialPreview() {
    OtpInputPreviewSurface {
        AppOtpInput(value = "84", onValueChange = {})
    }
}

@Preview(showBackground = true, name = "otp-input filled")
@Composable
internal fun AppOtpInputFilledPreview() {
    OtpInputPreviewSurface {
        AppOtpInput(value = "847291", onValueChange = {})
    }
}

@Preview(showBackground = true, name = "otp-input disabled")
@Composable
internal fun AppOtpInputDisabledPreview() {
    OtpInputPreviewSurface {
        AppOtpInput(value = "847291", onValueChange = {}, enabled = false)
    }
}

@Preview(showBackground = true, name = "otp-input rtl")
@Composable
internal fun AppOtpInputRtlPreview() {
    OtpInputPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppOtpInput(value = "84", onValueChange = {})
        }
    }
}

@Preview(showBackground = true, name = "otp-input dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppOtpInputDarkPreview() {
    OtpInputPreviewSurface(darkTheme = true) {
        AppOtpInput(value = "84", onValueChange = {})
    }
}

@Preview(showBackground = true, name = "otp-input large-font", fontScale = 2f)
@Composable
internal fun AppOtpInputLargeFontPreview() {
    OtpInputPreviewSurface {
        AppOtpInput(value = "847291", onValueChange = {})
    }
}
