package com.droidkit.registry.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.droidkit.registry.theme.AppTheme
import kotlin.math.roundToInt

/**
 * Labeled progress. Never a bar with no name.
 *
 * see:      [label] sits above the bar; determinate [progress] 0–1 announces a percent.
 * see:      null [progress] is indeterminate and reads Loading.
 */
@Composable
fun AppProgress(
    label: String,
    modifier: Modifier = Modifier,
    progress: Float? = null,
) {
    val spoken =
        if (progress == null) {
            "Loading"
        } else {
            "${(progress.coerceIn(0f, 1f) * 100f).roundToInt()} percent"
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    stateDescription = spoken
                    liveRegion = LiveRegionMode.Polite
                },
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (progress == null) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else {
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}
