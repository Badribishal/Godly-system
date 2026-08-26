package com.example.ui.screens.soul

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.ShadowType
import com.example.data.model.SoulIdentity
import com.example.data.model.VirtueType
import com.example.ui.components.IdentityHeader
import com.example.ui.components.ShadowItemRow
import com.example.ui.components.SoulRadarChart
import com.example.ui.components.VirtueItemRow
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

@Composable
fun SoulScreen(
    soul: SoulIdentity,
    onBack: () -> Unit,
    onOpenWardrobe: () -> Unit = {},
    onOpenHistory: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Shadows, 1: Virtues, 2: Evolution Matrix
    var selectedShadowForDetail by remember { mutableStateOf<ShadowType?>(null) }
    var selectedVirtueForDetail by remember { mutableStateOf<VirtueType?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("soul_screen_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
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
                        modifier = Modifier.testTag("soul_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column {
                        Text(
                            text = "SOUL MATRIX",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "The Astral Blueprint of the Vessel",
                            style = MaterialTheme.typography.bodySmall,
                            color = CelestialAmethystLight
                        )
                    }
                }

                // Shards & Wardrobe Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, EtherealCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .clickable { onOpenWardrobe() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "💎 ${soul.soulShards}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EtherealCyan)
                        Text(text = "Wardrobe", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Identity Card
        item {
            IdentityHeader(soul = soul)
        }

        // Radar Visualization Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedTab == 0) "THE SEVEN SHADOWS GEOMETRY" else "THE SEVEN VIRTUES GEOMETRY",
                            fontSize = 11.sp,
                            color = if (selectedTab == 0) Color(0xFFEF4444) else RadiantGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = soul.resonanceFrequency,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    SoulRadarChart(
                        shadowScores = soul.shadowScores,
                        virtueScores = soul.virtueScores,
                        isShadowMode = selectedTab == 0
                    )
                }
            }
        }

        // Tab Selector (Shadows vs Virtues vs Evolution)
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceCard,
                contentColor = RadiantGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = if (selectedTab == 0) Color(0xFFEF4444) else if (selectedTab == 1) RadiantGold else CelestialAmethyst
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "SHADOWS (7)",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "VIRTUES (7)",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "EVOLUTION",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // The Seven Shadows
                items(ShadowType.values().size) { idx ->
                    val shadow = ShadowType.values()[idx]
                    val score = soul.shadowScores[shadow] ?: 30
                    ShadowItemRow(
                        shadow = shadow,
                        score = score,
                        isDominant = soul.dominantShadow == shadow,
                        onClick = { selectedShadowForDetail = shadow }
                    )
                }
            }
            1 -> {
                // The Seven Virtues
                items(VirtueType.values().size) { idx ->
                    val virtue = VirtueType.values()[idx]
                    val score = soul.virtueScores[virtue] ?: 30
                    VirtueItemRow(
                        virtue = virtue,
                        score = score,
                        isDominant = soul.dominantVirtue == virtue,
                        onClick = { selectedVirtueForDetail = virtue }
                    )
                }
            }
            2 -> {
                // Evolution Matrix & Secret Archetypes
                item {
                    EvolutionMatrixCard(
                        soul = soul,
                        onOpenHistory = onOpenHistory
                    )
                }

                // Strengths & Weaknesses
                item {
                    ArchetypeTraitsCard(soul = soul)
                }
            }
        }
    }

    // Shadow Detail Dialog
    selectedShadowForDetail?.let { shadow ->
        ForceDetailDialog(
            name = shadow.displayName,
            title = shadow.title,
            rune = shadow.runeSymbol,
            score = soul.shadowScores[shadow] ?: 30,
            description = shadow.description,
            constructiveAspect = shadow.constructiveAspect,
            excessWarning = shadow.excessWarning,
            color = Color(shadow.colorHex),
            isShadow = true,
            onDismiss = { selectedShadowForDetail = null }
        )
    }

    // Virtue Detail Dialog
    selectedVirtueForDetail?.let { virtue ->
        ForceDetailDialog(
            name = virtue.displayName,
            title = virtue.title,
            rune = virtue.runeSymbol,
            score = soul.virtueScores[virtue] ?: 30,
            description = virtue.description,
            constructiveAspect = virtue.constructiveAspect,
            excessWarning = virtue.excessWarning,
            color = Color(virtue.colorHex),
            isShadow = false,
            onDismiss = { selectedVirtueForDetail = null }
        )
    }
}

@Composable
private fun EvolutionMatrixCard(
    soul: SoulIdentity,
    onOpenHistory: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, CelestialAmethyst.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "METAMORPHIC BRANCHES",
                    fontSize = 11.sp,
                    color = RadiantGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Unlocked Shifts: ${soul.evolutionHistoryCount}",
                    fontSize = 11.sp,
                    color = TextAmethyst
                )
            }

            // Current Active Stage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1B1433))
                    .border(1.dp, RadiantGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "⭐", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "CURRENT VESSEL",
                            fontSize = 10.sp,
                            color = RadiantGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${soul.race} • ${soul.advancedClass ?: soul.className}",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // View Soul History Log Action Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .clickable { onOpenHistory() }
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "📜", fontSize = 18.sp)
                        Column {
                            Text(
                                text = "SOUL HISTORY CHRONICLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "View chronological persona shifts & daily catalysts",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Text(
                        text = "VIEW →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Possible Evolution
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF140F26))
                    .border(1.dp, CelestialAmethyst.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "🌀", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "POTENTIAL EVOLUTION PATH",
                            fontSize = 10.sp,
                            color = CelestialAmethystLight,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = soul.possibleEvolution,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextAmethyst,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Mysterious Locked Secret Paths
            val secretArchetypes = listOf(
                "Solar Seraph • Divine Arbiter (Humility + Charity + Gratitude, Humanity > 75)",
                "The Paradox • Nexus Sovereign (Equal High Light & Shadow Resonance)",
                "The Worldbreaker • Primordial Titan (High Wrath + Courage, Humanity < 40)",
                "Vampire Progenitor • Crimson Sovereign (High Pride + Greed, Humanity < 38)",
                "Eldritch Hybrid • Cosmic Harbinger (High Gluttony + Envy, Stability < 45)",
                "Ancient Dragon • Astral Wyrm Sovereign (Pride + Courage + Diligence)",
                "Dreamwalker • Twilight Mystic (Sloth + Patience + Charity + Gratitude)",
                "Soulkeeper • Dark Elf Scholar (Gluttony + Diligence + Humility)",
                "Starforged Golem • Adamantine Runesmith (Greed + Diligence + Temperance)",
                "Celestial Kitsune • Starweaver (Desire + Diligence + Charity)"
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "KNOWN SECRET ARCHETYPES",
                    fontSize = 10.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                secretArchetypes.forEach { secret ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0D0A18))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = secret,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ArchetypeTraitsCard(soul: SoulIdentity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "VESSEL ATTRIBUTES & VULNERABILITIES",
                fontSize = 11.sp,
                color = RadiantGold,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            // Strengths
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "CORE STRENGTHS", fontSize = 10.sp, color = Color(0xFF34D399), fontWeight = FontWeight.Bold)
                soul.strengths.forEach { str ->
                    Text(text = "✦ $str", fontSize = 12.sp, color = TextPrimary)
                }
            }

            // Weaknesses
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "SYSTEMIC VULNERABILITIES", fontSize = 10.sp, color = Color(0xFFF87171), fontWeight = FontWeight.Bold)
                soul.weaknesses.forEach { wkn ->
                    Text(text = "⚠ $wkn", fontSize = 12.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun ForceDetailDialog(
    name: String,
    title: String,
    rune: String,
    score: Int,
    description: String,
    constructiveAspect: String,
    excessWarning: String,
    color: Color,
    isShadow: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceCardElevated)
                .border(1.5.dp, color.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = rune, fontSize = 24.sp)
                        Column {
                            Text(
                                text = name.uppercase(),
                                style = MaterialTheme.typography.titleLarge,
                                color = color,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextAmethyst
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )

                // Constructive Aspect Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F1E17))
                        .border(1.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "CONSTRUCTIVE ALCHEMY",
                            fontSize = 10.sp,
                            color = Color(0xFF34D399),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = constructiveAspect,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }

                // Excess Warning Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF261014))
                        .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "DISTORTION IN EXCESS",
                            fontSize = 10.sp,
                            color = Color(0xFFF87171),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = excessWarning,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}
