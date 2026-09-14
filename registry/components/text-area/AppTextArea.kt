package com.droidkit.registry.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

private const val DefaultMinLines = 3
private const val DefaultMaxLines = 6

/**
 * Multiline field that reserves height so the form does not jump on the first character.
 *
 * see:      three lines of height before anyone types.
 * act:      grows up to six lines, then scrolls; optional [maxLength] counts without blocking.
 * mistake:  shake + reject + on-field message, same as AppTextField.
 * leave:    ImeAction.Default — this is a paragraph, not a submit.
 */
@Composable
fun AppTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    minLines: Int = DefaultMinLines,
    maxLines: Int = DefaultMaxLines,
    maxLength: Int? = null,
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
        maxLength = maxLength,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
            ),
    )
}
