package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.DailyFantasyArchetypeResult
import com.example.data.engine.FantasyArchetypeEvaluationEngine
import com.example.data.local.AppDatabase
import com.example.data.local.DailyEmotionRecordEntity
import com.example.data.local.TrackedEmotionEntity
import com.example.data.model.EmotionCatalog
import com.example.data.model.EmotionItem
import com.example.data.model.EmotionValence
import com.example.data.repository.SoulRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EmotionTrackingUiState(
    val positiveEmotions: List<EmotionItem> = EmotionCatalog.TRACKED_POSITIVE_21,
    val negativeEmotions: List<EmotionItem> = EmotionCatalog.TRACKED_NEGATIVE_21,
    val allEmotions: List<EmotionItem> = EmotionCatalog.TRACKED_42_EMOTIONS,
    val selectedPositive: Set<String> = emptySet(),
    val selectedNegative: Set<String> = emptySet(),
    val activeCategoryFilter: String? = null,
    val searchQuery: String = "",
    val activeArchetypePreview: DailyFantasyArchetypeResult = FantasyArchetypeEvaluationEngine.evaluateDailyArchetype(emptySet(), emptySet()),
    val isSaving: Boolean = false,
    val lastRecordedArchetype: DailyFantasyArchetypeResult? = null,
    val feedbackMessage: String? = null
)

class EmotionTrackingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SoulRepository

    private val _selectedPositive = MutableStateFlow<Set<String>>(emptySet())
    val selectedPositive: StateFlow<Set<String>> = _selectedPositive.asStateFlow()

    private val _selectedNegative = MutableStateFlow<Set<String>>(emptySet())
    val selectedNegative: StateFlow<Set<String>> = _selectedNegative.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeCategoryFilter = MutableStateFlow<String?>(null)
    val activeCategoryFilter: StateFlow<String?> = _activeCategoryFilter.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _lastRecordedArchetype = MutableStateFlow<DailyFantasyArchetypeResult?>(null)
    val lastRecordedArchetype: StateFlow<DailyFantasyArchetypeResult?> = _lastRecordedArchetype.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    val allDailyEmotionRecords: StateFlow<List<DailyEmotionRecordEntity>>
    val allTrackedEmotionsFromDb: StateFlow<List<TrackedEmotionEntity>>

    val uiState: StateFlow<EmotionTrackingUiState>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = SoulRepository(database.soulDao())

        viewModelScope.launch {
            repository.initializeIfEmpty()
        }

        allDailyEmotionRecords = repository.allDailyEmotionRecordsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allTrackedEmotionsFromDb = repository.allTrackedEmotionsFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        val selectionAndFilterFlow = combine(
            _selectedPositive,
            _selectedNegative,
            _searchQuery,
            _activeCategoryFilter
        ) { pos, neg, query, cat ->
            val preview = FantasyArchetypeEvaluationEngine.evaluateDailyArchetype(pos, neg)
            Tuple4(pos, neg, query, cat, preview)
        }

        val statusFlow = combine(
            _isSaving,
            _lastRecordedArchetype,
            _feedbackMessage
        ) { saving, lastArch, msg ->
            Triple(saving, lastArch, msg)
        }

        uiState = combine(selectionAndFilterFlow, statusFlow) { sf, st ->
            EmotionTrackingUiState(
                positiveEmotions = EmotionCatalog.TRACKED_POSITIVE_21,
                negativeEmotions = EmotionCatalog.TRACKED_NEGATIVE_21,
                allEmotions = EmotionCatalog.TRACKED_42_EMOTIONS,
                selectedPositive = sf.pos,
                selectedNegative = sf.neg,
                searchQuery = sf.query,
                activeCategoryFilter = sf.cat,
                activeArchetypePreview = sf.preview,
                isSaving = st.first,
                lastRecordedArchetype = st.second,
                feedbackMessage = st.third
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmotionTrackingUiState()
        )
    }

    private data class Tuple4(
        val pos: Set<String>,
        val neg: Set<String>,
        val query: String,
        val cat: String?,
        val preview: DailyFantasyArchetypeResult
    )

    fun toggleEmotion(emotion: EmotionItem) {
        if (emotion.valence == EmotionValence.POSITIVE) {
            val current = _selectedPositive.value
            _selectedPositive.value = if (current.contains(emotion.name)) {
                current - emotion.name
            } else {
                current + emotion.name
            }
        } else {
            val current = _selectedNegative.value
            _selectedNegative.value = if (current.contains(emotion.name)) {
                current - emotion.name
            } else {
                current + emotion.name
            }
        }
    }

    fun togglePositive(name: String) {
        val current = _selectedPositive.value
        _selectedPositive.value = if (current.contains(name)) current - name else current + name
    }

    fun toggleNegative(name: String) {
        val current = _selectedNegative.value
        _selectedNegative.value = if (current.contains(name)) current - name else current + name
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoryFilter(category: String?) {
        _activeCategoryFilter.value = category
    }

    fun clearSelection() {
        _selectedPositive.value = emptySet()
        _selectedNegative.value = emptySet()
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    fun recordDailyEmotions(userNote: String = "", onFinished: (DailyFantasyArchetypeResult) -> Unit = {}) {
        val pos = _selectedPositive.value
        val neg = _selectedNegative.value
        if (pos.isEmpty() && neg.isEmpty()) {
            _feedbackMessage.value = "Select at least one emotion to manifest your Daily Fantasy Archetype."
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val (record, archetype) = repository.recordDailyEmotions(pos, neg, userNote)
                _lastRecordedArchetype.value = archetype
                _feedbackMessage.value = "✨ Manifested Daily Archetype: ${archetype.name} (${archetype.title})!"
                onFinished(archetype)
            } catch (e: Exception) {
                _feedbackMessage.value = "Evaluation Error: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun deleteDailyRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteEmotionRecord(id)
        }
    }
}
