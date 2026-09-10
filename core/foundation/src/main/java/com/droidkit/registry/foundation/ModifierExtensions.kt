package com.droidkit.registry.foundation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidkit.registry.theme.AppTheme

private val MinTouchTarget = 48.dp

fun Modifier.minTouchTarget(): Modifier = sizeIn(minWidth = MinTouchTarget, minHeight = MinTouchTarget)

@Composable
fun Modifier.screenPadding(): Modifier = padding(AppTheme.spacing.md)
