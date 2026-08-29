package com.example.ui.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.SoulExpHistoryEngine
import com.example.data.local.EvaluationRecordEntity
import com.example.data.local.EvolutionEventEntity
import com.example.data.model.SoulIdentity
import com.example.ui.components.SinVirtueBalanceVisualizer
import com.example.ui.components.SoulExpTimelineChart
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
 * Unified timeline item combining Evolution Events and Daily Evaluation Inputs
 * in a strict chronological sequence.
 */
sealed class TimelineEntry(val timestamp: Long) {
    data class Evolution(val event: EvolutionEventEntity) : TimelineEntry(event.timestamp)
    data class Record(val record: EvaluationRecordEntity) : TimelineEntry(record.timestamp)
}

@Composable
fun HistoryScreen(
    events: List<EvolutionEventEntity>,
    records: List<EvaluationRecordEntity>,
    soul: SoulIdentity? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: EXP Dynamics, 1: Chronology, 2: Evolutions, 3: Daily Inputs
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "METAMORPHOSIS", "AWAKENING", "INPUTS"

    // Generate 30-Day Soul EXP Progression Timeline
    val expTimelineStats = remember(soul, records, events) {
        if (soul != null) {
            SoulExpHistoryEngine.generate30DayTimeline(soul, records, events)
        } else null
    }

    // Merge and sort all entries chronologically (newest first)
    val unifiedChronology = remember(events, records) {
        val list = mutableListOf<TimelineEntry>()
        events.forEach { list.add(TimelineEntry.Evolution(it)) }
        records.forEach { list.add(TimelineEntry.Record(it)) }
        list.sortedByDescending { it.timestamp }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("history_screen_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("history_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column {
                    Text(
                        text = "SOUL HISTORY",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Chronicle of Persona Evolutions & Identity Shifts",
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialAmethystLight,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Persona Evolution Metric Summary Banner
        item {
            SoulHistorySummaryCard(
                eventsCount = events.size,
                recordsCount = records.size,
                soul = soul
            )
        }

        // Tabs Selection
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "EXP DYNAMICS (30D)",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.5.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "CHRONOLOGY (${unifiedChronology.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.5.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "EVOLUTIONS (${events.size})",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.5.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = {
                        Text(
                            text = "INPUTS (${records.size})",
                            fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.5.sp
                        )
                    }
                )
            }
        }

        // Tab Specific Chronological Content
        when (selectedTab) {
            0 -> {
                // Recharts Data Visualization: 30-Day Soul Experience Progress
                if (expTimelineStats != null) {
                    item {
                        SoulExpTimelineChart(stats = expTimelineStats)
                    }
                }

                // Sin & Virtue Balance Chart (Radar / Bar graph visualization)
                item {
                    SinVirtueBalanceVisualizer(
                        records = records,
                        soul = soul
                    )
                }
            }
            1 -> {
                // Unified Chronological Stream
                if (unifiedChronology.isEmpty()) {
                    item {
                        EmptyHistoryCard(
                            message = "The chronicle is silent. Record your daily choices or resolve trials to trigger evolutionary mutations in your vessel."
                        )
                    }
                } else {
                    items(unifiedChronology) { entry ->
                        when (entry) {
                            is TimelineEntry.Evolution -> {
                                EvolutionMilestoneCard(event = entry.event)
                            }
                            is TimelineEntry.Record -> {
                                DailyInputHistoryCard(record = entry.record)
                            }
                        }
                    }
                }
            }
            2 -> {
                // Evolutions Only
                if (events.isEmpty()) {
                    item {
                        EmptyHistoryCard(
                            message = "No major persona evolutions recorded yet. Daily inputs and dilemma choices gradually shift your dominant shadows and virtues to trigger metamorphosis."
                        )
                    }
                } else {
                    items(events) { event ->
                        EvolutionMilestoneCard(event = event)
                    }
                }
            }
            3 -> {
                // Daily Input Records Only
                if (records.isEmpty()) {
                    item {
                        EmptyHistoryCard(
                            message = "No daily input logs recorded yet. Begin your self-evaluation in the Record Sanctuary."
                        )
                    }
                } else {
                    items(records) { record ->
                        DailyInputHistoryCard(record = record)
                    }
                }
            }
        }

        // Bottom Footer Note
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "« Every recorded thought shifts the equilibrium of the soul. »",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

/**
 * Metric summary card displaying current persona and total shifts.
 */
@Composable
private fun SoulHistorySummaryCard(
    eventsCount: Int,
    recordsCount: Int,
    soul: SoulIdentity?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VESSEL EVOLUTION METRICS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                if (soul != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${soul.race} • ${soul.advancedClass ?: soul.className}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HistoryMetricItem(
                    count = eventsCount.toString(),
                    label = "Evolutions",
                    icon = "🧬"
                )
                HorizontalDivider(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp),
                    color = SurfaceCardBorder
                )
                HistoryMetricItem(
                    count = recordsCount.toString(),
                    label = "Inputs Logged",
                    icon = "📜"
                )
                HorizontalDivider(
                    modifier = Modifier
                        .height(28.dp)
                        .width(1.dp),
                    color = SurfaceCardBorder
                )
                HistoryMetricItem(
                    count = soul?.currentTitle ?: "The Seeker",
                    label = "Current Title",
                    icon = "👑",
                    isText = true
                )
            }
        }
    }
}

@Composable
private fun HistoryMetricItem(
    count: String,
    label: String,
    icon: String,
    isText: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 13.sp)
            Text(
                text = count,
                fontSize = if (isText) 11.sp else 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Detailed Evolution & Metamorphosis milestone card.
 */
@Composable
private fun EvolutionMilestoneCard(
    event: EvolutionEventEntity
) {
    var isExpanded by remember { mutableStateOf(false) }
    val dateStr = remember(event.timestamp) {
        SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(event.timestamp))
    }

    val badgeColor = when {
        event.isUnknownEvent -> Color(0xFFC084FC) // Violet for secret/mythic anomaly
        event.eventType.contains("METAMORPHOSIS") || event.eventType.contains("RACE") -> RadiantGold
        event.eventType.contains("TRAIT") -> EtherealCyan
        event.eventType.contains("GENESIS") -> Color(0xFF60A5FA)
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (event.isUnknownEvent) Color(0xFF1E1035)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .border(
                1.dp,
                if (event.isUnknownEvent) CelestialAmethyst else SurfaceCardBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable { isExpanded = !isExpanded }
            .padding(14.dp)
            .testTag("evolution_item_${event.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Milestone Rune Node
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(badgeColor.copy(alpha = 0.15f))
                        .border(1.2.dp, badgeColor.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = event.runeIcon, fontSize = 20.sp)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DAY ${event.dayNumber} • ${event.eventType.replace('_', ' ')}",
                            fontSize = 10.sp,
                            color = badgeColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = dateStr,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand details",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Expanded Insight Drawer
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0C0916))
                        .border(0.8.dp, SurfaceCardBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "👁️", fontSize = 12.sp)
                        Text(
                            text = "SYSTEM OBSERVATION & METAMORPHIC ANALYSIS",
                            fontSize = 9.sp,
                            color = CelestialAmethystLight,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "This evolutionary shift occurred as your behavioral choices resonated with extreme force thresholds, transmuting the physical vessel and unlocking new latent attributes.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

/**
 * Detailed Daily Input record card displaying situation, action, reflection, and force deltas.
 */
@Composable
private fun DailyInputHistoryCard(
    record: EvaluationRecordEntity
) {
    var isExpanded by remember { mutableStateOf(false) }
    val dateStr = remember(record.timestamp) {
        SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(record.timestamp))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(14.dp)
            .testTag("record_item_${record.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Header Row: Catalyst Emotion + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(RadiantGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚡", fontSize = 12.sp)
                    }
                    Text(
                        text = "INPUT: ${record.emotion.uppercase()}",
                        fontSize = 10.sp,
                        color = RadiantGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = dateStr,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Situation & Action Summary
            Text(
                text = "« ${record.situation} »",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Action: ${record.action}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )

            // System Analysis Snippet
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0D0A18))
                    .padding(8.dp)
            ) {
                Text(
                    text = record.analysisInsight,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            }

            // Expandable full details
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (record.intention.isNotBlank()) {
                        Text(
                            text = "Intention: ${record.intention}",
                            fontSize = 11.sp,
                            color = CelestialAmethystLight
                        )
                    }

                    if (record.reflection.isNotBlank()) {
                        Text(
                            text = "Reflection: « ${record.reflection} »",
                            fontSize = 11.sp,
                            color = EtherealCyan
                        )
                    }

                    // Stat Shifts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Humanity Shift: ${if (record.humanityShift >= 0) "+${record.humanityShift}" else "${record.humanityShift}"}",
                            fontSize = 10.sp,
                            color = if (record.humanityShift >= 0) EtherealCyan else Color(0xFFF87171)
                        )
                        Text(
                            text = "Stability Shift: ${if (record.stabilityShift >= 0) "+${record.stabilityShift}" else "${record.stabilityShift}"}",
                            fontSize = 10.sp,
                            color = if (record.stabilityShift >= 0) Color(0xFF34D399) else Color(0xFFF87171)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
