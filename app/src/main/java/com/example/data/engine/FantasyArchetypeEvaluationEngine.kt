package com.example.data.engine

import com.example.data.model.EmotionCatalog
import com.example.data.model.EmotionItem
import com.example.data.model.EmotionValence

data class DailyFantasyArchetypeResult(
    val archetypeId: String,
    val name: String,
    val title: String,
    val dominanceValence: String, // "POSITIVE", "NEGATIVE", "EQUILIBRIUM"
    val element: String,
    val runeIcon: String,
    val auraColorHex: Long,
    val description: String,
    val powerBonus: String,
    val dailyDecree: String,
    val positivityRatio: Float,
    val positiveCount: Int,
    val negativeCount: Int,
    val dominantCategory: String,
    val humanityDelta: Int,
    val stabilityDelta: Int,
    val suggestedAffirmation: String
)

data class FantasyArchetypeTemplate(
    val id: String,
    val name: String,
    val title: String,
    val valence: String,
    val element: String,
    val runeIcon: String,
    val auraColorHex: Long,
    val description: String,
    val powerBonus: String,
    val dailyDecree: String,
    val targetCategories: List<String>,
    val defaultHumanityShift: Int,
    val defaultStabilityShift: Int
)

object FantasyArchetypeEvaluationEngine {

    val ARCHETYPE_TEMPLATES: List<FantasyArchetypeTemplate> = listOf(
        // Positive Dominant Archetypes
        FantasyArchetypeTemplate(
            id = "solar_seraph",
            name = "Solar Seraph",
            title = "Archon of the Radiant Dawn",
            valence = "POSITIVE",
            element = "Solar Radiance",
            runeIcon = "☀️",
            auraColorHex = 0xFFFFD700,
            description = "Awakened by pure solar harmony and exultant joy. Channels celestial dawn to eradicate despair and illuminate obscure paths.",
            powerBonus = "+25% Radiance Force • Pierces all shadow illusions",
            dailyDecree = "Let your inner dawn shine unobstructed; today your warmth transmutes darkness into gold.",
            targetCategories = listOf("Joy & Radiance"),
            defaultHumanityShift = 4,
            defaultStabilityShift = 3
        ),
        FantasyArchetypeTemplate(
            id = "aether_chronomancer",
            name = "Aether Chronomancer",
            title = "Astral Seer of Infinite Horologes",
            valence = "POSITIVE",
            element = "Astral Aether",
            runeIcon = "🌌",
            auraColorHex = 0xFFA855F7,
            description = "Wielder of cosmic vision and timeless clarity. Transmutes curiosity and sublime awe into sacred revelation.",
            powerBonus = "+30% Insight Resonance • Unravels hidden timelines",
            dailyDecree = "Look beyond the mortal veil; the universe reveals its hidden geometry to the tranquil mind.",
            targetCategories = listOf("Transcendence"),
            defaultHumanityShift = 3,
            defaultStabilityShift = 4
        ),
        FantasyArchetypeTemplate(
            id = "valiant_aegis",
            name = "Valiant Aegis",
            title = "Sovereign Paladin of the Unbroken Oath",
            valence = "POSITIVE",
            element = "Holy Adamant",
            runeIcon = "🛡️",
            auraColorHex = 0xFFF59E0B,
            description = "An immovable bulwark of sovereign will, courage, and righteous fortitude standing between allies and calamity.",
            powerBonus = "+20% Adamantine Will • Impervious to intimidation",
            dailyDecree = "Stand firm in your sovereign station; no tempest can shatter an unwavering spirit.",
            targetCategories = listOf("Power & Sovereign"),
            defaultHumanityShift = 2,
            defaultStabilityShift = 5
        ),
        FantasyArchetypeTemplate(
            id = "verdant_sylph",
            name = "Heart of Gaia",
            title = "Verdant Archon of Living Flora",
            valence = "POSITIVE",
            element = "Verdant Life",
            runeIcon = "🌿",
            auraColorHex = 0xFF34D399,
            description = "Boundless conduit of unconditional empathy, tenderness, and restorative life currents.",
            powerBonus = "+35% Rejuvenation • Dissolves karmic friction",
            dailyDecree = "Heal what was broken through boundless grace; gentleness is the supreme power in creation.",
            targetCategories = listOf("Love & Connection"),
            defaultHumanityShift = 5,
            defaultStabilityShift = 3
        ),
        FantasyArchetypeTemplate(
            id = "harmonic_ascetic",
            name = "Zen Ascetic",
            title = "Harmonic Master of the Unmoved Center",
            valence = "POSITIVE",
            element = "Tranquil Flow",
            runeIcon = "🕊️",
            auraColorHex = 0xFF38BDF8,
            description = "Anchored in the unshakeable stillness of absolute inner peace and crystalline lucidity.",
            powerBonus = "+40% Mental Equilibrium • Zero emotional turbulence",
            dailyDecree = "Be the still lake that reflects the stars; turbulence fades before silent presence.",
            targetCategories = listOf("Peace & Stillness"),
            defaultHumanityShift = 3,
            defaultStabilityShift = 6
        ),

        // Negative Dominant Archetypes
        FantasyArchetypeTemplate(
            id = "abyssal_monarch",
            name = "Abyssal Monarch",
            title = "Dread Sovereign of Volcanic Fury",
            valence = "NEGATIVE",
            element = "Abyssal Netherflame",
            runeIcon = "🔥",
            auraColorHex = 0xFFEF4444,
            description = "Wields dark volcanic wrath and righteous indignation into sovereign tactical dominion and scorched-earth breakthrough.",
            powerBonus = "+30% Nether Strike • Converts frustration into raw breakthrough power",
            dailyDecree = "Forge the flames of wrath into an unbreakable sword; burn through obstacles without consuming your soul.",
            targetCategories = listOf("Anger & Fury"),
            defaultHumanityShift = -1,
            defaultStabilityShift = -2
        ),
        FantasyArchetypeTemplate(
            id = "void_stalker",
            name = "Void Stalker",
            title = "Twilight Phantom of the Silent Abyss",
            valence = "NEGATIVE",
            element = "Abyssal Void",
            runeIcon = "🌑",
            auraColorHex = 0xFF64748B,
            description = "Walks the edge of oblivion and isolation, transmuting despair into silent omnipresence and unflinching survival.",
            powerBonus = "+25% Spectral Phasing • Unaffected by worldly chaos",
            dailyDecree = "In the silence of the void, forge a citadel of pure resilience. Even the darkest night gives birth to stars.",
            targetCategories = listOf("Void & Emptiness"),
            defaultHumanityShift = -2,
            defaultStabilityShift = -3
        ),
        FantasyArchetypeTemplate(
            id = "calamity_berserker",
            name = "Calamity Berserker",
            title = "Dread Titan of the Howling Gale",
            valence = "NEGATIVE",
            element = "Chaos Storm",
            runeIcon = "⚡",
            auraColorHex = 0xFFF97316,
            description = "Channels primal existential dread and panic into hyper-vigilant combat supremacy and lightning-fast reflex.",
            powerBonus = "+35% Adrenaline Surge • Instantaneous threat neutralization",
            dailyDecree = "Face the howling tempest head-on; fear is merely raw kinetic power awaiting your sovereign command.",
            targetCategories = listOf("Fear & Dread"),
            defaultHumanityShift = 0,
            defaultStabilityShift = -3
        ),
        FantasyArchetypeTemplate(
            id = "venom_alchemist",
            name = "Eclipse Inquisitor",
            title = "Venom Alchemist of the Hidden Crucible",
            valence = "NEGATIVE",
            element = "Eclipse Miasma",
            runeIcon = "🧪",
            auraColorHex = 0xFF10B981,
            description = "Master of shadow transmutations, turning bitter envy and discontent into sharp strategic ambition and discernment.",
            powerBonus = "+20% Shadow Transmutation • Extracts wisdom from bitter stings",
            dailyDecree = "Transmute the poison of envy into fuel for your own ascension; seek supreme mastery, not rivalry.",
            targetCategories = listOf("Envy & Spite"),
            defaultHumanityShift = -2,
            defaultStabilityShift = -1
        ),
        FantasyArchetypeTemplate(
            id = "sorrow_tempest",
            name = "Weeping Specter",
            title = "Lord of the Cathartic Tempest",
            valence = "NEGATIVE",
            element = "Cathartic Tempest",
            runeIcon = "🌧️",
            auraColorHex = 0xFF60A5FA,
            description = "Summons weeping storms of profound catharsis, cleansing psychic wounds and spiritual trauma with sacred rain.",
            powerBonus = "+25% Cathartic Wave • Dissolves long-standing emotional stagnation",
            dailyDecree = "Honor your tears as sacred rain; from the depths of sorrow, the deepest wisdom blossoms.",
            targetCategories = listOf("Sorrow & Ache"),
            defaultHumanityShift = 2,
            defaultStabilityShift = -2
        ),
        FantasyArchetypeTemplate(
            id = "shadow_penitent",
            name = "Shadow Penitent",
            title = "Keeper of the Iron Shackles",
            valence = "NEGATIVE",
            element = "Penitential Obsidian",
            runeIcon = "⛓️",
            auraColorHex = 0xFF9333EA,
            description = "Carries the heavy crucible of shame and guilt, forging humble contrition into an unyielding moral compass.",
            powerBonus = "+20% Humility Ward • Immune to arrogance traps",
            dailyDecree = "The heaviest chain will become your strongest armor when tempered by honest self-forgiveness.",
            targetCategories = listOf("Shame & Guilt"),
            defaultHumanityShift = 1,
            defaultStabilityShift = -2
        ),

        // Equilibrium / Duality Archetypes
        FantasyArchetypeTemplate(
            id = "yin_yang_arbiter",
            name = "Yin-Yang Arbiter",
            title = "Grand Sovereign of Primordial Equilibrium",
            valence = "EQUILIBRIUM",
            element = "Primordial Dual-Force",
            runeIcon = "☯️",
            auraColorHex = 0xFF8B5CF6,
            description = "Master of dual polarities, weaving light and abyssal shadow in perfect transcendent balance.",
            powerBonus = "+50% Dual Resonance • Simultaneously commands Light and Netherflame",
            dailyDecree = "You stand at the sacred nexus where light and darkness meet; within your stillness, all worlds harmonize.",
            targetCategories = listOf("Peace & Stillness", "Transcendence", "Anger & Fury", "Joy & Radiance"),
            defaultHumanityShift = 3,
            defaultStabilityShift = 4
        ),
        FantasyArchetypeTemplate(
            id = "ethereal_chimera",
            name = "Ethereal Ascendant",
            title = "Transmuted Weaver of the Multiverse",
            valence = "EQUILIBRIUM",
            element = "Cosmic Quintessence",
            runeIcon = "✨",
            auraColorHex = 0xFFEC4899,
            description = "Transcends mortal emotional polarities, synthesizing multifaceted soul experiences into pure cosmic consciousness.",
            powerBonus = "+30% Adaptive Morph • Resonates with all dimensional frequencies",
            dailyDecree = "Embrace every current of your being; every feeling is a sacred note in the cosmic symphony.",
            targetCategories = listOf(),
            defaultHumanityShift = 3,
            defaultStabilityShift = 3
        )
    )

    /**
     * Evaluates a collection of selected emotions (names or IDs) and generates the daily Fantasy Archetype.
     */
    fun evaluateDailyArchetype(
        selectedPositive: Collection<String>,
        selectedNegative: Collection<String>,
        userNote: String = ""
    ): DailyFantasyArchetypeResult {
        val positiveItems = selectedPositive.mapNotNull { EmotionCatalog.getEmotionByName(it) ?: EmotionCatalog.getEmotionById(it) }
        val negativeItems = selectedNegative.mapNotNull { EmotionCatalog.getEmotionByName(it) ?: EmotionCatalog.getEmotionById(it) }

        val posCount = positiveItems.size
        val negCount = negativeItems.size
        val totalCount = posCount + negCount

        if (totalCount == 0) {
            // Default baseline archetype
            val defaultTemplate = ARCHETYPE_TEMPLATES.first { it.id == "harmonic_ascetic" }
            return DailyFantasyArchetypeResult(
                archetypeId = defaultTemplate.id,
                name = defaultTemplate.name,
                title = defaultTemplate.title,
                dominanceValence = "EQUILIBRIUM",
                element = defaultTemplate.element,
                runeIcon = defaultTemplate.runeIcon,
                auraColorHex = defaultTemplate.auraColorHex,
                description = defaultTemplate.description,
                powerBonus = defaultTemplate.powerBonus,
                dailyDecree = defaultTemplate.dailyDecree,
                positivityRatio = 0.5f,
                positiveCount = 0,
                negativeCount = 0,
                dominantCategory = "Peace & Stillness",
                humanityDelta = 1,
                stabilityDelta = 1,
                suggestedAffirmation = "I am centered, mindful, and open to the cosmic journey."
            )
        }

        val positivityRatio = posCount.toFloat() / totalCount.toFloat()

        // Determine Dominant Valence
        val dominanceValence = when {
            posCount > negCount + 1 -> "POSITIVE"
            negCount > posCount + 1 -> "NEGATIVE"
            else -> "EQUILIBRIUM"
        }

        // Aggregate category occurrences
        val allSelected = positiveItems + negativeItems
        val categoryCounts = allSelected.groupingBy { it.category }.eachCount()
        val dominantCategory = categoryCounts.maxByOrNull { it.value }?.key ?: "Transcendence"

        // Calculate dynamic humanity and stability deltas
        val humanityDelta = allSelected.sumOf { it.humanityShift }
        val stabilityDelta = allSelected.sumOf { it.stabilityShift }

        // Select the best matching archetype template
        val candidateTemplates = ARCHETYPE_TEMPLATES.filter { it.valence == dominanceValence }
        val matchingTemplate = candidateTemplates.find { template ->
            template.targetCategories.contains(dominantCategory)
        } ?: candidateTemplates.firstOrNull() ?: ARCHETYPE_TEMPLATES.first()

        // Generate tailored dynamic affirmation
        val affirmation = when (dominanceValence) {
            "POSITIVE" -> "My harmonic energy radiates through every trial, uplifting all who cross my path."
            "NEGATIVE" -> "I acknowledge my deep shadows, transmuting their raw fire into iron resilience."
            else -> "I hold the balance between light and shadow, walking with supreme serenity."
        }

        return DailyFantasyArchetypeResult(
            archetypeId = matchingTemplate.id,
            name = matchingTemplate.name,
            title = matchingTemplate.title,
            dominanceValence = dominanceValence,
            element = matchingTemplate.element,
            runeIcon = matchingTemplate.runeIcon,
            auraColorHex = matchingTemplate.auraColorHex,
            description = matchingTemplate.description,
            powerBonus = matchingTemplate.powerBonus,
            dailyDecree = matchingTemplate.dailyDecree,
            positivityRatio = positivityRatio,
            positiveCount = posCount,
            negativeCount = negCount,
            dominantCategory = dominantCategory,
            humanityDelta = humanityDelta,
            stabilityDelta = stabilityDelta,
            suggestedAffirmation = affirmation
        )
    }
}
