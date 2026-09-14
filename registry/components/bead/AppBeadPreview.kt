package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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

private val PreviewOptions =
    listOf(
        "Never send me notification emails",
        "Only send me a periodic notification email",
        "Send me an email for every notification",
    )

@Composable
private fun BeadPreviewSurface(
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

@Preview(showBackground = true, name = "bead default")
@Composable
internal fun AppBeadDefaultPreview() {
    BeadPreviewSurface {
        AppBead(
            options = PreviewOptions,
            selectedIndex = 1,
            onSelect = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "bead first")
@Composable
internal fun AppBeadFirstPreview() {
    BeadPreviewSurface {
        AppBead(
            options = PreviewOptions,
            selectedIndex = 0,
            onSelect = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "bead disabled")
@Composable
internal fun AppBeadDisabledPreview() {
    BeadPreviewSurface {
        AppBead(
            options = PreviewOptions,
            selectedIndex = 1,
            onSelect = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )
    }
}

@Preview(showBackground = true, name = "bead long-text", widthDp = 280)
@Composable
internal fun AppBeadLongTextPreview() {
    BeadPreviewSurface {
        AppBead(
            options =
                listOf(
                    "Never send me notification emails about anything at all",
                    "Only send me a periodic notification email that summarises the week",
                    "Send me an email for every notification including quiet hours",
                ),
            selectedIndex = 1,
            onSelect = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "bead dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AppBeadDarkPreview() {
    BeadPreviewSurface(darkTheme = true) {
        AppBead(
            options = PreviewOptions,
            selectedIndex = 1,
            onSelect = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "bead large-font", fontScale = 2f)
@Composable
internal fun AppBeadLargeFontPreview() {
    BeadPreviewSurface {
        AppBead(
            options = PreviewOptions,
            selectedIndex = 1,
            onSelect = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "bead rtl")
@Composable
internal fun AppBeadRtlPreview() {
    BeadPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppBead(
                options = PreviewOptions,
                selectedIndex = 1,
                onSelect = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
