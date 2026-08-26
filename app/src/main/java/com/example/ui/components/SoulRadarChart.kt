package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.model.ShadowType
import com.example.data.model.VirtueType
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SoulRadarChart(
    shadowScores: Map<ShadowType, Int>,
    virtueScores: Map<VirtueType, Int>,
    modifier: Modifier = Modifier,
    isShadowMode: Boolean = true
) {
    val shadows = ShadowType.values()
    val virtues = VirtueType.values()
    val count = 7

    val animatedScores = if (isShadowMode) {
        shadows.map { shadow ->
            animateFloatAsState(
                targetValue = (shadowScores[shadow] ?: 30).toFloat() / 100f,
                animationSpec = tween(durationMillis = 800),
                label = shadow.name
            ).value
        }
    } else {
        virtues.map { virtue ->
            animateFloatAsState(
                targetValue = (virtueScores[virtue] ?: 30).toFloat() / 100f,
                animationSpec = tween(durationMillis = 800),
                label = virtue.name
            ).value
        }
    }

    val primaryColor = if (isShadowMode) Color(0xFFEF4444) else Color(0xFF38BDF8)
    val secondaryColor = if (isShadowMode) Color(0xFFA855F7) else Color(0xFFF59E0B)

    Box(
        modifier = modifier
            .size(240.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width.coerceAtMost(size.height) / 2 * 0.85f
            val angleStep = (2 * Math.PI / count).toFloat()

            // 1. Draw web grid rings (25%, 50%, 75%, 100%)
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
                    color = Color(0xFF332958).copy(alpha = if (scale == 1.0f) 0.6f else 0.25f),
                    style = Stroke(width = if (scale == 1.0f) 1.5f else 1.0f)
                )
            }

            // 2. Draw 7 spoke axes from center
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

            // 3. Draw the animated polygon data shape
            val polygonPath = Path()
            val pointList = mutableListOf<Offset>()
            for (i in 0 until count) {
                val score = animatedScores[i].coerceIn(0.1f, 1.0f)
                val angle = i * angleStep - (Math.PI / 2).toFloat()
                val x = center.x + radius * score * cos(angle)
                val y = center.y + radius * score * sin(angle)
                val point = Offset(x, y)
                pointList.add(point)
                if (i == 0) polygonPath.moveTo(x, y) else polygonPath.lineTo(x, y)
            }
            polygonPath.close()

            // Fill polygon with glowing gradient aura
            drawPath(
                path = polygonPath,
                color = (if (isShadowMode) primaryColor else secondaryColor).copy(alpha = 0.28f),
                style = Fill
            )

            // Stroke polygon edge
            drawPath(
                path = polygonPath,
                color = (if (isShadowMode) primaryColor else secondaryColor).copy(alpha = 0.85f),
                style = Stroke(width = 2.5f, cap = StrokeCap.Round)
            )

            // Draw glowing vertex nodes
            pointList.forEach { pt ->
                drawCircle(
                    color = Color.White,
                    radius = 3.5f,
                    center = pt
                )
                drawCircle(
                    color = if (isShadowMode) primaryColor else secondaryColor,
                    radius = 5.5f,
                    center = pt,
                    style = Stroke(width = 1.5f)
                )
            }
        }
    }
}
