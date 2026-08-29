package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SoulDao {
    // Soul Profile
    @Query("SELECT * FROM soul_profile WHERE id = 1")
    fun getSoulProfileFlow(): Flow<SoulProfileEntity?>

    @Query("SELECT * FROM soul_profile WHERE id = 1")
    suspend fun getSoulProfile(): SoulProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: SoulProfileEntity)

    // Evaluation Records
    @Query("SELECT * FROM evaluation_records ORDER BY timestamp DESC")
    fun getAllRecordsFlow(): Flow<List<EvaluationRecordEntity>>

    @Query("SELECT * FROM evaluation_records ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentRecords(limit: Int): List<EvaluationRecordEntity>

    @Query("SELECT COUNT(*) FROM evaluation_records")
    suspend fun getRecordCount(): Int

    @Insert
    suspend fun insertRecord(record: EvaluationRecordEntity): Long

    // Evolution Events Timeline
    @Query("SELECT * FROM evolution_events ORDER BY timestamp DESC")
    fun getAllEvolutionEventsFlow(): Flow<List<EvolutionEventEntity>>

    @Query("SELECT * FROM evolution_events ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentEvents(limit: Int): List<EvolutionEventEntity>

    @Insert
    suspend fun insertEvolutionEvent(event: EvolutionEventEntity): Long

    // Daily Trials
    @Query("SELECT * FROM daily_trials ORDER BY id ASC")
    fun getAllTrialsFlow(): Flow<List<DailyTrialEntity>>

    @Query("SELECT * FROM daily_trials WHERE completedTimestamp IS NULL LIMIT 1")
    suspend fun getNextUncompletedTrial(): DailyTrialEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrials(trials: List<DailyTrialEntity>)

    @Update
    suspend fun updateTrial(trial: DailyTrialEntity)

    // Evaluation Draft Progress
    @Query("SELECT * FROM evaluation_draft WHERE id = 1")
    fun getEvaluationDraftFlow(): Flow<EvaluationDraftEntity?>

    @Query("SELECT * FROM evaluation_draft WHERE id = 1")
    suspend fun getEvaluationDraft(): EvaluationDraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvaluationDraft(draft: EvaluationDraftEntity)

    @Query("DELETE FROM evaluation_draft WHERE id = 1")
    suspend fun clearEvaluationDraft()

    // Daily Emotion Records & Daily Fantasy Archetypes
    @Query("SELECT * FROM daily_emotion_records ORDER BY timestamp DESC")
    fun getAllEmotionRecordsFlow(): Flow<List<DailyEmotionRecordEntity>>

    @Query("SELECT * FROM daily_emotion_records WHERE dateKey = :dateKey ORDER BY timestamp DESC LIMIT 1")
    fun getEmotionRecordForDateFlow(dateKey: String): Flow<DailyEmotionRecordEntity?>

    @Query("SELECT * FROM daily_emotion_records WHERE dateKey = :dateKey ORDER BY timestamp DESC LIMIT 1")
    suspend fun getEmotionRecordForDate(dateKey: String): DailyEmotionRecordEntity?

    @Query("SELECT * FROM daily_emotion_records ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentEmotionRecords(limit: Int): List<DailyEmotionRecordEntity>

    @Query("SELECT COUNT(*) FROM daily_emotion_records")
    suspend fun getEmotionRecordCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmotionRecord(record: DailyEmotionRecordEntity): Long

    @Query("DELETE FROM daily_emotion_records WHERE id = :id")
    suspend fun deleteEmotionRecordById(id: Long)

    // Tracked 42 Emotions Catalog Persistence
    @Query("SELECT * FROM tracked_emotions ORDER BY name ASC")
    fun getAllTrackedEmotionsFlow(): Flow<List<TrackedEmotionEntity>>

    @Query("SELECT * FROM tracked_emotions WHERE valence = :valence ORDER BY name ASC")
    fun getTrackedEmotionsByValenceFlow(valence: String): Flow<List<TrackedEmotionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedEmotions(emotions: List<TrackedEmotionEntity>)

    @Query("UPDATE tracked_emotions SET usageCount = usageCount + 1, lastUsedTimestamp = :timestamp WHERE id = :emotionId OR name = :emotionId")
    suspend fun incrementEmotionUsage(emotionId: String, timestamp: Long = System.currentTimeMillis())
}
