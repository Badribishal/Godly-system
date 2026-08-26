package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AchievementsDialog
import com.example.ui.components.AwakeningDialog
import com.example.ui.components.CosmicParticlesCanvas
import com.example.ui.components.SanctuaryLibraryDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.SoulRecordDialog
import com.example.ui.components.WardrobeDialog
import com.example.ui.screens.history.HistoryScreen
import com.example.ui.screens.main.MainScreen
import com.example.ui.screens.record.RecordScreen
import com.example.ui.screens.reflection.ReflectionScreen
import com.example.ui.screens.soul.SoulScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.SoulViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SoulViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val rarePalette by viewModel.rarePalette.collectAsStateWithLifecycle()

            MyApplicationTheme(
                themeMode = themeMode,
                palette = rarePalette
            ) {
                GodlySystemApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GodlySystemApp(
    viewModel: SoulViewModel
) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val soulProfile by viewModel.soulProfile.collectAsStateWithLifecycle()
    val records by viewModel.records.collectAsStateWithLifecycle()
    val evolutionEvents by viewModel.evolutionEvents.collectAsStateWithLifecycle()
    val dailyTrials by viewModel.dailyTrials.collectAsStateWithLifecycle()
    val recordFormState by viewModel.recordFormState.collectAsStateWithLifecycle()
    val lastEvaluationResult by viewModel.lastEvaluationResult.collectAsStateWithLifecycle()
    val showAwakeningModal by viewModel.showAwakeningModal.collectAsStateWithLifecycle()
    val showSettingsDialog by viewModel.showSettingsDialog.collectAsStateWithLifecycle()
    val showAchievementsDialog by viewModel.showAchievementsDialog.collectAsStateWithLifecycle()
    val showWardrobeDialog by viewModel.showWardrobeDialog.collectAsStateWithLifecycle()
    val showRecordDialog by viewModel.showRecordDialog.collectAsStateWithLifecycle()
    val showLibraryDialog by viewModel.showLibraryDialog.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val rarePalette by viewModel.rarePalette.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val dailyLoginState by viewModel.dailyLoginState.collectAsStateWithLifecycle()
    val soulResonance by viewModel.soulResonance.collectAsStateWithLifecycle()
    val checkedInEmotion by viewModel.checkedInEmotion.collectAsStateWithLifecycle()
    val isCheckedInToday by viewModel.isCheckedInToday.collectAsStateWithLifecycle()
    val systemToast by viewModel.systemToast.collectAsStateWithLifecycle()

    val hasClaimableAchievements = remember(achievements, dailyLoginState) {
        !dailyLoginState.isClaimedToday || achievements.any { it.isUnlocked && !it.isClaimed }
    }

    LaunchedEffect(systemToast) {
        systemToast?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearSystemToast()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Ambient Cosmic Particle Background
        CosmicParticlesCanvas()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                // Pillow Shaped Bottom Navigation Bar
                PillowBottomNavigationBar(
                    currentTab = currentTab,
                    onSelectTab = { tab ->
                        viewModel.setTab(tab)
                    }
                )
            }
        ) { paddingValues ->
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(420, easing = FastOutSlowInEasing)) +
                        scaleIn(initialScale = 0.96f, animationSpec = tween(420, easing = FastOutSlowInEasing)))
                        .togetherWith(
                            fadeOut(animationSpec = tween(320, easing = FastOutSlowInEasing)) +
                            scaleOut(targetScale = 1.04f, animationSpec = tween(320, easing = FastOutSlowInEasing))
                        )
                },
                label = "mystical_screen_crossfade",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) { targetTab ->
                when (targetTab) {
                    ScreenTab.MAIN -> MainScreen(
                        soul = soulProfile,
                        trials = dailyTrials,
                        records = records,
                        events = evolutionEvents,
                        resonance = soulResonance,
                        checkedInEmotion = checkedInEmotion,
                        isCheckedInToday = isCheckedInToday,
                        onDailyCheckIn = { viewModel.performDailyEmotionalCheckIn(it) },
                        hasClaimableAchievements = hasClaimableAchievements,
                        onNavigate = { viewModel.setTab(it) },
                        onOpenRecord = { viewModel.openRecordDialog() },
                        onOpenLibrary = { viewModel.openLibraryDialog() },
                        onArchiveCurrentEvolution = { note -> viewModel.archiveGodlyEvolution(note) },
                        onOpenSettings = { viewModel.openSettings() },
                        onOpenAchievements = { viewModel.openAchievements() },
                        onOpenWardrobe = { viewModel.openWardrobe() }
                    )
                    ScreenTab.SOUL -> SoulScreen(
                        soul = soulProfile,
                        onBack = { viewModel.setTab(ScreenTab.MAIN) },
                        onOpenWardrobe = { viewModel.openWardrobe() }
                    )
                }
            }
        }

        // Sanctuary Library Dialog (Full Codex)
        if (showLibraryDialog) {
            SanctuaryLibraryDialog(
                soul = soulProfile,
                events = evolutionEvents,
                onArchiveCurrentEvolution = { note -> viewModel.archiveGodlyEvolution(note) },
                onDismiss = { viewModel.closeLibraryDialog() }
            )
        }

        // Soul Record / Seven Sins & Seven Virtues Alchemical Calibration Dialog
        if (showRecordDialog) {
            SoulRecordDialog(
                initialShadows = recordFormState.selectedShadows,
                initialVirtues = recordFormState.selectedVirtues,
                onSubmit = { shadows, virtues, sit, ref ->
                    viewModel.submitForcesRecord(
                        selectedShadows = shadows,
                        selectedVirtues = virtues,
                        situation = sit,
                        reflection = ref
                    )
                },
                onDismiss = { viewModel.closeRecordDialog() }
            )
        }

        // Settings Dialog (Theme Mode, Rare Color Combos, Multi-Format Export/Import)
        if (showSettingsDialog) {
            SettingsDialog(
                currentThemeMode = themeMode,
                currentPalette = rarePalette,
                onSetThemeMode = { viewModel.setThemeMode(it) },
                onSetPalette = { viewModel.setRarePalette(it) },
                onExportData = { fmt, onReady -> viewModel.exportData(fmt, onReady) },
                onExportPdf = { cb -> viewModel.exportPdf(cb) },
                onImportData = { content, cb -> viewModel.importData(content, cb) },
                onDismiss = { viewModel.closeSettings() }
            )
        }

        // Achievements Dialog
        if (showAchievementsDialog) {
            AchievementsDialog(
                achievements = achievements,
                soulShards = soulProfile.soulShards,
                dailyLoginState = dailyLoginState,
                onClaimDailyLogin = { viewModel.claimDailyLoginReward() },
                onClaimReward = { viewModel.claimAchievementReward(it) },
                onDismiss = { viewModel.closeAchievements() }
            )
        }

        // Soul Wardrobe Dialog (Cosmetics)
        if (showWardrobeDialog) {
            WardrobeDialog(
                race = soulProfile.race,
                soulShards = soulProfile.soulShards,
                equippedEffectId = soulProfile.equippedEffectId,
                unlockedEffectIds = soulProfile.unlockedEffectIds,
                onUnlockEffect = { viewModel.unlockCosmeticEffect(it) },
                onEquipEffect = { viewModel.equipCosmeticEffect(it) },
                onDismiss = { viewModel.closeWardrobe() }
            )
        }

        // Awakening / Metamorphosis Results Dialog
        if (showAwakeningModal && lastEvaluationResult != null) {
            AwakeningDialog(
                result = lastEvaluationResult!!,
                onDismiss = { viewModel.dismissAwakeningModal() }
            )
        }
    }
}

@Composable
fun PillowBottomNavigationBar(
    currentTab: ScreenTab,
    onSelectTab: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .border(
                    width = 1.2.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(30.dp)
                )
                .testTag("pillow_bottom_nav"),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
            shadowElevation = 14.dp,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PillowNavItem(
                    selected = currentTab == ScreenTab.MAIN,
                    icon = Icons.Default.AutoAwesome,
                    label = "Sanctuary",
                    onClick = { onSelectTab(ScreenTab.MAIN) },
                    testTag = "nav_sanctuary"
                )

                PillowNavItem(
                    selected = currentTab == ScreenTab.SOUL,
                    icon = Icons.Default.SelfImprovement,
                    label = "Soul Matrix",
                    onClick = { onSelectTab(ScreenTab.SOUL) },
                    testTag = "nav_soul"
                )
            }
        }
    }
}

@Composable
private fun PillowNavItem(
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                else Color.Transparent
            )
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                fontSize = 12.5.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

