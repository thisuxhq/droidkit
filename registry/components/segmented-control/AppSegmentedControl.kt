@file:OptIn(ExperimentalMaterial3Api::class)

package com.droidkit.registry.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.rememberAppHaptics

private val SegmentMinHeight = 48.dp

/**
 * Two to five exclusive peers in one row. Not tabs — nothing pages.
 *
 * see:      equal-width segments; the selected one is filled.
 * act:      changing selection fires a tick haptic.
 * leave:    the selected index is caller-owned.
 */
@Composable
fun AppSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val haptics = rememberAppHaptics()

    SingleChoiceSegmentedButtonRow(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = SegmentMinHeight),
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = {
                    if (index != selectedIndex) {
                        haptics.tick()
                        onSelect(index)
                    }
                },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                enabled = enabled,
                label = { Text(text = label) },
            )
        }
    }
}
