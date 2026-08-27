package com.example.data.engine

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.DailyQuest
import com.example.data.model.DailyQuestState
import com.example.data.model.QuestCategory
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DailyQuestEngine {

    private const val PREFS_NAME = "godly_daily_quests_prefs"
    private const val KEY_QUEST_DATE = "current_quest_date"
    private const val KEY_QUESTS_JSON = "current_quests_json"
    private const val KEY_BONUS_CLAIMED = "quest_bonus_claimed_"

    fun getTodayDateKey(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    /**
     * Retrieves or generates today's daily quests.
     */
    fun getDailyQuests(context: Context, soul: SoulIdentity): DailyQuestState {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayKey = getTodayDateKey()
        val savedDate = prefs.getString(KEY_QUEST_DATE, "")
        val savedJson = prefs.getString(KEY_QUESTS_JSON, "")
        val bonusClaimed = prefs.getBoolean(KEY_BONUS_CLAIMED + todayKey, false)

        if (savedDate == todayKey && !savedJson.isNullOrBlank()) {
            val quests = parseQuestsJson(savedJson)
            if (quests.isNotEmpty()) {
                return DailyQuestState(
                    dateKey = todayKey,
                    quests = quests,
                    allCompletedBonusClaimed = bonusClaimed
                )
            }
        }

        // Generate fresh quests for today
        val newQuests = generateDailyQuests(todayKey, soul)
        saveQuests(prefs, todayKey, newQuests)

        return DailyQuestState(
            dateKey = todayKey,
            quests = newQuests,
            allCompletedBonusClaimed = bonusClaimed
        )
    }

    /**
     * Marks a quest as completed and saves the reflection.
     */
    fun completeQuest(
        context: Context,
        questId: String,
        reflectionText: String,
        soul: SoulIdentity
    ): Pair<DailyQuestState, DailyQuest?> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayKey = getTodayDateKey()
        val currentState = getDailyQuests(context, soul)

        var completedQuest: DailyQuest? = null
        val updatedQuests = currentState.quests.map { quest ->
            if (quest.id == questId && !quest.isCompleted) {
                val updated = quest.copy(
                    isCompleted = true,
                    completionTimestamp = System.currentTimeMillis(),
                    userReflection = reflectionText.ifBlank { "Alchemical attunement sealed into the soul matrix." }
                )
                completedQuest = updated
                updated
            } else {
                quest
            }
        }

        saveQuests(prefs, todayKey, updatedQuests)

        val newState = currentState.copy(quests = updatedQuests)
        return Pair(newState, completedQuest)
    }

    /**
     * Claims the all-completed bonus cache.
     */
    fun claimAllCompletedBonus(context: Context, soul: SoulIdentity): DailyQuestState {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayKey = getTodayDateKey()
        val currentState = getDailyQuests(context, soul)

        prefs.edit().putBoolean(KEY_BONUS_CLAIMED + todayKey, true).apply()
        return currentState.copy(allCompletedBonusClaimed = true)
    }

    private fun generateDailyQuests(dateKey: String, soul: SoulIdentity): List<DailyQuest> {
        val dayHash = dateKey.hashCode()
        val virtueList = VirtueType.entries
        val shadowList = ShadowType.entries

        val chosenVirtue = virtueList[kotlin.math.abs(dayHash) % virtueList.size]
        val chosenShadow = shadowList[kotlin.math.abs(dayHash + 3) % shadowList.size]

        val quests = mutableListOf<DailyQuest>()

        // 1. Virtue Attunement Quest
        quests.add(
            DailyQuest(
                id = "quest_virtue_${dateKey}_${chosenVirtue.name.lowercase()}",
                title = "Virtue Attunement: ${chosenVirtue.displayName}",
                category = QuestCategory.VIRTUE_CULTIVATION,
                subtitle = "Active Sacred Radiance",
                prompt = getVirtuePrompt(chosenVirtue),
                expReward = 50,
                shardsReward = 25,
                targetAffinity = chosenVirtue.displayName,
                guidanceTip = "How can you manifest ${chosenVirtue.displayName} in a tangible, honest action today?"
            )
        )

        // 2. Shadow Transmutation Quest
        quests.add(
            DailyQuest(
                id = "quest_shadow_${dateKey}_${chosenShadow.name.lowercase()}",
                title = "Shadow Transmutation: ${chosenShadow.displayName}",
                category = QuestCategory.SHADOW_TRANSMUTATION,
                subtitle = "Alchemical Alchemy",
                prompt = getShadowPrompt(chosenShadow),
                expReward = 55,
                shardsReward = 25,
                targetAffinity = chosenShadow.displayName,
                guidanceTip = "Observe the raw impulse of ${chosenShadow.displayName} without self-judgment and transmute its fire."
            )
        )

        // 3. Astral Mindfulness Quest
        quests.add(
            DailyQuest(
                id = "quest_mindfulness_${dateKey}",
                title = "Astral Centering: Harmonic Breath",
                category = QuestCategory.ASTRAL_MINDFULNESS,
                subtitle = "Vessel Stabilization",
                prompt = "Take 60 seconds of rhythmic diaphragmatic breathing. Focus attention on the center of your chest and note the primary feeling present.",
                expReward = 40,
                shardsReward = 20,
                targetAffinity = "Harmonic Equilibrium",
                guidanceTip = "Steady your inner tempest; what emotion arises when stillness is welcomed?"
            )
        )

        // 4. Wisdom & Axiom Inquiry
        val wisdomPrompts = listOf(
            Pair("The Paradox of Sovereignty", "True mastery requires no validation from external approval. In what area of your life are you currently seeking unnecessary external proof?"),
            Pair("The Law of Polarity", "Light without shadow produces no depth. How can today's friction or difficulty become the exact whetstone that sharpens your soul?"),
            Pair("The Sovereign Anchor", "When chaos stirs around you, the center of the wheel remains stationary. Where is your immovable point of calm today?"),
            Pair("The Stream of Non-Resistance", "Water conquers stone not through force, but through relentless gentle persistence. What rigid expectation can you soften right now?")
        )
        val selectedWisdom = wisdomPrompts[kotlin.math.abs(dayHash + 7) % wisdomPrompts.size]

        quests.add(
            DailyQuest(
                id = "quest_wisdom_${dateKey}",
                title = "Axiom: ${selectedWisdom.first}",
                category = QuestCategory.WISDOM_CONTEMPLATION,
                subtitle = "Sanctuary Inquiry",
                prompt = selectedWisdom.second,
                expReward = 45,
                shardsReward = 20,
                targetAffinity = "Wisdom & Intention",
                guidanceTip = "Write a concise, heartfelt realization to calibrate your soul matrix."
            )
        )

        return quests
    }

    private fun getVirtuePrompt(virtue: VirtueType): String {
        return when (virtue) {
            VirtueType.HUMILITY -> "Reflect on someone whose wisdom, service, or presence assisted you recently. What did their contribution teach you about quiet strength?"
            VirtueType.COURAGE -> "Identify one necessary conversation, boundary, or truth you have hesitated to confront. What is the courageous step forward?"
            VirtueType.CHARITY -> "How can you extend genuine kindness, empathy, or tangible help to someone today without expecting anything in return?"
            VirtueType.TEMPERANCE -> "Where have you recently experienced excess or scattered energy? What simple boundary will restore your mental and physical balance?"
            VirtueType.DILIGENCE -> "Identify one critical task or internal commitment you made to yourself. What focused, uninterrupted action will honor this promise today?"
            VirtueType.PATIENCE -> "Recall a delay or frustrating obstacle. How can you respond with serene composure rather than hurried agitation?"
            VirtueType.GRATITUDE -> "Name three specific things in your immediate present reality that you deeply appreciate, however simple or subtle."
        }
    }

    private fun getShadowPrompt(shadow: ShadowType): String {
        return when (shadow) {
            ShadowType.PRIDE -> "Where might defensiveness or the desire to be proven right be clouding your openness? How can you transmute pride into dignified self-respect?"
            ShadowType.WRATH -> "What triggered irritation or anger in you recently? How can you channel this fiery energy into protective resolve or constructive change?"
            ShadowType.GREED -> "Where are you holding onto scarcity or fear of lack? How can you open yourself to trust and intentional stewardship?"
            ShadowType.GLUTTONY -> "In what area (information, stimulation, consumption) are you consuming beyond necessity? How can mindful fasting restore your clarity?"
            ShadowType.SLOTH -> "Where is apathy or avoidance whispering excuses? What is one small, decisive 2-minute action you can execute right now?"
            ShadowType.ENVY -> "Whose accomplishment sparked a twinge of jealousy? Reframe their success as living proof that what you desire is possible, and celebrate them."
            ShadowType.DESIRE -> "Where is compulsive desire seeking instant gratification over enduring meaning? How can you channel this primal vitality into creative work?"
        }
    }

    private fun saveQuests(prefs: SharedPreferences, dateKey: String, quests: List<DailyQuest>) {
        val array = JSONArray()
        for (q in quests) {
            val obj = JSONObject()
            obj.put("id", q.id)
            obj.put("title", q.title)
            obj.put("category", q.category.name)
            obj.put("subtitle", q.subtitle)
            obj.put("prompt", q.prompt)
            obj.put("expReward", q.expReward)
            obj.put("shardsReward", q.shardsReward)
            obj.put("isCompleted", q.isCompleted)
            obj.put("completionTimestamp", q.completionTimestamp ?: -1L)
            obj.put("userReflection", q.userReflection ?: "")
            obj.put("targetAffinity", q.targetAffinity)
            obj.put("guidanceTip", q.guidanceTip)
            array.put(obj)
        }

        prefs.edit()
            .putString(KEY_QUEST_DATE, dateKey)
            .putString(KEY_QUESTS_JSON, array.toString())
            .apply()
    }

    private fun parseQuestsJson(json: String): List<DailyQuest> {
        val list = mutableListOf<DailyQuest>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val categoryName = obj.optString("category", QuestCategory.VIRTUE_CULTIVATION.name)
                val category = runCatching { QuestCategory.valueOf(categoryName) }.getOrDefault(QuestCategory.VIRTUE_CULTIVATION)

                val compTime = obj.optLong("completionTimestamp", -1L)
                val userRef = obj.optString("userReflection", "")

                list.add(
                    DailyQuest(
                        id = obj.getString("id"),
                        title = obj.getString("title"),
                        category = category,
                        subtitle = obj.optString("subtitle", ""),
                        prompt = obj.getString("prompt"),
                        expReward = obj.optInt("expReward", 45),
                        shardsReward = obj.optInt("shardsReward", 20),
                        isCompleted = obj.optBoolean("isCompleted", false),
                        completionTimestamp = if (compTime > 0) compTime else null,
                        userReflection = if (userRef.isNotBlank()) userRef else null,
                        targetAffinity = obj.optString("targetAffinity", "Conscious Alignment"),
                        guidanceTip = obj.optString("guidanceTip", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
