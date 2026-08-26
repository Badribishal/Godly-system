package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.SyncAlt
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShadowType
import com.example.data.model.VirtueType
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

data class PolarityPair(
    val shadow: ShadowType,
    val virtue: VirtueType,
    val axisName: String
)

val ARCHETYPAL_POLARITY_PAIRS = listOf(
    PolarityPair(ShadowType.PRIDE, VirtueType.HUMILITY, "Ego ⚔️ Grace"),
    PolarityPair(ShadowType.GREED, VirtueType.CHARITY, "Possession ⚔️ Munificence"),
    PolarityPair(ShadowType.DESIRE, VirtueType.TEMPERANCE, "Desire ⚔️ Harmony"),
    PolarityPair(ShadowType.ENVY, VirtueType.GRATITUDE, "Comparison ⚔️ Abundance"),
    PolarityPair(ShadowType.GLUTTONY, VirtueType.COURAGE, "Appetite ⚔️ Valor"),
    PolarityPair(ShadowType.WRATH, VirtueType.PATIENCE, "Fury ⚔️ Serenity"),
    PolarityPair(ShadowType.SLOTH, VirtueType.DILIGENCE, "Inertia ⚔️ Devotion")
)

@Composable
fun SinVirtueBalanceVisualizer(
    soul: com.example.data.model.SoulIdentity? = null,
    records: List<com.example.data.local.EvaluationRecordEntity> = emptyList(),
    shadowScores: Map<ShadowType, Int> = soul?.shadowScores ?: emptyMap(),
    virtueScores: Map<VirtueType, Int> = soul?.virtueScores ?: emptyMap(),
    modifier: Modifier = Modifier,
    initialMode: Boolean = true // true = Radar Chart, false = Bar Graph
) {
    var isRadarMode by remember { mutableStateOf(initialMode) }

    val effectiveShadowScores = remember(shadowScores, soul) {
        if (shadowScores.isNotEmpty()) shadowScores else soul?.shadowScores ?: emptyMap()
    }
    val effectiveVirtueScores = remember(virtueScores, soul) {
        if (virtueScores.isNotEmpty()) virtueScores else soul?.virtueScores ?: emptyMap()
    }

    val totalSin = remember(effectiveShadowScores) { effectiveShadowScores.values.sum() }
    val totalVirtue = remember(effectiveVirtueScores) { effectiveVirtueScores.values.sum() }
    val sumForces = totalSin + totalVirtue
    val virtueRatio = if (sumForces > 0) (totalVirtue.toFloat() / sumForces.toFloat()) else 0.5f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(20.dp))
            .testTag("sin_virtue_balance_visualizer"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row with Toggle Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ARCHETYPAL POLARITY BALANCE",
                        fontSize = 11.sp,
                        color = RadiantGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "7 Deadly Sins vs. 7 Heavenly Virtues",
                        fontSize = 10.sp,
                        color = CelestialAmethystLight
                    )
                }

                // View Toggle (Radar / Bar)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF130E26))
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                        .clickable { isRadarMode = !isRadarMode }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = if (isRadarMode) Icons.Default.BarChart else Icons.Default.DonutLarge,
                            contentDescription = "Toggle Chart",
                            tint = EtherealCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isRadarMode) "View Bars" else "View Radar",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EtherealCyan
                        )
                    }
                }
            }

            // Global Equilibrium Overview Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0C071A))
                    .border(0.8.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    Text(text = "Sins: $totalSin", fontSize = 11.sp, color = Color(0xFFF87171), fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "⚖️ ${(virtueRatio * 100).toInt()}% Virtue Alignment",
                    fontSize = 11.sp,
                    color = RadiantGoldBright,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RadiantGold))
                    Text(text = "Virtues: $totalVirtue", fontSize = 11.sp, color = RadiantGold, fontWeight = FontWeight.Bold)
                }
            }

            // Animated Visualizer: Dual Radar vs Comparative Bar Graph
            AnimatedContent(
                targetState = isRadarMode,
                transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) },
                label = "chart_mode"
            ) { radar ->
                if (radar) {
                    DualPolarityRadarChart(
                        shadowScores = effectiveShadowScores,
                        virtueScores = effectiveVirtueScores
                    )
                } else {
                    PolarityEquilibriumBarGraph(
                        shadowScores = effectiveShadowScores,
                        virtueScores = effectiveVirtueScores
                    )
                }
            }
        }
    }
}

@Composable
fun DualPolarityRadarChart(
    shadowScores: Map<ShadowType, Int>,
    virtueScores: Map<VirtueType, Int>,
    modifier: Modifier = Modifier
) {
    val count = ARCHETYPAL_POLARITY_PAIRS.size

    val sinAnimated = ARCHETYPAL_POLARITY_PAIRS.map { pair ->
        animateFloatAsState(
            targetValue = (shadowScores[pair.shadow] ?: 30).toFloat() / 100f,
            animationSpec = tween(700),
            label = "sin_${pair.shadow.name}"
        ).value
    }

    val virtueAnimated = ARCHETYPAL_POLARITY_PAIRS.map { pair ->
        animateFloatAsState(
            targetValue = (virtueScores[pair.virtue] ?: 30).toFloat() / 100f,
            animationSpec = tween(700),
            label = "virtue_${pair.virtue.name}"
        ).value
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(240.dp)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width.coerceAtMost(size.height) / 2 * 0.85f
                val angleStep = (2 * Math.PI / count).toFloat()

                // 1. Grid Rings
                val gridRings = listOf(0.25f, 0.5f, 0.75f, 1.0f)
                gridRings.forEach { scale ->
                    val ringPath = Path()
                    for (i in 0 until count) {
                        val angle = i * angleStep - (Math.PI / 2).toFloat()
                        val x = center.x + radius * scale * cos(angle)
                        val y = center.y + radius * scale * sin(angle)
                        if (i == 0) ringPath.moveTo(x, y) else ringPath.lineTo(x, y)
                    }
                    ringPath.close()
                    drawPath(
                        path = ringPath,
                        color = Color(0xFF3B2D62).copy(alpha = if (scale == 1.0f) 0.5f else 0.2f),
                        style = Stroke(width = if (scale == 1.0f) 1.2f else 0.8f)
                    )
                }

                // 2. Axes Lines
                for (i in 0 until count) {
                    val angle = i * angleStep - (Math.PI / 2).toFloat()
                    val x = center.x + radius * cos(angle)
                    val y = center.y + radius * sin(angle)
                    drawLine(
                        color = Color(0xFF4C3D82).copy(alpha = 0.35f),
                        start = center,
                        end = Offset(x, y),
                        strokeWidth = 1f
                    )
                }

                // 3. Sins Polygon (Red / Violet)
                val sinPath = Path()
                val sinPoints = mutableListOf<Offset>()
                for (i in 0 until count) {
                    val score = sinAnimated[i].coerceIn(0.1f, 1.0f)
                    val angle = i * angleStep - (Math.PI / 2).toFloat()
                    val x = center.x + radius * score * cos(angle)
                    val y = center.y + radius * score * sin(angle)
                    val pt = Offset(x, y)
                    sinPoints.add(pt)
                    if (i == 0) sinPath.moveTo(x, y) else sinPath.lineTo(x, y)
                }
                sinPath.close()

                drawPath(path = sinPath, color = Color(0xFFEF4444).copy(alpha = 0.20f), style = Fill)
                drawPath(path = sinPath, color = Color(0xFFEF4444).copy(alpha = 0.85f), style = Stroke(width = 2f, cap = StrokeCap.Round))
                sinPoints.forEach { pt ->
                    drawCircle(color = Color(0xFFEF4444), radius = 3.5f, center = pt)
                }

                // 4. Virtues Polygon (Gold / Cyan)
                val virtuePath = Path()
                val virtuePoints = mutableListOf<Offset>()
                for (i in 0 until count) {
                    val score = virtueAnimated[i].coerceIn(0.1f, 1.0f)
                    val angle = i * angleStep - (Math.PI / 2).toFloat()
                    val x = center.x + radius * score * cos(angle)
                    val y = center.y + radius * score * sin(angle)
                    val pt = Offset(x, y)
                    virtuePoints.add(pt)
                    if (i == 0) virtuePath.moveTo(x, y) else virtuePath.lineTo(x, y)
                }
                virtuePath.close()

                drawPath(path = virtuePath, color = Color(0xFFFBBF24).copy(alpha = 0.22f), style = Fill)
                drawPath(path = virtuePath, color = Color(0xFFFBBF24).copy(alpha = 0.90f), style = Stroke(width = 2f, cap = StrokeCap.Round))
                virtuePoints.forEach { pt ->
                    drawCircle(color = Color(0xFFFBBF24), radius = 3.5f, center = pt)
                }
            }
        }

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                Text(text = "Seven Sins Polygon", fontSize = 11.sp, color = Color(0xFFF87171))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFBBF24)))
                Text(text = "Seven Virtues Polygon", fontSize = 11.sp, color = RadiantGoldBright)
            }
        }
    }
}

@Composable
fun PolarityEquilibriumBarGraph(
    shadowScores: Map<ShadowType, Int>,
    virtueScores: Map<VirtueType, Int>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ARCHETYPAL_POLARITY_PAIRS.forEach { pair ->
            val sinVal = shadowScores[pair.shadow] ?: 30
            val virtueVal = virtueScores[pair.virtue] ?: 30
            val total = (sinVal + virtueVal).coerceAtLeast(1)
            val sinFrac = sinVal.toFloat() / total.toFloat()
            val virtueFrac = virtueVal.toFloat() / total.toFloat()

            val animVirtueFrac by animateFloatAsState(
                targetValue = virtueFrac,
                animationSpec = tween(600),
                label = "bar_${pair.axisName}"
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${pair.shadow.runeSymbol} ${pair.shadow.displayName} ($sinVal)",
                        fontSize = 11.sp,
                        color = Color(0xFFF87171),
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = pair.axisName,
                        fontSize = 9.5.sp,
                        color = TextMuted
                    )

                    Text(
                        text = "(${virtueVal}) ${pair.virtue.displayName} ${pair.virtue.runeSymbol}",
                        fontSize = 11.sp,
                        color = RadiantGoldBright,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Opposing Dual Balance Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF130E26))
                        .border(0.5.dp, SurfaceCardBorder, CircleShape)
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left Sin Segment
                        Box(
                            modifier = Modifier
                                .weight((1f - animVirtueFrac).coerceAtLeast(0.05f))
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF7F1D1D), Color(0xFFEF4444))
                                    )
                                )
                        )

                        // Center Balance Divider
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .fillMaxHeight()
                                .background(Color.White)
                        )

                        // Right Virtue Segment
                        Box(
                            modifier = Modifier
                                .weight(animVirtueFrac.coerceAtLeast(0.05f))
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(RadiantGold, RadiantGoldBright)
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}
