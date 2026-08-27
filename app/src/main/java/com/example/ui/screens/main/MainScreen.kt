package com.example.ui.screens.main

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.SoulExpHistoryEngine
import com.example.data.engine.SoulResonanceData
import com.example.data.local.DailyTrialEntity
import com.example.data.local.EvaluationRecordEntity
import com.example.data.local.EvolutionEventEntity
import com.example.data.model.DailyQuest
import com.example.data.model.DailyQuestState
import com.example.data.model.SoulIdentity
import com.example.ui.components.AstralEmotionalState
import com.example.ui.components.DailyEmotionalCheckInCard
import com.example.ui.components.DailyQuestsCard
import com.example.ui.components.IdentityHeader
import com.example.ui.components.OracleMessageCard
import com.example.ui.components.SanctuaryLibraryCard
import com.example.ui.components.SinVirtueBalanceVisualizer
import com.example.ui.components.SoulExpTimelineChart
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.ScreenTab

@Composable
fun MainScreen(
    soul: SoulIdentity,
    trials: List<DailyTrialEntity>,
    records: List<EvaluationRecordEntity> = emptyList(),
    events: List<EvolutionEventEntity> = emptyList(),
    resonance: SoulResonanceData? = null,
    questState: DailyQuestState? = null,
    onQuestClick: (DailyQuest) -> Unit = {},
    onClaimQuestBonus: () -> Unit = {},
    checkedInEmotion: String? = null,
    isCheckedInToday: Boolean = false,
    onDailyCheckIn: (AstralEmotionalState) -> Unit = {},
    hasClaimableAchievements: Boolean = false,
    onNavigate: (ScreenTab) -> Unit,
    onOpenRecord: () -> Unit = {},
    onOpenLibrary: () -> Unit = {},
    onArchiveCurrentEvolution: (String?) -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onOpenAchievements: () -> Unit = {},
    onOpenWardrobe: () -> Unit = {},
    onOpenArchetypes: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val expTimelineStats = remember(soul, records, events) {
        SoulExpHistoryEngine.generate30DayTimeline(soul, records, events)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("main_screen_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Applet Banner Header: Title on Left, Non-Overlapping Shards Pill & Action Icons on Right
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and subtitle with flexible constraint
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "GODLY SYSTEM",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Arcane Personality Evaluation",
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialAmethystLight,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Top Right Action Cluster: Independent, spacious, non-overlapping controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Soul Shards Pill (Clickable -> Wardrobe)
                    Surface(
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(onClick = onOpenWardrobe)
                            .testTag("top_bar_shards_chip"),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, EtherealCyan.copy(alpha = 0.45f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "💎",
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${soul.soulShards}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EtherealCyan
                            )
                        }
                    }

                    // Achievements Button with cleanly positioned badge indicator
                    Box(
                        modifier = Modifier.size(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .clickable(onClick = onOpenAchievements)
                                .testTag("top_bar_achievements_button"),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape,
                            border = BorderStroke(
                                1.dp,
                                if (hasClaimableAchievements) RadiantGoldBright.copy(alpha = 0.8f) else SurfaceCardBorder
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Achievements",
                                    tint = RadiantGoldBright,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Claimable Indicator Badge
                        if (hasClaimableAchievements) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(RadiantGoldBright)
                                    .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            )
                        }
                    }

                    // Settings Button
                    Surface(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onOpenSettings)
                            .testTag("top_bar_settings_button"),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, SurfaceCardBorder)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 1st Card: Current Identity Card with Soul Resonance Metric & Milestone Badges
        item {
            IdentityHeader(
                soul = soul,
                resonance = resonance,
                onOpenArchetypes = onOpenArchetypes
            )
        }

        // 2nd Card: SANCTUARY VAULT (Strict requirement: Keep the sanctuary vault on 2nd card on sanctuary tab)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SANCTUARY VAULT",
                            style = MaterialTheme.typography.labelMedium,
                            color = RadiantGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "ASTRAL PORTALS",
                            fontSize = 9.5.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SanctuaryIconButton(
                            label = "Record",
                            icon = Icons.Default.Flare,
                            glowColor = Color(0xFFF87171),
                            onClick = onOpenRecord,
                            modifier = Modifier.weight(1f),
                            testTag = "portal_record_button"
                        )

                        SanctuaryIconButton(
                            label = "Soul Matrix",
                            icon = Icons.Default.SelfImprovement,
                            glowColor = RadiantGold,
                            onClick = { onNavigate(ScreenTab.SOUL) },
                            modifier = Modifier.weight(1f),
                            testTag = "portal_soul_button"
                        )

                        SanctuaryIconButton(
                            label = "Library",
                            icon = Icons.Default.AutoStories,
                            glowColor = EtherealCyan,
                            onClick = onOpenLibrary,
                            modifier = Modifier.weight(1f),
                            testTag = "portal_library_button"
                        )

                        SanctuaryIconButton(
                            label = "Wardrobe",
                            icon = Icons.Default.AutoAwesome,
                            glowColor = CelestialAmethystLight,
                            onClick = onOpenWardrobe,
                            modifier = Modifier.weight(1f),
                            testTag = "portal_wardrobe_button"
                        )
                    }
                }
            }
        }

        // 3rd Card: Sanctuary Library Timeline (View Past Godly Evolutions & Save Snapshot)
        item {
            SanctuaryLibraryCard(
                soul = soul,
                events = events,
                onArchiveCurrentEvolution = onArchiveCurrentEvolution,
                onOpenFullLibrary = onOpenLibrary
            )
        }

        // Daily Soul Quests Card (Reflective tasks to earn Soul Experience)
        if (questState != null) {
            item {
                DailyQuestsCard(
                    questState = questState,
                    onQuestClick = onQuestClick,
                    onClaimBonus = onClaimQuestBonus
                )
            }
        }

        // 3rd Card: Daily Emotional Check-In Sequence
        item {
            DailyEmotionalCheckInCard(
                isCompletedToday = isCheckedInToday,
                checkedInEmotion = checkedInEmotion,
                onCheckIn = onDailyCheckIn
            )
        }

        // 4th Card: Daily Mysterious & Encouraging Oracle Message (Influenced by Emotional State)
        item {
            OracleMessageCard(
                soul = soul,
                checkedInEmotion = checkedInEmotion
            )
        }

        // 5th Card: Sin vs Virtue Balance Visualizer (Radar Chart & Bar Graph toggle)
        item {
            SinVirtueBalanceVisualizer(
                records = records,
                soul = soul
            )
        }

        // 6th Card: 30-Day Soul Experience Progress Timeline Data Visualization (Recharts-inspired dynamic curve)
        item {
            SoulExpTimelineChart(stats = expTimelineStats)
        }

        // 7th Card: System Omen / Mysterious Message Ticker
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF120C22))
                    .border(
                        1.dp,
                        Brush.horizontalGradient(
                            listOf(
                                CelestialAmethyst.copy(alpha = 0.4f),
                                SurfaceCardBorder
                            )
                        ),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "👁️", fontSize = 18.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "SYSTEM OBSERVATION",
                            fontSize = 10.sp,
                            color = CelestialAmethystLight,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "« ${soul.systemMessage} »",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }
        }

        // Footer Mystery Note
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "« The System evaluates patterns, not moral worth. Every choice leaves an astral imprint. »",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontFamily = FontFamily.Serif,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SanctuaryIconButton(
    label: String,
    icon: ImageVector,
    glowColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(glowColor.copy(alpha = 0.15f))
                .border(1.2.dp, glowColor.copy(alpha = 0.55f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = glowColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontSize = 11.5.sp
        )
    }
}
