package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
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
private fun ButtonPreviewSurface(
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

@Composable
private fun ContinueIcon() {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
        contentDescription = null,
    )
}

@Preview(showBackground = true, name = "button default")
@Composable
internal fun AppButtonDefaultPreview() {
    ButtonPreviewSurface {
        AppButton(text = "Continue", onClick = {})
    }
}

@Preview(showBackground = true, name = "button loading")
@Composable
internal fun AppButtonLoadingPreview() {
    ButtonPreviewSurface {
        AppButton(text = "Continue", loading = true, onClick = {})
    }
}

@Preview(showBackground = true, name = "button disabled")
@Composable
internal fun AppButtonDisabledPreview() {
    ButtonPreviewSurface {
        AppButton(text = "Continue", enabled = false, onClick = {})
    }
}

@Preview(showBackground = true, name = "button icon")
@Composable
internal fun AppButtonIconPreview() {
    ButtonPreviewSurface {
        AppButton(text = "Continue", onClick = {}, leadingIcon = { ContinueIcon() })
    }
}

@Preview(showBackground = true, name = "button long-text", widthDp = 240)
@Composable
internal fun AppButtonLongTextPreview() {
    ButtonPreviewSurface {
        AppButton(
            text = "Continue with a much longer label than usual",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "button rtl")
@Composable
internal fun AppButtonRtlPreview() {
    ButtonPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppButton(text = "Continue", onClick = {}, leadingIcon = { ContinueIcon() })
        }
    }
}

@Preview(showBackground = true, name = "button dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppButtonDarkPreview() {
    ButtonPreviewSurface(darkTheme = true) {
        AppButton(text = "Continue", onClick = {})
    }
}

@Preview(showBackground = true, name = "button large-font", fontScale = 2f)
@Composable
internal fun AppButtonLargeFontPreview() {
    ButtonPreviewSurface {
        AppButton(text = "Continue", onClick = {})
    }
}
