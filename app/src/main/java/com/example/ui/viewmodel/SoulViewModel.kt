package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.SoulResonanceData
import com.example.data.engine.SoulResonanceEngine
import com.example.data.local.AppDatabase
import com.example.data.local.DailyTrialEntity
import com.example.data.local.EvaluationDraftEntity
import com.example.data.local.EvaluationRecordEntity
import com.example.data.local.EvolutionEventEntity
import com.example.data.model.Achievement
import com.example.data.model.CosmeticCatalog
import com.example.data.model.CosmeticEffect
import com.example.data.model.EvaluationResult
import com.example.data.model.RecordInput
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import com.example.data.repository.SoulRepository
import com.example.ui.components.AstralEmotionalState
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.RarePalette
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class ScreenTab {
    MAIN,
    RECORD,
    SOUL,
    REFLECTION,
    HISTORY
}

enum class ExportFormat(val extension: String, val displayName: String, val mimeType: String, val defaultFilename: String, val isBinary: Boolean = false) {
    JSON("json", "JSON (.json)", "application/json", "godly_vessel_backup.json", false),
    PDF("pdf", "PDF Document (.pdf)", "application/pdf", "godly_soul_history.pdf", true),
    CSV("csv", "CSV Records (.csv)", "text/csv", "godly_soul_records.csv", false),
    MARKDOWN("md", "Markdown (.md)", "text/markdown", "godly_soul_chronicle.md", false),
    PLAIN_TEXT("txt", "Plain Text (.txt)", "text/plain", "godly_vessel_dossier.txt", false)
}

data class RecordFormState(
    val selectedEmotion: String = "Equilibrium",
    val primaryShadow: ShadowType? = null,
    val primaryVirtue: VirtueType? = null,
    val selectedShadows: Set<ShadowType> = emptySet(),
    val selectedVirtues: Set<VirtueType> = emptySet(),
    val intensityMultiplier: Float = 1.0f,
    val situation: String = "",
    val intention: String = "",
    val action: String = "",
    val consequence: String = "",
    val reflection: String = "",
    val isSubmitting: Boolean = false
)

data class DailyLoginRewardState(
    val streakDay: Int = 1,
    val isClaimedToday: Boolean = false,
    val todayRewardShards: Int = 30,
    val rewardsList: List<Int> = listOf(30, 45, 60, 80, 110, 150, 250),
    val totalClaimedCount: Int = 0
)

class SoulViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SoulRepository
    private val prefs = application.getSharedPreferences("godly_system_prefs", Context.MODE_PRIVATE)

    // Navigation State
    private val _currentTab = MutableStateFlow(ScreenTab.MAIN)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Dialog & Modal States
    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    private val _showAchievementsDialog = MutableStateFlow(false)
    val showAchievementsDialog: StateFlow<Boolean> = _showAchievementsDialog.asStateFlow()

    private val _showWardrobeDialog = MutableStateFlow(false)
    val showWardrobeDialog: StateFlow<Boolean> = _showWardrobeDialog.asStateFlow()

    private val _showAwakeningModal = MutableStateFlow(false)
    val showAwakeningModal: StateFlow<Boolean> = _showAwakeningModal.asStateFlow()

    // Theme & Minimal Palette State
    private val _themeMode = MutableStateFlow(
        AppThemeMode.valueOf(prefs.getString("theme_mode", AppThemeMode.DARK.name) ?: AppThemeMode.DARK.name)
    )
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _rarePalette = MutableStateFlow(
        try {
            RarePalette.valueOf(prefs.getString("rare_palette", RarePalette.CELESTIAL_TWILIGHT.name) ?: RarePalette.CELESTIAL_TWILIGHT.name)
        } catch (e: Exception) {
            RarePalette.CELESTIAL_TWILIGHT
        }
    )
    val rarePalette: StateFlow<RarePalette> = _rarePalette.asStateFlow()

    // Core Data Flows
    val soulProfile: StateFlow<SoulIdentity>
    val records: StateFlow<List<EvaluationRecordEntity>>
    val evolutionEvents: StateFlow<List<EvolutionEventEntity>>
    val dailyTrials: StateFlow<List<DailyTrialEntity>>
    val soulResonance: StateFlow<SoulResonanceData>

    // Record Evaluation Form State
    private val _recordFormState = MutableStateFlow(RecordFormState())
    val recordFormState: StateFlow<RecordFormState> = _recordFormState.asStateFlow()

    // Latest Transmutation Outcome
    private val _lastEvaluationResult = MutableStateFlow<EvaluationResult?>(null)
    val lastEvaluationResult: StateFlow<EvaluationResult?> = _lastEvaluationResult.asStateFlow()

    // Achievements State
    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    // Daily Login State (Integrated in Achievements)
    private val _dailyLoginState = MutableStateFlow(computeDailyLoginState())
    val dailyLoginState: StateFlow<DailyLoginRewardState> = _dailyLoginState.asStateFlow()

    // Status Message for Import / Export
    private val _systemToast = MutableStateFlow<String?>(null)
    val systemToast: StateFlow<String?> = _systemToast.asStateFlow()

    // Daily Emotional Check-in State
    private val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    private val _checkedInEmotion = MutableStateFlow<String?>(
        if (prefs.getString("last_checked_in_date", "") == todayDateStr) {
            prefs.getString("last_checked_in_emotion", null)
        } else null
    )
    val checkedInEmotion: StateFlow<String?> = _checkedInEmotion.asStateFlow()

    private val _isCheckedInToday = MutableStateFlow(
        prefs.getString("last_checked_in_date", "") == todayDateStr
    )
    val isCheckedInToday: StateFlow<Boolean> = _isCheckedInToday.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = SoulRepository(database.soulDao())

        soulProfile = repository.soulProfileFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SoulIdentity.initial()
        )

        records = repository.allRecordsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        evolutionEvents = repository.allEventsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        dailyTrials = repository.allTrialsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        soulResonance = combine(soulProfile, records) { soul, recs ->
            SoulResonanceEngine.calculateResonance(soul, recs)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            SoulResonanceEngine.calculateResonance(SoulIdentity.initial(), emptyList())
        )

        viewModelScope.launch {
            repository.initializeIfEmpty()
            refreshAchievements()
            // Restore evaluation draft if previously saved before app close
            val savedDraft = repository.getEvaluationDraft()
            if (savedDraft != null) {
                val restoredShadows = savedDraft.primaryShadow?.split(",")?.mapNotNull { name ->
                    runCatching { ShadowType.valueOf(name.trim()) }.getOrNull()
                }?.toSet() ?: emptySet()
                val restoredVirtues = savedDraft.primaryVirtue?.split(",")?.mapNotNull { name ->
                    runCatching { VirtueType.valueOf(name.trim()) }.getOrNull()
                }?.toSet() ?: emptySet()

                _recordFormState.value = RecordFormState(
                    selectedEmotion = savedDraft.emotion,
                    primaryShadow = restoredShadows.firstOrNull(),
                    primaryVirtue = restoredVirtues.firstOrNull(),
                    selectedShadows = restoredShadows,
                    selectedVirtues = restoredVirtues,
                    situation = savedDraft.situation,
                    intention = savedDraft.intention,
                    action = savedDraft.action,
                    consequence = savedDraft.consequence,
                    reflection = savedDraft.reflection,
                    isSubmitting = false
                )
            }
        }
    }

    fun setTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun openSettings() {
        _showSettingsDialog.value = true
    }

    fun closeSettings() {
        _showSettingsDialog.value = false
    }

    fun openAchievements() {
        viewModelScope.launch {
            refreshAchievements()
            _showAchievementsDialog.value = true
        }
    }

    fun closeAchievements() {
        _showAchievementsDialog.value = false
    }

    fun openWardrobe() {
        _showWardrobeDialog.value = true
    }

    fun closeWardrobe() {
        _showWardrobeDialog.value = false
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("theme_mode", mode.name).apply()
    }

    fun setRarePalette(palette: RarePalette) {
        _rarePalette.value = palette
        prefs.edit().putString("rare_palette", palette.name).apply()
    }

    fun refreshAchievements() {
        _dailyLoginState.value = computeDailyLoginState()
        viewModelScope.launch {
            _achievements.value = repository.computeAchievements()
        }
    }

    private fun computeDailyLoginState(): DailyLoginRewardState {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastClaimDate = prefs.getString("last_daily_login_date", "") ?: ""
        val streak = prefs.getInt("daily_login_streak", 1).coerceIn(1, 7)
        val totalClaimed = prefs.getInt("total_daily_logins_claimed", 0)
        val rewards = listOf(30, 45, 60, 80, 110, 150, 250)

        val isClaimed = lastClaimDate == todayStr
        val activeDay = if (isClaimed) {
            streak
        } else {
            val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
            val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(yesterdayCal.time)
            if (lastClaimDate == yesterdayStr) {
                if (streak >= 7) 1 else streak + 1
            } else if (lastClaimDate.isEmpty()) {
                1
            } else {
                1 // Missed days resets back to day 1
            }
        }

        val reward = rewards[(activeDay - 1).coerceIn(0, 6)]
        return DailyLoginRewardState(
            streakDay = activeDay,
            isClaimedToday = isClaimed,
            todayRewardShards = reward,
            rewardsList = rewards,
            totalClaimedCount = totalClaimed
        )
    }

    fun claimDailyLoginReward() {
        val state = _dailyLoginState.value
        if (state.isClaimedToday) return

        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val reward = state.todayRewardShards
        val newTotal = state.totalClaimedCount + 1

        prefs.edit()
            .putString("last_daily_login_date", todayStr)
            .putInt("daily_login_streak", state.streakDay)
            .putInt("total_daily_logins_claimed", newTotal)
            .apply()

        _dailyLoginState.value = state.copy(
            isClaimedToday = true,
            totalClaimedCount = newTotal
        )

        viewModelScope.launch {
            repository.addSoulShards(reward)
            _systemToast.value = "Daily Resonance Claimed: +$reward Soul Shards (Day ${state.streakDay}/7)!"
            refreshAchievements()
        }
    }

    fun claimAchievementReward(achievementId: String) {
        viewModelScope.launch {
            val rewarded = repository.claimAchievement(achievementId)
            if (rewarded > 0) {
                _systemToast.value = "Claimed +$rewarded Soul Shards!"
                refreshAchievements()
            }
        }
    }

    fun unlockCosmeticEffect(effect: CosmeticEffect) {
        viewModelScope.launch {
            val success = repository.unlockCosmetic(effect.id, effect.cost)
            if (success) {
                _systemToast.value = "Unlocked & Equipped: ${effect.name}!"
                refreshAchievements()
            } else {
                _systemToast.value = "Insufficient Soul Shards."
            }
        }
    }

    fun equipCosmeticEffect(effectId: String) {
        viewModelScope.launch {
            val success = repository.equipCosmetic(effectId)
            if (success) {
                val effect = CosmeticCatalog.getEffectById(effectId)
                _systemToast.value = "Equipped ${effect.name}!"
            }
        }
    }

    fun clearSystemToast() {
        _systemToast.value = null
    }

    fun performDailyEmotionalCheckIn(state: AstralEmotionalState) {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        prefs.edit()
            .putString("last_checked_in_date", todayStr)
            .putString("last_checked_in_emotion", state.name)
            .apply()

        _checkedInEmotion.value = state.name
        _isCheckedInToday.value = true

        viewModelScope.launch {
            repository.addSoulShards(25)
            _systemToast.value = "Calibrated with ${state.name} (${state.resonanceAffinity}): +25 💎 Shards & Resonance Tuned!"
            refreshAchievements()
        }
    }

    fun exportData(format: ExportFormat = ExportFormat.JSON, onReady: (String) -> Unit) {
        viewModelScope.launch {
            val content = when (format) {
                ExportFormat.JSON -> repository.exportJson()
                ExportFormat.MARKDOWN -> repository.exportMarkdown()
                ExportFormat.PLAIN_TEXT -> repository.exportPlainText()
                ExportFormat.CSV -> repository.exportCsv()
                ExportFormat.PDF -> repository.exportCsv() // Handled via binary exportPdf for PDF
            }
            onReady(content)
        }
    }

    fun exportPdf(onReady: (ByteArray) -> Unit) {
        viewModelScope.launch {
            val bytes = repository.exportPdf()
            onReady(bytes)
        }
    }

    fun writePdfToStream(outputStream: OutputStream) {
        viewModelScope.launch {
            repository.writePdfToStream(outputStream)
        }
    }

    fun importData(contentString: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = repository.importAny(contentString)
            result.onSuccess { msg ->
                refreshAchievements()
                _systemToast.value = "Vessel Restored Successfully"
                onResult(true, msg)
            }.onFailure { err ->
                onResult(false, err.message ?: "Failed to parse data matrix.")
            }
        }
    }

    fun updateRecordForm(update: (RecordFormState) -> RecordFormState) {
        val newState = update(_recordFormState.value)
        _recordFormState.value = newState
        persistDraft(newState)
    }

    private fun persistDraft(state: RecordFormState) {
        viewModelScope.launch {
            val allShadows = if (state.selectedShadows.isNotEmpty()) state.selectedShadows else listOfNotNull(state.primaryShadow).toSet()
            val allVirtues = if (state.selectedVirtues.isNotEmpty()) state.selectedVirtues else listOfNotNull(state.primaryVirtue).toSet()

            repository.saveEvaluationDraft(
                EvaluationDraftEntity(
                    id = 1,
                    emotion = state.selectedEmotion,
                    primaryShadow = if (allShadows.isNotEmpty()) allShadows.joinToString(",") { it.name } else null,
                    primaryVirtue = if (allVirtues.isNotEmpty()) allVirtues.joinToString(",") { it.name } else null,
                    situation = state.situation,
                    intention = state.intention,
                    action = state.action,
                    consequence = state.consequence,
                    reflection = state.reflection,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }

    fun submitRecord() {
        val state = _recordFormState.value
        val allShadows = if (state.selectedShadows.isNotEmpty()) state.selectedShadows else listOfNotNull(state.primaryShadow).toSet()
        val allVirtues = if (state.selectedVirtues.isNotEmpty()) state.selectedVirtues else listOfNotNull(state.primaryVirtue).toSet()

        val shadowNames = allShadows.joinToString(", ") { it.displayName }
        val virtueNames = allVirtues.joinToString(", ") { it.displayName }

        val effectiveEmotion = if (state.selectedEmotion.isNotBlank() && state.selectedEmotion != "Equilibrium") {
            state.selectedEmotion
        } else if (shadowNames.isNotBlank() && virtueNames.isNotBlank()) {
            "$shadowNames & $virtueNames"
        } else if (shadowNames.isNotBlank()) {
            shadowNames
        } else if (virtueNames.isNotBlank()) {
            virtueNames
        } else {
            "Conscious Alignment"
        }

        viewModelScope.launch {
            _recordFormState.value = state.copy(isSubmitting = true)

            val input = RecordInput(
                emotion = effectiveEmotion,
                primaryShadow = allShadows.firstOrNull(),
                primaryVirtue = allVirtues.firstOrNull(),
                situation = state.situation.ifBlank {
                    if (shadowNames.isNotBlank() && virtueNames.isNotBlank()) {
                        "Catalyzed by [$shadowNames] and [$virtueNames]"
                    } else if (shadowNames.isNotBlank()) {
                        "Transmuting shadow forces: $shadowNames"
                    } else if (virtueNames.isNotBlank()) {
                        "Cultivating sacred virtues: $virtueNames"
                    } else {
                        "Conscious alignment of inner forces"
                    }
                },
                intention = state.intention.ifBlank { "Transmuted cosmic forces into conscious growth" },
                action = state.action.ifBlank { "Balanced dualities within the soul matrix" },
                consequence = state.consequence.ifBlank { "Internal equilibrium heightened" },
                reflection = state.reflection.ifBlank { "Vessel harmonics refined through experience." }
            )

            val result = repository.recordEvaluation(input)
            _lastEvaluationResult.value = result
            _showAwakeningModal.value = true

            // Clear draft from Room database and reset form
            repository.clearEvaluationDraft()
            _recordFormState.value = RecordFormState()
            refreshAchievements()
        }
    }

    fun dismissAwakeningModal() {
        _showAwakeningModal.value = false
    }

    fun submitTrial(trial: DailyTrialEntity, selectedOptionIndex: Int, reflection: String) {
        viewModelScope.launch {
            repository.completeTrial(trial, selectedOptionIndex, reflection)
            refreshAchievements()
        }
    }

    fun selectPresetEmotion(emotion: String, shadow: ShadowType?, virtue: VirtueType?) {
        val newState = _recordFormState.value.copy(
            selectedEmotion = emotion,
            primaryShadow = shadow,
            primaryVirtue = virtue,
            selectedShadows = if (shadow != null) setOf(shadow) else emptySet(),
            selectedVirtues = if (virtue != null) setOf(virtue) else emptySet()
        )
        _recordFormState.value = newState
        persistDraft(newState)
    }
}
