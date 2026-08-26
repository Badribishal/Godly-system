package com.example.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val icon: String,
    val targetProgress: Int,
    val currentProgress: Int,
    val rewardShards: Int,
    val isUnlocked: Boolean,
    val isClaimed: Boolean
)

object AchievementCatalog {
    data class Definition(
        val id: String,
        val title: String,
        val description: String,
        val category: String,
        val icon: String,
        val target: Int,
        val rewardShards: Int
    )

    val DEFINITIONS = listOf(
        Definition(
            id = "first_record",
            title = "The Awakening Inscription",
            description = "Record your first daily psychological evaluation.",
            category = "Inscription",
            icon = "📜",
            target = 1,
            rewardShards = 30
        ),
        Definition(
            id = "five_records",
            title = "Disciplined Seeker",
            description = "Record 5 daily alchemical reflections in the sanctuary.",
            category = "Dedication",
            icon = "⚔️",
            target = 5,
            rewardShards = 60
        ),
        Definition(
            id = "ten_records",
            title = "Soul Chronicler",
            description = "Complete 10 total evaluations to deepen self-knowledge.",
            category = "Dedication",
            icon = "📖",
            target = 10,
            rewardShards = 100
        ),
        Definition(
            id = "first_trial",
            title = "Confronted Dilemma",
            description = "Make your first choice in a Daily Divine Trial.",
            category = "Trials",
            icon = "⚖️",
            target = 1,
            rewardShards = 40
        ),
        Definition(
            id = "three_trials",
            title = "Trial Master",
            description = "Complete 3 divine dilemmas with personal reflections.",
            category = "Trials",
            icon = "🏛️",
            target = 3,
            rewardShards = 80
        ),
        Definition(
            id = "first_cosmetic",
            title = "Astral Stylist",
            description = "Unlock and equip your first custom avatar visual effect.",
            category = "Cosmetics",
            icon = "✨",
            target = 1,
            rewardShards = 50
        ),
        Definition(
            id = "three_cosmetics",
            title = "Wardrobe Sovereign",
            description = "Unlock 3 distinct visual effects from the wardrobe.",
            category = "Cosmetics",
            icon = "👑",
            target = 3,
            rewardShards = 120
        ),
        Definition(
            id = "high_humanity",
            title = "Guardian of Humanity",
            description = "Elevate your Humanity score to 75 or higher.",
            category = "Ascension",
            icon = "🕊️",
            target = 75,
            rewardShards = 70
        ),
        Definition(
            id = "high_stability",
            title = "Unshakable Mind",
            description = "Attain a Stability balance score of 75 or higher.",
            category = "Ascension",
            icon = "🛡️",
            target = 75,
            rewardShards = 70
        ),
        Definition(
            id = "shadow_master",
            title = "Void Harmonizer",
            description = "Cultivate any Shadow force to level 60 or above without corruption.",
            category = "Forces",
            icon = "🌒",
            target = 60,
            rewardShards = 60
        ),
        Definition(
            id = "virtue_master",
            title = "Pillar of Light",
            description = "Cultivate any Virtue force to level 60 or above.",
            category = "Forces",
            icon = "☀️",
            target = 60,
            rewardShards = 60
        ),
        Definition(
            id = "shard_collector",
            title = "Soul Weaver",
            description = "Collect a total of 150 Soul Shards.",
            category = "Wealth",
            icon = "💎",
            target = 150,
            rewardShards = 50
        )
    )
}
