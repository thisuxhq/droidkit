package com.droidkit.registry.foundation

import android.animation.ValueAnimator
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

private const val PressedScale = 0.97f
private val ShakeAmplitude = 8.dp
private const val ShakeDurationMillis = 300
private const val ShakeCycles = 3
private const val LoadingShowAfterMillis = 150L
private const val LoadingMinVisibleMillis = 500L

/**
 * True when the user has turned animations off (Developer options animator scale 0, or the
 * "Remove animations" accessibility setting, which sets the same scale). Compose animations
 * do not read this on their own, so items ask here and skip motion. Haptics and end states
 * are unaffected. Read once per composition of the caller; previews always animate.
 */
@Composable
fun rememberReducedMotion(): Boolean {
    if (LocalInspectionMode.current) return false
    val context = LocalContext.current
    return remember(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            !ValueAnimator.areAnimatorsEnabled()
        } else {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f,
            ) == 0f
        }
    }
}

/**
 * The press. Scales the control to 0.97 while [interactionSource] reports pressed and back
 * on release over [AppTheme.motion.quick]. Under reduced motion the scale stays at 1.
 *
 * Pass the same interactionSource to the Material component so the ripple and the scale agree.
 */
@Composable
fun Modifier.pressScale(
    interactionSource: InteractionSource,
    enabled: Boolean = true,
): Modifier {
    val reducedMotion = rememberReducedMotion()
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled && !reducedMotion) PressedScale else 1f,
        animationSpec = tween(durationMillis = AppTheme.motion.quick),
        label = "press-scale",
    )
    return graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/**
 * The refusal. Each time [trigger] goes from false to true the content shakes horizontally
 * once (three cycles, 300 ms, 8 dp) and settles. It does not repeat while [trigger] stays true;
 * flip it to shake again. Under reduced motion nothing moves — pair it with `AppHaptics.reject()`
 * and error text so the moment still exists.
 */
@Composable
fun Modifier.shake(trigger: Boolean): Modifier {
    val reducedMotion = rememberReducedMotion()
    // 0 = at rest, 1 = finished. A decaying sine: three swings that get smaller and land on 0.
    var progress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(trigger) {
        if (!trigger || reducedMotion) return@LaunchedEffect
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(durationMillis = ShakeDurationMillis, easing = LinearEasing),
        ) { value, _ -> progress = value }
        progress = 0f
    }
    // Placement-only offset: no re-measure of the content, and the movement is real layout
    // position (visible to semantics bounds and tests), unlike a graphicsLayer translation.
    return offset {
        val x =
            if (progress <= 0f || progress >= 1f) {
                0f
            } else {
                sin(progress * ShakeCycles * 2f * PI.toFloat()) * (1f - progress) * ShakeAmplitude.toPx()
            }
        IntOffset(x.roundToInt(), 0)
    }
}

/**
 * When a spinner should actually be on screen. [loading] flips as fast as the network does;
 * the returned value waits 150 ms before showing (a fast call never flashes a spinner) and,
 * once shown, stays at least 500 ms (a spinner never blinks). Previews and screenshot tests
 * get [loading] back unchanged so the loading state can be captured.
 */
@Composable
fun rememberLoadingVisibility(loading: Boolean): Boolean {
    if (LocalInspectionMode.current) return loading
    var visible by remember { mutableStateOf(false) }
    var shownAt by remember { mutableLongStateOf(0L) }
    LaunchedEffect(loading) {
        if (loading) {
            if (!visible) {
                delay(LoadingShowAfterMillis)
                visible = true
                shownAt = withFrameMillis { it }
            }
        } else if (visible) {
            val elapsed = withFrameMillis { it } - shownAt
            if (elapsed < LoadingMinVisibleMillis) delay(LoadingMinVisibleMillis - elapsed)
            visible = false
        }
    }
    return visible
}
