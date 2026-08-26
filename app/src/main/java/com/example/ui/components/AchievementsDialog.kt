package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Achievement
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.DailyLoginRewardState

@Composable
fun AchievementsDialog(
    achievements: List<Achievement>,
    soulShards: Int,
    dailyLoginState: DailyLoginRewardState = DailyLoginRewardState(),
    onClaimDailyLogin: () -> Unit = {},
    onClaimReward: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val unlockedCount = achievements.count { it.isUnlocked }
    val sortedAchievements = achievements.sortedWith(
        compareByDescending<Achievement> { it.isUnlocked && !it.isClaimed }
            .thenByDescending { it.isUnlocked }
            .thenByDescending { it.currentProgress.toFloat() / it.targetProgress }
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, SurfaceCardBorder, RoundedCornerShape(24.dp))
                .testTag("achievements_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
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
                                .background(RadiantGold.copy(alpha = 0.2f))
                                .border(1.2.dp, RadiantGold.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Achievements",
                                tint = RadiantGoldBright,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "SOUL ACHIEVEMENTS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "$unlockedCount of ${achievements.size} Milestones Conquered",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_achievements_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Summary Stats Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(0.8.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "Current Soul Shards:", fontSize = 11.sp, color = TextMuted)
                        Text(text = "💎 $soulShards", fontSize = 13.sp, color = EtherealCyan, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(CelestialAmethyst.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${(unlockedCount.toFloat() / achievements.size.coerceAtLeast(1) * 100).toInt()}% Done",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CelestialAmethystLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Achievements List & Daily Login Harvest
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // INTEGRATED DAILY LOGIN REWARD (Inside Achievement Section)
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    1.2.dp,
                                    if (!dailyLoginState.isClaimedToday) RadiantGold.copy(alpha = 0.6f) else SurfaceCardBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .testTag("daily_login_harvest_card"),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
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
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.WbSunny,
                                            contentDescription = "Solar Login",
                                            tint = RadiantGoldBright,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "DAILY SOLAR RESONANCE",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RadiantGoldBright,
                                            letterSpacing = 1.sp
                                        )
                                    }
                                    Text(
                                        text = if (dailyLoginState.isClaimedToday) "Claimed Today ✨" else "Ready to Harvest",
                                        fontSize = 10.sp,
                                        color = if (dailyLoginState.isClaimedToday) CelestialAmethystLight else EtherealCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // 7-Day Track Pills
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    dailyLoginState.rewardsList.forEachIndexed { index, reward ->
                                        val dayNum = index + 1
                                        val isPastClaimed = (dayNum < dailyLoginState.streakDay) || (dayNum == dailyLoginState.streakDay && dailyLoginState.isClaimedToday)
                                        val isCurrent = dayNum == dailyLoginState.streakDay && !dailyLoginState.isClaimedToday

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(
                                                        when {
                                                            isCurrent -> RadiantGold.copy(alpha = 0.25f)
                                                            isPastClaimed -> CelestialAmethyst.copy(alpha = 0.2f)
                                                            else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                                                        }
                                                    )
                                                    .border(
                                                        width = if (isCurrent) 1.5.dp else 0.8.dp,
                                                        color = when {
                                                            isCurrent -> RadiantGoldBright
                                                            isPastClaimed -> CelestialAmethyst
                                                            else -> SurfaceCardBorder
                                                        },
                                                        shape = RoundedCornerShape(8.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isPastClaimed) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Claimed",
                                                        tint = RadiantGoldBright,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                } else {
                                                    Text(
                                                        text = "+$reward",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isCurrent) RadiantGoldBright else TextMuted
                                                    )
                                                }
                                            }
                                            Text(
                                                text = "D$dayNum",
                                                fontSize = 9.sp,
                                                color = if (isCurrent) RadiantGoldBright else TextMuted,
                                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }

                                // Claim Button
                                Button(
                                    onClick = onClaimDailyLogin,
                                    enabled = !dailyLoginState.isClaimedToday,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = RadiantGoldBright,
                                        contentColor = Color.Black,
                                        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                        disabledContentColor = TextMuted
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("claim_daily_resonance_button")
                                ) {
                                    if (!dailyLoginState.isClaimedToday) {
                                        Text(
                                            text = "Claim Day ${dailyLoginState.streakDay} Resonance (+${dailyLoginState.todayRewardShards} 💎)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else {
                                        Text(
                                            text = "✓ Day ${dailyLoginState.streakDay} Claimed (Next in 24h)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // MILESTONES HEADER
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MILESTONES & TRIALS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Tap Claim on conquered milestones",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }

                    // ACHIEVEMENT ITEMS
                    items(sortedAchievements, key = { it.id }) { item ->
                        AchievementCard(
                            achievement = item,
                            onClaim = { onClaimReward(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementCard(
    achievement: Achievement,
    onClaim: () -> Unit
) {
    val progressFraction = (achievement.currentProgress.toFloat() / achievement.targetProgress.coerceAtLeast(1)).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (achievement.isUnlocked && !achievement.isClaimed) 1.5.dp else 0.8.dp,
                color = when {
                    achievement.isUnlocked && !achievement.isClaimed -> RadiantGoldBright
                    achievement.isUnlocked -> CelestialAmethyst.copy(alpha = 0.5f)
                    else -> SurfaceCardBorder
                },
                shape = RoundedCornerShape(14.dp)
            )
            .testTag("achievement_card_${achievement.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked && !achievement.isClaimed)
                RadiantGold.copy(alpha = 0.08f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Icon Pill
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when {
                            achievement.isUnlocked -> RadiantGold.copy(alpha = 0.15f)
                            else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        }
                    )
                    .border(
                        0.8.dp,
                        if (achievement.isUnlocked) RadiantGold.copy(alpha = 0.5f) else SurfaceCardBorder,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = achievement.icon, fontSize = 20.sp)
            }

            // Info
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = achievement.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = achievement.category,
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = achievement.description,
                    fontSize = 10.sp,
                    color = TextMuted,
                    lineHeight = 13.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Progress Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(CircleShape),
                        color = if (achievement.isUnlocked) RadiantGoldBright else EtherealCyan,
                        trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "${achievement.currentProgress}/${achievement.targetProgress}",
                        fontSize = 9.sp,
                        color = TextMuted,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Action / Reward Pill
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (achievement.isClaimed) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Claimed",
                            tint = CelestialAmethystLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Claimed",
                            fontSize = 10.sp,
                            color = CelestialAmethystLight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (achievement.isUnlocked) {
                    Button(
                        onClick = onClaim,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RadiantGoldBright,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("claim_btn_${achievement.id}")
                    ) {
                        Text(
                            text = "Claim +${achievement.rewardShards} 💎",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = TextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "+${achievement.rewardShards} 💎",
                            fontSize = 10.sp,
                            color = EtherealCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
