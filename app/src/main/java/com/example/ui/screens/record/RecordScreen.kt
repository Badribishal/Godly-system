package com.example.ui.screens.record

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.engine.DailyFantasyArchetypeResult
import com.example.data.engine.FantasyArchetypeEvaluationEngine
import com.example.data.model.EmotionCatalog
import com.example.data.model.EmotionItem
import com.example.data.model.EmotionValence
import com.example.data.model.ShadowType
import com.example.data.model.VirtueType
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RecordFormState

/**
 * Enhanced Archetypal Record Menu with 21+ Positive Emotions, 21+ Negative Emotions,
 * and the Seven Deadly Sins & Seven Heavenly Virtues.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecordScreen(
    formState: RecordFormState,
    onFormUpdate: ((RecordFormState) -> RecordFormState) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var selectedCategoryTab by remember { mutableIntStateOf(0) } // 0: All, 1: Positive, 2: Negative, 3: Sins & Virtues
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubCategory by remember { mutableStateOf("All") }
    var showContextFields by remember { mutableStateOf(false) }

    val selectedShadows = remember(formState.selectedShadows, formState.primaryShadow) {
        if (formState.selectedShadows.isNotEmpty()) {
            formState.selectedShadows
        } else {
            listOfNotNull(formState.primaryShadow).toSet()
        }
    }

    val selectedVirtues = remember(formState.selectedVirtues, formState.primaryVirtue) {
        if (formState.selectedVirtues.isNotEmpty()) {
            formState.selectedVirtues
        } else {
            listOfNotNull(formState.primaryVirtue).toSet()
        }
    }

    val selectedEmotions = formState.selectedEmotions

    // Filtered emotions lists
    val filteredPositive = remember(searchQuery, selectedSubCategory) {
        EmotionCatalog.POSITIVE_EMOTIONS.filter { emotion ->
            val matchesSearch = searchQuery.isBlank() ||
                    emotion.name.contains(searchQuery, ignoreCase = true) ||
                    emotion.essence.contains(searchQuery, ignoreCase = true) ||
                    emotion.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedSubCategory == "All" || emotion.category == selectedSubCategory
            matchesSearch && matchesCategory
        }
    }

    val filteredNegative = remember(searchQuery, selectedSubCategory) {
        EmotionCatalog.NEGATIVE_EMOTIONS.filter { emotion ->
            val matchesSearch = searchQuery.isBlank() ||
                    emotion.name.contains(searchQuery, ignoreCase = true) ||
                    emotion.essence.contains(searchQuery, ignoreCase = true) ||
                    emotion.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedSubCategory == "All" || emotion.category == selectedSubCategory
            matchesSearch && matchesCategory
        }
    }

    val positiveSelectedCount = selectedEmotions.count { name ->
        EmotionCatalog.POSITIVE_EMOTIONS.any { it.name.equals(name, ignoreCase = true) || it.id.equals(name, ignoreCase = true) }
    }

    val negativeSelectedCount = selectedEmotions.count { name ->
        EmotionCatalog.NEGATIVE_EMOTIONS.any { it.name.equals(name, ignoreCase = true) || it.id.equals(name, ignoreCase = true) }
    }

    val positiveSelectedNames = remember(selectedEmotions) {
        selectedEmotions.filter { name ->
            EmotionCatalog.POSITIVE_EMOTIONS.any { it.name.equals(name, ignoreCase = true) || it.id.equals(name, ignoreCase = true) }
        }.toSet()
    }

    val negativeSelectedNames = remember(selectedEmotions) {
        selectedEmotions.filter { name ->
            EmotionCatalog.NEGATIVE_EMOTIONS.any { it.name.equals(name, ignoreCase = true) || it.id.equals(name, ignoreCase = true) }
        }.toSet()
    }

    val liveDailyArchetype = remember(positiveSelectedNames, negativeSelectedNames) {
        FantasyArchetypeEvaluationEngine.evaluateDailyArchetype(positiveSelectedNames, negativeSelectedNames)
    }

    val totalSelected = selectedEmotions.size + selectedShadows.size + selectedVirtues.size

    // Sub-categories for pills
    val currentSubCategories = remember(selectedCategoryTab) {
        when (selectedCategoryTab) {
            1 -> listOf("All", "Joy & Radiance", "Peace & Stillness", "Power & Sovereign", "Love & Connection", "Transcendence")
            2 -> listOf("All", "Anger & Fury", "Fear & Dread", "Sorrow & Ache", "Void & Emptiness", "Shame & Guilt", "Envy & Spite")
            else -> listOf("All", "Joy & Radiance", "Peace & Stillness", "Power & Sovereign", "Anger & Fury", "Fear & Dread", "Sorrow & Ache", "Void & Emptiness")
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("record_screen_column"),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 100.dp // Elevated spacing so submit button sits above bottom navigation bar
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Title
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SOUL RECORD MENU",
                            style = MaterialTheme.typography.titleLarge,
                            color = RadiantGoldBright,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Select positive & negative emotions or primal forces to calibrate your identity",
                            style = MaterialTheme.typography.bodySmall,
                            color = CelestialAmethystLight,
                            fontSize = 11.5.sp
                        )
                    }

                    if (totalSelected > 0) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    try {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    } catch (_: Exception) {}
                                    onFormUpdate {
                                        it.copy(
                                            selectedEmotions = emptySet(),
                                            selectedShadows = emptySet(),
                                            selectedVirtues = emptySet(),
                                            primaryShadow = null,
                                            primaryVirtue = null,
                                            selectedEmotion = "Equilibrium"
                                        )
                                    }
                                }
                                .testTag("clear_all_bubbles_button"),
                            color = Color(0xFF1E1433),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SurfaceCardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Reset All",
                                    tint = Color(0xFFF87171),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Reset ($totalSelected)",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFCA5A5),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Catalyst Balance Pill & Live Archetype Banner
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Live Fantasy Archetype Manifestation Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Color(liveDailyArchetype.auraColorHex).copy(alpha = 0.4f),
                            spotColor = Color(liveDailyArchetype.auraColorHex)
                        )
                        .testTag("daily_archetype_banner"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF110B22)
                    ),
                    border = BorderStroke(1.2.dp, Color(liveDailyArchetype.auraColorHex).copy(alpha = 0.7f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(liveDailyArchetype.auraColorHex).copy(alpha = 0.18f),
                                        Color(0xFF0F0820)
                                    )
                                )
                            )
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(liveDailyArchetype.auraColorHex).copy(alpha = 0.25f))
                                        .border(1.dp, Color(liveDailyArchetype.auraColorHex), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = liveDailyArchetype.runeIcon,
                                        fontSize = 18.sp
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "DAILY ARCHETYPE",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.2.sp,
                                            color = Color(liveDailyArchetype.auraColorHex)
                                        )
                                        Surface(
                                            color = when (liveDailyArchetype.dominanceValence) {
                                                "POSITIVE" -> Color(0xFF064E3B)
                                                "NEGATIVE" -> Color(0xFF7F1D1D)
                                                else -> Color(0xFF4C1D95)
                                            },
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = liveDailyArchetype.dominanceValence,
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = when (liveDailyArchetype.dominanceValence) {
                                                    "POSITIVE" -> Color(0xFF6EE7B7)
                                                    "NEGATIVE" -> Color(0xFFFCA5A5)
                                                    else -> Color(0xFFDDD6FE)
                                                },
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = liveDailyArchetype.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Text(
                                text = liveDailyArchetype.element,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CelestialAmethystLight
                            )
                        }

                        Text(
                            text = "“${liveDailyArchetype.dailyDecree}”",
                            fontSize = 11.5.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚡ ${liveDailyArchetype.powerBonus}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RadiantGoldBright,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "Harmonics: ${(liveDailyArchetype.positivityRatio * 100).toInt()}% Positive",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (liveDailyArchetype.positivityRatio >= 0.5f) Color(0xFF34D399) else Color(0xFFF87171)
                            )
                        }
                    }
                }

                // Balance Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0F0B1E).copy(alpha = 0.9f))
                        .border(
                            1.dp,
                            if (totalSelected > 0) RadiantGold.copy(alpha = 0.6f) else SurfaceCardBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("catalyst_summary_pill")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "✦", fontSize = 13.sp, color = RadiantGold)
                            Text(
                                text = if (totalSelected == 0) "Matrix Equilibrium" else "Active Emotional Alignment",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (totalSelected > 0) RadiantGoldBright else TextMuted
                            )
                        }

                        Text(
                            text = when {
                                totalSelected == 0 -> "0 Catalysts Active"
                                else -> buildString {
                                    val parts = mutableListOf<String>()
                                    if (positiveSelectedCount > 0) parts.add("✨ $positiveSelectedCount Pos")
                                    if (negativeSelectedCount > 0) parts.add("🔥 $negativeSelectedCount Neg")
                                    if (selectedShadows.isNotEmpty()) parts.add("🌑 ${selectedShadows.size} Sins")
                                    if (selectedVirtues.isNotEmpty()) parts.add("🌿 ${selectedVirtues.size} Virtues")
                                    append(parts.joinToString(" • "))
                                }
                            },
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (totalSelected > 0) EtherealCyan else TextMuted
                        )
                    }
                }
            }
        }

        // Main Category Tabs
        item {
            TabRow(
                selectedTabIndex = selectedCategoryTab,
                containerColor = Color(0xFF130D24),
                contentColor = RadiantGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategoryTab]),
                        color = RadiantGold,
                        height = 2.5.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedCategoryTab == 0,
                    onClick = {
                        selectedCategoryTab = 0
                        selectedSubCategory = "All"
                    },
                    text = { Text("All Catalysts", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("tab_all_catalysts")
                )
                Tab(
                    selected = selectedCategoryTab == 1,
                    onClick = {
                        selectedCategoryTab = 1
                        selectedSubCategory = "All"
                    },
                    text = {
                        Text(
                            "✨ Positive (${EmotionCatalog.POSITIVE_EMOTIONS.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedCategoryTab == 1) Color(0xFF34D399) else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_positive_emotions")
                )
                Tab(
                    selected = selectedCategoryTab == 2,
                    onClick = {
                        selectedCategoryTab = 2
                        selectedSubCategory = "All"
                    },
                    text = {
                        Text(
                            "🔥 Negative (${EmotionCatalog.NEGATIVE_EMOTIONS.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedCategoryTab == 2) Color(0xFFF87171) else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_negative_emotions")
                )
                Tab(
                    selected = selectedCategoryTab == 3,
                    onClick = {
                        selectedCategoryTab = 3
                        selectedSubCategory = "All"
                    },
                    text = { Text("⚡ 14 Forces", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("tab_sins_virtues")
                )
            }
        }

        // Search Bar & Sub-category Filter (shown when tabs 0, 1, 2 are selected)
        if (selectedCategoryTab != 3) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Search Input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("record_emotion_search"),
                        placeholder = { Text("Search 52+ emotions (e.g. Euphoria, Wrath, Peace...)", fontSize = 12.sp, color = TextMuted) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = RadiantGold, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RadiantGold,
                            unfocusedBorderColor = SurfaceCardBorder,
                            focusedContainerColor = Color(0xFF110B22),
                            unfocusedContainerColor = Color(0xFF0F091E),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Sub-category Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(currentSubCategories) { cat ->
                            val isSelected = selectedSubCategory == cat
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        try {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        } catch (_: Exception) {}
                                        selectedSubCategory = cat
                                    }
                                    .testTag("filter_chip_$cat"),
                                color = if (isSelected) RadiantGold.copy(alpha = 0.22f) else Color(0xFF160F2B),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, if (isSelected) RadiantGold else SurfaceCardBorder)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) RadiantGoldBright else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 1. POSITIVE EMOTIONS SECTION (26 items)
        if (selectedCategoryTab == 0 || selectedCategoryTab == 1) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "✨", fontSize = 15.sp)
                            Text(
                                text = "POSITIVE EMOTIONS (${filteredPositive.size} of ${EmotionCatalog.POSITIVE_EMOTIONS.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399),
                                letterSpacing = 0.8.sp
                            )
                        }

                        if (positiveSelectedCount > 0) {
                            Text(
                                text = "$positiveSelectedCount active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6EE7B7)
                            )
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredPositive.forEachIndexed { index, emotion ->
                            val isSelected = selectedEmotions.contains(emotion.name) || selectedEmotions.contains(emotion.id)
                            EmotionSelectableBubble(
                                emotion = emotion,
                                isSelected = isSelected,
                                index = index,
                                onToggle = {
                                    try {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    } catch (_: Exception) {}

                                    val newSet = if (isSelected) {
                                        selectedEmotions - emotion.name - emotion.id
                                    } else {
                                        selectedEmotions + emotion.name
                                    }

                                    // Also sync associated virtue if user toggles on
                                    val newVirtues = if (!isSelected && emotion.associatedVirtue != null) {
                                        selectedVirtues + emotion.associatedVirtue
                                    } else selectedVirtues

                                    onFormUpdate { current ->
                                        current.copy(
                                            selectedEmotions = newSet,
                                            selectedVirtues = newVirtues,
                                            primaryVirtue = newVirtues.firstOrNull(),
                                            selectedEmotion = if (newSet.isEmpty() && selectedShadows.isEmpty() && newVirtues.isEmpty()) "Equilibrium"
                                            else (newSet + selectedShadows.map { it.displayName } + newVirtues.map { it.displayName }).joinToString(", ")
                                        )
                                    }
                                },
                                testTag = "emotion_positive_${emotion.id}"
                            )
                        }
                    }
                }
            }
        }

        // 2. NEGATIVE EMOTIONS SECTION (26 items)
        if (selectedCategoryTab == 0 || selectedCategoryTab == 2) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🔥", fontSize = 15.sp)
                            Text(
                                text = "NEGATIVE EMOTIONS (${filteredNegative.size} of ${EmotionCatalog.NEGATIVE_EMOTIONS.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF87171),
                                letterSpacing = 0.8.sp
                            )
                        }

                        if (negativeSelectedCount > 0) {
                            Text(
                                text = "$negativeSelectedCount active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFCA5A5)
                            )
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredNegative.forEachIndexed { index, emotion ->
                            val isSelected = selectedEmotions.contains(emotion.name) || selectedEmotions.contains(emotion.id)
                            EmotionSelectableBubble(
                                emotion = emotion,
                                isSelected = isSelected,
                                index = index,
                                onToggle = {
                                    try {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    } catch (_: Exception) {}

                                    val newSet = if (isSelected) {
                                        selectedEmotions - emotion.name - emotion.id
                                    } else {
                                        selectedEmotions + emotion.name
                                    }

                                    // Also sync associated shadow if user toggles on
                                    val newShadows = if (!isSelected && emotion.associatedShadow != null) {
                                        selectedShadows + emotion.associatedShadow
                                    } else selectedShadows

                                    onFormUpdate { current ->
                                        current.copy(
                                            selectedEmotions = newSet,
                                            selectedShadows = newShadows,
                                            primaryShadow = newShadows.firstOrNull(),
                                            selectedEmotion = if (newSet.isEmpty() && newShadows.isEmpty() && selectedVirtues.isEmpty()) "Equilibrium"
                                            else (newSet + newShadows.map { it.displayName } + selectedVirtues.map { it.displayName }).joinToString(", ")
                                        )
                                    }
                                },
                                testTag = "emotion_negative_${emotion.id}"
                            )
                        }
                    }
                }
            }
        }

        // 3. SEVEN DEADLY SINS & SEVEN HEAVENLY VIRTUES (14 Primal Forces)
        if (selectedCategoryTab == 0 || selectedCategoryTab == 3) {
            // Sins
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🌑", fontSize = 14.sp)
                            Text(
                                text = "SEVEN DEADLY SINS (SHADOW FORCES)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF87171),
                                letterSpacing = 0.8.sp
                            )
                        }

                        if (selectedShadows.isNotEmpty()) {
                            Text(
                                text = "${selectedShadows.size} active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFCA5A5)
                            )
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ShadowType.entries.forEachIndexed { index, sin ->
                            val isSelected = selectedShadows.contains(sin)
                            ForceBubble(
                                runeSymbol = sin.runeSymbol,
                                name = sin.displayName,
                                subtitle = sin.title.replace("The ", ""),
                                color = Color(sin.colorHex),
                                isSelected = isSelected,
                                index = index,
                                onToggle = {
                                    try {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    } catch (_: Exception) {}

                                    val newSet = if (isSelected) selectedShadows - sin else selectedShadows + sin
                                    onFormUpdate { current ->
                                        current.copy(
                                            selectedShadows = newSet,
                                            primaryShadow = newSet.firstOrNull(),
                                            selectedEmotion = if (selectedEmotions.isEmpty() && newSet.isEmpty() && selectedVirtues.isEmpty()) "Equilibrium"
                                            else (selectedEmotions + newSet.map { it.displayName } + selectedVirtues.map { it.displayName }).joinToString(", ")
                                        )
                                    }
                                },
                                testTag = "sin_bubble_${sin.name.lowercase()}"
                            )
                        }
                    }
                }
            }

            // Virtues
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🌿", fontSize = 14.sp)
                            Text(
                                text = "SEVEN HEAVENLY VIRTUES (SACRED FORCES)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadiantGoldBright,
                                letterSpacing = 0.8.sp
                            )
                        }

                        if (selectedVirtues.isNotEmpty()) {
                            Text(
                                text = "${selectedVirtues.size} active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RadiantGold
                            )
                        }
                    }

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        VirtueType.entries.forEachIndexed { index, virtue ->
                            val isSelected = selectedVirtues.contains(virtue)
                            ForceBubble(
                                runeSymbol = virtue.runeSymbol,
                                name = virtue.displayName,
                                subtitle = virtue.title.replace("The ", ""),
                                color = Color(virtue.colorHex),
                                isSelected = isSelected,
                                index = index,
                                onToggle = {
                                    try {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    } catch (_: Exception) {}

                                    val newSet = if (isSelected) selectedVirtues - virtue else selectedVirtues + virtue
                                    onFormUpdate { current ->
                                        current.copy(
                                            selectedVirtues = newSet,
                                            primaryVirtue = newSet.firstOrNull(),
                                            selectedEmotion = if (selectedEmotions.isEmpty() && selectedShadows.isEmpty() && newSet.isEmpty()) "Equilibrium"
                                            else (selectedEmotions + selectedShadows.map { it.displayName } + newSet.map { it.displayName }).joinToString(", ")
                                        )
                                    }
                                },
                                testTag = "virtue_bubble_${virtue.name.lowercase()}"
                            )
                        }
                    }
                }
            }
        }

        // Optional Context / Reflection expander
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showContextFields = !showContextFields },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF120B22)),
                border = BorderStroke(1.dp, SurfaceCardBorder),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = RadiantGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Add Manifestation Context & Decree (Optional)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RadiantGoldBright
                            )
                        }
                        Text(
                            text = if (showContextFields) "▲ Hide" else "▼ Expand",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    AnimatedVisibility(
                        visible = showContextFields,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = formState.situation,
                                onValueChange = { text -> onFormUpdate { it.copy(situation = text) } },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("record_situation_input"),
                                label = { Text("What catalyzed these feelings?", fontSize = 11.5.sp, color = TextMuted) },
                                placeholder = { Text("e.g., Felt surge of serenity during dawn meditation.", fontSize = 11.sp, color = TextMuted.copy(alpha = 0.6f)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RadiantGold,
                                    unfocusedBorderColor = SurfaceCardBorder,
                                    focusedContainerColor = Color(0xFF0C0718),
                                    unfocusedContainerColor = Color(0xFF0C0718),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = formState.reflection,
                                onValueChange = { text -> onFormUpdate { it.copy(reflection = text) } },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("record_reflection_input"),
                                label = { Text("Inner Decree / Lesson Learned", fontSize = 11.5.sp, color = TextMuted) },
                                placeholder = { Text("e.g., I master my emotions to elevate my vessel.", fontSize = 11.sp, color = TextMuted.copy(alpha = 0.6f)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EtherealCyan,
                                    unfocusedBorderColor = SurfaceCardBorder,
                                    focusedContainerColor = Color(0xFF0C0718),
                                    unfocusedContainerColor = Color(0xFF0C0718),
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // ELEVATED TRANSMUTATION / CALCULATION BUTTON
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    try {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    } catch (_: Exception) {}
                    onSubmit()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(
                        elevation = if (totalSelected > 0) 14.dp else 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = RadiantGold.copy(alpha = 0.4f),
                        spotColor = RadiantGold
                    )
                    .testTag("submit_record_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (totalSelected > 0) RadiantGold else Color(0xFF23193C),
                    contentColor = if (totalSelected > 0) Color(0xFF0D061A) else RadiantGold
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = !formState.isSubmitting
            ) {
                if (formState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = if (totalSelected > 0) Color.Black else RadiantGold,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Transmute",
                            modifier = Modifier.size(20.dp),
                            tint = if (totalSelected > 0) Color(0xFF0D061A) else RadiantGold
                        )
                        Text(
                            text = if (totalSelected > 0) "ENTER & TRANSMUTE ($totalSelected CATALYSTS)" else "ENTER & CALCULATE EQUILIBRIUM",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            fontSize = 13.5.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Emotion Selectable Bubble for 21+ Positive & 21+ Negative Emotions with entrance animations & aura
 */
@Composable
private fun EmotionSelectableBubble(
    emotion: EmotionItem,
    isSelected: Boolean,
    index: Int = 0,
    onToggle: () -> Unit,
    testTag: String
) {
    val emotionColor = Color(emotion.colorHex)

    var hasEntered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        hasEntered = true
    }

    val entranceAlpha by animateFloatAsState(
        targetValue = if (hasEntered) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = (index * 18).coerceAtMost(360),
            easing = FastOutSlowInEasing
        ),
        label = "emotion_entrance_alpha"
    )

    val entranceScale by animateFloatAsState(
        targetValue = if (hasEntered) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = (index * 18).coerceAtMost(360),
            easing = FastOutSlowInEasing
        ),
        label = "emotion_entrance_scale"
    )

    val entranceOffsetY by animateFloatAsState(
        targetValue = if (hasEntered) 0f else 15f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = (index * 18).coerceAtMost(360),
            easing = FastOutSlowInEasing
        ),
        label = "emotion_entrance_offset"
    )

    val selectionScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = tween(180, easing = FastOutSlowInEasing),
        label = "emotion_scale"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) emotionColor else emotionColor.copy(alpha = 0.35f),
        animationSpec = tween(180),
        label = "emotion_border"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = entranceAlpha
                scaleX = selectionScale * entranceScale
                scaleY = selectionScale * entranceScale
                translationY = entranceOffsetY
            }
            .shadow(
                elevation = if (isSelected) 8.dp else 0.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = emotionColor.copy(alpha = 0.5f),
                spotColor = emotionColor
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (isSelected) {
                    Brush.radialGradient(
                        colors = listOf(
                            emotionColor.copy(alpha = 0.35f),
                            Color(0xFF160F2C)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF150E28),
                            Color(0xFF0E081C)
                        )
                    )
                }
            )
            .border(
                width = if (isSelected) 1.8.dp else 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            )
            .padding(horizontal = 12.dp, vertical = 9.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Rune Icon Frame
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) emotionColor.copy(alpha = 0.25f) else Color(0xFF1B1232))
                    .border(0.6.dp, if (isSelected) emotionColor else Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emotion.runeIcon,
                    fontSize = 13.sp
                )
            }

            Column(modifier = Modifier.widthIn(max = 130.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = emotion.name,
                        fontSize = 12.sp,
                        color = if (isSelected) emotionColor else TextPrimary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (emotion.valence == EmotionValence.POSITIVE) "✨" else "🔥",
                        fontSize = 9.sp
                    )
                }

                Text(
                    text = emotion.category,
                    fontSize = 9.sp,
                    color = if (isSelected) emotionColor.copy(alpha = 0.85f) else TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(emotionColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.Black,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}

/**
 * Primal Force Bubble (Seven Deadly Sins & Seven Heavenly Virtues) with entrance animations & aura
 */
@Composable
private fun ForceBubble(
    runeSymbol: String,
    name: String,
    subtitle: String,
    color: Color,
    isSelected: Boolean,
    index: Int = 0,
    onToggle: () -> Unit,
    testTag: String
) {
    var hasEntered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        hasEntered = true
    }

    val entranceAlpha by animateFloatAsState(
        targetValue = if (hasEntered) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = (index * 20).coerceAtMost(360),
            easing = FastOutSlowInEasing
        ),
        label = "force_entrance_alpha"
    )

    val entranceScale by animateFloatAsState(
        targetValue = if (hasEntered) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = (index * 20).coerceAtMost(360),
            easing = FastOutSlowInEasing
        ),
        label = "force_entrance_scale"
    )

    val entranceOffsetY by animateFloatAsState(
        targetValue = if (hasEntered) 0f else 15f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = (index * 20).coerceAtMost(360),
            easing = FastOutSlowInEasing
        ),
        label = "force_entrance_offset"
    )

    val selectionScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = tween(180, easing = FastOutSlowInEasing),
        label = "force_scale"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) color else color.copy(alpha = 0.35f),
        animationSpec = tween(180),
        label = "force_border"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = entranceAlpha
                scaleX = selectionScale * entranceScale
                scaleY = selectionScale * entranceScale
                translationY = entranceOffsetY
            }
            .shadow(
                elevation = if (isSelected) 8.dp else 0.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = color.copy(alpha = 0.5f),
                spotColor = color
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = if (isSelected) {
                    Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = 0.35f),
                            Color(0xFF160F2C)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF150E28),
                            Color(0xFF0E081C)
                        )
                    )
                }
            )
            .border(
                width = if (isSelected) 1.8.dp else 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) color.copy(alpha = 0.25f) else Color(0xFF1B1232))
                    .border(0.6.dp, if (isSelected) color else Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = runeSymbol, fontSize = 13.sp)
            }

            Column {
                Text(
                    text = name,
                    fontSize = 12.5.sp,
                    color = if (isSelected) color else TextPrimary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    color = if (isSelected) color.copy(alpha = 0.85f) else TextMuted
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.Black,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }
    }
}
