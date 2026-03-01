package com.moneytracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moneytracker.presentation.screen.account.CurrencyScreen
import com.moneytracker.presentation.screen.account.EditProfileScreen
import com.moneytracker.presentation.screen.add.AddTransactionScreen
import com.moneytracker.presentation.screen.add.AddTransactionViewModel
import com.moneytracker.presentation.screen.analytics.AnalyticsScreen
import com.moneytracker.presentation.screen.analytics.AnalyticsViewModel
import com.moneytracker.presentation.screen.biometricLock.BiometricLockScreen
import com.moneytracker.presentation.screen.history.HistoryScreen
import com.moneytracker.presentation.screen.history.HistoryViewModel
import com.moneytracker.presentation.screen.home.HomeScreen
import com.moneytracker.presentation.screen.home.HomeViewModel
import com.moneytracker.presentation.screen.notifications.NotificationsScreen
import com.moneytracker.presentation.screen.profile.ProfileScreen
import com.moneytracker.presentation.screen.security.ChangePinScreen

object Routes {
    const val EDIT_PROFILE = "edit_profile"
    const val NOTIFICATIONS = "notifications"
    const val CURRENCY = "currency"
    const val BIOMETRIC_LOCK = "biometric_lock"
    const val CHANGE_PIN = "change_pin"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route
    ) {
        composable(BottomNavItem.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                onAddClick = { navController.navigate(BottomNavItem.Add.route) },
                onSeeAllClick = { navController.navigate(BottomNavItem.History.route) }
            )
        }

        composable(BottomNavItem.History.route) {
            val viewModel: HistoryViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                uiState = uiState,
                onFilterChange = viewModel::onFilterChange,
                onSearchChange = viewModel::onSearchChange,
                onDeleteTransaction = viewModel::deleteTransaction
            )
        }

        composable(BottomNavItem.Add.route) {
            val viewModel: AddTransactionViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AddTransactionScreen(
                uiState = uiState,
                onTitleChange = viewModel::onTitleChange,
                onAmountChange = viewModel::onAmountChange,
                onCategorySelect = viewModel::onCategorySelect,
                onTypeChange = viewModel::onTypeChange,
                onNoteChange = viewModel::onNoteChange,
                onRecurringToggle = viewModel::onRecurringToggle,
                onSave = viewModel::saveTransaction,
                onSaveSuccess = { navController.navigate(BottomNavItem.Home.route) }
            )
        }

        composable(BottomNavItem.Analytics.route) {
            val viewModel: AnalyticsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AnalyticsScreen(
                uiState = uiState,
                onPeriodChange = viewModel::onPeriodChange
            )
        }

        composable(BottomNavItem.Profile.route) {
            ProfileScreen(onNavigate = { route -> navController.navigate(route) })
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.CURRENCY) {
            CurrencyScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.BIOMETRIC_LOCK) {
            BiometricLockScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.CHANGE_PIN) {
            ChangePinScreen(onBack = { navController.popBackStack() })
        }
    }
}
