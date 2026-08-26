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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OracleMessageCard(
    soul: SoulIdentity,
    checkedInEmotion: String? = null,
    modifier: Modifier = Modifier
) {
    var oracleIndexOffset by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "oracle_pulse")
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_glow"
    )

    val todayDateStr = remember {
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    }
    val dateHash = remember(todayDateStr, checkedInEmotion) {
        (todayDateStr + (checkedInEmotion ?: "")).hashCode()
    }

    val oracleMessages = remember(soul.race, soul.className, soul.dominantVirtue, soul.dominantShadow, soul.evolutionProgress, checkedInEmotion) {
        generateOracleWisdomPool(soul, checkedInEmotion)
    }

    val activeIndex = (kotlin.math.abs(dateHash + oracleIndexOffset)) % oracleMessages.size
    val currentOracle = oracleMessages[activeIndex]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF140D2B),
                        Color(0xFF0C081A)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        RadiantGold.copy(alpha = borderGlow),
                        CelestialAmethyst.copy(alpha = 0.5f),
                        EtherealCyan.copy(alpha = 0.4f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
            .testTag("oracle_message_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row
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
                            .background(Color(0xFF2A1B54))
                            .border(1.dp, RadiantGold.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👁️", fontSize = 12.sp)
                    }
                    Text(
                        text = "DAILY ORACLE REVELATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = RadiantGoldBright,
                        letterSpacing = 1.2.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentOracle.aspectTitle,
                        fontSize = 10.sp,
                        color = EtherealCyan,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(
                        onClick = { oracleIndexOffset++ },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("oracle_refresh_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Commune with Oracle",
                            tint = RadiantGold,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Mysterious Reflection Message
            AnimatedContent(
                targetState = currentOracle,
                transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(300)) },
                label = "oracle_quote_anim"
            ) { oracle ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "“${oracle.mysticReflection}”",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp,
                        fontSize = 14.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF090614).copy(alpha = 0.8f))
                            .border(0.5.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "✨", fontSize = 11.sp)
                        Text(
                            text = oracle.encouragingDirective,
                            fontSize = 11.5.sp,
                            color = CelestialAmethystLight,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

private data class OracleMessage(
    val aspectTitle: String,
    val mysticReflection: String,
    val encouragingDirective: String
)

private fun generateOracleWisdomPool(soul: SoulIdentity, checkedInEmotion: String? = null): List<OracleMessage> {
    val virtueName = soul.dominantVirtue.displayName
    val shadowName = soul.dominantShadow.displayName
    val raceName = soul.race
    val className = soul.advancedClass ?: soul.className

    val list = mutableListOf<OracleMessage>()

    if (!checkedInEmotion.isNullOrBlank()) {
        list.add(
            OracleMessage(
                aspectTitle = "Resonance of $checkedInEmotion",
                mysticReflection = "Your conscious frequency of '$checkedInEmotion' acts as a prism for cosmic energy. Through this specific emotional lens, $virtueName finds direct expression while taming the depths of $shadowName.",
                encouragingDirective = "Embrace today's state as purposeful raw material for your vessel's evolution."
            )
        )
    }

    list.addAll(
        listOf(
            OracleMessage(
                aspectTitle = "Veil of the $raceName",
                mysticReflection = "The cosmic tapestry does not demand your perfection; it observes the gravity of your resolve. Even shadows cast by $shadowName are proof of the light within you.",
                encouragingDirective = "Channel today's friction into unyielding momentum. Your awakening is drawing nearer."
            ),
            OracleMessage(
                aspectTitle = "Harmonic Decree",
                mysticReflection = "When chaos swirls in the waking world, the $className anchors in silent equilibrium. True sovereignty is choosing $virtueName when all else invites discord.",
                encouragingDirective = "Stand calm within your vessel. The universe honors those who remain unshakable."
            ),
            OracleMessage(
                aspectTitle = "The $virtueName Principle",
                mysticReflection = "A seed breaks in darkness not to perish, but to ascend. Every trial you record is an alchemy that refines raw experience into divine gold.",
                encouragingDirective = "Trust the subtle shifts occurring beneath your awareness. Your soul knows the path."
            ),
            OracleMessage(
                aspectTitle = "Astral Convergence",
                mysticReflection = "You carry the ancient resonance of the $raceName. What the mundane realm calls coincidence is the System aligning events to test your higher faculties.",
                encouragingDirective = "Act with deliberate intention today; your choices ripple far beyond the visible realm."
            ),
            OracleMessage(
                aspectTitle = "Alchemical Balance",
                mysticReflection = "Do not extinguish the fiery spark of $shadowName; transmute it through the cool clarity of $virtueName. Mastery is synthesis, not suppression.",
                encouragingDirective = "Wield both your light and shadow with conscious grace. You are the sovereign of your vessel."
            )
        )
    )

    return list
}
