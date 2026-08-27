package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.data.engine.LevelUpOutcome
import com.example.data.model.AdvancedArchetype
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
fun LevelUpCelebrationDialog(
    outcome: LevelUpOutcome,
    onAttuneArchetype: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tierColor = Color(outcome.newTier.colorHex)

    val infiniteTransition = rememberInfiniteTransition(label = "levelup_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(26.dp))
                .border(
                    BorderStroke(
                        2.dp,
                        Brush.verticalGradient(
                            listOf(
                                tierColor,
                                RadiantGoldBright,
                                CelestialAmethyst
                            )
                        )
                    ),
                    RoundedCornerShape(26.dp)
                )
                .testTag("levelup_celebration_dialog"),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.98f),
            shadowElevation = 32.dp
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Epic Celestial Visual Effect Layer
                EpicAscensionCelebrationEffect(
                    modifier = Modifier.matchParentSize(),
                    accentColor = tierColor
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Animated Pulsing Sigil
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        tierColor.copy(alpha = 0.45f),
                                        CelestialAmethyst.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(2.5.dp, Brush.sweepGradient(listOf(RadiantGoldBright, tierColor, EtherealCyan, RadiantGoldBright)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = outcome.newTier.rune,
                            fontSize = 42.sp
                        )
                    }

                    // Title & Subtitle
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (outcome.tierPromoted) "⚡ TIER ASCENSION REACHED!" else "🌟 SOUL MATRIX LEVEL UP!",
                            style = MaterialTheme.typography.titleLarge,
                            color = RadiantGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.2.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Vessel attained Level ${outcome.newLevel} (+${outcome.levelsGained})",
                            fontSize = 14.sp,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Tier ${outcome.newTier.romanNumeral}: ${outcome.newTier.title}",
                            fontSize = 12.sp,
                            color = tierColor,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Reward Banner: Soul Shards & Archetypes
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.dp, SurfaceCardBorder), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard.copy(alpha = 0.9f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Diamond,
                                    contentDescription = "Shards",
                                    tint = EtherealCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(text = "+${outcome.shardsReward} Shards", fontWeight = FontWeight.Bold, color = EtherealCyan, fontSize = 13.sp)
                                    Text(text = "Soul Resonance", fontSize = 10.sp, color = TextMuted)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .height(30.dp)
                                    .size(1.dp)
                                    .background(SurfaceCardBorder)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Tier",
                                    tint = RadiantGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(text = "Tier ${outcome.newTier.romanNumeral}", fontWeight = FontWeight.Bold, color = RadiantGold, fontSize = 13.sp)
                                    Text(text = "Ascended Matrix", fontSize = 10.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    // Unlocked Archetypes Section with Epic Celebration Banner
                    if (outcome.newlyUnlockedArchetypes.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = RadiantGold.copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, RadiantGold.copy(alpha = 0.6f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "✨ NEW ARCHETYPE DISCOVERY AWAKENED! ✨",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = RadiantGoldBright,
                                        letterSpacing = 1.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            outcome.newlyUnlockedArchetypes.forEach { arch ->
                                UnlockedArchetypeBanner(
                                    archetype = arch,
                                    onAttune = {
                                        onAttuneArchetype(arch.id)
                                        onDismiss()
                                    }
                                )
                            }
                        }
                    }

                    // Confirmation Button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("dismiss_levelup_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tierColor,
                            contentColor = Color(0xFF100A1C)
                        )
                    ) {
                        Text(
                            text = "Harmonize Power & Continue",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UnlockedArchetypeBanner(
    archetype: AdvancedArchetype,
    onAttune: () -> Unit
) {
    val accentColor = Color(archetype.accentColorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = archetype.sigilIcon, fontSize = 22.sp)
                Column {
                    Text(
                        text = archetype.name,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Boon: ${archetype.passivePerk}",
                        fontSize = 10.5.sp,
                        color = CelestialAmethystLight,
                        maxLines = 1
                    )
                }
            }

            Button(
                onClick = onAttune,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color(0xFF100A1C)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text(
                    text = "Attune",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
