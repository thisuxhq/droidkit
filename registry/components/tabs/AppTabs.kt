package com.droidkit.registry.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.droidkit.registry.foundation.rememberAppHaptics

/**
 * Peer panes. Selected is a heading you can jump to.
 *
 * see:      the selected tab is the heading for the pane below.
 * act:      changing tabs fires a tick haptic.
 * leave:    the selected index is caller-owned; this does not swipe pages.
 */
@Composable
fun AppTabs(
    titles: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = rememberAppHaptics()

    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        titles.forEachIndexed { index, title ->
            val selected = index == selectedIndex
            Tab(
                selected = selected,
                onClick = {
                    if (!selected) {
                        haptics.tick()
                        onSelect(index)
                    }
                },
                text = {
                    Text(
                        text = title,
                        modifier = if (selected) Modifier.semantics { heading() } else Modifier,
                    )
                },
            )
        }
    }
}
