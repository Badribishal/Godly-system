package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.engine.SoulResonanceData
import com.example.data.model.IdentityMilestoneBadge
import com.example.data.model.IdentityMilestoneCatalog
import com.example.data.model.SoulIdentity
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.RadiantGoldDim
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun IdentityHeader(
    soul: SoulIdentity,
    resonance: SoulResonanceData? = null,
    modifier: Modifier = Modifier
) {
    var showMilestonesDialog by remember { mutableStateOf(false) }
    val badges = remember(soul.race, soul.className, soul.soulShards, soul.evolutionProgress, soul.dominantVirtue, soul.dominantShadow) {
        IdentityMilestoneCatalog.evaluateMilestones(soul, emptyList(), emptyList())
    }
    val unlockedCount = badges.count { it.isUnlocked }
    val infiniteTransition = rememberInfiniteTransition(label = "soul_aura")
    val auraPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aura_pulse"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val evolutionProgressAnimated by animateFloatAsState(
        targetValue = soul.evolutionProgress.toFloat() / 100f,
        animationSpec = tween(700),
        label = "evo_progress"
    )

    val humanityAnimated by animateFloatAsState(
        targetValue = soul.humanity.toFloat() / 100f,
        animationSpec = tween(700),
        label = "humanity"
    )

    val resonancePercent = resonance?.percentage ?: 72
    val resonanceAnimated by animateFloatAsState(
        targetValue = resonancePercent.toFloat() / 100f,
        animationSpec = tween(700),
        label = "resonance_anim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceCardElevated,
                        SurfaceCard
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        RadiantGold.copy(alpha = glowAlpha),
                        CelestialAmethyst.copy(alpha = 0.4f),
                        SurfaceCardBorder
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
            .testTag("identity_header")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header Top: System Label & Alignment Badge
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
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "System Sigil",
                        tint = RadiantGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "VESSEL CLASSIFICATION",
                        style = MaterialTheme.typography.labelMedium,
                        color = RadiantGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF261D47))
                        .border(1.dp, CelestialAmethyst.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = soul.alignment,
                        color = TextAmethyst,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Current Title
            Text(
                text = "« ${soul.currentTitle} »",
                style = MaterialTheme.typography.titleMedium,
                color = TextGold,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold
            )

            // Race & Class Prominent Display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.weight(1f)) {
                    Text(
                        text = soul.race.uppercase(),
                        style = MaterialTheme.typography.displayMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = soul.advancedClass ?: soul.className,
                        style = MaterialTheme.typography.headlineMedium,
                        color = CelestialAmethystLight,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Element Sigil Crest with Equipped Cosmetic Visual Aura
                CosmeticAvatarCrest(
                    race = soul.race,
                    equippedEffectId = soul.equippedEffectId
                )
            }

            // Elemental Alignment & Archetype
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F0B1E).copy(alpha = 0.7f))
                    .border(0.5.dp, EtherealCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Elemental Alignment:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = soul.element,
                    style = MaterialTheme.typography.bodyMedium,
                    color = EtherealCyan,
                    fontWeight = FontWeight.Bold
                )
            }

            // Dominant Forces Highlights with subtle glowing badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0D0A18))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dominant Shadow (Sin)
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = "DOMINANT SHADOW",
                        fontSize = 9.sp,
                        color = Color(0xFFEF4444).copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = soul.dominantShadow.runeSymbol,
                            fontSize = 14.sp
                        )
                        Text(
                            text = soul.dominantShadow.displayName,
                            fontSize = 13.sp,
                            color = Color(soul.dominantShadow.colorHex),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                        .background(SurfaceCardBorder)
                )

                // Dominant Virtue
                Column(verticalArrangement = Arrangement.spacedBy(3.dp), horizontalAlignment = Alignment.End) {
                    Text(
                        text = "DOMINANT VIRTUE",
                        fontSize = 9.sp,
                        color = RadiantGold.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = soul.dominantVirtue.displayName,
                            fontSize = 13.sp,
                            color = Color(soul.dominantVirtue.colorHex),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = soul.dominantVirtue.runeSymbol,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // SOUL RESONANCE METRIC & PROGRESS BAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0A0716))
                    .border(0.8.dp, EtherealCyan.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(10.dp)
                    .testTag("soul_resonance_meter")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Resonance Wave",
                                tint = EtherealCyan,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Soul Resonance:",
                                style = MaterialTheme.typography.labelMedium,
                                color = EtherealCyan,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$resonancePercent%",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = resonance?.frequencyLabel ?: "528 Hz • Miraculous Harmony",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialAmethystLight,
                            fontSize = 10.sp
                        )
                    }

                    // Resonance Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF030A14))
                            .border(0.5.dp, EtherealCyan.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(resonanceAnimated.coerceIn(0.02f, 1.0f))
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF0284C7),
                                            Color(0xFF06B6D4),
                                            Color(0xFF38BDF8)
                                        )
                                    )
                                )
                        )
                    }

                    Text(
                        text = resonance?.resonanceInsight ?: "Synchronized with ${soul.dominantVirtue.displayName} & ${soul.dominantShadow.displayName}.",
                        fontSize = 10.5.sp,
                        color = TextMuted,
                        lineHeight = 14.sp
                    )
                }
            }

            // Humanity & Evolution Progress Bars
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Humanity Meter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Humanity Tether",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "${soul.humanity}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (soul.humanity > 50) Color(0xFF34D399) else Color(0xFFA78BFA),
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0F0B1E))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(humanityAnimated.coerceIn(0.02f, 1.0f))
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF10B981),
                                        Color(0xFF34D399)
                                    )
                                )
                            )
                    )
                }

                // Evolution Progress Meter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Metamorphic Awakening:",
                            style = MaterialTheme.typography.labelMedium,
                            color = RadiantGoldBright
                        )
                        Text(
                            text = "${soul.evolutionProgress}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = RadiantGold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Next: ${soul.possibleEvolution}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextAmethyst
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0F0B1E))
                        .border(0.5.dp, RadiantGold.copy(alpha = 0.3f), CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(evolutionProgressAnimated.coerceIn(0.02f, 1.0f))
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        RadiantGoldDim,
                                        RadiantGold,
                                        CelestialAmethystLight
                                    )
                                )
                            )
                    )
                }

                // IDENTITY MILESTONE BADGES RIBBON (Long-Term Persona Development Rewards)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D0820))
                        .border(0.8.dp, RadiantGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .clickable { showMilestonesDialog = true }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("identity_milestones_ribbon")
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
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "Milestone Badges",
                                tint = RadiantGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Milestone Badges:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadiantGold
                            )
                            Text(
                                text = "$unlockedCount / ${badges.size} Unlocked",
                                fontSize = 10.5.sp,
                                color = EtherealCyan,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Mini badge icons preview
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            badges.filter { it.isUnlocked }.take(4).forEach { badge ->
                                Text(text = badge.runeIcon, fontSize = 13.sp)
                            }
                            Text(
                                text = "View ❯",
                                fontSize = 10.sp,
                                color = CelestialAmethystLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // IDENTITY MILESTONE BADGES DIALOG
    if (showMilestonesDialog) {
        Dialog(onDismissRequest = { showMilestonesDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.2.dp, RadiantGold.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
                    .testTag("identity_milestones_dialog"),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "👑", fontSize = 20.sp)
                            Column {
                                Text(
                                    text = "IDENTITY MILESTONES",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RadiantGold,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = "$unlockedCount of ${badges.size} Astral Badges Unlocked",
                                    fontSize = 10.5.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        IconButton(onClick = { showMilestonesDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextMuted
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(badges) { badge ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (badge.isUnlocked) Color(0xFF160E30) else Color(0xFF0C081A)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (badge.isUnlocked) 1.dp else 0.5.dp,
                                    color = if (badge.isUnlocked) Color(badge.tier.colorHex).copy(alpha = 0.7f) else SurfaceCardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (badge.isUnlocked) Color(badge.tier.colorHex).copy(alpha = 0.2f)
                                                else Color(0xFF1B162E)
                                            )
                                            .border(
                                                1.dp,
                                                if (badge.isUnlocked) Color(badge.tier.colorHex) else Color.Gray.copy(alpha = 0.3f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (badge.isUnlocked) badge.runeIcon else "🔒",
                                            fontSize = 18.sp
                                        )
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
                                                text = badge.name,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (badge.isUnlocked) Color.White else TextMuted
                                            )
                                            Text(
                                                text = badge.tier.displayName,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(badge.tier.colorHex)
                                            )
                                        }

                                        Text(
                                            text = badge.description,
                                            fontSize = 10.5.sp,
                                            color = if (badge.isUnlocked) CelestialAmethystLight else TextMuted,
                                            lineHeight = 14.sp
                                        )

                                        Text(
                                            text = "Requirement: ${badge.requirementText}",
                                            fontSize = 9.5.sp,
                                            color = RadiantGold.copy(alpha = 0.8f)
                                        )

                                        if (!badge.isUnlocked) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(4.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF221A38))
                                                ) {
                                                    val progressRatio = (badge.progressCurrent.toFloat() / badge.progressMax.toFloat()).coerceIn(0f, 1f)
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(progressRatio)
                                                            .fillMaxHeight()
                                                            .clip(CircleShape)
                                                            .background(Color(badge.tier.colorHex))
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "${badge.progressCurrent}/${badge.progressMax}",
                                                    fontSize = 9.sp,
                                                    color = TextMuted
                                                )
                                            }
                                        } else if (badge.unlockedAtText != null) {
                                            Text(
                                                text = "✓ ${badge.unlockedAtText}",
                                                fontSize = 9.5.sp,
                                                color = Color(0xFF34D399),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
