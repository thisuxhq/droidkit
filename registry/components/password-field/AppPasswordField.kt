package com.droidkit.registry.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberReducedMotion
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.delay

private val PasswordToggleSize = 48.dp
private val PasswordIconSize = 24.dp
private val RuleMarkSize = 18.dp
private val RuleMarkStroke = 1.5.dp
private val StrengthBarHeight = 4.dp
private const val RevealTimeoutMillis = 10_000L
private const val StrengthSegments = 3
private const val CapsLockHint = "Caps lock is on"

/**
 * One requirement a new password must meet, shown under the field and ticked live.
 * Labels are plain strings so they can come from resources.
 */
data class PasswordRule(
    val label: String,
    val isMet: (String) -> Boolean,
) {
    companion object {
        fun minLength(
            length: Int,
            label: String = "At least $length characters",
        ) = PasswordRule(label) { it.length >= length }

        fun number(label: String = "A number") = PasswordRule(label) { value -> value.any(Char::isDigit) }

        fun upperCase(label: String = "An uppercase letter") = PasswordRule(label) { value -> value.any(Char::isUpperCase) }

        fun lowerCase(label: String = "A lowercase letter") = PasswordRule(label) { value -> value.any(Char::isLowerCase) }

        fun symbol(label: String = "A symbol") = PasswordRule(label) { value -> value.any { !it.isLetterOrDigit() && !it.isWhitespace() } }
    }
}

// Material Visibility / VisibilityOff paths, inlined so the copied file does not
// need material-icons-extended (icons-core does not ship these).
private val Visibility: ImageVector =
    ImageVector.Builder(
        name = "Filled.Visibility",
        defaultWidth = PasswordIconSize,
        defaultHeight = PasswordIconSize,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.0f, 4.5f)
            curveTo(7.0f, 4.5f, 2.73f, 7.61f, 1.0f, 12.0f)
            curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
            reflectiveCurveToRelative(9.27f, -3.11f, 11.0f, -7.5f)
            curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
            close()
            moveTo(12.0f, 17.0f)
            curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
            reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
            reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
            reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
            close()
            moveTo(12.0f, 9.0f)
            curveToRelative(-1.66f, 0.0f, -3.0f, 1.34f, -3.0f, 3.0f)
            reflectiveCurveToRelative(1.34f, 3.0f, 3.0f, 3.0f)
            reflectiveCurveToRelative(3.0f, -1.34f, 3.0f, -3.0f)
            reflectiveCurveToRelative(-1.34f, -3.0f, -3.0f, -3.0f)
            close()
        }
    }.build()

private val VisibilityOff: ImageVector =
    ImageVector.Builder(
        name = "Filled.VisibilityOff",
        defaultWidth = PasswordIconSize,
        defaultHeight = PasswordIconSize,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.0f, 7.0f)
            curveToRelative(2.76f, 0.0f, 5.0f, 2.24f, 5.0f, 5.0f)
            curveToRelative(0.0f, 0.65f, -0.13f, 1.26f, -0.36f, 1.83f)
            lineToRelative(2.92f, 2.92f)
            curveToRelative(1.51f, -1.26f, 2.7f, -2.89f, 3.43f, -4.75f)
            curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
            curveToRelative(-1.4f, 0.0f, -2.74f, 0.25f, -3.98f, 0.7f)
            lineToRelative(2.16f, 2.16f)
            curveTo(10.74f, 7.13f, 11.35f, 7.0f, 12.0f, 7.0f)
            close()
            moveTo(2.0f, 4.27f)
            lineToRelative(2.28f, 2.28f)
            lineToRelative(0.46f, 0.46f)
            curveTo(3.08f, 8.3f, 1.78f, 10.02f, 1.0f, 12.0f)
            curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
            curveToRelative(1.55f, 0.0f, 3.03f, -0.3f, 4.38f, -0.84f)
            lineToRelative(0.42f, 0.42f)
            lineTo(19.73f, 22.0f)
            lineTo(21.0f, 20.73f)
            lineTo(3.27f, 3.0f)
            lineTo(2.0f, 4.27f)
            close()
            moveTo(7.53f, 9.8f)
            lineToRelative(1.55f, 1.55f)
            curveToRelative(-0.05f, 0.21f, -0.08f, 0.43f, -0.08f, 0.65f)
            curveToRelative(0.0f, 1.66f, 1.34f, 3.0f, 3.0f, 3.0f)
            curveToRelative(0.22f, 0.0f, 0.44f, -0.03f, 0.65f, -0.08f)
            lineToRelative(1.55f, 1.55f)
            curveToRelative(-0.67f, 0.33f, -1.41f, 0.53f, -2.2f, 0.53f)
            curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
            curveToRelative(0.0f, -0.79f, 0.2f, -1.53f, 0.53f, -2.2f)
            close()
            moveTo(11.84f, 9.02f)
            lineToRelative(3.15f, 3.15f)
            lineToRelative(0.02f, -0.16f)
            curveToRelative(0.0f, -1.66f, -1.34f, -3.0f, -3.0f, -3.0f)
            lineToRelative(-0.17f, 0.01f)
            close()
        }
    }.build()

/**
 * Password field. Sign-in gets show/hide, autofill, and the IME chain; sign-up adds [rules]
 * that tick live under the field with a strength word, so nobody learns the rules from an error.
 *
 * reveal:   the eye crossfades; a reveal hides again on blur and after 10 s without typing.
 * rules:    each rule that flips to met ticks (haptic) and reads "Met" to TalkBack; all met → confirm.
 *           Strength is a word ("Weak", "Good", "Strong") plus segments, never colour alone.
 * paste:    a multi-character insert (paste, autofill) is trimmed of surrounding whitespace.
 * mistake / recover: inherited from [AppTextField] — shake once, reject haptic, quiet on first keystroke.
 * caps lock: with a hardware keyboard, the hint line says so while focused. Soft keyboards cannot report it.
 * rotation: visibility and the felt error survive; the reveal timer restarts.
 */
@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    rules: List<PasswordRule> = emptyList(),
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,
    contentType: ContentType = ContentType.Password,
    initialVisible: Boolean = false,
) {
    require(!isError || !supportingText.isNullOrBlank()) {
        "AppPasswordField: isError requires supportingText so the error is not color-only"
    }

    val inspection = LocalInspectionMode.current
    val reducedMotion = rememberReducedMotion()
    val haptics = rememberAppHaptics()
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    var visible by rememberSaveable { mutableStateOf(initialVisible) }
    var wasFocused by remember { mutableStateOf(focused) }
    LaunchedEffect(focused) {
        if (wasFocused && !focused) visible = false
        wasFocused = focused
    }
    LaunchedEffect(visible, value) {
        if (visible && !inspection) {
            delay(RevealTimeoutMillis)
            visible = false
        }
    }

    var capsLockOn by remember { mutableStateOf(false) }

    val metCount = rules.count { it.isMet(value) }
    var previousMet by remember { mutableIntStateOf(metCount) }
    LaunchedEffect(metCount) {
        if (metCount > previousMet) {
            if (metCount == rules.size) haptics.confirm() else haptics.tick()
        }
        previousMet = metCount
    }

    val performImeAction = {
        if (onImeAction != null) {
            onImeAction()
        } else {
            when (imeAction) {
                ImeAction.Next -> focusManager.moveFocus(FocusDirection.Next)
                ImeAction.Done, ImeAction.Go, ImeAction.Send, ImeAction.Search ->
                    focusManager.clearFocus()
                else -> Unit
            }
        }
    }

    Column(
        modifier =
            modifier.onPreviewKeyEvent { event ->
                capsLockOn = event.nativeKeyEvent.isCapsLockOn
                false
            },
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        AppTextField(
            value = value,
            onValueChange = { incoming ->
                val pasted = incoming.length > value.length + 1
                onValueChange(if (pasted) incoming.trim() else incoming)
            },
            label = label,
            supportingText =
                if (capsLockOn && focused && !isError) CapsLockHint else supportingText,
            isError = isError,
            enabled = enabled,
            visualTransformation =
                if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = imeAction,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = { performImeAction() },
                    onGo = { performImeAction() },
                    onNext = { performImeAction() },
                    onSend = { performImeAction() },
                    onSearch = { performImeAction() },
                ),
            contentType = contentType,
            trailingIcon = {
                IconButton(
                    onClick = { visible = !visible },
                    enabled = enabled,
                    modifier = Modifier.size(PasswordToggleSize),
                ) {
                    if (reducedMotion) {
                        VisibilityIcon(visible = visible)
                    } else {
                        Crossfade(
                            targetState = visible,
                            animationSpec = tween(durationMillis = AppTheme.motion.quick),
                            label = "password-visibility",
                        ) { shown ->
                            VisibilityIcon(visible = shown)
                        }
                    }
                }
            },
            interactionSource = interactionSource,
        )
        if (rules.isNotEmpty()) {
            if (value.isNotEmpty()) {
                PasswordStrength(metCount = metCount, total = rules.size, enabled = enabled)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
                modifier = Modifier.padding(horizontal = AppTheme.spacing.md),
            ) {
                rules.forEach { rule ->
                    PasswordRuleRow(
                        label = rule.label,
                        met = rule.isMet(value),
                        enabled = enabled,
                        reducedMotion = reducedMotion,
                    )
                }
            }
        }
    }
}

@Composable
private fun VisibilityIcon(visible: Boolean) {
    Icon(
        imageVector = if (visible) VisibilityOff else Visibility,
        contentDescription = if (visible) "Hide password" else "Show password",
    )
}

@Composable
private fun PasswordStrength(
    metCount: Int,
    total: Int,
    enabled: Boolean,
) {
    val level =
        when {
            metCount >= total -> StrengthSegments
            metCount * 2 >= total -> 2
            else -> 1
        }
    val word =
        when (level) {
            StrengthSegments -> "Strong password"
            2 -> "Good password"
            else -> "Weak password"
        }
    val ink = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.md)
                .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite },
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)) {
            repeat(StrengthSegments) { index ->
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(StrengthBarHeight)
                            .clip(CircleShape)
                            .background(if (index < level) ink else MaterialTheme.colorScheme.surfaceVariant),
                )
            }
        }
        Text(
            text = word,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PasswordRuleRow(
    label: String,
    met: Boolean,
    enabled: Boolean,
    reducedMotion: Boolean,
) {
    val textColor =
        when {
            !enabled -> MaterialTheme.colorScheme.onSurfaceVariant
            met -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
        modifier =
            Modifier.semantics(mergeDescendants = true) {
                stateDescription = if (met) "Met" else "Not met"
            },
    ) {
        if (reducedMotion) {
            RuleMark(met = met)
        } else {
            Crossfade(
                targetState = met,
                animationSpec = tween(durationMillis = AppTheme.motion.quick),
                label = "password-rule",
            ) { isMet ->
                RuleMark(met = isMet)
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
        )
    }
}

@Composable
private fun RuleMark(met: Boolean) {
    Box(
        modifier =
            Modifier
                .size(RuleMarkSize)
                .clip(CircleShape)
                .then(
                    if (met) {
                        Modifier.background(MaterialTheme.colorScheme.primary)
                    } else {
                        Modifier.border(RuleMarkStroke, MaterialTheme.colorScheme.outline, CircleShape)
                    },
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (met) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(RuleMarkSize - RuleMarkStroke * 4),
            )
        }
    }
}
