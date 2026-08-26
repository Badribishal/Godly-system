package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AuraType
import com.example.data.model.CosmeticCatalog
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.RadiantGold

/**
 * High-performance, clean, and battery-friendly avatar crest.
 * Uses crisp gradients and subtle static auras without continuous infinite transition loops.
 */
@Composable
fun CosmeticAvatarCrest(
    race: String,
    equippedEffectId: String?,
    modifier: Modifier = Modifier
) {
    val effect = CosmeticCatalog.getEffectById(equippedEffectId)

    val raceEmoji = when (race) {
        "Ancient Dragon", "Dragonborn", "Dragon Sovereign" -> "🐉"
        "Solar Seraph", "Celestial Angel", "Angel" -> "🪽"
        "Archdemon Sovereign", "Abyssal Demon", "Demon" -> "🔥"
        "Voidborn", "Astral Being" -> "🌌"
        "Phoenix Sovereign", "Phoenix" -> "🦅"
        "Archfey", "Fae" -> "✨"
        "Celestial Kitsune", "Kitsune" -> "🦊"
        "Primordial Titan", "Frost Titan", "Titan", "Giant" -> "⚡"
        "Vampire Progenitor" -> "🩸"
        "Starforged Golem" -> "⚙️"
        "Eldritch Hybrid" -> "👁️"
        "High Elf", "Elf", "Dark Elf", "Moon Elf" -> "🌿"
        else -> "⚔️"
    }

    Box(
        modifier = modifier.size(68.dp),
        contentAlignment = Alignment.Center
    ) {
        // Render crisp static aura backdrop based on effect type
        when (effect.auraType) {
            AuraType.AURORA -> {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFF06B6D4).copy(alpha = 0.75f),
                                    Color(0xFFA855F7).copy(alpha = 0.75f),
                                    Color(0xFF10B981).copy(alpha = 0.75f),
                                    Color(0xFF06B6D4).copy(alpha = 0.75f)
                                )
                            )
                        )
                )
            }
            AuraType.ECLIPSE -> {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF2B0A12),
                                    Color(0xFFEF4444).copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.8f), CircleShape)
                )
            }
            AuraType.SINGULARITY -> {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF070B1A),
                                    Color(0xFF38BDF8).copy(alpha = 0.6f),
                                    Color(0xFF4C1D95).copy(alpha = 0.4f)
                                )
                            )
                        )
                        .border(1.5.dp, Color(0xFF38BDF8).copy(alpha = 0.85f), CircleShape)
                )
            }
            AuraType.GOLDEN_HALO -> {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFFDE047).copy(alpha = 0.85f), CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFF59E0B).copy(alpha = 0.45f),
                                    Color(0xFF78350F).copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            AuraType.STARFALL -> {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color(0xFFF472B6).copy(alpha = 0.8f), CircleShape)
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFFF472B6).copy(alpha = 0.5f),
                                    Color(0xFF60A5FA).copy(alpha = 0.5f),
                                    Color(0xFFFDE047).copy(alpha = 0.5f),
                                    Color(0xFFF472B6).copy(alpha = 0.5f)
                                )
                            )
                        )
                )
            }
            AuraType.VERDANT_CROWN -> {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .border(1.8.dp, Color(0xFF10B981).copy(alpha = 0.85f), CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF064E3B).copy(alpha = 0.6f),
                                    Color(0xFF34D399).copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            AuraType.DRAGON_FLAME -> {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .border(1.8.dp, Color(0xFFEA580C).copy(alpha = 0.85f), CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFDC2626).copy(alpha = 0.7f),
                                    Color(0xFFEA580C).copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            AuraType.WINGS_OF_DAWN -> {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .border(1.8.dp, Color(0xFFFCD34D).copy(alpha = 0.85f), CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFFFFF).copy(alpha = 0.5f),
                                    Color(0xFFFDE047).copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            AuraType.DEFAULT -> {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    CelestialAmethyst.copy(alpha = 0.35f),
                                    Color(0xFF0F0B1E)
                                )
                            )
                        )
                        .border(1.2.dp, RadiantGold.copy(alpha = 0.75f), CircleShape)
                )
            }
        }

        // Inner Avatar Core
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF261A42),
                            Color(0xFF0C0819)
                        )
                    )
                )
                .border(1.dp, effect.primaryColor.copy(alpha = 0.7f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = raceEmoji,
                fontSize = 26.sp
            )
        }

        // Tiny cosmetic flare badge on bottom-right
        if (effect.auraType != AuraType.DEFAULT) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF140D26))
                    .border(0.8.dp, effect.primaryColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = effect.icon, fontSize = 10.sp)
            }
        }
    }
}

