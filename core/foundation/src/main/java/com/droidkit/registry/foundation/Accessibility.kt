package com.droidkit.registry.foundation

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

fun Modifier.hideFromAccessibility(): Modifier =
    semantics(mergeDescendants = true) {
        // decorative; merged parent speaks instead
    }

fun Modifier.loadingSemantics(
    loading: Boolean,
    label: String,
): Modifier =
    semantics {
        if (loading) {
            stateDescription = "Loading"
            liveRegion = LiveRegionMode.Polite
            disabled()
        } else {
            stateDescription = label
        }
    }
