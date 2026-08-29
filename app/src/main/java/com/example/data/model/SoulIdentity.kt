package com.example.data.model

data class EvolutionState(
    val race: String,
    val className: String,
    val advancedClass: String?,
    val archetype: String,
    val element: String,
    val alignment: String,
    val currentTitle: String,
    val dominantShadow: ShadowType,
    val dominantVirtue: VirtueType,
    val shadowScores: Map<ShadowType, Int>,
    val virtueScores: Map<VirtueType, Int>,
    val humanity: Int, // 0 - 100
    val stability: Int, // 0 - 100
    val evolutionProgress: Int, // 0 - 100
    val possibleEvolution: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val systemMessage: String,
    val resonanceFrequency: String = "432 Hz • Astral Harmonics",
    val evolutionHistoryCount: Int = 0,
    val soulShards: Int = 100,
    val soulLevel: Int = 1,
    val soulExp: Int = 0,
    val totalSoulExp: Int = 0,
    val attunedArchetypeId: String = "arch_seeker",
    val unlockedArchetypeIds: Set<String> = setOf("arch_seeker"),
    val equippedEffectId: String = "effect_default",
    val unlockedEffectIds: Set<String> = setOf("effect_default"),
    val claimedAchievementIds: Set<String> = emptySet(),
    val currentQi: Int = 120,
    val maxQi: Int = 300,
    val cultivationRealm: String = "Qi Condensation",
    val cultivationStage: Int = 1,
    val spiritualRoots: String = "Five Elements Balanced Root",
    val equippedArtifactId: String = "artifact_none",
    val unlockedArtifactIds: Set<String> = emptySet(),
    val activeTribulationTalismanCount: Int = 0,
    // Elemental Powers & Mastery Loadout
    val primaryElement: String = "Fire",
    val equippedAttackId: String = "atk_fire_sunflare",
    val equippedManipulationId: String = "man_fire_pyrokinesis",
    val equippedSupportId: String = "sup_fire_blaze_mantle",
    val equippedHealingId: String = "heal_fire_phoenix_rebirth",
    val equippedTraitId: String = "trait_fire_pyre_heart",
    val unlockedPowerIds: Set<String> = setOf(
        "atk_fire_sunflare", "man_fire_pyrokinesis", "sup_fire_blaze_mantle", "heal_fire_phoenix_rebirth", "trait_fire_pyre_heart",
        "atk_water_tidal_spear", "man_water_fluid_redirection", "sup_water_mist_veil", "heal_water_ocean_embrace", "trait_water_tidal_harmony",
        "atk_air_vortex_tempest", "man_air_barometric_control", "sup_air_tailwind_haste", "heal_air_prana_respiration", "trait_air_skywalker",
        "atk_earth_tectonic_cataclysm", "man_earth_geokinetic_mold", "sup_earth_granite_fortress", "heal_earth_mineral_spring", "trait_earth_unmovable_core",
        "atk_lightning_heaven_strike", "man_lightning_electromagnetism", "sup_lightning_synaptic_burst", "heal_lightning_defibrillating_arc", "trait_lightning_stormborn",
        "atk_ice_absolute_zero_lance", "man_ice_cryo_lattice", "sup_ice_permafrost_barrier", "heal_ice_stasis_cocoon", "trait_ice_glacial_equanimity",
        "atk_dark_abyssal_cleave", "man_dark_shadowmeld", "sup_dark_eclipse_shroud", "heal_dark_malice_transmutation", "trait_dark_umbra_sovereign",
        "atk_light_solar_lance", "man_light_prism_refraction", "sup_light_seraph_aegis", "heal_light_divine_sanative", "trait_light_solar_apostle",
        "atk_gravity_singularity_crush", "man_gravity_vector_inversion", "sup_gravity_heavy_anchor", "heal_gravity_weightless_restoration", "trait_gravity_graviton_core",
        "atk_cosmic_supernova_pulse", "man_cosmic_astral_leyline", "sup_cosmic_astral_projection", "heal_cosmic_nebula_purification", "trait_cosmic_starforged",
        "atk_void_null_oblivion", "man_void_spatial_rift", "sup_void_entropy_nullification", "heal_void_hollow_cleansing", "trait_void_nihil_sovereign",
        "atk_time_temporal_stasis_cleave", "man_time_chrono_dilation", "sup_time_future_precognition", "heal_time_rewind_recovery", "trait_time_chrono_wanderer",
        "atk_nature_primordial_thornburst", "man_nature_botanical_growth", "sup_nature_symbiotic_bloom", "heal_nature_tree_of_life", "trait_nature_verdant_heart",
        "atk_soul_severing_slash", "man_soul_spectral_weaving", "sup_soul_telepathic_harmony", "heal_soul_sanctuary_repair", "trait_soul_immortal_mind"
    ),
    val powerMasteryMap: Map<String, Int> = mapOf(
        "atk_fire_sunflare" to 1, "man_fire_pyrokinesis" to 1, "sup_fire_blaze_mantle" to 1, "heal_fire_phoenix_rebirth" to 1, "trait_fire_pyre_heart" to 1
    )
) {
    companion object {
        fun initial(): EvolutionState {
            val initialShadows = ShadowType.values().associateWith { 30 }
            val initialVirtues = VirtueType.values().associateWith { 30 }
            return EvolutionState(
                race = "Human",
                className = "Seeker",
                advancedClass = null,
                archetype = "The Awakening Vessel",
                element = "Fire / Thermal Ignition",
                alignment = "True Neutral",
                currentTitle = "The Unwritten Soul",
                dominantShadow = ShadowType.PRIDE,
                dominantVirtue = VirtueType.HUMILITY,
                shadowScores = initialShadows,
                virtueScores = initialVirtues,
                humanity = 85,
                stability = 70,
                evolutionProgress = 15,
                possibleEvolution = "??? [Unstable Resonance]",
                strengths = listOf("Malleable Potential", "Untapped Willpower", "Intuitive Perception"),
                weaknesses = listOf("Uncalibrated Forces", "Vulnerable to Cognitive Dissonance"),
                systemMessage = "The System has initiated observation. Your choices will shape the vessel.",
                soulShards = 100,
                soulLevel = 1,
                soulExp = 0,
                totalSoulExp = 0,
                attunedArchetypeId = "arch_seeker",
                unlockedArchetypeIds = setOf("arch_seeker"),
                equippedEffectId = "effect_default",
                unlockedEffectIds = setOf("effect_default"),
                claimedAchievementIds = emptySet(),
                currentQi = 120,
                maxQi = 300,
                cultivationRealm = "Qi Condensation",
                cultivationStage = 1,
                spiritualRoots = "Five Elements Balanced Root",
                equippedArtifactId = "artifact_none",
                unlockedArtifactIds = emptySet(),
                activeTribulationTalismanCount = 0,
                primaryElement = "Fire",
                equippedAttackId = "atk_fire_sunflare",
                equippedManipulationId = "man_fire_pyrokinesis",
                equippedSupportId = "sup_fire_blaze_mantle",
                equippedHealingId = "heal_fire_phoenix_rebirth",
                equippedTraitId = "trait_fire_pyre_heart"
            )
        }
    }
}

typealias SoulIdentity = EvolutionState

data class UserRecord(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val emotion: String,
    val primaryShadow: ShadowType? = null,
    val primaryVirtue: VirtueType? = null,
    val situation: String,
    val intention: String,
    val action: String,
    val consequence: String,
    val reflection: String
)

typealias RecordInput = UserRecord

data class EvaluationResult(
    val shadowDeltas: Map<ShadowType, Int>,
    val virtueDeltas: Map<VirtueType, Int>,
    val humanityDelta: Int,
    val stabilityDelta: Int,
    val evolutionProgressDelta: Int,
    val analysisInsight: String,
    val awakenedTrait: String?,
    val newTitle: String?,
    val evolutionTriggered: Boolean,
    val oldIdentitySummary: String?,
    val newIdentitySummary: String?,
    val systemOmen: String
)
