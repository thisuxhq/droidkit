package com.droidkit.registry.components

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberReducedMotion
import com.droidkit.registry.theme.AppTheme
import kotlin.math.max
import kotlin.math.roundToInt

private val PillHorizontalPadding = 8.dp
private val PillVerticalPadding = 2.dp
private val PillCorner = 8.dp
private val PillMaxWidth = 220.dp
private val PillTouchTarget = 48.dp
private val NoTouchOverflow = 0.dp
private val QuoteBarWidth = 2.dp
private val SourceHairline = 1.dp
private val SourceCardCorner = 12.dp
private val DefaultLineHeight = 24.sp
private const val DisabledChevronAlpha = 0.45f

private val MarkerRegex = Regex("""\[(\d+)\]""")

/**
 * A source attached to a claim. [url] is required; [title] falls back to the hostname;
 * [description] and [quote] are optional extras for the open card.
 */
data class AppCitation(
    val url: String,
    val title: String = "",
    val description: String = "",
    val quote: String? = null,
)

private data class CitationAnchor(
    val id: Int,
    val offset: Int,
    val widthPx: Int,
    val heightPx: Int,
)

/**
 * A paragraph that can name its sources without becoming a footnote.
 *
 * Drop `{cite}` or `[1]` where the claim is, pass [sources], and a hostname pill sits in the
 * line. Extra sources collapse to +N. Tap the pill and the source opens under the paragraph;
 * tap again to close. Multiple groups use `[1]`, `[2]` with the map overload.
 *
 * [expanded] / [expandedCitation] seed [rememberSaveable] once for previews and tests; later
 * parent changes are ignored. Open id and page survive rotation.
 *
 * see:      the pill is a hostname, plus +N when more sources wait behind it.
 * reach:    pressScale + click; the visual stays line-sized; the hit target is a
 *           min-48 dp box around that pill, not a square parked on its centre.
 * act:      tap opens the source card under the text; extra sources page one at a time.
 * leave:    a second tap on the pill closes the card; open id and page survive rotation.
 *
 * tap pill → open: the card appears under the paragraph; TalkBack reads the source title.
 * extra sources → next: index ticks; the title is a live region.
 * open → tap pill: the card hides; the sentence does not jump.
 */
@Composable
fun AppInlineCitation(
    text: String,
    sources: List<AppCitation>,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    onOpenUrl: ((String) -> Unit)? = null,
) {
    val prepared = normalizeCitationText(text)
    val withMark =
        when {
            MarkerRegex.containsMatchIn(prepared) -> prepared
            prepared.isBlank() -> "[1]"
            else -> "$prepared [1]"
        }
    AppInlineCitation(
        text = withMark,
        citations = mapOf(1 to sources),
        modifier = modifier,
        expandedCitation = if (expanded) 1 else null,
        style = style,
        onOpenUrl = onOpenUrl,
    )
}

@Composable
fun AppInlineCitation(
    text: String,
    citations: Map<Int, List<AppCitation>>,
    modifier: Modifier = Modifier,
    expandedCitation: Int? = null,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    onOpenUrl: ((String) -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current
    val openUrl = onOpenUrl ?: { url -> uriHandler.openUri(url) }
    var openId by rememberSaveable { mutableStateOf(expandedCitation) }
    var sourceIndex by rememberSaveable { mutableIntStateOf(0) }
    var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

    val citationIds = remember(citations) { citations.keys.filter { citations[it].orEmpty().isNotEmpty() } }
    val pillInteractions =
        remember(citationIds) {
            citationIds.associateWith { MutableInteractionSource() }
        }

    val spans = remember(text) { splitCitedText(normalizeCitationText(text)) }
    val inlineContent =
        citationInlineContent(
            citations = citations,
            openId = openId,
            style = style,
            interactions = pillInteractions,
        )

    val density = LocalDensity.current
    val (annotated, anchors) =
        remember(spans, inlineContent.keys, density) {
            val found = mutableListOf<CitationAnchor>()
            val built =
                buildAnnotatedString {
                    spans.forEach { span ->
                        when (span) {
                            is CitedSpan.Words -> append(span.value)
                            is CitedSpan.Mark -> {
                                val slot = inlineContent[span.key]
                                if (slot != null) {
                                    found +=
                                        CitationAnchor(
                                            id = span.id,
                                            offset = length,
                                            widthPx = with(density) { slot.placeholder.width.toPx() }.roundToInt(),
                                            heightPx = with(density) { slot.placeholder.height.toPx() }.roundToInt(),
                                        )
                                    appendInlineContent(span.key)
                                } else {
                                    append("[${span.id}]")
                                }
                            }
                        }
                    }
                }
            built to found.toList()
        }
    val lineHeight = if (style.lineHeight != TextUnit.Unspecified) style.lineHeight else DefaultLineHeight
    val touchOverflow = with(density) { ((PillTouchTarget - lineHeight.toDp()) / 2).coerceAtLeast(NoTouchOverflow) }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = annotated,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = touchOverflow),
                style = style,
                color = MaterialTheme.colorScheme.onSurface,
                inlineContent = inlineContent,
                onTextLayout = { textLayout = it },
            )
            val overflowPx = with(density) { touchOverflow.toPx() }
            Box(modifier = Modifier.matchParentSize()) {
                val layout = textLayout
                if (layout != null) {
                    val length = layout.layoutInput.text.length
                    anchors.forEach { anchor ->
                        val sourcesForMark = citations[anchor.id].orEmpty()
                        val interaction = pillInteractions[anchor.id]
                        if (sourcesForMark.isNotEmpty() && interaction != null && anchor.offset in 0 until length) {
                            CitationHitTarget(
                                box = layout.getBoundingBox(anchor.offset),
                                widthPx = anchor.widthPx,
                                heightPx = anchor.heightPx,
                                yShift = overflowPx,
                                sources = sourcesForMark,
                                expanded = openId == anchor.id,
                                pageIndex = if (openId == anchor.id) sourceIndex else 0,
                                interactionSource = interaction,
                                onClick = {
                                    if (openId == anchor.id) {
                                        openId = null
                                    } else {
                                        openId = anchor.id
                                        sourceIndex = 0
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
        val openSources = openId?.let { citations[it] }.orEmpty()
        if (openSources.isNotEmpty()) {
            val index = sourceIndex.coerceIn(0, openSources.lastIndex)
            CitationSourceCard(
                sources = openSources,
                index = index,
                onIndexChange = { sourceIndex = it },
                onOpenUrl = openUrl,
                modifier = Modifier.padding(top = AppTheme.spacing.sm),
            )
        }
    }
}

@Composable
private fun citationInlineContent(
    citations: Map<Int, List<AppCitation>>,
    openId: Int?,
    style: TextStyle,
    interactions: Map<Int, MutableInteractionSource>,
): Map<String, InlineTextContent> {
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val pillStyle = MaterialTheme.typography.labelSmall
    val placeholderHeight =
        if (style.lineHeight != TextUnit.Unspecified) style.lineHeight else DefaultLineHeight
    val maxHostPx = with(density) { PillMaxWidth.toPx() }

    return citations.mapNotNull { (id, sources) ->
        if (sources.isEmpty()) return@mapNotNull null
        val host = citationHostname(sources.first().url)
        val extra = sources.size - 1
        val extraLabel = if (extra > 0) " +$extra" else ""
        val extraWidth =
            if (extra > 0) {
                measurer.measure(text = extraLabel, style = pillStyle).size.width
            } else {
                0
            }
        val paddingPx = with(density) { PillHorizontalPadding.toPx() * 2 }
        val hostBudget = (maxHostPx - extraWidth - paddingPx).toInt().coerceAtLeast(0)
        val hostWidth =
            measurer.measure(
                text = host,
                style = pillStyle,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                constraints = Constraints(maxWidth = hostBudget),
            ).size.width
        val widthSp = with(density) { (hostWidth + extraWidth + paddingPx).toSp() }
        val interaction = interactions[id] ?: return@mapNotNull null
        val key = "cite-$id"
        key to
            InlineTextContent(
                placeholder =
                    Placeholder(
                        width = widthSp,
                        height = placeholderHeight,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                    ),
            ) {
                CitationPill(
                    sources = sources,
                    expanded = openId == id,
                    interactionSource = interaction,
                )
            }
    }.toMap()
}

@Composable
private fun CitationHitTarget(
    box: Rect,
    widthPx: Int,
    heightPx: Int,
    yShift: Float,
    sources: List<AppCitation>,
    expanded: Boolean,
    pageIndex: Int,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) {
    val density = LocalDensity.current
    val haptics = rememberAppHaptics()
    val minPx = with(density) { PillTouchTarget.roundToPx() }
    val targetWidth = max(minPx, max(box.width.roundToInt(), widthPx))
    val targetHeight = max(minPx, max(box.height.roundToInt(), heightPx))
    val left = box.left.roundToInt()
    val top = (box.center.y - targetHeight / 2f + yShift).roundToInt()
    val host = citationHostname(sources.first().url)
    val extra = sources.size - 1
    val spoken =
        when {
            extra <= 0 -> host
            extra == 1 -> "$host and 1 more source"
            else -> "$host and $extra more sources"
        }
    val showing = sources.getOrNull(pageIndex)?.displayTitle() ?: sources.first().displayTitle()
    val clickLabel = if (expanded) "Hide sources" else "Show sources"

    Box(
        modifier =
            Modifier
                .offset { IntOffset(left, top) }
                .size(
                    width = with(density) { targetWidth.toDp() },
                    height = with(density) { targetHeight.toDp() },
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Button,
                    onClickLabel = clickLabel,
                    onClick = {
                        haptics.click()
                        onClick()
                    },
                )
                .semantics {
                    contentDescription = spoken
                    if (expanded) stateDescription = "Showing $showing"
                },
    )
}

@Composable
private fun CitationPill(
    sources: List<AppCitation>,
    expanded: Boolean,
    interactionSource: MutableInteractionSource,
) {
    val host = citationHostname(sources.first().url)
    val extra = sources.size - 1
    val container =
        if (expanded) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    val hostColor =
        if (expanded) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    val extraColor =
        if (expanded) {
            MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Box(
        modifier =
            Modifier
                .pressScale(interactionSource = interactionSource)
                .clearAndSetSemantics { },
        contentAlignment = Alignment.Center,
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Row(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(PillCorner))
                        .background(container)
                        .indication(interactionSource, LocalIndication.current)
                        .padding(horizontal = PillHorizontalPadding, vertical = PillVerticalPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = host,
                    modifier = Modifier.weight(1f, fill = false),
                    style = MaterialTheme.typography.labelSmall,
                    color = hostColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (extra > 0) {
                    Text(
                        text = " +$extra",
                        style = MaterialTheme.typography.labelSmall,
                        color = extraColor,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun CitationSourceCard(
    sources: List<AppCitation>,
    index: Int,
    onIndexChange: (Int) -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = rememberAppHaptics()
    val reducedMotion = rememberReducedMotion()
    val motionQuick = AppTheme.motion.quick
    val disabledChevron = MaterialTheme.colorScheme.onSurface.copy(alpha = DisabledChevronAlpha)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(SourceCardCorner))
                .border(SourceHairline, MaterialTheme.colorScheme.outline, RoundedCornerShape(SourceCardCorner))
                .background(MaterialTheme.colorScheme.surface)
                .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        if (sources.size > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = {
                        haptics.tick()
                        onIndexChange((index - 1).coerceAtLeast(0))
                    },
                    enabled = index > 0,
                    colors = IconButtonDefaults.iconButtonColors(disabledContentColor = disabledChevron),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous source",
                    )
                }
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Text(
                        text = "${index + 1} of ${sources.size}",
                        modifier =
                            Modifier.semantics {
                                contentDescription = "Source ${index + 1} of ${sources.size}"
                            },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(
                    onClick = {
                        haptics.tick()
                        onIndexChange((index + 1).coerceAtMost(sources.lastIndex))
                    },
                    enabled = index < sources.lastIndex,
                    colors = IconButtonDefaults.iconButtonColors(disabledContentColor = disabledChevron),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next source",
                    )
                }
            }
        }

        AnimatedContent(
            targetState = index,
            transitionSpec = {
                val duration = if (reducedMotion) 0 else motionQuick
                fadeIn(tween(duration)) togetherWith fadeOut(tween(duration))
            },
            label = "citation-source",
        ) { page ->
            val current = sources[page]
            CitationSourceBody(
                source = current,
                host = citationHostname(current.url),
                onOpenUrl = onOpenUrl,
            )
        }
    }
}

@Composable
private fun CitationSourceBody(
    source: AppCitation,
    host: String,
    onOpenUrl: (String) -> Unit,
) {
    val haptics = rememberAppHaptics()
    val title = source.displayTitle()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.spacing.md,
                    top = AppTheme.spacing.md,
                    end = AppTheme.spacing.md,
                    bottom = AppTheme.spacing.sm,
                ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs),
    ) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(
                text = source.url.ifBlank { host },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (source.description.isNotBlank()) {
            Text(
                text = source.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
        val quote = source.quote
        if (!quote.isNullOrBlank()) {
            Row(
                modifier =
                    Modifier
                        .padding(top = AppTheme.spacing.xs)
                        .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            ) {
                Box(
                    modifier =
                        Modifier
                            .width(QuoteBarWidth)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(QuoteBarWidth))
                            .background(MaterialTheme.colorScheme.outline),
                )
                Text(
                    text = quote,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        TextButton(
            onClick = {
                haptics.click()
                onOpenUrl(source.url)
            },
            modifier = Modifier.pressScale(interactionSource = interactionSource),
            interactionSource = interactionSource,
        ) {
            Text(text = "Open source")
        }
    }
}

private fun AppCitation.displayTitle(): String = title.ifBlank { citationHostname(url) }

internal fun citationHostname(url: String): String {
    val parsed = Uri.parse(url)
    val host = parsed.host ?: Uri.parse("https://$url").host
    return host?.removePrefix("www.") ?: url
}

internal fun normalizeCitationText(text: String): String = text.replace("{cite}", "[1]")

internal sealed class CitedSpan {
    data class Words(val value: String) : CitedSpan()

    data class Mark(val id: Int) : CitedSpan() {
        val key: String get() = "cite-$id"
    }
}

internal fun splitCitedText(text: String): List<CitedSpan> {
    val out = mutableListOf<CitedSpan>()
    var last = 0
    MarkerRegex.findAll(text).forEach { match ->
        if (match.range.first > last) {
            out += CitedSpan.Words(text.substring(last, match.range.first))
        }
        val id = match.groupValues[1].toInt()
        out += CitedSpan.Mark(id)
        last = match.range.last + 1
    }
    if (last < text.length) out += CitedSpan.Words(text.substring(last))
    return out
}
