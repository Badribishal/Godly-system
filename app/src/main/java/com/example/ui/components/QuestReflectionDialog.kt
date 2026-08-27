package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.data.model.DailyQuest
import com.example.data.model.QuestCategory
import com.example.ui.theme.CelestialAmethyst
import com.example.ui.theme.CelestialAmethystLight
import com.example.ui.theme.EtherealCyan
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RadiantGoldBright
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestReflectionDialog(
    quest: DailyQuest,
    onCompleteQuest: (reflection: String) -> Unit,
    onDismiss: () -> Unit
) {
    var reflectionText by remember { mutableStateOf(quest.userReflection ?: "") }
    var breathingStep by remember { mutableIntStateOf(0) } // For mindfulness quests

    val isMindfulness = quest.category == QuestCategory.ASTRAL_MINDFULNESS
    val categoryColor = Color(quest.category.colorHex)

    val infiniteTransition = rememberInfiniteTransition(label = "quest_breath")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath_scale"
    )

    val quickIntentionChips = remember(quest.category) {
        when (quest.category) {
            QuestCategory.VIRTUE_CULTIVATION -> listOf(
                "Practicing conscious patience",
                "Extending genuine empathy",
                "Cultivating quiet humility",
                "Acting with courageous truth"
            )
            QuestCategory.SHADOW_TRANSMUTATION -> listOf(
                "Transmuting anger into resolve",
                "Realigning pride with respect",
                "Dissolving hesitation into action",
                "Reframing envy into inspiration"
            )
            QuestCategory.ASTRAL_MINDFULNESS -> listOf(
                "Centering in present awareness",
                "Harmonizing chest resonance",
                "Releasing bodily tension",
                "Anchoring in deep stillness"
            )
            QuestCategory.WISDOM_CONTEMPLATION -> listOf(
                "Accepting what cannot be forced",
                "Finding strength through balance",
                "Observing without judgment",
                "Walking the middle way"
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, Brush.verticalGradient(listOf(categoryColor.copy(alpha = 0.6f), SurfaceCardBorder)), RoundedCornerShape(24.dp))
                .testTag("quest_reflection_dialog"),
            color = SurfaceCard,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                .background(categoryColor.copy(alpha = 0.2f))
                                .border(1.dp, categoryColor.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = quest.category.rune, fontSize = 20.sp)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = quest.category.displayName.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = categoryColor,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = quest.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                // Reward & Target Affinity Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF140D24))
                        .border(1.dp, SurfaceCardBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "🎯", fontSize = 12.sp)
                        Text(
                            text = quest.targetAffinity,
                            fontSize = 11.sp,
                            color = CelestialAmethystLight,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = RadiantGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, RadiantGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "+${quest.expReward} EXP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RadiantGoldBright,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            color = EtherealCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, EtherealCyan.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "+${quest.shardsReward} 💎",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EtherealCyan,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Reflective Prompt Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(categoryColor.copy(alpha = 0.08f))
                        .border(1.dp, categoryColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "QUEST INQUIRY",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = quest.prompt,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontFamily = FontFamily.Serif,
                            lineHeight = 21.sp
                        )
                    }
                }

                // Interactive Breathing Cycle for Mindfulness Quests
                if (isMindfulness) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, EtherealCyan.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(EtherealCyan.copy(alpha = 0.25f))
                                    .border(1.5.dp, EtherealCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SelfImprovement,
                                    contentDescription = "Breathe",
                                    tint = EtherealCyan,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Text(
                                text = "Breathe In (4s) • Hold (4s) • Release (4s)",
                                fontSize = 11.sp,
                                color = EtherealCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Quick Intention Suggestions
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "QUICK INTENTION CHIPS",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        quickIntentionChips.forEach { chip ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        reflectionText = if (reflectionText.isBlank()) chip else "$reflectionText • $chip"
                                    },
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, SurfaceCardBorder)
                            ) {
                                Text(
                                    text = chip,
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // User Reflection Input
                OutlinedTextField(
                    value = reflectionText,
                    onValueChange = { reflectionText = it },
                    label = { Text("Your Conscious Reflection & Realization", fontSize = 12.sp) },
                    placeholder = { Text("Write your honest realization or action taken...", fontSize = 12.sp, color = TextMuted) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .testTag("quest_reflection_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = categoryColor,
                        unfocusedBorderColor = SurfaceCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color(0xFF130D22),
                        unfocusedContainerColor = Color(0xFF130D22)
                    ),
                    maxLines = 4
                )

                // Complete Quest Action Button
                Button(
                    onClick = {
                        onCompleteQuest(reflectionText)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quest_complete_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = categoryColor
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (quest.isCompleted) "Update Soul Matrix" else "Infuse Soul Matrix & Claim EXP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
