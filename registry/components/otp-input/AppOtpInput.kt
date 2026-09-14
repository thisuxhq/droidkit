package com.droidkit.registry.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberReducedMotion
import com.droidkit.registry.foundation.shake
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.delay

private val OtpBoxMinSize = 48.sp
private val OtpBoxCorner = 12.dp
private val OtpBoxStroke = 1.dp
private val OtpActiveStroke = 2.dp
private val OtpCaretWidth = 2.dp
private const val DefaultErrorText = "Invalid code"
private const val CompleteDelayMillis = 150L
private const val CaretBlinkMillis = 1_000
private const val DigitPopScale = 0.8f

/** Material 3 disabled content opacity (`md.sys.state.disabled.content.opacity`). */
private const val DisabledContentAlpha = 0.38f

/**
 * SMS or email one-time code. Digits only, fixed length, one field — not six TextFields.
 *
 * act:      each digit pops into its cell (0.8 → 1) with a tick; the active cell has a caret.
 * succeed:  when the last digit lands, [onComplete] fires 150 ms later with a confirm haptic —
 *           long enough to see the full code, short enough to feel automatic.
 * mistake:  when [isError] turns on, the row shakes once, fires reject, keeps the wrong digits
 *           visible in red, and takes focus back. The next digit typed replaces the whole code.
 * paste:    keep digits only, cap at [length].
 * IME Done: [onDone] if the caller passed one (for a code that is not auto-submitted).
 */
@Composable
fun AppOtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onComplete: ((String) -> Unit)? = null,
    onDone: (() -> Unit)? = null,
    length: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false,
    label: String = "Code",
    supportingText: String? = null,
) {
    val digits = value.filter(Char::isDigit).take(length)
    val inspection = LocalInspectionMode.current
    val reducedMotion = rememberReducedMotion()
    val haptics = rememberAppHaptics()
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }
    val latestOnComplete by rememberUpdatedState(onComplete)

    // The wrong code stays on screen so the message makes sense; the next digit starts over.
    var replaceOnNextInput by rememberSaveable { mutableStateOf(false) }
    var errorFelt by rememberSaveable { mutableStateOf(false) }
    var shakeTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(isError) {
        if (isError) {
            replaceOnNextInput = true
            if (!errorFelt) {
                errorFelt = true
                shakeTrigger = true
                haptics.reject()
                if (enabled) focusRequester.requestFocus()
            }
        } else {
            errorFelt = false
            shakeTrigger = false
        }
    }

    var previousCount by remember { mutableStateOf(digits.length) }
    LaunchedEffect(digits) {
        val landed = digits.length > previousCount
        previousCount = digits.length
        if (!landed) return@LaunchedEffect
        if (digits.length == length) {
            if (!inspection) delay(CompleteDelayMillis)
            haptics.confirm()
            latestOnComplete?.invoke(digits)
        } else {
            haptics.tick()
        }
    }

    val disabledContentColor =
        MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledContentAlpha)
    val digitColor =
        if (enabled) MaterialTheme.colorScheme.onBackground else disabledContentColor
    val digitStyle = MaterialTheme.typography.titleLarge
    val boxSize =
        with(LocalDensity.current) {
            max(
                OtpBoxMinSize.toDp(),
                digitStyle.fontSize.toDp() + AppTheme.spacing.md,
            )
        }
    val visibleSupporting =
        if (isError) supportingText ?: DefaultErrorText else supportingText
    val labelColor =
        when {
            !enabled -> disabledContentColor
            isError -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
    val activeIndex = if ((focused || inspection) && digits.length < length) digits.length else -1

    Column(modifier = modifier) {
        BasicTextField(
            value = digits,
            onValueChange = { incoming ->
                if (!enabled) return@BasicTextField
                val clean = incoming.filter(Char::isDigit)
                val next =
                    if (replaceOnNextInput && clean.length > digits.length) {
                        insertedDigits(before = digits, after = clean)
                    } else {
                        clean
                    }
                replaceOnNextInput = false
                onValueChange(next.take(length))
            },
            modifier =
                Modifier
                    .focusRequester(focusRequester)
                    .semantics {
                        contentDescription = label
                        contentType = ContentType.SmsOtpCode
                        if (!enabled) {
                            stateDescription = "Disabled"
                        }
                        if (isError) {
                            error(visibleSupporting ?: DefaultErrorText)
                        }
                    },
            enabled = enabled,
            singleLine = true,
            interactionSource = interactionSource,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = { onDone?.invoke() },
                ),
            decorationBox = {
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = labelColor,
                        modifier =
                            Modifier
                                .padding(bottom = AppTheme.spacing.xs)
                                .semantics { hideFromAccessibility() },
                    )
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                            modifier = Modifier.shake(shakeTrigger),
                        ) {
                            repeat(length) { index ->
                                OtpCell(
                                    char = digits.getOrNull(index)?.toString().orEmpty(),
                                    active = index == activeIndex && enabled && !isError,
                                    stroke =
                                        otpStrokeColor(
                                            active = index == activeIndex,
                                            enabled = enabled,
                                            isError = isError,
                                            disabledContentColor = disabledContentColor,
                                        ),
                                    strokeWidth = if (index == activeIndex && enabled) OtpActiveStroke else OtpBoxStroke,
                                    boxSize = boxSize,
                                    digitColor = digitColor,
                                    digitStyle = digitStyle,
                                    reducedMotion = reducedMotion,
                                )
                            }
                        }
                    }
                }
            },
        )
        if (visibleSupporting != null) {
            Text(
                text = visibleSupporting,
                style = MaterialTheme.typography.bodyMedium,
                color =
                    if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.spacing.xs),
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
private fun OtpCell(
    char: String,
    active: Boolean,
    stroke: Color,
    strokeWidth: Dp,
    boxSize: Dp,
    digitColor: Color,
    digitStyle: TextStyle,
    reducedMotion: Boolean,
) {
    Box(
        modifier =
            Modifier
                .sizeIn(minWidth = boxSize, minHeight = boxSize)
                .border(
                    width = strokeWidth,
                    color = stroke,
                    shape = RoundedCornerShape(OtpBoxCorner),
                ),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = char.isNotEmpty(),
            enter =
                if (reducedMotion) {
                    EnterTransition.None
                } else {
                    scaleIn(initialScale = DigitPopScale, animationSpec = tween(AppTheme.motion.quick)) +
                        fadeIn(animationSpec = tween(AppTheme.motion.quick))
                },
            exit = ExitTransition.None,
        ) {
            BasicText(
                text = char,
                modifier = Modifier.clearAndSetSemantics { },
                style =
                    digitStyle.copy(
                        color = digitColor,
                        textAlign = TextAlign.Center,
                    ),
            )
        }
        if (active && char.isEmpty()) {
            OtpCaret(
                height = with(LocalDensity.current) { digitStyle.fontSize.toDp() },
                color = MaterialTheme.colorScheme.primary,
                reducedMotion = reducedMotion,
            )
        }
    }
}

@Composable
private fun OtpCaret(
    height: Dp,
    color: Color,
    reducedMotion: Boolean,
) {
    val alpha =
        if (reducedMotion || LocalInspectionMode.current) {
            1f
        } else {
            val transition = rememberInfiniteTransition(label = "otp-caret")
            transition.animateFloat(
                initialValue = 1f,
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            keyframes {
                                durationMillis = CaretBlinkMillis
                                1f at 0
                                1f at CaretBlinkMillis / 2 - 1
                                0f at CaretBlinkMillis / 2
                                0f at CaretBlinkMillis - 1
                            },
                        repeatMode = RepeatMode.Restart,
                    ),
                label = "otp-caret-alpha",
            ).value
        }
    Box(
        modifier =
            Modifier
                .width(OtpCaretWidth)
                .height(height)
                .alpha(alpha)
                .background(color)
                .clearAndSetSemantics { },
    )
}

/** The characters typed into [before] to get [after], wherever the cursor was. */
private fun insertedDigits(
    before: String,
    after: String,
): String {
    val start = before.commonPrefixWith(after).length
    return after.substring(start, start + (after.length - before.length))
}

@Composable
private fun otpStrokeColor(
    active: Boolean,
    enabled: Boolean,
    isError: Boolean,
    disabledContentColor: Color,
): Color =
    when {
        !enabled -> disabledContentColor
        isError -> MaterialTheme.colorScheme.error
        active -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
