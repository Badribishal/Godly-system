package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class EmotionValence(val displayName: String, val rune: String, val themeColorHex: Long) {
    POSITIVE("Positive Harmonic", "✨", 0xFF34D399),
    NEGATIVE("Negative Shadow", "🔥", 0xFFF87171)
}

data class EmotionItem(
    val id: String,
    val name: String,
    val valence: EmotionValence,
    val category: String,
    val runeIcon: String,
    val essence: String,
    val colorHex: Long,
    val associatedShadow: ShadowType? = null,
    val associatedVirtue: VirtueType? = null,
    val humanityShift: Int = 0,
    val stabilityShift: Int = 0
)

object EmotionCatalog {

    val POSITIVE_EMOTIONS: List<EmotionItem> = listOf(
        EmotionItem(
            id = "euphoria",
            name = "Euphoria",
            valence = EmotionValence.POSITIVE,
            category = "Joy & Radiance",
            runeIcon = "🌟",
            essence = "Radiant surge of ecstatic joy and celestial elevation",
            colorHex = 0xFFFFD700,
            associatedVirtue = VirtueType.GRATITUDE,
            humanityShift = 3,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "serenity",
            name = "Serenity",
            valence = EmotionValence.POSITIVE,
            category = "Peace & Stillness",
            runeIcon = "🕊️",
            essence = "Undisturbed stillness and profound inner tranquility",
            colorHex = 0xFF38BDF8,
            associatedVirtue = VirtueType.TEMPERANCE,
            humanityShift = 2,
            stabilityShift = 4
        ),
        EmotionItem(
            id = "triumph",
            name = "Triumph",
            valence = EmotionValence.POSITIVE,
            category = "Power & Sovereign",
            runeIcon = "👑",
            essence = "Victorious exultation from overcoming monumental trials",
            colorHex = 0xFFF59E0B,
            associatedShadow = ShadowType.PRIDE,
            associatedVirtue = VirtueType.COURAGE,
            humanityShift = 1,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "gratitude",
            name = "Gratitude",
            valence = EmotionValence.POSITIVE,
            category = "Joy & Radiance",
            runeIcon = "🌸",
            essence = "Deep sacred thankfulness for cosmic existence and gifts",
            colorHex = 0xFFF472B6,
            associatedVirtue = VirtueType.GRATITUDE,
            humanityShift = 4,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "courage",
            name = "Courage",
            valence = EmotionValence.POSITIVE,
            category = "Power & Sovereign",
            runeIcon = "🛡️",
            essence = "Unyielding fire stepping into the perilous unknown",
            colorHex = 0xFFF97316,
            associatedVirtue = VirtueType.COURAGE,
            humanityShift = 2,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "compassion",
            name = "Compassion",
            valence = EmotionValence.POSITIVE,
            category = "Love & Connection",
            runeIcon = "💖",
            essence = "Boundless empathy and unconditional benevolent care",
            colorHex = 0xFFEC4899,
            associatedVirtue = VirtueType.CHARITY,
            humanityShift = 5,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "wonder",
            name = "Wonder",
            valence = EmotionValence.POSITIVE,
            category = "Transcendence",
            runeIcon = "🌌",
            essence = "Awe-struck marvel at the infinite universe and mysteries",
            colorHex = 0xFFA855F7,
            associatedVirtue = VirtueType.HUMILITY,
            humanityShift = 3,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "inspiration",
            name = "Inspiration",
            valence = EmotionValence.POSITIVE,
            category = "Transcendence",
            runeIcon = "✨",
            essence = "Divine creative spark channeling higher vision",
            colorHex = 0xFFC084FC,
            associatedVirtue = VirtueType.DILIGENCE,
            humanityShift = 2,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "devotion",
            name = "Devotion",
            valence = EmotionValence.POSITIVE,
            category = "Love & Connection",
            runeIcon = "🕯️",
            essence = "Sacred steadfast dedication to an elevated purpose",
            colorHex = 0xFFEAB308,
            associatedVirtue = VirtueType.DILIGENCE,
            humanityShift = 3,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "hope",
            name = "Hope",
            valence = EmotionValence.POSITIVE,
            category = "Joy & Radiance",
            runeIcon = "🌱",
            essence = "Luminous dawn piercing through any impending darkness",
            colorHex = 0xFF34D399,
            associatedVirtue = VirtueType.PATIENCE,
            humanityShift = 3,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "confidence",
            name = "Confidence",
            valence = EmotionValence.POSITIVE,
            category = "Power & Sovereign",
            runeIcon = "💎",
            essence = "Unshakeable trust in innate sovereign capability",
            colorHex = 0xFF60A5FA,
            associatedShadow = ShadowType.PRIDE,
            associatedVirtue = VirtueType.COURAGE,
            humanityShift = 1,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "playfulness",
            name = "Playfulness",
            valence = EmotionValence.POSITIVE,
            category = "Joy & Radiance",
            runeIcon = "🎭",
            essence = "Joyful, lighthearted spontaneity and cosmic dance",
            colorHex = 0xFFFBBF24,
            associatedVirtue = VirtueType.TEMPERANCE,
            humanityShift = 3,
            stabilityShift = 1
        ),
        EmotionItem(
            id = "ecstasy",
            name = "Ecstasy",
            valence = EmotionValence.POSITIVE,
            category = "Transcendence",
            runeIcon = "🌈",
            essence = "Transcendent rapture merging with cosmic flow",
            colorHex = 0xFF818CF8,
            associatedVirtue = VirtueType.GRATITUDE,
            humanityShift = 2,
            stabilityShift = 1
        ),
        EmotionItem(
            id = "harmony",
            name = "Harmony",
            valence = EmotionValence.POSITIVE,
            category = "Peace & Stillness",
            runeIcon = "⚖️",
            essence = "Balanced alignment of mind, spirit, and body",
            colorHex = 0xFF2DD4BF,
            associatedVirtue = VirtueType.TEMPERANCE,
            humanityShift = 2,
            stabilityShift = 4
        ),
        EmotionItem(
            id = "tenderness",
            name = "Tenderness",
            valence = EmotionValence.POSITIVE,
            category = "Love & Connection",
            runeIcon = "🌷",
            essence = "Soft, gentle warmth embracing vulnerability with grace",
            colorHex = 0xFFFB7185,
            associatedVirtue = VirtueType.CHARITY,
            humanityShift = 4,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "enthusiasm",
            name = "Enthusiasm",
            valence = EmotionValence.POSITIVE,
            category = "Power & Sovereign",
            runeIcon = "🔥",
            essence = "Zealous, vibrant energy propelling immediate action",
            colorHex = 0xFFFB923C,
            associatedVirtue = VirtueType.DILIGENCE,
            humanityShift = 2,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "clarity",
            name = "Clarity",
            valence = EmotionValence.POSITIVE,
            category = "Peace & Stillness",
            runeIcon = "🔮",
            essence = "Crystalline lucidity cutting through all illusions",
            colorHex = 0xFF38BDF8,
            associatedVirtue = VirtueType.TEMPERANCE,
            humanityShift = 1,
            stabilityShift = 4
        ),
        EmotionItem(
            id = "empowerment",
            name = "Empowerment",
            valence = EmotionValence.POSITIVE,
            category = "Power & Sovereign",
            runeIcon = "⚡",
            essence = "Awakened realization of boundless personal agency",
            colorHex = 0xFFFCD34D,
            associatedVirtue = VirtueType.COURAGE,
            humanityShift = 2,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "contentment",
            name = "Contentment",
            valence = EmotionValence.POSITIVE,
            category = "Peace & Stillness",
            runeIcon = "☕",
            essence = "Quiet, restful satisfaction in the eternal present",
            colorHex = 0xFF86EFAC,
            associatedVirtue = VirtueType.PATIENCE,
            humanityShift = 2,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "reverence",
            name = "Reverence",
            valence = EmotionValence.POSITIVE,
            category = "Transcendence",
            runeIcon = "🏛️",
            essence = "Humbled sacred adoration of the sublime cosmos",
            colorHex = 0xFFDDD6FE,
            associatedVirtue = VirtueType.HUMILITY,
            humanityShift = 3,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "forgiveness",
            name = "Forgiveness",
            valence = EmotionValence.POSITIVE,
            category = "Love & Connection",
            runeIcon = "🌊",
            essence = "Liberating release of past grievances and emotional debt",
            colorHex = 0xFF67E8F9,
            associatedVirtue = VirtueType.CHARITY,
            humanityShift = 4,
            stabilityShift = 4
        ),
        EmotionItem(
            id = "vitality",
            name = "Vitality",
            valence = EmotionValence.POSITIVE,
            category = "Power & Sovereign",
            runeIcon = "☀️",
            essence = "Invigorating life force surging through physical vessels",
            colorHex = 0xFFFDE047,
            associatedVirtue = VirtueType.DILIGENCE,
            humanityShift = 2,
            stabilityShift = 3
        ),
        EmotionItem(
            id = "curiosity",
            name = "Curiosity",
            valence = EmotionValence.POSITIVE,
            category = "Transcendence",
            runeIcon = "🗝️",
            essence = "Eager philosophical exploration of hidden truths",
            colorHex = 0xFF93C5FD,
            associatedVirtue = VirtueType.HUMILITY,
            humanityShift = 2,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "affection",
            name = "Affection",
            valence = EmotionValence.POSITIVE,
            category = "Love & Connection",
            runeIcon = "🫂",
            essence = "Warm, endearing closeness and genuine connection",
            colorHex = 0xFFF472B6,
            associatedVirtue = VirtueType.CHARITY,
            humanityShift = 4,
            stabilityShift = 2
        ),
        EmotionItem(
            id = "invincibility",
            name = "Invincibility",
            valence = EmotionValence.POSITIVE,
            category = "Power & Sovereign",
            runeIcon = "⚔️",
            essence = "Adamantine fortitude unshakeable by external chaos",
            colorHex = 0xFFF59E0B,
            associatedVirtue = VirtueType.COURAGE,
            humanityShift = 0,
            stabilityShift = 4
        ),
        EmotionItem(
            id = "bliss",
            name = "Bliss",
            valence = EmotionValence.POSITIVE,
            category = "Peace & Stillness",
            runeIcon = "💫",
            essence = "Deep meditative contentment radiating stillness",
            colorHex = 0xFFE879F9,
            associatedVirtue = VirtueType.GRATITUDE,
            humanityShift = 3,
            stabilityShift = 3
        )
    )

    val NEGATIVE_EMOTIONS: List<EmotionItem> = listOf(
        EmotionItem(
            id = "wrath",
            name = "Wrath",
            valence = EmotionValence.NEGATIVE,
            category = "Anger & Fury",
            runeIcon = "🔥",
            essence = "Blazing fury against injustice or boundary violation",
            colorHex = 0xFFEF4444,
            associatedShadow = ShadowType.WRATH,
            associatedVirtue = VirtueType.COURAGE,
            humanityShift = -1,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "despair",
            name = "Despair",
            valence = EmotionValence.NEGATIVE,
            category = "Void & Emptiness",
            runeIcon = "🥀",
            essence = "Abyssal sinking into hopelessness and spiritual void",
            colorHex = 0xFF64748B,
            associatedShadow = ShadowType.SLOTH,
            humanityShift = -2,
            stabilityShift = -4
        ),
        EmotionItem(
            id = "anxiety",
            name = "Anxiety",
            valence = EmotionValence.NEGATIVE,
            category = "Fear & Dread",
            runeIcon = "⚡",
            essence = "Restless tremor of impending unknown catastrophe",
            colorHex = 0xFFF97316,
            associatedShadow = ShadowType.SLOTH,
            associatedVirtue = VirtueType.PATIENCE,
            humanityShift = 0,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "grief",
            name = "Grief",
            valence = EmotionValence.NEGATIVE,
            category = "Sorrow & Ache",
            runeIcon = "🌧️",
            essence = "Aching sorrow mourning lost connections or fragments",
            colorHex = 0xFF60A5FA,
            associatedVirtue = VirtueType.CHARITY,
            humanityShift = 2,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "bitterness",
            name = "Bitterness",
            valence = EmotionValence.NEGATIVE,
            category = "Envy & Spite",
            runeIcon = "🧪",
            essence = "Corrosive, lingering resentment poisoning the spirit",
            colorHex = 0xFF10B981,
            associatedShadow = ShadowType.ENVY,
            humanityShift = -3,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "terror",
            name = "Terror",
            valence = EmotionValence.NEGATIVE,
            category = "Fear & Dread",
            runeIcon = "🌑",
            essence = "Paralyzing primal dread before overwhelming peril",
            colorHex = 0xFF475569,
            associatedShadow = ShadowType.SLOTH,
            humanityShift = -1,
            stabilityShift = -5
        ),
        EmotionItem(
            id = "shame",
            name = "Shame",
            valence = EmotionValence.NEGATIVE,
            category = "Shame & Guilt",
            runeIcon = "⛓️",
            essence = "Painful conviction of intrinsic brokenness or flaw",
            colorHex = 0xFF9333EA,
            associatedShadow = ShadowType.PRIDE,
            associatedVirtue = VirtueType.HUMILITY,
            humanityShift = -2,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "envy",
            name = "Envy",
            valence = EmotionValence.NEGATIVE,
            category = "Envy & Spite",
            runeIcon = "🐍",
            essence = "Burning discontent triggered by another's prosperity",
            colorHex = 0xFF06B6D4,
            associatedShadow = ShadowType.ENVY,
            humanityShift = -3,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "loneliness",
            name = "Loneliness",
            valence = EmotionValence.NEGATIVE,
            category = "Void & Emptiness",
            runeIcon = "🕳️",
            essence = "Cold isolation wandering an unpopulated abyss",
            colorHex = 0xFF6B7280,
            associatedShadow = ShadowType.SLOTH,
            humanityShift = 1,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "guilt",
            name = "Guilt",
            valence = EmotionValence.NEGATIVE,
            category = "Shame & Guilt",
            runeIcon = "⚖️",
            essence = "Heavy conscience burdened by past missteps or neglect",
            colorHex = 0xFFA855F7,
            associatedVirtue = VirtueType.HUMILITY,
            humanityShift = 1,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "frustration",
            name = "Frustration",
            valence = EmotionValence.NEGATIVE,
            category = "Anger & Fury",
            runeIcon = "💥",
            essence = "Volcanic obstruction when momentum is denied",
            colorHex = 0xFFF43F5E,
            associatedShadow = ShadowType.WRATH,
            associatedVirtue = VirtueType.PATIENCE,
            humanityShift = 0,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "dread",
            name = "Dread",
            valence = EmotionValence.NEGATIVE,
            category = "Fear & Dread",
            runeIcon = "⏳",
            essence = "Heavy ominous certainty of oncoming suffering",
            colorHex = 0xFF4B5563,
            associatedShadow = ShadowType.SLOTH,
            humanityShift = -1,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "apathy",
            name = "Apathy",
            valence = EmotionValence.NEGATIVE,
            category = "Void & Emptiness",
            runeIcon = "🧊",
            essence = "Numb emotional paralysis and disengagement",
            colorHex = 0xFF94A3B8,
            associatedShadow = ShadowType.SLOTH,
            humanityShift = -3,
            stabilityShift = -1
        ),
        EmotionItem(
            id = "betrayal",
            name = "Betrayal",
            valence = EmotionValence.NEGATIVE,
            category = "Sorrow & Ache",
            runeIcon = "🗡️",
            essence = "Sting of broken allegiance leaving psychic wounds",
            colorHex = 0xFFDC2626,
            associatedShadow = ShadowType.WRATH,
            associatedVirtue = VirtueType.CHARITY,
            humanityShift = -1,
            stabilityShift = -4
        ),
        EmotionItem(
            id = "helplessness",
            name = "Helplessness",
            valence = EmotionValence.NEGATIVE,
            category = "Void & Emptiness",
            runeIcon = "🌪️",
            essence = "Disempowered drift in chaotic unyielding currents",
            colorHex = 0xFF64748B,
            associatedShadow = ShadowType.SLOTH,
            humanityShift = 0,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "humiliation",
            name = "Humiliation",
            valence = EmotionValence.NEGATIVE,
            category = "Shame & Guilt",
            runeIcon = "🍂",
            essence = "Crushing defeat and stripped dignity",
            colorHex = 0xFF78716C,
            associatedShadow = ShadowType.PRIDE,
            humanityShift = -1,
            stabilityShift = -4
        ),
        EmotionItem(
            id = "greed",
            name = "Greed",
            valence = EmotionValence.NEGATIVE,
            category = "Envy & Spite",
            runeIcon = "💰",
            essence = "Insatiable craving to hoard resources and mastery",
            colorHex = 0xFF059669,
            associatedShadow = ShadowType.GREED,
            humanityShift = -2,
            stabilityShift = 0
        ),
        EmotionItem(
            id = "paranoia",
            name = "Paranoia",
            valence = EmotionValence.NEGATIVE,
            category = "Fear & Dread",
            runeIcon = "👁️",
            essence = "Hyper-vigilant suspicion of concealed malevolence",
            colorHex = 0xFFD97706,
            associatedShadow = ShadowType.WRATH,
            humanityShift = -2,
            stabilityShift = -4
        ),
        EmotionItem(
            id = "regret",
            name = "Regret",
            valence = EmotionValence.NEGATIVE,
            category = "Sorrow & Ache",
            runeIcon = "📜",
            essence = "Haunting longing to rewrite unalterable decisions",
            colorHex = 0xFF8B5CF6,
            associatedShadow = ShadowType.SLOTH,
            associatedVirtue = VirtueType.DILIGENCE,
            humanityShift = 1,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "jealousy",
            name = "Jealousy",
            valence = EmotionValence.NEGATIVE,
            category = "Envy & Spite",
            runeIcon = "👁️‍🗨️",
            essence = "Defensive fear of losing cherished territory or love",
            colorHex = 0xFF0284C7,
            associatedShadow = ShadowType.ENVY,
            humanityShift = -2,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "melancholy",
            name = "Melancholy",
            valence = EmotionValence.NEGATIVE,
            category = "Sorrow & Ache",
            runeIcon = "🌙",
            essence = "Poetic, pensive sadness lingering in twilight",
            colorHex = 0xFF818CF8,
            associatedShadow = ShadowType.SLOTH,
            humanityShift = 1,
            stabilityShift = -1
        ),
        EmotionItem(
            id = "overwhelm",
            name = "Overwhelm",
            valence = EmotionValence.NEGATIVE,
            category = "Void & Emptiness",
            runeIcon = "🌊",
            essence = "Sensory and cognitive inundation breaking mental gates",
            colorHex = 0xFF0EA5E9,
            associatedShadow = ShadowType.SLOTH,
            associatedVirtue = VirtueType.TEMPERANCE,
            humanityShift = 0,
            stabilityShift = -3
        ),
        EmotionItem(
            id = "anguish",
            name = "Anguish",
            valence = EmotionValence.NEGATIVE,
            category = "Sorrow & Ache",
            runeIcon = "💔",
            essence = "Sharp visceral agony piercing the spiritual core",
            colorHex = 0xFFBE123C,
            associatedShadow = ShadowType.WRATH,
            humanityShift = 0,
            stabilityShift = -4
        ),
        EmotionItem(
            id = "disgust",
            name = "Disgust",
            valence = EmotionValence.NEGATIVE,
            category = "Anger & Fury",
            runeIcon = "🤢",
            essence = "Visceral revulsion and moral aversion to corruption",
            colorHex = 0xFF15803D,
            associatedShadow = ShadowType.GLUTTONY,
            associatedVirtue = VirtueType.TEMPERANCE,
            humanityShift = -1,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "alienation",
            name = "Alienation",
            valence = EmotionValence.NEGATIVE,
            category = "Void & Emptiness",
            runeIcon = "🛸",
            essence = "Feeling like an incomprehensible outcast to humanity",
            colorHex = 0xFF6366F1,
            associatedShadow = ShadowType.PRIDE,
            humanityShift = -2,
            stabilityShift = -2
        ),
        EmotionItem(
            id = "restlessness",
            name = "Restlessness",
            valence = EmotionValence.NEGATIVE,
            category = "Anger & Fury",
            runeIcon = "🌀",
            essence = "Turbulent, ungrounded urge to escape the present",
            colorHex = 0xFFFB923C,
            associatedShadow = ShadowType.DESIRE,
            humanityShift = 0,
            stabilityShift = -2
        )
    )

    val ALL_EMOTIONS: List<EmotionItem> = POSITIVE_EMOTIONS + NEGATIVE_EMOTIONS

    // Standardized 42 Tracked Emotions (21 Positive Harmonic & 21 Negative Shadow)
    val TRACKED_POSITIVE_21: List<EmotionItem> = POSITIVE_EMOTIONS.take(21)
    val TRACKED_NEGATIVE_21: List<EmotionItem> = NEGATIVE_EMOTIONS.take(21)
    val TRACKED_42_EMOTIONS: List<EmotionItem> = TRACKED_POSITIVE_21 + TRACKED_NEGATIVE_21

    fun getEmotionById(id: String): EmotionItem? {
        return ALL_EMOTIONS.find { it.id.equals(id, ignoreCase = true) }
    }

    fun getEmotionByName(name: String): EmotionItem? {
        return ALL_EMOTIONS.find { it.name.equals(name, ignoreCase = true) }
    }
}

fun EmotionItem.toEntity(): com.example.data.local.TrackedEmotionEntity {
    return com.example.data.local.TrackedEmotionEntity(
        id = id,
        name = name,
        valence = valence.name,
        category = category,
        runeIcon = runeIcon,
        essence = essence,
        colorHex = colorHex,
        associatedForce = associatedShadow?.name ?: associatedVirtue?.name
    )
}

// Property extension for secondary shadow if needed
val EmotionItem.associatedShadowSecondary: ShadowType?
    get() = if (id == "loneliness") ShadowType.DESIRE else null

