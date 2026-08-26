package com.example.ui.screens.record

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.example.data.model.ShadowType
import com.example.data.model.VirtueType
import com.example.ui.components.ArchetypeGuideDialog
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RecordFormState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecordScreen(
    formState: RecordFormState,
    onFormUpdate: ((RecordFormState) -> RecordFormState) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showGuideDialog by remember { mutableStateOf(false) }
    var selectedForceTab by remember { mutableIntStateOf(0) } // 0: Dual Catalyst, 1: 7 Sins, 2: 7 Virtues

    if (showGuideDialog) {
        ArchetypeGuideDialog(onDismiss = { showGuideDialog = false })
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("record_screen_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with Back Button and Guide Codex Icon on Top Right
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("record_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }

                    Column {
                        Text(
                            text = "RECORD SANCTUARY",
                            style = MaterialTheme.typography.headlineMedium,
                            color = RadiantGoldBright,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Direct Archetypal Transmutation (Sins & Virtues)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextAmethyst
                        )
                    }
                }

                // Guide Icon on Top Right (Explains all traits of 7 Sins & 7 Virtues)
                IconButton(
                    onClick = { showGuideDialog = true },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF1F1538))
                        .border(1.2.dp, RadiantGold.copy(alpha = 0.8f), CircleShape)
                        .testTag("record_guide_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Trait Codex Guide",
                        tint = RadiantGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // View Mode Filter Tabs
        item {
            TabRow(
                selectedTabIndex = selectedForceTab,
                containerColor = Color(0xFF120C24),
                contentColor = RadiantGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedForceTab]),
                        color = when (selectedForceTab) {
                            0 -> RadiantGold
                            1 -> Color(0xFFEF4444)
                            else -> Color(0xFF10B981)
                        }
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(0.8.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedForceTab == 0,
                    onClick = { selectedForceTab = 0 },
                    text = {
                        Text(
                            text = "ALL FORCES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedForceTab == 0) RadiantGold else TextMuted
                        )
                    }
                )
                Tab(
                    selected = selectedForceTab == 1,
                    onClick = { selectedForceTab = 1 },
                    text = {
                        Text(
                            text = "7 DEADLY SINS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedForceTab == 1) Color(0xFFFCA5A5) else TextMuted
                        )
                    }
                )
                Tab(
                    selected = selectedForceTab == 2,
                    onClick = { selectedForceTab = 2 },
                    text = {
                        Text(
                            text = "7 VIRTUES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedForceTab == 2) Color(0xFF6EE7B7) else TextMuted
                        )
                    }
                )
            }
        }

        // 1. SEVEN DEADLY SINS SECTION
        if (selectedForceTab == 0 || selectedForceTab == 1) {
            item {
                val selectedShadows = if (formState.selectedShadows.isNotEmpty()) {
                    formState.selectedShadows
                } else {
                    listOfNotNull(formState.primaryShadow).toSet()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCard)
                        .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "🔥", fontSize = 14.sp)
                                Text(
                                    text = "SEVEN DEADLY SINS (SHADOW FORCE)",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFFF87171),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            if (selectedShadows.isNotEmpty()) {
                                Text(
                                    text = "${selectedShadows.size} Selected",
                                    fontSize = 10.5.sp,
                                    color = Color(0xFFF87171),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Grid of 7 Sins (Multi-select enabled)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ShadowType.values().forEach { sin ->
                                val isSelected = selectedShadows.contains(sin)
                                val sinColor = Color(sin.colorHex)
                                val animatedBg by animateColorAsState(
                                    targetValue = if (isSelected) sinColor.copy(alpha = 0.28f) else Color(0xFF130E26),
                                    animationSpec = tween(200),
                                    label = "sin_bg"
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(animatedBg)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.8.dp,
                                            color = if (isSelected) sinColor else SurfaceCardBorder,
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            val newSet = if (isSelected) selectedShadows - sin else selectedShadows + sin
                                            onFormUpdate {
                                                it.copy(
                                                    selectedShadows = newSet,
                                                    primaryShadow = newSet.firstOrNull(),
                                                    selectedEmotion = if (newSet.isEmpty()) "Equilibrium" else newSet.joinToString(", ") { s -> s.displayName }
                                                )
                                            }
                                        }
                                        .padding(horizontal = 12.dp, vertical = 9.dp)
                                        .testTag("sin_chip_${sin.name.lowercase()}")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = sin.runeSymbol, fontSize = 15.sp)
                                        Column {
                                            Text(
                                                text = sin.displayName,
                                                fontSize = 12.5.sp,
                                                color = if (isSelected) sinColor else TextSecondary,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                            Text(
                                                text = getSinSubtitle(sin),
                                                fontSize = 9.sp,
                                                color = if (isSelected) sinColor.copy(alpha = 0.8f) else TextMuted
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = sinColor,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. SEVEN HEAVENLY VIRTUES SECTION
        if (selectedForceTab == 0 || selectedForceTab == 2) {
            item {
                val selectedVirtues = if (formState.selectedVirtues.isNotEmpty()) {
                    formState.selectedVirtues
                } else {
                    listOfNotNull(formState.primaryVirtue).toSet()
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCard)
                        .border(1.dp, RadiantGold.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "✨", fontSize = 14.sp)
                                Text(
                                    text = "SEVEN HEAVENLY VIRTUES (LIGHT FORCE)",
                                    fontSize = 11.5.sp,
                                    color = RadiantGoldBright,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }

                            if (selectedVirtues.isNotEmpty()) {
                                Text(
                                    text = "${selectedVirtues.size} Selected",
                                    fontSize = 10.5.sp,
                                    color = RadiantGoldBright,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Grid of 7 Virtues (Multi-select enabled)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            VirtueType.values().forEach { virtue ->
                                val isSelected = selectedVirtues.contains(virtue)
                                val virtueColor = Color(virtue.colorHex)
                                val animatedBg by animateColorAsState(
                                    targetValue = if (isSelected) virtueColor.copy(alpha = 0.28f) else Color(0xFF130E26),
                                    animationSpec = tween(200),
                                    label = "virtue_bg"
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(animatedBg)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.8.dp,
                                            color = if (isSelected) virtueColor else SurfaceCardBorder,
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            val newSet = if (isSelected) selectedVirtues - virtue else selectedVirtues + virtue
                                            onFormUpdate {
                                                it.copy(
                                                    selectedVirtues = newSet,
                                                    primaryVirtue = newSet.firstOrNull(),
                                                    selectedEmotion = if (newSet.isEmpty()) "Equilibrium" else newSet.joinToString(", ") { v -> v.displayName }
                                                )
                                            }
                                        }
                                        .padding(horizontal = 12.dp, vertical = 9.dp)
                                        .testTag("virtue_chip_${virtue.name.lowercase()}")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(text = virtue.runeSymbol, fontSize = 15.sp)
                                        Column {
                                            Text(
                                                text = virtue.displayName,
                                                fontSize = 12.5.sp,
                                                color = if (isSelected) virtueColor else TextSecondary,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                            Text(
                                                text = getVirtueSubtitle(virtue),
                                                fontSize = 9.sp,
                                                color = if (isSelected) virtueColor.copy(alpha = 0.8f) else TextMuted
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = virtueColor,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. MINIMALISTIC CONFIRMATION & SYNTHESIS CARD
        item {
            val allShadows = if (formState.selectedShadows.isNotEmpty()) {
                formState.selectedShadows
            } else {
                listOfNotNull(formState.primaryShadow).toSet()
            }
            val allVirtues = if (formState.selectedVirtues.isNotEmpty()) {
                formState.selectedVirtues
            } else {
                listOfNotNull(formState.primaryVirtue).toSet()
            }
            val hasSelections = allShadows.isNotEmpty() || allVirtues.isNotEmpty()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F0A1F))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .testTag("transmutation_summary_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "✦", fontSize = 12.sp, color = RadiantGold)
                            Text(
                                text = "TRANSMUTATION MATRIX",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadiantGold,
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = when {
                                allShadows.isNotEmpty() && allVirtues.isNotEmpty() -> "${allShadows.size} Shadows • ${allVirtues.size} Virtues"
                                allShadows.isNotEmpty() -> "${allShadows.size} Shadow Catalysts"
                                allVirtues.isNotEmpty() -> "${allVirtues.size} Virtue Forces"
                                else -> "Awaiting Selection"
                            },
                            fontSize = 10.sp,
                            color = if (hasSelections) EtherealCyan else TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (hasSelections) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            allShadows.forEach { sin ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(sin.colorHex).copy(alpha = 0.18f))
                                        .border(0.6.dp, Color(sin.colorHex).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "${sin.runeSymbol} ${sin.displayName}",
                                        fontSize = 10.5.sp,
                                        color = Color(sin.colorHex),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            allVirtues.forEach { virtue ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(virtue.colorHex).copy(alpha = 0.18f))
                                        .border(0.6.dp, Color(virtue.colorHex).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "${virtue.runeSymbol} ${virtue.displayName}",
                                        fontSize = 10.5.sp,
                                        color = Color(virtue.colorHex),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Select one or more archetypal catalysts above to begin transmutation.",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // 4. SUBMIT / TRANSMUTE BUTTON
        item {
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("submit_record_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RadiantGold,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = !formState.isSubmitting
            ) {
                if (formState.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Evaluate",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "TRANSMUTE & EVALUATE SOUL",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontSize = 13.5.sp
                        )
                    }
                }
            }
        }
    }
}

private fun getSinSubtitle(sin: ShadowType): String {
    return when (sin) {
        ShadowType.PRIDE -> "Sovereign Ego"
        ShadowType.GREED -> "Infinite Hunger"
        ShadowType.DESIRE -> "Primordial Pulse"
        ShadowType.ENVY -> "Comparative Void"
        ShadowType.GLUTTONY -> "Sensory Excess"
        ShadowType.WRATH -> "Infernal Flare"
        ShadowType.SLOTH -> "Abyssal Stagnation"
    }
}

private fun getVirtueSubtitle(virtue: VirtueType): String {
    return when (virtue) {
        VirtueType.HUMILITY -> "Grounded Clarity"
        VirtueType.CHARITY -> "Radiant Munificence"
        VirtueType.COURAGE -> "Sacred Valor"
        VirtueType.GRATITUDE -> "Abundance Resonance"
        VirtueType.TEMPERANCE -> "Harmonic Balance"
        VirtueType.PATIENCE -> "Eternal Starlight"
        VirtueType.DILIGENCE -> "Unstoppable Will"
    }
}
