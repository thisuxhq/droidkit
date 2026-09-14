package com.droidkit.registry.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.rememberReducedMotion

private val SkeletonHeight = 16.dp
private val SkeletonCorner = 8.dp
private const val SkeletonPulseMillis = 900
private const val InspectionAlpha = 0.55f

/**
 * A placeholder that breathes only while it is on screen.
 *
 * see:      a quiet bar in the space content will fill.
 * act:      the pulse stops when the item leaves the composition.
 * leave:    reduced motion is a static bar; TalkBack skips it.
 */
@Composable
fun AppSkeleton(
    modifier: Modifier = Modifier,
    height: Dp = SkeletonHeight,
) {
    val reducedMotion = rememberReducedMotion()
    val inspection = LocalInspectionMode.current
    var visible by remember { mutableStateOf(true) }
    DisposableEffect(Unit) {
        visible = true
        onDispose { visible = false }
    }

    val transition = rememberInfiniteTransition(label = "skeleton-pulse")
    val pulsed by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = SkeletonPulseMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "skeleton-alpha",
    )
    val alpha =
        when {
            inspection -> InspectionAlpha
            reducedMotion || !visible -> 1f
            else -> pulsed
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(SkeletonCorner))
                .graphicsLayer { this.alpha = alpha }
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .semantics { invisibleToUser() },
    )
}
