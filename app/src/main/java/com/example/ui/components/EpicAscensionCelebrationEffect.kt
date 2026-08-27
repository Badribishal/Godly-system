package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class AscensionParticle(
    var x: Float,
    var y: Float,
    val speedX: Float,
    val speedY: Float,
    val size: Float,
    val color: Color,
    val maxAlpha: Float,
    val isStar: Boolean = false,
    val rotationSpeed: Float = 0f
)

@Composable
fun EpicAscensionCelebrationEffect(
    modifier: Modifier = Modifier,
    accentColor: Color = RadiantGold
) {
    // Infinite animations for continuous celebration
    val infiniteTransition = rememberInfiniteTransition(label = "ascension_loop")

    // Rotating celestial light rays
    val rayRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ray_rotation"
    )

    // Pulsing shockwave ring expansion
    val shockwaveProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shockwave_progress"
    )

    // Second offset shockwave
    val shockwave2Progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing, delayMillis = 1100),
            repeatMode = RepeatMode.Restart
        ),
        label = "shockwave2_progress"
    )

    // Cosmic Shimmer Sparks
    val particles = remember {
        val rng = Random(42)
        val palette = listOf(RadiantGoldBright, RadiantGold, CelestialAmethystLight, EtherealCyan, Color.White)
        List(45) {
            val angle = rng.nextFloat() * 2f * Math.PI.toFloat()
            val speed = rng.nextFloat() * 1.8f + 0.6f
            AscensionParticle(
                x = 0.5f,
                y = 0.5f,
                speedX = cos(angle) * speed,
                speedY = sin(angle) * speed - 0.4f, // upward drift
                size = rng.nextFloat() * 3.5f + 1.5f,
                color = palette[rng.nextInt(palette.size)],
                maxAlpha = rng.nextFloat() * 0.5f + 0.5f,
                isStar = rng.nextBoolean(),
                rotationSpeed = rng.nextFloat() * 4f - 2f
            )
        }
    }

    // Particle lifecycle animator
    val particleTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2f, height * 0.28f) // Aligned with the top badge
        val maxRadius = width.coerceAtLeast(height) * 0.75f

        // 1. Draw Rotating Celestial Sunburst Rays (Behind sigil)
        val numRays = 12
        val rayLength = maxRadius * 0.9f
        for (i in 0 until numRays) {
            val angle = Math.toRadians((i * (360f / numRays) + rayRotation).toDouble()).toFloat()
            val rayWidth = 0.12f // in radians
            val p1 = Offset(center.x + rayLength * cos(angle - rayWidth), center.y + rayLength * sin(angle - rayWidth))
            val p2 = Offset(center.x + rayLength * cos(angle + rayWidth), center.y + rayLength * sin(angle + rayWidth))

            val rayPath = Path().apply {
                moveTo(center.x, center.y)
                lineTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
                close()
            }

            drawPath(
                path = rayPath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.18f),
                        CelestialAmethyst.copy(alpha = 0.08f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = rayLength
                ),
                style = Fill
            )
        }

        // 2. Draw Expanding Celestial Shockwaves
        drawShockwave(
            center = center,
            progress = shockwaveProgress,
            maxRadius = maxRadius * 0.7f,
            color = accentColor
        )

        drawShockwave(
            center = center,
            progress = shockwave2Progress,
            maxRadius = maxRadius * 0.7f,
            color = EtherealCyan
        )

        // 3. Draw Orbiting/Rising Cosmic Sparks & Starbursts
        particles.forEachIndexed { idx, p ->
            val localT = (particleTime + (idx.toFloat() / particles.size.toFloat())) % 1f
            val curDist = localT * maxRadius * 0.65f
            val px = center.x + p.speedX * curDist * 0.7f
            val py = center.y + p.speedY * curDist * 0.7f

            val alpha = (sin(localT * Math.PI.toFloat()) * p.maxAlpha).coerceIn(0f, 1f)

            if (p.isStar) {
                drawStar(
                    center = Offset(px, py),
                    size = p.size.dp.toPx() * (1f + localT * 0.5f),
                    color = p.color.copy(alpha = alpha.toFloat())
                )
            } else {
                drawCircle(
                    color = p.color.copy(alpha = alpha.toFloat()),
                    radius = p.size.dp.toPx() * (1f - localT * 0.3f),
                    center = Offset(px, py)
                )
            }
        }
    }
}

private fun DrawScope.drawShockwave(
    center: Offset,
    progress: Float,
    maxRadius: Float,
    color: Color
) {
    val radius = progress * maxRadius
    val alpha = ((1f - progress) * 0.6f).coerceIn(0f, 1f)
    if (alpha > 0.01f && radius > 0f) {
        drawCircle(
            color = color.copy(alpha = alpha),
            radius = radius,
            center = center,
            style = Stroke(width = (2.5f * (1f - progress) + 0.5f).dp.toPx())
        )
    }
}

private fun DrawScope.drawStar(
    center: Offset,
    size: Float,
    color: Color
) {
    // 4-point star diamond
    val path = Path().apply {
        moveTo(center.x, center.y - size * 1.6f)
        lineTo(center.x + size * 0.4f, center.y - size * 0.4f)
        lineTo(center.x + size * 1.6f, center.y)
        lineTo(center.x + size * 0.4f, center.y + size * 0.4f)
        lineTo(center.x, center.y + size * 1.6f)
        lineTo(center.x - size * 0.4f, center.y + size * 0.4f)
        lineTo(center.x - size * 1.6f, center.y)
        lineTo(center.x - size * 0.4f, center.y - size * 0.4f)
        close()
    }
    drawPath(path = path, color = color, style = Fill)
}
