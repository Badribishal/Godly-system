package com.example.data.engine

import com.example.data.model.CultivationRealm
import com.example.data.model.SoulIdentity
import com.example.data.model.SpiritShopItem
import com.example.data.model.SpiritTreasuryCatalog
import kotlin.math.roundToInt
import kotlin.random.Random

data class BreakthroughChanceInfo(
    val baseChance: Int,
    val stabilityBonus: Int,
    val humanityBonus: Int,
    val itemBonus: Int,
    val totalChance: Int,
    val isGuaranteed: Boolean
)

data class BreakthroughResult(
    val success: Boolean,
    val oldRealm: CultivationRealm,
    val newRealm: CultivationRealm,
    val oldStage: Int,
    val newStage: Int,
    val isMajorRealmBreakthrough: Boolean,
    val titleBestowed: String?,
    val evolutionBonus: Int,
    val shardsAwarded: Int,
    val omenMessage: String,
    val updatedSoul: SoulIdentity
)

data class CauldronRefineResult(
    val itemAwarded: SpiritShopItem,
    val qiGained: Int,
    val message: String
)

object QiCultivationEngine {

    fun calculateBaseMaxQi(realm: CultivationRealm, stage: Int, unlockedArtifactIds: Set<String>): Int {
        val stageMultiplier = 1.0f + ((stage - 1) * 0.25f)
        var maxQi = (realm.baseMaxQi * stageMultiplier).roundToInt()

        // Add bonuses from permanent artifacts
        unlockedArtifactIds.forEach { artifactId ->
            val item = SpiritTreasuryCatalog.getItemById(artifactId)
            if (item != null) {
                maxQi += item.maxQiBonus
            }
        }

        return maxQi.coerceAtLeast(100)
    }

    fun calculateBreakthroughChance(soul: SoulIdentity, activeBonusPercent: Int = 0): BreakthroughChanceInfo {
        val currentRealm = CultivationRealm.fromNameOrId(soul.cultivationRealm)
        val isMajor = soul.cultivationStage >= currentRealm.maxStages

        val base = if (isMajor) {
            when (currentRealm) {
                CultivationRealm.QI_CONDENSATION -> 75
                CultivationRealm.FOUNDATION_ESTABLISHMENT -> 65
                CultivationRealm.CORE_FORMATION -> 55
                CultivationRealm.NASCENT_SOUL -> 45
                CultivationRealm.SOUL_SEVERING -> 35
                CultivationRealm.TRIBULATION_TRANSCENDENCE -> 25
                CultivationRealm.CELESTIAL_DIVINITY -> 100
            }
        } else {
            85
        }

        val stabilityBonus = (soul.stability / 10).coerceIn(0, 10)
        val humanityBonus = (soul.humanity / 15).coerceIn(0, 8)
        val itemBonus = activeBonusPercent

        val total = (base + stabilityBonus + humanityBonus + itemBonus).coerceIn(5, 100)

        return BreakthroughChanceInfo(
            baseChance = base,
            stabilityBonus = stabilityBonus,
            humanityBonus = humanityBonus,
            itemBonus = itemBonus,
            totalChance = total,
            isGuaranteed = total >= 100
        )
    }

    fun performBreakthrough(soul: SoulIdentity, activeBonusPercent: Int = 0): BreakthroughResult {
        val currentRealm = CultivationRealm.fromNameOrId(soul.cultivationRealm)
        val chanceInfo = calculateBreakthroughChance(soul, activeBonusPercent)
        val roll = Random.nextInt(1, 101)
        val success = roll <= chanceInfo.totalChance

        if (success) {
            val isMajor = soul.cultivationStage >= currentRealm.maxStages
            val (nextRealm, nextStage) = if (isMajor) {
                val nextOrdinal = (currentRealm.ordinal + 1).coerceAtMost(CultivationRealm.entries.lastIndex)
                Pair(CultivationRealm.entries[nextOrdinal], 1)
            } else {
                Pair(currentRealm, soul.cultivationStage + 1)
            }

            val newMaxQi = calculateBaseMaxQi(nextRealm, nextStage, soul.unlockedArtifactIds)
            val shardsBonus = if (isMajor) 150 else 50
            val evoBonus = if (isMajor) 15 else 5

            val titleBestowed = if (isMajor) {
                "${nextRealm.defaultTitle} • Stage $nextStage"
            } else null

            val omen = if (isMajor) {
                "⚡ A grand celestial omen tears through the firmament! The vessel has ascended to [${nextRealm.displayName}]."
            } else {
                "✨ Spiritual meridians widen. Qi flows unimpeded through Stage $nextStage of ${currentRealm.displayName}."
            }

            val updatedSoul = soul.copy(
                cultivationRealm = nextRealm.displayName,
                cultivationStage = nextStage,
                currentQi = 0,
                maxQi = newMaxQi,
                evolutionProgress = (soul.evolutionProgress + evoBonus).coerceIn(0, 100),
                soulShards = soul.soulShards + shardsBonus,
                currentTitle = titleBestowed ?: soul.currentTitle
            )

            return BreakthroughResult(
                success = true,
                oldRealm = currentRealm,
                newRealm = nextRealm,
                oldStage = soul.cultivationStage,
                newStage = nextStage,
                isMajorRealmBreakthrough = isMajor,
                titleBestowed = titleBestowed,
                evolutionBonus = evoBonus,
                shardsAwarded = shardsBonus,
                omenMessage = omen,
                updatedSoul = updatedSoul
            )
        } else {
            // Failed breakthrough: minor Qi backlash (retains 40% Qi, loses 5 stability)
            val qiRetained = (soul.maxQi * 0.40f).roundToInt()
            val newStability = (soul.stability - 5).coerceAtLeast(10)
            val updatedSoul = soul.copy(
                currentQi = qiRetained,
                stability = newStability
            )

            return BreakthroughResult(
                success = false,
                oldRealm = currentRealm,
                newRealm = currentRealm,
                oldStage = soul.cultivationStage,
                newStage = soul.cultivationStage,
                isMajorRealmBreakthrough = false,
                titleBestowed = null,
                evolutionBonus = 0,
                shardsAwarded = 0,
                omenMessage = "⚠️ Tribulation Backlash! Spiritual meridians experienced turbulent turbulence. (40% Qi preserved, -5 Stability)",
                updatedSoul = updatedSoul
            )
        }
    }

    fun refineElixirInCauldron(soul: SoulIdentity, gemCost: Int): CauldronRefineResult {
        val roll = Random.nextInt(100)
        val item = when {
            roll < 40 -> SpiritTreasuryCatalog.getItemById("pill_spirit_gathering")!!
            roll < 70 -> SpiritTreasuryCatalog.getItemById("pill_pure_yang")!!
            roll < 88 -> SpiritTreasuryCatalog.getItemById("pill_mind_clarity_dew")!!
            roll < 97 -> SpiritTreasuryCatalog.getItemById("pill_foundation_solidifying")!!
            else -> SpiritTreasuryCatalog.getItemById("pill_void_nirvana")!!
        }

        val qiGain = item.qiBonus + Random.nextInt(50, 150)
        return CauldronRefineResult(
            itemAwarded = item,
            qiGained = qiGain,
            message = "🔥 The Sacred Cauldron bursts with golden flames! You distilled 1x [${item.name}] and gathered +$qiGain Qi!"
        )
    }
}
