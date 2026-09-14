package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppAvatar
import com.droidkit.registry.components.AppBadge
import com.droidkit.registry.components.AppBottomSheet
import com.droidkit.registry.components.AppButton
import com.droidkit.registry.components.AppCard
import com.droidkit.registry.components.AppCheckbox
import com.droidkit.registry.components.AppChip
import com.droidkit.registry.components.AppIconButton
import com.droidkit.registry.components.AppPreferenceOption
import com.droidkit.registry.components.AppPreferencePicker
import com.droidkit.registry.components.AppProgress
import com.droidkit.registry.components.AppRadioGroup
import com.droidkit.registry.components.AppRadioOption
import com.droidkit.registry.components.AppSearchField
import com.droidkit.registry.components.AppSegmentedControl
import com.droidkit.registry.components.AppSettingRow
import com.droidkit.registry.components.AppSettingToggle
import com.droidkit.registry.components.AppSkeleton
import com.droidkit.registry.components.AppSnackbar
import com.droidkit.registry.components.AppSwitch
import com.droidkit.registry.components.AppTabs
import com.droidkit.registry.components.AppTextArea
import com.droidkit.registry.patterns.ConfirmationDialog
import com.droidkit.registry.patterns.ErrorState
import com.droidkit.registry.theme.AppTheme

@Composable
private fun CatalogColumn(
    padding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        content = content,
    )
}

@Composable
fun IconButtonScreen(onBack: () -> Unit) {
    var loading by rememberSaveable { mutableStateOf(false) }
    ShowcaseScaffold(title = "icon-button", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppIconButton(contentDescription = "More", onClick = {}) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
            }
            AppIconButton(contentDescription = "More", onClick = { loading = !loading }, loading = loading) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
            }
        }
    }
}

@Composable
fun SearchFieldScreen(onBack: () -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var lastSearch by rememberSaveable { mutableStateOf("") }
    ShowcaseScaffold(title = "search-field", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppSearchField(value = query, onValueChange = { query = it }, onSearch = { lastSearch = it })
            if (lastSearch.isNotEmpty()) {
                Text(text = "Last search: $lastSearch", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun TextAreaScreen(onBack: () -> Unit) {
    var notes by rememberSaveable { mutableStateOf("") }
    ShowcaseScaffold(title = "text-area", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppTextArea(value = notes, onValueChange = { notes = it }, label = "Notes", maxLength = 140)
        }
    }
}

@Composable
fun SwitchScreen(onBack: () -> Unit) {
    var on by rememberSaveable { mutableStateOf(true) }
    ShowcaseScaffold(title = "switch", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppSwitch(
                text = "Notifications",
                checked = on,
                onCheckedChange = { on = it },
                description = "Receive important updates",
            )
        }
    }
}

@Composable
fun CheckboxScreen(onBack: () -> Unit) {
    var on by rememberSaveable { mutableStateOf(false) }
    ShowcaseScaffold(title = "checkbox", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppCheckbox(text = "Remember this device", checked = on, onCheckedChange = { on = it })
        }
    }
}

@Composable
fun RadioScreen(onBack: () -> Unit) {
    var selected by rememberSaveable { mutableStateOf("system") }
    ShowcaseScaffold(title = "radio", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppRadioGroup(
                options =
                    listOf(
                        AppRadioOption("system", "System"),
                        AppRadioOption("light", "Light"),
                        AppRadioOption("dark", "Dark"),
                    ),
                selectedId = selected,
                onSelect = { selected = it },
            )
        }
    }
}

@Composable
fun ChipScreen(onBack: () -> Unit) {
    var selected by rememberSaveable { mutableStateOf(false) }
    ShowcaseScaffold(title = "chip", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppChip(text = "On sale", selected = selected, onClick = { selected = !selected })
        }
    }
}

@Composable
fun AvatarScreen(onBack: () -> Unit) {
    ShowcaseScaffold(title = "avatar", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppAvatar(name = "Ada Lovelace")
            AppAvatar(name = "Ada")
        }
    }
}

@Composable
fun BadgeScreen(onBack: () -> Unit) {
    ShowcaseScaffold(title = "badge", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppBadge(count = 3) {
                Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
            }
            AppBadge(count = 140) {
                Icon(imageVector = Icons.Filled.Email, contentDescription = "Inbox")
            }
        }
    }
}

@Composable
fun CardScreen(onBack: () -> Unit) {
    ShowcaseScaffold(title = "card", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppCard(title = "Weekly report", supporting = "Ready to review before Friday.")
            AppCard(
                title = "Weekly report",
                supporting = "Ready to review before Friday.",
                action = "Review",
                onAction = {},
            )
        }
    }
}

@Composable
fun SegmentedControlScreen(onBack: () -> Unit) {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    ShowcaseScaffold(title = "segmented-control", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppSegmentedControl(
                options = listOf("Day", "Week", "Month"),
                selectedIndex = selected,
                onSelect = { selected = it },
            )
        }
    }
}

@Composable
fun TabsScreen(onBack: () -> Unit) {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    ShowcaseScaffold(title = "tabs", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppTabs(
                titles = listOf("Inbox", "Sent", "Drafts"),
                selectedIndex = selected,
                onSelect = { selected = it },
            )
            Text(
                text = listOf("Inbox", "Sent", "Drafts")[selected],
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun ProgressScreen(onBack: () -> Unit) {
    ShowcaseScaffold(title = "progress", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppProgress(label = "Uploading photo", progress = 0.45f)
            AppProgress(label = "Preparing file")
        }
    }
}

@Composable
fun SkeletonScreen(onBack: () -> Unit) {
    ShowcaseScaffold(title = "skeleton", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppSkeleton()
            AppSkeleton()
        }
    }
}

@Composable
fun SnackbarScreen(onBack: () -> Unit) {
    var undone by rememberSaveable { mutableStateOf(false) }
    ShowcaseScaffold(title = "snackbar", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppSnackbar(message = "Draft saved", actionLabel = "Undo", onAction = { undone = true })
            if (undone) {
                Text(text = "Undone", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ConfirmDialogScreen(onBack: () -> Unit) {
    var open by rememberSaveable { mutableStateOf(false) }
    ShowcaseScaffold(title = "confirm-dialog", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppButton(text = "Discard draft", onClick = { open = true })
            if (open) {
                ConfirmationDialog(
                    title = "Discard draft",
                    description = "This draft will be gone. You can start a new one later.",
                    confirm = "Discard",
                    onConfirm = { open = false },
                    onDismiss = { open = false },
                    destructive = true,
                )
            }
        }
    }
}

@Composable
fun BottomSheetScreen(onBack: () -> Unit) {
    var open by rememberSaveable { mutableStateOf(false) }
    var theme by rememberSaveable { mutableStateOf("system") }
    ShowcaseScaffold(title = "bottom-sheet", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppButton(text = "Appearance", onClick = { open = true })
            Text(text = theme, style = MaterialTheme.typography.bodyMedium)
            if (open) {
                AppBottomSheet(title = "Appearance", onDismiss = { open = false }) {
                    AppRadioGroup(
                        options =
                            listOf(
                                AppRadioOption("system", "System"),
                                AppRadioOption("light", "Light"),
                                AppRadioOption("dark", "Dark"),
                            ),
                        selectedId = theme,
                        onSelect = { theme = it },
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorStateScreen(onBack: () -> Unit) {
    ShowcaseScaffold(title = "error-state", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            ErrorState(
                title = "Could not load projects",
                description = "Check the connection and try again.",
                action = "Try again",
                onAction = {},
                icon = Icons.Filled.Info,
            )
        }
    }
}

@Composable
fun SettingRowScreen(onBack: () -> Unit) {
    var notify by rememberSaveable { mutableStateOf(true) }
    ShowcaseScaffold(title = "setting-row", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppSettingRow(title = "Appearance", value = "System", onClick = {})
            AppSettingToggle(
                title = "Notifications",
                checked = notify,
                onCheckedChange = { notify = it },
                description = "Receive important updates",
            )
        }
    }
}

@Composable
fun PreferencePickerScreen(onBack: () -> Unit) {
    var selected by rememberSaveable { mutableStateOf("system") }
    ShowcaseScaffold(title = "preference-picker", onBack = onBack) { padding ->
        CatalogColumn(padding) {
            AppPreferencePicker(
                title = "Appearance",
                options =
                    listOf(
                        AppPreferenceOption("system", "System"),
                        AppPreferenceOption("light", "Light"),
                        AppPreferenceOption("dark", "Dark"),
                    ),
                selectedId = selected,
                onSelect = { selected = it },
            )
        }
    }
}
