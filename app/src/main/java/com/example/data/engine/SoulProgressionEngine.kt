package com.example.data.engine

import com.example.data.model.AdvancedArchetype
import com.example.data.model.AdvancedArchetypesCatalog

enum class MatrixTier(
    val tierNumber: Int,
    val romanNumeral: String,
    val title: String,
    val rune: String,
    val minLevel: Int,
    val maxLevel: Int,
    val colorHex: Long
) {
    INITIATE(1, "I", "Initiate Vessel", "💠", 1, 4, 0xFF60A5FA),
    AWAKENED_CONDUIT(2, "II", "Awakened Conduit", "⚡", 5, 9, 0xFF34D399),
    ASTRAL_ADEPT(3, "III", "Astral Adept", "🌌", 10, 14, 0xFFA78BFA),
    ETHEREAL_SOVEREIGN(4, "IV", "Ethereal Sovereign", "👑", 15, 24, 0xFFFBBF24),
    PRIMORDIAL_ASCENDANT(5, "V", "Primordial Ascendant", "🐉", 25, 39, 0xFFF43F5E),
    COSMIC_OVERLORD(6, "VI", "Cosmic Overlord", "🔱", 40, 999, 0xFFF59E0B);

    companion object {
        fun fromLevel(level: Int): MatrixTier {
            return entries.find { level in it.minLevel..it.maxLevel } ?: COSMIC_OVERLORD
        }
    }
}

data class LevelUpOutcome(
    val oldLevel: Int,
    val newLevel: Int,
    val currentExp: Int,
    val maxExpForLevel: Int,
    val totalExp: Int,
    val levelsGained: Int,
    val shardsReward: Int,
    val oldTier: MatrixTier,
    val newTier: MatrixTier,
    val newlyUnlockedArchetypes: List<AdvancedArchetype>,
    val tierPromoted: Boolean
)

object SoulProgressionEngine {

    /**
     * Calculates required EXP for a given level.
     * Level 1 -> 2: 100 EXP
     * Level 2 -> 3: 150 EXP
     * Level 3 -> 4: 215 EXP
     * Level 4 -> 5: 290 EXP
     * Level 5 -> 6: 380 EXP, etc.
     */
    fun expRequiredForLevel(level: Int): Int {
        val base = 90
        val linear = level * 30
        val quadratic = (level * level * 4)
        return (base + linear + quadratic).coerceAtLeast(100)
    }

    /**
     * Processes added EXP and computes level advancement, rewards, and unlocked archetypes.
     */
    fun applyExpGain(
        currentLevel: Int,
        currentExp: Int,
        totalExp: Int,
        gainedExp: Int,
        alreadyUnlockedArchetypeIds: Set<String>
    ): Pair<Triple<Int, Int, Int>, LevelUpOutcome?> {
        var level = currentLevel.coerceAtLeast(1)
        var exp = currentExp + gainedExp
        var newTotalExp = totalExp + gainedExp
        var reqExp = expRequiredForLevel(level)
        var levelsGained = 0
        var bonusShards = 0

        val oldTier = MatrixTier.fromLevel(currentLevel)

        while (exp >= reqExp && level < 100) {
            exp -= reqExp
            level++
            levelsGained++
            // Award 50 + (level * 5) bonus Soul Shards per level gained
            bonusShards += (50 + (level * 5))
            reqExp = expRequiredForLevel(level)
        }

        val newTier = MatrixTier.fromLevel(level)
        val tierPromoted = newTier.tierNumber > oldTier.tierNumber

        val newlyUnlocked = AdvancedArchetypesCatalog.ALL_ARCHETYPES.filter {
            it.requiredLevel <= level && !alreadyUnlockedArchetypeIds.contains(it.id)
        }

        val outcome = if (levelsGained > 0) {
            LevelUpOutcome(
                oldLevel = currentLevel,
                newLevel = level,
                currentExp = exp,
                maxExpForLevel = reqExp,
                totalExp = newTotalExp,
                levelsGained = levelsGained,
                shardsReward = bonusShards,
                oldTier = oldTier,
                newTier = newTier,
                newlyUnlockedArchetypes = newlyUnlocked,
                tierPromoted = tierPromoted
            )
        } else {
            null
        }

        return Pair(Triple(level, exp, newTotalExp), outcome)
    }
}
