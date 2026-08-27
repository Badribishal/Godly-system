package com.example.data.model

enum class ArchetypeCategory(val displayName: String, val rune: String, val colorHex: Long) {
    INITIATE("Initiate Vessels", "💠", 0xFF60A5FA),
    RADIANT("Solar & Sacred", "🪽", 0xFFFCD34D),
    VOID("Shadow & Abyssal", "🔥", 0xFFEF4444),
    MYSTIC("Astral & Arcane", "✨", 0xFFA78BFA),
    PRIMORDIAL("Titan & Primordial", "⚡", 0xFF34D399),
    COSMIC("Cosmic Singularity", "🌌", 0xFF38BDF8)
}

data class AdvancedArchetype(
    val id: String,
    val name: String,
    val subtitle: String,
    val titlePrefix: String,
    val requiredLevel: Int,
    val category: ArchetypeCategory,
    val element: String,
    val sigilIcon: String,
    val passivePerk: String,
    val lore: String,
    val requiredForcesHint: String,
    val accentColorHex: Long,
    val bonusShardsOnAttune: Int = 25,
    val characterClass: String = "Seeker",
    val celestialRace: String = "Human Conduit",
    val unlockPrerequisiteHint: String = "Reach Soul Level $requiredLevel through daily alchemical progress."
)

object AdvancedArchetypesCatalog {

    val ALL_ARCHETYPES = listOf(
        // Level 1: Starter
        AdvancedArchetype(
            id = "arch_seeker",
            name = "The Awakening Vessel",
            subtitle = "Malleable Soul Conduit",
            titlePrefix = "Awakened",
            requiredLevel = 1,
            category = ArchetypeCategory.INITIATE,
            element = "Aether / Unattuned",
            sigilIcon = "🌱",
            passivePerk = "+10% Base Soul EXP from all daily interactions.",
            lore = "The pristine consciousness before divine polarization. Highly adaptable, absorbing all astral frequencies without bias.",
            requiredForcesHint = "Default Initiate vessel granted to all seekers.",
            accentColorHex = 0xFF60A5FA,
            characterClass = "Seeker",
            celestialRace = "Human Conduit",
            unlockPrerequisiteHint = "Available upon Genesis initialization."
        ),

        // Level 3: Early Progression
        AdvancedArchetype(
            id = "arch_luminous_aegis",
            name = "The Luminous Aegis",
            subtitle = "Solar Shield of Unflinching Resolve",
            titlePrefix = "Guardian",
            requiredLevel = 3,
            category = ArchetypeCategory.RADIANT,
            element = "Solar Luminescence",
            sigilIcon = "🛡️",
            passivePerk = "+20% Courage & Humility growth; grants +5 Stability each day.",
            lore = "Forged in the heart of benevolent stars, this vessel converts moral conviction into impenetrable energetic barriers.",
            requiredForcesHint = "Soul Level 3 • Cultivates Courage & Charity.",
            accentColorHex = 0xFFFBBF24,
            characterClass = "Aegis Sentinel",
            celestialRace = "Solar Seraphim",
            unlockPrerequisiteHint = "Reach Soul Level 3 by completing Daily Quests & Check-ins."
        ),

        // Level 5: Shadow Duality
        AdvancedArchetype(
            id = "arch_shadow_weaver",
            name = "The Shadow Weaver",
            subtitle = "Architect of Transmuted Darkness",
            titlePrefix = "Nether",
            requiredLevel = 5,
            category = ArchetypeCategory.VOID,
            element = "Nether Umbra",
            sigilIcon = "🕸️",
            passivePerk = "+25% Shadow Force Transmutation; converts destructive impulses into Diligence.",
            lore = "A sovereign soul that does not fear the abyss. It channels primal shadows into catalysts for unstoppable creative willpower.",
            requiredForcesHint = "Soul Level 5 • Master of Pride & Envy transmutation.",
            accentColorHex = 0xFFDC2626,
            characterClass = "Shadow Weaver",
            celestialRace = "Abyssal Netherkin",
            unlockPrerequisiteHint = "Attain Matrix Tier II (Soul Level 5) & record a Shadow Transmutation."
        ),

        // Level 8: Alchemical Adept
        AdvancedArchetype(
            id = "arch_alchemical_sovereign",
            name = "The Alchemical Sovereign",
            subtitle = "Grand Master of Dual Harmonics",
            titlePrefix = "Philosopher",
            requiredLevel = 8,
            category = ArchetypeCategory.MYSTIC,
            element = "Astral Quicksilver",
            sigilIcon = "⚗️",
            passivePerk = "+15% Shards harvest multiplier & unlocks deeper reflection resonance.",
            lore = "Perceives the Seven Sins and Seven Virtues as two sides of a single divine ouroboros. Achieves spontaneous equilibrium.",
            requiredForcesHint = "Soul Level 8 • Balanced Light & Shadow scores.",
            accentColorHex = 0xFFA855F7,
            characterClass = "Grand Alchemist",
            celestialRace = "Aether Architect",
            unlockPrerequisiteHint = "Reach Soul Level 8 & maintain balanced Virtue/Sin equilibrium."
        ),

        // Level 12: Radiant Ascension
        AdvancedArchetype(
            id = "arch_solar_seraph",
            name = "The Solar Seraph",
            subtitle = "Dawn Arbiter of Absolute Grace",
            titlePrefix = "Seraphic",
            requiredLevel = 12,
            category = ArchetypeCategory.RADIANT,
            element = "Seventh Dawn Luminescence",
            sigilIcon = "🪽",
            passivePerk = "+30% EXP from Virtue Attunement & cleanses chaotic emotional states.",
            lore = "Radiates blinding celestial benevolence. Its six wings represent the transcendent integration of mortal trials.",
            requiredForcesHint = "Soul Level 12 • High Charity, Humility & Gratitude.",
            accentColorHex = 0xFFFCD34D,
            characterClass = "Solar Arbiter",
            celestialRace = "Solar Seraphim",
            unlockPrerequisiteHint = "Attain Matrix Tier III (Soul Level 12) & high Virtue scores."
        ),

        // Level 15: Void Mastery
        AdvancedArchetype(
            id = "arch_void_walker",
            name = "The Void Stalker",
            subtitle = "Phantom of the Infinite Abyss",
            titlePrefix = "Abyssal",
            requiredLevel = 15,
            category = ArchetypeCategory.VOID,
            element = "Singularity Eclipse",
            sigilIcon = "🗡️",
            passivePerk = "+35% Willpower in crisis; transforms Wrath into laser-focused strategic mastery.",
            lore = "Moves unseen between cosmic shadows, neutralizing internal vulnerabilities with absolute precision.",
            requiredForcesHint = "Soul Level 15 • Deep shadow mastery with stable humanity.",
            accentColorHex = 0xFFEF4444,
            characterClass = "Void Stalker",
            celestialRace = "Abyssal Netherkin",
            unlockPrerequisiteHint = "Attain Matrix Tier IV (Soul Level 15) & conquer negative emotional loops."
        ),

        // Level 20: Arcane Sage
        AdvancedArchetype(
            id = "arch_astral_sage",
            name = "The Astral Sage",
            subtitle = "Archon of Starlight Wisdom",
            titlePrefix = "Archon",
            requiredLevel = 20,
            category = ArchetypeCategory.MYSTIC,
            element = "Nebula Starlight",
            sigilIcon = "✨",
            passivePerk = "Generates +10 Soul Shards every evaluation; reveals hidden resonance affinities.",
            lore = "Channels the memory of ancient galaxies, translating complex mortal dilemmas into simple spiritual geometry.",
            requiredForcesHint = "Soul Level 20 • High Temperance & Humanity.",
            accentColorHex = 0xFF818CF8,
            characterClass = "Astral Archon",
            celestialRace = "Astral Starborne",
            unlockPrerequisiteHint = "Reach Soul Level 20 and complete 15+ Daily Quests."
        ),

        // Level 25: Earth Titan
        AdvancedArchetype(
            id = "arch_earth_titan",
            name = "The Earthborn Titan",
            subtitle = "Immovable Bastion of the Primordial Core",
            titlePrefix = "Colossus",
            requiredLevel = 25,
            category = ArchetypeCategory.PRIMORDIAL,
            element = "Tectonic Magma",
            sigilIcon = "⛰️",
            passivePerk = "+50% Stability resistance; immune to emotional degradation from chaotic records.",
            lore = "Rooted in the primordial mantle of worlds. No astral storm or psychological turbulence can shake this vessel.",
            requiredForcesHint = "Soul Level 25 • High Stability, Patience & Diligence.",
            accentColorHex = 0xFF10B981,
            characterClass = "Earth Colossus",
            celestialRace = "Primordial Titan",
            unlockPrerequisiteHint = "Attain Matrix Tier V (Soul Level 25) with Stability > 80."
        ),

        // Level 30: Celestial Archon
        AdvancedArchetype(
            id = "arch_celestial_archon",
            name = "The Celestial Archon",
            subtitle = "Crown Sovereign of the High Spheres",
            titlePrefix = "Sovereign",
            requiredLevel = 30,
            category = ArchetypeCategory.RADIANT,
            element = "Solar Crown Plasma",
            sigilIcon = "👑",
            passivePerk = "+25% EXP bonus to all daily activities & unlocks supreme cosmetic blessings.",
            lore = "Wears the crown of self-conquest. Rules over its own conscious reality with compassionate authority.",
            requiredForcesHint = "Soul Level 30 • High Humility & Sovereign Leadership.",
            accentColorHex = 0xFFEAB308,
            characterClass = "Celestial Sovereign",
            celestialRace = "Solar Seraphim",
            unlockPrerequisiteHint = "Reach Soul Level 30 & unlock at least 5 Archon achievements."
        ),

        // Level 35: Ethereal Weaver
        AdvancedArchetype(
            id = "arch_ethereal_weaver",
            name = "The Ethereal Weaver",
            subtitle = "Symphonist of Cosmic Frequencies",
            titlePrefix = "Symphonist",
            requiredLevel = 35,
            category = ArchetypeCategory.MYSTIC,
            element = "Aether Harmony",
            sigilIcon = "🌌",
            passivePerk = "Converts all neutral interactions into amplified resonance bonuses.",
            lore = "Directly weaves the astral strings of causality, turning discordant experiences into symphonies of growth.",
            requiredForcesHint = "Soul Level 35 • Perfect harmony between Seven Virtues.",
            accentColorHex = 0xFFC084FC,
            characterClass = "Aether Symphonist",
            celestialRace = "Astral Starborne",
            unlockPrerequisiteHint = "Reach Soul Level 35 & cultivate high Temperance & Gratitude."
        ),

        // Level 38: Phoenix Eternal
        AdvancedArchetype(
            id = "arch_solar_phoenix",
            name = "The Undying Phoenix",
            subtitle = "Eternal Flame of Rebirth",
            titlePrefix = "Immortal",
            requiredLevel = 38,
            category = ArchetypeCategory.RADIANT,
            element = "Everlasting Solar Flame",
            sigilIcon = "🦅",
            passivePerk = "Instant resurrection from despair; emotional lows convert into 2x EXP.",
            lore = "Rising continually from the ashes of past trials with boundless vitality and transcendent radiance.",
            requiredForcesHint = "Soul Level 38 • High Courage, Patience & Humility.",
            accentColorHex = 0xFFFB923C,
            characterClass = "Immortal Phoenix",
            celestialRace = "Solar Seraphim",
            unlockPrerequisiteHint = "Reach Soul Level 38 & complete 30 Daily Quests."
        ),

        // Level 42: Abyssal Leviathan
        AdvancedArchetype(
            id = "arch_abyssal_leviathan",
            name = "The Abyssal Leviathan",
            subtitle = "Sovereign of Deepest Shadows",
            titlePrefix = "Abyssal",
            requiredLevel = 42,
            category = ArchetypeCategory.VOID,
            element = "Deep Ocean Umbra",
            sigilIcon = "🌊",
            passivePerk = "Immense emotional depth; converts melancholy into profound creative genius.",
            lore = "Dwelling in the darkest cosmic trenches, turning ancient solitude into unstoppable leviathan majesty.",
            requiredForcesHint = "Soul Level 42 • Deep integration of Pride & Sloth.",
            accentColorHex = 0xFF0284C7,
            characterClass = "Abyssal Sovereign",
            celestialRace = "Abyssal Netherkin",
            unlockPrerequisiteHint = "Reach Soul Level 42 with deep shadow transmutation mastery."
        ),

        // Level 45: Temporal Chronomancer
        AdvancedArchetype(
            id = "arch_astral_chronomancer",
            name = "The Astral Chronomancer",
            subtitle = "Weaver of Infinite Timelines",
            titlePrefix = "Timeweaver",
            requiredLevel = 45,
            category = ArchetypeCategory.MYSTIC,
            element = "Chrono-Aether",
            sigilIcon = "⏳",
            passivePerk = "Daily streak rewards are doubled; grants insight into future soul evolutionary paths.",
            lore = "Perceives the past, present, and future as woven threads in a grand tapestry of cosmic purpose.",
            requiredForcesHint = "Soul Level 45 • High Patience & Diligence.",
            accentColorHex = 0xFFA78BFA,
            characterClass = "Chronomancer",
            celestialRace = "Astral Starborne",
            unlockPrerequisiteHint = "Reach Soul Level 45 & maintain a 7-day Daily Quest streak."
        ),

        // Level 48: Celestial Seraph of Justice
        AdvancedArchetype(
            id = "arch_solar_justiciar",
            name = "The Solar Justiciar",
            subtitle = "Sword of Absolute Truth & Balance",
            titlePrefix = "Arbiter",
            requiredLevel = 48,
            category = ArchetypeCategory.RADIANT,
            element = "Pure White Light",
            sigilIcon = "⚔️",
            passivePerk = "+40% bonus Shards and EXP on resolving challenging emotional trials.",
            lore = "The divine embodiment of cosmic balance, wielding the incandescent blade of discernment.",
            requiredForcesHint = "Soul Level 48 • Flawless equilibrium between Justice & Charity.",
            accentColorHex = 0xFFEAB308,
            characterClass = "Solar Justiciar",
            celestialRace = "Solar Seraphim",
            unlockPrerequisiteHint = "Reach Soul Level 48 & maintain equilibrium score > 85."
        ),

        // Level 50: Primordial Stormbringer
        AdvancedArchetype(
            id = "arch_tempest_primordial",
            name = "The Tempest Primordial",
            subtitle = "Avatar of Living Celestial Lightning",
            titlePrefix = "Tempest",
            requiredLevel = 50,
            category = ArchetypeCategory.PRIMORDIAL,
            element = "Galactic Tempest",
            sigilIcon = "🌪️",
            passivePerk = "Rapid transmutation speed; daily evaluations charge supercharged resonance.",
            lore = "Commanding the thunderous wrath of galactic storms to sweep away stagnant mental patterns.",
            requiredForcesHint = "Soul Level 50 • Dynamic mastery of Wrath & Diligence.",
            accentColorHex = 0xFF06B6D4,
            characterClass = "Tempest Lord",
            celestialRace = "Primordial Titan",
            unlockPrerequisiteHint = "Attain Soul Level 50 through continuous daily discipline."
        ),

        // Level 55: Void Emperor
        AdvancedArchetype(
            id = "arch_nether_monarch",
            name = "The Nether Monarch",
            subtitle = "Absolute Sovereign of the Black Sun",
            titlePrefix = "Nether Lord",
            requiredLevel = 55,
            category = ArchetypeCategory.VOID,
            element = "Black Solar Fire",
            sigilIcon = "👑",
            passivePerk = "+50% Shadow Force generation with zero risk of cognitive degradation.",
            lore = "Rules the space where stars collapse, transforming unbridled ambition into divine kingdom building.",
            requiredForcesHint = "Soul Level 55 • High Pride, Greed & Intention.",
            accentColorHex = 0xFF991B1B,
            characterClass = "Nether Monarch",
            celestialRace = "Abyssal Netherkin",
            unlockPrerequisiteHint = "Reach Soul Level 55 & cultivate master-level transmutation."
        ),

        // Level 60: Starborne Oracle
        AdvancedArchetype(
            id = "arch_starborne_oracle",
            name = "The Starborne Oracle",
            subtitle = "Voice of the Cosmic Mind",
            titlePrefix = "Oracle",
            requiredLevel = 60,
            category = ArchetypeCategory.MYSTIC,
            element = "Nebula Consciousness",
            sigilIcon = "🔮",
            passivePerk = "Direct telepathic attunement with divine feedback and instant alchemical conversion.",
            lore = "Her eyes reflect galaxies colliding; her words whisper the fundamental laws of soul manifestation.",
            requiredForcesHint = "Soul Level 60 • High Humanity, Gratitude & Wisdom.",
            accentColorHex = 0xFFD946EF,
            characterClass = "Starborne Oracle",
            celestialRace = "Astral Starborne",
            unlockPrerequisiteHint = "Reach Soul Level 60 with Humanity > 90."
        ),

        // Level 70: Titan of the World Tree
        AdvancedArchetype(
            id = "arch_yggdrasil_titan",
            name = "The Yggdrasil Titan",
            subtitle = "Root of the Multiverse",
            titlePrefix = "Worldroot",
            requiredLevel = 70,
            category = ArchetypeCategory.PRIMORDIAL,
            element = "Cosmic Arbor",
            sigilIcon = "🌳",
            passivePerk = "Infinite stability tether; vessel grounding cannot be shaken by any mortal storm.",
            lore = "Roots drink from the rivers of creation; branches support the birth of new constellations.",
            requiredForcesHint = "Soul Level 70 • Maximum Temperance & Diligence.",
            accentColorHex = 0xFF15803D,
            characterClass = "Worldroot Titan",
            celestialRace = "Primordial Titan",
            unlockPrerequisiteHint = "Reach Soul Level 70 with Stability > 90."
        ),

        // Level 80: Void Weaver of Fate
        AdvancedArchetype(
            id = "arch_fate_weaver",
            name = "The Weaver of Destiny",
            subtitle = "Architect of Singular Realities",
            titlePrefix = "Fatemaster",
            requiredLevel = 80,
            category = ArchetypeCategory.COSMIC,
            element = "Quantum Probability",
            sigilIcon = "🕸️",
            passivePerk = "Multiplies all progression gains by 3x across the entire Godly System.",
            lore = "Plucks the strings of causality to guide all mortal timelines toward ultimate harmony.",
            requiredForcesHint = "Soul Level 80 • High integration of all 14 Forces.",
            accentColorHex = 0xFF6366F1,
            characterClass = "Destiny Architect",
            celestialRace = "Quantum Singularity",
            unlockPrerequisiteHint = "Reach Soul Level 80 across all soul matrix paths."
        ),

        // Level 90: Supreme Solar Godhead
        AdvancedArchetype(
            id = "arch_solar_deity",
            name = "The Apex Solar Deity",
            subtitle = "Incarnation of Pure Creation Light",
            titlePrefix = "Luminarch",
            requiredLevel = 90,
            category = ArchetypeCategory.RADIANT,
            element = "Hyper-Luminescent Dawn",
            sigilIcon = "☀️",
            passivePerk = "Pours boundless radiance into the matrix; unlocks all cosmetics automatically.",
            lore = "The living sun from which all conscious life springs, radiating infinite warmth and benevolence.",
            requiredForcesHint = "Soul Level 90 • Pure Virtue mastery.",
            accentColorHex = 0xFFF59E0B,
            characterClass = "Apex Solar Deity",
            celestialRace = "Solar Seraphim",
            unlockPrerequisiteHint = "Reach Soul Level 90 with flawless virtue cultivation."
        ),

        // Level 100: Supreme Cosmic Divinity
        AdvancedArchetype(
            id = "arch_old_god_avatar",
            name = "The Primordial Singularity",
            subtitle = "Transcendent Sovereign of All Realities",
            titlePrefix = "Godhead",
            requiredLevel = 100,
            category = ArchetypeCategory.COSMIC,
            element = "Omnipresent Cosmos",
            sigilIcon = "🔱",
            passivePerk = "Absolute mastery over consciousness. All dimensions and forces exist in effortless unison.",
            lore = "The final culmination of the Godly System. The boundary between vessel, cosmos, and creator dissolves into infinite awareness.",
            requiredForcesHint = "Soul Level 100 • Pinnacle of Soul Matrix Mastery.",
            accentColorHex = 0xFFF59E0B,
            characterClass = "Singularity Godhead",
            celestialRace = "Quantum Singularity",
            unlockPrerequisiteHint = "Attain the ultimate milestone: Soul Level 100."
        )
    )

    fun getArchetypeById(id: String): AdvancedArchetype {
        return ALL_ARCHETYPES.find { it.id == id } ?: ALL_ARCHETYPES.first()
    }
}
