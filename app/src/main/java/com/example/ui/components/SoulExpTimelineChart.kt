package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.DailyExpDataPoint
import com.example.data.engine.SoulExpTimelineStats
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
import kotlin.math.max

enum class ChartTimeframe(val label: String, val days: Int) {
    DAYS_7("7D", 7),
    DAYS_14("14D", 14),
    DAYS_30("30D", 30)
}

enum class ChartMode(val label: String) {
    DAILY_SPIKES("Daily Velocity & Spikes"),
    CUMULATIVE_GROWTH("Ascension Cumulative")
}

@Composable
fun SoulExpTimelineChart(
    stats: SoulExpTimelineStats,
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf(ChartTimeframe.DAYS_30) }
    var selectedMode by remember { mutableStateOf(ChartMode.DAILY_SPIKES) }

    // Filter points based on timeframe
    val activePoints = remember(stats, selectedTimeframe) {
        val count = selectedTimeframe.days
        if (stats.dataPoints.size >= count) {
            stats.dataPoints.takeLast(count)
        } else {
            stats.dataPoints
        }
    }

    // Interactive scrubber selection (index in activePoints)
    var selectedIndex by remember(activePoints) { mutableIntStateOf(activePoints.size - 1) }
    val selectedPoint = activePoints.getOrNull(selectedIndex) ?: activePoints.lastOrNull()

    val infiniteTransition = rememberInfiniteTransition(label = "spike_glow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(
                BorderStroke(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(
                            RadiantGold.copy(alpha = 0.5f),
                            SurfaceCardBorder,
                            CelestialAmethyst.copy(alpha = 0.3f)
                        )
                    )
                ),
                RoundedCornerShape(22.dp)
            )
            .testTag("soul_exp_timeline_chart_card"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header with Title & Live Metric Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timeline,
                            contentDescription = "EXP Analytics",
                            tint = RadiantGoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "SOUL MATRIX PROGRESSION",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = RadiantGold,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "30-Day Soul Experience Dynamics & Spike Analysis",
                        style = MaterialTheme.typography.bodySmall,
                        color = CelestialAmethystLight,
                        fontSize = 11.sp
                    )
                }

                // Growth badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (stats.growthPercentage >= 0) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f),
                    border = BorderStroke(
                        1.dp,
                        if (stats.growthPercentage >= 0) Color(0xFF10B981).copy(alpha = 0.5f) else Color(0xFFEF4444).copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (stats.growthPercentage >= 0) Icons.Default.TrendingUp else Icons.Default.ShowChart,
                            contentDescription = "Growth",
                            tint = if (stats.growthPercentage >= 0) Color(0xFF34D399) else Color(0xFFF87171),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${if (stats.growthPercentage >= 0) "+" else ""}${stats.growthPercentage}% Trajectory",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (stats.growthPercentage >= 0) Color(0xFF34D399) else Color(0xFFF87171)
                        )
                    }
                }
            }

            // 2. High-Level Summary Stats Grid (Recharts KPI Header)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricKpiCard(
                    title = "30D Total EXP",
                    value = "+${stats.totalExp30Days}",
                    subText = "Soul Energy",
                    accentColor = RadiantGold,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiCard(
                    title = "Peak Surge",
                    value = "+${stats.peakDayExp}",
                    subText = "Breakthrough",
                    accentColor = EtherealCyan,
                    modifier = Modifier.weight(1f)
                )
                MetricKpiCard(
                    title = "Daily Velocity",
                    value = "~${stats.averageDailyExp}",
                    subText = "EXP / Day",
                    accentColor = CelestialAmethystLight,
                    modifier = Modifier.weight(1f)
                )
            }

            // 3. Mode and Timeframe Selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timeframe Chips (7D, 14D, 30D)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ChartTimeframe.values().forEach { tf ->
                        val isSelected = selectedTimeframe == tf
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) RadiantGold else SurfaceCardBorder,
                                    RoundedCornerShape(8.dp)
                                ),
                            color = if (isSelected) RadiantGold.copy(alpha = 0.2f) else SurfaceCard,
                            onClick = {
                                selectedTimeframe = tf
                            }
                        ) {
                            Text(
                                text = tf.label,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) RadiantGoldBright else TextMuted
                            )
                        }
                    }
                }

                // Mode Toggle (Daily Spikes vs Cumulative)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(8.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                        color = if (selectedMode == ChartMode.DAILY_SPIKES) CelestialAmethyst.copy(alpha = 0.35f) else Color.Transparent,
                        onClick = { selectedMode = ChartMode.DAILY_SPIKES }
                    ) {
                        Text(
                            text = "Spikes",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.5.sp,
                            fontWeight = if (selectedMode == ChartMode.DAILY_SPIKES) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedMode == ChartMode.DAILY_SPIKES) Color.White else TextMuted
                        )
                    }

                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                        color = if (selectedMode == ChartMode.CUMULATIVE_GROWTH) EtherealCyan.copy(alpha = 0.35f) else Color.Transparent,
                        onClick = { selectedMode = ChartMode.CUMULATIVE_GROWTH }
                    ) {
                        Text(
                            text = "Total Curve",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.5.sp,
                            fontWeight = if (selectedMode == ChartMode.CUMULATIVE_GROWTH) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedMode == ChartMode.CUMULATIVE_GROWTH) EtherealCyan else TextMuted
                        )
                    }
                }
            }

            // 4. Interactive Floating Scrubber Tooltip Card (Recharts Tooltip)
            if (selectedPoint != null) {
                InteractiveScrubberTooltip(
                    point = selectedPoint,
                    isCumulativeMode = selectedMode == ChartMode.CUMULATIVE_GROWTH
                )
            }

            // 5. Native High-Precision Compose Canvas Chart Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F0A1E))
                    .border(1.dp, Color(0xFF281C44), RoundedCornerShape(14.dp))
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                InteractiveRechartsCanvas(
                    points = activePoints,
                    selectedIndex = selectedIndex,
                    onIndexChange = { newIdx ->
                        if (newIdx in activePoints.indices) {
                            selectedIndex = newIdx
                        }
                    },
                    isCumulativeMode = selectedMode == ChartMode.CUMULATIVE_GROWTH,
                    pulseAlpha = pulseAlpha
                )
            }

            // 6. Footer Milestone Guide
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
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(RadiantGoldBright)
                    )
                    Text(
                        text = "⚡ Peak Breakthrough Spike",
                        fontSize = 10.5.sp,
                        color = TextMuted
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(EtherealCyan)
                    )
                    Text(
                        text = "Touch/drag to inspect days",
                        fontSize = 10.5.sp,
                        color = CelestialAmethystLight
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricKpiCard(
    title: String,
    value: String,
    subText: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
        color = SurfaceCard
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor
            )
            Text(
                text = subText,
                fontSize = 9.sp,
                color = accentColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun InteractiveScrubberTooltip(
    point: DailyExpDataPoint,
    isCumulativeMode: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                BorderStroke(
                    1.dp,
                    if (point.isSpike) RadiantGold else EtherealCyan.copy(alpha = 0.4f)
                ),
                RoundedCornerShape(12.dp)
            ),
        color = Color(0xFF191030)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${point.dateFormatted} (${point.dayLabel})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (point.isSpike) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = RadiantGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, RadiantGold.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = "⚡ SPIKE",
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadiantGoldBright
                            )
                        }
                    }
                }

                if (point.milestoneNote != null) {
                    Text(
                        text = "• ${point.milestoneNote}",
                        fontSize = 10.5.sp,
                        color = CelestialAmethystLight,
                        maxLines = 1
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isCumulativeMode) "${point.cumulativeExp} Total EXP" else "+${point.dailyExp} EXP",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isCumulativeMode) EtherealCyan else RadiantGoldBright
                )
                Text(
                    text = if (isCumulativeMode) "Cumulative Gain" else "Daily Energy",
                    fontSize = 9.5.sp,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun InteractiveRechartsCanvas(
    points: List<DailyExpDataPoint>,
    selectedIndex: Int,
    onIndexChange: (Int) -> Unit,
    isCumulativeMode: Boolean,
    pulseAlpha: Float
) {
    val textMeasurer = rememberTextMeasurer()

    if (points.isEmpty()) return

    val values = remember(points, isCumulativeMode) {
        if (isCumulativeMode) points.map { it.cumulativeExp.toFloat() }
        else points.map { it.dailyExp.toFloat() }
    }

    val maxValue = remember(values) {
        val maxV = values.maxOrNull() ?: 100f
        max(maxV * 1.15f, 50f)
    }

    val minValue = 0f

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(points) {
                detectTapGestures { offset ->
                    val width = size.width
                    val stepX = width / (points.size - 1).coerceAtLeast(1)
                    val idx = ((offset.x + stepX / 2) / stepX).toInt().coerceIn(0, points.size - 1)
                    onIndexChange(idx)
                }
            }
            .pointerInput(points) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val width = size.width
                    val stepX = width / (points.size - 1).coerceAtLeast(1)
                    val idx = ((change.position.x + stepX / 2) / stepX).toInt().coerceIn(0, points.size - 1)
                    onIndexChange(idx)
                }
            }
    ) {
        val chartWidth = size.width
        val chartHeight = size.height
        val paddingBottom = 22f
        val paddingTop = 16f
        val paddingLeft = 32f
        val paddingRight = 16f

        val effectiveWidth = chartWidth - paddingLeft - paddingRight
        val effectiveHeight = chartHeight - paddingTop - paddingBottom
        val stepX = if (points.size > 1) effectiveWidth / (points.size - 1) else effectiveWidth

        // 1. Draw Grid Lines & Y-Axis Labels (Recharts CartesianGrid)
        val gridLinesCount = 4
        for (i in 0..gridLinesCount) {
            val yNorm = i.toFloat() / gridLinesCount.toFloat()
            val yPos = paddingTop + effectiveHeight * (1f - yNorm)
            val gridVal = (minValue + (maxValue - minValue) * yNorm).toInt()

            // Horizontal Grid Line
            drawLine(
                color = Color(0xFF2C2248).copy(alpha = 0.5f),
                start = Offset(paddingLeft, yPos),
                end = Offset(chartWidth - paddingRight, yPos),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
            )

            // Y-Axis label
            drawText(
                textMeasurer = textMeasurer,
                text = "$gridVal",
                topLeft = Offset(4f, yPos - 10f),
                style = TextStyle(
                    color = TextMuted.copy(alpha = 0.8f),
                    fontSize = 8.5.sp
                )
            )
        }

        // Calculate (x, y) coordinates for all points
        val coords = points.mapIndexed { index, _ ->
            val v = values[index]
            val x = paddingLeft + index * stepX
            val y = paddingTop + effectiveHeight * (1f - ((v - minValue) / (maxValue - minValue)).coerceIn(0f, 1f))
            Offset(x, y)
        }

        // 2. Build Smooth Area Path (Cubic Bezier Spline)
        val areaPath = Path().apply {
            if (coords.isNotEmpty()) {
                moveTo(coords.first().x, paddingTop + effectiveHeight)
                lineTo(coords.first().x, coords.first().y)

                for (i in 0 until coords.size - 1) {
                    val p0 = coords[i]
                    val p1 = coords[i + 1]
                    val cx1 = p0.x + (p1.x - p0.x) / 2f
                    val cy1 = p0.y
                    val cx2 = p0.x + (p1.x - p0.x) / 2f
                    val cy2 = p1.y
                    cubicTo(cx1, cy1, cx2, cy2, p1.x, p1.y)
                }

                lineTo(coords.last().x, paddingTop + effectiveHeight)
                close()
            }
        }

        // Fill area gradient (Recharts defs gradient)
        val gradientBrush = if (isCumulativeMode) {
            Brush.verticalGradient(
                colors = listOf(
                    EtherealCyan.copy(alpha = 0.45f),
                    CelestialAmethyst.copy(alpha = 0.2f),
                    Color.Transparent
                ),
                startY = paddingTop,
                endY = paddingTop + effectiveHeight
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    RadiantGoldBright.copy(alpha = 0.5f),
                    CelestialAmethyst.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                startY = paddingTop,
                endY = paddingTop + effectiveHeight
            )
        }

        drawPath(path = areaPath, brush = gradientBrush, style = Fill)

        // 3. Build Stroke Line Path
        val linePath = Path().apply {
            if (coords.isNotEmpty()) {
                moveTo(coords.first().x, coords.first().y)
                for (i in 0 until coords.size - 1) {
                    val p0 = coords[i]
                    val p1 = coords[i + 1]
                    val cx1 = p0.x + (p1.x - p0.x) / 2f
                    val cy1 = p0.y
                    val cx2 = p0.x + (p1.x - p0.x) / 2f
                    val cy2 = p1.y
                    cubicTo(cx1, cy1, cx2, cy2, p1.x, p1.y)
                }
            }
        }

        val strokeBrush = if (isCumulativeMode) {
            Brush.horizontalGradient(
                listOf(CelestialAmethystLight, EtherealCyan)
            )
        } else {
            Brush.horizontalGradient(
                listOf(CelestialAmethystLight, RadiantGoldBright, RadiantGold)
            )
        }

        drawPath(
            path = linePath,
            brush = strokeBrush,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // 4. Draw Peak Spike Burst Rings & Milestone Dots
        coords.forEachIndexed { i, coord ->
            val point = points[i]
            if (point.isSpike && !isCumulativeMode) {
                // Outer glowing pulse ring
                drawCircle(
                    color = RadiantGoldBright.copy(alpha = pulseAlpha * 0.4f),
                    radius = 8.dp.toPx(),
                    center = coord
                )
                // Inner spike dot
                drawCircle(
                    color = RadiantGoldBright,
                    radius = 4.dp.toPx(),
                    center = coord
                )
                drawCircle(
                    color = Color.White,
                    radius = 2.dp.toPx(),
                    center = coord
                )
            } else if (i == selectedIndex) {
                // Selected point highlight
                drawCircle(
                    color = if (isCumulativeMode) EtherealCyan.copy(alpha = 0.35f) else RadiantGold.copy(alpha = 0.35f),
                    radius = 9.dp.toPx(),
                    center = coord
                )
            }
        }

        // 5. Draw Active Scrubber Vertical Indicator (Recharts Tooltip Cursor)
        if (selectedIndex in coords.indices) {
            val selectedCoord = coords[selectedIndex]

            // Vertical dashed scrubber cursor
            drawLine(
                color = Color.White.copy(alpha = 0.75f),
                start = Offset(selectedCoord.x, paddingTop),
                end = Offset(selectedCoord.x, paddingTop + effectiveHeight),
                strokeWidth = 1.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
            )

            // Scrubber Target Center Dot
            drawCircle(
                color = if (isCumulativeMode) EtherealCyan else RadiantGoldBright,
                radius = 5.5.dp.toPx(),
                center = selectedCoord
            )
            drawCircle(
                color = Color(0xFF0F0A1E),
                radius = 2.5.dp.toPx(),
                center = selectedCoord
            )
        }

        // 6. Draw X-Axis Date Markers (Sample every N points)
        val xLabelStep = if (points.size <= 7) 1 else if (points.size <= 14) 2 else 5
        for (i in 0 until points.size step xLabelStep) {
            val xPos = coords[i].x
            val dateLabel = points[i].dateFormatted
            drawText(
                textMeasurer = textMeasurer,
                text = dateLabel,
                topLeft = Offset(xPos - 14f, paddingTop + effectiveHeight + 4f),
                style = TextStyle(
                    color = if (i == selectedIndex) RadiantGoldBright else TextMuted.copy(alpha = 0.8f),
                    fontSize = 8.5.sp,
                    fontWeight = if (i == selectedIndex) FontWeight.Bold else FontWeight.Normal
                )
            )
        }
    }
}
