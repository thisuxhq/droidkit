package com.droidkit.registry.foundation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.droidkit.registry.theme.AppTheme

// Motion cannot be shown in a still image. The board shows the resting and pressed sizes side
// by side and names the helpers, so the golden proves the item renders and what it offers.

private const val PreviewPressedScale = 0.97f

@Composable
private fun FoundationPreviewSurface(
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
private fun FoundationPreviewBoard() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
    ) {
        Text(
            text = "Foundation",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Haptics: click, tick, confirm, reject. Motion: press scale, shake, spinner timing, reduced motion.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FoundationSwatch(scale = 1f, label = "Resting")
            FoundationSwatch(scale = PreviewPressedScale, label = "Pressed")
        }
    }
}

@Composable
private fun FoundationSwatch(
    scale: Float,
    label: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        Box(
            modifier =
                Modifier
                    .size(AppTheme.spacing.xl + AppTheme.spacing.lg)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primary),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true, name = "foundation default")
@Composable
fun AppFoundationDefaultPreview() {
    FoundationPreviewSurface {
        FoundationPreviewBoard()
    }
}

@Preview(showBackground = true, name = "foundation dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppFoundationDarkPreview() {
    FoundationPreviewSurface(darkTheme = true) {
        FoundationPreviewBoard()
    }
}
