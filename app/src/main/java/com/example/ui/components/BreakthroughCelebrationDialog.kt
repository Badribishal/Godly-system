package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.engine.BreakthroughResult
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun BreakthroughCelebrationDialog(
    result: BreakthroughResult,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breakthrough_anim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(26.dp))
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        if (result.success) {
                            listOf(RadiantGoldBright, CelestialAmethyst, Color(result.newRealm.colorHex))
                        } else {
                            listOf(Color(0xFFEF4444), Color(0xFFB91C1C), Color(0xFF7F1D1D))
                        }
                    ),
                    shape = RoundedCornerShape(26.dp)
                ),
            color = Color(0xFF0F1221)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Aura Icon Header
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            if (result.success) {
                                Brush.radialGradient(
                                    listOf(
                                        RadiantGold.copy(alpha = 0.4f),
                                        Color(result.newRealm.colorHex).copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                )
                            } else {
                                Brush.radialGradient(
                                    listOf(
                                        Color(0xFFEF4444).copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                )
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = if (result.success) RadiantGoldBright else Color(0xFFEF4444),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (result.success) {
                        Text(result.newRealm.runeSymbol, fontSize = 36.sp)
                    } else {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(36.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (result.success) "REALM ASCENSION SUCCESS" else "TRIBULATION BACKLASH",
                    color = if (result.success) TextGold else Color(0xFFEF4444),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )

                Text(
                    text = if (result.success) {
                        if (result.isMajorRealmBreakthrough) "Major Realm Ascension!" else "Meridian Stage Breakthrough!"
                    } else {
                        "Tribulation Backlash Encountered"
                    },
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (result.success) {
                    Surface(
                        color = Color(result.newRealm.colorHex).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(result.newRealm.colorHex).copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "${result.newRealm.displayName} • Stage ${result.newStage}",
                            color = Color(result.newRealm.colorHex),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = result.omenMessage,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                if (result.success) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Rewards Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceCard
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("💎 Reward", color = TextMuted, fontSize = 11.sp)
                                    Text("+${result.shardsAwarded} Gems", color = TextGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("⚡ Evolution", color = TextMuted, fontSize = 11.sp)
                                    Text("+${result.evolutionBonus}%", color = EtherealCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }

                            if (result.titleBestowed != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(RadiantGold.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                        .padding(vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Dao Title: [${result.titleBestowed}]",
                                        color = RadiantGoldBright,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("dismiss_breakthrough_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (result.success) RadiantGold else Color(0xFF334155)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (result.success) "Embrace the Grand Dao" else "Recalibrate Dantian",
                        color = if (result.success) Color(0xFF0F172A) else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
