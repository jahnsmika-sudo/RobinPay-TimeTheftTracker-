package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.BossScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.RanksScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TrackerScreen
import com.example.ui.theme.Emerald400
import com.example.ui.theme.RobinPayTheme
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RobinPayTheme {
                RobinPayApp()
            }
        }
    }
}

@Composable
fun RobinPayApp(viewModel: RobinPayViewModel = viewModel()) {
    val isBossMode by viewModel.isBossModeActive.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeSession by viewModel.activeSession.collectAsStateWithLifecycle()
    val salaryConfig by viewModel.salaryConfig.collectAsStateWithLifecycle()
    val sessions by viewModel.allSessions.collectAsStateWithLifecycle()
    val totalEarnedHistory by viewModel.totalEarnedFromHistory.collectAsStateWithLifecycle()
    val totalDurationSecs by viewModel.totalDurationSeconds.collectAsStateWithLifecycle()

    val combinedEarned = totalEarnedHistory + activeSession.earnedAmount
    val rankProgress = viewModel.getRankProgress()
    val milestone = viewModel.getActiveMilestoneProgress()

    // Fullscreen Boss Screen (Stealth Mode)
    if (isBossMode) {
        BossScreen(
            onDismiss = { viewModel.deactivateBossMode() }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_nav_bar"),
                    containerColor = Slate900,
                    tonalElevation = 8.dp
                ) {
                    // Tab 1: Tracker
                    NavigationBarItem(
                        selected = currentTab == AppTab.TRACKER,
                        onClick = { viewModel.selectTab(AppTab.TRACKER) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.TRACKER) Icons.Filled.HourglassTop else Icons.Outlined.HourglassTop,
                                contentDescription = AppTab.TRACKER.getTitle(salaryConfig.language)
                            )
                        },
                        label = { Text(AppTab.TRACKER.getTitle(salaryConfig.language), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Slate950,
                            selectedTextColor = Emerald400,
                            indicatorColor = Emerald400,
                            unselectedIconColor = Slate700,
                            unselectedTextColor = Slate700
                        ),
                        modifier = Modifier.testTag("nav_tracker")
                    )

                    // Tab 2: Historie / History
                    NavigationBarItem(
                        selected = currentTab == AppTab.HISTORY,
                        onClick = { viewModel.selectTab(AppTab.HISTORY) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                                contentDescription = AppTab.HISTORY.getTitle(salaryConfig.language)
                            )
                        },
                        label = { Text(AppTab.HISTORY.getTitle(salaryConfig.language), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Slate950,
                            selectedTextColor = Emerald400,
                            indicatorColor = Emerald400,
                            unselectedIconColor = Slate700,
                            unselectedTextColor = Slate700
                        ),
                        modifier = Modifier.testTag("nav_history")
                    )

                    // Tab 3: Beute & Ränge / Ranks
                    NavigationBarItem(
                        selected = currentTab == AppTab.LOOT_RANKS,
                        onClick = { viewModel.selectTab(AppTab.LOOT_RANKS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.LOOT_RANKS) Icons.Filled.EmojiEvents else Icons.Outlined.EmojiEvents,
                                contentDescription = AppTab.LOOT_RANKS.getTitle(salaryConfig.language)
                            )
                        },
                        label = { Text(AppTab.LOOT_RANKS.getTitle(salaryConfig.language), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Slate950,
                            selectedTextColor = Emerald400,
                            indicatorColor = Emerald400,
                            unselectedIconColor = Slate700,
                            unselectedTextColor = Slate700
                        ),
                        modifier = Modifier.testTag("nav_ranks")
                    )

                    // Tab 4: Einstellungen / Settings
                    NavigationBarItem(
                        selected = currentTab == AppTab.SETTINGS,
                        onClick = { viewModel.selectTab(AppTab.SETTINGS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.SETTINGS) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                                contentDescription = AppTab.SETTINGS.getTitle(salaryConfig.language)
                            )
                        },
                        label = { Text(AppTab.SETTINGS.getTitle(salaryConfig.language), fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Slate950,
                            selectedTextColor = Emerald400,
                            indicatorColor = Emerald400,
                            unselectedIconColor = Slate700,
                            unselectedTextColor = Slate700
                        ),
                        modifier = Modifier.testTag("nav_settings")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    AppTab.TRACKER -> {
                        TrackerScreen(
                            viewModel = viewModel,
                            activeSession = activeSession,
                            salaryConfig = salaryConfig,
                            milestone = milestone,
                            onOpenBossMode = { viewModel.activateBossMode() }
                        )
                    }
                    AppTab.HISTORY -> {
                        HistoryScreen(
                            viewModel = viewModel,
                            sessions = sessions,
                            totalEarned = combinedEarned,
                            totalDurationSeconds = totalDurationSecs + activeSession.elapsedSeconds,
                            salaryConfig = salaryConfig
                        )
                    }
                    AppTab.LOOT_RANKS -> {
                        RanksScreen(
                            viewModel = viewModel,
                            rankProgress = rankProgress,
                            totalEarned = combinedEarned,
                            salaryConfig = salaryConfig
                        )
                    }
                    AppTab.SETTINGS -> {
                        SettingsScreen(
                            viewModel = viewModel,
                            salaryConfig = salaryConfig,
                            sessions = sessions
                        )
                    }
                }
            }
        }
    }
}
