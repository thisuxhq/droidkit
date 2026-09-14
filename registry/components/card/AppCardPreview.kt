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
private fun CardPreviewSurface(
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

@Preview(showBackground = true, name = "card default")
@Composable
internal fun CardDefaultPreview() {
    CardPreviewSurface {
        AppCard(title = "Weekly report", supporting = "Ready to review before Friday.")
    }
}

@Preview(showBackground = true, name = "card clickable")
@Composable
internal fun CardClickablePreview() {
    CardPreviewSurface {
        AppCard(title = "Weekly report", supporting = "Ready to review before Friday.", onClick = {})
    }
}

@Preview(showBackground = true, name = "card with-action")
@Composable
internal fun CardWithActionPreview() {
    CardPreviewSurface {
        AppCard(title = "Weekly report", supporting = "Ready to review before Friday.", action = "Review", onAction = {})
    }
}

@Preview(showBackground = true, name = "card long-text", widthDp = 320)
@Composable
internal fun CardLongTextPreview() {
    CardPreviewSurface {
        AppCard(
            title = "Weekly report for the north-east accounts team",
            supporting = "Twelve invoices still need a second look before Friday afternoon close.",
        )
    }
}

@Preview(showBackground = true, name = "card rtl")
@Composable
internal fun CardRtlPreview() {
    CardPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppCard(title = "Weekly report", supporting = "Ready to review before Friday.")
        }
    }
}

@Preview(showBackground = true, name = "card dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun CardDarkPreview() {
    CardPreviewSurface(darkTheme = true) {
        AppCard(title = "Weekly report", supporting = "Ready to review before Friday.")
    }
}

@Preview(showBackground = true, name = "card large-font", fontScale = 2f)
@Composable
internal fun CardLargeFontPreview() {
    CardPreviewSurface {
        AppCard(title = "Weekly report", supporting = "Ready to review before Friday.")
    }
}
