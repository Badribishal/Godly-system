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
    val equippedEffectId: String = "effect_default",
    val unlockedEffectIds: Set<String> = setOf("effect_default"),
    val claimedAchievementIds: Set<String> = emptySet()
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
                element = "Aether / Unattuned",
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
                equippedEffectId = "effect_default",
                unlockedEffectIds = setOf("effect_default"),
                claimedAchievementIds = emptySet()
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

