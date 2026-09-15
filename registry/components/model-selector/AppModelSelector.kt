package com.droidkit.registry.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.theme.AppTheme
import java.text.Normalizer

private val TriggerMinHeight = 48.dp
private val TriggerCorner = 14.dp
private val TriggerMarkSize = 20.dp
private val RowMinHeight = 48.dp
private val HeaderMarkSize = 18.dp
private val SheetListMaxHeight = 420.dp
private const val SearchThreshold = 8
private const val DefaultTitle = "Choose a model"
private const val DefaultSearchPlaceholder = "Search models"
private const val EmptyQueryCopy = "No models match"
private const val TriggerExpandedDescription = "Expanded"
private const val TriggerCollapsedDescription = "Collapsed"

@Immutable
data class AppModelProvider(
    val id: String,
    val name: String,
    val mark: ImageVector? = null,
) {
    init {
        require(id.isNotBlank()) { "Provider id must not be blank" }
        require(name.isNotBlank()) { "Provider name must not be blank" }
    }
}

@Immutable
data class AppModelOption(
    val id: String,
    val name: String,
    val provider: AppModelProvider,
    val capability: String,
) {
    init {
        require(id.isNotBlank()) { "Model id must not be blank" }
        require(name.isNotBlank()) { "Model name must not be blank" }
        require(capability.isNotBlank()) { "Model capability must not be blank" }
    }
}

@Immutable
private sealed interface ModelSelectorPresentation {
    data object Closed : ModelSelectorPresentation

    @Immutable
    data class Open(val query: String) : ModelSelectorPresentation
}

@Immutable
internal data class ModelGroup(
    val provider: AppModelProvider,
    val models: List<AppModelOption>,
)

private val PresentationSaver =
    Saver<ModelSelectorPresentation, List<String>>(
        save = { value ->
            when (value) {
                ModelSelectorPresentation.Closed -> listOf("closed")
                is ModelSelectorPresentation.Open -> listOf("open", value.query)
            }
        },
        restore = { saved ->
            if (saved.firstOrNull() == "open") {
                ModelSelectorPresentation.Open(saved.getOrElse(1) { "" })
            } else {
                ModelSelectorPresentation.Closed
            }
        },
    )

/**
 * A compact current-model control backed by one provider-grouped Android sheet.
 *
 * see:      the trigger always names the selected model.
 * reach:    48 dp, pressScale, click haptic.
 * act:      catalogs through seven stay thumb-first; eight or more show Search and filter live.
 * mistake:  no matches keep Search and show one polite live-region line.
 * succeed:  a new row ticks, emits its id, and closes; the current row only closes.
 * leave:    pick, back, drag, and scrim all discard the query.
 */
@Composable
fun AppModelSelector(
    models: List<AppModelOption>,
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String = DefaultTitle,
) {
    validateModelCatalog(models, selectedId)
    val current = models.first { it.id == selectedId }
    var presentation by rememberSaveable(stateSaver = PresentationSaver) {
        mutableStateOf<ModelSelectorPresentation>(ModelSelectorPresentation.Closed)
    }
    val keyboard = LocalSoftwareKeyboardController.current
    val expanded = presentation is ModelSelectorPresentation.Open
    val query =
        if (models.size >= SearchThreshold) {
            (presentation as? ModelSelectorPresentation.Open)?.query.orEmpty()
        } else {
            ""
        }

    fun dismiss() {
        keyboard?.hide()
        presentation = ModelSelectorPresentation.Closed
    }

    ModelTrigger(
        model = current,
        expanded = expanded,
        enabled = enabled,
        onClick = {
            if (expanded) {
                dismiss()
            } else {
                presentation = ModelSelectorPresentation.Open("")
            }
        },
        modifier = modifier,
    )

    if (expanded) {
        AppBottomSheet(title = title, onDismiss = { dismiss() }) {
            AppModelSelectorSheetPreview(
                models = models,
                selectedId = selectedId,
                query = query,
                onQueryChange = { presentation = ModelSelectorPresentation.Open(it) },
                onSelect = { id ->
                    val next = emitIfChanged(selectedId, id)
                    dismiss()
                    if (next != null) onSelect(next)
                },
            )
        }
    }
}

/**
 * Screenshot seam and windowless tests. Runtime callers use [AppModelSelector].
 */
@Composable
internal fun AppModelSelectorSheetPreview(
    models: List<AppModelOption>,
    selectedId: String,
    query: String,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit = {},
    onSelect: (String) -> Unit = {},
) {
    val showSearch = models.size >= SearchThreshold
    val activeQuery = if (showSearch) query else ""
    val groups = groupedModels(models, activeQuery)
    val haptics = rememberAppHaptics()
    val keyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .imePadding(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
    ) {
        if (showSearch) {
            AppSearchField(
                value = query,
                onValueChange = onQueryChange,
                onSearch = { keyboard?.hide() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.spacing.md),
                placeholder = DefaultSearchPlaceholder,
            )
        }
        if (groups.isEmpty()) {
            Text(
                text = EmptyQueryCopy,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.md, vertical = AppTheme.spacing.sm)
                        .semantics(mergeDescendants = true) {
                            liveRegion = LiveRegionMode.Polite
                        },
            )
        } else {
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = SheetListMaxHeight)
                        .selectableGroup(),
            ) {
                groups.forEach { group ->
                    if (group.provider.name.isNotEmpty()) {
                        item(key = "header-${group.provider.id}") {
                            ProviderHeader(provider = group.provider)
                        }
                    }
                    items(items = group.models, key = { it.id }) { model ->
                        val selected = model.id == selectedId
                        ModelRow(
                            model = model,
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    haptics.tick()
                                }
                                onSelect(model.id)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModelTrigger(
    model: AppModelOption,
    expanded: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberAppHaptics()
    val spokenState = if (expanded) TriggerExpandedDescription else TriggerCollapsedDescription

    Row(
        modifier =
            modifier
                .heightIn(min = TriggerMinHeight)
                .clip(RoundedCornerShape(TriggerCorner))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .pressScale(interactionSource = interactionSource, enabled = enabled)
                .clickable(
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        haptics.click()
                        onClick()
                    },
                )
                .semantics(mergeDescendants = true) {
                    stateDescription = spokenState
                }
                .padding(horizontal = AppTheme.spacing.sm, vertical = AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        model.provider.mark?.let { mark ->
            Icon(
                imageVector = mark,
                contentDescription = null,
                modifier = Modifier.size(TriggerMarkSize),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = model.name,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false),
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ProviderHeader(provider: AppModelProvider) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.md, vertical = AppTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        provider.mark?.let { mark ->
            Icon(
                imageVector = mark,
                contentDescription = null,
                modifier = Modifier.size(HeaderMarkSize),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = provider.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ModelRow(
    model: AppModelOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember(model.id) { MutableInteractionSource() }
    ListItem(
        headlineContent = {
            Text(text = model.name, style = MaterialTheme.typography.titleMedium)
        },
        supportingContent = {
            Text(text = model.capability, style = MaterialTheme.typography.bodyMedium)
        },
        trailingContent =
            if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                null
            },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = RowMinHeight)
                .pressScale(interactionSource = interactionSource)
                .selectable(
                    selected = selected,
                    role = Role.RadioButton,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
    )
}

internal fun foldForSearch(value: String): String {
    val normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
    return buildString(normalized.length) {
        for (ch in normalized) {
            if (ch.category == CharCategory.NON_SPACING_MARK) continue
            if (ch.isLetterOrDigit()) append(ch.lowercaseChar())
        }
    }
}

internal fun AppModelOption.matchesQuery(query: String): Boolean {
    val tokens = query.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (tokens.isEmpty()) return true
    val haystack = foldForSearch("${provider.name} $name")
    return tokens.all { token -> haystack.contains(foldForSearch(token)) }
}

internal fun groupedModels(
    models: List<AppModelOption>,
    query: String,
): List<ModelGroup> {
    val usesHeaders = models.map { it.provider.id }.distinct().size > 1
    val visible = models.filter { it.matchesQuery(query) }
    if (visible.isEmpty()) return emptyList()
    val groups = linkedMapOf<String, MutableList<AppModelOption>>()
    val providers = linkedMapOf<String, AppModelProvider>()
    visible.forEach { model ->
        providers.putIfAbsent(model.provider.id, model.provider)
        groups.getOrPut(model.provider.id) { mutableListOf() }.add(model)
    }
    return groups.map { (id, groupModels) ->
        val provider = providers.getValue(id)
        ModelGroup(
            provider = if (usesHeaders) provider else provider.copy(name = "", mark = null),
            models = groupModels,
        )
    }
}

internal fun emitIfChanged(
    currentId: String,
    pickedId: String,
): String? = pickedId.takeIf { it != currentId }

internal fun validateModelCatalog(
    models: List<AppModelOption>,
    selectedId: String,
) {
    require(models.isNotEmpty()) { "A model selector needs at least one model." }
    val ids = models.map { it.id }
    require(ids.size == ids.distinct().size) { "Model ids must be unique." }
    require(models.count { it.id == selectedId } == 1) {
        "selectedId must match exactly one model."
    }
    val byProvider = models.groupBy { it.provider.id }
    byProvider.forEach { (_, group) ->
        val first = group.first().provider
        require(group.all { it.provider.name == first.name }) {
            "Provider ${first.id} has conflicting names."
        }
        require(group.all { it.provider.mark == first.mark }) {
            "Provider ${first.id} has conflicting marks."
        }
    }
}
