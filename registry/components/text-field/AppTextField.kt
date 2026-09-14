package com.droidkit.registry.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberReducedMotion
import com.droidkit.registry.foundation.shake
import com.droidkit.registry.theme.AppTheme

private val TextFieldMinHeight = 56.dp
private val ClearButtonSize = 48.dp

/**
 * Labeled text field that owns the mistake and the recovery, not just the border colour.
 *
 * mistake:  when [isError] turns on, the field shakes once and fires the reject haptic;
 *           [supportingText] is the message, in the error colour, on the field.
 * recover:  the first keystroke after the error drops the error colour; the message stays as a
 *           quiet hint so the line does not jump. If the field loses focus and the caller still
 *           says [isError], the error shows again (and shakes again) — the caller's flag is the truth.
 *           So: set [isError] on submit or blur, not on every keystroke.
 * act:      a clear button appears whenever the field has text and is enabled; clearing keeps focus.
 *           [maxLength] shows "n / max" and turns the field to error past the limit; it does not block.
 * rotation: whether the current error was already felt survives, so it does not shake twice.
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLength: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    contentType: ContentType? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }
    val haptics = rememberAppHaptics()
    val reducedMotion = rememberReducedMotion()

    // The value the caller flagged. While the person is editing away from it, the error goes quiet.
    var flaggedValue by rememberSaveable { mutableStateOf<String?>(null) }
    LaunchedEffect(isError) {
        flaggedValue = if (isError) value else null
    }
    val recovering = isError && focused && flaggedValue != null && value != flaggedValue
    val overLimit = maxLength != null && value.length > maxLength
    val showError = (isError && !recovering) || overLimit

    var errorFelt by rememberSaveable { mutableStateOf(false) }
    var shakeTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(showError) {
        if (showError) {
            if (!errorFelt) {
                errorFelt = true
                shakeTrigger = true
                haptics.reject()
            }
        } else {
            errorFelt = false
            shakeTrigger = false
        }
    }

    val showClear = enabled && value.isNotEmpty() && trailingIcon == null
    val counter = maxLength?.let { "${value.length} / $it" }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = TextFieldMinHeight)
                .focusRequester(focusRequester)
                .shake(shakeTrigger)
                .then(
                    if (reducedMotion) {
                        Modifier
                    } else {
                        Modifier.animateContentSize(tween(durationMillis = AppTheme.motion.normal))
                    },
                )
                .then(
                    if (contentType != null) {
                        Modifier.semantics { this.contentType = contentType }
                    } else {
                        Modifier
                    },
                ),
        enabled = enabled,
        textStyle = MaterialTheme.typography.bodyLarge,
        label = label?.let { { Text(text = it, style = MaterialTheme.typography.labelLarge) } },
        placeholder = placeholder?.let { { Text(text = it, style = MaterialTheme.typography.bodyLarge) } },
        supportingText =
            if (supportingText != null || counter != null) {
                {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = supportingText.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        if (counter != null) {
                            Text(
                                text = counter,
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    if (overLimit) {
                                        MaterialTheme.colorScheme.error
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    },
                                modifier = Modifier.padding(start = AppTheme.spacing.sm),
                            )
                        }
                    }
                }
            } else {
                null
            },
        isError = showError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        interactionSource = interactionSource,
        leadingIcon = leadingIcon,
        trailingIcon =
            when {
                trailingIcon != null -> trailingIcon
                showClear -> {
                    {
                        IconButton(
                            onClick = {
                                onValueChange("")
                                focusRequester.requestFocus()
                            },
                            modifier = Modifier.size(ClearButtonSize),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = clearDescription(label),
                            )
                        }
                    }
                }
                else -> null
            },
        shape = MaterialTheme.shapes.small,
    )
}

private fun clearDescription(label: String?): String =
    if (label.isNullOrBlank()) "Clear" else "Clear ${label.replaceFirstChar { it.lowercaseChar() }}"
