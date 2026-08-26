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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Soul Record & Force Calibration Dialog in the Sanctuary Vault.
 * Users can enter Seven Heavenly Virtues & Seven Deadly Sins to calculate and define their Godly Identity.
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
    var situationText by remember { mutableStateOf("") }
    var reflectionText by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Duality Calibration, 1: 7 Deadly Sins, 2: 7 Heavenly Virtues
    var showGuideDialog by remember { mutableStateOf(false) }

    if (showGuideDialog) {
        ArchetypeGuideDialog(onDismiss = { showGuideDialog = false })
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                .size(42.dp)
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
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "7 Deadly Sins & 7 Heavenly Virtues Calibration",
                                style = MaterialTheme.typography.bodySmall,
                                color = CelestialAmethystLight,
                                fontSize = 11.sp
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

                // Duality Harmony Meter
                val totalForces = selectedShadows.size + selectedVirtues.size
                val shadowRatio = if (totalForces > 0) selectedShadows.size.toFloat() / totalForces else 0.5f
                val virtueRatio = if (totalForces > 0) selectedVirtues.size.toFloat() / totalForces else 0.5f

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0B1C)),
                    border = BorderStroke(1.dp, CelestialAmethyst.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔥 ${selectedShadows.size} Sins Active",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF87171)
                            )
                            Text(
                                text = if (totalForces == 0) "« Balanced Alignment »"
                                else if (selectedShadows.size > selectedVirtues.size) "« Shadow Dominant Resonance »"
                                else if (selectedVirtues.size > selectedShadows.size) "« Sacred Dominant Resonance »"
                                else "« Equal Duality Equilibrium »",
                                fontSize = 11.sp,
                                color = RadiantGold,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "${selectedVirtues.size} Virtues Active 🌿",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399)
                            )
                        }

                        // Split Gradient Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF221535))
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .weight(shadowRatio.coerceAtLeast(0.01f))
                                        .fillMaxHeight()
                                        .background(Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFF59E0B))))
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(virtueRatio.coerceAtLeast(0.01f))
                                        .fillMaxHeight()
                                        .background(Brush.horizontalGradient(listOf(Color(0xFF10B981), Color(0xFF06B6D4))))
                                )
                            }
                        }
                    }
                }

                // Force View Selector Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF110B1E),
                    contentColor = RadiantGold,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = RadiantGold,
                            height = 2.5.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Dual Matrix", fontSize = 11.5.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("7 Deadly Sins", fontSize = 11.5.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("7 Virtues", fontSize = 11.5.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                // Content Area
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .testTag("record_dialog_content_column"),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    // Tab 0: Dual Matrix (Both Sins and Virtues side by side)
                    if (selectedTab == 0 || selectedTab == 1) {
                        item {
                            Text(
                                text = "✦ SEVEN DEADLY SINS (SHADOW FORCES)",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFFF87171),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                ShadowType.values().forEach { shadow ->
                                    val isSelected = selectedShadows.contains(shadow)
                                    ArchetypeSelectableChip(
                                        title = shadow.displayName,
                                        rune = shadow.runeSymbol,
                                        aspect = shadow.title,
                                        glowColor = Color(shadow.colorHex),
                                        isSelected = isSelected,
                                        onClick = {
                                            selectedShadows = if (isSelected) {
                                                selectedShadows - shadow
                                            } else {
                                                selectedShadows + shadow
                                            }
                                        },
                                        testTag = "sin_chip_${shadow.name.lowercase()}"
                                    )
                                }
                            }
                        }
                    }

                    if (selectedTab == 0 || selectedTab == 2) {
                        item {
                            Text(
                                text = "✦ SEVEN HEAVENLY VIRTUES (SACRED FORCES)",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF34D399),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                VirtueType.values().forEach { virtue ->
                                    val isSelected = selectedVirtues.contains(virtue)
                                    ArchetypeSelectableChip(
                                        title = virtue.displayName,
                                        rune = virtue.runeSymbol,
                                        aspect = virtue.title,
                                        glowColor = Color(virtue.colorHex),
                                        isSelected = isSelected,
                                        onClick = {
                                            selectedVirtues = if (isSelected) {
                                                selectedVirtues - virtue
                                            } else {
                                                selectedVirtues + virtue
                                            }
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
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "✦ MANIFESTATION CONTEXT (OPTIONAL)",
                                    fontSize = 10.sp,
                                    color = RadiantGold,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                OutlinedTextField(
                                    value = situationText,
                                    onValueChange = { situationText = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                        .testTag("record_situation_input"),
                                    label = { Text("What catalyzed these forces?", fontSize = 11.5.sp, color = TextMuted) },
                                    placeholder = { Text("e.g., Faced intense rivalry; channeled envy into focused mastery.", fontSize = 11.sp, color = TextMuted.copy(alpha = 0.6f)) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RadiantGold,
                                        unfocusedBorderColor = SurfaceCardBorder,
                                        focusedContainerColor = Color(0xFF0B0716),
                                        unfocusedContainerColor = Color(0xFF0B0716),
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                OutlinedTextField(
                                    value = reflectionText,
                                    onValueChange = { reflectionText = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                        .testTag("record_reflection_input"),
                                    label = { Text("Soul Insight / Inner Decree", fontSize = 11.5.sp, color = TextMuted) },
                                    placeholder = { Text("e.g., My pride protects my boundary without arrogance.", fontSize = 11.sp, color = TextMuted.copy(alpha = 0.6f)) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EtherealCyan,
                                        unfocusedBorderColor = SurfaceCardBorder,
                                        focusedContainerColor = Color(0xFF0B0716),
                                        unfocusedContainerColor = Color(0xFF0B0716),
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }
                }

                // Submit Button
                Button(
                    onClick = {
                        onSubmit(selectedShadows, selectedVirtues, situationText, reflectionText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("calculate_define_me_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RadiantGold,
                        contentColor = Color(0xFF1E1102)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF1E1102)
                        )
                        Text(
                            text = "✦ CALCULATE & DEFINE GODLY IDENTITY",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArchetypeSelectableChip(
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
        animationSpec = tween(200), label = "chip_bg"
    )

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        color = animatedBg,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.2.dp,
            if (isSelected) glowColor else SurfaceCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = rune, fontSize = 14.sp)
            Column {
                Text(
                    text = title,
                    fontSize = 11.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) TextPrimary else TextSecondary
                )
                Text(
                    text = aspect,
                    fontSize = 9.sp,
                    color = if (isSelected) glowColor else TextMuted
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = glowColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
