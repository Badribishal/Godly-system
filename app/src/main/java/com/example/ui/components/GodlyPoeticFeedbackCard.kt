package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.GodlyPoeticFeedbackEngine
import com.example.data.model.SoulIdentity
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GodlyPoeticFeedbackCard(
    soul: SoulIdentity,
    modifier: Modifier = Modifier
) {
    var cycleIndex by remember { mutableIntStateOf(0) }

    val feedback = remember(
        soul.race,
        soul.className,
        soul.currentTitle,
        soul.dominantVirtue,
        soul.dominantShadow,
        soul.virtueScores,
        soul.shadowScores,
        soul.stability,
        soul.humanity,
        cycleIndex
    ) {
        GodlyPoeticFeedbackEngine.generateFeedback(soul, cycleIndex)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "godly_glow")
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "godly_border_glow"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF191033),
                        Color(0xFF0F0A22),
                        Color(0xFF070412)
                    )
                )
            )
            .border(
                width = 1.3.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        RadiantGoldBright.copy(alpha = borderGlow),
                        CelestialAmethystLight.copy(alpha = 0.6f),
                        EtherealCyan.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
            .testTag("godly_poetic_feedback_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header Row: System Identity & Cycle Action
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(RadiantGold.copy(alpha = 0.15f))
                            .border(1.dp, RadiantGold.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Godly Sigil",
                            tint = RadiantGoldBright,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "GODLY IDENTITY FEEDBACK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = RadiantGold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Automated Alchemical Reflection",
                            fontSize = 10.sp,
                            color = CelestialAmethystLight
                        )
                    }
                }

                IconButton(
                    onClick = { cycleIndex++ },
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("godly_feedback_cycle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Cycle Divine Echoes",
                        tint = TextGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Divine Epithet Tag
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2B1B54).copy(alpha = 0.7f))
                    .border(1.dp, CelestialAmethyst.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flare,
                        contentDescription = null,
                        tint = RadiantGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "« ${feedback.epithet} »",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RadiantGoldBright,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            // Animated Poetic Stanza
            AnimatedContent(
                targetState = feedback,
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(250))
                },
                label = "poetic_verse_content"
            ) { currentFeedback ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0B061A).copy(alpha = 0.65f))
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "“${currentFeedback.poeticVerse}”",
                        fontSize = 13.5.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )

                    Text(
                        text = "“${currentFeedback.secondaryStanza}”",
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        color = CelestialAmethystLight,
                        lineHeight = 19.sp
                    )
                }
            }

            // Harmonic Balance Bar: 7 Sins vs 7 Virtues ratio
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF140D2B))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = feedback.dualityBalanceLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RadiantGold
                    )
                    Text(
                        text = feedback.divineAura,
                        fontSize = 10.5.sp,
                        color = EtherealCyan
                    )
                }

                // Dual Color Progress Bar (Virtues Gold vs Shadows Crimson)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFDC2626).copy(alpha = 0.5f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(feedback.virtuePercent.toFloat() / 100f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        RadiantGold,
                                        RadiantGoldBright
                                    )
                                )
                            )
                    )
                }

                // Sub-labels for the ratio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Sacred Virtues: ${feedback.virtuePercent}%",
                        fontSize = 9.5.sp,
                        color = RadiantGoldBright
                    )
                    Text(
                        text = "Shadow Sins: ${feedback.shadowPercent}%",
                        fontSize = 9.5.sp,
                        color = Color(0xFFF87171)
                    )
                }
            }

            // Cosmic Guidance / Verdict Footer
            Text(
                text = feedback.cosmicVerdict,
                fontSize = 11.sp,
                color = TextSecondary,
                fontFamily = FontFamily.Serif,
                lineHeight = 16.sp
            )
        }
    }
}
