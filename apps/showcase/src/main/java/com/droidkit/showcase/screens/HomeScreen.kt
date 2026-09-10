package com.droidkit.showcase.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.droidkit.registry.theme.AppTheme
import com.droidkit.showcase.navigation.ShowcaseDestination
import com.droidkit.showcase.navigation.showcaseCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onOpen: (ShowcaseDestination) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("droidkit showcase") })
        },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
            contentPadding = PaddingValues(AppTheme.spacing.md),
        ) {
            item {
                Text(
                    text = "taste-test every registered item here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = AppTheme.spacing.md),
                )
            }
            items(showcaseCatalog, key = { it.title }) { destination ->
                Text(
                    text = destination.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { onOpen(destination) }
                            .padding(vertical = AppTheme.spacing.md),
                )
                HorizontalDivider()
            }
        }
    }
}
