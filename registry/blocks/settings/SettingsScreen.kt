@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class,
)

package com.droidkit.registry.blocks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.droidkit.registry.components.AppButton
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.launch

// Appearance click → detail pane (phone replaces the list; tablet keeps list | detail).
// Theme value stays on SettingsState; onThemeClick is the caller's picker or cycle.
// Notifications toggle → onNotificationsChanged. Switch is display-only (onCheckedChange = null).
// Rotation: pane navigator is rememberSaveable; settings values are caller-owned.

private val SettingsRowMinHeight = 48.dp

data class SettingsState(
    val notifications: Boolean,
    val theme: ThemeOption,
)

enum class ThemeOption(
    val label: String,
) {
    System("System"),
    Light("Light"),
    Dark("Dark"),
}

private enum class SettingsDetail {
    Appearance,
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onNotificationsChanged: (Boolean) -> Unit,
    onThemeClick: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<SettingsDetail>()
    val scope = rememberCoroutineScope()
    val onNotificationsChangedState = rememberUpdatedState(onNotificationsChanged)
    val onThemeClickState = rememberUpdatedState(onThemeClick)
    val onLogoutState = rememberUpdatedState(onLogout)
    val onNotificationsToggle =
        remember {
            { checked: Boolean -> onNotificationsChangedState.value(checked) }
        }
    val onAppearanceClick =
        remember(navigator, scope) {
            {
                scope.launch {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, SettingsDetail.Appearance)
                }
                Unit
            }
        }
    val onLogoutClick =
        remember {
            { onLogoutState.value() }
        }
    val onChangeTheme =
        remember {
            { onThemeClickState.value() }
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
            )
        },
    ) { padding ->
        NavigableListDetailPaneScaffold(
            navigator = navigator,
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
            listPane = {
                AnimatedPane {
                    SettingsListPane(
                        state = state,
                        onNotificationsToggle = onNotificationsToggle,
                        onAppearanceClick = onAppearanceClick,
                        onLogoutClick = onLogoutClick,
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    SettingsDetailPane(
                        destination = navigator.currentDestination?.contentKey,
                        state = state,
                        onChangeTheme = onChangeTheme,
                    )
                }
            },
        )
    }
}

@Composable
private fun SettingsListPane(
    state: SettingsState,
    onNotificationsToggle: (Boolean) -> Unit,
    onAppearanceClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(AppTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        item(key = "section-general", contentType = "section") {
            SettingsSectionTitle("General")
        }
        item(key = "row-notifications", contentType = "toggle") {
            SettingsToggleRow(
                title = "Notifications",
                description = "Receive important updates",
                checked = state.notifications,
                onCheckedChange = onNotificationsToggle,
            )
        }
        item(key = "row-appearance", contentType = "nav") {
            SettingsRow(
                title = "Appearance",
                value = state.theme.label,
                onClick = onAppearanceClick,
            )
        }
        item(key = "spacer-logout", contentType = "spacer") {
            Spacer(Modifier.height(AppTheme.spacing.lg))
        }
        item(key = "row-logout", contentType = "action") {
            AppButton(text = "Log out", onClick = onLogoutClick)
        }
    }
}

@Composable
private fun SettingsDetailPane(
    destination: SettingsDetail?,
    state: SettingsState,
    onChangeTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(AppTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        when (destination) {
            SettingsDetail.Appearance -> {
                Text(
                    text = "Appearance",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = state.theme.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                AppButton(text = "Change theme", onClick = onChangeTheme)
            }
            null -> {
                Text(
                    text = "Select a setting",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "Choose a row to see details.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier.padding(vertical = AppTheme.spacing.xs),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun SettingsToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = rememberAppHaptics()
    ListItem(
        headlineContent = {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = null)
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = SettingsRowMinHeight)
                .toggleable(
                    value = checked,
                    role = Role.Switch,
                    onValueChange = {
                        haptics.tick()
                        onCheckedChange(it)
                    },
                ),
    )
}

@Composable
private fun SettingsRow(
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        },
        trailingContent = {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = SettingsRowMinHeight)
                .clickable(role = Role.Button, onClick = onClick),
    )
}
