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
private fun TextAreaPreviewSurface(
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

@Preview(showBackground = true, name = "text-area default")
@Composable
internal fun TextAreaDefaultPreview() {
    TextAreaPreviewSurface {
        AppTextArea(value = "", onValueChange = {}, label = "Notes")
    }
}

@Preview(showBackground = true, name = "text-area filled")
@Composable
internal fun TextAreaFilledPreview() {
    TextAreaPreviewSurface {
        AppTextArea(value = "Bring the extra charger and the spare keys.", onValueChange = {}, label = "Notes")
    }
}

@Preview(showBackground = true, name = "text-area counter")
@Composable
internal fun TextAreaCounterPreview() {
    TextAreaPreviewSurface {
        AppTextArea(value = "Short note", onValueChange = {}, label = "Notes", maxLength = 140)
    }
}

@Preview(showBackground = true, name = "text-area error")
@Composable
internal fun TextAreaErrorPreview() {
    TextAreaPreviewSurface {
        AppTextArea(value = "", onValueChange = {}, label = "Notes", supportingText = "Add a note before you send", isError = true)
    }
}

@Preview(showBackground = true, name = "text-area disabled")
@Composable
internal fun TextAreaDisabledPreview() {
    TextAreaPreviewSurface {
        AppTextArea(value = "Bring the extra charger.", onValueChange = {}, label = "Notes", enabled = false)
    }
}

@Preview(showBackground = true, name = "text-area rtl")
@Composable
internal fun TextAreaRtlPreview() {
    TextAreaPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppTextArea(value = "Bring the extra charger and the spare keys.", onValueChange = {}, label = "Notes")
        }
    }
}

@Preview(showBackground = true, name = "text-area dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TextAreaDarkPreview() {
    TextAreaPreviewSurface(darkTheme = true) {
        AppTextArea(value = "Bring the extra charger and the spare keys.", onValueChange = {}, label = "Notes")
    }
}

@Preview(showBackground = true, name = "text-area large-font", fontScale = 2f)
@Composable
internal fun TextAreaLargeFontPreview() {
    TextAreaPreviewSurface {
        AppTextArea(value = "Bring the extra charger and the spare keys.", onValueChange = {}, label = "Notes")
    }
}
