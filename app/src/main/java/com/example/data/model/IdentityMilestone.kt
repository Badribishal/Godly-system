package com.example.data.model

import com.example.data.local.EvaluationRecordEntity
import com.example.data.local.EvolutionEventEntity

enum class BadgeTier(val displayName: String, val colorHex: Long, val rune: String) {
    BRONZE("Bronze", 0xFFCD7F32, "🥉"),
    SILVER("Silver", 0xFFC0C0C0, "🥈"),
    GOLD("Gold", 0xFFFFD700, "🥇"),
    RADIANT("Radiant", 0xFF38BDF8, "✨"),
    DIVINE("Divine", 0xFFA855F7, "👑")
}

data class IdentityMilestoneBadge(
    val id: String,
    val name: String,
    val runeIcon: String,
    val tier: BadgeTier,
    val description: String,
    val requirementText: String,
    val isUnlocked: Boolean,
    val progressCurrent: Int,
    val progressMax: Int,
    val unlockedAtText: String? = null
)

object IdentityMilestoneCatalog {

    fun evaluateMilestones(
        soul: SoulIdentity,
        records: List<EvaluationRecordEntity>,
        events: List<EvolutionEventEntity>,
        loginStreak: Int = 1
    ): List<IdentityMilestoneBadge> {
        val totalTransmutations = records.size
        val totalEvolutions = events.size
        val shards = soul.soulShards
        val maxVirtueScore = soul.virtueScores.values.maxOrNull() ?: 30
        val maxShadowScore = soul.shadowScores.values.maxOrNull() ?: 30
        val hasAdvancedClass = !soul.advancedClass.isNullOrBlank() || soul.race != "Human"
        val resonancePercent = ((soul.humanity * 0.5f) + (soul.stability * 0.5f)).toInt()

        return listOf(
            IdentityMilestoneBadge(
                id = "badge_genesis",
                name = "Awakened Neophyte",
                runeIcon = "🌟",
                tier = BadgeTier.BRONZE,
                description = "Ignited the persona vessel within the Godly System.",
                requirementText = "Initialize your vessel and unlock the Matrix.",
                isUnlocked = true,
                progressCurrent = 1,
                progressMax = 1,
                unlockedAtText = "Genesis Era"
            ),
            IdentityMilestoneBadge(
                id = "badge_alchemist_1",
                name = "Adept Transmuter",
                runeIcon = "🔮",
                tier = BadgeTier.SILVER,
                description = "Conducted 3 self-evaluations to transmute archetypal forces.",
                requirementText = "Complete 3 Soul Records or Trials.",
                isUnlocked = totalTransmutations >= 3,
                progressCurrent = totalTransmutations.coerceAtMost(3),
                progressMax = 3,
                unlockedAtText = if (totalTransmutations >= 3) "Transmutation Tier I" else null
            ),
            IdentityMilestoneBadge(
                id = "badge_alchemist_2",
                name = "Astral Alchemist",
                runeIcon = "⚡",
                tier = BadgeTier.GOLD,
                description = "Mastered the flow of internal polarity with 10 total evaluations.",
                requirementText = "Complete 10 Soul Records or Trials.",
                isUnlocked = totalTransmutations >= 10,
                progressCurrent = totalTransmutations.coerceAtMost(10),
                progressMax = 10,
                unlockedAtText = if (totalTransmutations >= 10) "Transmutation Tier II" else null
            ),
            IdentityMilestoneBadge(
                id = "badge_virtue_paragon",
                name = "Virtue Paragon",
                runeIcon = "🛡️",
                tier = BadgeTier.RADIANT,
                description = "Cultivated supreme affinity with a Heavenly Virtue exceeding 65 resonance.",
                requirementText = "Raise any Virtue score to 65 or higher.",
                isUnlocked = maxVirtueScore >= 65,
                progressCurrent = maxVirtueScore.coerceAtMost(65),
                progressMax = 65,
                unlockedAtText = if (maxVirtueScore >= 65) "Paragon of ${soul.dominantVirtue.displayName}" else null
            ),
            IdentityMilestoneBadge(
                id = "badge_shadow_conqueror",
                name = "Shadow Sovereign",
                runeIcon = "🌑",
                tier = BadgeTier.GOLD,
                description = "Harnessed the raw potency of a Deadly Sin above 65 resonance.",
                requirementText = "Raise any Shadow score to 65 or higher.",
                isUnlocked = maxShadowScore >= 65,
                progressCurrent = maxShadowScore.coerceAtMost(65),
                progressMax = 65,
                unlockedAtText = if (maxShadowScore >= 65) "Master of ${soul.dominantShadow.displayName}" else null
            ),
            IdentityMilestoneBadge(
                id = "badge_metamorphosis",
                name = "Metamorphic Ascendant",
                runeIcon = "🧬",
                tier = BadgeTier.DIVINE,
                description = "Underwent an evolutionary racial metamorphosis beyond mundane humanity.",
                requirementText = "Evolve vessel into a Mythic or Celestial race.",
                isUnlocked = hasAdvancedClass,
                progressCurrent = if (hasAdvancedClass) 1 else 0,
                progressMax = 1,
                unlockedAtText = if (hasAdvancedClass) "${soul.race} Transmutation" else null
            ),
            IdentityMilestoneBadge(
                id = "badge_harmonic_sage",
                name = "Harmonic Sage",
                runeIcon = "🕊️",
                tier = BadgeTier.RADIANT,
                description = "Maintained high soul resonance and humanity tether.",
                requirementText = "Reach 75%+ Humanity with sustained stability.",
                isUnlocked = soul.humanity >= 75,
                progressCurrent = soul.humanity.coerceAtMost(75),
                progressMax = 75,
                unlockedAtText = if (soul.humanity >= 75) "Equilibrium Attained" else null
            ),
            IdentityMilestoneBadge(
                id = "badge_shard_magnate",
                name = "Astral Shard Sovereign",
                runeIcon = "💎",
                tier = BadgeTier.DIVINE,
                description = "Amassed an immense hoard of 150+ Soul Shards.",
                requirementText = "Collect 150 total Soul Shards.",
                isUnlocked = shards >= 150,
                progressCurrent = shards.coerceAtMost(150),
                progressMax = 150,
                unlockedAtText = if (shards >= 150) "Vault of Shards" else null
            )
        )
    }
}
