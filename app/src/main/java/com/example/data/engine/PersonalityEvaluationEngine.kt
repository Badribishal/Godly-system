package com.example.data.engine

import com.example.data.model.EvaluationResult
import com.example.data.model.RecordInput
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object PersonalityEvaluationEngine {

    /**
     * Evaluates a user's record: Emotion → Intention → Action → Consequence → Pattern.
     * Calculates constructive vs destructive channeling of Shadows and Virtues,
     * updates Humanity, Stability, determines new titles, triggers evolutions and system omens.
     */
    fun evaluateRecord(
        input: RecordInput,
        current: SoulIdentity,
        recordCount: Int
    ): Pair<SoulIdentity, EvaluationResult> {
        val shadowDeltas = mutableMapOf<ShadowType, Int>()
        val virtueDeltas = mutableMapOf<VirtueType, Int>()

        ShadowType.values().forEach { shadowDeltas[it] = 0 }
        VirtueType.values().forEach { virtueDeltas[it] = 0 }

        var humanityDelta = 0
        var stabilityDelta = 0
        val textCorpus = "${input.emotion} ${input.situation} ${input.intention} ${input.action} ${input.consequence} ${input.reflection}".lowercase()

        // 1. Shadow & Virtue keyword analysis
        // Pride
        if (containsAny(textCorpus, "pride", "proud", "superior", "confidence", "dignity", "ego", "lead", "command", "sovereign", "dominate", "refuse defeat")) {
            if (containsAny(textCorpus, "protect", "inspire", "uplift", "mastery", "standards", "refused to give up", "stood tall")) {
                // Constructive Pride -> Leadership / Sovereign
                shadowDeltas[ShadowType.PRIDE] = shadowDeltas[ShadowType.PRIDE]!! + 6
                virtueDeltas[VirtueType.COURAGE] = virtueDeltas[VirtueType.COURAGE]!! + 4
                stabilityDelta += 2
            } else if (containsAny(textCorpus, "humiliate", "mock", "belittle", "superior", "arrogant", "looked down")) {
                // Destructive Pride -> Tyrant / Hubris
                shadowDeltas[ShadowType.PRIDE] = shadowDeltas[ShadowType.PRIDE]!! + 8
                humanityDelta -= 4
                stabilityDelta -= 3
            } else {
                shadowDeltas[ShadowType.PRIDE] = shadowDeltas[ShadowType.PRIDE]!! + 4
            }
        }

        // Greed
        if (containsAny(textCorpus, "greed", "want more", "acquire", "hoard", "money", "power", "ambition", "wealth", "accumulate", "resources", "monopolize")) {
            if (containsAny(textCorpus, "invest", "build", "provide", "strategy", "prepared", "abundance", "future")) {
                // Constructive Greed -> Strategic Architect / Abundance
                shadowDeltas[ShadowType.GREED] = shadowDeltas[ShadowType.GREED]!! + 5
                virtueDeltas[VirtueType.DILIGENCE] = virtueDeltas[VirtueType.DILIGENCE]!! + 4
            } else {
                shadowDeltas[ShadowType.GREED] = shadowDeltas[ShadowType.GREED]!! + 6
                humanityDelta -= 2
            }
        }

        // Envy
        if (containsAny(textCorpus, "envy", "jealous", "rival", "compared", "compete", "outdo", "behind", "surpassed", "inferior")) {
            if (containsAny(textCorpus, "practiced", "worked harder", "learned from", "motivated", "trained", "sharpened", "stepped up", "improved")) {
                // Constructive Envy -> Rival / Challenger / Evolution Catalyst
                shadowDeltas[ShadowType.ENVY] = shadowDeltas[ShadowType.ENVY]!! + 5
                virtueDeltas[VirtueType.DILIGENCE] = virtueDeltas[VirtueType.DILIGENCE]!! + 6
                virtueDeltas[VirtueType.COURAGE] = virtueDeltas[VirtueType.COURAGE]!! + 3
                stabilityDelta += 1
            } else if (containsAny(textCorpus, "resent", "bitter", "hoped they fail", "gossiped", "sabotage")) {
                shadowDeltas[ShadowType.ENVY] = shadowDeltas[ShadowType.ENVY]!! + 8
                humanityDelta -= 5
                stabilityDelta -= 4
            } else {
                shadowDeltas[ShadowType.ENVY] = shadowDeltas[ShadowType.ENVY]!! + 4
            }
        }

        // Wrath
        if (containsAny(textCorpus, "wrath", "anger", "furious", "rage", "injustice", "screamed", "fight", "retaliate", "defend", "boundary")) {
            if (containsAny(textCorpus, "protect", "shield", "defended someone", "stood up for", "stopped injustice", "guarded")) {
                // Constructive Wrath -> Guardian / Berserker of Justice
                shadowDeltas[ShadowType.WRATH] = shadowDeltas[ShadowType.WRATH]!! + 5
                virtueDeltas[VirtueType.COURAGE] = virtueDeltas[VirtueType.COURAGE]!! + 6
                humanityDelta += 3
            } else if (containsAny(textCorpus, "lashed out", "broke", "punched", "cursed", "revenge", "blind fury", "destroyed")) {
                shadowDeltas[ShadowType.WRATH] = shadowDeltas[ShadowType.WRATH]!! + 8
                stabilityDelta -= 6
                humanityDelta -= 3
            } else {
                shadowDeltas[ShadowType.WRATH] = shadowDeltas[ShadowType.WRATH]!! + 4
            }
        }

        // Gluttony
        if (containsAny(textCorpus, "gluttony", "binge", "craving", "obsessed", "devoured", "consumed", "insatiable", "experience everything", "indulged")) {
            if (containsAny(textCorpus, "learned deeply", "absorbed knowledge", "cultural immersion", "wonder", "art")) {
                shadowDeltas[ShadowType.GLUTTONY] = shadowDeltas[ShadowType.GLUTTONY]!! + 4
                virtueDeltas[VirtueType.TEMPERANCE] = virtueDeltas[VirtueType.TEMPERANCE]!! + 2
            } else {
                shadowDeltas[ShadowType.GLUTTONY] = shadowDeltas[ShadowType.GLUTTONY]!! + 6
                stabilityDelta -= 3
            }
        }

        // Desire
        if (containsAny(textCorpus, "desire", "passion", "longing", "lust", "craved connection", "magnetic", "yearning", "captivated", "attraction")) {
            if (containsAny(textCorpus, "deep bond", "artistic expression", "devotion", "shared joy", "expressed beauty", "authentic")) {
                shadowDeltas[ShadowType.DESIRE] = shadowDeltas[ShadowType.DESIRE]!! + 5
                virtueDeltas[VirtueType.GRATITUDE] = virtueDeltas[VirtueType.GRATITUDE]!! + 4
                humanityDelta += 4
            } else {
                shadowDeltas[ShadowType.DESIRE] = shadowDeltas[ShadowType.DESIRE]!! + 5
            }
        }

        // Sloth
        if (containsAny(textCorpus, "sloth", "rest", "procrastinate", "tired", "lazy", "withdrew", "stillness", "ignored", "refused action", "escaped")) {
            if (containsAny(textCorpus, "recharged", "meditated", "avoided unnecessary battle", "restored peace", "stepped back", "recovered")) {
                // Constructive Sloth -> Ascetic / Sage / Contemplative Stillness
                shadowDeltas[ShadowType.SLOTH] = shadowDeltas[ShadowType.SLOTH]!! + 4
                virtueDeltas[VirtueType.PATIENCE] = virtueDeltas[VirtueType.PATIENCE]!! + 5
                stabilityDelta += 4
            } else if (containsAny(textCorpus, "paralyzed", "wasted day", "escaped duty", "avoided truth", "decayed")) {
                shadowDeltas[ShadowType.SLOTH] = shadowDeltas[ShadowType.SLOTH]!! + 7
                virtueDeltas[VirtueType.DILIGENCE] = virtueDeltas[VirtueType.DILIGENCE]!! - 3
                stabilityDelta -= 2
            } else {
                shadowDeltas[ShadowType.SLOTH] = shadowDeltas[ShadowType.SLOTH]!! + 3
            }
        }

        // Virtues evaluation
        // Humility
        if (containsAny(textCorpus, "humility", "humble", "apologized", "listened", "accepted fault", "admitted mistake", "surrendered ego", "student")) {
            virtueDeltas[VirtueType.HUMILITY] = virtueDeltas[VirtueType.HUMILITY]!! + 6
            if (containsAny(textCorpus, "self-hatred", "worthless", "small", "erased myself", "shame")) {
                // Excessive / Distorted Humility -> Self-erasure
                stabilityDelta -= 4
            } else {
                stabilityDelta += 3
                humanityDelta += 3
            }
        }

        // Charity
        if (containsAny(textCorpus, "charity", "helped", "gave", "supported", "shared", "volunteered", "comforted", "donated", "uplifted")) {
            virtueDeltas[VirtueType.CHARITY] = virtueDeltas[VirtueType.CHARITY]!! + 6
            humanityDelta += 5
            if (containsAny(textCorpus, "exhausted myself", "took advantage of me", "could not say no", "depleted")) {
                stabilityDelta -= 3
            }
        }

        // Courage
        if (containsAny(textCorpus, "courage", "brave", "faced fear", "confronted", "took risk", "spoke up", "unflinching", "dared", "persevered")) {
            virtueDeltas[VirtueType.COURAGE] = virtueDeltas[VirtueType.COURAGE]!! + 7
            stabilityDelta += 2
        }

        // Patience
        if (containsAny(textCorpus, "patience", "waited", "endured", "calm", "tolerated", "deep breath", "non-reactive", "long-term", "steely")) {
            virtueDeltas[VirtueType.PATIENCE] = virtueDeltas[VirtueType.PATIENCE]!! + 6
            stabilityDelta += 4
        }

        // Temperance
        if (containsAny(textCorpus, "temperance", "moderation", "restraint", "declined temptation", "balanced", "disciplined eating", "calm center", "fasted")) {
            virtueDeltas[VirtueType.TEMPERANCE] = virtueDeltas[VirtueType.TEMPERANCE]!! + 6
            stabilityDelta += 3
        }

        // Diligence
        if (containsAny(textCorpus, "diligence", "discipline", "worked", "studied", "built", "trained", "exercised", "consistent", "focused", "routine")) {
            virtueDeltas[VirtueType.DILIGENCE] = virtueDeltas[VirtueType.DILIGENCE]!! + 6
            if (containsAny(textCorpus, "burnout", "no sleep", "collapsed", "obsessive", "ignored health")) {
                // Diligence excess
                stabilityDelta -= 5
                humanityDelta -= 2
            } else {
                stabilityDelta += 3
            }
        }

        // Gratitude
        if (containsAny(textCorpus, "gratitude", "grateful", "thankful", "blessed", "appreciated", "cherished", "content", "grace")) {
            virtueDeltas[VirtueType.GRATITUDE] = virtueDeltas[VirtueType.GRATITUDE]!! + 6
            humanityDelta += 4
            stabilityDelta += 4
        }

        // Emotion Catalog parsing (21+ positive & 21+ negative emotions)
        com.example.data.model.EmotionCatalog.ALL_EMOTIONS.forEach { emotion ->
            if (textCorpus.contains(emotion.name.lowercase()) || textCorpus.contains(emotion.id.lowercase())) {
                emotion.associatedShadow?.let { s ->
                    shadowDeltas[s] = (shadowDeltas[s] ?: 0) + 3
                }
                emotion.associatedVirtue?.let { v ->
                    virtueDeltas[v] = (virtueDeltas[v] ?: 0) + 3
                }
                humanityDelta += emotion.humanityShift
                stabilityDelta += emotion.stabilityShift
            }
        }

        // Direct selections fallback
        input.primaryShadow?.let {
            shadowDeltas[it] = (shadowDeltas[it] ?: 0) + 4
        }
        input.primaryVirtue?.let {
            virtueDeltas[it] = (virtueDeltas[it] ?: 0) + 4
        }

        // Baseline progression
        val totalDelta = shadowDeltas.values.sum() + virtueDeltas.values.sum()
        if (totalDelta == 0) {
            // General reflection
            virtueDeltas[VirtueType.HUMILITY] = 2
            virtueDeltas[VirtueType.PATIENCE] = 2
            shadowDeltas[ShadowType.SLOTH] = 2
            stabilityDelta += 1
            humanityDelta += 1
        }

        // 2. Compute updated scores (clamped 0 - 100)
        val newShadowScores = current.shadowScores.mapValues { (k, v) ->
            min(100, max(5, v + (shadowDeltas[k] ?: 0)))
        }
        val newVirtueScores = current.virtueScores.mapValues { (k, v) ->
            min(100, max(5, v + (virtueDeltas[k] ?: 0)))
        }

        val newHumanity = min(100, max(0, current.humanity + humanityDelta))
        val newStability = min(100, max(10, current.stability + stabilityDelta))

        // Dominants
        val dominantShadow = newShadowScores.maxByOrNull { it.value }?.key ?: current.dominantShadow
        val dominantVirtue = newVirtueScores.maxByOrNull { it.value }?.key ?: current.dominantVirtue

        // 3. Dynamic Archetype, Race, Class and Evolution calculation
        val (newRace, newClass, newAdvancedClass, newArchetype, newElement, newAlignment, newPossibleEvolution, awakenedTrait, secretAwakened) = calculateIdentityMatrix(
            shadows = newShadowScores,
            virtues = newVirtueScores,
            humanity = newHumanity,
            stability = newStability,
            current = current,
            recordCount = recordCount,
            textCorpus = textCorpus
        )

        // Title generation
        val newTitle = determineTitle(
            race = newRace,
            className = newAdvancedClass ?: newClass,
            dominantShadow = dominantShadow,
            dominantVirtue = dominantVirtue,
            humanity = newHumanity,
            stability = newStability
        )

        // Strengths & Weaknesses
        val strengths = generateStrengths(dominantShadow, dominantVirtue, newClass, newRace)
        val weaknesses = generateWeaknesses(dominantShadow, dominantVirtue, newStability, newHumanity)

        // Evolution progress calculation
        var newEvolutionProgress = current.evolutionProgress + 12 + (totalDelta / 3)
        var evolutionTriggered = false
        var oldSummary: String? = null
        var newSummary: String? = null

        if (newEvolutionProgress >= 100 || newRace != current.race || (newAdvancedClass != null && newAdvancedClass != current.advancedClass) || secretAwakened) {
            evolutionTriggered = true
            oldSummary = "${current.race} • ${current.advancedClass ?: current.className} [${current.currentTitle}]"
            newSummary = "$newRace • ${newAdvancedClass ?: newClass} [$newTitle]"
            newEvolutionProgress = if (newEvolutionProgress >= 100) (newEvolutionProgress % 100) else 45
        }

        // System Omen / Message
        val omen = generateSystemOmen(
            evolutionTriggered = evolutionTriggered,
            secretAwakened = secretAwakened,
            stability = newStability,
            humanity = newHumanity,
            dominantShadow = dominantShadow,
            dominantVirtue = dominantVirtue
        )

        val analysisInsight = generateAnalysisInsight(
            input = input,
            dominantShadow = dominantShadow,
            dominantVirtue = dominantVirtue,
            humanityDelta = humanityDelta,
            stabilityDelta = stabilityDelta
        )

        val updatedIdentity = current.copy(
            race = newRace,
            className = newClass,
            advancedClass = newAdvancedClass,
            archetype = newArchetype,
            element = newElement,
            alignment = newAlignment,
            currentTitle = newTitle,
            dominantShadow = dominantShadow,
            dominantVirtue = dominantVirtue,
            shadowScores = newShadowScores,
            virtueScores = newVirtueScores,
            humanity = newHumanity,
            stability = newStability,
            evolutionProgress = min(99, newEvolutionProgress),
            possibleEvolution = newPossibleEvolution,
            strengths = strengths,
            weaknesses = weaknesses,
            systemMessage = omen,
            evolutionHistoryCount = current.evolutionHistoryCount + if (evolutionTriggered) 1 else 0
        )

        val result = EvaluationResult(
            shadowDeltas = shadowDeltas,
            virtueDeltas = virtueDeltas,
            humanityDelta = humanityDelta,
            stabilityDelta = stabilityDelta,
            evolutionProgressDelta = 12 + (totalDelta / 3),
            analysisInsight = analysisInsight,
            awakenedTrait = awakenedTrait,
            newTitle = if (newTitle != current.currentTitle) newTitle else null,
            evolutionTriggered = evolutionTriggered,
            oldIdentitySummary = oldSummary,
            newIdentitySummary = newSummary,
            systemOmen = omen
        )

        return Pair(updatedIdentity, result)
    }

    internal data class IdentityMatrixResult(
        val race: String,
        val className: String,
        val advancedClass: String?,
        val archetype: String,
        val element: String,
        val alignment: String,
        val possibleEvolution: String,
        val awakenedTrait: String?,
        val secretAwakened: Boolean
    )

    internal fun calculateIdentityMatrix(
        shadows: Map<ShadowType, Int>,
        virtues: Map<VirtueType, Int>,
        humanity: Int,
        stability: Int,
        current: SoulIdentity,
        recordCount: Int,
        textCorpus: String
    ): IdentityMatrixResult {
        val pride = shadows[ShadowType.PRIDE] ?: 30
        val greed = shadows[ShadowType.GREED] ?: 30
        val envy = shadows[ShadowType.ENVY] ?: 30
        val wrath = shadows[ShadowType.WRATH] ?: 30
        val gluttony = shadows[ShadowType.GLUTTONY] ?: 30
        val desire = shadows[ShadowType.DESIRE] ?: 30
        val sloth = shadows[ShadowType.SLOTH] ?: 30

        val humility = virtues[VirtueType.HUMILITY] ?: 30
        val charity = virtues[VirtueType.CHARITY] ?: 30
        val courage = virtues[VirtueType.COURAGE] ?: 30
        val patience = virtues[VirtueType.PATIENCE] ?: 30
        val temperance = virtues[VirtueType.TEMPERANCE] ?: 30
        val diligence = virtues[VirtueType.DILIGENCE] ?: 30
        val gratitude = virtues[VirtueType.GRATITUDE] ?: 30

        // Check for SECRET / MYTHIC Anomalies first
        // 1. The Paradox: Extreme balance or equal high forces of light and dark
        val avgShadow = shadows.values.average()
        val avgVirtue = virtues.values.average()
        if (abs(avgShadow - avgVirtue) < 3.0 && avgShadow > 55) {
            return IdentityMatrixResult(
                race = "Astral Being",
                className = "Chronomancer",
                advancedClass = "Nexus Sovereign (The Paradox)",
                archetype = "The Inscrutable Equinox",
                element = "Aether / Twilight Singularity",
                alignment = "Ethereal Balanced",
                possibleEvolution = "The Eternal",
                awakenedTrait = "Paradoxical Singularity: Light and Void operate in flawless resonance.",
                secretAwakened = true
            )
        }

        // 2. The Worldbreaker: High Wrath + High Courage + Low Humanity (<40)
        if (wrath >= 65 && courage >= 65 && humanity <= 40) {
            return IdentityMatrixResult(
                race = "Primordial Titan",
                className = "Berserker",
                advancedClass = "The Worldbreaker",
                archetype = "The Relentless Cataclysm",
                element = "Chaos / Infernal Magma",
                alignment = "Chaotic Cataclysm",
                possibleEvolution = "Primordial Calamity",
                awakenedTrait = "Cataclysmic Aegis: Pain is directly converted into devastating kinetic momentum.",
                secretAwakened = true
            )
        }

        // 3. Vampire Progenitor / Blood Magus: High Pride + High Greed/Desire + Low Humanity (<38)
        if (pride >= 58 && (greed >= 55 || desire >= 55) && humanity < 38) {
            return IdentityMatrixResult(
                race = "Vampire Progenitor",
                className = "Blood Magus",
                advancedClass = "Crimson Sovereign",
                archetype = "The Nocturnal Aristocrat",
                element = "Blood / Sanguine Mists",
                alignment = "Lawful Malice",
                possibleEvolution = "Eclipse Patriarch",
                awakenedTrait = "Sanguine Domain: Feasts upon chaos to enforce sovereign psychic domain.",
                secretAwakened = true
            )
        }

        // 4. Eldritch Hybrid / Old God Scion: High Gluttony + High Envy + Low Stability (<45)
        if (gluttony >= 58 && envy >= 55 && stability < 45) {
            return IdentityMatrixResult(
                race = "Eldritch Hybrid",
                className = "Void Reaver",
                advancedClass = "Cosmic Harbinger",
                archetype = "The Outer Whisper",
                element = "Eldritch Warp / Dark Plasma",
                alignment = "Chaotic Inscrutable",
                possibleEvolution = "Old God Avatar",
                awakenedTrait = "Warp Resonance: Distorts conventional reality rules through taboo perception.",
                secretAwakened = true
            )
        }

        // 5. Seraph / Solar Angel: High Humility + High Charity + High Gratitude + High Humanity (>75)
        if (humility >= 58 && charity >= 58 && gratitude >= 55 && humanity >= 75) {
            val isSeraph = recordCount >= 4 && charity >= 70
            return IdentityMatrixResult(
                race = if (isSeraph) "Solar Seraph" else "Celestial Angel",
                className = "Templar of Dawn",
                advancedClass = if (isSeraph) "Divine Arbiter" else "Aegis of the Dawn",
                archetype = "The Luminous Harmonizer",
                element = "Radiant / Celestial Gold",
                alignment = "Radiant Crusader",
                possibleEvolution = "Seraph of the Infinite Dawn",
                awakenedTrait = "Aegis of Grace: Radiates absolute sanctuary and dispels mental despair.",
                secretAwakened = isSeraph
            )
        }

        // 6. Ancient Dragon / Astral Drake: High Pride + High Courage + High Diligence
        if (pride >= 58 && courage >= 58 && diligence >= 55) {
            val isAscendedDragon = recordCount >= 5 && pride >= 70
            return IdentityMatrixResult(
                race = if (isAscendedDragon) "Ancient Dragon" else "Dragonborn",
                className = "Sun Champion",
                advancedClass = if (isAscendedDragon) "Astral Wyrm Sovereign" else "Dragon Knight",
                archetype = "The Unyielding Sovereign",
                element = "Fire / Solar Crest",
                alignment = "Lawful Sovereign",
                possibleEvolution = if (isAscendedDragon) "Cosmic Dragon Sovereign" else "Ancient Dragon",
                awakenedTrait = "Dragonheart Sovereign: Imposes calm authority and unbending will over chaos.",
                secretAwakened = isAscendedDragon
            )
        }

        // 7. Abyssal Demon / Archdemon: High Desire + High Pride + Low Humanity (<42)
        if (desire >= 58 && pride >= 55 && humanity < 42) {
            val isArchdemon = pride > 70
            return IdentityMatrixResult(
                race = if (isArchdemon) "Archdemon Sovereign" else "Abyssal Demon",
                className = "Nethermancer",
                advancedClass = if (isArchdemon) "Lord of the Nine Hells" else "Eclipseblade",
                archetype = "The Ambitious Sovereign",
                element = "Shadow / Crimson Flame",
                alignment = "Chaotic Sovereign",
                possibleEvolution = "Abyssal Emperor",
                awakenedTrait = "Infernal Charisma: Commands magnetic allure and bends circumstances to desire.",
                secretAwakened = isArchdemon
            )
        }

        // 8. Dreamwalker Archfey: High Sloth + High Patience + High Charity + High Gratitude
        if (sloth >= 52 && patience >= 55 && charity >= 52 && gratitude >= 52) {
            return IdentityMatrixResult(
                race = "Archfey",
                className = "Twilight Mystic",
                advancedClass = "Dreamwalker",
                archetype = "The Ethereal Weaver",
                element = "Aether / Starlight Mist",
                alignment = "Chaotic Radiant",
                possibleEvolution = "Astral Weaver",
                awakenedTrait = "Oneiric Sanctuary: Transcends waking conflict through boundless empathy.",
                secretAwakened = true
            )
        }

        // 9. Soulkeeper Dark Elf: High Gluttony (knowledge/lore) + High Diligence + High Humility
        if (gluttony >= 52 && diligence >= 55 && humility >= 52) {
            return IdentityMatrixResult(
                race = "Dark Elf",
                className = "Scholar",
                advancedClass = "Soulkeeper",
                archetype = "The Infinite Archivist",
                element = "Shadow / Ethereal Memory",
                alignment = "Lawful Neutral",
                possibleEvolution = "The Forgotten Scribe",
                awakenedTrait = "Anima Siphon: Extracts wisdom and structure from all transient suffering.",
                secretAwakened = true
            )
        }

        // 10. Voidborn / Shadowblade: High Envy + High Temperance + Low Humanity (<50)
        if (envy >= 55 && temperance >= 52 && humanity < 50) {
            return IdentityMatrixResult(
                race = "Voidborn",
                className = "Shadowblade",
                advancedClass = "Shadow Sage",
                archetype = "The Maverick Catalyst",
                element = "Void / Cold Plasma",
                alignment = "Shadow Ascendant",
                possibleEvolution = "Void Sovereign",
                awakenedTrait = "Eclipse Focus: Renders comparison into hyper-focused esoteric innovation.",
                secretAwakened = true
            )
        }

        // 11. Celestial Kitsune: High Desire + High Diligence + High Charity
        if (desire >= 48 && diligence >= 48 && charity >= 48) {
            return IdentityMatrixResult(
                race = "Celestial Kitsune",
                className = "Arcane Weaver",
                advancedClass = "Starweaver",
                archetype = "The Mystical Trickster",
                element = "Foxfire / Radiant Illusion",
                alignment = "Chaotic Radiant",
                possibleEvolution = "Nine-Tailed Celestial",
                awakenedTrait = "Foxfire Allure: Weaves inspiration, charm, and strategic insight effortlessly.",
                secretAwakened = false
            )
        }

        // 12. Phoenix Sovereign: High Courage + High Gratitude + High Wrath
        if (courage >= 52 && gratitude >= 48 && wrath >= 48) {
            return IdentityMatrixResult(
                race = "Phoenix Sovereign",
                className = "Storm Herald",
                advancedClass = "Pyre Vanguard",
                archetype = "The Resilient Reborn",
                element = "Solar Fire / Tempest",
                alignment = "True Neutral",
                possibleEvolution = "Solar Sovereign",
                awakenedTrait = "Pyre Rebirth: Emerges from every emotional trial stronger and purer.",
                secretAwakened = false
            )
        }

        // 13. Starforged Automaton / Runesmith: High Greed + High Diligence + High Temperance
        if (greed >= 48 && diligence >= 52 && temperance >= 48) {
            return IdentityMatrixResult(
                race = "Starforged Golem",
                className = "Runesmith",
                advancedClass = "Adamantine Forge-Master",
                archetype = "The Relentless Builder",
                element = "Adamantine / Sacred Metallurgy",
                alignment = "Lawful Neutral",
                possibleEvolution = "Rune Sovereign",
                awakenedTrait = "Adamantine Foundation: Manifests tangible, unshakeable structures from abstract ideas.",
                secretAwakened = false
            )
        }

        // 14. High Elf / Moon Elf Druid: High Patience + High Temperance
        if (patience >= 48 && temperance >= 48) {
            val isArchdruid = humanity >= 70
            return IdentityMatrixResult(
                race = if (isArchdruid) "High Elf" else "Moon Elf",
                className = if (charity > diligence) "Verdant Archdruid" else "Moonblade Ranger",
                advancedClass = "Verdant Sentinel",
                archetype = "The Silent Watcher",
                element = "Flora / Ethereal Wind",
                alignment = "Neutral Good",
                possibleEvolution = "Ancient Forest Spirit",
                awakenedTrait = "Verdant Attunement: Senses micro-shifts in emotional atmospheres before they erupt.",
                secretAwakened = false
            )
        }

        // 15. Chimera Beastlord / Orc Berserker: High Wrath + High Courage
        if (wrath >= 48 && courage >= 48) {
            val isBeastLord = stability < 50
            return IdentityMatrixResult(
                race = if (isBeastLord) "Chimera Beastlord" else "Orc Champion",
                className = "Warrior",
                advancedClass = if (isBeastLord) "Beast Sovereign" else "Iron Vanguard",
                archetype = "The Frontline Bulwark",
                element = "Earth / Bloodstone",
                alignment = "Chaotic Neutral",
                possibleEvolution = "Titan Warlord",
                awakenedTrait = "Iron Will: Converts physical and mental pressure into unyielding endurance.",
                secretAwakened = false
            )
        }

        // 16. Frost Titan / Storm Giant: High Sloth + High Patience + High Stability
        if (sloth >= 42 && patience >= 48 && stability >= 68) {
            return IdentityMatrixResult(
                race = "Frost Titan",
                className = "Monk",
                advancedClass = "Glacial Bastion",
                archetype = "The Immovable Sentinel",
                element = "Glacial Ice / Deep Resonance",
                alignment = "True Neutral",
                possibleEvolution = "World Anchor",
                awakenedTrait = "Continental Calm: Completely unshakeable during interpersonal chaos.",
                secretAwakened = false
            )
        }

        // Fallback / Early Stage: Human
        val fallbackClass = when {
            diligence > 38 -> "Knight Exemplar"
            humility > 38 -> "Arcane Scholar"
            courage > 38 -> "Warrior Adept"
            desire > 38 -> "Shadowblade Rogue"
            gluttony > 38 -> "Alchemist"
            charity > 38 -> "Ascendant Monk"
            else -> "Wanderer of the Path"
        }

        return IdentityMatrixResult(
            race = "Human Adept",
            className = fallbackClass,
            advancedClass = if (recordCount >= 3) "Adept of the Seventh Seal" else null,
            archetype = "The Awakening Vessel",
            element = "Aether / Primordial",
            alignment = "True Neutral",
            possibleEvolution = "??? [Shadow & Virtue Vector Developing]",
            awakenedTrait = "Malleable Will: Free from rigid racial binding.",
            secretAwakened = false
        )
    }

    internal fun determineTitle(
        race: String,
        className: String,
        dominantShadow: ShadowType,
        dominantVirtue: VirtueType,
        humanity: Int,
        stability: Int
    ): String {
        return when {
            dominantShadow == ShadowType.PRIDE && dominantVirtue == VirtueType.COURAGE -> "The Sovereign Vanguard"
            dominantShadow == ShadowType.ENVY && dominantVirtue == VirtueType.DILIGENCE -> "The Unbent Challenger"
            dominantShadow == ShadowType.WRATH && dominantVirtue == VirtueType.CHARITY -> "The Crimson Shield"
            dominantShadow == ShadowType.SLOTH && dominantVirtue == VirtueType.PATIENCE -> "Warden of the Deep Quiet"
            dominantShadow == ShadowType.DESIRE && dominantVirtue == VirtueType.GRATITUDE -> "The Radiant Muse"
            dominantShadow == ShadowType.GREED && dominantVirtue == VirtueType.TEMPERANCE -> "The Vault Architect"
            dominantShadow == ShadowType.GLUTTONY && dominantVirtue == VirtueType.HUMILITY -> "The Endless Seeker"
            humanity < 30 -> "The Transcendent Outsider"
            humanity > 85 && stability > 75 -> "The Luminous Anchor"
            else -> "Bearer of the Obsidian Sigil"
        }
    }

    internal fun generateStrengths(
        shadow: ShadowType,
        virtue: VirtueType,
        className: String,
        race: String
    ): List<String> {
        val list = mutableListOf<String>()
        when (shadow) {
            ShadowType.PRIDE -> list.add("Unshakable Autonomy & Leadership Presence")
            ShadowType.ENVY -> list.add("Relentless Drive for Rapid Competence Upgrades")
            ShadowType.WRATH -> list.add("Decisive Boundary Defense & Righteous Fury")
            ShadowType.SLOTH -> list.add("Deep Systemic Energy Conservation & Stillness")
            ShadowType.GREED -> list.add("Long-Range Resource Strategy & Acquisition")
            ShadowType.DESIRE -> list.add("Magnetic Charisma & Aesthetic Resonance")
            ShadowType.GLUTTONY -> list.add("Voracious Knowledge & Experiential Absorption")
        }
        when (virtue) {
            VirtueType.COURAGE -> list.add("Fearless Leap into Ambiguous Horizons")
            VirtueType.DILIGENCE -> list.add("Unstoppable Momentum Through Focused Labor")
            VirtueType.HUMILITY -> list.add("Ego-Free Receptivity to Subtle Wisdom")
            VirtueType.CHARITY -> list.add("Luminous Vessel for Community Upliftment")
            VirtueType.PATIENCE -> list.add("Adamantine Composure During Prolonged Trials")
            VirtueType.TEMPERANCE -> list.add("Masterful Self-Regulation & Equilibrium")
            VirtueType.GRATITUDE -> list.add("Transmutative Grace Over Hard Circumstances")
        }
        list.add("Resonant Alignment with $race Archetype")
        return list
    }

    internal fun generateWeaknesses(
        shadow: ShadowType,
        virtue: VirtueType,
        stability: Int,
        humanity: Int
    ): List<String> {
        val list = mutableListOf<String>()
        if (stability < 50) {
            list.add("Turbulent Internal Resonance (Vulnerable to sudden spikes)")
        }
        if (humanity < 40) {
            list.add("Detached from Mortal Nuances & Conventional Empathy")
        } else if (humanity > 90) {
            list.add("Hyper-Permeable Empathic Boundary (Risk of absorbing external gloom)")
        }

        when (shadow) {
            ShadowType.PRIDE -> list.add("Hesitation to Ask for Assistance in Dire Straits")
            ShadowType.ENVY -> list.add("Hyper-Fixation on External Benchmarks")
            ShadowType.WRATH -> list.add("Risk of Collateral Heat in Defensive Reactions")
            ShadowType.SLOTH -> list.add("Inertia When Fleeting Tactical Windows Open")
            ShadowType.GREED -> list.add("Reluctance to Release Sub-Optimal Assets")
            ShadowType.DESIRE -> list.add("Vulnerability to Seductive Distractions")
            ShadowType.GLUTTONY -> list.add("Danger of Cognitive Overload & Sensory Burnout")
        }
        return list
    }

    private fun generateSystemOmen(
        evolutionTriggered: Boolean,
        secretAwakened: Boolean,
        stability: Int,
        humanity: Int,
        dominantShadow: ShadowType,
        dominantVirtue: VirtueType
    ): String {
        return when {
            secretAwakened -> "An unknown archetype has appeared in the soul stream."
            evolutionTriggered -> "Your behavior has forged an unexpected metamorphic catalyst."
            stability < 40 -> "Classification unstable. Opposing spiritual currents clash within the vessel."
            humanity < 35 -> "Mortal tether fading. Celestial / Abyssal resonance is ascending."
            dominantShadow == ShadowType.PRIDE && dominantVirtue == VirtueType.HUMILITY -> "The Paradoxical Crown: Pride and Humility intertwine in balance."
            dominantShadow == ShadowType.ENVY -> "The Rival's Flame burns bright. The system detects accelerated growth."
            dominantShadow == ShadowType.WRATH -> "Defensive martial ether detected. The Guardian spark intensifies."
            else -> "The System continues its silent observation. The chrysalis stirs."
        }
    }

    private fun generateAnalysisInsight(
        input: RecordInput,
        dominantShadow: ShadowType,
        dominantVirtue: VirtueType,
        humanityDelta: Int,
        stabilityDelta: Int
    ): String {
        val tone = if (stabilityDelta >= 0) "Constructive Harmony" else "Tension Observed"
        return "System Evaluation [$tone]: Emotion (${input.emotion}) was channeled into action. " +
                "The resonance of ${dominantShadow.displayName} (${dominantShadow.title}) and ${dominantVirtue.displayName} (${dominantVirtue.title}) shifted your spiritual matrix. " +
                if (humanityDelta >= 0) "Mortal empathy deepened." else "Spiritual detachment broadened."
    }

    private fun containsAny(text: String, vararg keywords: String): Boolean {
        return keywords.any { text.contains(it) }
    }
}

typealias PersonalityEngine = PersonalityEvaluationEngine

