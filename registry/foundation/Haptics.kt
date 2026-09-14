package com.droidkit.registry.foundation

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

/**
 * The four haptics an item is allowed to use. Anything else is a product decision to argue for.
 *
 * Every haptic has a semantics counterpart in the item that fires it (a `stateDescription`,
 * an `error(...)`, an announcement): the moment must exist for someone with haptics off.
 * Respects the system "touch feedback" setting because it goes through [View.performHapticFeedback].
 */
class AppHaptics(
    private val view: View,
) {
    /** A control was pressed: button, chip, row. */
    fun click() {
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    /** A step changed: segmented control moved, a rule ticked, a digit landed. */
    fun tick() {
        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }

    /** Something completed: code accepted, form submitted, success shown. */
    fun confirm() {
        val constant =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                HapticFeedbackConstants.CONFIRM
            } else {
                HapticFeedbackConstants.CONTEXT_CLICK
            }
        view.performHapticFeedback(constant)
    }

    /** Something was refused: wrong password, wrong code, invalid input. */
    fun reject() {
        val constant =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                HapticFeedbackConstants.REJECT
            } else {
                HapticFeedbackConstants.LONG_PRESS
            }
        view.performHapticFeedback(constant)
    }
}

@Composable
fun rememberAppHaptics(): AppHaptics {
    val view = LocalView.current
    return remember(view) { AppHaptics(view) }
}
