package com.droidkit.registry.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
private fun ThemePreviewSurface(
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
private fun ThemePreviewBoard() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
    ) {
        Text(
            text = "AppTheme",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Color, type, and shape come from MaterialTheme. Spacing comes from AppTheme.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
            ThemeSwatch(color = MaterialTheme.colorScheme.primary)
            ThemeSwatch(color = MaterialTheme.colorScheme.surface)
            ThemeSwatch(color = MaterialTheme.colorScheme.error)
        }
        Button(onClick = {}) {
            Text("Continue")
        }
    }
}

@Composable
private fun ThemeSwatch(color: Color) {
    Box(
        modifier =
            Modifier
                .size(AppTheme.spacing.xl)
                .clip(MaterialTheme.shapes.small)
                .background(color),
    )
}

@Preview(showBackground = true, name = "theme default")
@Composable
fun AppThemeDefaultPreview() {
    ThemePreviewSurface {
        ThemePreviewBoard()
    }
}

@Preview(showBackground = true, name = "theme dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppThemeDarkPreview() {
    ThemePreviewSurface(darkTheme = true) {
        ThemePreviewBoard()
    }
}

@Preview(showBackground = true, name = "theme large-font", fontScale = 2f)
@Composable
fun AppThemeLargeFontPreview() {
    ThemePreviewSurface {
        ThemePreviewBoard()
    }
}
