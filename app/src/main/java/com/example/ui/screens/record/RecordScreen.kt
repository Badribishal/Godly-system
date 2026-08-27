package com.example.ui.screens.record

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShadowType
import com.example.data.model.VirtueType
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RecordFormState

/**
 * Minimalist, uncluttered Record tab page showing only the Seven Deadly Sins
 * and Seven Heavenly Virtues as interactive, multi-selectable glowing bubbles,
 * with the entry button elevated above the bottom navigation area.
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

    val totalSelected = selectedShadows.size + selectedVirtues.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("record_screen_column"),
        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 18.dp,
            bottom = 96.dp // Elevated spacing so content and button sit well above bottom nav
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Minimalist Header & Reset Control
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ARCHETYPAL RECORD",
                            style = MaterialTheme.typography.titleLarge,
                            color = RadiantGoldBright,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.4.sp
                        )
                        Text(
                            text = "Touch bubbles to infuse dualities into the matrix",
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
                            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Selections",
                                    tint = TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Reset",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Catalyst Balance Pill
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F0B1E).copy(alpha = 0.85f))
                    .border(
                        1.dp,
                        if (totalSelected > 0) RadiantGold.copy(alpha = 0.5f) else SurfaceCardBorder,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("catalyst_summary_pill")
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
                        Text(text = "✦", fontSize = 14.sp, color = RadiantGold)
                        Text(
                            text = if (totalSelected == 0) "Awaiting Catalyst Selection" else "Transmutation Matrix Active",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (totalSelected > 0) RadiantGoldBright else TextMuted
                        )
                    }

                    Text(
                        text = when {
                            selectedShadows.isNotEmpty() && selectedVirtues.isNotEmpty() ->
                                "${selectedShadows.size} Shadows • ${selectedVirtues.size} Virtues"
                            selectedShadows.isNotEmpty() -> "${selectedShadows.size} Shadows"
                            selectedVirtues.isNotEmpty() -> "${selectedVirtues.size} Virtues"
                            else -> "0 / 14 Selected"
                        },
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (totalSelected > 0) EtherealCyan else TextMuted
                    )
                }
            }
        }

        // 1. SEVEN DEADLY SINS BUBBLES
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        Text(text = "🔥", fontSize = 14.sp)
                        Text(
                            text = "SEVEN DEADLY SINS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF87171),
                            letterSpacing = 1.sp
                        )
                    }

                    if (selectedShadows.isNotEmpty()) {
                        Text(
                            text = "${selectedShadows.size} selected",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFCA5A5)
                        )
                    }
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShadowType.entries.forEach { sin ->
                        val isSelected = selectedShadows.contains(sin)
                        ArchetypeBubble(
                            runeSymbol = sin.runeSymbol,
                            name = sin.displayName,
                            subtitle = sin.title.replace("The ", ""),
                            color = Color(sin.colorHex),
                            isSelected = isSelected,
                            onToggle = {
                                try {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                } catch (_: Exception) {}

                                val newSet = if (isSelected) selectedShadows - sin else selectedShadows + sin
                                onFormUpdate { current ->
                                    current.copy(
                                        selectedShadows = newSet,
                                        primaryShadow = newSet.firstOrNull(),
                                        selectedEmotion = if (newSet.isEmpty() && selectedVirtues.isEmpty()) "Equilibrium"
                                        else (newSet.map { it.displayName } + selectedVirtues.map { it.displayName }).joinToString(", ")
                                    )
                                }
                            },
                            testTag = "sin_bubble_${sin.name.lowercase()}"
                        )
                    }
                }
            }
        }

        // 2. SEVEN HEAVENLY VIRTUES BUBBLES
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        Text(text = "✨", fontSize = 14.sp)
                        Text(
                            text = "SEVEN HEAVENLY VIRTUES",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RadiantGoldBright,
                            letterSpacing = 1.sp
                        )
                    }

                    if (selectedVirtues.isNotEmpty()) {
                        Text(
                            text = "${selectedVirtues.size} selected",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = RadiantGold
                        )
                    }
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VirtueType.entries.forEach { virtue ->
                        val isSelected = selectedVirtues.contains(virtue)
                        ArchetypeBubble(
                            runeSymbol = virtue.runeSymbol,
                            name = virtue.displayName,
                            subtitle = virtue.title.replace("The ", ""),
                            color = Color(virtue.colorHex),
                            isSelected = isSelected,
                            onToggle = {
                                try {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                } catch (_: Exception) {}

                                val newSet = if (isSelected) selectedVirtues - virtue else selectedVirtues + virtue
                                onFormUpdate { current ->
                                    current.copy(
                                        selectedVirtues = newSet,
                                        primaryVirtue = newSet.firstOrNull(),
                                        selectedEmotion = if (newSet.isEmpty() && selectedShadows.isEmpty()) "Equilibrium"
                                        else (selectedShadows.map { it.displayName } + newSet.map { it.displayName }).joinToString(", ")
                                    )
                                }
                            },
                            testTag = "virtue_bubble_${virtue.name.lowercase()}"
                        )
                    }
                }
            }
        }

        // ELEVATED ENTRY / TRANSMUTATION BUTTON (Positioned higher from bottom with generous spacing)
        item {
            Spacer(modifier = Modifier.height(14.dp))

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
                        ambientColor = RadiantGold.copy(alpha = 0.35f),
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
                            text = if (totalSelected > 0) "ENTER & TRANSMUTE ($totalSelected FORCES)" else "ENTER & CALCULATE EQUILIBRIUM",
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
 * Interactive Archetypal Glowing Bubble Component
 */
@Composable
private fun ArchetypeBubble(
    runeSymbol: String,
    name: String,
    subtitle: String,
    color: Color,
    isSelected: Boolean,
    onToggle: () -> Unit,
    testTag: String
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1.0f,
        animationSpec = tween(180, easing = FastOutSlowInEasing),
        label = "bubble_scale"
    )

    val animatedBg by animateColorAsState(
        targetValue = if (isSelected) color.copy(alpha = 0.28f) else Color(0xFF130E26),
        animationSpec = tween(180),
        label = "bubble_bg"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) color else color.copy(alpha = 0.35f),
        animationSpec = tween(180),
        label = "bubble_border"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(18.dp))
            .background(
                brush = if (isSelected) {
                    Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = 0.38f),
                            Color(0xFF160F2C)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF160F2A),
                            Color(0xFF100A20)
                        )
                    )
                }
            )
            .border(
                width = if (isSelected) 1.8.dp else 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            )
            .padding(horizontal = 14.dp, vertical = 11.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Rune Symbol with luminous aura
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) color.copy(alpha = 0.25f) else Color(0xFF1C1335))
                    .border(0.6.dp, if (isSelected) color else Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = runeSymbol,
                    fontSize = 14.sp
                )
            }

            Column {
                Text(
                    text = name,
                    fontSize = 13.sp,
                    color = if (isSelected) color else TextPrimary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    fontSize = 9.5.sp,
                    color = if (isSelected) color.copy(alpha = 0.85f) else TextMuted
                )
            }

            if (isSelected) {
                Spacer(modifier = Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.Black,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
    }
}
