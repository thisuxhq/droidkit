package com.droidkit.registry.components

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidkit.registry.foundation.pressScale
import com.droidkit.registry.foundation.rememberAppHaptics
import com.droidkit.registry.foundation.rememberReducedMotion
import com.droidkit.registry.theme.AppTheme

private val PillHorizontalPadding = 8.dp
private val PillVerticalPadding = 2.dp
private val PillCorner = 8.dp
private val QuoteBarWidth = 2.dp
private val SourceHairline = 1.dp
private val SourceCardCorner = 12.dp
private val QuoteBarHeight = 24.dp
private val DefaultLineHeight = 24.sp

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

/**
 * A paragraph that can name its sources without becoming a footnote.
 *
 * Drop `{cite}` or `[1]` where the claim is, pass [sources], and a hostname pill sits in the
 * line. Extra sources collapse to +N. Tap the pill and the source opens under the paragraph;
 * tap again to close. Multiple groups use `[1]`, `[2]` with the map overload.
 *
 * see:      the pill is a hostname, plus +N when more sources wait behind it.
 * reach:    pressScale + click; the visual stays line-sized, the tap target is 48 dp.
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
    val reducedMotion = rememberReducedMotion()
    var openId by rememberSaveable { mutableStateOf(expandedCitation) }
    var sourceIndex by rememberSaveable { mutableIntStateOf(0) }

    val spans = remember(text) { splitCitedText(normalizeCitationText(text)) }
    val inlineContent =
        citationInlineContent(
            citations = citations,
            openId = openId,
            style = style,
            onToggle = { id ->
                if (openId == id) {
                    openId = null
                } else {
                    openId = id
                    sourceIndex = 0
                }
            },
        )

    val annotated =
        remember(spans, inlineContent.keys) {
            buildAnnotatedString {
                spans.forEach { span ->
                    when (span) {
                        is CitedSpan.Words -> append(span.value)
                        is CitedSpan.Mark ->
                            if (inlineContent.containsKey(span.key)) {
                                appendInlineContent(span.key, "[${span.id}]")
                            }
                    }
                }
            }
        }

    val rootModifier =
        if (reducedMotion) {
            modifier
        } else {
            modifier.animateContentSize(animationSpec = tween(durationMillis = AppTheme.motion.normal))
        }

    Column(modifier = rootModifier.fillMaxWidth()) {
        Text(
            text = annotated,
            modifier = Modifier.fillMaxWidth(),
            style = style,
            color = MaterialTheme.colorScheme.onSurface,
            inlineContent = inlineContent,
        )
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

/**
 * The pill on its own, for a custom layout that already owns the sentence.
 */
@Composable
fun AppInlineCitation(
    sources: List<AppCitation>,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onOpenUrl: ((String) -> Unit)? = null,
) {
    AppInlineCitation(
        text = "[1]",
        sources = sources,
        modifier = modifier,
        expanded = expanded,
        onOpenUrl = onOpenUrl,
    )
}

@Composable
private fun citationInlineContent(
    citations: Map<Int, List<AppCitation>>,
    openId: Int?,
    style: TextStyle,
    onToggle: (Int) -> Unit,
): Map<String, InlineTextContent> {
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val pillStyle = MaterialTheme.typography.labelSmall
    val placeholderHeight =
        if (style.lineHeight != TextUnit.Unspecified) style.lineHeight else DefaultLineHeight

    return citations.mapNotNull { (id, sources) ->
        if (sources.isEmpty()) return@mapNotNull null
        val host = citationHostname(sources.first().url)
        val extra = sources.size - 1
        val extraLabel = if (extra > 0) " +$extra" else ""
        val hostWidth = measurer.measure(text = host, style = pillStyle).size.width
        val extraWidth =
            if (extra > 0) {
                measurer.measure(text = extraLabel, style = pillStyle).size.width
            } else {
                0
            }
        val widthSp =
            with(density) {
                (hostWidth + extraWidth + PillHorizontalPadding.toPx() * 2).toSp()
            }
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
                    onClick = { onToggle(id) },
                )
            }
    }.toMap()
}

@Composable
private fun CitationPill(
    sources: List<AppCitation>,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val haptics = rememberAppHaptics()
    val host = citationHostname(sources.first().url)
    val extra = sources.size - 1
    val spoken =
        when {
            extra <= 0 -> "Citation, $host"
            extra == 1 -> "Citation, $host and 1 more source"
            else -> "Citation, $host and $extra more sources"
        }
    val clickLabel = if (expanded) "Hide sources" else "Show sources"
    val showing = sources.first().displayTitle()

    Box(
        modifier =
            Modifier
                .wrapContentSize(unbounded = true, align = Alignment.Center)
                .minimumInteractiveComponentSize()
                .pressScale(interactionSource = interactionSource)
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    role = Role.Button,
                    onClickLabel = clickLabel,
                    onClick = {
                        haptics.click()
                        onClick()
                    },
                )
                .semantics(mergeDescendants = true) {
                    contentDescription = spoken
                    role = Role.Button
                    if (expanded) stateDescription = "Showing $showing"
                },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(PillCorner))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = PillHorizontalPadding, vertical = PillVerticalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = host,
                modifier = Modifier.clearAndSetSemantics { },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (extra > 0) {
                Text(
                    text = " +$extra",
                    modifier = Modifier.clearAndSetSemantics { },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
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
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous source",
                    )
                }
                Text(
                    text = "${index + 1} of ${sources.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                IconButton(
                    onClick = {
                        haptics.tick()
                        onIndexChange((index + 1).coerceAtMost(sources.lastIndex))
                    },
                    enabled = index < sources.lastIndex,
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
                if (reducedMotion) {
                    fadeIn(tween(0)) togetherWith fadeOut(tween(0))
                } else {
                    fadeIn(tween(AppTheme.motion.quick)) togetherWith fadeOut(tween(AppTheme.motion.quick))
                }
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
        Text(
            text = source.url.ifBlank { host },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
                modifier = Modifier.padding(top = AppTheme.spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm),
            ) {
                Box(
                    modifier =
                        Modifier
                            .width(QuoteBarWidth)
                            .height(QuoteBarHeight)
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
