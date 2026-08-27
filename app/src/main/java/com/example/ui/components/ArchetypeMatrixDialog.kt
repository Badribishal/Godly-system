package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.engine.MatrixTier
import com.example.data.engine.SoulProgressionEngine
import com.example.data.model.AdvancedArchetype
import com.example.data.model.AdvancedArchetypesCatalog
import com.example.data.model.ArchetypeCategory
import com.example.data.model.SoulIdentity
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ArchetypeMatrixDialog(
    soul: SoulIdentity,
    onAttune: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<ArchetypeCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val currentTier = remember(soul.soulLevel) {
        MatrixTier.fromLevel(soul.soulLevel)
    }

    val maxExp = remember(soul.soulLevel) {
        SoulProgressionEngine.expRequiredForLevel(soul.soulLevel)
    }

    val expProgress = (soul.soulExp.toFloat() / maxExp.toFloat()).coerceIn(0f, 1f)

    val archetypes = remember(selectedCategory) {
        if (selectedCategory == null) {
            AdvancedArchetypesCatalog.ALL_ARCHETYPES
        } else {
            AdvancedArchetypesCatalog.ALL_ARCHETYPES.filter { it.category == selectedCategory }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    BorderStroke(
                        1.2.dp,
                        Brush.verticalGradient(
                            listOf(
                                Color(currentTier.colorHex).copy(alpha = 0.8f),
                                CelestialAmethyst.copy(alpha = 0.4f),
                                SurfaceCardBorder
                            )
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .testTag("archetype_matrix_dialog"),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.98f),
            shadowElevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Header Bar
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
                                .background(Color(currentTier.colorHex).copy(alpha = 0.2f))
                                .border(1.dp, Color(currentTier.colorHex), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = currentTier.rune, fontSize = 20.sp)
                        }

                        Column {
                            Text(
                                text = "SOUL ARCHETYPE MATRIX",
                                style = MaterialTheme.typography.titleMedium,
                                color = RadiantGold,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "Level Up to Unlock Advanced Transmutations",
                                fontSize = 11.sp,
                                color = CelestialAmethystLight
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("close_archetypes_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Soul Matrix Level & EXP Progression Hero Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            BorderStroke(
                                1.dp,
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(currentTier.colorHex).copy(alpha = 0.6f),
                                        CelestialAmethyst.copy(alpha = 0.3f)
                                    )
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
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
                                Surface(
                                    color = Color(currentTier.colorHex).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(currentTier.colorHex))
                                ) {
                                    Text(
                                        text = "LV. ${soul.soulLevel}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(currentTier.colorHex),
                                        fontSize = 12.sp
                                    )
                                }

                                Text(
                                    text = "Tier ${currentTier.romanNumeral}: ${currentTier.title}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "${soul.soulExp} / $maxExp EXP",
                                style = MaterialTheme.typography.labelSmall,
                                color = EtherealCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // EXP Progress Bar
                        LinearProgressIndicator(
                            progress = { expProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(currentTier.colorHex),
                            trackColor = Color(0xFF1E1635)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total EXP: ${soul.totalSoulExp} • Unlocked Archetypes: ${soul.unlockedArchetypeIds.size}/${AdvancedArchetypesCatalog.ALL_ARCHETYPES.size}",
                                fontSize = 10.5.sp,
                                color = TextMuted
                            )

                            Text(
                                text = "${(expProgress * 100).toInt()}% to Lv. ${soul.soulLevel + 1}",
                                fontSize = 10.5.sp,
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    item {
                        CategoryChip(
                            label = "All (${AdvancedArchetypesCatalog.ALL_ARCHETYPES.size})",
                            rune = "🌌",
                            isSelected = selectedCategory == null,
                            onClick = { selectedCategory = null }
                        )
                    }

                    items(ArchetypeCategory.entries) { cat ->
                        val count = AdvancedArchetypesCatalog.ALL_ARCHETYPES.count { it.category == cat }
                        CategoryChip(
                            label = "${cat.displayName} ($count)",
                            rune = cat.rune,
                            isSelected = selectedCategory == cat,
                            accentColor = Color(cat.colorHex),
                            onClick = { selectedCategory = cat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Archetypes List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(archetypes, key = { it.id }) { arch ->
                        val isUnlocked = soul.soulLevel >= arch.requiredLevel || soul.unlockedArchetypeIds.contains(arch.id)
                        val isAttuned = soul.attunedArchetypeId == arch.id

                        ArchetypeCard(
                            archetype = arch,
                            currentLevel = soul.soulLevel,
                            isUnlocked = isUnlocked,
                            isAttuned = isAttuned,
                            onAttune = { onAttune(arch.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    rune: String,
    isSelected: Boolean,
    accentColor: Color = RadiantGold,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) accentColor.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) accentColor else SurfaceCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = rune, fontSize = 12.sp)
            Text(
                text = label,
                fontSize = 11.5.sp,
                color = if (isSelected) accentColor else TextPrimary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ArchetypeCard(
    archetype: AdvancedArchetype,
    currentLevel: Int,
    isUnlocked: Boolean,
    isAttuned: Boolean,
    onAttune: () -> Unit
) {
    val accentColor = Color(archetype.accentColorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(
                    if (isAttuned) 1.5.dp else 1.dp,
                    if (isAttuned) accentColor else if (isUnlocked) SurfaceCardBorder else SurfaceCardBorder.copy(alpha = 0.4f)
                ),
                RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isAttuned) accentColor.copy(alpha = 0.08f) else SurfaceCard
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Sigil, Title, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = if (isUnlocked) 0.18f else 0.06f))
                            .border(1.dp, if (isUnlocked) accentColor else Color.Gray.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = archetype.sigilIcon,
                            fontSize = 22.sp,
                            color = if (isUnlocked) Color.Unspecified else Color.Gray
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = archetype.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) TextPrimary else TextMuted
                            )
                            if (isAttuned) {
                                Surface(
                                    color = accentColor.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, accentColor)
                                ) {
                                    Text(
                                        text = "ATTUNED",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = accentColor
                                    )
                                }
                            }
                        }

                        Text(
                            text = "${archetype.subtitle} • ${archetype.element}",
                            fontSize = 11.sp,
                            color = if (isUnlocked) CelestialAmethystLight else TextMuted
                        )
                    }
                }

                // Unlock level or Status pill
                if (!isUnlocked) {
                    Surface(
                        color = Color(0xFF1E1428),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFF4A3E62))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Requires Lv. ${archetype.requiredLevel}",
                                fontSize = 10.5.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Lore Snippet
            Text(
                text = "« ${archetype.lore} »",
                fontSize = 11.sp,
                color = if (isUnlocked) TextPrimary.copy(alpha = 0.85f) else TextMuted,
                fontFamily = FontFamily.Serif
            )

            // Passive Perk Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF100A1C))
                    .border(1.dp, accentColor.copy(alpha = if (isUnlocked) 0.35f else 0.15f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Boon",
                        tint = if (isUnlocked) accentColor else TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Passive Boon: ${archetype.passivePerk}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isUnlocked) TextPrimary else TextMuted
                    )
                }
            }

            // Action / Attunement Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = archetype.requiredForcesHint,
                    fontSize = 10.sp,
                    color = TextMuted
                )

                if (isUnlocked && !isAttuned) {
                    Button(
                        onClick = onAttune,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor.copy(alpha = 0.25f),
                            contentColor = accentColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, accentColor),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = "Attune Archetype",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (isAttuned) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Active",
                            tint = accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Active Resonance",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                }
            }
        }
    }
}
