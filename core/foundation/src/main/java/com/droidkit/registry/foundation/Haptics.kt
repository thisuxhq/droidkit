package com.droidkit.registry.foundation

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

fun View.performAppClickHaptic() {
    performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
}

class AppHaptics(
    private val view: View,
) {
    fun click() = view.performAppClickHaptic()
}

@Composable
fun rememberAppHaptics(): AppHaptics {
    val view = LocalView.current
    return remember(view) { AppHaptics(view) }
}
