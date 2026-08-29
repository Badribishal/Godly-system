package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluation_records")
data class EvaluationRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val emotion: String,
    val primaryShadow: String?,
    val primaryVirtue: String?,
    val situation: String,
    val intention: String,
    val action: String,
    val consequence: String,
    val reflection: String,
    val analysisInsight: String,
    val forcesResonatedJson: String,
    val humanityShift: Int,
    val stabilityShift: Int
)

@Entity(tableName = "soul_profile")
data class SoulProfileEntity(
    @PrimaryKey
    val id: Int = 1, // Single profile singleton
    val race: String,
    val className: String,
    val advancedClass: String?,
    val archetype: String,
    val element: String,
    val alignment: String,
    val currentTitle: String,
    val dominantShadow: String,
    val dominantVirtue: String,
    val shadowScoresJson: String, // Map serialized to JSON
    val virtueScoresJson: String, // Map serialized to JSON
    val humanity: Int,
    val stability: Int,
    val evolutionProgress: Int,
    val possibleEvolution: String,
    val strengthsJson: String,
    val weaknessesJson: String,
    val systemMessage: String,
    val soulShards: Int = 100,
    val soulLevel: Int = 1,
    val soulExp: Int = 0,
    val totalSoulExp: Int = 0,
    val attunedArchetypeId: String = "arch_seeker",
    val unlockedArchetypesJson: String = "[\"arch_seeker\"]",
    val equippedEffectId: String = "effect_default",
    val unlockedEffectsJson: String = "[\"effect_default\"]",
    val claimedAchievementsJson: String = "[]",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "evolution_events")
data class EvolutionEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dayNumber: Int,
    val eventType: String, // "RACE_MUTATION", "CLASS_EVOLUTION", "TRAIT_AWAKENED", "UNKNOWN_EVENT", "TITLE_BESTOWED", "FORCE_SURGE"
    val title: String,
    val description: String,
    val isUnknownEvent: Boolean = false,
    val runeIcon: String = "✨"
)

@Entity(tableName = "daily_trials")
data class DailyTrialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // "Dilemma", "The Paradox", "Trial of Iron", "Echo of the Void"
    val scenario: String,
    val optionsJson: String, // List of choices with force affinities
    val completedTimestamp: Long? = null,
    val selectedOptionIndex: Int? = null,
    val userReflection: String? = null
)

@Entity(tableName = "evaluation_draft")
data class EvaluationDraftEntity(
    @PrimaryKey
    val id: Int = 1,
    val emotion: String = "Serene",
    val primaryShadow: String? = null,
    val primaryVirtue: String? = null,
    val situation: String = "",
    val intention: String = "",
    val action: String = "",
    val consequence: String = "",
    val reflection: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_emotion_records")
data class DailyEmotionRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dateKey: String, // e.g. "2026-08-29"
    val positiveEmotionsJson: String, // JSON array string
    val negativeEmotionsJson: String, // JSON array string
    val positiveCount: Int,
    val negativeCount: Int,
    val dominantValence: String, // "POSITIVE", "NEGATIVE", "EQUILIBRIUM"
    val positivityRatio: Float, // 0.0f to 1.0f
    val fantasyArchetypeId: String,
    val fantasyArchetypeName: String,
    val fantasyArchetypeTitle: String,
    val fantasyArchetypeDescription: String,
    val fantasyArchetypeRune: String,
    val fantasyArchetypeElement: String,
    val fantasyArchetypeAuraColorHex: Long,
    val fantasyArchetypePowerBonus: String,
    val dailyDecree: String,
    val humanityShift: Int,
    val stabilityShift: Int,
    val userNote: String = ""
)

@Entity(tableName = "tracked_emotions")
data class TrackedEmotionEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val valence: String, // "POSITIVE" or "NEGATIVE"
    val category: String,
    val runeIcon: String,
    val essence: String,
    val colorHex: Long,
    val associatedForce: String? = null,
    val usageCount: Int = 0,
    val lastUsedTimestamp: Long? = null
)

