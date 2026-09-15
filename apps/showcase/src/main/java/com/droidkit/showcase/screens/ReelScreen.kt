package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppReel
import com.droidkit.registry.components.ReelMoment
import com.droidkit.registry.theme.AppTheme

private val WalkMoments =
    listOf(
        ReelMoment(id = "arrive", label = "Arrive", icon = Icons.Filled.Place),
        ReelMoment(id = "stay", label = "Stay", icon = Icons.Filled.Home),
        ReelMoment(id = "taste", label = "Taste", icon = Icons.Filled.Favorite),
        ReelMoment(id = "look", label = "Look", icon = Icons.Filled.Star),
        ReelMoment(id = "meet", label = "Meet", icon = Icons.Filled.Person),
    )

private val WalkBodies =
    listOf(
        "Step off the train and find the square. The first turn is the one with the bakery.",
        "Drop the bags and open the windows. Give the room an hour before you ask anything of it.",
        "Order the thing the room is talking about. If they hesitate, take the second one they mention.",
        "Walk until the street gets quiet. That is usually two blocks past where you thought it would.",
        "Sit with the person who knows the city. Let them pick the last place.",
    )

@Composable
fun ReelScreen(onBack: () -> Unit) {
    var selected by rememberSaveable { mutableIntStateOf(2) }

    ShowcaseScaffold(title = "reel", onBack = onBack) { padding ->
        AppReel(
            moments = WalkMoments,
            selectedIndex = selected,
            onSelect = { selected = it },
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
        ) { index ->
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.spacing.lg),
            ) {
                Text(
                    text = WalkMoments[index].label,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = WalkBodies[index],
                    modifier = Modifier.padding(top = AppTheme.spacing.sm),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
