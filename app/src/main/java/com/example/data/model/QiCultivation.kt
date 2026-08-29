package com.example.data.model

enum class CultivationRealm(
    val id: String,
    val displayName: String,
    val maxStages: Int,
    val baseMaxQi: Int,
    val qiMultiplier: Float,
    val defaultTitle: String,
    val runeSymbol: String,
    val colorHex: Long,
    val description: String
) {
    QI_CONDENSATION(
        id = "realm_qi_condensation",
        displayName = "Qi Condensation",
        maxStages = 9,
        baseMaxQi = 300,
        qiMultiplier = 1.0f,
        defaultTitle = "Initiate of the Breath",
        runeSymbol = "🌬️",
        colorHex = 0xFF38BDF8,
        description = "Opening the subtle meridians and gathering ambient spiritual aether into the lower Dantian."
    ),
    FOUNDATION_ESTABLISHMENT(
        id = "realm_foundation_establishment",
        displayName = "Foundation Establishment",
        maxStages = 4,
        baseMaxQi = 1500,
        qiMultiplier = 1.6f,
        defaultTitle = "Aether Foundation Master",
        runeSymbol = "🌿",
        colorHex = 0xFF34D399,
        description = "Solidifying spiritual liquid into an unbreakable foundation beneath the celestial pillar."
    ),
    CORE_FORMATION(
        id = "realm_core_formation",
        displayName = "Golden Core Formation",
        maxStages = 4,
        baseMaxQi = 5000,
        qiMultiplier = 2.5f,
        defaultTitle = "Golden Elixir Sovereign",
        runeSymbol = "☀️",
        colorHex = 0xFFFBBF24,
        description = "Condensing the sea of Qi into a revolving, radiant Golden Core of eternal energy."
    ),
    NASCENT_SOUL(
        id = "realm_nascent_soul",
        displayName = "Nascent Soul",
        maxStages = 4,
        baseMaxQi = 18000,
        qiMultiplier = 4.0f,
        defaultTitle = "Astral Infant Monarch",
        runeSymbol = "🔮",
        colorHex = 0xFFA855F7,
        description = "Birth of the second spirit body, capable of projecting consciousness across the cosmic veil."
    ),
    SOUL_SEVERING(
        id = "realm_soul_severing",
        displayName = "Soul Severing & Void Refinement",
        maxStages = 4,
        baseMaxQi = 60000,
        qiMultiplier = 6.5f,
        defaultTitle = "Void Cleaver Archon",
        runeSymbol = "🌌",
        colorHex = 0xFFEC4899,
        description = "Severing mortal attachments to merge the primordial mind with the laws of Heaven and Earth."
    ),
    TRIBULATION_TRANSCENDENCE(
        id = "realm_tribulation_transcendence",
        displayName = "Tribulation Transcendence",
        maxStages = 9,
        baseMaxQi = 200000,
        qiMultiplier = 10.0f,
        defaultTitle = "Heaven-Defying Ascendant",
        runeSymbol = "⚡",
        colorHex = 0xFF6366F1,
        description = "Enduring nine heavenly lightning tribulations to cleanse all mortal impurities."
    ),
    CELESTIAL_DIVINITY(
        id = "realm_celestial_divinity",
        displayName = "Celestial Immortal Divinity",
        maxStages = 1,
        baseMaxQi = 800000,
        qiMultiplier = 20.0f,
        defaultTitle = "Supreme Dao Primordial",
        runeSymbol = "👑",
        colorHex = 0xFFFFD700,
        description = "Omnipresent oneness with the Grand Dao, wielding boundless Aetheric Omnipotence."
    );

    companion object {
        fun fromNameOrId(value: String): CultivationRealm {
            return entries.firstOrNull { 
                it.name.equals(value, ignoreCase = true) ||
                it.id.equals(value, ignoreCase = true) ||
                it.displayName.equals(value, ignoreCase = true)
            } ?: QI_CONDENSATION
        }
    }
}

enum class SpiritItemType(val displayName: String, val rune: String) {
    ELIXIR_PILL("Spiritual Elixirs & Pills", "🧪"),
    SPIRIT_ARTIFACT("Magical Artifacts & Relics", "🪞"),
    SUTRA_MANUAL("Daoist Sutras & Manuals", "📜"),
    TRIBULATION_TALISMAN("Heavenly Talismans", "🏮")
}

data class SpiritShopItem(
    val id: String,
    val name: String,
    val type: SpiritItemType,
    val description: String,
    val gemCost: Int,
    val iconEmoji: String,
    val qiBonus: Int = 0,
    val maxQiBonus: Int = 0,
    val breakthroughBonusPercent: Int = 0,
    val stabilityBonus: Int = 0,
    val humanityBonus: Int = 0,
    val passiveQiPerMinute: Int = 0,
    val isPermanentArtifact: Boolean = false,
    val isConsumable: Boolean = true,
    val rarity: String = "Rare", // Common, Rare, Epic, Legendary, Mythic
    val colorHex: Long = 0xFF38BDF8
)

object SpiritTreasuryCatalog {
    val ITEMS: List<SpiritShopItem> = listOf(
        // === ELIXIRS & PILLS (Consumables) ===
        SpiritShopItem(
            id = "pill_spirit_gathering",
            name = "Spirit Gathering Pill",
            type = SpiritItemType.ELIXIR_PILL,
            description = "Refined from celestial morning dew. Instantly infuses +250 Qi into your dantian.",
            gemCost = 35,
            iconEmoji = "💊",
            qiBonus = 250,
            rarity = "Common",
            colorHex = 0xFF38BDF8
        ),
        SpiritShopItem(
            id = "pill_pure_yang",
            name = "Pure Yang Golden Elixir",
            type = SpiritItemType.ELIXIR_PILL,
            description = "Cleanses mental shadow fatigue and surges +750 Qi directly into the spiritual meridians.",
            gemCost = 75,
            iconEmoji = "✨",
            qiBonus = 750,
            stabilityBonus = 5,
            rarity = "Rare",
            colorHex = 0xFFFBBF24
        ),
        SpiritShopItem(
            id = "pill_foundation_solidifying",
            name = "Foundation Solidifying Pill",
            type = SpiritItemType.ELIXIR_PILL,
            description = "Tempers your core foundation. Permanently increases Max Qi capacity by +400 and adds +20% Breakthrough chance.",
            gemCost = 110,
            iconEmoji = "🏺",
            maxQiBonus = 400,
            breakthroughBonusPercent = 20,
            rarity = "Epic",
            colorHex = 0xFF34D399
        ),
        SpiritShopItem(
            id = "pill_mind_clarity_dew",
            name = "Mind Clarity Dew",
            type = SpiritItemType.ELIXIR_PILL,
            description = "Brewed from mountain moon blossoms. Restores +15 Stability and +10 Humanity.",
            gemCost = 50,
            iconEmoji = "💧",
            stabilityBonus = 15,
            humanityBonus = 10,
            qiBonus = 150,
            rarity = "Rare",
            colorHex = 0xFF67E8F9
        ),
        SpiritShopItem(
            id = "pill_void_nirvana",
            name = "Void Nirvana Pill",
            type = SpiritItemType.ELIXIR_PILL,
            description = "A supreme mythical pill. Grants +2,500 Qi, +800 Max Qi, and +35% Breakthrough protection.",
            gemCost = 220,
            iconEmoji = "🌌",
            qiBonus = 2500,
            maxQiBonus = 800,
            breakthroughBonusPercent = 35,
            stabilityBonus = 20,
            rarity = "Mythic",
            colorHex = 0xFFFFD700
        ),

        // === MAGICAL ARTIFACTS (Permanent Equippable Relics) ===
        SpiritShopItem(
            id = "artifact_nine_lotus",
            name = "Nine-Petal Celestial Lotus",
            type = SpiritItemType.SPIRIT_ARTIFACT,
            description = "Passively gathers ambient celestial energy, granting +8 Qi every meditation pulse.",
            gemCost = 180,
            iconEmoji = "🪷",
            passiveQiPerMinute = 8,
            maxQiBonus = 500,
            isPermanentArtifact = true,
            isConsumable = false,
            rarity = "Epic",
            colorHex = 0xFF34D399
        ),
        SpiritShopItem(
            id = "artifact_yin_yang_mirror",
            name = "Prismatic Yin-Yang Mirror",
            type = SpiritItemType.SPIRIT_ARTIFACT,
            description = "Harmonizes dual forces of shadow and virtue, converting emotional reflections into +35% bonus Qi.",
            gemCost = 240,
            iconEmoji = "🪞",
            maxQiBonus = 1000,
            breakthroughBonusPercent = 15,
            isPermanentArtifact = true,
            isConsumable = false,
            rarity = "Legendary",
            colorHex = 0xFFA855F7
        ),
        SpiritShopItem(
            id = "artifact_dragon_orb",
            name = "Dragon-Vein Spirit Orb",
            type = SpiritItemType.SPIRIT_ARTIFACT,
            description = "Houses the dormant will of an astral dragon. Expands Qi meridian capacity by +2,500.",
            gemCost = 320,
            iconEmoji = "🔮",
            maxQiBonus = 2500,
            passiveQiPerMinute = 15,
            isPermanentArtifact = true,
            isConsumable = false,
            rarity = "Mythic",
            colorHex = 0xFFFFD700
        ),

        // === DAOIST SUTRAS & MANUALS (Permanent Techniques) ===
        SpiritShopItem(
            id = "sutra_boundless_light",
            name = "Heart Sutra of Boundless Light",
            type = SpiritItemType.SUTRA_MANUAL,
            description = "Ancient scripture teaching total mental clarity. Grants +1,200 Qi and +10% Breakthrough power.",
            gemCost = 140,
            iconEmoji = "📜",
            qiBonus = 1200,
            breakthroughBonusPercent = 10,
            humanityBonus = 12,
            isPermanentArtifact = true,
            isConsumable = false,
            rarity = "Epic",
            colorHex = 0xFFFBBF24
        ),
        SpiritShopItem(
            id = "sutra_primordial_chaos",
            name = "Primordial Chaos Breath Technique",
            type = SpiritItemType.SUTRA_MANUAL,
            description = "Mastery of the primordial breath. Triples the efficiency of manual Qi cultivation sessions.",
            gemCost = 210,
            iconEmoji = "🌀",
            maxQiBonus = 1500,
            passiveQiPerMinute = 12,
            isPermanentArtifact = true,
            isConsumable = false,
            rarity = "Legendary",
            colorHex = 0xFF6366F1
        ),

        // === TRIBULATION TALISMANS (Protection & Guarantees) ===
        SpiritShopItem(
            id = "talisman_heaven_cleaving",
            name = "Heaven-Cleaving Tribulation Talisman",
            type = SpiritItemType.TRIBULATION_TALISMAN,
            description = "Empowered by celestial decree. Guarantees 100% SUCCESS on your next Realm Breakthrough attempt!",
            gemCost = 160,
            iconEmoji = "🏮",
            breakthroughBonusPercent = 100,
            rarity = "Legendary",
            colorHex = 0xFFF43F5E
        )
    )

    fun getItemById(id: String): SpiritShopItem? = ITEMS.firstOrNull { it.id == id }
}
