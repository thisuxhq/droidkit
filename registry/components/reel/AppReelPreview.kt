package com.droidkit.registry.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.droidkit.registry.theme.AppTheme

private val PreviewMoments =
    listOf(
        ReelMoment(id = "arrive", label = "Arrive", icon = Icons.Filled.Place),
        ReelMoment(id = "stay", label = "Stay", icon = Icons.Filled.Home),
        ReelMoment(id = "taste", label = "Taste", icon = Icons.Filled.Favorite),
        ReelMoment(id = "look", label = "Look", icon = Icons.Filled.Star),
        ReelMoment(id = "meet", label = "Meet", icon = Icons.Filled.Person),
    )

private val PreviewBodies =
    listOf(
        "Step off the train and find the square.",
        "Drop the bags and open the windows.",
        "Order the thing the room is talking about.",
        "Walk until the street gets quiet.",
        "Sit with the person who knows the city.",
    )

@Composable
private fun ReelPreviewSurface(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    AppTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Box(modifier = Modifier.fillMaxSize().padding(AppTheme.spacing.lg)) {
                content()
            }
        }
    }
}

@Composable
private fun ReelPreviewPane(index: Int) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(AppTheme.spacing.md),
    ) {
        Text(
            text = PreviewMoments[index].label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = PreviewBodies[index],
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true, name = "reel default", widthDp = 360, heightDp = 280)
@Composable
internal fun AppReelDefaultPreview() {
    ReelPreviewSurface {
        AppReel(
            moments = PreviewMoments,
            selectedIndex = 2,
            onSelect = {},
            modifier = Modifier.fillMaxSize(),
        ) { index ->
            ReelPreviewPane(index)
        }
    }
}

@Preview(showBackground = true, name = "reel first", widthDp = 360, heightDp = 280)
@Composable
internal fun AppReelFirstPreview() {
    ReelPreviewSurface {
        AppReel(
            moments = PreviewMoments,
            selectedIndex = 0,
            onSelect = {},
            modifier = Modifier.fillMaxSize(),
        ) { index ->
            ReelPreviewPane(index)
        }
    }
}

@Preview(showBackground = true, name = "reel last", widthDp = 360, heightDp = 280)
@Composable
internal fun AppReelLastPreview() {
    ReelPreviewSurface {
        AppReel(
            moments = PreviewMoments,
            selectedIndex = PreviewMoments.lastIndex,
            onSelect = {},
            modifier = Modifier.fillMaxSize(),
        ) { index ->
            ReelPreviewPane(index)
        }
    }
}

@Preview(showBackground = true, name = "reel disabled", widthDp = 360, heightDp = 280)
@Composable
internal fun AppReelDisabledPreview() {
    ReelPreviewSurface {
        AppReel(
            moments = PreviewMoments,
            selectedIndex = 2,
            onSelect = {},
            modifier = Modifier.fillMaxSize(),
            enabled = false,
        ) { index ->
            ReelPreviewPane(index)
        }
    }
}

@Preview(showBackground = true, name = "reel rtl", widthDp = 360, heightDp = 280)
@Composable
internal fun AppReelRtlPreview() {
    ReelPreviewSurface {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            AppReel(
                moments = PreviewMoments,
                selectedIndex = 2,
                onSelect = {},
                modifier = Modifier.fillMaxSize(),
            ) { index ->
                ReelPreviewPane(index)
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "reel dark",
    widthDp = 360,
    heightDp = 280,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
internal fun AppReelDarkPreview() {
    ReelPreviewSurface(darkTheme = true) {
        AppReel(
            moments = PreviewMoments,
            selectedIndex = 2,
            onSelect = {},
            modifier = Modifier.fillMaxSize(),
        ) { index ->
            ReelPreviewPane(index)
        }
    }
}

@Preview(showBackground = true, name = "reel large-font", widthDp = 360, heightDp = 360, fontScale = 2f)
@Composable
internal fun AppReelLargeFontPreview() {
    ReelPreviewSurface {
        AppReel(
            moments = PreviewMoments,
            selectedIndex = 2,
            onSelect = {},
            modifier = Modifier.fillMaxSize(),
        ) { index ->
            ReelPreviewPane(index)
        }
    }
}
