@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidkit.registry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.theme.AppTheme

private val SheetCorner = 28.dp
private val HandleWidth = 32.dp
private val HandleHeight = 4.dp
private val HandleTarget = 48.dp
private val SheetPartialMax = 220.dp

private const val HandleDescription = "Drag handle"
private const val PartialStateDescription = "Half expanded"
private const val ExpandedStateDescription = "Expanded"

/** How the sheet sits on the screen. Bleed docks to the edges; Float insets as a card. */
enum class AppSheetChrome {
    Bleed,
    Float,
}

/**
 * The stop the sheet is sitting on. Material owns Hidden / Partial / Expanded;
 * content only designs for the two visible ones.
 *
 * skipPartiallyExpanded is true unless [AppBottomSheet] is called with expandable.
 */
enum class AppSheetDetent {
    Partial,
    Expanded,
}

/**
 * The detent the sheet is on right now. Read this in [content] to swap a peek
 * summary for the expanded body. Defaults to [AppSheetDetent.Expanded] so a
 * non-expandable sheet never looks collapsed.
 */
val LocalAppSheetDetent = compositionLocalOf { AppSheetDetent.Expanded }

/**
 * A modal sheet. The handle grows it; drag, back, or scrim leaves — there is no close X.
 *
 * see:      handle and title sit above the body; a footer stays reserved at the bottom.
 * act:      crossing a detent ticks and the body is the only slot that grows.
 * leave:    [onDismiss] is the only way out.
 *
 * expandable → Hidden / Partial / Expanded. Default fits content and skips Partial.
 */
@Composable
fun AppBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    chrome: AppSheetChrome = AppSheetChrome.Bleed,
    expandable: Boolean = false,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    AppBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier,
        chrome = chrome,
        expandable = expandable,
        header = { SheetTitle(title) },
        footer = footer,
        content = content,
    )
}

@Composable
fun AppBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    chrome: AppSheetChrome = AppSheetChrome.Bleed,
    expandable: Boolean = false,
    header: @Composable () -> Unit = {},
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (LocalInspectionMode.current) {
        AppBottomSheetChrome(
            modifier = modifier,
            chrome = chrome,
            detent = if (expandable) AppSheetDetent.Partial else AppSheetDetent.Expanded,
            header = header,
            footer = footer,
            content = content,
        )
        return
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = !expandable)
    val detent = sheetState.currentValue.toAppSheetDetent()
    val haptics = rememberAppHaptics()
    var lastDetent by remember { mutableStateOf<AppSheetDetent?>(null) }

    // tick + stateDescription change together when a detent settles.
    LaunchedEffect(detent) {
        if (lastDetent != null && lastDetent != detent) {
            haptics.tick()
        }
        lastDetent = detent
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier =
            if (chrome == AppSheetChrome.Float) {
                modifier.padding(AppTheme.spacing.md)
            } else {
                modifier
            },
        sheetState = sheetState,
        shape = sheetShape(chrome),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = AppTheme.elevation.flat,
        dragHandle = { SheetHandle(detent) },
    ) {
        CompositionLocalProvider(LocalAppSheetDetent provides detent) {
            AppBottomSheetBody(
                header = header,
                footer = footer,
                content = content,
            )
        }
    }
}

@Composable
internal fun AppBottomSheetChrome(
    modifier: Modifier = Modifier,
    title: String? = null,
    chrome: AppSheetChrome = AppSheetChrome.Bleed,
    detent: AppSheetDetent = AppSheetDetent.Expanded,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = sheetShape(chrome)
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (chrome == AppSheetChrome.Float) {
                        Modifier.padding(AppTheme.spacing.md)
                    } else {
                        Modifier
                    },
                )
                .then(
                    if (detent == AppSheetDetent.Partial) {
                        Modifier.heightIn(max = SheetPartialMax)
                    } else {
                        Modifier
                    },
                )
                .clip(shape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(bottom = AppTheme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SheetHandle(detent)
        CompositionLocalProvider(LocalAppSheetDetent provides detent) {
            AppBottomSheetBody(
                header = header ?: title?.let { { SheetTitle(it) } } ?: {},
                footer = footer,
                content = content,
            )
        }
    }
}

@Composable
private fun AppBottomSheetBody(
    header: @Composable () -> Unit,
    footer: (@Composable () -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
    ) {
        header()
        content()
        footer?.let { slot ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .imePadding(),
            ) {
                slot()
            }
        }
    }
}

@Composable
private fun SheetTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleLarge)
}

@Composable
private fun SheetHandle(detent: AppSheetDetent) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = HandleTarget)
                .semantics {
                    contentDescription = HandleDescription
                    stateDescription = detent.stateDescription()
                },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .width(HandleWidth)
                    .height(HandleHeight)
                    .clip(RoundedCornerShape(HandleHeight))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)),
        )
    }
}

private fun sheetShape(chrome: AppSheetChrome) =
    when (chrome) {
        AppSheetChrome.Bleed ->
            RoundedCornerShape(topStart = SheetCorner, topEnd = SheetCorner)
        AppSheetChrome.Float -> RoundedCornerShape(SheetCorner)
    }

private fun SheetValue.toAppSheetDetent(): AppSheetDetent =
    when (this) {
        SheetValue.Expanded -> AppSheetDetent.Expanded
        SheetValue.PartiallyExpanded,
        SheetValue.Hidden,
        -> AppSheetDetent.Partial
    }

private fun AppSheetDetent.stateDescription(): String =
    when (this) {
        AppSheetDetent.Partial -> PartialStateDescription
        AppSheetDetent.Expanded -> ExpandedStateDescription
    }
