package com.example.data.repository

import com.example.data.engine.PersonalityEvaluationEngine
import com.example.data.engine.SoulResonanceData
import com.example.data.export.SoulPdfExporter
import com.example.data.local.DailyTrialEntity
import com.example.data.local.EvaluationDraftEntity
import com.example.data.local.EvaluationRecordEntity
import com.example.data.local.EvolutionEventEntity
import com.example.data.local.SoulDao
import com.example.data.local.SoulProfileEntity
import com.example.data.model.EvaluationResult
import com.example.data.model.RecordInput
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream

class SoulRepository(private val soulDao: SoulDao) {

    val soulProfileFlow: Flow<SoulIdentity> = soulDao.getSoulProfileFlow()
        .map { entity ->
            entity?.toSoulIdentity() ?: SoulIdentity.initial()
        }
        .flowOn(Dispatchers.IO)

    val allRecordsFlow: Flow<List<EvaluationRecordEntity>> = soulDao.getAllRecordsFlow()
        .flowOn(Dispatchers.IO)

    val allEventsFlow: Flow<List<EvolutionEventEntity>> = soulDao.getAllEvolutionEventsFlow()
        .flowOn(Dispatchers.IO)

    val allTrialsFlow: Flow<List<DailyTrialEntity>> = soulDao.getAllTrialsFlow()
        .flowOn(Dispatchers.IO)

    val evaluationDraftFlow: Flow<EvaluationDraftEntity?> = soulDao.getEvaluationDraftFlow()
        .flowOn(Dispatchers.IO)

    suspend fun getEvaluationDraft(): EvaluationDraftEntity? = withContext(Dispatchers.IO) {
        soulDao.getEvaluationDraft()
    }

    suspend fun saveEvaluationDraft(draft: EvaluationDraftEntity) = withContext(Dispatchers.IO) {
        soulDao.saveEvaluationDraft(draft)
    }

    suspend fun clearEvaluationDraft() = withContext(Dispatchers.IO) {
        soulDao.clearEvaluationDraft()
    }

    suspend fun initializeIfEmpty() = withContext(Dispatchers.IO) {
        val existing = soulDao.getSoulProfile()
        if (existing == null) {
            val initial = SoulIdentity.initial()
            soulDao.insertOrUpdateProfile(initial.toEntity())

            // Initial Genesis Event
            soulDao.insertEvolutionEvent(
                EvolutionEventEntity(
                    dayNumber = 1,
                    eventType = "GENESIS",
                    title = "The System Awakens",
                    description = "Soul vessel initialized as Human Seeker. The seven shadows and seven virtues are uncalibrated.",
                    runeIcon = "🌌"
                )
            )

            // Seed Initial Fantasy Scenarios / Daily Trials
            val defaultTrials = listOf(
                DailyTrialEntity(
                    title = "The Echo of Defeat",
                    category = "Trial of the Mind",
                    scenario = "You observe an adversary whom you previously competed with celebrating a monumental victory. A bitter spike of Envy flares in your chest.",
                    optionsJson = JSONArray(
                        listOf(
                            JSONObject().put("label", "Dissect their victory objectively and integrate their technique into your regimen.")
                                .put("force", "ENVY_CONSTRUCTIVE").toString(),
                            JSONObject().put("label", "Extend genuine congratulations and celebrate their milestone.")
                                .put("force", "CHARITY").toString(),
                            JSONObject().put("label", "Withdraw in silent resolve, focusing purely on your own path.")
                                .put("force", "PATIENCE").toString()
                        )
                    ).toString()
                ),
                DailyTrialEntity(
                    title = "The Unclaimed Crown",
                    category = "The Paradox",
                    scenario = "An opportunity to take supreme command of a floundering endeavor appears. If you step up, your pride will be tested; if you step back, the endeavor may collapse.",
                    optionsJson = JSONArray(
                        listOf(
                            JSONObject().put("label", "Take the mantle with absolute authority and shield the participants from collapse.")
                                .put("force", "PRIDE_SOVEREIGN").toString(),
                            JSONObject().put("label", "Empower the group to elect a decentralized council rather than ruling alone.")
                                .put("force", "HUMILITY").toString(),
                            JSONObject().put("label", "Observe the collapse from afar to let natural consequences teach the lesson.")
                                .put("force", "SLOTH_ASCETIC").toString()
                        )
                    ).toString()
                ),
                DailyTrialEntity(
                    title = "The Shield of Righteous Fury",
                    category = "Trial of Iron",
                    scenario = "A vulnerable ally is subjected to systemic malice by a dominant force. Wrath surges in your veins.",
                    optionsJson = JSONArray(
                        listOf(
                            JSONObject().put("label", "Channel the rage into an immovable defensive barrier and confront the aggressor.")
                                .put("force", "WRATH_GUARDIAN").toString(),
                            JSONObject().put("label", "Formulate a meticulous long-term strategy to neutralize the aggressor peacefully.")
                                .put("force", "TEMPERANCE").toString(),
                            JSONObject().put("label", "Provide solace and sanctuary to the ally while absorbing the blow yourself.")
                                .put("force", "CHARITY").toString()
                        )
                    ).toString()
                ),
                DailyTrialEntity(
                    title = "The Midnight Labyrinth",
                    category = "Echo of the Void",
                    scenario = "Exhaustion tempts you to abandon an uncompleted promise and succumb to sleep. Sloth whispers of comfort.",
                    optionsJson = JSONArray(
                        listOf(
                            JSONObject().put("label", "Acknowledge the physical limit, rest mindfully, and rise with renewed vigor before dawn.")
                                .put("force", "TEMPERANCE_PATIENCE").toString(),
                            JSONObject().put("label", "Forge through the exhaustion with iron diligence to complete the covenant.")
                                .put("force", "DILIGENCE_IRON").toString(),
                            JSONObject().put("label", "Surrender entirely to stillness and let go of the attachment to perfection.")
                                .put("force", "SLOTH_SURRENDER").toString()
                        )
                    ).toString()
                )
            )
            soulDao.insertTrials(defaultTrials)
        }
    }

    suspend fun recordEvaluation(input: RecordInput): EvaluationResult = withContext(Dispatchers.IO) {
        val currentEntity = soulDao.getSoulProfile() ?: SoulIdentity.initial().toEntity()
        val currentIdentity = currentEntity.toSoulIdentity()
        val count = soulDao.getRecordCount()

        val (updatedIdentityTemp, result) = PersonalityEvaluationEngine.evaluateRecord(
            input = input,
            current = currentIdentity,
            recordCount = count + 1
        )

        // Calculate Soul EXP earned: base 85 + depth bonus up to 65
        val depthBonus = kotlin.math.min(65, (input.situation.length + input.reflection.length) / 4)
        val earnedExp = 85 + depthBonus

        val (progressionTriple, levelOutcome) = com.example.data.engine.SoulProgressionEngine.applyExpGain(
            currentLevel = updatedIdentityTemp.soulLevel,
            currentExp = updatedIdentityTemp.soulExp,
            totalExp = updatedIdentityTemp.totalSoulExp,
            gainedExp = earnedExp,
            alreadyUnlockedArchetypeIds = updatedIdentityTemp.unlockedArchetypeIds
        )
        val (newLevel, newExp, newTotalExp) = progressionTriple
        val newlyUnlockedIds = levelOutcome?.newlyUnlockedArchetypes?.map { it.id }?.toSet() ?: emptySet()

        // Award 15 Soul Shards for consistent daily recording + bonus level shards
        val updatedIdentity = updatedIdentityTemp.copy(
            soulShards = updatedIdentityTemp.soulShards + 15 + (levelOutcome?.shardsReward ?: 0),
            soulLevel = newLevel,
            soulExp = newExp,
            totalSoulExp = newTotalExp,
            unlockedArchetypeIds = updatedIdentityTemp.unlockedArchetypeIds + newlyUnlockedIds
        )

        // Save Record
        val recordEntity = EvaluationRecordEntity(
            emotion = input.emotion,
            primaryShadow = input.primaryShadow?.name,
            primaryVirtue = input.primaryVirtue?.name,
            situation = input.situation,
            intention = input.intention,
            action = input.action,
            consequence = input.consequence,
            reflection = input.reflection,
            analysisInsight = result.analysisInsight,
            forcesResonatedJson = JSONObject().apply {
                result.shadowDeltas.forEach { (k, v) -> if (v != 0) put(k.name, v) }
                result.virtueDeltas.forEach { (k, v) -> if (v != 0) put(k.name, v) }
            }.toString(),
            humanityShift = result.humanityDelta,
            stabilityShift = result.stabilityDelta
        )
        soulDao.insertRecord(recordEntity)

        // Update Profile
        soulDao.insertOrUpdateProfile(updatedIdentity.toEntity())

        // Insert Level Up or Tier Ascension Event if triggered
        if (levelOutcome != null) {
            val dayNum = count + 2
            soulDao.insertEvolutionEvent(
                EvolutionEventEntity(
                    dayNumber = dayNum,
                    eventType = if (levelOutcome.tierPromoted) "TIER_ASCENSION" else "SOUL_LEVEL_UP",
                    title = if (levelOutcome.tierPromoted) "Ascended to Tier ${levelOutcome.newTier.romanNumeral}: ${levelOutcome.newTier.title}" else "Soul Matrix Level ${levelOutcome.newLevel}",
                    description = "Vessel reached Level ${levelOutcome.newLevel} (+${levelOutcome.levelsGained} levels). Awarded 💎 ${levelOutcome.shardsReward} Shards." +
                            if (levelOutcome.newlyUnlockedArchetypes.isNotEmpty()) " Unlocked Archetypes: " + levelOutcome.newlyUnlockedArchetypes.joinToString { it.name } else "",
                    runeIcon = levelOutcome.newTier.rune
                )
            )
        }

        // Insert Evolution Event if triggered
        val dayNum = count + 2
        if (result.evolutionTriggered) {
            soulDao.insertEvolutionEvent(
                EvolutionEventEntity(
                    dayNumber = dayNum,
                    eventType = if (result.awakenedTrait != null) "AWAKENING" else "METAMORPHOSIS",
                    title = result.newTitle ?: "Identity Metamorphosis",
                    description = "${result.oldIdentitySummary ?: "Vessel"} evolved into ${result.newIdentitySummary ?: updatedIdentity.race}. ${result.awakenedTrait ?: ""}",
                    isUnknownEvent = result.awakenedTrait?.contains("Singularity") == true || updatedIdentity.race == "Astral Being" || updatedIdentity.race == "Voidborn",
                    runeIcon = when (updatedIdentity.race) {
                        "Ancient Dragon", "Dragonborn" -> "🐉"
                        "Angel", "Solar Seraph", "Celestial Angel" -> "🪽"
                        "Demon", "Archdemon", "Abyssal Demon" -> "🔥"
                        "Voidborn", "Astral Being" -> "🌌"
                        "Phoenix", "Phoenix Sovereign" -> "🦅"
                        "Fae", "Archfey" -> "✨"
                        "Kitsune", "Celestial Kitsune" -> "🦊"
                        "Titan", "Giant", "Primordial Titan", "Frost Titan" -> "⚡"
                        "Vampire Progenitor" -> "🩸"
                        "High Elf", "Elf", "Dark Elf", "Moon Elf" -> "🌿"
                        else -> "⚔️"
                    }
                )
            )
        } else if (result.awakenedTrait != null) {
            soulDao.insertEvolutionEvent(
                EvolutionEventEntity(
                    dayNumber = dayNum,
                    eventType = "TRAIT_AWAKENED",
                    title = "Spiritual Resonance",
                    description = result.awakenedTrait,
                    runeIcon = "✨"
                )
            )
        }

        result
    }

    suspend fun completeTrial(trial: DailyTrialEntity, selectedOptionIndex: Int, userReflection: String) = withContext(Dispatchers.IO) {
        val updated = trial.copy(
            completedTimestamp = System.currentTimeMillis(),
            selectedOptionIndex = selectedOptionIndex,
            userReflection = userReflection
        )
        soulDao.updateTrial(updated)

        // Evaluate choice through engine and award 25 Soul Shards for trial completion
        val recordInput = RecordInput(
            emotion = "Trial Resolution",
            primaryShadow = null,
            primaryVirtue = null,
            situation = trial.scenario,
            intention = "Confronted the Divine Dilemma: ${trial.title}",
            action = "Chose Option: $selectedOptionIndex",
            consequence = "Soul resonated with scenario choices.",
            reflection = userReflection.ifEmpty { "Contemplated the systemic consequence." }
        )
        recordEvaluation(recordInput)

        // Add bonus trial completion shards (+10 bonus on top of standard record)
        val profile = soulDao.getSoulProfile()
        if (profile != null) {
            val soul = profile.toSoulIdentity()
            soulDao.insertOrUpdateProfile(soul.copy(soulShards = soul.soulShards + 10).toEntity())
        }
    }

    suspend fun archiveGodlyEvolution(customNote: String? = null): EvolutionEventEntity = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile()?.toSoulIdentity() ?: SoulIdentity.initial()
        val count = soulDao.getRecordCount()
        val dayNum = count + 1
        val event = EvolutionEventEntity(
            dayNumber = dayNum,
            eventType = "GODLY_ARCHIVE",
            title = "Godly Archive: ${profile.race} [${profile.currentTitle}]",
            description = if (!customNote.isNullOrBlank()) {
                customNote
            } else {
                "Preserved state of ${profile.race} ${profile.className} (${profile.alignment}). Dominant Forces: ${profile.dominantShadow.displayName} & ${profile.dominantVirtue.displayName}. Humanity: ${profile.humanity}%, Stability: ${profile.stability}%."
            },
            isUnknownEvent = false,
            runeIcon = when (profile.race) {
                "Ancient Dragon", "Dragonborn" -> "🐉"
                "Angel", "Solar Seraph", "Celestial Angel" -> "🪽"
                "Demon", "Archdemon", "Abyssal Demon" -> "🔥"
                "Voidborn", "Astral Being" -> "🌌"
                "Phoenix", "Phoenix Sovereign" -> "🦅"
                "Fae", "Archfey" -> "✨"
                "Kitsune", "Celestial Kitsune" -> "🦊"
                "Titan", "Giant", "Primordial Titan", "Frost Titan" -> "⚡"
                "Vampire Progenitor" -> "🩸"
                "High Elf", "Elf", "Dark Elf", "Moon Elf" -> "🌿"
                else -> "👑"
            }
        )
        val id = soulDao.insertEvolutionEvent(event)
        event.copy(id = id)
    }

    suspend fun recalculateIdentityDescription(): SoulIdentity = withContext(Dispatchers.IO) {
        val current = soulDao.getSoulProfile()?.toSoulIdentity() ?: SoulIdentity.initial()
        val allRecords = soulDao.getRecentRecords(10)
        val recordCount = soulDao.getRecordCount()
        val textCorpus = allRecords.joinToString(" ") { "${it.situation} ${it.reflection} ${it.emotion}" }

        val matrixResult = PersonalityEvaluationEngine.calculateIdentityMatrix(
            shadows = current.shadowScores,
            virtues = current.virtueScores,
            humanity = current.humanity,
            stability = current.stability,
            current = current,
            recordCount = recordCount,
            textCorpus = textCorpus
        )

        val newTitle = PersonalityEvaluationEngine.determineTitle(
            race = matrixResult.race,
            className = matrixResult.advancedClass ?: matrixResult.className,
            dominantShadow = current.dominantShadow,
            dominantVirtue = current.dominantVirtue,
            humanity = current.humanity,
            stability = current.stability
        )

        val strengths = PersonalityEvaluationEngine.generateStrengths(
            current.dominantShadow,
            current.dominantVirtue,
            matrixResult.className,
            matrixResult.race
        )
        val weaknesses = PersonalityEvaluationEngine.generateWeaknesses(
            current.dominantShadow,
            current.dominantVirtue,
            current.stability,
            current.humanity
        )

        val updated = current.copy(
            race = matrixResult.race,
            className = matrixResult.className,
            advancedClass = matrixResult.advancedClass,
            archetype = matrixResult.archetype,
            element = matrixResult.element,
            alignment = matrixResult.alignment,
            possibleEvolution = matrixResult.possibleEvolution,
            currentTitle = newTitle,
            strengths = strengths,
            weaknesses = weaknesses
        )
        soulDao.insertOrUpdateProfile(updated.toEntity())
        updated
    }

    suspend fun unlockCosmetic(effectId: String, cost: Int): Boolean = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: return@withContext false
        val soul = profile.toSoulIdentity()
        if (soul.soulShards >= cost && !soul.unlockedEffectIds.contains(effectId)) {
            val updated = soul.copy(
                soulShards = soul.soulShards - cost,
                unlockedEffectIds = soul.unlockedEffectIds + effectId,
                equippedEffectId = effectId
            )
            soulDao.insertOrUpdateProfile(updated.toEntity())
            true
        } else {
            false
        }
    }

    suspend fun equipCosmetic(effectId: String): Boolean = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: return@withContext false
        val soul = profile.toSoulIdentity()
        if (soul.unlockedEffectIds.contains(effectId) || effectId == "effect_default") {
            val updated = soul.copy(equippedEffectId = effectId)
            soulDao.insertOrUpdateProfile(updated.toEntity())
            true
        } else {
            false
        }
    }

    suspend fun addSoulShards(amount: Int): Boolean = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: return@withContext false
        val soul = profile.toSoulIdentity()
        val updated = soul.copy(soulShards = soul.soulShards + amount)
        soulDao.insertOrUpdateProfile(updated.toEntity())
        true
    }

    suspend fun addSoulExp(amount: Int): com.example.data.engine.LevelUpOutcome? = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: return@withContext null
        val soul = profile.toSoulIdentity()
        val (progressionTriple, outcome) = com.example.data.engine.SoulProgressionEngine.applyExpGain(
            currentLevel = soul.soulLevel,
            currentExp = soul.soulExp,
            totalExp = soul.totalSoulExp,
            gainedExp = amount,
            alreadyUnlockedArchetypeIds = soul.unlockedArchetypeIds
        )
        val (newLevel, newExp, newTotalExp) = progressionTriple
        val newlyUnlockedIds = outcome?.newlyUnlockedArchetypes?.map { it.id }?.toSet() ?: emptySet()
        val updatedUnlocked = soul.unlockedArchetypeIds + newlyUnlockedIds
        val updatedShards = soul.soulShards + (outcome?.shardsReward ?: 0)

        val updatedTitle = if (outcome != null) {
            com.example.data.engine.GodlyTitleEngine.computeGodlyTitle(
                soul.copy(
                    soulLevel = newLevel,
                    soulExp = newExp,
                    totalSoulExp = newTotalExp,
                    unlockedArchetypeIds = updatedUnlocked
                )
            )
        } else {
            soul.currentTitle
        }

        val updatedSoul = soul.copy(
            soulLevel = newLevel,
            soulExp = newExp,
            totalSoulExp = newTotalExp,
            soulShards = updatedShards,
            unlockedArchetypeIds = updatedUnlocked,
            currentTitle = updatedTitle
        )
        soulDao.insertOrUpdateProfile(updatedSoul.toEntity())

        if (outcome != null) {
            val count = soulDao.getRecordCount()
            soulDao.insertEvolutionEvent(
                EvolutionEventEntity(
                    dayNumber = count + 1,
                    eventType = if (outcome.tierPromoted) "TIER_ASCENSION" else "SOUL_LEVEL_UP",
                    title = if (outcome.tierPromoted) "Ascended to Tier ${outcome.newTier.romanNumeral}: ${outcome.newTier.title}" else "Soul Matrix Level ${outcome.newLevel}",
                    description = "Vessel reached Level ${outcome.newLevel} (+${outcome.levelsGained} levels). Bestowed Godly Title: [$updatedTitle]. Awarded 💎 ${outcome.shardsReward} Shards." +
                            if (outcome.newlyUnlockedArchetypes.isNotEmpty()) " Unlocked Archetypes: " + outcome.newlyUnlockedArchetypes.joinToString { it.name } else "",
                    runeIcon = outcome.newTier.rune
                )
            )
        }
        outcome
    }

    suspend fun attuneArchetype(archetypeId: String): Boolean = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: return@withContext false
        val soul = profile.toSoulIdentity()
        if (soul.unlockedArchetypeIds.contains(archetypeId) || archetypeId == "arch_seeker") {
            val archetypeDef = com.example.data.model.AdvancedArchetypesCatalog.getArchetypeById(archetypeId)
            val tempSoul = soul.copy(
                attunedArchetypeId = archetypeId,
                archetype = archetypeDef.name,
                element = archetypeDef.element,
                className = archetypeDef.characterClass,
                race = archetypeDef.celestialRace
            )
            val dynamicTitle = com.example.data.engine.GodlyTitleEngine.computeGodlyTitle(tempSoul)
            val updated = tempSoul.copy(currentTitle = dynamicTitle)
            soulDao.insertOrUpdateProfile(updated.toEntity())
            true
        } else {
            false
        }
    }

    suspend fun claimAchievement(achievementId: String): Pair<Int, com.example.data.engine.LevelUpOutcome?> = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: return@withContext Pair(0, null)
        val soul = profile.toSoulIdentity()
        val definition = com.example.data.model.AchievementCatalog.DEFINITIONS.find { it.id == achievementId } ?: return@withContext Pair(0, null)

        if (!soul.claimedAchievementIds.contains(achievementId)) {
            val reward = definition.rewardShards
            val gainedExp = 35

            val (progressionTriple, outcome) = com.example.data.engine.SoulProgressionEngine.applyExpGain(
                currentLevel = soul.soulLevel,
                currentExp = soul.soulExp,
                totalExp = soul.totalSoulExp,
                gainedExp = gainedExp,
                alreadyUnlockedArchetypeIds = soul.unlockedArchetypeIds
            )
            val (newLevel, newExp, newTotalExp) = progressionTriple
            val newlyUnlockedIds = outcome?.newlyUnlockedArchetypes?.map { it.id }?.toSet() ?: emptySet()

            val updated = soul.copy(
                soulShards = soul.soulShards + reward + (outcome?.shardsReward ?: 0),
                soulLevel = newLevel,
                soulExp = newExp,
                totalSoulExp = newTotalExp,
                unlockedArchetypeIds = soul.unlockedArchetypeIds + newlyUnlockedIds,
                claimedAchievementIds = soul.claimedAchievementIds + achievementId
            )
            soulDao.insertOrUpdateProfile(updated.toEntity())

            if (outcome != null) {
                val count = soulDao.getRecordCount()
                soulDao.insertEvolutionEvent(
                    EvolutionEventEntity(
                        dayNumber = count + 1,
                        eventType = if (outcome.tierPromoted) "TIER_ASCENSION" else "SOUL_LEVEL_UP",
                        title = if (outcome.tierPromoted) "Ascended to Tier ${outcome.newTier.romanNumeral}: ${outcome.newTier.title}" else "Soul Matrix Level ${outcome.newLevel}",
                        description = "Vessel reached Level ${outcome.newLevel}. Awarded 💎 ${outcome.shardsReward} Shards.",
                        runeIcon = outcome.newTier.rune
                    )
                )
            }
            Pair(reward, outcome)
        } else {
            Pair(0, null)
        }
    }

    suspend fun claimAllAchievements(): Pair<Int, com.example.data.engine.LevelUpOutcome?> = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: return@withContext Pair(0, null)
        val soul = profile.toSoulIdentity()
        val achievements = computeAchievements()
        val unclaimedUnlocked = achievements.filter { it.isUnlocked && !it.isClaimed }
        if (unclaimedUnlocked.isEmpty()) return@withContext Pair(0, null)

        val totalReward = unclaimedUnlocked.sumOf { it.rewardShards }
        val gainedExp = unclaimedUnlocked.size * 35

        val (progressionTriple, outcome) = com.example.data.engine.SoulProgressionEngine.applyExpGain(
            currentLevel = soul.soulLevel,
            currentExp = soul.soulExp,
            totalExp = soul.totalSoulExp,
            gainedExp = gainedExp,
            alreadyUnlockedArchetypeIds = soul.unlockedArchetypeIds
        )
        val (newLevel, newExp, newTotalExp) = progressionTriple
        val newlyUnlockedIds = outcome?.newlyUnlockedArchetypes?.map { it.id }?.toSet() ?: emptySet()

        val newClaimedIds = soul.claimedAchievementIds + unclaimedUnlocked.map { it.id }.toSet()
        val updated = soul.copy(
            soulShards = soul.soulShards + totalReward + (outcome?.shardsReward ?: 0),
            soulLevel = newLevel,
            soulExp = newExp,
            totalSoulExp = newTotalExp,
            unlockedArchetypeIds = soul.unlockedArchetypeIds + newlyUnlockedIds,
            claimedAchievementIds = newClaimedIds
        )
        soulDao.insertOrUpdateProfile(updated.toEntity())

        if (outcome != null) {
            val count = soulDao.getRecordCount()
            soulDao.insertEvolutionEvent(
                EvolutionEventEntity(
                    dayNumber = count + 1,
                    eventType = if (outcome.tierPromoted) "TIER_ASCENSION" else "SOUL_LEVEL_UP",
                    title = if (outcome.tierPromoted) "Ascended to Tier ${outcome.newTier.romanNumeral}: ${outcome.newTier.title}" else "Soul Matrix Level ${outcome.newLevel}",
                    description = "Vessel reached Level ${outcome.newLevel}. Awarded 💎 ${outcome.shardsReward} Shards.",
                    runeIcon = outcome.newTier.rune
                )
            )
        }
        Pair(totalReward, outcome)
    }

    suspend fun computeAchievements(): List<com.example.data.model.Achievement> = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile()?.toSoulIdentity() ?: SoulIdentity.initial()
        val recordCount = soulDao.getRecordCount()
        val allTrials = soulDao.getAllTrialsFlow()
        val completedTrialsCount = try {
            val trials = soulDao.getNextUncompletedTrial()
            // approximate count of completed trials
            recordCount.coerceAtLeast(1)
        } catch (_: Exception) { 1 }

        val highestShadow = profile.shadowScores.values.maxOrNull() ?: 30
        val highestVirtue = profile.virtueScores.values.maxOrNull() ?: 30
        val unlockedCosmeticsCount = profile.unlockedEffectIds.size

        val shadowScores = profile.shadowScores
        val virtueScores = profile.virtueScores
        val resonancePercent = (profile.stability * 0.5f + profile.humanity * 0.5f).toInt().coerceIn(10, 100)

        com.example.data.model.AchievementCatalog.DEFINITIONS.map { def ->
            val progress = when {
                def.id == "first_record" || def.id == "five_records" || def.id == "ten_records" -> recordCount
                def.id.startsWith("inscr_") -> recordCount
                def.id == "first_trial" || def.id == "three_trials" -> recordCount
                def.id == "first_cosmetic" || def.id == "three_cosmetics" -> unlockedCosmeticsCount.coerceAtLeast(if (profile.equippedEffectId != "effect_default") 1 else 0)
                def.id.startsWith("cosmetic_") -> unlockedCosmeticsCount.coerceAtLeast(if (profile.equippedEffectId != "effect_default") 1 else 0)
                def.id == "high_humanity" || def.id.startsWith("humanity_") -> profile.humanity
                def.id == "high_stability" || def.id.startsWith("stability_") -> profile.stability
                def.id == "shadow_master" -> highestShadow
                def.id == "virtue_master" -> highestVirtue
                def.id == "shard_collector" || def.id.startsWith("wealth_") -> profile.soulShards
                def.id.startsWith("resonance_") -> resonancePercent
                def.id.startsWith("metamorph_") -> profile.evolutionProgress
                def.id.startsWith("virtue_humility_") -> virtueScores[com.example.data.model.VirtueType.HUMILITY] ?: 30
                def.id.startsWith("virtue_charity_") -> virtueScores[com.example.data.model.VirtueType.CHARITY] ?: 30
                def.id.startsWith("virtue_courage_") -> virtueScores[com.example.data.model.VirtueType.COURAGE] ?: 30
                def.id.startsWith("virtue_gratitude_") -> virtueScores[com.example.data.model.VirtueType.GRATITUDE] ?: 30
                def.id.startsWith("virtue_temperance_") -> virtueScores[com.example.data.model.VirtueType.TEMPERANCE] ?: 30
                def.id.startsWith("virtue_patience_") -> virtueScores[com.example.data.model.VirtueType.PATIENCE] ?: 30
                def.id.startsWith("virtue_diligence_") -> virtueScores[com.example.data.model.VirtueType.DILIGENCE] ?: 30
                def.id.startsWith("shadow_pride_") -> shadowScores[com.example.data.model.ShadowType.PRIDE] ?: 30
                def.id.startsWith("shadow_greed_") -> shadowScores[com.example.data.model.ShadowType.GREED] ?: 30
                def.id.startsWith("shadow_desire_") -> shadowScores[com.example.data.model.ShadowType.DESIRE] ?: 30
                def.id.startsWith("shadow_envy_") -> shadowScores[com.example.data.model.ShadowType.ENVY] ?: 30
                def.id.startsWith("shadow_gluttony_") -> shadowScores[com.example.data.model.ShadowType.GLUTTONY] ?: 30
                def.id.startsWith("shadow_wrath_") -> shadowScores[com.example.data.model.ShadowType.WRATH] ?: 30
                def.id.startsWith("shadow_sloth_") -> shadowScores[com.example.data.model.ShadowType.SLOTH] ?: 30
                else -> 0
            }
            val isUnlocked = progress >= def.target
            val isClaimed = profile.claimedAchievementIds.contains(def.id)

            com.example.data.model.Achievement(
                id = def.id,
                title = def.title,
                description = def.description,
                category = def.category,
                icon = def.icon,
                targetProgress = def.target,
                currentProgress = progress.coerceAtMost(def.target),
                rewardShards = def.rewardShards,
                isUnlocked = isUnlocked,
                isClaimed = isClaimed
            )
        }
    }

    // Export Formats: JSON, Markdown, Plain Text, CSV
    suspend fun exportJson(): String = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: SoulIdentity.initial().toEntity()
        val records = soulDao.getRecentRecords(100)
        
        val json = JSONObject()
        json.put("app", "Godly System")
        json.put("version", 2)
        json.put("timestamp", System.currentTimeMillis())

        // Soul Identity
        val soulJson = JSONObject().apply {
            put("race", profile.race)
            put("className", profile.className)
            put("advancedClass", profile.advancedClass ?: "")
            put("archetype", profile.archetype)
            put("element", profile.element)
            put("alignment", profile.alignment)
            put("currentTitle", profile.currentTitle)
            put("dominantShadow", profile.dominantShadow)
            put("dominantVirtue", profile.dominantVirtue)
            put("shadowScores", profile.shadowScoresJson)
            put("virtueScores", profile.virtueScoresJson)
            put("humanity", profile.humanity)
            put("stability", profile.stability)
            put("evolutionProgress", profile.evolutionProgress)
            put("possibleEvolution", profile.possibleEvolution)
            put("systemMessage", profile.systemMessage)
            put("soulShards", profile.soulShards)
            put("equippedEffectId", profile.equippedEffectId)
            put("unlockedEffects", profile.unlockedEffectsJson)
            put("claimedAchievements", profile.claimedAchievementsJson)
        }
        json.put("soulProfile", soulJson)

        val recordsArray = JSONArray()
        records.forEach { r ->
            val rJson = JSONObject().apply {
                put("timestamp", r.timestamp)
                put("emotion", r.emotion)
                put("primaryShadow", r.primaryShadow ?: "")
                put("primaryVirtue", r.primaryVirtue ?: "")
                put("situation", r.situation)
                put("intention", r.intention)
                put("action", r.action)
                put("consequence", r.consequence)
                put("reflection", r.reflection)
                put("analysisInsight", r.analysisInsight)
            }
            recordsArray.put(rJson)
        }
        json.put("records", recordsArray)

        json.toString(2)
    }

    suspend fun exportMarkdown(): String = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: SoulIdentity.initial().toEntity()
        val soul = profile.toSoulIdentity()
        val records = soulDao.getRecentRecords(50)
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())

        buildString {
            appendLine("# 🌌 GODLY SYSTEM — SOUL CHRONICLE DOSSIER")
            appendLine("Generated on: ${dateFormat.format(java.util.Date())}")
            appendLine()
            appendLine("## 👑 Vessel Identity")
            appendLine("- **Title:** ${soul.currentTitle}")
            appendLine("- **Race:** ${soul.race}")
            appendLine("- **Class:** ${soul.advancedClass ?: soul.className}")
            appendLine("- **Archetype:** ${soul.archetype}")
            appendLine("- **Element:** ${soul.element}")
            appendLine("- **Alignment:** ${soul.alignment}")
            appendLine("- **Dominant Shadow (Sin):** ${soul.dominantShadow.runeSymbol} ${soul.dominantShadow.displayName}")
            appendLine("- **Dominant Virtue:** ${soul.dominantVirtue.runeSymbol} ${soul.dominantVirtue.displayName}")
            appendLine("- **Soul Shards:** 💎 ${soul.soulShards}")
            appendLine("- **Humanity Tether:** ${soul.humanity}%")
            appendLine("- **Metamorphic Awakening:** ${soul.evolutionProgress}% (Next: ${soul.possibleEvolution})")
            appendLine()
            appendLine("## ⚖️ The Seven Shadows & Seven Virtues")
            appendLine("| Force | Type | Score | Rune |")
            appendLine("| :--- | :--- | :--- | :--- |")
            ShadowType.values().forEach { shadow ->
                appendLine("| ${shadow.displayName} | Shadow | ${soul.shadowScores[shadow] ?: 30} | ${shadow.runeSymbol} |")
            }
            VirtueType.values().forEach { virtue ->
                appendLine("| ${virtue.displayName} | Virtue | ${soul.virtueScores[virtue] ?: 30} | ${virtue.runeSymbol} |")
            }
            appendLine()
            appendLine("## 📜 Transmuted Evaluation Records (${records.size})")
            records.forEachIndexed { i, r ->
                appendLine("### Trial #${i + 1} • ${r.emotion} (${dateFormat.format(java.util.Date(r.timestamp))})")
                appendLine("- **Catalyst:** ${r.primaryShadow ?: "—"} / ${r.primaryVirtue ?: "—"}")
                appendLine("- **Situation:** ${r.situation}")
                appendLine("- **Intention:** ${r.intention}")
                appendLine("- **Action:** ${r.action}")
                appendLine("- **Consequence:** ${r.consequence}")
                appendLine("- **Reflection:** ${r.reflection}")
                appendLine("- **System Insight:** *${r.analysisInsight}*")
                appendLine()
            }
            appendLine("---")
            appendLine("<!-- GODLY_SYSTEM_DATA_BEGIN")
            appendLine(exportJson())
            appendLine("GODLY_SYSTEM_DATA_END -->")
        }
    }

    suspend fun exportPlainText(): String = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: SoulIdentity.initial().toEntity()
        val soul = profile.toSoulIdentity()
        val records = soulDao.getRecentRecords(30)
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())

        buildString {
            appendLine("==================================================")
            appendLine("       GODLY SYSTEM — VESSEL DOSSIER REPORT       ")
            appendLine("==================================================")
            appendLine("Export Date   : ${dateFormat.format(java.util.Date())}")
            appendLine("Vessel Title  : ${soul.currentTitle}")
            appendLine("Vessel Race   : ${soul.race}")
            appendLine("Vessel Class  : ${soul.advancedClass ?: soul.className}")
            appendLine("Alignment     : ${soul.alignment}")
            appendLine("Element       : ${soul.element}")
            appendLine("Soul Shards   : ${soul.soulShards}")
            appendLine("Humanity      : ${soul.humanity}% | Stability: ${soul.stability}%")
            appendLine("Awakening     : ${soul.evolutionProgress}% -> ${soul.possibleEvolution}")
            appendLine("Dominant Sin  : ${soul.dominantShadow.runeSymbol} ${soul.dominantShadow.displayName}")
            appendLine("Dominant Virtue: ${soul.dominantVirtue.runeSymbol} ${soul.dominantVirtue.displayName}")
            appendLine("--------------------------------------------------")
            appendLine("SYSTEM OBSERVATION: ${soul.systemMessage}")
            appendLine("--------------------------------------------------")
            appendLine("RECENT SOUL RECORDS:")
            records.forEachIndexed { idx, r ->
                appendLine("[${idx + 1}] ${dateFormat.format(java.util.Date(r.timestamp))} | ${r.emotion}")
                appendLine("    Situation : ${r.situation}")
                appendLine("    Action    : ${r.action}")
                appendLine("    Insight   : ${r.analysisInsight}")
                appendLine()
            }
            appendLine("==================================================")
            appendLine("DATA_PAYLOAD:")
            appendLine(exportJson())
        }
    }

    suspend fun exportPdf(resonance: SoulResonanceData? = null): ByteArray = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: SoulIdentity.initial().toEntity()
        val soul = profile.toSoulIdentity()
        val records = soulDao.getRecentRecords(100)
        val events = soulDao.getRecentEvents(50)
        SoulPdfExporter.generateSoulHistoryPdf(soul, records, events, resonance)
    }

    suspend fun writePdfToStream(outputStream: OutputStream, resonance: SoulResonanceData? = null) = withContext(Dispatchers.IO) {
        val profile = soulDao.getSoulProfile() ?: SoulIdentity.initial().toEntity()
        val soul = profile.toSoulIdentity()
        val records = soulDao.getRecentRecords(100)
        val events = soulDao.getRecentEvents(50)
        SoulPdfExporter.writeSoulHistoryPdfToStream(soul, records, events, resonance, outputStream)
    }

    suspend fun exportCsv(): String = withContext(Dispatchers.IO) {
        val records = soulDao.getRecentRecords(200)
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())

        buildString {
            appendLine("id,timestamp,formatted_date,emotion,primary_shadow,primary_virtue,situation,intention,action,consequence,reflection,insight")
            records.forEach { r ->
                val dateStr = dateFormat.format(java.util.Date(r.timestamp))
                fun escape(s: String?) = "\"${(s ?: "").replace("\"", "\"\"")}\""
                appendLine("${r.id},${r.timestamp},${escape(dateStr)},${escape(r.emotion)},${escape(r.primaryShadow)},${escape(r.primaryVirtue)},${escape(r.situation)},${escape(r.intention)},${escape(r.action)},${escape(r.consequence)},${escape(r.reflection)},${escape(r.analysisInsight)}")
            }
        }
    }

    suspend fun importCsv(csvStr: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val lines = csvStr.lines().filter { it.isNotBlank() }
            if (lines.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("CSV content is empty"))
            }

            var importedCount = 0
            val headerLine = lines.first().lowercase()
            val dataLines = if (headerLine.contains("emotion") || headerLine.contains("timestamp") || headerLine.contains("primary_shadow")) {
                lines.drop(1)
            } else {
                lines
            }

            dataLines.forEach { line ->
                // Simple CSV row parser handling quoted fields
                val tokens = parseCsvLine(line)
                if (tokens.isNotEmpty()) {
                    val timestamp = tokens.getOrNull(1)?.toLongOrNull() ?: System.currentTimeMillis()
                    val emotion = tokens.getOrNull(3)?.takeIf { it.isNotBlank() } ?: "Restored Resonance"
                    val shadow = tokens.getOrNull(4)?.takeIf { it.isNotBlank() }
                    val virtue = tokens.getOrNull(5)?.takeIf { it.isNotBlank() }
                    val situation = tokens.getOrNull(6)?.takeIf { it.isNotBlank() } ?: "Restored CSV Record"
                    val intention = tokens.getOrNull(7) ?: ""
                    val action = tokens.getOrNull(8) ?: ""
                    val consequence = tokens.getOrNull(9) ?: ""
                    val reflection = tokens.getOrNull(10) ?: ""
                    val insight = tokens.getOrNull(11) ?: "Restored from CSV"

                    soulDao.insertRecord(
                        EvaluationRecordEntity(
                            timestamp = timestamp,
                            emotion = emotion,
                            primaryShadow = shadow,
                            primaryVirtue = virtue,
                            situation = situation,
                            intention = intention,
                            action = action,
                            consequence = consequence,
                            reflection = reflection,
                            analysisInsight = insight,
                            forcesResonatedJson = "{}",
                            humanityShift = 0,
                            stabilityShift = 0
                        )
                    )
                    importedCount++
                }
            }

            if (importedCount > 0) {
                soulDao.insertEvolutionEvent(
                    EvolutionEventEntity(
                        dayNumber = 1,
                        eventType = "CSV_IMPORT",
                        title = "CSV Soul History Integrated",
                        description = "Imported and calibrated $importedCount transmutation records from external CSV log.",
                        runeIcon = "📜"
                    )
                )
                Result.success("Successfully imported $importedCount records from CSV.")
            } else {
                Result.failure(IllegalArgumentException("No valid CSV rows parsed."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var cur = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '\"') {
                    cur.append('\"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            } else if (c == ',' && !inQuotes) {
                result.add(cur.toString().trim())
                cur = StringBuilder()
            } else {
                cur.append(c)
            }
            i++
        }
        result.add(cur.toString().trim())
        return result
    }

    suspend fun importAny(contentStr: String): Result<String> = withContext(Dispatchers.IO) {
        val trimmed = contentStr.trim()
        if (trimmed.startsWith("{") || trimmed.contains("soulProfile") || trimmed.contains("DATA_PAYLOAD:") || trimmed.contains("GODLY_SYSTEM_DATA_BEGIN")) {
            importJson(trimmed)
        } else if (trimmed.contains(",") && (trimmed.contains("primary_shadow") || trimmed.contains("emotion") || trimmed.lines().size > 1)) {
            importCsv(trimmed)
        } else {
            importJson(trimmed)
        }
    }

    suspend fun importJson(jsonStr: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Check if text is wrapped in Markdown or Text block
            var cleanJson = jsonStr.trim()
            if (cleanJson.contains("<!-- GODLY_SYSTEM_DATA_BEGIN") && cleanJson.contains("GODLY_SYSTEM_DATA_END -->")) {
                cleanJson = cleanJson.substringAfter("<!-- GODLY_SYSTEM_DATA_BEGIN")
                    .substringBefore("GODLY_SYSTEM_DATA_END -->")
                    .trim()
            } else if (cleanJson.contains("DATA_PAYLOAD:")) {
                cleanJson = cleanJson.substringAfter("DATA_PAYLOAD:").trim()
            }

            val root = JSONObject(cleanJson)
            if (!root.has("soulProfile")) {
                return@withContext Result.failure(IllegalArgumentException("Invalid Godly System data file: missing soulProfile"))
            }

            val soulJson = root.getJSONObject("soulProfile")
            val race = soulJson.optString("race", "Human")
            val className = soulJson.optString("className", "Seeker")
            val advancedClass = soulJson.optString("advancedClass", "").takeIf { it.isNotBlank() }
            val archetype = soulJson.optString("archetype", "The Awakening Vessel")
            val element = soulJson.optString("element", "Aether / Unattuned")
            val alignment = soulJson.optString("alignment", "True Neutral")
            val currentTitle = soulJson.optString("currentTitle", "The Unwritten Soul")
            val dominantShadow = soulJson.optString("dominantShadow", "PRIDE")
            val dominantVirtue = soulJson.optString("dominantVirtue", "HUMILITY")
            val shadowScoresJson = soulJson.optString("shadowScores", "{}")
            val virtueScoresJson = soulJson.optString("virtueScores", "{}")
            val humanity = soulJson.optInt("humanity", 80)
            val stability = soulJson.optInt("stability", 70)
            val evolutionProgress = soulJson.optInt("evolutionProgress", 20)
            val possibleEvolution = soulJson.optString("possibleEvolution", "Unknown Path")
            val systemMessage = soulJson.optString("systemMessage", "Imported Vessel matrix.")
            val soulShards = soulJson.optInt("soulShards", 100)
            val equippedEffectId = soulJson.optString("equippedEffectId", "effect_default")
            val unlockedEffectsJson = soulJson.optString("unlockedEffects", "[\"effect_default\"]")
            val claimedAchievementsJson = soulJson.optString("claimedAchievements", "[]")

            val importedProfile = SoulProfileEntity(
                id = 1,
                race = race,
                className = className,
                advancedClass = advancedClass,
                archetype = archetype,
                element = element,
                alignment = alignment,
                currentTitle = currentTitle,
                dominantShadow = dominantShadow,
                dominantVirtue = dominantVirtue,
                shadowScoresJson = shadowScoresJson,
                virtueScoresJson = virtueScoresJson,
                humanity = humanity,
                stability = stability,
                evolutionProgress = evolutionProgress,
                possibleEvolution = possibleEvolution,
                strengthsJson = "[\"Imported Resonance\", \"Memory Integration\"]",
                weaknessesJson = "[]",
                systemMessage = systemMessage,
                soulShards = soulShards,
                equippedEffectId = equippedEffectId,
                unlockedEffectsJson = unlockedEffectsJson,
                claimedAchievementsJson = claimedAchievementsJson,
                lastUpdated = System.currentTimeMillis()
            )

            soulDao.insertOrUpdateProfile(importedProfile)

            // Import records if available
            var importedRecordCount = 0
            if (root.has("records")) {
                val recordsArr = root.getJSONArray("records")
                for (i in 0 until recordsArr.length()) {
                    val r = recordsArr.getJSONObject(i)
                    soulDao.insertRecord(
                        EvaluationRecordEntity(
                            timestamp = r.optLong("timestamp", System.currentTimeMillis()),
                            emotion = r.optString("emotion", "Equilibrium"),
                            primaryShadow = r.optString("primaryShadow", "").takeIf { it.isNotBlank() },
                            primaryVirtue = r.optString("primaryVirtue", "").takeIf { it.isNotBlank() },
                            situation = r.optString("situation", "Archived record"),
                            intention = r.optString("intention", "Preserved"),
                            action = r.optString("action", "Recorded"),
                            consequence = r.optString("consequence", "Imported matrix"),
                            reflection = r.optString("reflection", "Past memory restored."),
                            analysisInsight = r.optString("analysisInsight", "Imported"),
                            forcesResonatedJson = "{}",
                            humanityShift = 0,
                            stabilityShift = 0
                        )
                    )
                    importedRecordCount++
                }
            }

            soulDao.insertEvolutionEvent(
                EvolutionEventEntity(
                    dayNumber = 1,
                    eventType = "IMPORT_RESTORE",
                    title = "Soul Memory Restored",
                    description = "Imported vessel matrix: $race $className with $soulShards Soul Shards and $importedRecordCount records.",
                    runeIcon = "🌌"
                )
            )

            Result.success("Restored vessel: $race ($className) with $soulShards Soul Shards and $importedRecordCount records.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Converters
    private fun SoulProfileEntity.toSoulIdentity(): SoulIdentity {
        val shadowMap = mutableMapOf<ShadowType, Int>()
        try {
            val json = JSONObject(shadowScoresJson)
            ShadowType.values().forEach { shadowMap[it] = json.optInt(it.name, 30) }
        } catch (_: Exception) {
            ShadowType.values().forEach { shadowMap[it] = 30 }
        }

        val virtueMap = mutableMapOf<VirtueType, Int>()
        try {
            val json = JSONObject(virtueScoresJson)
            VirtueType.values().forEach { virtueMap[it] = json.optInt(it.name, 30) }
        } catch (_: Exception) {
            VirtueType.values().forEach { virtueMap[it] = 30 }
        }

        val strengths = mutableListOf<String>()
        try {
            val arr = JSONArray(strengthsJson)
            for (i in 0 until arr.length()) strengths.add(arr.getString(i))
        } catch (_: Exception) {}

        val weaknesses = mutableListOf<String>()
        try {
            val arr = JSONArray(weaknessesJson)
            for (i in 0 until arr.length()) weaknesses.add(arr.getString(i))
        } catch (_: Exception) {}

        val unlockedSet = mutableSetOf<String>("effect_default")
        try {
            val arr = JSONArray(unlockedEffectsJson)
            for (i in 0 until arr.length()) unlockedSet.add(arr.getString(i))
        } catch (_: Exception) {}

        val claimedSet = mutableSetOf<String>()
        try {
            val arr = JSONArray(claimedAchievementsJson)
            for (i in 0 until arr.length()) claimedSet.add(arr.getString(i))
        } catch (_: Exception) {}

        val unlockedArchetypes = mutableSetOf<String>("arch_seeker")
        try {
            val arr = JSONArray(unlockedArchetypesJson)
            for (i in 0 until arr.length()) unlockedArchetypes.add(arr.getString(i))
        } catch (_: Exception) {}

        // Add starter archetype if not present
        unlockedArchetypes.add("arch_seeker")

        return SoulIdentity(
            race = race,
            className = className,
            advancedClass = advancedClass,
            archetype = archetype,
            element = element,
            alignment = alignment,
            currentTitle = currentTitle,
            dominantShadow = try { ShadowType.valueOf(dominantShadow) } catch (_: Exception) { ShadowType.PRIDE },
            dominantVirtue = try { VirtueType.valueOf(dominantVirtue) } catch (_: Exception) { VirtueType.HUMILITY },
            shadowScores = shadowMap,
            virtueScores = virtueMap,
            humanity = humanity,
            stability = stability,
            evolutionProgress = evolutionProgress,
            possibleEvolution = possibleEvolution,
            strengths = strengths.ifEmpty { listOf("Resonant Will", "Spiritual Flexibility") },
            weaknesses = weaknesses.ifEmpty { listOf("Uncalibrated Forces") },
            systemMessage = systemMessage,
            soulShards = soulShards,
            soulLevel = soulLevel.coerceAtLeast(1),
            soulExp = soulExp.coerceAtLeast(0),
            totalSoulExp = totalSoulExp.coerceAtLeast(0),
            attunedArchetypeId = attunedArchetypeId.ifBlank { "arch_seeker" },
            unlockedArchetypeIds = unlockedArchetypes,
            equippedEffectId = equippedEffectId,
            unlockedEffectIds = unlockedSet,
            claimedAchievementIds = claimedSet
        )
    }

    private fun SoulIdentity.toEntity(): SoulProfileEntity {
        val shadowsJson = JSONObject().apply {
            shadowScores.forEach { (k, v) -> put(k.name, v) }
        }.toString()

        val virtuesJson = JSONObject().apply {
            virtueScores.forEach { (k, v) -> put(k.name, v) }
        }.toString()

        val strengthsArr = JSONArray(strengths).toString()
        val weaknessesArr = JSONArray(weaknesses).toString()
        val unlockedArr = JSONArray(unlockedEffectIds).toString()
        val claimedArr = JSONArray(claimedAchievementIds).toString()
        val unlockedArchArr = JSONArray(unlockedArchetypeIds).toString()

        return SoulProfileEntity(
            id = 1,
            race = race,
            className = className,
            advancedClass = advancedClass,
            archetype = archetype,
            element = element,
            alignment = alignment,
            currentTitle = currentTitle,
            dominantShadow = dominantShadow.name,
            dominantVirtue = dominantVirtue.name,
            shadowScoresJson = shadowsJson,
            virtueScoresJson = virtuesJson,
            humanity = humanity,
            stability = stability,
            evolutionProgress = evolutionProgress,
            possibleEvolution = possibleEvolution,
            strengthsJson = strengthsArr,
            weaknessesJson = weaknessesArr,
            systemMessage = systemMessage,
            soulShards = soulShards,
            soulLevel = soulLevel,
            soulExp = soulExp,
            totalSoulExp = totalSoulExp,
            attunedArchetypeId = attunedArchetypeId,
            unlockedArchetypesJson = unlockedArchArr,
            equippedEffectId = equippedEffectId,
            unlockedEffectsJson = unlockedArr,
            claimedAchievementsJson = claimedArr
        )
    }
}
