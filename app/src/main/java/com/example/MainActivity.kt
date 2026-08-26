package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.example.ui.components.SettingsDialog
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
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val rarePalette by viewModel.rarePalette.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()
    val dailyLoginState by viewModel.dailyLoginState.collectAsStateWithLifecycle()
    val soulResonance by viewModel.soulResonance.collectAsStateWithLifecycle()
    val checkedInEmotion by viewModel.checkedInEmotion.collectAsStateWithLifecycle()
    val isCheckedInToday by viewModel.isCheckedInToday.collectAsStateWithLifecycle()
    val systemToast by viewModel.systemToast.collectAsStateWithLifecycle()

    val tabs = remember {
        listOf(
            ScreenTab.MAIN,
            ScreenTab.SOUL,
            ScreenTab.RECORD,
            ScreenTab.REFLECTION,
            ScreenTab.HISTORY
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = tabs.indexOf(currentTab).coerceAtLeast(0),
        pageCount = { tabs.size }
    )

    // Synchronize programmatic tab changes smoothly into the continuous vertical pager
    LaunchedEffect(currentTab) {
        val targetIndex = tabs.indexOf(currentTab)
        if (targetIndex >= 0 && pagerState.currentPage != targetIndex && !pagerState.isScrollInProgress) {
            pagerState.animateScrollToPage(
                page = targetIndex,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    // Synchronize natural gesture drag / swipe changes back to the ViewModel state
    LaunchedEffect(pagerState.currentPage) {
        val newTab = tabs.getOrNull(pagerState.currentPage) ?: ScreenTab.MAIN
        if (currentTab != newTab) {
            viewModel.setTab(newTab)
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

    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        snapPositionalThreshold = 0.20f // Short swipe threshold (20%) responsive tracking; prevents accidental triggers from tiny movements
    )

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
                        val targetIndex = tabs.indexOf(tab)
                        if (targetIndex >= 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = targetIndex,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMediumLow
                                    )
                                )
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                flingBehavior = flingBehavior,
                key = { pageIndex -> tabs[pageIndex].name }
            ) { pageIndex ->
                when (tabs[pageIndex]) {
                    ScreenTab.MAIN -> MainScreen(
                        soul = soulProfile,
                        trials = dailyTrials,
                        records = records,
                        resonance = soulResonance,
                        checkedInEmotion = checkedInEmotion,
                        isCheckedInToday = isCheckedInToday,
                        onDailyCheckIn = { viewModel.performDailyEmotionalCheckIn(it) },
                        hasClaimableAchievements = hasClaimableAchievements,
                        onNavigate = { viewModel.setTab(it) },
                        onOpenSettings = { viewModel.openSettings() },
                        onOpenAchievements = { viewModel.openAchievements() },
                        onOpenWardrobe = { viewModel.openWardrobe() }
                    )
                    ScreenTab.SOUL -> SoulScreen(
                        soul = soulProfile,
                        onBack = { viewModel.setTab(ScreenTab.MAIN) },
                        onOpenWardrobe = { viewModel.openWardrobe() },
                        onOpenHistory = { viewModel.setTab(ScreenTab.HISTORY) }
                    )
                    ScreenTab.RECORD -> RecordScreen(
                        formState = recordFormState,
                        onFormUpdate = { viewModel.updateRecordForm(it) },
                        onSubmit = { viewModel.submitRecord() },
                        onBack = { viewModel.setTab(ScreenTab.MAIN) }
                    )
                    ScreenTab.REFLECTION -> ReflectionScreen(
                        trials = dailyTrials,
                        onCompleteTrial = { trial, index, reflection ->
                            viewModel.submitTrial(trial, index, reflection)
                        },
                        onBack = { viewModel.setTab(ScreenTab.MAIN) }
                    )
                    ScreenTab.HISTORY -> HistoryScreen(
                        events = evolutionEvents,
                        records = records,
                        soul = soulProfile,
                        onBack = { viewModel.setTab(ScreenTab.MAIN) }
                    )
                }
            }
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
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
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
                    .padding(horizontal = 6.dp, vertical = 4.dp),
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
                    label = "Soul",
                    onClick = { onSelectTab(ScreenTab.SOUL) },
                    testTag = "nav_soul"
                )

                PillowNavItem(
                    selected = currentTab == ScreenTab.RECORD,
                    icon = Icons.Default.Flare,
                    label = "Record",
                    onClick = { onSelectTab(ScreenTab.RECORD) },
                    testTag = "nav_record"
                )

                PillowNavItem(
                    selected = currentTab == ScreenTab.REFLECTION,
                    icon = Icons.Default.AutoStories,
                    label = "Trials",
                    onClick = { onSelectTab(ScreenTab.REFLECTION) },
                    testTag = "nav_reflection"
                )

                PillowNavItem(
                    selected = currentTab == ScreenTab.HISTORY,
                    icon = Icons.Default.History,
                    label = "Chronicle",
                    onClick = { onSelectTab(ScreenTab.HISTORY) },
                    testTag = "nav_history"
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
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                else Color.Transparent
            )
            .border(
                width = if (selected) 1.dp else 0.dp,
                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = if (selected) 10.dp else 6.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            if (selected) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

