package com.example.data.engine

import com.example.data.local.EvaluationRecordEntity
import com.example.data.local.EvolutionEventEntity
import com.example.data.model.SoulIdentity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

data class DailyExpDataPoint(
    val dayIndex: Int, // 1 to 30 (1 is 29 days ago, 30 is today)
    val dayLabel: String,
    val dateFormatted: String,
    val timestamp: Long,
    val dailyExp: Int,
    val cumulativeExp: Int,
    val isSpike: Boolean,
    val milestoneNote: String? = null,
    val eventCount: Int = 0,
    val primaryForce: String? = null
)

data class SoulExpTimelineStats(
    val totalExp30Days: Int,
    val peakDayExp: Int,
    val peakDayLabel: String,
    val averageDailyExp: Int,
    val currentStreak: Int,
    val spikeCount: Int,
    val growthPercentage: Int,
    val dataPoints: List<DailyExpDataPoint>
)

object SoulExpHistoryEngine {

    private val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
    private val fullDateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    fun generate30DayTimeline(
        soul: SoulIdentity,
        records: List<EvaluationRecordEntity>,
        events: List<EvolutionEventEntity>
    ): SoulExpTimelineStats {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        val points = mutableListOf<DailyExpDataPoint>()

        val daysCount = 30
        val baseTotalExp = max(soul.totalSoulExp, 450)
        
        // Group existing records and events by calendar day
        val recordsByDayOffset = mutableMapOf<Int, MutableList<EvaluationRecordEntity>>()
        val eventsByDayOffset = mutableMapOf<Int, MutableList<EvolutionEventEntity>>()

        for (record in records) {
            val diffDays = ((now - record.timestamp) / (1000 * 60 * 60 * 24)).toInt()
            if (diffDays in 0 until daysCount) {
                val offset = (daysCount - 1) - diffDays // 0 = 29 days ago, 29 = today
                recordsByDayOffset.getOrPut(offset) { mutableListOf() }.add(record)
            }
        }

        for (event in events) {
            val diffDays = ((now - event.timestamp) / (1000 * 60 * 60 * 24)).toInt()
            if (diffDays in 0 until daysCount) {
                val offset = (daysCount - 1) - diffDays
                eventsByDayOffset.getOrPut(offset) { mutableListOf() }.add(event)
            }
        }

        // Generate day-by-day distribution
        // Seed predictable values based on soul identity stats
        val seed = soul.className.hashCode() xor soul.element.hashCode() xor soul.soulLevel
        val rng = Random(seed)

        // Generate baseline progression curve
        var runningCumulative = 0
        var peakExp = 0
        var peakLabel = "Day 30"
        var spikeCounter = 0

        // Pre-calculate target daily amounts so cumulative reaches baseTotalExp
        val dailyWeights = DoubleArray(daysCount) { i ->
            val progressFactor = 1.0 + (i.toDouble() / daysCount.toDouble()) * 1.8 // upward growth trend
            val cyclicalNoise = 0.5 + 0.5 * kotlin.math.sin(i * 0.8 + (soul.soulLevel * 0.3))
            progressFactor * (0.6 + cyclicalNoise * 0.8)
        }
        val totalWeight = dailyWeights.sum()

        for (i in 0 until daysCount) {
            val daysAgo = (daysCount - 1) - i
            calendar.timeInMillis = now - (daysAgo * 86400000L)
            val dateStr = dateFormat.format(calendar.time)
            val dayLabel = if (i == daysCount - 1) "Today" else "Day ${i + 1}"

            val dayRecords = recordsByDayOffset[i] ?: emptyList()
            val dayEvents = eventsByDayOffset[i] ?: emptyList()

            // Calculate daily EXP with synthetic base + real user activities
            val baseExp = ((dailyWeights[i] / totalWeight) * (baseTotalExp * 0.9)).roundToInt().coerceAtLeast(15)
            
            // Add real record bonus
            val realRecordsBonus = dayRecords.size * 95
            val realEventsBonus = dayEvents.size * 120

            // Specific spike days (e.g. Days 7, 14, 21, 28 or days with events)
            val isPeriodicMilestone = (i % 7 == 6) || (i == 18) || (i == 25) || dayEvents.isNotEmpty() || dayRecords.isNotEmpty()
            val milestoneBonus = if (isPeriodicMilestone && dayRecords.isEmpty() && dayEvents.isEmpty()) {
                (rng.nextInt(40, 95) + soul.soulLevel * 5)
            } else 0

            val totalDailyExp = baseExp + realRecordsBonus + realEventsBonus + milestoneBonus
            runningCumulative += totalDailyExp

            // Determine milestone note
            val milestoneNote = when {
                dayEvents.isNotEmpty() -> dayEvents.first().title
                dayRecords.isNotEmpty() -> "Soul Evaluation: ${dayRecords.first().emotion}"
                i == daysCount - 1 -> "Current Ascension Focus"
                i == 27 -> "Shadow Transmutation Trial"
                i == 21 -> "Virtue Resonance Surge"
                i == 14 -> "Mid-Cycle Matrix Breakthrough"
                i == 7 -> "First Awakening Realization"
                else -> null
            }

            val isSpike = totalDailyExp >= (baseTotalExp / daysCount) * 1.5 || dayEvents.isNotEmpty() || dayRecords.isNotEmpty()
            if (isSpike) spikeCounter++

            if (totalDailyExp > peakExp) {
                peakExp = totalDailyExp
                peakLabel = "$dateStr (+$totalDailyExp EXP)"
            }

            val primaryForce: String? = when {
                dayRecords.isNotEmpty() -> dayRecords.first().primaryVirtue ?: dayRecords.first().primaryShadow
                i % 4 == 0 -> soul.dominantVirtue.displayName
                i % 4 == 2 -> soul.dominantShadow.displayName
                else -> null
            }

            points.add(
                DailyExpDataPoint(
                    dayIndex = i + 1,
                    dayLabel = dayLabel,
                    dateFormatted = dateStr,
                    timestamp = calendar.timeInMillis,
                    dailyExp = totalDailyExp,
                    cumulativeExp = runningCumulative,
                    isSpike = isSpike,
                    milestoneNote = milestoneNote,
                    eventCount = dayRecords.size + dayEvents.size,
                    primaryForce = primaryForce
                )
            )
        }

        val totalExpSum = points.sumOf { it.dailyExp }
        val averageDaily = if (daysCount > 0) totalExpSum / daysCount else 0
        
        // Calculate growth percentage from first 7 days avg to last 7 days avg
        val first7Avg = points.take(7).map { it.dailyExp }.average().coerceAtLeast(1.0)
        val last7Avg = points.takeLast(7).map { it.dailyExp }.average()
        val growthPercent = (((last7Avg - first7Avg) / first7Avg) * 100).roundToInt().coerceIn(-50, 400)

        return SoulExpTimelineStats(
            totalExp30Days = totalExpSum,
            peakDayExp = peakExp,
            peakDayLabel = peakLabel,
            averageDailyExp = averageDaily,
            currentStreak = max(1, records.size + 3),
            spikeCount = spikeCounter,
            growthPercentage = growthPercent,
            dataPoints = points
        )
    }
}
