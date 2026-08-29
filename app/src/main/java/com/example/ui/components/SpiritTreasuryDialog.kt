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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.SoulIdentity
import com.example.data.model.SpiritItemType
import com.example.data.model.SpiritShopItem
import com.example.data.model.SpiritTreasuryCatalog
import com.example.ui.theme.CelestialAmethyst
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

enum class TreasuryTab(val title: String, val rune: String) {
    PILLS("Elixirs & Pills", "🧪"),
    RELICS("Spirit Relics", "🪞"),
    SUTRAS("Ancient Sutras", "📜"),
    CAULDRON("Alchemy Cauldron", "🔥")
}

@Composable
fun SpiritTreasuryDialog(
    soul: SoulIdentity,
    onDismiss: () -> Unit,
    onPurchaseItem: (String) -> Unit,
    onEquipArtifact: (String) -> Unit,
    onRefineCauldron: (Int) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = TreasuryTab.values()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            RadiantGold.copy(alpha = 0.8f),
                            CelestialAmethyst.copy(alpha = 0.6f),
                            EtherealCyan.copy(alpha = 0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            color = Color(0xFF0B0D18)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
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
                                .background(RadiantGold.copy(alpha = 0.2f))
                                .border(1.dp, RadiantGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🏛️", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Spirit Treasury",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Exchange Gems for Daoist Treasures",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Gem Counter Chip
                        Surface(
                            color = RadiantGold.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.6f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💎", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${soul.soulShards}",
                                    color = RadiantGoldBright,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_treasury_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary,
                    edgePadding = 0.dp,
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = selectedTabIndex == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(tab.rune, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = tab.title,
                                        color = if (isSelected) TextGold else TextMuted,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Content View
                when (tabs[selectedTabIndex]) {
                    TreasuryTab.PILLS -> {
                        val pillItems = remember {
                            SpiritTreasuryCatalog.ITEMS.filter { it.type == SpiritItemType.ELIXIR_PILL }
                        }
                        TreasuryItemList(
                            items = pillItems,
                            soul = soul,
                            onPurchase = onPurchaseItem,
                            onEquip = onEquipArtifact
                        )
                    }
                    TreasuryTab.RELICS -> {
                        val relicItems = remember {
                            SpiritTreasuryCatalog.ITEMS.filter { it.type == SpiritItemType.SPIRIT_ARTIFACT }
                        }
                        TreasuryItemList(
                            items = relicItems,
                            soul = soul,
                            onPurchase = onPurchaseItem,
                            onEquip = onEquipArtifact
                        )
                    }
                    TreasuryTab.SUTRAS -> {
                        val sutraItems = remember {
                            SpiritTreasuryCatalog.ITEMS.filter { 
                                it.type == SpiritItemType.SUTRA_MANUAL || it.type == SpiritItemType.TRIBULATION_TALISMAN 
                            }
                        }
                        TreasuryItemList(
                            items = sutraItems,
                            soul = soul,
                            onPurchase = onPurchaseItem,
                            onEquip = onEquipArtifact
                        )
                    }
                    TreasuryTab.CAULDRON -> {
                        CauldronAlchemyView(
                            soul = soul,
                            onRefine = onRefineCauldron
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TreasuryItemList(
    items: List<SpiritShopItem>,
    soul: SoulIdentity,
    onPurchase: (String) -> Unit,
    onEquip: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items) { item ->
            val isOwned = soul.unlockedArtifactIds.contains(item.id)
            val isEquipped = soul.equippedArtifactId == item.id
            val canAfford = soul.soulShards >= item.gemCost

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SurfaceCard
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isEquipped) Color(item.colorHex) else SurfaceCardBorder
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(item.colorHex).copy(alpha = 0.15f))
                                    .border(1.dp, Color(item.colorHex).copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item.iconEmoji, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = item.name,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = item.rarity,
                                    color = Color(item.colorHex),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Price / Owned Badge
                        if (isOwned && item.isPermanentArtifact) {
                            Surface(
                                color = CelestialAmethyst.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CelestialAmethyst)
                            ) {
                                Text(
                                    text = if (isEquipped) "Equipped" else "Owned",
                                    color = TextAmethyst,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            Surface(
                                color = RadiantGold.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("💎", fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${item.gemCost}",
                                        color = TextGold,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = item.description,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Stat Tags Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (item.qiBonus > 0) {
                            StatPill(label = "+${item.qiBonus} Qi", color = EtherealCyan)
                        }
                        if (item.maxQiBonus > 0) {
                            StatPill(label = "+${item.maxQiBonus} Max Qi", color = Color(0xFF34D399))
                        }
                        if (item.breakthroughBonusPercent > 0) {
                            StatPill(label = "+${item.breakthroughBonusPercent}% Breakthrough", color = RadiantGold)
                        }
                        if (item.stabilityBonus > 0) {
                            StatPill(label = "+${item.stabilityBonus} Stability", color = CelestialAmethyst)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Button
                    if (isOwned && item.isPermanentArtifact) {
                        if (item.type == SpiritItemType.SPIRIT_ARTIFACT) {
                            OutlinedButton(
                                onClick = {
                                    if (isEquipped) onEquip("artifact_none") else onEquip(item.id)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isEquipped) CelestialAmethyst else Color(item.colorHex)
                                )
                            ) {
                                Text(
                                    text = if (isEquipped) "Unequip Relic" else "Equip Relic",
                                    color = if (isEquipped) TextMuted else TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            }
                        } else {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp),
                                color = Color(0xFF1E293B),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "✓ Bound to Soul Matrix",
                                        color = Color(0xFF34D399),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = { onPurchase(item.id) },
                            enabled = canAfford,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RadiantGold,
                                disabledContainerColor = Color(0xFF334155)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (canAfford) "Purchase & Ingest (💎 ${item.gemCost} Gems)" else "Insufficient Gems (💎 ${item.gemCost})",
                                color = if (canAfford) Color(0xFF0F172A) else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(label: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun CauldronAlchemyView(
    soul: SoulIdentity,
    onRefine: (Int) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cauldron_anim")
    val flameScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Cauldron Visual
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFFF97316).copy(alpha = 0.35f),
                                Color(0xFFDC2626).copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.sweepGradient(
                            listOf(
                                RadiantGold,
                                Color(0xFFF97316),
                                CelestialAmethyst,
                                RadiantGold
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔥",
                    fontSize = 64.sp,
                    modifier = Modifier.scale(flameScale)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Nine-Revolutions Cosmic Cauldron",
                color = RadiantGoldBright,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Feed spiritual gems into the sacred flames to distill random high-grade pills and draw massive Qi surges!",
                color = TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        // Refine Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { onRefine(35) },
                enabled = soul.soulShards >= 35,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("refine_cauldron_35_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEA580C),
                    disabledContainerColor = Color(0xFF334155)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔥", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Standard Distillation",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "💎 35 Gems",
                        color = RadiantGoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Button(
                onClick = { onRefine(80) },
                enabled = soul.soulShards >= 80,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("refine_cauldron_80_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RadiantGold,
                    disabledContainerColor = Color(0xFF334155)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✨", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Grand Cosmic Distillation",
                            color = Color(0xFF0F172A),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "💎 80 Gems",
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
