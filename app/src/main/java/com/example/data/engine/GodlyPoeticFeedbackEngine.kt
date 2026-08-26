package com.example.data.engine

import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType

data class GodlyPoeticFeedback(
    val title: String,
    val epithet: String,
    val poeticVerse: String,
    val secondaryStanza: String,
    val divineAura: String,
    val dualityBalanceLabel: String,
    val virtuePercent: Int,
    val shadowPercent: Int,
    val cosmicVerdict: String
)

object GodlyPoeticFeedbackEngine {

    /**
     * Generates an automated, poetic description of the user's current 'Godly' identity
     * derived dynamically from their latest 7 Sins & 7 Virtues alchemical calculations,
     * dominant forces, humanity, stability, race, and title.
     */
    fun generateFeedback(soul: SoulIdentity, cycleIndex: Int = 0): GodlyPoeticFeedback {
        val totalVirtueScore = soul.virtueScores.values.sum().coerceAtLeast(1)
        val totalShadowScore = soul.shadowScores.values.sum().coerceAtLeast(1)
        val totalForce = totalVirtueScore + totalShadowScore
        val virtuePercent = ((totalVirtueScore.toFloat() / totalForce) * 100).toInt().coerceIn(5, 95)
        val shadowPercent = 100 - virtuePercent

        val dominantShadow = soul.dominantShadow
        val dominantVirtue = soul.dominantVirtue
        val race = soul.race
        val title = soul.currentTitle

        // Balance Classification
        val dualityLabel = when {
            virtuePercent >= 68 -> "✦ $virtuePercent% Sacred Virtues ✦ Luminous Ascendance"
            shadowPercent >= 68 -> "◈ $shadowPercent% Shadow Forces ◈ Primordial Eclipse"
            else -> "⚖️ $virtuePercent% Virtues | $shadowPercent% Shadows ⚖️ Alchemical Equilibrium"
        }

        // Divine Epithet
        val epithet = generateEpithet(dominantVirtue, dominantShadow, race)

        // Generate dynamic multi-line poetic stanzas based on force combinations
        val verses = generatePoeticVerses(
            soul = soul,
            dominantVirtue = dominantVirtue,
            dominantShadow = dominantShadow,
            virtuePercent = virtuePercent,
            shadowPercent = shadowPercent
        )

        val activeVerseIndex = (kotlin.math.abs(cycleIndex)) % verses.size
        val selectedVerse = verses[activeVerseIndex]

        val divineAura = when {
            soul.stability >= 80 && soul.humanity >= 70 -> "Incandescent Solar Radiance"
            soul.stability < 45 && shadowPercent > 55 -> "Turbulent Abyssal Supernova"
            soul.humanity < 40 -> "Transcendent Cosmic Void"
            virtuePercent > 60 -> "Aureate Celestial Luminescence"
            else -> "Harmonized Prismatic Ether"
        }

        val cosmicVerdict = generateCosmicVerdict(dominantVirtue, dominantShadow, soul.stability, soul.humanity)

        return GodlyPoeticFeedback(
            title = "THE $race OF $title".uppercase(),
            epithet = epithet,
            poeticVerse = selectedVerse.first,
            secondaryStanza = selectedVerse.second,
            divineAura = divineAura,
            dualityBalanceLabel = dualityLabel,
            virtuePercent = virtuePercent,
            shadowPercent = shadowPercent,
            cosmicVerdict = cosmicVerdict
        )
    }

    private fun generateEpithet(virtue: VirtueType, shadow: ShadowType, race: String): String {
        val virtuePrefix = when (virtue) {
            VirtueType.COURAGE -> "Unyielding"
            VirtueType.DILIGENCE -> "Adamantine"
            VirtueType.HUMILITY -> "Serene"
            VirtueType.CHARITY -> "Radiant"
            VirtueType.PATIENCE -> "Eternal"
            VirtueType.TEMPERANCE -> "Harmonious"
            VirtueType.GRATITUDE -> "Luminous"
        }

        val shadowSuffix = when (shadow) {
            ShadowType.PRIDE -> "Sovereign"
            ShadowType.GREED -> "Architect"
            ShadowType.ENVY -> "Catalyst"
            ShadowType.WRATH -> "Vanguard"
            ShadowType.GLUTTONY -> "Voyager"
            ShadowType.DESIRE -> "Enchanter"
            ShadowType.SLOTH -> "Arbiter"
        }

        return "$virtuePrefix $shadowSuffix of the $race"
    }

    private fun generatePoeticVerses(
        soul: SoulIdentity,
        dominantVirtue: VirtueType,
        dominantShadow: ShadowType,
        virtuePercent: Int,
        shadowPercent: Int
    ): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()

        // Specific force pairings
        when {
            dominantShadow == ShadowType.PRIDE && dominantVirtue == VirtueType.HUMILITY -> {
                list.add(
                    "You wear the sovereign crown not out of vanity, but as a sacred stewardship for the cosmos." to
                    "In kneeling before the eternal truth, your divine stature rises infinitely beyond mortal heights."
                )
                list.add(
                    "A paradox of supreme majesty: you command the astral tides yet listen to the quietest whisper of the void." to
                    "True divinity is forged where boundless pride surrenders willingly to gentle grace."
                )
            }
            dominantShadow == ShadowType.WRATH && dominantVirtue == VirtueType.COURAGE -> {
                list.add(
                    "Righteous lightning coils within your veins, transmuting primal wrath into an unbreachable aegis." to
                    "Where lesser souls shatter in fear, your warrior flame blazes as an eternal sanctuary for the innocent."
                )
                list.add(
                    "The celestial anvil rings beneath your stride; your fury is not destruction, but the holy hammer that reshapes destiny." to
                    "No abyssal tempest can extinguish a heart ignited by divine fortitude."
                )
            }
            dominantShadow == ShadowType.ENVY && dominantVirtue == VirtueType.DILIGENCE -> {
                list.add(
                    "The yearning to surpass every horizon has become the sacred catalyst for boundless metamorphosis." to
                    "Through relentless discipline and unwavering focus, you turn the bitter spark of rivalry into pure golden mastery."
                )
                list.add(
                    "You look upon the stars not with sorrow, but with the solemn oath to carve your own constellation across eternity." to
                    "Each labor refines your vessel, transmuting raw hunger into godly perfection."
                )
            }
            dominantShadow == ShadowType.GREED && dominantVirtue == VirtueType.CHARITY -> {
                list.add(
                    "You gather the celestial treasures of wisdom and power only to pour them out upon the barren worlds." to
                    "The insatiable hunger to acquire is sanctified through the infinite joy of unconditional bestowal."
                )
                list.add(
                    "An architect of boundless abundance, you hoard neither light nor gold, but expand the universe's sacred vault." to
                    "In giving everything away, your divine treasury remains forever overflowing."
                )
            }
            dominantShadow == ShadowType.SLOTH && dominantVirtue == VirtueType.PATIENCE -> {
                list.add(
                    "In the profound stillness of the cosmic ocean, you discover the immovable center of all creation." to
                    "While realms rage in fleeting panic, your silent meditation anchors the balance of celestial spheres."
                )
                list.add(
                    "You do not flee the world; you rest within the timeless breath of the infinite." to
                    "From absolute rest springs effortless power, moving mountains without the rustle of a leaf."
                )
            }
            dominantShadow == ShadowType.DESIRE && dominantVirtue == VirtueType.TEMPERANCE -> {
                list.add(
                    "Passions burn like wild nebulae within your chest, yet are steered with the immaculate precision of an archangel." to
                    "You taste the sweetest nectar of existence without ever losing the pristine clarity of your divine core."
                )
                list.add(
                    "Magnetic, alluring, yet utterly sovereign over every impulse; a vessel that celebrates beauty while standing inviolate." to
                    "The flame of desire warms the sanctuary, held safe within crystal walls of sacred balance."
                )
            }
            dominantShadow == ShadowType.GLUTTONY && dominantVirtue == VirtueType.GRATITUDE -> {
                list.add(
                    "Your spirit drinks deeply of every sensation, transmuting all cosmic experiences into hymns of holy reverence." to
                    "Nothing is wasted in your boundless awareness; every shadow and light is savored as divine communion."
                )
            }
        }

        // Generic Archetype / Balance Verses
        if (virtuePercent > 65) {
            list.add(
                "Bathed in the white-gold fires of heavenly virtue, your presence illuminates the labyrinth of the soul." to
                "Shadows do not perish in your gaze—they are redeemed, dissolving into the warm splendor of your sovereign dawn."
            )
        } else if (shadowPercent > 65) {
            list.add(
                "You walk the deep precipice of the abyss, commanding primordial shadow with an iron will." to
                "Where others succumb to the dark, you transmute the raw gravity of the void into sovereign, untamed power."
            )
        } else {
            list.add(
                "The sacred marriage of light and dark is realized within the crucible of your being." to
                "Neither angel nor demon alone, you stand as the Sovereign Transmuter, wielding duality as a single radiant staff."
            )
        }

        // Race-themed verse
        list.add(
            "As a transcendent ${soul.race}, your soul resonates with the ancient frequency of ${soul.resonanceFrequency}." to
            "The cosmic chronicle records your journey: ${soul.evolutionProgress}% awakened into the next epoch of existence."
        )

        return list
    }

    private fun generateCosmicVerdict(
        virtue: VirtueType,
        shadow: ShadowType,
        stability: Int,
        humanity: Int
    ): String {
        return when {
            stability < 50 -> "✦ Cosmic Verdict: Stabilize the internal vortex through meditative grounding and patience."
            humanity < 45 -> "✦ Cosmic Verdict: Divine ascension is soaring; tether your vessel with acts of compassionate communion."
            virtue == VirtueType.COURAGE -> "✦ Cosmic Verdict: The astral gates swing wide before your fearlessness. Advance without hesitation."
            virtue == VirtueType.HUMILITY -> "✦ Cosmic Verdict: Silent strength magnifies your aura. The oracle sees unclouded vision ahead."
            else -> "✦ Cosmic Verdict: Light and Shadow dance in harmonious cadence. Your godly identity is firmly anchored."
        }
    }
}
