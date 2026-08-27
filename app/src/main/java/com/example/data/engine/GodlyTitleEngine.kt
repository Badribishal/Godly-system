package com.example.data.engine

import com.example.data.model.AdvancedArchetypesCatalog
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType

object GodlyTitleEngine {

    /**
     * Generates a unique, prestigious Godly Title based on the vessel's:
     * - Soul Level and Matrix Tier
     * - Dominant Virtue and Dominant Shadow
     * - Attuned Archetype
     * - Humanity & Stability ratios
     */
    fun computeGodlyTitle(soul: SoulIdentity): String {
        val level = soul.soulLevel
        val tier = MatrixTier.fromLevel(level)
        val archetype = AdvancedArchetypesCatalog.getArchetypeById(soul.attunedArchetypeId)

        val virtue = soul.dominantVirtue
        val shadow = soul.dominantShadow

        // Tier Prefix / Epithet
        val tierPrefix = when (tier) {
            MatrixTier.INITIATE -> when {
                level == 1 -> "The Awakening"
                level == 2 -> "The Seeking"
                else -> "The Resonant"
            }
            MatrixTier.AWAKENED_CONDUIT -> "The Radiant"
            MatrixTier.ASTRAL_ADEPT -> "The Astral"
            MatrixTier.ETHEREAL_SOVEREIGN -> "The Ethereal"
            MatrixTier.PRIMORDIAL_ASCENDANT -> "The Primordial"
            MatrixTier.COSMIC_OVERLORD -> "The Apex"
        }

        // Core Epithet / Role derived from Archetype + Dominant Virtue/Shadow
        val role = when (archetype.id) {
            "arch_seeker" -> when (virtue) {
                VirtueType.HUMILITY -> "Seeker of Quiet Grace"
                VirtueType.COURAGE -> "Seeker of Dawnfire"
                VirtueType.CHARITY -> "Seeker of Living Empathy"
                VirtueType.TEMPERANCE -> "Seeker of Equilibrium"
                VirtueType.DILIGENCE -> "Seeker of Iron Will"
                VirtueType.PATIENCE -> "Seeker of Deep Stillness"
                VirtueType.GRATITUDE -> "Seeker of Cosmic Radiance"
            }
            "arch_luminous_aegis" -> "Aegis Sentinel of the Solar Crest"
            "arch_shadow_weaver" -> "Weaver of Transmuted Nether"
            "arch_alchemical_sovereign" -> "Sovereign of Dual Harmonics"
            "arch_solar_seraph" -> "Seraph of the Seventh Dawn"
            "arch_void_walker" -> "Stalker of the Infinite Abyss"
            "arch_astral_sage" -> "Archon of Starlight Wisdom"
            "arch_earth_titan" -> "Titan of Immovable Roots"
            "arch_solar_phoenix" -> "Phoenix of Eternal Luminescence"
            "arch_abyssal_leviathan" -> "Leviathan of the Deepest Void"
            "arch_astral_chronomancer" -> "Timeweaver of Unfolding Realities"
            "arch_solar_justiciar" -> "Justiciar of Incandescent Truth"
            "arch_tempest_primordial" -> "Lord of Celestial Tempests"
            "arch_nether_monarch" -> "Nether Monarch of the Black Sun"
            "arch_starborne_oracle" -> "Oracle of Nebula Harmonics"
            "arch_yggdrasil_titan" -> "Pillar of the World Tree"
            "arch_fate_weaver" -> "Architect of Singular Realities"
            "arch_solar_deity" -> "Luminarch of the Infinite Sun"
            "arch_old_god_avatar" -> "Godhead of the Cosmic Singularity"
            else -> archetype.name.removePrefix("The ")
        }

        // Milestone / Affinity Suffix based on Humanity and Stability
        val suffix = when {
            soul.humanity >= 90 && soul.stability >= 80 -> "✦ Ascended Paragon"
            soul.humanity >= 85 -> "✦ Pure Harmony"
            soul.stability >= 85 -> "✦ Unshakable Foundation"
            level >= 40 -> "✦ Cosmic Pinnacle"
            level >= 25 -> "✦ Sovereign Vessel"
            level >= 15 -> "✦ Ethereal Master"
            level >= 5 -> "✦ Awakened Conduit"
            else -> ""
        }

        return if (suffix.isNotBlank()) {
            "$tierPrefix $role $suffix"
        } else {
            "$tierPrefix $role"
        }
    }
}
