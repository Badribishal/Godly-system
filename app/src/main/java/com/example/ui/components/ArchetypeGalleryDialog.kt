package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.engine.MatrixTier
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

enum class UnlockFilter(val label: String) {
    ALL("All"),
    UNLOCKED("Unlocked"),
    LOCKED("Locked")
}

@Composable
fun ArchetypeGalleryDialog(
    soul: SoulIdentity,
    onAttune: (archetypeId: String) -> Unit,
    onDismiss: () -> Unit
) {
    val allArchetypes = remember { AdvancedArchetypesCatalog.ALL_ARCHETYPES }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedUnlockFilter by remember { mutableStateOf(UnlockFilter.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val categories = remember {
        listOf<ArchetypeCategory?>(null) + ArchetypeCategory.entries
    }

    val unlockedCount = remember(soul.unlockedArchetypeIds, soul.soulLevel) {
        allArchetypes.count { it.requiredLevel <= soul.soulLevel || soul.unlockedArchetypeIds.contains(it.id) }
    }

    val totalClasses = remember {
        allArchetypes.map { it.characterClass }.distinct().size
    }

    val totalRaces = remember {
        allArchetypes.map { it.celestialRace }.distinct().size
    }

    val filteredList = remember(selectedCategoryIndex, selectedUnlockFilter, searchQuery, soul) {
        val selectedCat = categories[selectedCategoryIndex]
        allArchetypes.filter { arch ->
            val isUnlocked = arch.requiredLevel <= soul.soulLevel || soul.unlockedArchetypeIds.contains(arch.id)
            val matchesCat = selectedCat == null || arch.category == selectedCat
            val matchesFilter = when (selectedUnlockFilter) {
                UnlockFilter.ALL -> true
                UnlockFilter.UNLOCKED -> isUnlocked
                UnlockFilter.LOCKED -> !isUnlocked
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                arch.name.contains(searchQuery, ignoreCase = true) ||
                        arch.characterClass.contains(searchQuery, ignoreCase = true) ||
                        arch.celestialRace.contains(searchQuery, ignoreCase = true) ||
                        arch.lore.contains(searchQuery, ignoreCase = true)
            }
            matchesCat && matchesFilter && matchesSearch
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(
                            RadiantGold.copy(alpha = 0.5f),
                            CelestialAmethyst.copy(alpha = 0.3f),
                            SurfaceCardBorder
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .testTag("archetype_gallery_dialog"),
            color = Color(0xFF0C0718),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Header Row
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
                                .border(1.dp, RadiantGold.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏛️", fontSize = 20.sp)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "ARCHETYPE GALLERY",
                                style = MaterialTheme.typography.titleMedium,
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "Classes & Celestial Heritages Codex",
                                fontSize = 11.sp,
                                color = CelestialAmethystLight
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                // Summary Stats Metrics Strip
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = SurfaceCard,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, SurfaceCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$unlockedCount / ${allArchetypes.size}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RadiantGoldBright)
                            Text(text = "Unlocked", fontSize = 9.5.sp, color = TextMuted)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfaceCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$totalClasses Classes", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EtherealCyan)
                            Text(text = "Disciplines", fontSize = 9.5.sp, color = TextMuted)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(SurfaceCardBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$totalRaces Heritages", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CelestialAmethystLight)
                            Text(text = "Celestial Blood", fontSize = 9.5.sp, color = TextMuted)
                        }
                    }
                }

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search class, race, perk or lore...", fontSize = 12.sp, color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = TextMuted, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold.copy(alpha = 0.6f),
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color(0xFF130D22),
                        unfocusedContainerColor = Color(0xFF130D22)
                    ),
                    singleLine = true
                )

                // Category Scrollable Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedCategoryIndex,
                    containerColor = Color.Transparent,
                    contentColor = RadiantGold,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                            height = 2.dp,
                            color = RadiantGold
                        )
                    },
                    divider = {}
                ) {
                    categories.forEachIndexed { index, cat ->
                        Tab(
                            selected = selectedCategoryIndex == index,
                            onClick = { selectedCategoryIndex = index },
                            text = {
                                Text(
                                    text = if (cat == null) "All Realms" else "${cat.rune} ${cat.displayName}",
                                    fontSize = 11.5.sp,
                                    fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedCategoryIndex == index) RadiantGoldBright else TextMuted
                                )
                            }
                        )
                    }
                }

                // Unlock Status Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UnlockFilter.entries.forEach { filter ->
                        val isSelected = selectedUnlockFilter == filter
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedUnlockFilter = filter },
                            color = if (isSelected) RadiantGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) RadiantGold.copy(alpha = 0.6f) else SurfaceCardBorder
                            )
                        ) {
                            Text(
                                text = when (filter) {
                                    UnlockFilter.ALL -> "All (${allArchetypes.size})"
                                    UnlockFilter.UNLOCKED -> "Unlocked ($unlockedCount)"
                                    UnlockFilter.LOCKED -> "Locked (${allArchetypes.size - unlockedCount})"
                                },
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) RadiantGoldBright else TextMuted,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Archetypes List
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No archetypes match the active filter criteria.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Serif
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredList, key = { it.id }) { archetype ->
                            val isUnlocked = archetype.requiredLevel <= soul.soulLevel || soul.unlockedArchetypeIds.contains(archetype.id)
                            val isAttuned = soul.attunedArchetypeId == archetype.id

                            ArchetypeGalleryCard(
                                archetype = archetype,
                                isUnlocked = isUnlocked,
                                isAttuned = isAttuned,
                                currentSoulLevel = soul.soulLevel,
                                onAttune = { onAttune(archetype.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArchetypeGalleryCard(
    archetype: AdvancedArchetype,
    isUnlocked: Boolean,
    isAttuned: Boolean,
    currentSoulLevel: Int,
    onAttune: () -> Unit
) {
    val tier = MatrixTier.fromLevel(archetype.requiredLevel)
    val accentColor = Color(archetype.accentColorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                if (isAttuned) RadiantGoldBright.copy(alpha = 0.8f) else if (isUnlocked) accentColor.copy(alpha = 0.4f) else SurfaceCardBorder,
                RoundedCornerShape(16.dp)
            )
            .testTag("gallery_card_${archetype.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isAttuned) Color(0xFF1B142E) else if (isUnlocked) SurfaceCard else Color(0xFF0F0B18)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Row: Class & Race Badges on Left, Level/Tier on Right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Celestial Heritage Badge
                    Surface(
                        color = accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = archetype.celestialRace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Character Class Badge
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, SurfaceCardBorder)
                    ) {
                        Text(
                            text = archetype.characterClass,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Level / Tier Badge
                Surface(
                    color = Color(tier.colorHex).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(tier.colorHex).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Tier ${tier.romanNumeral} • Lv ${archetype.requiredLevel}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(tier.colorHex),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Main Info: Sigil Icon + Name + Subtitle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isUnlocked) accentColor.copy(alpha = 0.2f) else Color(0xFF1E172E))
                        .border(1.dp, if (isUnlocked) accentColor.copy(alpha = 0.6f) else SurfaceCardBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isUnlocked) {
                        Text(text = archetype.sigilIcon, fontSize = 24.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = archetype.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) TextPrimary else TextMuted,
                            fontSize = 14.sp
                        )
                        if (isAttuned) {
                            Surface(
                                color = RadiantGoldBright,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE CONDUIT",
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = archetype.subtitle,
                        fontSize = 11.sp,
                        color = if (isUnlocked) CelestialAmethystLight else TextMuted
                    )
                }
            }

            // Element, Passive Perk & Dynamic Theme Preview Banner
            val archTheme = remember(archetype.id) {
                com.example.ui.theme.ArchetypeThemeEngine.getThemeForArchetype(archetype.id)
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isUnlocked) Color(0xFF140D24) else Color(0xFF0D0818),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isUnlocked) SurfaceCardBorder else Color(0xFF1F1830))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ELEMENT: ${archetype.element.uppercase()}",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) EtherealCyan else TextMuted
                        )
                        // Dynamic Theme Tone Tag & Color Dots
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (archTheme.isWarm) "🔥 Warm Aura" else if (archTheme.isCoolEthereal) "✨ Cool Ethereal" else "🌿 Primordial",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (archTheme.isWarm) Color(0xFFFDBA74) else if (archTheme.isCoolEthereal) Color(0xFFC4B5FD) else Color(0xFF6EE7B7)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy((-3).dp)) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(archTheme.primary))
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(archTheme.secondary))
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(archTheme.tertiary))
                            }
                        }
                    }
                    Text(
                        text = "✦ Perk: ${archetype.passivePerk}",
                        fontSize = 11.sp,
                        color = if (isUnlocked) RadiantGoldBright else TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Lore Snippet
            Text(
                text = "« ${archetype.lore} »",
                style = MaterialTheme.typography.bodySmall,
                color = if (isUnlocked) TextPrimary.copy(alpha = 0.85f) else TextMuted.copy(alpha = 0.7f),
                fontFamily = FontFamily.Serif,
                lineHeight = 17.sp
            )

            // Bottom Action / Unlock Prerequisite Hint
            if (isUnlocked) {
                if (!isAttuned) {
                    Button(
                        onClick = onAttune,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Attune Form [${archetype.characterClass}]",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            } else {
                // Locked Prerequisite Hint Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF1E1318),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Unlock Requirement",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                            Text(
                                text = "UNLOCK PREREQUISITE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF4444),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = archetype.unlockPrerequisiteHint,
                                fontSize = 11.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
