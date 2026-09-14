package com.droidkit.registry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AvatarSize = 40.dp
private val AvatarHairline = 1.dp

/**
 * A person, never an empty circle.
 *
 * see:      initials from [name] (one letter, or two from first and last word).
 * act:      [image] overlays the initials when the photo is ready; the name stays the description.
 */
@Composable
fun AppAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = AvatarSize,
    image: (@Composable () -> Unit)? = null,
) {
    val initials = initialsFor(name)
    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(AvatarHairline, MaterialTheme.colorScheme.outline, CircleShape)
                .semantics { contentDescription = name },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = (size.value * 0.4f).sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (image != null) {
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .clip(CircleShape),
            ) {
                image()
            }
        }
    }
}

internal fun initialsFor(name: String): String {
    val parts = name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (parts.isEmpty()) return "?"
    val first = parts.first().first().uppercaseChar()
    val last = parts.getOrNull(1)?.first()?.uppercaseChar()
    return if (last == null) first.toString() else "$first$last"
}
