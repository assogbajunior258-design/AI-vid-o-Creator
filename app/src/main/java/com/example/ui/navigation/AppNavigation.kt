package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.VideoCreatorViewModel

data class NavItem(
    val screen: AppScreen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userNotificationMessage) {
        uiState.userNotificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearNotification()
        }
    }

    // BackHandler: Handle back navigation cleanly
    BackHandler(enabled = uiState.currentScreen != AppScreen.DASHBOARD) {
        if (uiState.currentScreen == AppScreen.STUDIO && uiState.studioState.currentStep > 1) {
            viewModel.setStudioStep(uiState.studioState.currentStep - 1)
        } else {
            viewModel.navigateTo(AppScreen.DASHBOARD)
        }
    }

    val navItems = listOf(
        NavItem(AppScreen.DASHBOARD, "Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "nav_dashboard"),
        NavItem(AppScreen.STUDIO, "Studio", Icons.Filled.MovieCreation, Icons.Outlined.MovieCreation, "nav_studio"),
        NavItem(AppScreen.PROJECTS, "Projets", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary, "nav_projects"),
        NavItem(AppScreen.BILLING, "Crédits", Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet, "nav_billing"),
        NavItem(AppScreen.LANDING, "Découvrir", Icons.Filled.Explore, Icons.Outlined.Explore, "nav_landing"),
        NavItem(AppScreen.SETTINGS, "Réglages", Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings")
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 700.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        containerColor = ElectricVioletDark,
                        contentColor = Color.White,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = data.visuals.message, fontWeight = FontWeight.SemiBold)
                    }
                }
            },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = StudioDarkSurface,
                        contentColor = TextPrimary,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .testTag("app_bottom_nav_bar")
                    ) {
                        navItems.forEach { item ->
                            val isSelected = uiState.currentScreen == item.screen
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.screen) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = NeonCyanLight,
                                    indicatorColor = ElectricViolet,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag(item.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            if (isWideScreen) {
                // Wide Screen: Navigation Rail + Main Content
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    NavigationRail(
                        containerColor = StudioDarkSurface,
                        contentColor = TextPrimary,
                        modifier = Modifier
                            .fillMaxHeight()
                            .testTag("app_navigation_rail")
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        navItems.forEach { item ->
                            val isSelected = uiState.currentScreen == item.screen
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = { viewModel.navigateTo(item.screen) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label, fontSize = 11.sp) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = NeonCyanLight,
                                    indicatorColor = ElectricViolet,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag(item.testTag)
                            )
                        }
                    }

                    // Main screen content
                    Box(modifier = Modifier.weight(1f)) {
                        ScreenContent(
                            screen = uiState.currentScreen,
                            viewModel = viewModel,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            } else {
                // Standard Mobile Layout
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ScreenContent(
                        screen = uiState.currentScreen,
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun ScreenContent(
    screen: AppScreen,
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    when (screen) {
        AppScreen.LANDING -> LandingScreen(viewModel = viewModel, modifier = modifier)
        AppScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel, modifier = modifier)
        AppScreen.STUDIO -> StudioScreen(viewModel = viewModel, modifier = modifier)
        AppScreen.PROJECTS -> ProjectsScreen(viewModel = viewModel, modifier = modifier)
        AppScreen.HISTORY -> HistoryScreen(viewModel = viewModel, modifier = modifier)
        AppScreen.BILLING -> BillingScreen(viewModel = viewModel, modifier = modifier)
        AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel, modifier = modifier)
        AppScreen.ADMIN -> AdminScreen(viewModel = viewModel, modifier = modifier)
    }
}
