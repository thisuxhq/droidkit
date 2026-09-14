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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon

// One preview per declared state in registry.json. The name must be "<item> <state>";
// :registry:lintRegistryStates enforces it and the screenshot test renders each one.

@Composable
private fun BadgePreviewSurface(
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

@Preview(showBackground = true, name = "badge default")
@Composable
internal fun BadgeDefaultPreview() {
    BadgePreviewSurface {
        AppBadge(count = 3) {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
        }
    }
}

@Preview(showBackground = true, name = "badge capped")
@Composable
internal fun BadgeCappedPreview() {
    BadgePreviewSurface {
        AppBadge(count = 140) {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
        }
    }
}

@Preview(showBackground = true, name = "badge dot")
@Composable
internal fun BadgeDotPreview() {
    BadgePreviewSurface {
        AppBadge {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
        }
    }
}

@Preview(showBackground = true, name = "badge hidden")
@Composable
internal fun BadgeHiddenPreview() {
    BadgePreviewSurface {
        AppBadge(count = 0) {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
        }
    }
}

@Preview(showBackground = true, name = "badge rtl")
@Composable
internal fun BadgeRtlPreview() {
    BadgePreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        AppBadge(count = 3) {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
        }
        }
    }
}

@Preview(showBackground = true, name = "badge dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun BadgeDarkPreview() {
    BadgePreviewSurface(darkTheme = true) {
        AppBadge(count = 3) {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
        }
    }
}

@Preview(showBackground = true, name = "badge large-font", fontScale = 2f)
@Composable
internal fun BadgeLargeFontPreview() {
    BadgePreviewSurface {
        AppBadge(count = 3) {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
        }
    }
}
