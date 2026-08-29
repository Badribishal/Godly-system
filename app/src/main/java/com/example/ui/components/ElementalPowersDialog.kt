package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.scale
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
import com.example.data.model.ElementType
import com.example.data.model.ElementalPower
import com.example.data.model.ElementalPowersCatalog
import com.example.data.model.PowerCategory
import com.example.data.model.SoulIdentity
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ElementalPowersDialog(
    soul: SoulIdentity,
    selectedElementFilter: ElementType?,
    selectedCategoryFilter: PowerCategory?,
    onDismiss: () -> Unit,
    onSelectElementFilter: (ElementType?) -> Unit,
    onSelectCategoryFilter: (PowerCategory?) -> Unit,
    onEquipPower: (String, PowerCategory) -> Unit,
    onSetPrimaryElement: (String) -> Unit,
    onTrainMastery: (String, Int) -> Unit,
    onChannelArt: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val primaryElement = remember(soul.primaryElement) { ElementType.fromString(soul.primaryElement) }

    val infiniteTransition = rememberInfiniteTransition(label = "elemental_dialog_anim")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    // Filter powers list
    val allPowers = remember { ElementalPowersCatalog.ALL_POWERS }
    val filteredPowers = remember(selectedElementFilter, selectedCategoryFilter, searchQuery) {
        allPowers.filter { power ->
            val matchesElement = selectedElementFilter == null || power.element == selectedElementFilter
            val matchesCategory = selectedCategoryFilter == null || power.category == selectedCategoryFilter
            val matchesSearch = searchQuery.isBlank() ||
                    power.name.contains(searchQuery, ignoreCase = true) ||
                    power.description.contains(searchQuery, ignoreCase = true) ||
                    power.combatEffect.contains(searchQuery, ignoreCase = true) ||
                    power.tags.any { it.contains(searchQuery, ignoreCase = true) }
            matchesElement && matchesCategory && matchesSearch
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.94f)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(primaryElement.colorHex).copy(alpha = 0.8f),
                            CelestialAmethyst.copy(alpha = 0.6f),
                            RadiantGold.copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            color = Color(0xFF070A14)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(primaryElement.colorHex).copy(alpha = 0.2f))
                                .border(1.dp, Color(primaryElement.colorHex), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(primaryElement.runeSymbol, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Elemental Powers & Arts",
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(primaryElement.colorHex).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${allPowers.size}+ Arts",
                                        color = Color(primaryElement.colorHex),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Attuned: ${primaryElement.displayName} • ${primaryElement.masteryTitle}",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_elemental_powers_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Active Quick-Slots Ribbon
                EquippedSlotsRibbon(
                    soul = soul,
                    onUnequip = { cat -> onEquipPower("none", cat) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("search_elemental_powers"),
                    placeholder = { Text("Search 50+ traits, attacks, arts, support, healing...", fontSize = 12.sp, color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RadiantGold,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedContainerColor = Color(0xFF0E1322),
                        unfocusedContainerColor = Color(0xFF0E1322),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Element Tabs Horizontal Scroll (14 elements + ALL)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    item {
                        ElementFilterChip(
                            label = "All Elements",
                            symbol = "🌌",
                            isSelected = selectedElementFilter == null,
                            color = RadiantGold,
                            onClick = { onSelectElementFilter(null) }
                        )
                    }
                    items(ElementType.entries) { element ->
                        ElementFilterChip(
                            label = element.displayName,
                            symbol = element.runeSymbol,
                            isSelected = selectedElementFilter == element,
                            color = Color(element.colorHex),
                            onClick = { onSelectElementFilter(element) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Filter Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    item {
                        CategoryFilterChip(
                            label = "All Roles",
                            symbol = "✦",
                            isSelected = selectedCategoryFilter == null,
                            color = EtherealCyan,
                            onClick = { onSelectCategoryFilter(null) }
                        )
                    }
                    items(PowerCategory.entries) { category ->
                        CategoryFilterChip(
                            label = category.displayName,
                            symbol = category.runeSymbol,
                            isSelected = selectedCategoryFilter == category,
                            color = Color(category.colorHex),
                            onClick = { onSelectCategoryFilter(category) }
                        )
                    }
                }

                // If element filter selected, show Attune Primary Element banner
                if (selectedElementFilter != null && selectedElementFilter != primaryElement) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSetPrimaryElement(selectedElementFilter.displayName) },
                        color = Color(selectedElementFilter.colorHex).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(selectedElementFilter.colorHex).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(selectedElementFilter.runeSymbol, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Attune as Primary Element",
                                        color = Color(selectedElementFilter.colorHex),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = selectedElementFilter.description,
                                        color = TextMuted,
                                        fontSize = 10.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                            Text(
                                text = "Attune ›",
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Powers Lazy Column
                if (filteredPowers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔮", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No matching elemental arts found.",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredPowers, key = { it.id }) { power ->
                            val masteryTier = soul.powerMasteryMap[power.id] ?: 1
                            val isEquipped = when (power.category) {
                                PowerCategory.ATTACK_ART -> soul.equippedAttackId == power.id
                                PowerCategory.MANIPULATION_ART -> soul.equippedManipulationId == power.id
                                PowerCategory.SUPPORT_CLASS -> soul.equippedSupportId == power.id
                                PowerCategory.HEALING_CLASS -> soul.equippedHealingId == power.id
                                PowerCategory.PASSIVE_TRAIT -> soul.equippedTraitId == power.id
                            }

                            PowerCardItem(
                                power = power,
                                masteryTier = masteryTier,
                                isEquipped = isEquipped,
                                currentQi = soul.currentQi,
                                onEquip = { onEquipPower(power.id, power.category) },
                                onUnequip = { onEquipPower("none", power.category) },
                                onTrainMastery = { onTrainMastery(power.id, 50) },
                                onChannel = { onChannelArt(power.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EquippedSlotsRibbon(
    soul: SoulIdentity,
    onUnequip: (PowerCategory) -> Unit
) {
    val attackPower = remember(soul.equippedAttackId) { ElementalPowersCatalog.getPowerById(soul.equippedAttackId) }
    val manipulationPower = remember(soul.equippedManipulationId) { ElementalPowersCatalog.getPowerById(soul.equippedManipulationId) }
    val supportPower = remember(soul.equippedSupportId) { ElementalPowersCatalog.getPowerById(soul.equippedSupportId) }
    val healingPower = remember(soul.equippedHealingId) { ElementalPowersCatalog.getPowerById(soul.equippedHealingId) }

    val activeCount = listOfNotNull(attackPower, manipulationPower, supportPower, healingPower).size

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1426)),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceCardBorder)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE POWER SLOTS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$activeCount / 4 Slots",
                    fontSize = 10.sp,
                    color = RadiantGoldBright,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val slotItems = listOf(
                    Triple(PowerCategory.ATTACK_ART, attackPower, "Attack"),
                    Triple(PowerCategory.MANIPULATION_ART, manipulationPower, "Manipulate"),
                    Triple(PowerCategory.SUPPORT_CLASS, supportPower, "Support"),
                    Triple(PowerCategory.HEALING_CLASS, healingPower, "Healing")
                )

                slotItems.forEach { (cat, equippedPower, defaultLabel) ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(enabled = equippedPower != null) { onUnequip(cat) },
                        color = if (equippedPower != null) Color(equippedPower.colorHex).copy(alpha = 0.18f) else Color(0xFF161C30),
                        border = androidx.compose.foundation.BorderStroke(
                            0.8.dp,
                            if (equippedPower != null) Color(equippedPower.colorHex).copy(alpha = 0.6f) else SurfaceCardBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = equippedPower?.runeSymbol ?: cat.runeSymbol,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = equippedPower?.name ?: defaultLabel,
                                fontSize = 9.sp,
                                color = if (equippedPower != null) Color(equippedPower.colorHex) else TextMuted,
                                fontWeight = if (equippedPower != null) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ElementFilterChip(
    label: String,
    symbol: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = if (isSelected) color.copy(alpha = 0.25f) else Color(0xFF13182C),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.2.dp else 0.6.dp,
            color = if (isSelected) color else SurfaceCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(symbol, fontSize = 12.sp)
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) color else TextMuted
            )
        }
    }
}

@Composable
private fun CategoryFilterChip(
    label: String,
    symbol: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = if (isSelected) color.copy(alpha = 0.22f) else Color(0xFF101526),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.dp else 0.5.dp,
            color = if (isSelected) color else SurfaceCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(symbol, fontSize = 11.sp)
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) color else TextSecondary
            )
        }
    }
}

@Composable
private fun PowerCardItem(
    power: ElementalPower,
    masteryTier: Int,
    isEquipped: Boolean,
    currentQi: Int,
    onEquip: () -> Unit,
    onUnequip: () -> Unit,
    onTrainMastery: () -> Unit,
    onChannel: () -> Unit
) {
    val isTrait = power.category == PowerCategory.PASSIVE_TRAIT

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("power_card_${power.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isEquipped) 1.2.dp else 0.8.dp,
            color = if (isEquipped) Color(power.colorHex) else SurfaceCardBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Rune, Name, Rarity & Element Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(power.colorHex).copy(alpha = 0.18f))
                            .border(1.dp, Color(power.colorHex).copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(power.runeSymbol, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = power.name,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = power.element.displayName,
                                color = Color(power.element.colorHex),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("•", color = TextMuted, fontSize = 10.sp)
                            Text(
                                text = power.category.displayName,
                                color = Color(power.category.colorHex),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Mastery Stars & Rarity
                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        color = Color(power.colorHex).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(0.6.dp, Color(power.colorHex).copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = power.rarity,
                            color = Color(power.colorHex),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row {
                        repeat(5) { starIdx ->
                            Text(
                                text = if (starIdx < masteryTier) "★" else "☆",
                                color = if (starIdx < masteryTier) RadiantGoldBright else TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = power.description,
                color = TextSecondary,
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Combat & Mind Effects Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0C101F),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(0.6.dp, Color(0xFF1E2742))
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text("⚔️ ", fontSize = 11.sp)
                        Text(
                            text = power.combatEffect,
                            color = Color(0xFFE2E8F0),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.Top) {
                        Text("🧘 ", fontSize = 11.sp)
                        Text(
                            text = power.mindSpiritEffect,
                            color = CelestialAmethystLight,
                            fontSize = 10.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stat & Cost Pills Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (power.qiCost > 0) {
                    Surface(
                        color = EtherealCyan.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Cost: ${power.qiCost} Qi",
                            color = EtherealCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                if (power.cooldownTurns > 0) {
                    Surface(
                        color = Color(0xFFF59E0B).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${power.cooldownTurns} Turn CD",
                            color = Color(0xFFF59E0B),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                power.tags.take(2).forEach { tag ->
                    Surface(
                        color = Color(0xFF334155).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = tag,
                            color = TextMuted,
                            fontSize = 9.5.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isTrait) {
                    // Equip / Unequip Button
                    Button(
                        onClick = { if (isEquipped) onUnequip() else onEquip() },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("equip_power_btn_${power.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEquipped) Color(0xFF1E293B) else Color(power.colorHex)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isEquipped) "Unequip Art" else "Equip to Slot",
                            color = if (isEquipped) TextMuted else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    }

                    // Channel / Cast Button
                    OutlinedButton(
                        onClick = onChannel,
                        enabled = currentQi >= power.qiCost,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .testTag("channel_power_btn_${power.id}"),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(power.colorHex).copy(alpha = 0.7f))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⚡", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Channel Art",
                                color = if (currentQi >= power.qiCost) Color(power.colorHex) else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                } else {
                    // Passive Trait Inherent Status
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        color = Color(0xFF1E1B4B),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, CelestialAmethyst)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "🧬 Inherent Bloodline Trait Active",
                                color = CelestialAmethystLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Train Mastery Button (if < 5 stars)
                if (masteryTier < 5) {
                    OutlinedButton(
                        onClick = onTrainMastery,
                        enabled = currentQi >= 50,
                        modifier = Modifier
                            .weight(0.85f)
                            .height(38.dp)
                            .testTag("train_mastery_btn_${power.id}"),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "Train (50 Qi)",
                            color = if (currentQi >= 50) RadiantGoldBright else TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
