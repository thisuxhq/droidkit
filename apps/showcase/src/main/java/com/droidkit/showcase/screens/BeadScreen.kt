package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppBead
import com.droidkit.registry.theme.AppTheme

@Composable
fun BeadScreen(onBack: () -> Unit) {
    var frequency by rememberSaveable { mutableIntStateOf(1) }
    var intensity by rememberSaveable { mutableIntStateOf(0) }

    ShowcaseScaffold(title = "bead", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xl),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                Text(
                    text = "Notification emails",
                    style = MaterialTheme.typography.titleMedium,
                )
                AppBead(
                    options =
                        listOf(
                            "Never send me notification emails",
                            "Only send me a periodic notification email",
                            "Send me an email for every notification",
                        ),
                    selectedIndex = frequency,
                    onSelect = { frequency = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md)) {
                Text(
                    text = "Disabled",
                    style = MaterialTheme.typography.titleMedium,
                )
                AppBead(
                    options = listOf("Low", "Medium", "High"),
                    selectedIndex = intensity,
                    onSelect = { intensity = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                )
            }
        }
    }
}
