package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.EvolutionEventEntity
import com.example.data.model.SoulIdentity
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Sanctuary Library Card displayed directly in the Sanctuary Tab.
 * Allows users to view past Godly identity evolutions as a timeline
 * and save current Godly evolution snapshots into the chronicle.
 */
@Composable
fun SanctuaryLibraryCard(
    soul: SoulIdentity,
    events: List<EvolutionEventEntity>,
    onArchiveCurrentEvolution: (note: String?) -> Unit,
    onOpenFullLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showArchiveDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "EVOLUTIONS", "ARCHIVES", "AWAKENINGS"
    var expandedEventId by remember { mutableStateOf<Long?>(null) }

    if (showArchiveDialog) {
        ArchiveEvolutionDialog(
            soul = soul,
            onConfirm = { note ->
                onArchiveCurrentEvolution(note)
                showArchiveDialog = false
            },
            onDismiss = { showArchiveDialog = false }
        )
    }

    val filteredEvents = remember(events, selectedFilter) {
        when (selectedFilter) {
            "EVOLUTIONS" -> events.filter { it.eventType == "METAMORPHOSIS" || it.eventType == "GENESIS" || it.eventType == "RACE_MUTATION" }
            "ARCHIVES" -> events.filter { it.eventType == "GODLY_ARCHIVE" }
            "AWAKENINGS" -> events.filter { it.eventType == "AWAKENING" || it.eventType == "TRAIT_AWAKENED" }
            else -> events
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(20.dp))
            .testTag("sanctuary_library_card"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Title, Subtitle, and Archive Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CelestialAmethyst.copy(alpha = 0.25f))
                            .border(1.dp, RadiantGold.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = "Sanctuary Library",
                            tint = RadiantGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "SANCTUARY LIBRARY",
                            style = MaterialTheme.typography.titleMedium,
                            color = RadiantGoldBright,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Historical Logs • ${events.size} Chronicles",
                            style = MaterialTheme.typography.bodySmall,
                            color = CelestialAmethystLight,
                            fontSize = 11.5.sp
                        )
                    }
                }

                // Quick Snapshot Button
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showArchiveDialog = true }
                        .testTag("save_godly_evolution_button"),
                    color = RadiantGold.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, RadiantGold.copy(alpha = 0.65f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Archive Evolution",
                            tint = RadiantGoldBright,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Archive State",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RadiantGoldBright
                        )
                    }
                }
            }

            // Current Godly Identity Snapshot Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F0B1C))
                    .border(1.dp, CelestialAmethyst.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "CURRENT VESSEL STATE",
                            fontSize = 9.5.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${soul.race} • ${soul.className}",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "« ${soul.currentTitle} »",
                            fontSize = 11.5.sp,
                            color = EtherealCyan
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(0.8.dp, SurfaceCardBorder)
                        ) {
                            Text(
                                text = soul.alignment,
                                fontSize = 10.5.sp,
                                color = RadiantGold,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = "Harmonics: ${soul.humanity}% H • ${soul.stability}% S",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // Filter Tabs Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LibraryFilterChip(
                    label = "All (${events.size})",
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" }
                )
                LibraryFilterChip(
                    label = "Evolutions",
                    selected = selectedFilter == "EVOLUTIONS",
                    onClick = { selectedFilter = "EVOLUTIONS" }
                )
                LibraryFilterChip(
                    label = "Archives",
                    selected = selectedFilter == "ARCHIVES",
                    onClick = { selectedFilter = "ARCHIVES" }
                )
                LibraryFilterChip(
                    label = "Awakenings",
                    selected = selectedFilter == "AWAKENINGS",
                    onClick = { selectedFilter = "AWAKENINGS" }
                )
            }

            // Historical Logs Timeline
            if (filteredEvents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No historical logs match this filter.\nArchive your current Godly identity to log an epoch.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // Show up to 4 most recent entries inline, with button to expand full library
                    filteredEvents.take(4).forEachIndexed { index, event ->
                        TimelineLogItem(
                            event = event,
                            isLast = index == filteredEvents.take(4).lastIndex,
                            isExpanded = expandedEventId == event.id,
                            onToggleExpand = {
                                expandedEventId = if (expandedEventId == event.id) null else event.id
                            }
                        )
                    }
                }
            }

            // Expand Full Chronicle Button
            if (events.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onOpenFullLibrary)
                        .testTag("open_library_dialog_button"),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = "Full Timeline",
                            tint = EtherealCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Explore Full Sanctuary Codex (${events.size} Epochs)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EtherealCyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        color = if (selected) CelestialAmethyst.copy(alpha = 0.35f) else Color(0xFF130E22),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            if (selected) RadiantGold.copy(alpha = 0.8f) else SurfaceCardBorder
        )
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) RadiantGoldBright else TextMuted,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
        )
    }
}

/**
 * Individual Timeline Log Node in the Sanctuary Library
 */
@Composable
fun TimelineLogItem(
    event: EvolutionEventEntity,
    isLast: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateStr = remember(event.timestamp) {
        SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(event.timestamp))
    }

    val typeBadgeColor = when (event.eventType) {
        "METAMORPHOSIS", "RACE_MUTATION" -> RadiantGold
        "GODLY_ARCHIVE" -> EtherealCyan
        "AWAKENING", "TRAIT_AWAKENED" -> Color(0xFFF472B6)
        "GENESIS" -> CelestialAmethystLight
        else -> RadiantGoldBright
    }

    val typeLabel = when (event.eventType) {
        "METAMORPHOSIS", "RACE_MUTATION" -> "EVOLUTION"
        "GODLY_ARCHIVE" -> "SAVED ARCHIVE"
        "AWAKENING", "TRAIT_AWAKENED" -> "AWAKENING"
        "GENESIS" -> "GENESIS"
        else -> event.eventType
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleExpand)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Vertical Timeline Column: Rune Node + Glowing Connecting Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            // Node Rune Circle
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF140D24))
                    .border(
                        1.2.dp,
                        Brush.radialGradient(listOf(typeBadgeColor, Color(0xFF2D1B4E))),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = event.runeIcon,
                    fontSize = 16.sp
                )
            }

            // Connecting vertical line if not the last item
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(if (isExpanded) 110.dp else 44.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    typeBadgeColor.copy(alpha = 0.6f),
                                    SurfaceCardBorder.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
            }
        }

        // Event Log Content Card
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isExpanded) Color(0xFF171028) else Color(0xFF110B1E))
                .border(
                    1.dp,
                    if (isExpanded) typeBadgeColor.copy(alpha = 0.5f) else SurfaceCardBorder.copy(alpha = 0.6f),
                    RoundedCornerShape(12.dp)
                )
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Event Type Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = typeBadgeColor.copy(alpha = 0.15f),
                    border = BorderStroke(0.8.dp, typeBadgeColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = typeLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = typeBadgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "Day ${event.dayNumber} • $dateStr",
                    fontSize = 9.5.sp,
                    color = TextMuted
                )
            }

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                fontSize = 11.5.sp,
                lineHeight = 16.sp
            )

            // Expand / Collapse indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Less" else "Details",
                    fontSize = 10.sp,
                    color = CelestialAmethystLight
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = CelestialAmethystLight,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * Full-screen Sanctuary Library Dialog with search, timeline inspection, and filtering
 */
@Composable
fun SanctuaryLibraryDialog(
    soul: SoulIdentity,
    events: List<EvolutionEventEntity>,
    onArchiveCurrentEvolution: (note: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }
    var expandedEventId by remember { mutableStateOf<Long?>(null) }
    var showArchiveDialog by remember { mutableStateOf(false) }

    if (showArchiveDialog) {
        ArchiveEvolutionDialog(
            soul = soul,
            onConfirm = { note ->
                onArchiveCurrentEvolution(note)
                showArchiveDialog = false
            },
            onDismiss = { showArchiveDialog = false }
        )
    }

    val filteredList = remember(events, selectedFilter, searchQuery) {
        var list = when (selectedFilter) {
            "EVOLUTIONS" -> events.filter { it.eventType == "METAMORPHOSIS" || it.eventType == "GENESIS" || it.eventType == "RACE_MUTATION" }
            "ARCHIVES" -> events.filter { it.eventType == "GODLY_ARCHIVE" }
            "AWAKENINGS" -> events.filter { it.eventType == "AWAKENING" || it.eventType == "TRAIT_AWAKENED" }
            else -> events
        }
        if (searchQuery.isNotBlank()) {
            list = list.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true) ||
                it.eventType.contains(searchQuery, ignoreCase = true)
            }
        }
        list
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, SurfaceCardBorder, RoundedCornerShape(24.dp))
                .testTag("sanctuary_library_dialog"),
            color = SurfaceCardElevated
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Dialog Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(CelestialAmethyst.copy(alpha = 0.25f))
                                .border(1.5.dp, RadiantGoldBright, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = "Library Codex",
                                tint = RadiantGoldBright,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "SANCTUARY CODEX",
                                style = MaterialTheme.typography.titleLarge,
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Chronicle of Godly Evolutions & Epochs",
                                style = MaterialTheme.typography.bodySmall,
                                color = CelestialAmethystLight
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_library_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextPrimary
                        )
                    }
                }

                // Search Bar & Quick Archive Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("library_search_input"),
                        placeholder = { Text("Search logs, titles, traits...", fontSize = 12.sp, color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextMuted, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RadiantGold,
                            unfocusedBorderColor = SurfaceCardBorder,
                            focusedContainerColor = Color(0xFF0F0A1D),
                            unfocusedContainerColor = Color(0xFF0F0A1D),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Button(
                        onClick = { showArchiveDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RadiantGold.copy(alpha = 0.2f),
                            contentColor = RadiantGoldBright
                        ),
                        border = BorderStroke(1.dp, RadiantGold.copy(alpha = 0.7f)),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("dialog_archive_state_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = "Archive",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Archive", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Filter Tabs Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LibraryFilterChip(
                        label = "All (${events.size})",
                        selected = selectedFilter == "ALL",
                        onClick = { selectedFilter = "ALL" }
                    )
                    LibraryFilterChip(
                        label = "Evolutions",
                        selected = selectedFilter == "EVOLUTIONS",
                        onClick = { selectedFilter = "EVOLUTIONS" }
                    )
                    LibraryFilterChip(
                        label = "Archives",
                        selected = selectedFilter == "ARCHIVES",
                        onClick = { selectedFilter = "ARCHIVES" }
                    )
                    LibraryFilterChip(
                        label = "Awakenings",
                        selected = selectedFilter == "AWAKENINGS",
                        onClick = { selectedFilter = "AWAKENINGS" }
                    )
                }

                HorizontalDivider(color = SurfaceCardBorder, thickness = 0.8.dp)

                // Timeline List
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No chronicle logs found.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .testTag("library_timeline_list"),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredList, key = { it.id }) { event ->
                            TimelineLogItem(
                                event = event,
                                isLast = event == filteredList.last(),
                                isExpanded = expandedEventId == event.id,
                                onToggleExpand = {
                                    expandedEventId = if (expandedEventId == event.id) null else event.id
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dialog to Archive Current Godly Identity with an optional custom inscription/reflection note.
 */
@Composable
fun ArchiveEvolutionDialog(
    soul: SoulIdentity,
    onConfirm: (note: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var customNote by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .border(1.2.dp, RadiantGold.copy(alpha = 0.8f), RoundedCornerShape(22.dp)),
            color = SurfaceCardElevated
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(RadiantGold.copy(alpha = 0.2f))
                            .border(1.dp, RadiantGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = null,
                            tint = RadiantGoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "ARCHIVE GODLY IDENTITY",
                            style = MaterialTheme.typography.titleMedium,
                            color = RadiantGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Preserve current vessel state into Sanctuary Library",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextAmethyst
                        )
                    }
                }

                // Current vessel summary box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF100B1F))
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Vessel: ${soul.race} • ${soul.className} [${soul.alignment}]",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Title: « ${soul.currentTitle} »",
                            fontSize = 11.5.sp,
                            color = EtherealCyan
                        )
                        Text(
                            text = "Humanity: ${soul.humanity}% • Stability: ${soul.stability}% • Dominant: ${soul.dominantShadow.displayName} & ${soul.dominantVirtue.displayName}",
                            fontSize = 10.5.sp,
                            color = TextMuted
                        )
                    }
                }

                OutlinedTextField(
                    value = customNote,
                    onValueChange = { customNote = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("archive_note_input"),
                    label = { Text("Archival Inscription (Optional)", color = TextMuted, fontSize = 12.sp) },
                    placeholder = { Text("Enter a reflection or solemn decree for this epoch...", color = TextMuted.copy(alpha = 0.6f), fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Color(0xFF0D0819),
                        unfocusedContainerColor = Color(0xFF0D0819),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }

                    Button(
                        onClick = { onConfirm(customNote.ifBlank { null }) },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(44.dp)
                            .testTag("confirm_archive_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RadiantGold,
                            contentColor = Color(0xFF1E1303)
                        )
                    ) {
                        Text("✦ Inscribe Archive", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
