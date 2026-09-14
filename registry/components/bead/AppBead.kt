package com.droidkit.registry.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.droidkit.registry.theme.AppTheme
import kotlin.math.roundToInt

private val TrackWidth = 28.dp
private val BeadSize = 20.dp
private val TrackEndInset = 6.dp
private val RowMinHeight = 56.dp
private const val TrackAlpha = 0.16f
private const val BeadAlpha = 0.64f
private const val DisabledAlpha = 0.38f

private const val SlotTrack = "track"
private const val SlotOption = "option"

// select → select: the bead travels the track (tween AppTheme.motion.normal) and
// retargets if the user taps mid-motion. Animator scale 0 snaps (Compose clock).
// selectedIndex is caller-owned. Disabled rows do not fire onSelect.

@Composable
fun AppBead(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    if (options.isEmpty()) {
        Box(modifier)
        return
    }

    val index = selectedIndex.coerceIn(0, options.lastIndex)
    val gapPx = with(LocalDensity.current) { AppTheme.spacing.md.roundToPx() }
    val trackWidthPx = with(LocalDensity.current) { TrackWidth.roundToPx() }
    val motion = AppTheme.motion.normal

    SubcomposeLayout(
        modifier =
            modifier
                .selectableGroup()
                .graphicsLayer { alpha = if (enabled) 1f else DisabledAlpha },
    ) { constraints ->
        val bounded = constraints.maxWidth != Constraints.Infinity
        val optionMaxWidth =
            if (bounded) {
                (constraints.maxWidth - trackWidthPx - gapPx).coerceAtLeast(0)
            } else {
                Constraints.Infinity
            }
        val optionMinWidth = if (bounded) optionMaxWidth else 0
        val optionConstraints =
            Constraints(
                minWidth = optionMinWidth,
                maxWidth = optionMaxWidth,
                minHeight = 0,
                maxHeight = Constraints.Infinity,
            )

        val optionPlaceables =
            options.mapIndexed { optionIndex, option ->
                subcompose("$SlotOption-$optionIndex") {
                    BeadOption(
                        text = option,
                        selected = optionIndex == index,
                        enabled = enabled,
                        onSelect = { onSelect(optionIndex) },
                    )
                }.first().measure(optionConstraints)
            }

        val contentHeight = optionPlaceables.sumOf { it.height }
        val selectedTop = optionPlaceables.take(index).sumOf { it.height }
        val beadCenterPx = selectedTop + optionPlaceables[index].height / 2f

        val trackPlaceable =
            subcompose(SlotTrack) {
                BeadTrack(
                    beadCenterPx = beadCenterPx,
                    trackHeightPx = contentHeight,
                    motionMillis = motion,
                )
            }.first().measure(
                Constraints.fixed(trackWidthPx, contentHeight),
            )

        val optionsWidth = optionPlaceables.maxOf { it.width }
        val width =
            if (bounded) {
                constraints.maxWidth
            } else {
                trackWidthPx + gapPx + optionsWidth
            }
        val rtl = layoutDirection == LayoutDirection.Rtl
        val trackX = if (rtl) width - trackWidthPx else 0
        val optionX = if (rtl) 0 else trackWidthPx + gapPx

        layout(width, contentHeight) {
            trackPlaceable.place(trackX, 0)
            var optionY = 0
            optionPlaceables.forEach { placeable ->
                placeable.place(optionX, optionY)
                optionY += placeable.height
            }
        }
    }
}

@Composable
private fun BeadOption(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = RowMinHeight)
                .selectable(
                    selected = selected,
                    enabled = enabled,
                    role = Role.RadioButton,
                    onClick = onSelect,
                )
                .padding(vertical = AppTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            color =
                if (selected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
        )
    }
}

@Composable
private fun BeadTrack(
    beadCenterPx: Float,
    trackHeightPx: Int,
    motionMillis: Int,
) {
    val beadSizePx = with(LocalDensity.current) { BeadSize.toPx() }
    val insetPx = with(LocalDensity.current) { TrackEndInset.toPx() }
    val animatedCenterPx by animateFloatAsState(
        targetValue = beadCenterPx,
        animationSpec = tween(motionMillis),
        label = "bead",
    )
    val maxTop = (trackHeightPx - beadSizePx - insetPx).coerceAtLeast(insetPx)
    val beadTop = (animatedCenterPx - beadSizePx / 2f).coerceIn(insetPx, maxTop)
    val trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = TrackAlpha)
    val beadColor = MaterialTheme.colorScheme.onSurface.copy(alpha = BeadAlpha)

    Box(
        modifier =
            Modifier
                .clip(CircleShape)
                .background(trackColor),
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .offset { IntOffset(x = 0, y = beadTop.roundToInt()) }
                    .size(BeadSize)
                    .clip(CircleShape)
                    .background(beadColor),
        )
    }
}
