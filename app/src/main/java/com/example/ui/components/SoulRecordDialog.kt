package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Soul Record & Force Calibration Dialog with full catalog of 21+ Positive Emotions,
 * 21+ Negative Emotions, and the Seven Deadly Sins & Seven Heavenly Virtues.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SoulRecordDialog(
    initialShadows: Set<ShadowType> = emptySet(),
    initialVirtues: Set<VirtueType> = emptySet(),
    onSubmit: (selectedShadows: Set<ShadowType>, selectedVirtues: Set<VirtueType>, situation: String, reflection: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedShadows by remember { mutableStateOf(initialShadows) }
    var selectedVirtues by remember { mutableStateOf(initialVirtues) }
    var selectedEmotionNames by remember { mutableStateOf(emptySet<String>()) }
    var situationText by remember { mutableStateOf("") }
    var reflectionText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: All Catalysts, 1: Positive, 2: Negative, 3: 7 Sins, 4: 7 Virtues
    var searchQuery by remember { mutableStateOf("") }
    var showGuideDialog by remember { mutableStateOf(false) }

    if (showGuideDialog) {
        ArchetypeGuideDialog(onDismiss = { showGuideDialog = false })
    }

    val totalSelected = selectedEmotionNames.size + selectedShadows.size + selectedVirtues.size

    val filteredPositive = remember(searchQuery) {
        EmotionCatalog.POSITIVE_EMOTIONS.filter { emotion ->
            searchQuery.isBlank() || emotion.name.contains(searchQuery, ignoreCase = true) || emotion.category.contains(searchQuery, ignoreCase = true)
        }
    }

    val filteredNegative = remember(searchQuery) {
        EmotionCatalog.NEGATIVE_EMOTIONS.filter { emotion ->
            searchQuery.isBlank() || emotion.name.contains(searchQuery, ignoreCase = true) || emotion.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(RadiantGold, CelestialAmethyst, Color(0xFF1E1035))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .testTag("soul_record_dialog"),
            color = SurfaceCardElevated
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RadiantGold.copy(alpha = 0.2f))
                                .border(1.2.dp, RadiantGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flare,
                                contentDescription = "Record Calibration",
                                tint = RadiantGoldBright,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "SOUL TRANSMUTATION",
                                style = MaterialTheme.typography.titleLarge,
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "52+ Positive & Negative Emotions • 14 Primal Forces",
                                style = MaterialTheme.typography.bodySmall,
                                color = CelestialAmethystLight,
                                fontSize = 10.5.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showGuideDialog = true },
                            modifier = Modifier.testTag("record_dialog_guide_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Force Guide",
                                tint = EtherealCyan
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_record_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextPrimary
                            )
                        }
                    }
                }

                // Balance Meter Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0B1C)),
                    border = BorderStroke(1.dp, CelestialAmethyst.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${selectedShadows.size} Sins Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF87171)
                            )
                            Text(
                                text = if (totalSelected == 0) "« Balanced Alignment »"
                                else "« $totalSelected Catalysts Selected »",
                                fontSize = 11.sp,
                                color = RadiantGold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "${selectedVirtues.size} Virtues Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399)
                            )
                        }
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dialog_search_input"),
                    placeholder = { Text("Search 52+ emotions & forces...", fontSize = 11.5.sp, color = TextMuted) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = RadiantGold, modifier = Modifier.size(16.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(14.dp))
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
                    shape = RoundedCornerShape(10.dp)
                )

                // Scrollable Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF110B1E),
                    contentColor = RadiantGold,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = RadiantGold,
                            height = 2.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Dual Matrix", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("✨ Positive (${filteredPositive.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34D399)) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("🔥 Negative (${filteredNegative.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF87171)) }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("7 Sins", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        text = { Text("7 Virtues", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                // Content Area
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .testTag("record_dialog_content_column"),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    // Positive Emotions
                    if (selectedTab == 0 || selectedTab == 1) {
                        item {
                            Text(
                                text = "✨ POSITIVE EMOTIONS (${EmotionCatalog.POSITIVE_EMOTIONS.size})",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF34D399),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                filteredPositive.forEach { emotion ->
                                    val isSelected = selectedEmotionNames.contains(emotion.name)
                                    DialogSelectableChip(
                                        title = emotion.name,
                                        rune = emotion.runeIcon,
                                        aspect = emotion.category,
                                        glowColor = Color(emotion.colorHex),
                                        isSelected = isSelected,
                                        onClick = {
                                            selectedEmotionNames = if (isSelected) selectedEmotionNames - emotion.name else selectedEmotionNames + emotion.name
                                            if (!isSelected && emotion.associatedVirtue != null) {
                                                selectedVirtues = selectedVirtues + emotion.associatedVirtue
                                            }
                                        },
                                        testTag = "dialog_pos_chip_${emotion.id}"
                                    )
                                }
                            }
                        }
                    }

                    // Negative Emotions
                    if (selectedTab == 0 || selectedTab == 2) {
                        item {
                            Text(
                                text = "🔥 NEGATIVE EMOTIONS (${EmotionCatalog.NEGATIVE_EMOTIONS.size})",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFF87171),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                filteredNegative.forEach { emotion ->
                                    val isSelected = selectedEmotionNames.contains(emotion.name)
                                    DialogSelectableChip(
                                        title = emotion.name,
                                        rune = emotion.runeIcon,
                                        aspect = emotion.category,
                                        glowColor = Color(emotion.colorHex),
                                        isSelected = isSelected,
                                        onClick = {
                                            selectedEmotionNames = if (isSelected) selectedEmotionNames - emotion.name else selectedEmotionNames + emotion.name
                                            if (!isSelected && emotion.associatedShadow != null) {
                                                selectedShadows = selectedShadows + emotion.associatedShadow
                                            }
                                        },
                                        testTag = "dialog_neg_chip_${emotion.id}"
                                    )
                                }
                            }
                        }
                    }

                    // 7 Sins
                    if (selectedTab == 0 || selectedTab == 3) {
                        item {
                            Text(
                                text = "🌑 SEVEN DEADLY SINS (SHADOW FORCES)",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFF87171),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                ShadowType.values().forEach { shadow ->
                                    val isSelected = selectedShadows.contains(shadow)
                                    DialogSelectableChip(
                                        title = shadow.displayName,
                                        rune = shadow.runeSymbol,
                                        aspect = shadow.title,
                                        glowColor = Color(shadow.colorHex),
                                        isSelected = isSelected,
                                        onClick = {
                                            selectedShadows = if (isSelected) selectedShadows - shadow else selectedShadows + shadow
                                        },
                                        testTag = "sin_chip_${shadow.name.lowercase()}"
                                    )
                                }
                            }
                        }
                    }

                    // 7 Virtues
                    if (selectedTab == 0 || selectedTab == 4) {
                        item {
                            Text(
                                text = "🌿 SEVEN HEAVENLY VIRTUES (SACRED FORCES)",
                                style = MaterialTheme.typography.labelMedium,
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                VirtueType.values().forEach { virtue ->
                                    val isSelected = selectedVirtues.contains(virtue)
                                    DialogSelectableChip(
                                        title = virtue.displayName,
                                        rune = virtue.runeSymbol,
                                        aspect = virtue.title,
                                        glowColor = Color(virtue.colorHex),
                                        isSelected = isSelected,
                                        onClick = {
                                            selectedVirtues = if (isSelected) selectedVirtues - virtue else selectedVirtues + virtue
                                        },
                                        testTag = "virtue_chip_${virtue.name.lowercase()}"
                                    )
                                }
                            }
                        }
                    }

                    // Optional Situation & Manifestation Context
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF110C20)),
                            border = BorderStroke(1.dp, SurfaceCardBorder),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "✦ MANIFESTATION CONTEXT (OPTIONAL)",
                                    fontSize = 10.sp,
                                    color = RadiantGold,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )

                                OutlinedTextField(
                                    value = situationText,
                                    onValueChange = { situationText = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("record_situation_input"),
                                    label = { Text("What catalyzed these forces?", fontSize = 11.sp, color = TextMuted) },
                                    placeholder = { Text("e.g., Felt courage & euphoria during breakthroughs.", fontSize = 10.5.sp, color = TextMuted.copy(alpha = 0.6f)) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RadiantGold,
                                        unfocusedBorderColor = SurfaceCardBorder,
                                        focusedContainerColor = Color(0xFF0B0716),
                                        unfocusedContainerColor = Color(0xFF0B0716),
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )

                                OutlinedTextField(
                                    value = reflectionText,
                                    onValueChange = { reflectionText = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("record_reflection_input"),
                                    label = { Text("Soul Insight / Inner Decree", fontSize = 11.sp, color = TextMuted) },
                                    placeholder = { Text("e.g., I channel inner fire into sovereign clarity.", fontSize = 10.5.sp, color = TextMuted.copy(alpha = 0.6f)) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EtherealCyan,
                                        unfocusedBorderColor = SurfaceCardBorder,
                                        focusedContainerColor = Color(0xFF0B0716),
                                        unfocusedContainerColor = Color(0xFF0B0716),
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }

                // Submit Button
                Button(
                    onClick = {
                        val effectiveSituation = if (selectedEmotionNames.isNotEmpty()) {
                            val emotionPrefix = "Emotions: ${selectedEmotionNames.joinToString(", ")}. "
                            if (situationText.isNotBlank()) "$emotionPrefix$situationText" else emotionPrefix
                        } else situationText

                        onSubmit(selectedShadows, selectedVirtues, effectiveSituation, reflectionText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("calculate_define_me_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RadiantGold,
                        contentColor = Color(0xFF1E1102)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF1E1102)
                        )
                        Text(
                            text = if (totalSelected > 0) "CALCULATE & TRANSMUTE ($totalSelected CATALYSTS)" else "CALCULATE & DEFINE GODLY IDENTITY",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogSelectableChip(
    title: String,
    rune: String,
    aspect: String,
    glowColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val animatedBg by animateColorAsState(
        targetValue = if (isSelected) glowColor.copy(alpha = 0.25f) else Color(0xFF130D22),
        animationSpec = tween(180), label = "dialog_chip_bg"
    )

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        color = animatedBg,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) glowColor else SurfaceCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = rune, fontSize = 13.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) TextPrimary else TextSecondary
                )
                Text(
                    text = aspect,
                    fontSize = 8.5.sp,
                    color = if (isSelected) glowColor else TextMuted
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = glowColor,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
