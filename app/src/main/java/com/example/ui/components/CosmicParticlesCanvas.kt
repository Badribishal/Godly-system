package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Luminous celestial ambient background canvas.
 * Renders rich dual radial nebulae, cosmic dust, and constellation starlight
 * while maintaining smooth performance and energy efficiency.
 */
@Composable
fun CosmicParticlesCanvas(
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val backgroundColor = MaterialTheme.colorScheme.background

    val starOffsets = remember {
        listOf(
            Triple(0.12f, 0.08f, 1.8f),
            Triple(0.85f, 0.12f, 2.2f),
            Triple(0.25f, 0.22f, 1.4f),
            Triple(0.72f, 0.28f, 1.9f),
            Triple(0.08f, 0.45f, 1.2f),
            Triple(0.92f, 0.52f, 2.0f),
            Triple(0.38f, 0.65f, 1.5f),
            Triple(0.82f, 0.74f, 1.8f),
            Triple(0.18f, 0.85f, 2.4f),
            Triple(0.65f, 0.90f, 1.3f)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Upper celestial nebula glow (Primary Astral Aura)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.14f),
                    primaryColor.copy(alpha = 0.04f),
                    Color.Transparent
                ),
                center = Offset(width * 0.5f, height * 0.15f),
                radius = width * 0.9f
            )
        )

        // Lower-right secondary nebula glow (Ethereal Cosmic Drift)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    secondaryColor.copy(alpha = 0.09f),
                    secondaryColor.copy(alpha = 0.02f),
                    Color.Transparent
                ),
                center = Offset(width * 0.85f, height * 0.75f),
                radius = width * 0.75f
            )
        )

        // Subtle ambient constellation stars
        starOffsets.forEach { (xRel, yRel, radius) ->
            drawCircle(
                color = primaryColor.copy(alpha = 0.35f),
                radius = radius,
                center = Offset(width * xRel, height * yRel)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.6f),
                radius = radius * 0.5f,
                center = Offset(width * xRel, height * yRel)
            )
        }
    }
}


