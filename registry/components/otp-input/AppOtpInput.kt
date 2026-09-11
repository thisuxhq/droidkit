package com.droidkit.registry.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.droidkit.registry.theme.AppTheme

private val OtpBoxMinSize = 48.dp
private val OtpBoxCorner = 12.dp
private val OtpBoxStroke = 1.dp

/** Material 3 disabled content opacity (`md.sys.state.disabled.content.opacity`). */
private const val DisabledContentAlpha = 0.38f

/**
 * SMS or email one-time code. Digits only, fixed length, one field — not six TextFields.
 *
 * error → typing: [isError] is caller-owned; clear it on the first [onValueChange].
 * paste: keep digits only, cap at [length].
 * backspace: delete the last digit.
 */
@Composable
fun AppOtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
) {
    val digits = value.filter(Char::isDigit).take(length)
    val disabledContentColor =
        MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledContentAlpha)
    val digitColor =
        if (enabled) MaterialTheme.colorScheme.onBackground else disabledContentColor
    val digitStyle = MaterialTheme.typography.titleLarge
    val boxSize =
        max(
            OtpBoxMinSize,
            with(LocalDensity.current) { digitStyle.lineHeight.toDp() } + AppTheme.spacing.xs,
        )

    Column(modifier = modifier) {
        BasicTextField(
            value = digits,
            onValueChange = { incoming ->
                if (enabled) {
                    onValueChange(incoming.filter(Char::isDigit).take(length))
                }
            },
            modifier =
                Modifier.semantics {
                    contentType = ContentType.SmsOtpCode
                    if (!enabled) {
                        stateDescription = "Disabled"
                    }
                    if (isError) {
                        error(supportingText ?: "Invalid code")
                    }
                },
            enabled = enabled,
            singleLine = true,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done,
                ),
            decorationBox = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
                    ) {
                        repeat(length) { index ->
                            val char = digits.getOrNull(index)?.toString().orEmpty()
                            val stroke = otpStrokeColor(
                                index = index,
                                filledCount = digits.length,
                                enabled = enabled,
                                isError = isError,
                                disabledContentColor = disabledContentColor,
                            )
                            Box(
                                modifier =
                                    Modifier
                                        .sizeIn(minWidth = boxSize, minHeight = boxSize)
                                        .border(
                                            width = OtpBoxStroke,
                                            color = stroke,
                                            shape = RoundedCornerShape(OtpBoxCorner),
                                        ),
                                contentAlignment = Alignment.Center,
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
                        }
                    }
                }
            },
        )
        if (supportingText != null) {
            Text(
                text = supportingText,
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
private fun otpStrokeColor(
    index: Int,
    filledCount: Int,
    enabled: Boolean,
    isError: Boolean,
    disabledContentColor: Color,
): Color =
    when {
        !enabled -> disabledContentColor
        isError -> MaterialTheme.colorScheme.error
        index == filledCount -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
