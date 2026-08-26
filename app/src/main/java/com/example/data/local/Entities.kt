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
