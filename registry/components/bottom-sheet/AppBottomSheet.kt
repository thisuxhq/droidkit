@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidkit.registry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.droidkit.registry.theme.AppTheme

private val SheetCorner = 28.dp
private val HandleWidth = 32.dp
private val HandleHeight = 4.dp

/**
 * A titled sheet. Drag, back, or scrim dismisses — there is no close X.
 *
 * see:      a handle and a title sit above the content.
 * leave:    [onDismiss] is the only way out.
 */
@Composable
fun AppBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (LocalInspectionMode.current) {
        AppBottomSheetChrome(
            title = title,
            modifier = modifier,
            content = content,
        )
        return
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = SheetCorner, topEnd = SheetCorner),
        dragHandle = { SheetHandle() },
    ) {
        AppBottomSheetBody(title = title, content = content)
    }
}

@Composable
internal fun AppBottomSheetChrome(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = SheetCorner, topEnd = SheetCorner))
                .background(MaterialTheme.colorScheme.surface)
                .padding(bottom = AppTheme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SheetHandle()
        AppBottomSheetBody(title = title, content = content)
    }
}

@Composable
private fun AppBottomSheetBody(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        content()
    }
}

@Composable
private fun SheetHandle() {
    Box(
        modifier =
            Modifier
                .padding(vertical = AppTheme.spacing.sm)
                .width(HandleWidth)
                .height(HandleHeight)
                .clip(RoundedCornerShape(HandleHeight))
                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)),
    )
}
