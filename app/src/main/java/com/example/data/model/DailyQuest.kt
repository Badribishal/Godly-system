package com.example.data.model

enum class QuestCategory(
    val displayName: String,
    val rune: String,
    val colorHex: Long
) {
    VIRTUE_CULTIVATION("Virtue Attunement", "🪽", 0xFFFCD34D),
    SHADOW_TRANSMUTATION("Shadow Alchemy", "🔥", 0xFFEF4444),
    ASTRAL_MINDFULNESS("Mindful Equilibrium", "🧘", 0xFF38BDF8),
    WISDOM_CONTEMPLATION("Axiom Inquiry", "📜", 0xFFA78BFA)
}

data class DailyQuest(
    val id: String,
    val title: String,
    val category: QuestCategory,
    val subtitle: String,
    val prompt: String,
    val expReward: Int = 45,
    val shardsReward: Int = 20,
    val isCompleted: Boolean = false,
    val completionTimestamp: Long? = null,
    val userReflection: String? = null,
    val targetAffinity: String = "Conscious Alignment",
    val guidanceTip: String = "Reflect deeply and infuse your answer into the vessel."
)

data class DailyQuestState(
    val dateKey: String,
    val quests: List<DailyQuest>,
    val allCompletedBonusClaimed: Boolean = false,
    val bonusExpReward: Int = 100,
    val bonusShardsReward: Int = 50
) {
    val completedCount: Int get() = quests.count { it.isCompleted }
    val totalCount: Int get() = quests.size
    val isAllCompleted: Boolean get() = totalCount > 0 && completedCount == totalCount
    val progressPercent: Float get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount.toFloat()
}
