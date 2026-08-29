package com.example.ui.screens.reflection

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.local.DailyTrialEntity
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
import org.json.JSONArray

@Composable
fun ReflectionScreen(
    trials: List<DailyTrialEntity>,
    onCompleteTrial: (DailyTrialEntity, Int, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTrialToSolve by remember { mutableStateOf<DailyTrialEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("reflection_screen_column"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("reflection_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Column {
                    Text(
                        text = "DIVINE REFLECTION",
                        style = MaterialTheme.typography.headlineMedium,
                        color = RadiantGoldBright,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Contemplative Inquiries & Astral Dilemmas",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextAmethyst
                    )
                }
            }
        }

        // Philosophy Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF140E26))
                    .border(1.dp, CelestialAmethyst.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Philosophy",
                        tint = RadiantGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "No choice is inherently right or wrong in the eyes of the System. Each option weaves a different spiritual consequence.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Active Solving Panel if selected
        activeTrialToSolve?.let { trial ->
            item {
                ActiveTrialSolverCard(
                    trial = trial,
                    onSolve = { index, reflection ->
                        onCompleteTrial(trial, index, reflection)
                        activeTrialToSolve = null
                    },
                    onCancel = { activeTrialToSolve = null }
                )
            }
        }

        // Trials List Header
        item {
            Text(
                text = "AVAILABLE DIVINE TRIALS",
                fontSize = 11.sp,
                color = RadiantGold,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        items(trials) { trial ->
            TrialListItem(
                trial = trial,
                onClick = {
                    if (trial.completedTimestamp == null) {
                        activeTrialToSolve = trial
                    }
                }
            )
        }
    }
}

@Composable
private fun ActiveTrialSolverCard(
    trial: DailyTrialEntity,
    onSolve: (Int, String) -> Unit,
    onCancel: () -> Unit
) {
    var selectedOption by remember { mutableIntStateOf(0) }
    var userRationale by remember { mutableStateOf("") }

    val optionsList = remember(trial.optionsJson) {
        val list = mutableListOf<String>()
        try {
            val arr = JSONArray(trial.optionsJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(obj.optString("label", "Option $i"))
            }
        } catch (_: Exception) {}
        list
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.5.dp, RadiantGold, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE SCENARIO TRIAL",
                    fontSize = 11.sp,
                    color = RadiantGoldBright,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = trial.category,
                    fontSize = 11.sp,
                    color = EtherealCyan
                )
            }

            Text(
                text = trial.title,
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = trial.scenario,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Text(
                text = "HOW WILL YOUR CONSCIOUSNESS RESOLVE THIS?",
                fontSize = 10.sp,
                color = TextAmethyst,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            // Options List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                optionsList.forEachIndexed { index, optionText ->
                    val isSelected = selectedOption == index
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFF32235A) else Color(0xFF140F26))
                            .border(
                                1.dp,
                                if (isSelected) RadiantGold else SurfaceCardBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedOption = index }
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, if (isSelected) RadiantGold else TextMuted, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(RadiantGold)
                                    )
                                }
                            }

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSelected) TextGold else TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Optional Personal Rationale
            OutlinedTextField(
                value = userRationale,
                onValueChange = { userRationale = it },
                label = { Text("Personal Rationale / Nuance (Optional)", color = TextMuted) },
                placeholder = { Text("Explain why your soul resonates with this path...", color = TextMuted) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("trial_rationale_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RadiantGold,
                    unfocusedBorderColor = SurfaceCardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                minLines = 2
            )

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1F1838),
                        contentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "CANCEL")
                }

                Button(
                    onClick = { onSolve(selectedOption, userRationale) },
                    modifier = Modifier
                        .weight(2f)
                        .testTag("submit_trial_choice_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RadiantGold,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "COMMIT CHOICE",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun TrialListItem(
    trial: DailyTrialEntity,
    onClick: () -> Unit
) {
    val isCompleted = trial.completedTimestamp != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceCard)
            .border(
                1.dp,
                if (isCompleted) Color(0xFF10B981).copy(alpha = 0.4f) else SurfaceCardBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = trial.category.uppercase(),
                        fontSize = 10.sp,
                        color = if (isCompleted) Color(0xFF34D399) else EtherealCyan,
                        fontWeight = FontWeight.Bold
                    )
                    if (isCompleted) {
                        Text(text = "• COMPLETED", fontSize = 10.sp, color = Color(0xFF34D399))
                    }
                }

                Text(
                    text = trial.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = trial.scenario,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 2,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF261D47))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "SOLVE",
                        fontSize = 11.sp,
                        color = RadiantGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
