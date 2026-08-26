package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Clean, static ambient background canvas.
 * Renders an ethereal cosmic radial glow without any background animation loops,
 * preserving battery life while maintaining a polished, clutter-free aesthetic.
 */
@Composable
fun CosmicParticlesCanvas(
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Static ethereal ambient radial glow
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.08f),
                    backgroundColor.copy(alpha = 0.4f),
                    Color.Transparent
                ),
                center = Offset(width * 0.5f, height * 0.22f),
                radius = width * 0.85f
            )
        )
    }
}

