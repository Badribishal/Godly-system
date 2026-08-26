package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class ShadowType(
    val displayName: String,
    val title: String,
    val description: String,
    val constructiveAspect: String,
    val excessWarning: String,
    val colorHex: Long,
    val runeSymbol: String
) {
    PRIDE(
        displayName = "Pride",
        title = "The Sovereign's Mantle",
        description = "Belief in one's own supreme capability, dignity, and autonomy.",
        constructiveAspect = "Fuels leadership, unshakable self-respect, and refusal to surrender to mediocrity.",
        excessWarning = "Blinds one to fatal misjudgments and isolates the soul in tyrannical vanity.",
        colorHex = 0xFFF59E0B, // Amber Gold
        runeSymbol = "👑"
    ),
    GREED(
        displayName = "Greed",
        title = "The Endless Grasp",
        description = "Relentless yearning for acquisition, mastery, and resources.",
        constructiveAspect = "Drives intense ambition, innovation, and strategic resource preparation.",
        excessWarning = "Corrodes trust, creates endless scarcity mindset, and hoards without purpose.",
        colorHex = 0xFF10B981, // Emerald Green
        runeSymbol = "🗝️"
    ),
    ENVY(
        displayName = "Envy",
        title = "The Rival's Fire",
        description = "Intense recognition of what others possess or achieve.",
        constructiveAspect = "Sparks fierce determination, rivalry, self-correction, and upward leaps in skill.",
        excessWarning = "Breeds bitter resentment, sabotage, and obsession with another's path.",
        colorHex = 0xFF06B6D4, // Cyan Ice
        runeSymbol = "⚡"
    ),
    WRATH(
        displayName = "Wrath",
        title = "The Blazing Aegis",
        description = "Explosive emotional surge against injustice, boundary violations, or weakness.",
        constructiveAspect = "Ignites righteous protection of the innocent and decisive boundary defense.",
        excessWarning = "Incinerates allies, clouds rational strategy, and leaves ash in its wake.",
        colorHex = 0xFFEF4444, // Crimson Red
        runeSymbol = "🔥"
    ),
    GLUTTONY(
        displayName = "Gluttony",
        title = "The Voracious Abyss",
        description = "Insatiable appetite for experience, sensation, knowledge, or sustenance.",
        constructiveAspect = "Spurs voracious learning, experiential depth, and passionate zest for life.",
        excessWarning = "Drowns self-discipline, leads to hedonistic burnout and chronic overconsumption.",
        colorHex = 0xFFEC4899, // Magenta Rose
        runeSymbol = "🌌"
    ),
    DESIRE(
        displayName = "Desire",
        title = "The Primordial Magnet",
        description = "Intense magnetic longing for intimacy, aesthetic beauty, and conquest.",
        constructiveAspect = "Creates captivating charisma, deep emotional bonding, and aesthetic mastery.",
        excessWarning = "Leads to compulsive distraction, emotional entanglements, and loss of sovereignty.",
        colorHex = 0xFF8B5CF6, // Amethyst Violet
        runeSymbol = "✨"
    ),
    SLOTH(
        displayName = "Sloth",
        title = "The Abyssal Stillness",
        description = "Deep inclination toward stillness, conservation of energy, and disengagement.",
        constructiveAspect = "Allows profound inner restoration, contemplation, and avoidance of futile conflicts.",
        excessWarning = "Paralyzes potential, fosters chronic avoidance, and lets destiny atrophy.",
        colorHex = 0xFF64748B, // Slate Silver
        runeSymbol = "🌙"
    )
}

enum class VirtueType(
    val displayName: String,
    val title: String,
    val description: String,
    val constructiveAspect: String,
    val excessWarning: String,
    val colorHex: Long,
    val runeSymbol: String
) {
    HUMILITY(
        displayName = "Humility",
        title = "The Root of Wisdom",
        description = "Honest self-appraisal and willingness to learn from all beings.",
        constructiveAspect = "Fosters deep wisdom, receptivity, unassailable composure, and perpetual growth.",
        excessWarning = "Can degenerate into self-erasure, timidity, and surrender of rightful authority.",
        colorHex = 0xFF60A5FA, // Sky Blue
        runeSymbol = "🌿"
    ),
    CHARITY(
        displayName = "Charity",
        title = "The Luminous Well",
        description = "Generous pouring out of care, wealth, strength, and guidance to others.",
        constructiveAspect = "Builds legendary alliances, lifts communities, and brings divine favor.",
        excessWarning = "Depletes own vessel, enables parasites, and neglects personal sanctuary.",
        colorHex = 0xFF34D399, // Mint Emerald
        runeSymbol = "🕊️"
    ),
    COURAGE(
        displayName = "Courage",
        title = "The Iron Heart",
        description = "Stepping unflinchingly into the unknown, dangerous, or terrifying.",
        constructiveAspect = "Overcomes impossible odds, breaks generational curses, and sparks breakthroughs.",
        excessWarning = "Morphs into reckless bravado, unnecessary martyrdom, and reckless casualty.",
        colorHex = 0xFFF97316, // Solar Orange
        runeSymbol = "🛡️"
    ),
    PATIENCE(
        displayName = "Patience",
        title = "The Timeless Mountain",
        description = "Enduring delays, provocation, and harsh seasons without breaking.",
        constructiveAspect = "Outlasts all storms, masters compounding mastery, and maintains divine peace.",
        excessWarning = "Turns into passive stagnation, tolerating abuse, and missed fleeting windows.",
        colorHex = 0xFFA855F7, // Deep Purple
        runeSymbol = "⏳"
    ),
    TEMPERANCE(
        displayName = "Temperance",
        title = "The Golden Harmony",
        description = "Mastery of moderation, self-restraint, and balanced equilibrium.",
        constructiveAspect = "Protects long-term vitality, emotional stability, and clarity of intent.",
        excessWarning = "Smothers spontaneous passion, numbs emotional ecstasy, and breeds rigidity.",
        colorHex = 0xFF38BDF8, // Cyan Sky
        runeSymbol = "⚖️"
    ),
    DILIGENCE(
        displayName = "Diligence",
        title = "The Relentless Forge",
        description = "Consistent, disciplined labor toward great works and refined virtue.",
        constructiveAspect = "Constructs lasting legacies, unstoppable momentum, and unmatched competence.",
        excessWarning = "Risks robotic exhaustion, worship of work over living, and brittle burnout.",
        colorHex = 0xFFEAB308, // Radiance Gold
        runeSymbol = "⚒️"
    ),
    GRATITUDE(
        displayName = "Gratitude",
        title = "The Sanctified Vessel",
        description = "Profound appreciation of life, gifts, mentors, and the cosmic fabric.",
        constructiveAspect = "Attracts abundance, transmutes sorrow into peace, and harmonizes the spirit.",
        excessWarning = "Can gloss over necessary discontent needed to dismantle unjust situations.",
        colorHex = 0xFFF472B6, // Rose Radiance
        runeSymbol = "🌸"
    )
}
