package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Flare
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AchievementsDialog
import com.example.ui.components.ArchetypeGalleryDialog
import com.example.ui.components.AwakeningDialog
import com.example.ui.components.CosmicParticlesCanvas
import com.example.ui.components.LevelUpCelebrationDialog
import com.example.ui.components.QuestReflectionDialog
import com.example.ui.components.SanctuaryLibraryDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.SoulRecordDialog
import com.example.ui.components.WardrobeDialog
import com.example.ui.screens.main.MainScreen
import com.example.ui.screens.record.RecordScreen
import com.example.ui.screens.soul.SoulScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ScreenTab
import com.example.ui.viewmodel.SoulViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SoulViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val rarePalette by viewModel.rarePalette.collectAsStateWithLifecycle()
            val soul by viewModel.soulProfile.collectAsStateWithLifecycle()

            MyApplicationTheme(
                themeMode = themeMode,
                palette = rarePalette,
                soul = soul
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
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

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
    val showArchetypesDialog by viewModel.showArchetypesDialog.collectAsStateWithLifecycle()
    val showRecordDialog by viewModel.showRecordDialog.collectAsStateWithLifecycle()
    val showLibraryDialog by viewModel.showLibraryDialog.collectAsStateWithLifecycle()
    val showLevelUpModal by viewModel.showLevelUpModal.collectAsStateWithLifecycle()
    val levelUpOutcome by viewModel.levelUpOutcome.collectAsStateWithLifecycle()
    val dailyQuestsState by viewModel.dailyQuestsState.collectAsStateWithLifecycle()
    val selectedQuestForReflection by viewModel.selectedQuestForReflection.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val rarePalette by viewModel.rarePalette.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val dailyLoginState by viewModel.dailyLoginState.collectAsStateWithLifecycle()
    val soulResonance by viewModel.soulResonance.collectAsStateWithLifecycle()
    val checkedInEmotion by viewModel.checkedInEmotion.collectAsStateWithLifecycle()
    val isCheckedInToday by viewModel.isCheckedInToday.collectAsStateWithLifecycle()
    val systemToast by viewModel.systemToast.collectAsStateWithLifecycle()

    val tabs = remember { ScreenTab.entries }
    val pagerState = rememberPagerState(
        initialPage = currentTab.ordinal,
        pageCount = { tabs.size }
    )

    // Sync ViewModel tab change to Pager (e.g. from internal buttons)
    LaunchedEffect(currentTab) {
        if (pagerState.currentPage != currentTab.ordinal) {
            pagerState.animateScrollToPage(
                page = currentTab.ordinal,
                animationSpec = tween(340, easing = FastOutSlowInEasing)
            )
        }
    }

    // Sync Pager settlement back to ViewModel & perform haptic feedback
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { settledPageIndex ->
            val targetTab = tabs.getOrElse(settledPageIndex) { ScreenTab.MAIN }
            if (targetTab != currentTab) {
                viewModel.setTab(targetTab)
                try {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                } catch (_: Exception) {}
            }
        }
    }

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
        // Ambient Cosmic Particle Background (Unified continuous canvas)
        CosmicParticlesCanvas()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                // Pillow Shaped Bottom Navigation Bar (Supports touch & indicates active swipe position)
                PillowBottomNavigationBar(
                    currentTab = currentTab,
                    onSelectTab = { tab ->
                        viewModel.setTab(tab)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                page = tab.ordinal,
                                animationSpec = tween(340, easing = FastOutSlowInEasing)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Smooth Horizontal Pager for Fluid Swipe Navigation Across Tabs
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                beyondViewportPageCount = 1,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    snapAnimationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            ) { pageIndex ->
                val tab = tabs.getOrElse(pageIndex) { ScreenTab.MAIN }

                // Continuous vertical space feeling with subtle graphicsLayer transition
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val pageOffset = ((pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction).absoluteValue
                            alpha = (1f - (pageOffset * 0.35f)).coerceIn(0.6f, 1f)
                            scaleX = (1f - (pageOffset * 0.03f)).coerceIn(0.97f, 1f)
                            scaleY = (1f - (pageOffset * 0.03f)).coerceIn(0.97f, 1f)
                        }
                ) {
                    when (tab) {
                        ScreenTab.MAIN -> MainScreen(
                            soul = soulProfile,
                            trials = dailyTrials,
                            records = records,
                            events = evolutionEvents,
                            resonance = soulResonance,
                            questState = dailyQuestsState,
                            onQuestClick = { quest -> viewModel.openQuestReflection(quest) },
                            onClaimQuestBonus = { viewModel.claimDailyQuestsBonus() },
                            checkedInEmotion = checkedInEmotion,
                            isCheckedInToday = isCheckedInToday,
                            onDailyCheckIn = { viewModel.performDailyEmotionalCheckIn(it) },
                            hasClaimableAchievements = hasClaimableAchievements,
                            onNavigate = { targetTab ->
                                viewModel.setTab(targetTab)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(targetTab.ordinal)
                                }
                            },
                            onOpenRecord = {
                                viewModel.setTab(ScreenTab.RECORD)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(ScreenTab.RECORD.ordinal)
                                }
                            },
                            onOpenLibrary = { viewModel.openLibraryDialog() },
                            onArchiveCurrentEvolution = { note -> viewModel.archiveGodlyEvolution(note) },
                            onOpenSettings = { viewModel.openSettings() },
                            onOpenAchievements = { viewModel.openAchievements() },
                            onOpenWardrobe = { viewModel.openWardrobe() },
                            onOpenArchetypes = { viewModel.openArchetypesDialog() }
                        )

                        ScreenTab.RECORD -> RecordScreen(
                            formState = recordFormState,
                            onFormUpdate = { update -> viewModel.updateRecordForm(update) },
                            onSubmit = {
                                viewModel.submitForcesRecord(
                                    selectedShadows = recordFormState.selectedShadows,
                                    selectedVirtues = recordFormState.selectedVirtues,
                                    situation = recordFormState.situation,
                                    reflection = recordFormState.reflection
                                )
                            },
                            onBack = {
                                viewModel.setTab(ScreenTab.MAIN)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(ScreenTab.MAIN.ordinal)
                                }
                            }
                        )

                        ScreenTab.SOUL -> SoulScreen(
                            soul = soulProfile,
                            onBack = {
                                viewModel.setTab(ScreenTab.MAIN)
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(ScreenTab.MAIN.ordinal)
                                }
                            },
                            onOpenWardrobe = { viewModel.openWardrobe() },
                            onOpenArchetypes = { viewModel.openArchetypesDialog() }
                        )
                    }
                }
            }
        }

        // Daily Quest Reflective Practice Dialog
        if (selectedQuestForReflection != null) {
            QuestReflectionDialog(
                quest = selectedQuestForReflection!!,
                onCompleteQuest = { reflection ->
                    selectedQuestForReflection?.let { quest ->
                        viewModel.completeDailyQuest(quest.id, reflection)
                    }
                },
                onDismiss = { viewModel.dismissQuestReflection() }
            )
        }

        // Archetype Gallery Codex Dialog (Classes & Races with unlock hints & attunement)
        if (showArchetypesDialog) {
            ArchetypeGalleryDialog(
                soul = soulProfile,
                onAttune = { archetypeId -> viewModel.attuneArchetype(archetypeId) },
                onDismiss = { viewModel.closeArchetypesDialog() }
            )
        }

        // Level Up & Tier Ascension Celebration Dialog
        if (showLevelUpModal && levelUpOutcome != null) {
            LevelUpCelebrationDialog(
                outcome = levelUpOutcome!!,
                onAttuneArchetype = { archetypeId -> viewModel.attuneArchetype(archetypeId) },
                onDismiss = { viewModel.dismissLevelUpModal() }
            )
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

        // Soul Record / Seven Sins & Seven Virtues Alchemical Calibration Dialog (if triggered directly)
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
                onClaimAllRewards = { viewModel.claimAllAchievementsReward() },
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
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
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
                    .padding(horizontal = 8.dp, vertical = 4.dp),
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
                    selected = currentTab == ScreenTab.RECORD,
                    icon = Icons.Default.Flare,
                    label = "Record",
                    onClick = { onSelectTab(ScreenTab.RECORD) },
                    testTag = "nav_record"
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
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
