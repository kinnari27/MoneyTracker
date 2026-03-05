package com.moneytracker.presentation.screen.settings.notifications

data class NotificationsUiState(
    val allNotifications: Boolean = true,
    val budgetAlert: Boolean = true,
    val largeTransaction: Boolean = true,
    val dailySummary: Boolean = false,
    val billReminder: Boolean = true,
    val monthlyReport: Boolean = true,
    val tips: Boolean = false,
    val appUpdates: Boolean = true
)