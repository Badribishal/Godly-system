package com.example.data.engine

import com.example.data.local.EvaluationRecordEntity
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SoulResonanceData(
    val percentage: Int, // 0 to 100
    val tierLabel: String,
    val frequencyLabel: String,
    val resonanceInsight: String,
    val matchingTraitCount: Int,
    val totalRecentCount: Int
)

object SoulResonanceEngine {

    fun calculateResonance(
        soul: SoulIdentity,
        recentRecords: List<EvaluationRecordEntity>
    ): SoulResonanceData {
        if (recentRecords.isEmpty()) {
            // Baseline resonance based on humanity and stability
            val base = ((soul.humanity * 0.5f) + (soul.stability * 0.5f)).toInt().coerceIn(45, 80)
            return SoulResonanceData(
                percentage = base,
                tierLabel = "Equilibrium Baseline",
                frequencyLabel = "432 Hz • Astral Harmonics",
                resonanceInsight = "Vessel maintains serene baseline synchrony. Record daily trials to deepen harmonic alignment with ${soul.dominantVirtue.displayName}.",
                matchingTraitCount = 0,
                totalRecentCount = 0
            )
        }

        // Check recent records (up to 10)
        val window = recentRecords.take(10)
        var matchScore = 0
        var totalPoints = 0

        window.forEach { record ->
            totalPoints += 10
            var alignedThisRecord = false

            // Check virtue alignment
            if (record.primaryVirtue != null && record.primaryVirtue.equals(soul.dominantVirtue.name, ignoreCase = true)) {
                matchScore += 8
                alignedThisRecord = true
            } else if (record.primaryVirtue != null) {
                matchScore += 3
            }

            // Check shadow transmutation / alignment
            if (record.primaryShadow != null && record.primaryShadow.equals(soul.dominantShadow.name, ignoreCase = true)) {
                matchScore += 6
                alignedThisRecord = true
            } else if (record.primaryShadow != null) {
                matchScore += 2
            }

            // Humanity / reflection quality bonus
            if (record.reflection.isNotBlank() && record.reflection.length > 10) {
                matchScore += 2
            }
        }

        val rawRatio = if (totalPoints > 0) (matchScore.toFloat() / totalPoints.toFloat()) else 0.6f
        // Scale with vessel stability and humanity
        val stabilityFactor = (soul.stability.toFloat() / 100f).coerceIn(0.7f, 1.2f)
        val calculatedPercent = ((rawRatio * 75f + (soul.humanity * 0.25f)) * stabilityFactor)
            .toInt()
            .coerceIn(15, 99)

        val (tier, freq, insight) = when {
            calculatedPercent >= 88 -> Triple(
                "Aetheric Super-Resonance",
                "963 Hz • Divine Crown",
                "Actions are in supreme alignment with your ${soul.dominantVirtue.displayName} virtue and ${soul.archetype} matrix."
            )
            calculatedPercent >= 70 -> Triple(
                "Harmonic Synchrony",
                "741 Hz • Awakening Tone",
                "Strong harmonic flow. Your daily conduct actively feeds your vessel's spiritual evolution."
            )
            calculatedPercent >= 50 -> Triple(
                "Tempered Equilibrium",
                "528 Hz • Miraculous Harmony",
                "Steady resonance with minor elemental deviations. Cultivating ${soul.dominantVirtue.displayName} will bridge the gap."
            )
            calculatedPercent >= 35 -> Triple(
                "Fluctuating Resonance",
                "432 Hz • Astral Waves",
                "Recent choices exhibit slight dissonance with your dominant archetype forces."
            )
            else -> Triple(
                "Turbulent Dissonance",
                "396 Hz • Grounding Frequency",
                "Forces are conflicting with current vessel alignment. Transmute chaotic impulses into disciplined virtue."
            )
        }

        return SoulResonanceData(
            percentage = calculatedPercent,
            tierLabel = tier,
            frequencyLabel = freq,
            resonanceInsight = insight,
            matchingTraitCount = (matchScore / 6).coerceAtMost(window.size),
            totalRecentCount = window.size
        )
    }
}
