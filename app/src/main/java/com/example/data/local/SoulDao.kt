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
}
