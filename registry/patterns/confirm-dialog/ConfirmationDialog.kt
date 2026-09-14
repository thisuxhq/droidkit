package com.droidkit.registry.patterns

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppDestructiveButton
import com.droidkit.registry.components.AppTextButton

/**
 * A decision with an easy out.
 *
 * see:      title names the action; the body names the consequence.
 * act:      confirm is red text, never a red fill; cancel is the quiet text button.
 * leave:    back and scrim call [onDismiss] — the same as cancel.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    confirm: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismiss: String = "Cancel",
    destructive: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            if (destructive) {
                AppDestructiveButton(text = confirm, onClick = onConfirm)
            } else {
                AppTextButton(text = confirm, onClick = onConfirm)
            }
        },
        dismissButton = {
            AppTextButton(text = dismiss, onClick = onDismiss)
        },
    )
}
