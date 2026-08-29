package com.example.data.model

enum class ElementType(
    val id: String,
    val displayName: String,
    val runeSymbol: String,
    val colorHex: Long,
    val masteryTitle: String,
    val description: String
) {
    FIRE(
        id = "element_fire",
        displayName = "Fire",
        runeSymbol = "🔥",
        colorHex = 0xFFEF4444,
        masteryTitle = "Pyromancer",
        description = "Primal thermal combustion, blazing destruction, and phoenix-like cellular renewal."
    ),
    WATER(
        id = "element_water",
        displayName = "Water",
        runeSymbol = "🌊",
        colorHex = 0xFF0EA5E9,
        masteryTitle = "Hydromancer",
        description = "Fluid adaptability, sovereign tidal pressure, kinetic redirection, and sanative currents."
    ),
    AIR(
        id = "element_air",
        displayName = "Air",
        runeSymbol = "🌪️",
        colorHex = 0xFF38BDF8,
        masteryTitle = "Aeromancer",
        description = "Atmospheric velocity, vacuum slicing gales, barometric control, and pranic respiration."
    ),
    EARTH(
        id = "element_earth",
        displayName = "Earth",
        runeSymbol = "⛰️",
        colorHex = 0xFF10B981,
        masteryTitle = "Terramancer",
        description = "Unshakable tectonic density, bedrock shaping, granite bastion defense, and mineral vitality."
    ),
    LIGHTNING(
        id = "element_lightning",
        displayName = "Lightning",
        runeSymbol = "⚡",
        colorHex = 0xFFF59E0B,
        masteryTitle = "Electromancer",
        description = "Heavenly ion discharge, electromagnetic control, synaptic overdrive, and instantaneous reflex."
    ),
    ICE(
        id = "element_ice",
        displayName = "Ice",
        runeSymbol = "❄️",
        colorHex = 0xFF06B6D4,
        masteryTitle = "Cryomancer",
        description = "Absolute zero thermal drain, crystalline stasis lattices, permafrost auras, and cold serenity."
    ),
    DARK(
        id = "element_dark",
        displayName = "Dark",
        runeSymbol = "🌑",
        colorHex = 0xFF7C3AED,
        masteryTitle = "Shadowmancer",
        description = "Abyssal antimatter, umbral silhouette transposition, intent absorption, and nocturnal vigor."
    ),
    LIGHT(
        id = "element_light",
        displayName = "Light",
        runeSymbol = "✨",
        colorHex = 0xFFFBBF24,
        masteryTitle = "Lumimancer",
        description = "Photonic radiance, solar dawn lances, seraphic wards, and miraculous holistic restoration."
    ),
    GRAVITY(
        id = "element_gravity",
        displayName = "Gravity",
        runeSymbol = "🪐",
        colorHex = 0xFF6366F1,
        masteryTitle = "Gravimancer",
        description = "Singularity event horizons, vector manipulation, heavy anchor wards, and weightless drift."
    ),
    COSMIC(
        id = "element_cosmic",
        displayName = "Cosmic",
        runeSymbol = "🌌",
        colorHex = 0xFFEC4899,
        masteryTitle = "Astromancer",
        description = "Supernova cataclysms, stellar constellation weaving, and astral consciousness projection."
    ),
    VOID(
        id = "element_void",
        displayName = "Void",
        runeSymbol = "🕳️",
        colorHex = 0xFF475569,
        masteryTitle = "Voidmancer",
        description = "Null oblivion erasure, dimensional spacetime rifts, and entropy curse nullification."
    ),
    TIME(
        id = "element_time",
        displayName = "Time",
        runeSymbol = "⏳",
        colorHex = 0xFFA855F7,
        masteryTitle = "Chronomancer",
        description = "Temporal stasis cleaves, chronological dilation acceleration, and cellular timeline rewind."
    ),
    NATURE(
        id = "element_nature",
        displayName = "Nature",
        runeSymbol = "🌿",
        colorHex = 0xFF22C55E,
        masteryTitle = "Biomancer",
        description = "Living ancient roots, botanical shapeshifting, symbiotic spore blooms, and continuous regeneration."
    ),
    SOUL(
        id = "element_soul",
        displayName = "Soul",
        runeSymbol = "🔮",
        colorHex = 0xFF818CF8,
        masteryTitle = "Pneumamancer",
        description = "Astral spirit cleaves, consciousness construct weaving, telepathic harmony, and vessel mending."
    );

    companion object {
        fun fromString(value: String): ElementType {
            val normalized = value.trim().lowercase()
            return entries.firstOrNull { 
                it.name.lowercase() == normalized || 
                it.displayName.lowercase() == normalized ||
                normalized.contains(it.name.lowercase()) ||
                normalized.contains(it.displayName.lowercase())
            } ?: FIRE
        }
    }
}

enum class PowerCategory(
    val id: String,
    val displayName: String,
    val runeSymbol: String,
    val colorHex: Long,
    val roleDescription: String
) {
    ATTACK_ART(
        id = "cat_attack",
        displayName = "Power Attack",
        runeSymbol = "⚔️",
        colorHex = 0xFFEF4444,
        roleDescription = "High-impact offensive strikes, elemental cleaves, energy beams, and cataclysms."
    ),
    MANIPULATION_ART(
        id = "cat_manipulation",
        displayName = "Art Manipulation",
        runeSymbol = "🌀",
        colorHex = 0xFF38BDF8,
        roleDescription = "Shaping vectors, environmental control, mirages, spatial rifts, and kinetic redirection."
    ),
    SUPPORT_CLASS(
        id = "cat_support",
        displayName = "Support Class",
        runeSymbol = "🛡️",
        colorHex = 0xFFF59E0B,
        roleDescription = "Fortified auras, speed accelerations, protective barriers, and sensory enhancements."
    ),
    HEALING_CLASS(
        id = "cat_healing",
        displayName = "Healing Class",
        runeSymbol = "💚",
        colorHex = 0xFF10B981,
        roleDescription = "Cellular regeneration, emotional soothing, poison purging, and temporal wound rewinds."
    ),
    PASSIVE_TRAIT(
        id = "cat_trait",
        displayName = "Elemental Trait",
        runeSymbol = "🧬",
        colorHex = 0xFFA855F7,
        roleDescription = "Permanent inherent bloodline traits, resonance bonuses, and passive elemental blessings."
    )
}

data class ElementalPower(
    val id: String,
    val name: String,
    val element: ElementType,
    val category: PowerCategory,
    val runeSymbol: String,
    val description: String,
    val combatEffect: String,
    val mindSpiritEffect: String,
    val qiCost: Int,
    val cooldownTurns: Int,
    val rarity: String = "Rare", // Initiate, Adept, Master, Sovereign, Mythic
    val colorHex: Long = element.colorHex,
    val unlockLevel: Int = 1,
    val tags: List<String> = emptyList()
)

object ElementalPowersCatalog {

    val ALL_POWERS: List<ElementalPower> = listOf(
        // ==========================================
        // 1. FIRE (Pyromancy & Destruction)
        // ==========================================
        ElementalPower(
            id = "atk_fire_sunflare",
            name = "Infernal Sunflare Cleave",
            element = ElementType.FIRE,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🔥",
            description = "Compresses intense solar flames into an incandescent plasma blade that severs darkness.",
            combatEffect = "Deals 280% Blazing Solar Damage + Applies Scorched Soul burn.",
            mindSpiritEffect = "Incinerates hesitation and instills fearless decisive courage.",
            qiCost = 45,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Burn", "Solar")
        ),
        ElementalPower(
            id = "man_fire_pyrokinesis",
            name = "Pyrokinesis Thermal Shaper",
            element = ElementType.FIRE,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🌋",
            description = "Guides and shapes ambient thermal energy, bending fires into defensive spirals or precision darts.",
            combatEffect = "Controls battlefield temperature, diverting 40% incoming kinetic attacks.",
            mindSpiritEffect = "Refines chaotic emotions into focused alchemical passion.",
            qiCost = 30,
            cooldownTurns = 1,
            rarity = "Initiate",
            unlockLevel = 1,
            tags = listOf("Control", "Thermal", "Vector")
        ),
        ElementalPower(
            id = "sup_fire_blaze_mantle",
            name = "Blazing Corona Mantle",
            element = ElementType.FIRE,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🛡️",
            description = "Cloaks the vessel in an armor of radiant fire that retaliates against hostile intentions.",
            combatEffect = "Grants +35% Attack Power and reflects 25% damage to attackers.",
            mindSpiritEffect = "Protects willpower against spiritual intimidation and despair.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Buff", "Retaliation", "Armor")
        ),
        ElementalPower(
            id = "heal_fire_phoenix_rebirth",
            name = "Phoenix Flame Rejuvenation",
            element = ElementType.FIRE,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🦅",
            description = "Channels sacred emerald and gold phoenix flames to incinerate poisons and restore vitality.",
            combatEffect = "Instantly restores 45% Vessel Health and cleanses all debuffs.",
            mindSpiritEffect = "Transmutes past grief and burnout into renewed life force.",
            qiCost = 55,
            cooldownTurns = 4,
            rarity = "Sovereign",
            unlockLevel = 5,
            tags = listOf("Healing", "Cleanse", "Phoenix")
        ),
        ElementalPower(
            id = "trait_fire_pyre_heart",
            name = "Heart of the Eternal Pyre",
            element = ElementType.FIRE,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "❤️‍🔥",
            description = "The inner spirit burns with unquenchable volcanic ignition, gaining strength when challenged.",
            combatEffect = "Passive: Increases Qi generation by +15% and Fire Art potency by +25%.",
            mindSpiritEffect = "Maintains radiant optimism even in the darkest circumstances.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 2,
            tags = listOf("Passive", "Bloodline", "Ignition")
        ),

        // ==========================================
        // 2. WATER (Hydromancy & Adaptation)
        // ==========================================
        ElementalPower(
            id = "atk_water_tidal_spear",
            name = "Tidal Surge Hydro-Lance",
            element = ElementType.WATER,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🌊",
            description = "Pressurizes ocean depths into an ultra-dense aquatic spear capable of piercing armor.",
            combatEffect = "Deals 260% Hydro Piercing Damage + Pierces 50% target defense.",
            mindSpiritEffect = "Imparts absolute mental calm under turbulent external pressure.",
            qiCost = 40,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Piercing", "Tidal")
        ),
        ElementalPower(
            id = "man_water_fluid_redirection",
            name = "Hydrokinetic Flow Deflection",
            element = ElementType.WATER,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "💧",
            description = "Emulates fluid mechanics to absorb and harmlessly redirect enemy momentum.",
            combatEffect = "Deflects 60% incoming impact into an explosive counter-wave.",
            mindSpiritEffect = "Cultivates effortless psychological adaptability and non-attachment.",
            qiCost = 35,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Control", "Parry", "Redirection")
        ),
        ElementalPower(
            id = "sup_water_mist_veil",
            name = "Mist Veil Mirage Ward",
            element = ElementType.WATER,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🌫️",
            description = "Envelops the vessel in refractile ocean mist, confusing enemy targeting.",
            combatEffect = "Grants +40% Evasion and suppresses hostile tracking.",
            mindSpiritEffect = "Shields private thoughts from psychic intrusion and noise.",
            qiCost = 30,
            cooldownTurns = 3,
            rarity = "Initiate",
            unlockLevel = 1,
            tags = listOf("Support", "Stealth", "Evasion")
        ),
        ElementalPower(
            id = "heal_water_ocean_embrace",
            name = "Sovereign Ocean's Sanative Wave",
            element = ElementType.WATER,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🐬",
            description = "Calls down healing waters from the primordial celestial spring to soothe the nervous system.",
            combatEffect = "Restores 30% Health immediately + 10% Regeneration for 3 turns.",
            mindSpiritEffect = "Alleviates chronic anxiety and restores emotional homeostasis.",
            qiCost = 50,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Healing", "Regen", "Sanative")
        ),
        ElementalPower(
            id = "trait_water_tidal_harmony",
            name = "Primordial Tide Bloodline",
            element = ElementType.WATER,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "🧜",
            description = "Vessel blood resonates with the celestial oceans, granting infinite fluidity.",
            combatEffect = "Passive: Reduces all skill cooldowns by 1 turn and increases Water mastery by +25%.",
            mindSpiritEffect = "Unshakeable composure that remains serene during any crisis.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 2,
            tags = listOf("Passive", "Bloodline", "Harmony")
        ),

        // ==========================================
        // 3. AIR (Aeromancy & Agility)
        // ==========================================
        ElementalPower(
            id = "atk_air_vortex_tempest",
            name = "Vortex Blade Tempest",
            element = ElementType.AIR,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🌪️",
            description = "Unleashes razor-sharp vacuum blades compressed within an accelerating cyclone.",
            combatEffect = "Strikes all enemies for 240% Aero Slashing Damage + Inflicts Bleed.",
            mindSpiritEffect = "Blows away mental stagnation, bringing crystalline intellectual clarity.",
            qiCost = 45,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Area", "Cyclone")
        ),
        ElementalPower(
            id = "man_air_barometric_control",
            name = "Atmospheric Vector Domain",
            element = ElementType.AIR,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "💨",
            description = "Alters local air pressure gradients to launch allies forward or paralyze foes in vacuum.",
            combatEffect = "Controls airflow to suspend enemies in midair, reducing speed by 50%.",
            mindSpiritEffect = "Broadens strategic perspective to encompass high-altitude overviews.",
            qiCost = 35,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Control", "Slow", "Pressure")
        ),
        ElementalPower(
            id = "sup_air_tailwind_haste",
            name = "Celestial Zephyr Tailwind",
            element = ElementType.AIR,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🍃",
            description = "Bestows aerodynamic lift, allowing lightning-fast steps and frictionless agility.",
            combatEffect = "Increases Movement & Action Speed by +45% for the party.",
            mindSpiritEffect = "Lifts heaviness of heart, inspiring playful curiosity and quick action.",
            qiCost = 35,
            cooldownTurns = 3,
            rarity = "Initiate",
            unlockLevel = 1,
            tags = listOf("Buff", "Speed", "Agility")
        ),
        ElementalPower(
            id = "heal_air_prana_respiration",
            name = "Pranic Breath Vitalizer",
            element = ElementType.AIR,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🌬️",
            description = "Infuses pure atmospheric prana directly into the lungs and blood meridians.",
            combatEffect = "Restores 25% Vessel Health and recovers 40 Qi immediately.",
            mindSpiritEffect = "Regulates autonomic nervous rhythm, soothing hypervigilance.",
            qiCost = 25,
            cooldownTurns = 2,
            rarity = "Initiate",
            unlockLevel = 1,
            tags = listOf("Healing", "Prana", "Breath")
        ),
        ElementalPower(
            id = "trait_air_skywalker",
            name = "Unfettered Skywalker Will",
            element = ElementType.AIR,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "🪶",
            description = "The soul refuses all chains, moving freely across the sky and mind.",
            combatEffect = "Passive: Grants immunity to Immobilize and +20% Critical Strike Chance.",
            mindSpiritEffect = "Sovereign freedom and immunity to peer pressure or dogma.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Passive", "Freedom", "Speed")
        ),

        // ==========================================
        // 4. EARTH (Terramancy & Fortitude)
        // ==========================================
        ElementalPower(
            id = "atk_earth_tectonic_cataclysm",
            name = "Tectonic Cataclysm Slam",
            element = ElementType.EARTH,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "💥",
            description = "Stamps into the bedrock to trigger a devastating seismic shockwave of shattering stone.",
            combatEffect = "Deals 300% Earth Bludgeoning Damage + Stuns enemies for 1 turn.",
            mindSpiritEffect = "Roots intention deeply into reality, manifesting tangible results.",
            qiCost = 50,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Offensive", "Stun", "Seismic")
        ),
        ElementalPower(
            id = "man_earth_geokinetic_mold",
            name = "Geokinetic Bedrock Shaper",
            element = ElementType.EARTH,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🧱",
            description = "Commands stone and mineral lattices to rise into walls, trenches, or spikes on demand.",
            combatEffect = "Creates protective stone ramparts absorbing 500 damage.",
            mindSpiritEffect = "Develops pragmatic patience and builder's craftsmanship.",
            qiCost = 35,
            cooldownTurns = 2,
            rarity = "Initiate",
            unlockLevel = 1,
            tags = listOf("Control", "Terrain", "Wall")
        ),
        ElementalPower(
            id = "sup_earth_granite_fortress",
            name = "Granite Bastion Fortification",
            element = ElementType.EARTH,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🏰",
            description = "Crystallizes the outer aura into unbreakable diamond-granite plating.",
            combatEffect = "Increases Physical & Magical Defense by +50% for 3 turns.",
            mindSpiritEffect = "Builds rock-solid emotional boundaries against manipulation.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Buff", "Defense", "Armor")
        ),
        ElementalPower(
            id = "heal_earth_mineral_spring",
            name = "Gaia Subterranean Spring",
            element = ElementType.EARTH,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🌱",
            description = "Draws subterranean mineral salts and geomagnetic vitality to knit fractured bones.",
            combatEffect = "Restores 35% Health and cures Physical Weakness.",
            mindSpiritEffect = "Grounds scattered mental static back into earthy presence.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Healing", "Grounding", "Mineral")
        ),
        ElementalPower(
            id = "trait_earth_unmovable_core",
            name = "Unyielding Mountain Core",
            element = ElementType.EARTH,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "⛰️",
            description = "The vessel possesses the dense gravity of a mountain range that cannot be shaken.",
            combatEffect = "Passive: Max Health increased by +30% and knockback resistance 100%.",
            mindSpiritEffect = "Total immunity to panic, hesitation, or peer intimidation.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 2,
            tags = listOf("Passive", "Tenacity", "Fortitude")
        ),

        // ==========================================
        // 5. LIGHTNING (Electromancy & Speed)
        // ==========================================
        ElementalPower(
            id = "atk_lightning_heaven_strike",
            name = "Heavenly Judgement Bolt",
            element = ElementType.LIGHTNING,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "⚡",
            description = "Summons pure celestial ionization from the upper heavens in an instantaneous piercing flash.",
            combatEffect = "Deals 320% Lightning Arc Damage + Chains to 2 nearby targets.",
            mindSpiritEffect = "Awakens lightning-fast analytical comprehension.",
            qiCost = 50,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Chain", "Ion")
        ),
        ElementalPower(
            id = "man_lightning_electromagnetism",
            name = "Bio-Electric Magnetic Control",
            element = ElementType.LIGHTNING,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🧲",
            description = "Manipulates electromagnetic flux lines to disarm metal weapons or pull targets.",
            combatEffect = "Disorients enemy reflexes and disarms projectile arts for 2 turns.",
            mindSpiritEffect = "Magnetizes helpful opportunities and divine synchronicities.",
            qiCost = 35,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Control", "Magnetic", "Disarm")
        ),
        ElementalPower(
            id = "sup_lightning_synaptic_burst",
            name = "Synaptic Overdrive Surge",
            element = ElementType.LIGHTNING,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🧠",
            description = "Accelerates the biological nervous system with micro-currents of spirit electricity.",
            combatEffect = "Increases Critical Rate by +30% and grants double turn chance.",
            mindSpiritEffect = "Eliminates brain fog, sharpening focus to razor precision.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Buff", "Synapse", "Crit")
        ),
        ElementalPower(
            id = "heal_lightning_defibrillating_arc",
            name = "Aetheric Neural Restart",
            element = ElementType.LIGHTNING,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "⚡",
            description = "Uses gentle bio-electric resonance to defibrillate blocked meridians and stagnant life force.",
            combatEffect = "Revives unconscious vigor, restoring 25% Health and removing Paralysis.",
            mindSpiritEffect = "Breaks depressive numbness with a surge of life excitement.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Healing", "Revive", "Current")
        ),
        ElementalPower(
            id = "trait_lightning_stormborn",
            name = "Stormborn Celestial Spark",
            element = ElementType.LIGHTNING,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "🌩️",
            description = "Carries the primordial spark of the first cosmic lightning bolt.",
            combatEffect = "Passive: Whenever Qi is gathered, gain +15% bonus Qi and +10% Speed.",
            mindSpiritEffect = "High-voltage enthusiasm and infectious creative charisma.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 2,
            tags = listOf("Passive", "Spark", "Acceleration")
        ),

        // ==========================================
        // 6. ICE (Cryomancy & Stasis)
        // ==========================================
        ElementalPower(
            id = "atk_ice_absolute_zero_lance",
            name = "Absolute Zero Glacial Lance",
            element = ElementType.ICE,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "❄️",
            description = "Draws all heat away from a concentrated point, piercing the foe with glacial diamond ice.",
            combatEffect = "Deals 270% Frost Damage + Freezes target solid for 1 turn.",
            mindSpiritEffect = "Cools hot-headed rage into diamond-hard strategic composure.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Freeze", "Glacial")
        ),
        ElementalPower(
            id = "man_ice_cryo_lattice",
            name = "Cryogenic Moisture Crystalline Art",
            element = ElementType.ICE,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🧊",
            description = "Freezes atmospheric humidity into intricate geometric barriers, stairs, and spikes.",
            combatEffect = "Covers ground in slick frost, causing enemy attacks to miss 35%.",
            mindSpiritEffect = "Brings elegant mathematical order to chaotic emotions.",
            qiCost = 30,
            cooldownTurns = 2,
            rarity = "Initiate",
            unlockLevel = 1,
            tags = listOf("Control", "Geometry", "Terrain")
        ),
        ElementalPower(
            id = "sup_ice_permafrost_barrier",
            name = "Permafrost Frostbite Aura",
            element = ElementType.ICE,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🛡️",
            description = "Radiates sub-zero temperatures that slow attackers and numb incoming trauma.",
            combatEffect = "Reduces all incoming damage by 25% and slows attacker by 30%.",
            mindSpiritEffect = "Provides stoic insulation against cruel words or toxic environments.",
            qiCost = 35,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Buff", "Slow", "Frostbite")
        ),
        ElementalPower(
            id = "heal_ice_stasis_cocoon",
            name = "Stasis Cryo Preservation Cocoon",
            element = ElementType.ICE,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "💠",
            description = "Envelops the vessel in a cryogenic stasis crystal, halting all degeneration while healing.",
            combatEffect = "Immune to all damage for 1 turn while regenerating 35% Health.",
            mindSpiritEffect = "Allows deep contemplative sleep and complete mental reset.",
            qiCost = 50,
            cooldownTurns = 4,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Healing", "Stasis", "Invulnerable")
        ),
        ElementalPower(
            id = "trait_ice_glacial_equanimity",
            name = "Glacial Mind Equanimity",
            element = ElementType.ICE,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "🏔️",
            description = "The consciousness is as clear and unshakeable as an ancient polar glacier.",
            combatEffect = "Passive: +20% Resistance to all elemental effects and +15% Mind Stability.",
            mindSpiritEffect = "Zero emotional leakage; absolute calmness under extreme stress.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Passive", "Stoicism", "Stability")
        ),

        // ==========================================
        // 7. DARK (Shadowmancy & Concealment)
        // ==========================================
        ElementalPower(
            id = "atk_dark_abyssal_cleave",
            name = "Abyssal Void-Edge Strike",
            element = ElementType.DARK,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🌑",
            description = "Draws pure shadow essence into an edge that ignores physical armor and strikes the soul.",
            combatEffect = "Deals 290% Shadow Damage + Drains 15% of target's energy.",
            mindSpiritEffect = "Unflinchingly confronts one's own shadow, claiming its power.",
            qiCost = 45,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Drain", "Shadow")
        ),
        ElementalPower(
            id = "man_dark_shadowmeld",
            name = "Umbral Silhouette Transposition",
            element = ElementType.DARK,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "👤",
            description = "Steps completely into the shadow plane, reappearing anywhere light is absent.",
            combatEffect = "Teleports behind target, guaranteeing a 100% Critical Strike next turn.",
            mindSpiritEffect = "Teaches stealth, patience, and waiting for the optimal moment.",
            qiCost = 35,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Control", "Teleport", "Crit")
        ),
        ElementalPower(
            id = "sup_dark_eclipse_shroud",
            name = "Eclipse Aegis Intent Absorber",
            element = ElementType.DARK,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🌘",
            description = "Erects an event shroud that swallows incoming hostile curses and transforms them into shield.",
            combatEffect = "Absorbs up to 400 magic damage, converting 50% into vessel Qi.",
            mindSpiritEffect = "Transforms malicious criticism into fuel for personal triumph.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Support", "Absorb", "Shield")
        ),
        ElementalPower(
            id = "heal_dark_malice_transmutation",
            name = "Shadow Essence Transmutation",
            element = ElementType.DARK,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🖤",
            description = "Feeds on suppressed fears and heavy emotions, transmuting their raw mass into vital health.",
            combatEffect = "Consumes negative status effects to restore 40% Vessel Health.",
            mindSpiritEffect = "Integrates psychological trauma into authentic wisdom.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Healing", "Transmute", "ShadowWork")
        ),
        ElementalPower(
            id = "trait_dark_umbra_sovereign",
            name = "Nightstalker Umbra Sight",
            element = ElementType.DARK,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "👁️‍🗨️",
            description = "Sees clearly through total darkness, illusions, deception, and hidden motives.",
            combatEffect = "Passive: Cannot be blinded or ambushed; +25% Dark Art effectiveness.",
            mindSpiritEffect = "Effortless psychological discernment to detect lies immediately.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Sovereign",
            unlockLevel = 4,
            tags = listOf("Passive", "Sight", "Discernment")
        ),

        // ==========================================
        // 8. LIGHT (Lumimancy & Cleansing)
        // ==========================================
        ElementalPower(
            id = "atk_light_solar_lance",
            name = "Radiant Dawn Photonic Spear",
            element = ElementType.LIGHT,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "✨",
            description = "Focuses pure solar photon rays into a piercing spear of holy illumination.",
            combatEffect = "Deals 285% Holy Radiant Damage + Blinds target for 2 turns.",
            mindSpiritEffect = "Disperses self-doubt with uncompromising truth.",
            qiCost = 45,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Blind", "Photonic")
        ),
        ElementalPower(
            id = "man_light_prism_refraction",
            name = "Photonic Mirage & Illusion Art",
            element = ElementType.LIGHT,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🌈",
            description = "Refracts ambient light rays through crystalline spiritual lenses, creating holographic decoys.",
            combatEffect = "Creates 3 holographic duplicates, absorbing 3 enemy strikes completely.",
            mindSpiritEffect = "Allows viewing situations through multifaceted compassionate lenses.",
            qiCost = 35,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Control", "Illusion", "Decoy")
        ),
        ElementalPower(
            id = "sup_light_seraph_aegis",
            name = "Seraphic Aegis Ward",
            element = ElementType.LIGHT,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "👼",
            description = "Invokes the luminous golden wings of the celestial realm to guard the party.",
            combatEffect = "Grants 350 Holy Shield to all allies and cleanses mental corruption.",
            mindSpiritEffect = "Bestows a feeling of unconditional spiritual safety and divine guidance.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Support", "Barrier", "Aegis")
        ),
        ElementalPower(
            id = "heal_light_divine_sanative",
            name = "Celestial Dawn Miraculous Grace",
            element = ElementType.LIGHT,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🌟",
            description = "Bathes the mortal form in original primordial light, regenerating flesh and spirit instantly.",
            combatEffect = "Restores 50% Health and removes all curses, poisons, and fatigue.",
            mindSpiritEffect = "Floods the heart with profound gratitude and spiritual ecstasy.",
            qiCost = 60,
            cooldownTurns = 4,
            rarity = "Sovereign",
            unlockLevel = 4,
            tags = listOf("Healing", "Miracle", "Cleanse")
        ),
        ElementalPower(
            id = "trait_light_solar_apostle",
            name = "Illuminated Solar Beacon",
            element = ElementType.LIGHT,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "☀️",
            description = "The inner spirit radiates constant warm illumination that inspires everyone around.",
            combatEffect = "Passive: Increases party morale and passive healing by +20%.",
            mindSpiritEffect = "Unshakeable benevolent charisma and natural moral leadership.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 2,
            tags = listOf("Passive", "Aura", "Inspiration")
        ),

        // ==========================================
        // 9. GRAVITY (Gravimancy & Spatial Force)
        // ==========================================
        ElementalPower(
            id = "atk_gravity_singularity_crush",
            name = "Event Horizon Singularity Crush",
            element = ElementType.GRAVITY,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🪐",
            description = "Creates a micro-black hole with near-infinite mass that crushes targets inward.",
            combatEffect = "Deals 340% Gravitational Crush Damage + Pulls all enemies to center.",
            mindSpiritEffect = "Teaches intense laser focus that pulls all scattered resources together.",
            qiCost = 60,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Offensive", "Singularity", "Crush")
        ),
        ElementalPower(
            id = "man_gravity_vector_inversion",
            name = "Gravitational Vector Repulsion",
            element = ElementType.GRAVITY,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🌌",
            description = "Inverts gravitational orientation, causing incoming projectiles to fall harmlessly upward.",
            combatEffect = "Repels 100% projectile damage and pushes enemies away.",
            mindSpiritEffect = "Effortlessly shrugs off social pressure and heavy expectations.",
            qiCost = 40,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Control", "Repulsion", "Vector")
        ),
        ElementalPower(
            id = "sup_gravity_heavy_anchor",
            name = "Unshakable Graviton Anchor",
            element = ElementType.GRAVITY,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "⚓",
            description = "Increases personal localized gravity tenfold, preventing any forced displacement.",
            combatEffect = "Immune to knockback, stun, and displacement; +40% Physical Defense.",
            mindSpiritEffect = "Anchors convictions so firmly that no emotional storm can move them.",
            qiCost = 35,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Support", "Anchor", "Immunity")
        ),
        ElementalPower(
            id = "heal_gravity_weightless_restoration",
            name = "Zero-G Cellular Decompression",
            element = ElementType.GRAVITY,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🫧",
            description = "Suspends the body in zero-gravity stasis, relieving spinal, organ, and meridian compression.",
            combatEffect = "Restores 30% Health and removes exhaustion and debuffs.",
            mindSpiritEffect = "Releases the crushing weight of psychological burnout.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Healing", "Decompression", "ZeroG")
        ),
        ElementalPower(
            id = "trait_gravity_graviton_core",
            name = "Graviton Singularity Core",
            element = ElementType.GRAVITY,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "⚫",
            description = "Vessel center of gravity is an inexhaustible vortex of dense spiritual mass.",
            combatEffect = "Passive: Increases Qi reservoir cap by +25% and power attack damage by +20%.",
            mindSpiritEffect = "Generates an irresistible magnetic presence and personal authority.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Sovereign",
            unlockLevel = 4,
            tags = listOf("Passive", "Mass", "Magnetism")
        ),

        // ==========================================
        // 10. COSMIC (Astromancy & Stellar Fusion)
        // ==========================================
        ElementalPower(
            id = "atk_cosmic_supernova_pulse",
            name = "Supernova Cataclysmic Blast",
            element = ElementType.COSMIC,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "💥",
            description = "Synthesizes nuclear stellar fusion at the dantian, unleashing a cosmic starburst.",
            combatEffect = "Deals 350% Cosmic Energy Damage across all dimensions.",
            mindSpiritEffect = "Expands consciousness to perceive the grand cosmic scale of existence.",
            qiCost = 65,
            cooldownTurns = 4,
            rarity = "Sovereign",
            unlockLevel = 4,
            tags = listOf("Offensive", "Supernova", "Cosmic")
        ),
        ElementalPower(
            id = "man_cosmic_astral_leyline",
            name = "Stellar Constellation Weaving",
            element = ElementType.COSMIC,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "✨",
            description = "Draws astral lines between star constellations to establish cosmic teleportation arrays.",
            combatEffect = "Links all allies in a stellar web, sharing defensive buffs and 20% healing.",
            mindSpiritEffect = "Synthesizes disparate concepts into unified creative genius.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Control", "Constellation", "Web")
        ),
        ElementalPower(
            id = "sup_cosmic_astral_projection",
            name = "Cosmic Consciousness Expansion",
            element = ElementType.COSMIC,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🌌",
            description = "Projects the second spiritual body into the astral plane to gather transcendent insight.",
            combatEffect = "Reveals all hidden enemies, weaknesses, and grants +50% Accuracy.",
            mindSpiritEffect = "Unlocks lucid insight and prophetic intuition.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Support", "Astral", "Insight")
        ),
        ElementalPower(
            id = "heal_cosmic_nebula_purification",
            name = "Nebular Starlight Purification",
            element = ElementType.COSMIC,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "💫",
            description = "Rains shimmering stardust from ancient interstellar nebulae to heal vessel wear.",
            combatEffect = "Restores 40% Vessel Health + regenerates 20 Qi per turn.",
            mindSpiritEffect = "Transmutes existential dread into awe-filled celestial wonder.",
            qiCost = 55,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Healing", "Starlight", "Nebula")
        ),
        ElementalPower(
            id = "trait_cosmic_starforged",
            name = "Starforged Astral Vessel",
            element = ElementType.COSMIC,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "⭐",
            description = "The vessel was tempered in the heart of a dying star before birth.",
            combatEffect = "Passive: Increases all Elemental Masteries by +15% and EXP gained by +20%.",
            mindSpiritEffect = "Transcendent cosmic patience and deep sense of destined purpose.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Mythic",
            unlockLevel = 5,
            tags = listOf("Passive", "Starforged", "Ascension")
        ),

        // ==========================================
        // 11. VOID (Voidmancy & Erasure)
        // ==========================================
        ElementalPower(
            id = "atk_void_null_oblivion",
            name = "Null Oblivion Particle Cleave",
            element = ElementType.VOID,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🕳️",
            description = "Wields a blade of pure non-existence that erases matter and enchantments.",
            combatEffect = "Deals 310% Void Damage + Erases all enemy shields and positive buffs.",
            mindSpiritEffect = "Erases limiting beliefs, guilt, and emotional baggage instantly.",
            qiCost = 55,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Offensive", "Dispel", "Erasure")
        ),
        ElementalPower(
            id = "man_void_spatial_rift",
            name = "Spatial Dimensional Rift Manipulation",
            element = ElementType.VOID,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🌀",
            description = "Tears small rifts in the fabric of space to swallow attacks or stash physical items.",
            combatEffect = "Swallows 1 hostile attack into null space, nullifying it completely.",
            mindSpiritEffect = "Creates spacious inner detachment from external drama.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Control", "Rift", "Nullify")
        ),
        ElementalPower(
            id = "sup_void_entropy_nullification",
            name = "Null-Field Curse Eater",
            element = ElementType.VOID,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🛡️",
            description = "Generates a zero-entropy sphere where no negative spiritual magic or hex can function.",
            combatEffect = "Immunity to curses, poisons, and debuffs for the next 3 turns.",
            mindSpiritEffect = "Absolute psychological boundary that rejects toxic manipulation.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 2,
            tags = listOf("Support", "CurseEater", "Entropy")
        ),
        ElementalPower(
            id = "heal_void_hollow_cleansing",
            name = "Void Sink Toxin Dissipation",
            element = ElementType.VOID,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🔘",
            description = "Channels toxic biological and mental pollutants into an empty pocket of void.",
            combatEffect = "Restores 35% Health and clears internal fatigue.",
            mindSpiritEffect = "Provides pristine mental silence, erasing overthinking.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Healing", "VoidSink", "Detox")
        ),
        ElementalPower(
            id = "trait_void_nihil_sovereign",
            name = "Primordial Void Resonator",
            element = ElementType.VOID,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "🖤",
            description = "Rooted in the primordial nothingness that preceded the birth of all realms.",
            combatEffect = "Passive: 20% chance to completely negate incoming damage.",
            mindSpiritEffect = "Freedom from ego fragility; unaffected by praise or blame.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Sovereign",
            unlockLevel = 4,
            tags = listOf("Passive", "Negation", "Nothingness")
        ),

        // ==========================================
        // 12. TIME (Chronomancy & Dilation)
        // ==========================================
        ElementalPower(
            id = "atk_time_temporal_stasis_cleave",
            name = "Temporal Stasis Executioner",
            element = ElementType.TIME,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "⏳",
            description = "Freezes target in a localized temporal loop before striking across three moments at once.",
            combatEffect = "Deals 330% Chrono Damage + Prevents target from taking action next turn.",
            mindSpiritEffect = "Masters timing to act at the precise golden window.",
            qiCost = 60,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Offensive", "Stasis", "Time")
        ),
        ElementalPower(
            id = "man_time_chrono_dilation",
            name = "Chronological Flow Accelerator",
            element = ElementType.TIME,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "⏱️",
            description = "Accelerates subjective personal time, making the external world appear frozen.",
            combatEffect = "Grants 2 consecutive actions in a single turn.",
            mindSpiritEffect = "Compresses hours of learning into minutes of lightning insight.",
            qiCost = 50,
            cooldownTurns = 4,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Control", "Dilation", "DoubleTurn")
        ),
        ElementalPower(
            id = "sup_time_future_precognition",
            name = "Probabilistic Timeline Foresight",
            element = ElementType.TIME,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🔮",
            description = "Glances seconds into probabilistic futures to evade attacks effortlessly.",
            combatEffect = "Grants 100% Evasion against the next 2 attacks.",
            mindSpiritEffect = "Deep strategic foresight and avoidance of disastrous pitfalls.",
            qiCost = 40,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Support", "Precognition", "Foresight")
        ),
        ElementalPower(
            id = "heal_time_rewind_recovery",
            name = "Temporal Cellular Rewind",
            element = ElementType.TIME,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "⌛",
            description = "Reverses the physical condition of the vessel to its exact state 60 seconds prior.",
            combatEffect = "Restores 50% Vessel Health and clears all damage taken in the last 2 turns.",
            mindSpiritEffect = "Wipes away regrets, resetting the emotional baseline to peace.",
            qiCost = 60,
            cooldownTurns = 4,
            rarity = "Sovereign",
            unlockLevel = 4,
            tags = listOf("Healing", "Rewind", "Recovery")
        ),
        ElementalPower(
            id = "trait_time_chrono_wanderer",
            name = "Eternal Timeweaver Mind",
            element = ElementType.TIME,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "🕰️",
            description = "The consciousness exists slightly outside the linear flow of mortal time.",
            combatEffect = "Passive: Reduces all skill cooldowns by 1 and increases reaction speed by +25%.",
            mindSpiritEffect = "Unshakeable patience, knowing that time compounds all great works.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Sovereign",
            unlockLevel = 4,
            tags = listOf("Passive", "Timeless", "Patience")
        ),

        // ==========================================
        // 13. NATURE (Biomancy & Regrowth)
        // ==========================================
        ElementalPower(
            id = "atk_nature_primordial_thornburst",
            name = "Primordial Ancient Root Spear",
            element = ElementType.NATURE,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🌿",
            description = "Commands ancient subterranean ironwood roots to erupt upward with crushing force.",
            combatEffect = "Deals 275% Nature Piercing Damage + Roots enemy in place.",
            mindSpiritEffect = "Connects the soul to the ancient living memory of the biosphere.",
            qiCost = 40,
            cooldownTurns = 2,
            rarity = "Adept",
            unlockLevel = 1,
            tags = listOf("Offensive", "Root", "Ironwood")
        ),
        ElementalPower(
            id = "man_nature_botanical_growth",
            name = "Living Flora Biomantic Shaper",
            element = ElementType.NATURE,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🌳",
            description = "Rapidly blooms bio-spores and entangling vines to reshape the tactical environment.",
            combatEffect = "Entangles enemies, reducing their attack output by 30%.",
            mindSpiritEffect = "Teaches organic growth principles over artificial force.",
            qiCost = 35,
            cooldownTurns = 2,
            rarity = "Initiate",
            unlockLevel = 1,
            tags = listOf("Control", "Entangle", "Spore")
        ),
        ElementalPower(
            id = "sup_nature_symbiotic_bloom",
            name = "Symbiotic Vitality Spore Bloom",
            element = ElementType.NATURE,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🌸",
            description = "Releases fragrant bio-luminescent spores that revitalize muscular stamina and spirit.",
            combatEffect = "Increases all stats by +15% and grants 5% passive regeneration.",
            mindSpiritEffect = "Fosters deep emotional empathy and interpersonal warmth.",
            qiCost = 35,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Support", "Spore", "Vitality")
        ),
        ElementalPower(
            id = "heal_nature_tree_of_life",
            name = "Yggdrasil Celestial Regeneration",
            element = ElementType.NATURE,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "🌲",
            description = "Channels the life-blood of the World Tree to perpetually regrow damaged tissue.",
            combatEffect = "Restores 45% Health + 15% regen per turn for 3 turns.",
            mindSpiritEffect = "Purges cellular stress and restores child-like vitality.",
            qiCost = 50,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Healing", "Regeneration", "Yggdrasil")
        ),
        ElementalPower(
            id = "trait_nature_verdant_heart",
            name = "Verdant Living Biosphere",
            element = ElementType.NATURE,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "🌱",
            description = "The vessel acts as an open conduit for the thriving life energy of the planet.",
            combatEffect = "Passive: Increases all healing received by +30% and poison immunity.",
            mindSpiritEffect = "Unfailing resilience and joyful celebration of living existence.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Master",
            unlockLevel = 2,
            tags = listOf("Passive", "Resilience", "Biosphere")
        ),

        // ==========================================
        // 14. SOUL (Pneumamancy & Consciousness)
        // ==========================================
        ElementalPower(
            id = "atk_soul_severing_slash",
            name = "Astral Spirit Cleaver",
            element = ElementType.SOUL,
            category = PowerCategory.ATTACK_ART,
            runeSymbol = "🔮",
            description = "Cleaves spectral ties directly without harming physical matter, targeting the ego.",
            combatEffect = "Deals 300% Pure True Soul Damage (Ignores all armor and shields).",
            mindSpiritEffect = "Cleaves through self-deception with total honesty.",
            qiCost = 55,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Offensive", "TrueDamage", "Astral")
        ),
        ElementalPower(
            id = "man_soul_spectral_weaving",
            name = "Consciousness Thought-Construct Weaving",
            element = ElementType.SOUL,
            category = PowerCategory.MANIPULATION_ART,
            runeSymbol = "🪡",
            description = "Shapes raw mental intention into solid telekinetic tools, shields, and phantom limbs.",
            combatEffect = "Summons a spectral twin that assists in all actions for 2 turns.",
            mindSpiritEffect = "Empowers willful manifestation of thoughts into reality.",
            qiCost = 45,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Control", "Manifestation", "Construct")
        ),
        ElementalPower(
            id = "sup_soul_telepathic_harmony",
            name = "Unified Soul-Link Harmonizer",
            element = ElementType.SOUL,
            category = PowerCategory.SUPPORT_CLASS,
            runeSymbol = "🔗",
            description = "Harmonizes brainwaves and heartbeats into a unified telepathic resonance network.",
            combatEffect = "Increases Team Resonance by +40% and shares tactical perception.",
            mindSpiritEffect = "Bridges misunderstandings and heals relational conflicts.",
            qiCost = 35,
            cooldownTurns = 3,
            rarity = "Adept",
            unlockLevel = 2,
            tags = listOf("Support", "Resonance", "Telepathy")
        ),
        ElementalPower(
            id = "heal_soul_sanctuary_repair",
            name = "Ego-Vessel Spectral Mend",
            element = ElementType.SOUL,
            category = PowerCategory.HEALING_CLASS,
            runeSymbol = "✨",
            description = "Repairs fractures in the astral subtle body caused by shock or trauma.",
            combatEffect = "Restores 40% Vessel Health + increases Humanity and Stability by +10.",
            mindSpiritEffect = "Mends fragmented self-esteem and restores sacred wholeness.",
            qiCost = 50,
            cooldownTurns = 3,
            rarity = "Master",
            unlockLevel = 3,
            tags = listOf("Healing", "Wholeness", "Mend")
        ),
        ElementalPower(
            id = "trait_soul_immortal_mind",
            name = "Unbreakable Astral Monad",
            element = ElementType.SOUL,
            category = PowerCategory.PASSIVE_TRAIT,
            runeSymbol = "👑",
            description = "The central observer within remains eternal, uncreated, and indestructible.",
            combatEffect = "Passive: Survives lethal blows with 1 Health (once per day) + 20% Qi efficiency.",
            mindSpiritEffect = "Supreme spiritual sovereignty and fearlessness in the face of death.",
            qiCost = 0,
            cooldownTurns = 0,
            rarity = "Mythic",
            unlockLevel = 5,
            tags = listOf("Passive", "Immortal", "Indestructible")
        )
    )

    fun getPowerById(id: String): ElementalPower? {
        return ALL_POWERS.firstOrNull { it.id == id }
    }

    fun getPowersByElement(element: ElementType): List<ElementalPower> {
        return ALL_POWERS.filter { it.element == element }
    }

    fun getPowersByCategory(category: PowerCategory): List<ElementalPower> {
        return ALL_POWERS.filter { it.category == category }
    }

    val DEFAULT_ATTACK_ID = "atk_fire_sunflare"
    val DEFAULT_MANIPULATION_ID = "man_fire_pyrokinesis"
    val DEFAULT_SUPPORT_ID = "sup_fire_blaze_mantle"
    val DEFAULT_HEALING_ID = "heal_fire_phoenix_rebirth"
    val DEFAULT_TRAIT_ID = "trait_fire_pyre_heart"
}
