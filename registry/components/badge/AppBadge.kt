package com.droidkit.registry.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

private const val BadgeCap = 99

/**
 * A count on something else. Zero is absence. A hundred is 99+.
 *
 * see:      [count] 1–99 shows the number; 100 and up shows 99+; 0 hides unless [showZero].
 * see:      [count] null is a dot — present, uncounted.
 * act:      TalkBack reads the badge as part of [content]'s description.
 */
@Composable
fun AppBadge(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    count: Int? = null,
    showZero: Boolean = false,
) {
    val visible = when (count) {
        null -> true
        0 -> showZero
        else -> count > 0
    }
    val label = when {
        count == null -> null
        count > BadgeCap -> "$BadgeCap+"
        else -> count.toString()
    }
    val spoken =
        when {
            !visible -> null
            count == null -> "New"
            count > BadgeCap -> "More than $BadgeCap"
            count == 1 -> "1 notification"
            else -> "$count notifications"
        }

    BadgedBox(
        badge = {
            if (visible) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ) {
                    if (label != null) {
                        Text(text = label, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        },
        modifier =
            modifier.semantics {
                if (spoken != null) contentDescription = spoken
            },
    ) {
        content()
    }
}
