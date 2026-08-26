package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class AstralEmotionalState(
    val id: String,
    val runeIcon: String,
    val name: String,
    val essence: String,
    val colorHex: Long,
    val resonanceAffinity: String
)

val ASTRAL_EMOTIONAL_STATES = listOf(
    AstralEmotionalState("serene", "🕊️", "Serene Equilibrium", "Centered, tranquil, harmonious", 0xFF38BDF8, "Temperance & Patience"),
    AstralEmotionalState("fiery", "🔥", "Fiery Ambition", "Driven, zealous, passionate", 0xFFF59E0B, "Diligence & Sovereign Will"),
    AstralEmotionalState("contemplative", "🌌", "Deep Contemplation", "Introspective, searching, philosophical", 0xFFA78BFA, "Humility & Astral Insight"),
    AstralEmotionalState("turbulent", "⚡", "Restless Turbulence", "Friction, intense energy, seeking release", 0xFFEF4444, "Shadow Transmutation Catalyst"),
    AstralEmotionalState("grateful", "✨", "Radiant Gratitude", "Elevated, appreciative, joyful", 0xFFFFD700, "Charity & Benevolence"),
    AstralEmotionalState("stoic", "🛡️", "Unyielding Resolve", "Firm, grounded, impenetrable", 0xFF34D399, "Courage & Discipline"),
    AstralEmotionalState("patient", "🌿", "Grounded Stillness", "Enduring, quiet, accepting", 0xFF10B981, "Patience & Chastity"),
    AstralEmotionalState("transcendent", "🔮", "Transcendent Clarity", "Awakened, visionary, lucid", 0xFFC084FC, "Unified Soul Resonance")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyEmotionalCheckInCard(
    isCompletedToday: Boolean,
    checkedInEmotion: String?,
    onCheckIn: (AstralEmotionalState) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedState by remember {
        mutableStateOf<AstralEmotionalState?>(
            ASTRAL_EMOTIONAL_STATES.find { it.name.equals(checkedInEmotion, ignoreCase = true) }
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "checkin_pulse")
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "checkin_glow"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                if (!isCompletedToday) RadiantGold.copy(alpha = borderGlow) else SurfaceCardBorder,
                RoundedCornerShape(20.dp)
            )
            .testTag("daily_emotional_checkin_card"),
        colors = CardDefaults.cardColors(
            containerColor = if (!isCompletedToday) Color(0xFF140C28) else SurfaceCardElevated
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
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
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (isCompletedToday) Color(0xFF064E3B) else Color(0xFF28194E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (isCompletedToday) "✓" else "🔮", fontSize = 13.sp)
                    }

                    Column {
                        Text(
                            text = "DAILY EMOTIONAL CHECK-IN",
                            fontSize = 11.sp,
                            color = RadiantGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (isCompletedToday) "Resonance Calibrated Today (+25 💎)" else "Tune Soul Resonance & Oracle Revelation",
                            fontSize = 10.sp,
                            color = if (isCompletedToday) Color(0xFF34D399) else CelestialAmethystLight
                        )
                    }
                }

                if (isCompletedToday) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF042F2E))
                            .border(0.8.dp, Color(0xFF14B8A6), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "ALIGNED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2DD4BF)
                        )
                    }
                }
            }

            if (isCompletedToday && checkedInEmotion != null) {
                // Aligned State Banner
                val stateObj = ASTRAL_EMOTIONAL_STATES.find { it.name.equals(checkedInEmotion, ignoreCase = true) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0B0818))
                        .border(0.8.dp, EtherealCyan.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = stateObj?.runeIcon ?: "✨", fontSize = 20.sp)
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Today's Frequency: $checkedInEmotion",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stateObj?.essence ?: "Soul resonance elevated for today's journey.",
                            fontSize = 10.5.sp,
                            color = TextMuted
                        )
                    }
                }
            } else {
                Text(
                    text = "What is the dominant frequency of your consciousness today? This directly tunes your Soul Resonance and summons the Oracle Revelation:",
                    fontSize = 11.5.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )

                // Emotional State Selector Chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ASTRAL_EMOTIONAL_STATES.forEach { state ->
                        val isSelected = selectedState?.id == state.id
                        val stateColor = Color(state.colorHex)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) stateColor.copy(alpha = 0.22f)
                                    else Color(0xFF0E091E)
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.8.dp,
                                    color = if (isSelected) stateColor else SurfaceCardBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedState = state }
                                .padding(horizontal = 10.dp, vertical = 7.dp)
                                .testTag("emotion_chip_${state.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = state.runeIcon, fontSize = 13.sp)
                                Text(
                                    text = state.name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) stateColor else TextPrimary
                                )
                            }
                        }
                    }
                }

                // Confirm Check-In Button
                Button(
                    onClick = {
                        selectedState?.let { onCheckIn(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("submit_daily_checkin_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RadiantGold,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = selectedState != null
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.GraphicEq, contentDescription = "Calibrate", modifier = Modifier.size(16.dp))
                        Text(
                            text = if (selectedState != null) "CALIBRATE RESONANCE (+25 💎)" else "SELECT AN EMOTIONAL FREQUENCY",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }
        }
    }
}
