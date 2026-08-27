package com.example.data.model

import java.util.Locale

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

    private val ROMAN_NUMERALS = listOf(
        "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
        "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX",
        "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX",
        "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL",
        "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L",
        "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX",
        "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX",
        "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX",
        "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC",
        "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"
    )

    private fun getRoman(n: Int): String {
        return if (n in 1..100) ROMAN_NUMERALS[n - 1] else "$n"
    }

    val DEFINITIONS: List<Definition> by lazy {
        val list = mutableListOf<Definition>()

        // 1. ORIGINAL CORE DEFINITIONS (Guarantees backward compatibility)
        list.add(
            Definition(
                id = "first_record",
                title = "The Awakening Inscription",
                description = "Record your first daily psychological evaluation.",
                category = "Inscription",
                icon = "📜",
                target = 1,
                rewardShards = 30
            )
        )
        list.add(
            Definition(
                id = "five_records",
                title = "Disciplined Seeker",
                description = "Record 5 daily alchemical reflections in the sanctuary.",
                category = "Dedication",
                icon = "⚔️",
                target = 5,
                rewardShards = 60
            )
        )
        list.add(
            Definition(
                id = "ten_records",
                title = "Soul Chronicler",
                description = "Complete 10 total evaluations to deepen self-knowledge.",
                category = "Dedication",
                icon = "📖",
                target = 10,
                rewardShards = 100
            )
        )
        list.add(
            Definition(
                id = "first_trial",
                title = "Confronted Dilemma",
                description = "Make your first choice in a Daily Divine Trial.",
                category = "Trials",
                icon = "⚖️",
                target = 1,
                rewardShards = 40
            )
        )
        list.add(
            Definition(
                id = "three_trials",
                title = "Trial Master",
                description = "Complete 3 divine dilemmas with personal reflections.",
                category = "Trials",
                icon = "🏛️",
                target = 3,
                rewardShards = 80
            )
        )
        list.add(
            Definition(
                id = "first_cosmetic",
                title = "Astral Stylist",
                description = "Unlock and equip your first custom avatar visual effect.",
                category = "Cosmetics",
                icon = "✨",
                target = 1,
                rewardShards = 50
            )
        )
        list.add(
            Definition(
                id = "three_cosmetics",
                title = "Wardrobe Sovereign",
                description = "Unlock 3 distinct visual effects from the wardrobe.",
                category = "Cosmetics",
                icon = "👑",
                target = 3,
                rewardShards = 120
            )
        )
        list.add(
            Definition(
                id = "high_humanity",
                title = "Guardian of Humanity",
                description = "Elevate your Humanity score to 75 or higher.",
                category = "Ascension",
                icon = "🕊️",
                target = 75,
                rewardShards = 70
            )
        )
        list.add(
            Definition(
                id = "high_stability",
                title = "Unshakable Mind",
                description = "Attain a Stability balance score of 75 or higher.",
                category = "Ascension",
                icon = "🛡️",
                target = 75,
                rewardShards = 70
            )
        )
        list.add(
            Definition(
                id = "shadow_master",
                title = "Void Harmonizer",
                description = "Cultivate any Shadow force to level 60 or above without corruption.",
                category = "Shadows",
                icon = "🌒",
                target = 60,
                rewardShards = 60
            )
        )
        list.add(
            Definition(
                id = "virtue_master",
                title = "Pillar of Light",
                description = "Cultivate any Virtue force to level 60 or above.",
                category = "Virtues",
                icon = "☀️",
                target = 60,
                rewardShards = 60
            )
        )
        list.add(
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

        // 2. EXPANSIVE INSCRIPTION CHRONICLES (100 Milestones from 2 to 200)
        for (i in 2..100) {
            val count = i * 2
            val roman = getRoman(i)
            list.add(
                Definition(
                    id = "inscr_tier_$i",
                    title = "Codex of Transmutation $roman",
                    description = "Inscribe $count psychological reflections and alchemical calibrations.",
                    category = "Inscriptions",
                    icon = if (i % 5 == 0) "🔮" else "📜",
                    target = count,
                    rewardShards = 20 + (i * 3)
                )
            )
        }

        // 3. SEVEN HEAVENLY VIRTUES MASTERY (7 Virtues x 70 Tiers = 490 Milestones)
        val virtueTitles = mapOf<VirtueType, List<String>>(
            VirtueType.HUMILITY to listOf("Seed of Grace", "Humble Mirror", "Quiet Sovereign", "Starlight Humility", "Celestial Modesty"),
            VirtueType.CHARITY to listOf("Kindling Giver", "Compassion Spring", "Boundless Philanthropist", "Astral Benefactor", "Heart of the Cosmos"),
            VirtueType.COURAGE to listOf("Lionheart Vanguard", "Fearless Aegis", "Adamant Hero", "Valorous Knight", "Unbroken Shield"),
            VirtueType.GRATITUDE to listOf("Praising Voice", "Eucharistic Heart", "Abundance Attunement", "Grace Resonator", "Infinite Thanksgiving"),
            VirtueType.TEMPERANCE to listOf("Measured Equilibrium", "Alchemical Balancer", "Sovereign Restraint", "Middle Pillar", "Cosmic Stasis"),
            VirtueType.PATIENCE to listOf("Granite Stillness", "Timeless River", "Enduring Anchor", "Patient Watcher", "Eternal Horizon"),
            VirtueType.DILIGENCE to listOf("Unyielding Flame", "Master Craftsman", "Indomitable Will", "Ascendant Work", "Infinite Forge")
        )

        VirtueType.entries.forEach { virtue ->
            val prefixes = virtueTitles[virtue] ?: listOf("Adept of ${virtue.displayName}")
            for (tier in 1..70) {
                val scoreReq = 10 + (tier * 2)
                val roman = getRoman(tier)
                val prefix = prefixes[(tier - 1) % prefixes.size]
                list.add(
                    Definition(
                        id = "virtue_${virtue.name.lowercase(Locale.ROOT)}_tier_$tier",
                        title = "$prefix $roman",
                        description = "Cultivate the divine resonance of ${virtue.displayName} to score $scoreReq.",
                        category = "Virtues",
                        icon = virtue.runeSymbol,
                        target = scoreReq,
                        rewardShards = 15 + (tier * 2)
                    )
                )
            }
        }

        // 4. SEVEN DEADLY SINS HARMONIZATION (7 Sins x 55 Tiers = 385 Milestones)
        val shadowTitles = mapOf<ShadowType, List<String>>(
            ShadowType.PRIDE to listOf("Solar Sovereign", "Unbowed Flame", "Apex Majesty", "Self-Creation", "Crown of the Void"),
            ShadowType.GREED to listOf("Hungry Collector", "Primal Ambition", "Treasure Architect", "Empire Binder", "Cosmic Ingester"),
            ShadowType.DESIRE to listOf("Vital Passion", "Sensory Blaze", "Eros Catalyst", "Ecstatic Wave", "Infinite Longing"),
            ShadowType.ENVY to listOf("Mirror of Rivalry", "Catalyst of Emulation", "Aspiration Blade", "Peerless Seeker", "Horizon Chaser"),
            ShadowType.GLUTTONY to listOf("Sacred Feaster", "Omnivorous Spirit", "Abyssal Thirst", "Plentiful Vessel", "Cosmic Devourer"),
            ShadowType.WRATH to listOf("Volcanic Wrath", "Righteous Fury", "Thunderous Cleaver", "Tempest of Truth", "Unstoppable Force"),
            ShadowType.SLOTH to listOf("Deep Stillness", "Dormant Potential", "Sovereign Repose", "Void Meditation", "Quiet Cosmos")
        )

        ShadowType.entries.forEach { shadow ->
            val prefixes = shadowTitles[shadow] ?: listOf("Harmonizer of ${shadow.displayName}")
            for (tier in 1..55) {
                val scoreReq = 10 + (tier * 2)
                val roman = getRoman(tier)
                val prefix = prefixes[(tier - 1) % prefixes.size]
                list.add(
                    Definition(
                        id = "shadow_${shadow.name.lowercase(Locale.ROOT)}_tier_$tier",
                        title = "$prefix $roman",
                        description = "Integrate and transmute the primal shadow of ${shadow.displayName} to score $scoreReq.",
                        category = "Shadows",
                        icon = shadow.runeSymbol,
                        target = scoreReq,
                        rewardShards = 15 + (tier * 2)
                    )
                )
            }
        }

        // 5. ASCENSION & HUMANITY TETHER (40 Milestones)
        for (i in 1..40) {
            val req = 20 + (i * 2)
            val roman = getRoman(i)
            list.add(
                Definition(
                    id = "humanity_tier_$i",
                    title = "Humanity Tether $roman",
                    description = "Maintain empathetic grounding and mortal tether score at $req%.",
                    category = "Ascension",
                    icon = "🕊️",
                    target = req,
                    rewardShards = 25 + (i * 3)
                )
            )
        }

        // 6. STABILITY & PSYCHOLOGICAL EQUILIBRIUM (40 Milestones)
        for (i in 1..40) {
            val req = 20 + (i * 2)
            val roman = getRoman(i)
            list.add(
                Definition(
                    id = "stability_tier_$i",
                    title = "Mind Equilibrium $roman",
                    description = "Attain an unbroken psychological stability score of $req%.",
                    category = "Ascension",
                    icon = "🛡️",
                    target = req,
                    rewardShards = 25 + (i * 3)
                )
            )
        }

        // 7. SOUL RESONANCE FREQUENCIES (30 Milestones)
        for (i in 1..30) {
            val req = 30 + (i * 2)
            val roman = getRoman(i)
            list.add(
                Definition(
                    id = "resonance_tier_$i",
                    title = "Harmonic Octave $roman",
                    description = "Harmonize the vessel's internal frequency to $req% soul resonance.",
                    category = "Resonance",
                    icon = "🎶",
                    target = req,
                    rewardShards = 30 + (i * 3)
                )
            )
        }

        // 8. SOUL SHARD WEALTH & FORTUNE (40 Milestones)
        val shardTiers = listOf(
            25, 50, 75, 100, 150, 200, 250, 300, 400, 500,
            600, 750, 900, 1000, 1200, 1500, 1800, 2000, 2500, 3000,
            3500, 4000, 4500, 5000, 6000, 7000, 8000, 9000, 10000, 12000,
            15000, 18000, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 100000
        )
        shardTiers.forEachIndexed { idx, targetShards ->
            val roman = getRoman(idx + 1)
            list.add(
                Definition(
                    id = "wealth_tier_${idx + 1}",
                    title = "Astral Treasury $roman",
                    description = "Accumulate a balance of $targetShards Soul Shards.",
                    category = "Wealth",
                    icon = "💎",
                    target = targetShards,
                    rewardShards = 40 + (idx * 5)
                )
            )
        }

        // 9. METAMORPHIC AWAKENING EVOLUTION (20 Milestones)
        for (i in 1..20) {
            val req = i * 5
            val roman = getRoman(i)
            list.add(
                Definition(
                    id = "metamorph_tier_$i",
                    title = "Metamorphosis Gate $roman",
                    description = "Advance vessel metamorphic awakening progress to $req%.",
                    category = "Evolution",
                    icon = "⚡",
                    target = req,
                    rewardShards = 35 + (i * 4)
                )
            )
        }

        // 10. COSMETIC SOVEREIGN (10 Milestones)
        for (i in 1..10) {
            val roman = getRoman(i)
            list.add(
                Definition(
                    id = "cosmetic_tier_$i",
                    title = "Astral Couturier $roman",
                    description = "Unlock and possess $i distinct visual aura cosmetics in the wardrobe.",
                    category = "Cosmetics",
                    icon = "✨",
                    target = i,
                    rewardShards = 50 + (i * 10)
                )
            )
        }

        list
    }
}
