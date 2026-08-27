package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyQuest
import com.example.data.model.DailyQuestState
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun DailyQuestsCard(
    questState: DailyQuestState,
    onQuestClick: (DailyQuest) -> Unit,
    onClaimBonus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        RadiantGold.copy(alpha = 0.45f),
                        SurfaceCardBorder
                    )
                ),
                RoundedCornerShape(18.dp)
            )
            .testTag("daily_quests_card"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with Daily Badge and Progress Ratio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(RadiantGold.copy(alpha = 0.2f))
                            .border(1.dp, RadiantGold.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📜", fontSize = 16.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            text = "DAILY SOUL QUESTS",
                            style = MaterialTheme.typography.labelMedium,
                            color = RadiantGoldBright,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Reflective Tasks • Matrix EXP",
                            fontSize = 10.5.sp,
                            color = TextMuted
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Text(
                        text = "${questState.completedCount} / ${questState.totalCount} DONE",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (questState.isAllCompleted) RadiantGoldBright else EtherealCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                LinearProgressIndicator(
                    progress = { questState.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = RadiantGold,
                    trackColor = Color(0xFF161026),
                    strokeCap = StrokeCap.Round
                )
            }

            // Quests List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                questState.quests.forEach { quest ->
                    QuestItemRow(
                        quest = quest,
                        onClick = { onQuestClick(quest) }
                    )
                }
            }

            // All-Completed Celestial Cache Banner
            if (questState.isAllCompleted) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            1.dp,
                            Brush.horizontalGradient(
                                listOf(
                                    RadiantGold.copy(alpha = 0.7f),
                                    EtherealCyan.copy(alpha = 0.5f)
                                )
                            ),
                            RoundedCornerShape(14.dp)
                        ),
                    color = Color(0xFF1E1535),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "👑", fontSize = 20.sp)
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "ASCENSION DAILY CACHE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RadiantGoldBright,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "+${questState.bonusExpReward} EXP & +${questState.bonusShardsReward} 💎 Shards",
                                    fontSize = 10.5.sp,
                                    color = EtherealCyan
                                )
                            }
                        }

                        if (!questState.allCompletedBonusClaimed) {
                            Button(
                                onClick = onClaimBonus,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = RadiantGold),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    text = "CLAIM",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        } else {
                            Surface(
                                color = RadiantGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, RadiantGold.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "CLAIMED ✓",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RadiantGoldBright,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestItemRow(
    quest: DailyQuest,
    onClick: () -> Unit
) {
    val categoryColor = Color(quest.category.colorHex)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("quest_item_${quest.id}"),
        color = if (quest.isCompleted) Color(0xFF130D20) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            if (quest.isCompleted) RadiantGold.copy(alpha = 0.35f) else SurfaceCardBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Status Checkbox Icon
                Icon(
                    imageVector = if (quest.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = if (quest.isCompleted) "Completed" else "Incomplete",
                    tint = if (quest.isCompleted) RadiantGoldBright else categoryColor.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = quest.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (quest.isCompleted) TextPrimary.copy(alpha = 0.85f) else TextPrimary,
                        fontSize = 12.5.sp
                    )
                    Text(
                        text = "${quest.category.rune} ${quest.category.displayName} • ${quest.targetAffinity}",
                        fontSize = 10.sp,
                        color = categoryColor.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Rewards Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    color = RadiantGold.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "+${quest.expReward} EXP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RadiantGoldBright,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = EtherealCyan.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "+${quest.shardsReward} 💎",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EtherealCyan,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
