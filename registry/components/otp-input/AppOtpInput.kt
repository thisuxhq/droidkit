package com.droidkit.registry.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val OtpBoxSize = 48.dp
private val OtpBoxCorner = 12.dp
private val OtpBoxStroke = 1.dp

@Composable
fun AppOtpInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    enabled: Boolean = true,
) {
    val digits = value.filter(Char::isDigit).take(length)

    BasicTextField(
        value = digits,
        onValueChange = { incoming ->
            onValueChange(incoming.filter(Char::isDigit).take(length))
        },
        modifier =
            modifier.semantics {
                contentDescription = "One-time code"
                contentType = ContentType.SmsOtpCode
            },
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppOtpGap),
            ) {
                repeat(length) { index ->
                    val char = digits.getOrNull(index)?.toString().orEmpty()
                    Box(
                        modifier =
                            Modifier
                                .size(OtpBoxSize)
                                .border(
                                    width = OtpBoxStroke,
                                    color =
                                        if (index == digits.length && enabled) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.outline
                                        },
                                    shape = RoundedCornerShape(OtpBoxCorner),
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        BasicText(
                            text = char,
                            style =
                                MaterialTheme.typography.titleLarge.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center,
                                ),
                        )
                    }
                }
            }
        },
    )
}

private val AppOtpGap = 8.dp
