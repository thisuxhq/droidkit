package com.droidkit.registry.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.droidkit.registry.foundation.rememberAppHaptics

/**
 * A short message with at most one action. No close X — timeout or swipe leaves.
 *
 * see:      the message is the whole point; the action is optional and one word.
 * act:      tapping the action fires a click haptic and [onAction].
 * leave:    the host owns duration; this is the surface, not the timer.
 */
@Composable
fun AppSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val haptics = rememberAppHaptics()

    Snackbar(
        modifier = modifier,
        action =
            if (actionLabel != null && onAction != null) {
                {
                    TextButton(
                        onClick = {
                            haptics.click()
                            onAction()
                        },
                    ) {
                        Text(text = actionLabel)
                    }
                }
            } else {
                null
            },
        dismissAction = null,
    ) {
        Text(text = message)
    }
}
