package com.example.data.model

import androidx.compose.ui.graphics.Color

data class CosmeticEffect(
    val id: String,
    val name: String,
    val category: String,
    val cost: Int,
    val description: String,
    val icon: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val auraType: AuraType
)

enum class AuraType {
    DEFAULT,
    AURORA,
    ECLIPSE,
    SINGULARITY,
    GOLDEN_HALO,
    STARFALL,
    VERDANT_CROWN,
    DRAGON_FLAME,
    WINGS_OF_DAWN
}

object CosmeticCatalog {
    val ALL_EFFECTS = listOf(
        CosmeticEffect(
            id = "effect_default",
            name = "Genesis Starlight",
            category = "Basic Aura",
            cost = 0,
            description = "The foundational starlight resonance of a newly awakened soul vessel.",
            icon = "✨",
            primaryColor = Color(0xFFF59E0B),
            secondaryColor = Color(0xFF8B5CF6),
            auraType = AuraType.DEFAULT
        ),
        CosmeticEffect(
            id = "effect_aurora",
            name = "Celestial Aurora",
            category = "Astral",
            cost = 50,
            description = "Ethereal polar light ribbons swirling in cosmic balance across the soul spectrum.",
            icon = "🌌",
            primaryColor = Color(0xFF06B6D4),
            secondaryColor = Color(0xFFA855F7),
            auraType = AuraType.AURORA
        ),
        CosmeticEffect(
            id = "effect_eclipse",
            name = "Abyssal Eclipse",
            category = "Shadow Rift",
            cost = 75,
            description = "A dark sun coronary ring radiating intense crimson shadow pulses.",
            icon = "🌒",
            primaryColor = Color(0xFFEF4444),
            secondaryColor = Color(0xFF1E1B4B),
            auraType = AuraType.ECLIPSE
        ),
        CosmeticEffect(
            id = "effect_singularity",
            name = "Void Singularity",
            category = "Cosmic Void",
            cost = 100,
            description = "A localized gravitational distortion emitting cyan micro-sparks and void matter.",
            icon = "🌀",
            primaryColor = Color(0xFF38BDF8),
            secondaryColor = Color(0xFF4C1D95),
            auraType = AuraType.SINGULARITY
        ),
        CosmeticEffect(
            id = "effect_golden_halo",
            name = "Solar Golden Halo",
            category = "Solar Radiance",
            cost = 120,
            description = "A radiant solar halo forged from purified virtues, emitting warm golden flares.",
            icon = "☀️",
            primaryColor = Color(0xFFF59E0B),
            secondaryColor = Color(0xFFFDE047),
            auraType = AuraType.GOLDEN_HALO
        ),
        CosmeticEffect(
            id = "effect_starfall",
            name = "Starfall Sparks",
            category = "Constellation",
            cost = 150,
            description = "Cascading astral meteorites and glittering stardust showering around the vessel.",
            icon = "🌠",
            primaryColor = Color(0xFFF472B6),
            secondaryColor = Color(0xFF60A5FA),
            auraType = AuraType.STARFALL
        ),
        CosmeticEffect(
            id = "effect_verdant",
            name = "Verdant Spirit Bloom",
            category = "Primal Nature",
            cost = 180,
            description = "Ancient forest spirits manifesting as floating jade leaves and ethereal life blooms.",
            icon = "🌿",
            primaryColor = Color(0xFF10B981),
            secondaryColor = Color(0xFF34D399),
            auraType = AuraType.VERDANT_CROWN
        ),
        CosmeticEffect(
            id = "effect_dragon_flame",
            name = "Dragon Primordial Pyre",
            category = "Ancient Drake",
            cost = 250,
            description = "Roaring draconic embers and sovereign flame waves protecting the soul core.",
            icon = "🐉",
            primaryColor = Color(0xFFEA580C),
            secondaryColor = Color(0xFFDC2626),
            auraType = AuraType.DRAGON_FLAME
        ),
        CosmeticEffect(
            id = "effect_wings_of_dawn",
            name = "Seraph Wings of Dawn",
            category = "Divine Transcendence",
            cost = 300,
            description = "Six luminous angel wings of holy radiant energy framing your transcendent avatar.",
            icon = "🪽",
            primaryColor = Color(0xFFFCD34D),
            secondaryColor = Color(0xFFFFFFFF),
            auraType = AuraType.WINGS_OF_DAWN
        )
    )

    fun getEffectById(id: String?): CosmeticEffect {
        return ALL_EFFECTS.find { it.id == id } ?: ALL_EFFECTS.first()
    }
}
