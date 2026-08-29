package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.engine.SoulResonanceData
import com.example.data.model.IdentityMilestoneBadge
import com.example.data.model.IdentityMilestoneCatalog
import com.example.data.model.SoulIdentity
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.RadiantGoldDim
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextAmethyst
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun IdentityHeader(
    soul: SoulIdentity,
    resonance: SoulResonanceData? = null,
    onOpenArchetypes: (() -> Unit)? = null,
    onOpenQiChamber: (() -> Unit)? = null,
    onOpenTreasury: (() -> Unit)? = null,
    onOpenElementalPowers: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showMilestonesDialog by remember { mutableStateOf(false) }
    val realm = remember(soul.cultivationRealm) { com.example.data.model.CultivationRealm.fromNameOrId(soul.cultivationRealm) }
    val qiRatio = (soul.currentQi.toFloat() / soul.maxQi.toFloat()).coerceIn(0f, 1f)
    val equippedArtifact = remember(soul.equippedArtifactId) {
        com.example.data.model.SpiritTreasuryCatalog.getItemById(soul.equippedArtifactId)
    }
    val badges = remember(soul.race, soul.className, soul.soulShards, soul.evolutionProgress, soul.dominantVirtue, soul.dominantShadow) {
        IdentityMilestoneCatalog.evaluateMilestones(soul, emptyList(), emptyList())
    }
    val unlockedCount = badges.count { it.isUnlocked }

    val currentTier = remember(soul.soulLevel) {
        com.example.data.engine.MatrixTier.fromLevel(soul.soulLevel)
    }
    val currentArchetype = remember(soul.attunedArchetypeId) {
        com.example.data.model.AdvancedArchetypesCatalog.getArchetypeById(soul.attunedArchetypeId)
    }
    val maxExp = remember(soul.soulLevel) {
        com.example.data.engine.SoulProgressionEngine.expRequiredForLevel(soul.soulLevel)
    }
    val expProgress = (soul.soulExp.toFloat() / maxExp.toFloat()).coerceIn(0f, 1f)

    val infiniteTransition = rememberInfiniteTransition(label = "soul_matrix_aura")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val wavePulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_pulse"
    )

    val evolutionProgressAnimated by animateFloatAsState(
        targetValue = soul.evolutionProgress.toFloat() / 100f,
        animationSpec = tween(700),
        label = "evo_progress"
    )

    val humanityAnimated by animateFloatAsState(
        targetValue = soul.humanity.toFloat() / 100f,
        animationSpec = tween(700),
        label = "humanity"
    )

    val resonancePercent = resonance?.percentage ?: ((soul.stability + soul.humanity) / 2).coerceIn(10, 100)
    val resonanceAnimated by animateFloatAsState(
        targetValue = resonancePercent.toFloat() / 100f,
        animationSpec = tween(700),
        label = "resonance_anim"
    )

    // Calculate Duality Balance
    val shadowScore = soul.shadowScores[soul.dominantShadow] ?: 30
    val virtueScore = soul.virtueScores[soul.dominantVirtue] ?: 30
    val totalForces = (shadowScore + virtueScore).coerceAtLeast(1)
    val virtueRatio = (virtueScore.toFloat() / totalForces).coerceIn(0.1f, 0.9f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF140D2B),
                        Color(0xFF0D081D),
                        Color(0xFF070410)
                    )
                )
            )
            .border(
                width = 1.4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        RadiantGoldBright.copy(alpha = glowAlpha),
                        CelestialAmethyst.copy(alpha = 0.6f),
                        EtherealCyan.copy(alpha = 0.4f),
                        SurfaceCardBorder
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
            .testTag("identity_header")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Top Bar: System Sigil, Classification & Alignment Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(RadiantGold.copy(alpha = 0.18f))
                            .border(1.dp, RadiantGold.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Cosmic Sigil",
                            tint = RadiantGoldBright,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Text(
                        text = "VESSEL ASTRAL MATRIX",
                        style = MaterialTheme.typography.labelMedium,
                        color = RadiantGoldBright,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.6.sp
                    )
                }

                // Alignment Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF261D47),
                                    Color(0xFF3B1E54)
                                )
                            )
                        )
                        .border(1.dp, CelestialAmethyst.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = soul.alignment.uppercase(),
                        color = TextGold,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            // Grand Title Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1B1438).copy(alpha = 0.75f))
                    .border(0.8.dp, RadiantGold.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "« ${soul.currentTitle} »",
                    style = MaterialTheme.typography.titleMedium,
                    color = RadiantGoldBright,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 0.5.sp
                )
            }

            // REDESIGNED SOUL MATRIX PROGRESSION & ARCHETYPE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(
                        1.2.dp,
                        Brush.horizontalGradient(
                            listOf(
                                Color(currentTier.colorHex).copy(alpha = 0.8f),
                                RadiantGold.copy(alpha = 0.5f),
                                CelestialAmethyst.copy(alpha = 0.4f)
                            )
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable(enabled = onOpenArchetypes != null) { onOpenArchetypes?.invoke() }
                    .testTag("soul_progression_card"),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F091F)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header: Level Badge, Tier Title, Attuned Archetype Pill
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
                                color = Color(currentTier.colorHex).copy(alpha = 0.22f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(currentTier.colorHex))
                            ) {
                                Text(
                                    text = "LV. ${soul.soulLevel}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(currentTier.colorHex),
                                    fontSize = 12.sp
                                )
                            }

                            Column {
                                Text(
                                    text = "Tier ${currentTier.romanNumeral}: ${currentTier.title}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${soul.soulExp} / $maxExp EXP",
                                    fontSize = 10.sp,
                                    color = EtherealCyan,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Attuned Archetype Pill
                        Surface(
                            color = Color(currentArchetype.accentColorHex).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(currentArchetype.accentColorHex).copy(alpha = 0.7f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = currentArchetype.sigilIcon, fontSize = 12.sp)
                                Text(
                                    text = currentArchetype.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(currentArchetype.accentColorHex)
                                )
                            }
                        }
                    }

                    // Linear Soul Experience Progress Bar
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { expProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(currentTier.colorHex),
                        trackColor = Color(0xFF1B132C)
                    )

                    // Passive Boon Summary & Archetype Codex CTA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✦ Boon: ${currentArchetype.passivePerk}",
                            fontSize = 10.5.sp,
                            color = CelestialAmethystLight,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )

                        if (onOpenArchetypes != null) {
                            Text(
                                text = "Archetype Matrix ›",
                                fontSize = 11.sp,
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // QI CULTIVATION & SPIRIT TREASURY STATUS CARD (Fixed Aspect Ratio & Adaptive Constraint Layout)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3.3f, matchHeightConstraintsFirst = false)
                        .heightIn(min = 104.dp, max = 136.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(
                            1.2.dp,
                            Brush.horizontalGradient(
                                listOf(
                                    Color(realm.colorHex).copy(alpha = 0.85f),
                                    RadiantGold.copy(alpha = 0.6f),
                                    EtherealCyan.copy(alpha = 0.5f)
                                )
                            ),
                            RoundedCornerShape(18.dp)
                        )
                        .testTag("qi_cultivation_header_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0A0D1A)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp, vertical = 11.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header Row: Realm Badge, Stage/Qi Stats & Diamond Gem Counter Chip
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f, fill = false),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    color = Color(realm.colorHex).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(realm.colorHex).copy(alpha = 0.7f))
                                ) {
                                    Text(
                                        text = "${realm.runeSymbol} ${realm.displayName}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(realm.colorHex),
                                        fontSize = 11.5.sp,
                                        maxLines = 1
                                    )
                                }

                                Column(modifier = Modifier.weight(1f, fill = false)) {
                                    Text(
                                        text = "Stage ${soul.cultivationStage}/${realm.maxStages}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${soul.currentQi}/${soul.maxQi} Qi (${(qiRatio * 100).toInt()}%)",
                                        fontSize = 10.sp,
                                        color = if (soul.currentQi >= soul.maxQi) RadiantGoldBright else EtherealCyan,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Diamond / Gem Counter Chip with Treasury Link (Fixed horizontal row, shrink-safe)
                            Surface(
                                modifier = Modifier
                                    .clickable(enabled = onOpenTreasury != null) { onOpenTreasury?.invoke() }
                                    .testTag("qi_gem_counter_chip"),
                                color = RadiantGold.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text("💎", fontSize = 12.sp)
                                    Text(
                                        text = "${soul.soulShards}",
                                        color = RadiantGoldBright,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Shop ›",
                                        fontSize = 9.5.sp,
                                        color = TextGold,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        // Linear Qi Reservoir Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1B2236))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(qiRatio.coerceIn(0.02f, 1f))
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(realm.colorHex),
                                                if (soul.currentQi >= soul.maxQi) RadiantGoldBright else EtherealCyan
                                            )
                                        )
                                    )
                            )
                        }

                        // Footer Row: Equipped Relic / Spiritual Root & Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                if (equippedArtifact != null) {
                                    Text(text = equippedArtifact.iconEmoji, fontSize = 11.5.sp)
                                    Text(
                                        text = "[${equippedArtifact.name}]",
                                        fontSize = 10.sp,
                                        color = Color(equippedArtifact.colorHex),
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                } else {
                                    Text(
                                        text = "Root: ${soul.spiritualRoots}",
                                        fontSize = 10.sp,
                                        color = TextMuted,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (onOpenQiChamber != null) {
                                    Surface(
                                        modifier = Modifier
                                            .clickable { onOpenQiChamber() }
                                            .testTag("open_qi_chamber_header_btn"),
                                        color = Color(realm.colorHex).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(realm.colorHex).copy(alpha = 0.6f))
                                    ) {
                                        Text(
                                            text = "⚡ Qi Chamber",
                                            color = Color(realm.colorHex),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.5.dp),
                                            maxLines = 1
                                        )
                                    }
                                }

                                if (onOpenTreasury != null) {
                                    Surface(
                                        modifier = Modifier
                                            .clickable { onOpenTreasury() }
                                            .testTag("open_treasury_header_btn"),
                                        color = RadiantGold.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.6f))
                                    ) {
                                        Text(
                                            text = "🏛️ Treasury",
                                            color = RadiantGoldBright,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.5.dp),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Main Vessel Showcase: Avatar Crest, Race, Class & Element
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = soul.race.uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = soul.advancedClass ?: soul.className,
                        style = MaterialTheme.typography.titleMedium,
                        color = CelestialAmethystLight,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Elemental & Dynamic Theme Badge
                    val archTheme = com.example.ui.theme.LocalArchetypeTheme.current
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A).copy(alpha = 0.8f))
                                .border(0.6.dp, archTheme.primary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .clickable(enabled = onOpenElementalPowers != null) { onOpenElementalPowers?.invoke() }
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                .testTag("header_affinity_chip"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "Affinity:", fontSize = 10.sp, color = TextMuted)
                            Text(
                                text = soul.primaryElement.ifBlank { soul.element },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = archTheme.primary
                            )
                            if (onOpenElementalPowers != null) {
                                Text("⚡", fontSize = 10.sp)
                            }
                        }

                        if (onOpenElementalPowers != null) {
                            Surface(
                                modifier = Modifier
                                    .clickable { onOpenElementalPowers() }
                                    .testTag("header_elemental_powers_btn"),
                                color = archTheme.primary.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, archTheme.primary.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "🔥 Powers & Arts ›",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = archTheme.primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        } else {
                            Surface(
                                color = archTheme.primary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(0.6.dp, archTheme.primary.copy(alpha = 0.35f))
                            ) {
                                Text(
                                    text = if (archTheme.isWarm) "🔥 Warm Palette" else if (archTheme.isCoolEthereal) "✨ Cool Ethereal" else "🌿 Primordial",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = archTheme.primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                // Interactive Avatar Crest with Visual Cosmic Aura
                CosmeticAvatarCrest(
                    race = soul.race,
                    equippedEffectId = soul.equippedEffectId
                )
            }

            // REDESIGNED DUAL FORCES ALCHEMICAL CORE (Dominant Shadow vs Virtue)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, SurfaceCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0716))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Dominant Shadow Column
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "DOMINANT SHADOW",
                                fontSize = 8.5.sp,
                                color = Color(0xFFF87171),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = soul.dominantShadow.runeSymbol, fontSize = 14.sp)
                                Text(
                                    text = soul.dominantShadow.displayName,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(soul.dominantShadow.colorHex)
                                )
                                Text(
                                    text = "($shadowScore)",
                                    fontSize = 10.5.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        // Alchemical Equilibrium Emblem
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E1538))
                                .border(0.8.dp, RadiantGold.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "⚖️", fontSize = 12.sp)
                        }

                        // Dominant Virtue Column
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "DOMINANT VIRTUE",
                                fontSize = 8.5.sp,
                                color = RadiantGoldBright,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "($virtueScore)",
                                    fontSize = 10.5.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = soul.dominantVirtue.displayName,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(soul.dominantVirtue.colorHex)
                                )
                                Text(text = soul.dominantVirtue.runeSymbol, fontSize = 14.sp)
                            }
                        }
                    }

                    // Duality Balance Track Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF140D24))
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Shadow portion
                            Box(
                                modifier = Modifier
                                    .weight(1f - virtueRatio)
                                    .fillMaxHeight()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF991B1B),
                                                Color(0xFFEF4444)
                                            )
                                        )
                                    )
                            )
                            // Virtue portion
                            Box(
                                modifier = Modifier
                                    .weight(virtueRatio)
                                    .fillMaxHeight()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                RadiantGold,
                                                RadiantGoldBright
                                            )
                                        )
                                    )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${((1f - virtueRatio) * 100).toInt()}% Shadow Force",
                            fontSize = 9.sp,
                            color = Color(0xFFF87171)
                        )
                        Text(
                            text = "${(virtueRatio * 100).toInt()}% Virtue Force",
                            fontSize = 9.sp,
                            color = RadiantGoldBright
                        )
                    }
                }
            }

            // SOUL RESONANCE AUDIO-WAVEFORM METER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF090615))
                    .border(0.8.dp, EtherealCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
                    .testTag("soul_resonance_meter")
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
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Resonance Waveform",
                                tint = EtherealCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "SOUL RESONANCE:",
                                style = MaterialTheme.typography.labelMedium,
                                color = EtherealCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "$resonancePercent%",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Text(
                            text = resonance?.frequencyLabel ?: "528 Hz • Miraculous Harmony",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialAmethystLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Equalizer Audio Bars Visualizer Simulation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF040A14))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val barHeights = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f, 0.85f, 1.0f, 0.75f, 0.95f, 0.6f, 0.8f, 0.5f, 0.7f, 0.4f)
                        barHeights.forEachIndexed { idx, h ->
                            val dynamicHeight = (h * (if (idx % 2 == 0) wavePulse else (1.6f - wavePulse))).coerceIn(0.2f, 1f)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(dynamicHeight)
                                    .padding(horizontal = 1.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                EtherealCyan,
                                                Color(0xFF0284C7)
                                            )
                                        )
                                    )
                            )
                        }
                    }

                    Text(
                        text = resonance?.resonanceInsight ?: "The vessel vibrates in harmony with ${soul.dominantVirtue.displayName} & ${soul.dominantShadow.displayName}.",
                        fontSize = 10.5.sp,
                        color = TextMuted,
                        lineHeight = 14.sp
                    )
                }
            }

            // HUMANITY & METAMORPHOSIS PROGRESS METERS
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Humanity Meter
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Humanity Tether (Mortal Grounding)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "${soul.humanity}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (soul.humanity > 50) Color(0xFF34D399) else Color(0xFFA78BFA),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF110C24))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(humanityAnimated.coerceIn(0.02f, 1.0f))
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF059669),
                                            Color(0xFF10B981),
                                            Color(0xFF34D399)
                                        )
                                    )
                                )
                        )
                    }
                }

                // Metamorphic Awakening Meter
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Metamorphic Awakening:",
                                style = MaterialTheme.typography.labelSmall,
                                color = RadiantGoldBright
                            )
                            Text(
                                text = "${soul.evolutionProgress}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = RadiantGold,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "Next: ${soul.possibleEvolution}",
                            style = MaterialTheme.typography.labelSmall,
                            color = CelestialAmethystLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF110C24))
                            .border(0.6.dp, RadiantGold.copy(alpha = 0.35f), CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(evolutionProgressAnimated.coerceIn(0.02f, 1.0f))
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            RadiantGoldDim,
                                            RadiantGold,
                                            RadiantGoldBright,
                                            CelestialAmethystLight
                                        )
                                    )
                                )
                        )
                    }
                }

                // IDENTITY MILESTONE BADGES RIBBON
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF120B28))
                        .border(0.8.dp, RadiantGold.copy(alpha = 0.45f), RoundedCornerShape(12.dp))
                        .clickable { showMilestonesDialog = true }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("identity_milestones_ribbon")
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
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "Milestone Badges",
                                tint = RadiantGoldBright,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Milestone Badges:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadiantGoldBright
                            )
                            Text(
                                text = "$unlockedCount / ${badges.size} Unlocked",
                                fontSize = 10.5.sp,
                                color = EtherealCyan,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Mini badge icons preview
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            badges.filter { it.isUnlocked }.take(4).forEach { badge ->
                                Text(text = badge.runeIcon, fontSize = 13.sp)
                            }
                            Text(
                                text = "Inspect ❯",
                                fontSize = 10.5.sp,
                                color = CelestialAmethystLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // IDENTITY MILESTONE BADGES DIALOG
    if (showMilestonesDialog) {
        Dialog(onDismissRequest = { showMilestonesDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.2.dp, RadiantGold.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
                    .testTag("identity_milestones_dialog"),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "👑", fontSize = 20.sp)
                            Column {
                                Text(
                                    text = "IDENTITY MILESTONES",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RadiantGoldBright
                                )
                                Text(
                                    text = "$unlockedCount of ${badges.size} Astral Badges Unlocked",
                                    fontSize = 10.5.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        IconButton(onClick = { showMilestonesDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextMuted
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(badges) { badge ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (badge.isUnlocked) Color(0xFF160E30) else Color(0xFF0C081A)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (badge.isUnlocked) 1.dp else 0.5.dp,
                                    color = if (badge.isUnlocked) Color(badge.tier.colorHex).copy(alpha = 0.7f) else SurfaceCardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (badge.isUnlocked) Color(badge.tier.colorHex).copy(alpha = 0.2f)
                                                else Color(0xFF1B162E)
                                            )
                                            .border(
                                                1.dp,
                                                if (badge.isUnlocked) Color(badge.tier.colorHex) else Color.Gray.copy(alpha = 0.3f),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (badge.isUnlocked) badge.runeIcon else "🔒",
                                            fontSize = 18.sp
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = badge.name,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (badge.isUnlocked) Color.White else TextMuted
                                            )
                                            Text(
                                                text = badge.tier.displayName,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(badge.tier.colorHex)
                                            )
                                        }

                                        Text(
                                            text = badge.description,
                                            fontSize = 10.5.sp,
                                            color = if (badge.isUnlocked) CelestialAmethystLight else TextMuted,
                                            lineHeight = 14.sp
                                        )

                                        Text(
                                            text = "Requirement: ${badge.requirementText}",
                                            fontSize = 9.5.sp,
                                            color = RadiantGold.copy(alpha = 0.8f)
                                        )

                                        if (!badge.isUnlocked) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(4.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF221A38))
                                                ) {
                                                    val progressRatio = (badge.progressCurrent.toFloat() / badge.progressMax.toFloat()).coerceIn(0f, 1f)
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(progressRatio)
                                                            .fillMaxHeight()
                                                            .clip(CircleShape)
                                                            .background(Color(badge.tier.colorHex))
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "${badge.progressCurrent}/${badge.progressMax}",
                                                    fontSize = 9.sp,
                                                    color = TextMuted
                                                )
                                            }
                                        } else if (badge.unlockedAtText != null) {
                                            Text(
                                                text = "✓ ${badge.unlockedAtText}",
                                                fontSize = 9.5.sp,
                                                color = Color(0xFF34D399),
                                                fontWeight = FontWeight.Bold
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
    }
}
