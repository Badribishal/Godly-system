package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShadowType
import com.example.data.model.VirtueType
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ShadowItemRow(
    shadow: ShadowType,
    score: Int,
    isDominant: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = score.toFloat() / 100f,
        animationSpec = tween(600),
        label = "shadow_progress"
    )

    val shadowColor = Color(shadow.colorHex)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(
                width = if (isDominant) 1.5.dp else 1.dp,
                color = if (isDominant) shadowColor.copy(alpha = 0.8f) else SurfaceCardBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = shadow.runeSymbol, fontSize = 16.sp)
                    Text(
                        text = shadow.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDominant) shadowColor else TextPrimary,
                        fontWeight = if (isDominant) FontWeight.Bold else FontWeight.Medium
                    )
                    if (isDominant) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(shadowColor.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "DOMINANT",
                                color = shadowColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.titleMedium,
                    color = shadowColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // Glowing Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F0B1E))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0.02f, 1.0f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    shadowColor.copy(alpha = 0.5f),
                                    shadowColor
                                )
                            )
                        )
                )
            }

            Text(
                text = shadow.constructiveAspect,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun VirtueItemRow(
    virtue: VirtueType,
    score: Int,
    isDominant: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = score.toFloat() / 100f,
        animationSpec = tween(600),
        label = "virtue_progress"
    )

    val virtueColor = Color(virtue.colorHex)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(
                width = if (isDominant) 1.5.dp else 1.dp,
                color = if (isDominant) virtueColor.copy(alpha = 0.8f) else SurfaceCardBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = virtue.runeSymbol, fontSize = 16.sp)
                    Text(
                        text = virtue.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDominant) virtueColor else TextPrimary,
                        fontWeight = if (isDominant) FontWeight.Bold else FontWeight.Medium
                    )
                    if (isDominant) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(virtueColor.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "DOMINANT",
                                color = virtueColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.titleMedium,
                    color = virtueColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // Glowing Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F0B1E))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0.02f, 1.0f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    virtueColor.copy(alpha = 0.5f),
                                    virtueColor
                                )
                            )
                        )
                )
            }

            Text(
                text = virtue.constructiveAspect,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
    }
}
