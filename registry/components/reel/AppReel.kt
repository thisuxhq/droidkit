package com.droidkit.registry.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberReducedMotion
import com.droidkit.registry.theme.AppTheme
import kotlin.math.roundToInt

private val TrackBarHeight = 56.dp
private val TrackHitHeight = 48.dp
private val TrackLineHeight = 2.dp
private val MarkerHit = 48.dp
private val MarkerIconSize = 24.dp
private val MarkerPitch = 64.dp
private val PlayheadWidth = 2.dp
private val PlayheadHeight = 20.dp
private val PlayheadCap = 8.dp
private val PaneMinHeight = 120.dp
private const val RecedeAlpha = 0.38f
private const val TrackLineAlpha = 0.24f
private const val DisabledAlpha = 0.38f

data class ReelMoment(
    val id: String,
    val label: String,
    val icon: ImageVector,
)

/**
 * Center-locked reel of discrete moments. The playhead stays; the track and pane move to it.
 *
 * see:      playhead on the current moment; that icon is bright; the pane already shows it.
 * reach:    48 dp bar; a tap on a marker uses pressScale.
 * act:      snap + pane settle + tick on each index change.
 * mistake:  the ends are a wall; no reject.
 * leave:    selectedIndex is caller-owned.
 *
 * selectedIndex change → track and pager settle (tween AppTheme.motion.normal).
 * Reduced motion and inspection jump. Drag across a moment ticks and calls onSelect.
 */
@Composable
fun AppReel(
    moments: List<ReelMoment>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable (index: Int) -> Unit,
) {
    if (moments.isEmpty()) {
        Box(modifier)
        return
    }

    val index = selectedIndex.coerceIn(0, moments.lastIndex)
    val haptics = rememberAppHaptics()
    val reducedMotion = rememberReducedMotion()
    val inspection = LocalInspectionMode.current
    val layoutDirection = LocalLayoutDirection.current
    val instant = reducedMotion || inspection
    val current = moments[index]

    fun moveTo(next: Int) {
        val target = next.coerceIn(0, moments.lastIndex)
        if (target != index) {
            haptics.tick()
            onSelect(target)
        }
    }

    Column(
        modifier =
            modifier.graphicsLayer {
                alpha = if (enabled) 1f else DisabledAlpha
            },
    ) {
        ReelPane(
            moments = moments,
            index = index,
            label = current.label,
            enabled = enabled,
            instant = instant,
            onSettled = ::moveTo,
            content = content,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
                    .heightIn(min = PaneMinHeight),
        )
        ReelTrack(
            moments = moments,
            selectedIndex = index,
            enabled = enabled,
            instant = instant,
            layoutDirection = layoutDirection,
            onSelect = ::moveTo,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.lg)
                    .padding(bottom = AppTheme.spacing.md),
        )
    }
}

@Composable
private fun ReelPane(
    moments: List<ReelMoment>,
    index: Int,
    label: String,
    enabled: Boolean,
    instant: Boolean,
    onSettled: (Int) -> Unit,
    content: @Composable (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState =
        rememberPagerState(initialPage = index) { moments.size }

    LaunchedEffect(index, instant) {
        if (pagerState.currentPage != index) {
            if (instant) {
                pagerState.scrollToPage(index)
            } else {
                pagerState.animateScrollToPage(index)
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .collect { page ->
                if (page != index) onSettled(page)
            }
    }

    Box(
        modifier =
            modifier.semantics {
                liveRegion = LiveRegionMode.Polite
                stateDescription = label
            },
    ) {
        if (instant) {
            Box(modifier = Modifier.fillMaxSize()) {
                content(index)
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = enabled,
            ) { page ->
                content(page)
            }
        }
    }
}

@Composable
private fun ReelTrack(
    moments: List<ReelMoment>,
    selectedIndex: Int,
    enabled: Boolean,
    instant: Boolean,
    layoutDirection: LayoutDirection,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val pitchPx = with(density) { MarkerPitch.toPx() }
    val markerHitPx = with(density) { MarkerHit.toPx() }
    val last = moments.lastIndex
    val rtl = layoutDirection == LayoutDirection.Rtl

    var dragging by remember { mutableStateOf(false) }
    var dragPx by remember { mutableFloatStateOf(0f) }

    val visualSelected = if (rtl) last - selectedIndex else selectedIndex
    val settledShift by animateFloatAsState(
        targetValue = visualSelected * pitchPx,
        animationSpec =
            if (instant || dragging) {
                snap()
            } else {
                tween(AppTheme.motion.normal)
            },
        label = "reel-track",
    )

    val colors = MaterialTheme.colorScheme
    val lineColor = colors.onInverseSurface.copy(alpha = TrackLineAlpha)
    val playheadColor = colors.onInverseSurface

    fun liveIndex(drag: Float): Int {
        val visualDelta = if (rtl) drag else -drag
        return (selectedIndex + (visualDelta / pitchPx)).roundToInt().coerceIn(0, last)
    }

    val dragState =
        rememberDraggableState { delta ->
            if (!enabled) return@rememberDraggableState
            val nextDrag = dragPx + delta
            val unclamped = if (rtl) selectedIndex + nextDrag / pitchPx else selectedIndex - nextDrag / pitchPx
            val clamped = unclamped.coerceIn(0f, last.toFloat())
            dragPx =
                if (rtl) {
                    (clamped - selectedIndex) * pitchPx
                } else {
                    (selectedIndex - clamped) * pitchPx
                }
            val live = liveIndex(dragPx)
            if (live != selectedIndex) {
                val consumed = (live - selectedIndex) * pitchPx
                dragPx -= if (rtl) consumed else -consumed
                onSelect(live)
            }
        }

    Box(
        modifier =
            modifier
                .height(TrackBarHeight)
                .clip(CircleShape)
                .background(colors.inverseSurface)
                .selectableGroup()
                .focusable(enabled = enabled)
                .onKeyEvent { event ->
                    if (!enabled || event.type != KeyEventType.KeyDown) return@onKeyEvent false
                    when (event.key) {
                        Key.DirectionRight -> {
                            onSelect(if (rtl) selectedIndex - 1 else selectedIndex + 1)
                            true
                        }
                        Key.DirectionLeft -> {
                            onSelect(if (rtl) selectedIndex + 1 else selectedIndex - 1)
                            true
                        }
                        else -> false
                    }
                }
                .draggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    enabled = enabled,
                    onDragStarted = { dragging = true },
                    onDragStopped = {
                        val live = liveIndex(dragPx)
                        dragPx = 0f
                        dragging = false
                        if (live != selectedIndex) onSelect(live)
                    },
                )
                .drawBehind {
                    val lineHeight = TrackLineHeight.toPx()
                    val playheadW = PlayheadWidth.toPx()
                    val playheadH = PlayheadHeight.toPx()
                    val cap = PlayheadCap.toPx()
                    val centerX = size.width / 2f
                    val centerY = size.height / 2f
                    drawRoundRect(
                        color = lineColor,
                        topLeft = Offset(0f, centerY - lineHeight / 2f),
                        size = Size(size.width, lineHeight),
                        cornerRadius = CornerRadius(lineHeight / 2f),
                    )
                    drawRoundRect(
                        color = playheadColor,
                        topLeft = Offset(centerX - playheadW / 2f, centerY - playheadH / 2f),
                        size = Size(playheadW, playheadH),
                        cornerRadius = CornerRadius(playheadW / 2f),
                    )
                    drawCircle(
                        color = playheadColor,
                        radius = cap / 2f,
                        center = Offset(centerX, centerY),
                    )
                },
        contentAlignment = Alignment.Center,
    ) {
        var viewportWidthPx by remember { mutableIntStateOf(0) }
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(TrackHitHeight)
                    .clipToBounds()
                    .onSizeChanged { viewportWidthPx = it.width },
        ) {
            val displayOrder = if (rtl) moments.indices.reversed() else moments.indices
            Row(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .offset {
                            val x = viewportWidthPx / 2f - markerHitPx / 2f - settledShift + dragPx
                            IntOffset(x.roundToInt(), 0)
                        },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                displayOrder.forEach { logical ->
                    ReelMarker(
                        moment = moments[logical],
                        selected = logical == selectedIndex,
                        enabled = enabled,
                        onSelect = { onSelect(logical) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ReelMarker(
    moment: ReelMoment,
    selected: Boolean,
    enabled: Boolean,
    onSelect: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier =
            Modifier
                .size(MarkerHit)
                .pressScale(interactionSource = interactionSource, enabled = enabled)
                .selectable(
                    selected = selected,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    enabled = enabled,
                    role = Role.Tab,
                    onClick = onSelect,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = moment.icon,
            contentDescription = moment.label,
            modifier = Modifier.size(MarkerIconSize),
            tint =
                MaterialTheme.colorScheme.onInverseSurface.copy(
                    alpha = if (selected) 1f else RecedeAlpha,
                ),
        )
    }
}
