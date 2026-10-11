package com.moneytracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moneytracker.presentation.screen.auth.ForgotPasswordScreen
import com.moneytracker.presentation.screen.auth.LoginScreen
import com.moneytracker.presentation.screen.auth.RegisterScreen
import com.moneytracker.presentation.screen.dashboard.analytics.AnalyticsScreen
import com.moneytracker.presentation.screen.dashboard.analytics.AnalyticsViewModel
import com.moneytracker.presentation.screen.dashboard.history.HistoryScreen
import com.moneytracker.presentation.screen.dashboard.history.HistoryViewModel
import com.moneytracker.presentation.screen.dashboard.home.HomeScreen
import com.moneytracker.presentation.screen.dashboard.home.HomeViewModel
import com.moneytracker.presentation.screen.settings.biometricLock.BiometricLockScreen
import com.moneytracker.presentation.screen.settings.changePassword.ChangePasswordScreen
import com.moneytracker.presentation.screen.settings.currency.CurrencyScreen
import com.moneytracker.presentation.screen.settings.editProfile.EditProfileScreen
import com.moneytracker.presentation.screen.settings.notifications.NotificationsScreen
import com.moneytracker.presentation.screen.settings.profile.ProfileScreen
import com.moneytracker.presentation.screen.splash.SplashScreen
import com.moneytracker.presentation.screen.transaction.add.AddTransactionScreen
import com.moneytracker.presentation.screen.transaction.add.AddTransactionViewModel
import com.moneytracker.presentation.screen.transaction.edit.EditTransactionScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        // ── Auth ──────────────────────────────────────────────────────────────
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(BottomNavItem.Home.route) {
                        popUpTo(
                            Routes.SPLASH
                        ) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(BottomNavItem.Home.route) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(BottomNavItem.Home.route) {
                        popUpTo(
                            Routes.REGISTER
                        ) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ── Home ──────────────────────────────────────────────────────────────
        composable(BottomNavItem.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                onSeeAllClick = { navController.navigate(BottomNavItem.History.route) },
                onTransactionClick = { tx -> navController.navigate(Routes.editTransaction(tx.id)) }
            )
        }

        // ── History ───────────────────────────────────────────────────────────
        composable(BottomNavItem.History.route) {
            val viewModel: HistoryViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HistoryScreen(
                uiState = uiState,
                onFilterChange = viewModel::onFilterChange,
                onSearchChange = viewModel::onSearchChange,
                onDeleteTransaction = viewModel::showDeleteConfirmation,
                onConfirmDelete = viewModel::deleteTransaction,
                onDismissDelete = viewModel::hideDeleteConfirmation,
                onEditTransaction = { tx -> navController.navigate(Routes.editTransaction(tx.id)) }
            )
        }

        // ── Add Transaction ───────────────────────────────────────────────────
        composable(BottomNavItem.Add.route) {
            val viewModel: AddTransactionViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AddTransactionScreen(
                uiState = uiState,
                onAmountChange = viewModel::onAmountChange,
                onCategorySelect = viewModel::onCategorySelect,
                onTypeChange = viewModel::onTypeChange,
                onDateChange = viewModel::onDateChange,
                onMaturityDateChange = viewModel::onMaturityDateChange,
                onInterestRateChange = viewModel::onInterestRateChange,
                onRdMonthlyAmountChange = viewModel::onRdMonthlyAmountChange,
                onRdMaturityDateChange = viewModel::onRdMaturityDateChange,
                onRdInterestRateChange = viewModel::onRdInterestRateChange,
                onNoteChange = viewModel::onNoteChange,
                onRecurringToggle = viewModel::onRecurringToggle,
                onSave = viewModel::saveTransaction,
                onDelete = viewModel::deleteTransaction,
                onSaveSuccess = { navController.navigate(BottomNavItem.Home.route) }
            )
        }

        // ── Edit Transaction ──────────────────────────────────────────────────
        composable(
            route = Routes.EDIT_TRANSACTION,
            arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
        ) { backStack ->
            val txId = backStack.arguments?.getInt("transactionId") ?: return@composable
            EditTransactionScreen(
                transactionId = txId,
                onSaved = { navController.popBackStack() },
                onDeleted = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Analytics ─────────────────────────────────────────────────────────
        composable(BottomNavItem.Analytics.route) {
            val viewModel: AnalyticsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AnalyticsScreen(uiState = uiState, onPeriodChange = viewModel::onPeriodChange)
        }

        // ── Profile ───────────────────────────────────────────────────────────
        composable(BottomNavItem.Profile.route) {
            ProfileScreen(
                onNavigate = { route -> navController.navigate(route) },
                onSignOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Settings ──────────────────────────────────────────────────────────
        composable(Routes.EDIT_PROFILE) { EditProfileScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.NOTIFICATIONS) { NotificationsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.CURRENCY) { CurrencyScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.BIOMETRIC_LOCK) { BiometricLockScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.CHANGE_PASSWORD) { ChangePasswordScreen(onBack = { navController.popBackStack() }) }
    }
}
