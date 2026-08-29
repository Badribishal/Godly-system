package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.engine.QiCultivationEngine
import com.example.data.model.CultivationRealm
import com.example.data.model.SoulIdentity
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
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiChamberDialog(
    soul: SoulIdentity,
    onDismiss: () -> Unit,
    onGatherQi: (Int) -> Unit,
    onBreakthrough: () -> Unit,
    onOpenTreasury: () -> Unit
) {
    val realm = remember(soul.cultivationRealm) { CultivationRealm.fromNameOrId(soul.cultivationRealm) }
    val qiRatio = (soul.currentQi.toFloat() / soul.maxQi.toFloat()).coerceIn(0f, 1f)
    val isQiFull = soul.currentQi >= soul.maxQi
    val isMajorBreakthrough = soul.cultivationStage >= realm.maxStages

    val breakthroughChance = remember(soul.currentQi, soul.stability, soul.humanity, soul.activeTribulationTalismanCount) {
        QiCultivationEngine.calculateBreakthroughChance(soul, if (soul.activeTribulationTalismanCount > 0) 100 else 0)
    }

    var channelCount by remember { mutableIntStateOf(0) }
    var breathPhase by remember { mutableStateOf("Inhale Cosmic Qi...") }
    val breathScale = remember { Animatable(1.0f) }

    // Breathing cultivation loop
    LaunchedEffect(Unit) {
        while (true) {
            breathPhase = "Inhale Cosmic Aether..."
            breathScale.animateTo(1.35f, animationSpec = tween(4000, easing = FastOutSlowInEasing))
            breathPhase = "Retain & Temper Dantian..."
            delay(2000)
            breathPhase = "Exhale Impurities..."
            breathScale.animateTo(1.0f, animationSpec = tween(4000, easing = FastOutSlowInEasing))
            delay(1000)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "qi_chamber_anim")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rot"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(realm.colorHex).copy(alpha = 0.8f),
                            CelestialAmethyst.copy(alpha = 0.6f),
                            RadiantGold.copy(alpha = 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            color = Color(0xFF090B14)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(realm.colorHex).copy(alpha = 0.2f))
                                .border(1.dp, Color(realm.colorHex), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(realm.runeSymbol, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Qi Cultivation Chamber",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Meridian Aether Sanctuary",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_qi_chamber_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Realm & Stage Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SurfaceCard
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f, fill = false)) {
                                    Text(
                                        text = "CURRENT REALM",
                                        color = TextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${realm.displayName} • Stage ${soul.cultivationStage}/${realm.maxStages}",
                                        color = Color(realm.colorHex),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Surface(
                                    color = RadiantGold.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("💎", fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${soul.soulShards} Gems",
                                            color = TextGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Qi Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Spiritual Qi Reservoir",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${soul.currentQi} / ${soul.maxQi} Qi (${(qiRatio * 100).toInt()}%)",
                                    color = if (isQiFull) RadiantGoldBright else EtherealCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E293B))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(qiRatio)
                                        .fillMaxHeight()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Color(realm.colorHex),
                                                    if (isQiFull) RadiantGoldBright else EtherealCyan
                                                )
                                            )
                                        )
                                    )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Root: ${soul.spiritualRoots}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                if (soul.activeTribulationTalismanCount > 0) {
                                    Text(
                                        text = "🏮 Talisman Active (100% Success)",
                                        color = Color(0xFFF43F5E),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Meditation Breathing Circle Visualizer
                    Box(
                        modifier = Modifier
                            .size(230.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Dynamic canvas for orbit rings
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val center = Offset(size.width / 2, size.height / 2)
                            val radius = size.minDimension / 2 - 16.dp.toPx()

                            // Outer Orbit Ring
                            drawCircle(
                                color = Color(realm.colorHex).copy(alpha = 0.25f),
                                radius = radius,
                                center = center,
                                style = Stroke(width = 2.dp.toPx())
                            )

                            // Inner Meridian Ring
                            drawCircle(
                                color = CelestialAmethyst.copy(alpha = 0.2f),
                                radius = radius * 0.75f,
                                center = center,
                                style = Stroke(width = 1.5.dp.toPx())
                            )

                            // Orbiting Spiritual Spark
                            val sparkRad = Math.toRadians(orbitAngle.toDouble())
                            val sparkX = center.x + radius * cos(sparkRad).toFloat()
                            val sparkY = center.y + radius * sin(sparkRad).toFloat()
                            drawCircle(
                                color = RadiantGoldBright,
                                radius = 6.dp.toPx(),
                                center = Offset(sparkX, sparkY)
                            )
                        }

                        // Central Pulsing Dantian
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .scale(breathScale.value)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            Color(realm.colorHex).copy(alpha = 0.6f * pulseGlow),
                                            CelestialAmethyst.copy(alpha = 0.35f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .border(
                                    width = 2.dp,
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            Color(realm.colorHex),
                                            CelestialAmethyst,
                                            RadiantGold,
                                            Color(realm.colorHex)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clickable {
                                    onGatherQi(25)
                                    channelCount++
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = realm.runeSymbol,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(realm.colorHex)
                                )
                                Text(
                                    text = "Tap to Gather",
                                    color = TextPrimary.copy(alpha = 0.9f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Text(
                        text = breathPhase,
                        color = EtherealCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Channel Qi & Meditate Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                onGatherQi(35)
                                channelCount++
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("gather_qi_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(realm.colorHex).copy(alpha = 0.25f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(realm.colorHex))
                        ) {
                            Icon(Icons.Default.Spa, contentDescription = null, tint = Color(realm.colorHex), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Meditate (+35 Qi)",
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = onOpenTreasury,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("open_spirit_treasury_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RadiantGold.copy(alpha = 0.25f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold)
                        ) {
                            Text("💎", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Spend Gems",
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Breakthrough Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isQiFull) Color(0xFF1E1B4B) else SurfaceCard
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isQiFull) 1.5.dp else 1.dp,
                            color = if (isQiFull) RadiantGoldBright else SurfaceCardBorder
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = if (isQiFull) RadiantGoldBright else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isMajorBreakthrough) "Heavenly Realm Ascension" else "Meridian Stage Breakthrough",
                                        color = if (isQiFull) TextGold else TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                Surface(
                                    color = if (breakthroughChance.totalChance >= 80) Color(0xFF065F46) else Color(0xFF78350F),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Success: ${breakthroughChance.totalChance}%",
                                        color = if (breakthroughChance.totalChance >= 80) Color(0xFF34D399) else Color(0xFFFBBF24),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (isQiFull) {
                                    if (isMajorBreakthrough) {
                                        "⚡ Qi reservoir is surging! Attempting breakthrough will transcend your vessel to the next Heavenly Realm."
                                    } else {
                                        "✨ Qi is fully condensed. Breakthrough will advance your cultivation to Stage ${soul.cultivationStage + 1}."
                                    }
                                } else {
                                    "Requires ${soul.maxQi - soul.currentQi} more Qi to trigger realm breakthrough."
                                },
                                color = if (isQiFull) TextSecondary else TextMuted,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = onBreakthrough,
                                enabled = isQiFull,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("breakthrough_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RadiantGold,
                                    disabledContainerColor = Color(0xFF334155)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    text = if (isQiFull) "⚡ Attempt Breakthrough ⚡" else "Accumulate More Qi (${soul.currentQi}/${soul.maxQi})",
                                    color = if (isQiFull) Color(0xFF0F172A) else TextMuted,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
