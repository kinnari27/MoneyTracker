package com.moneytracker.presentation.screen.settings.notifications

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    fun onAllNotificationsToggle(enabled: Boolean) =
        _uiState.update { it.copy(allNotifications = enabled) }

    fun onBudgetAlertToggle(enabled: Boolean) =
        _uiState.update { it.copy(budgetAlert = enabled) }

    fun onLargeTransactionToggle(enabled: Boolean) =
        _uiState.update { it.copy(largeTransaction = enabled) }

    fun onDailySummaryToggle(enabled: Boolean) =
        _uiState.update { it.copy(dailySummary = enabled) }

    fun onBillReminderToggle(enabled: Boolean) =
        _uiState.update { it.copy(billReminder = enabled) }

    fun onMonthlyReportToggle(enabled: Boolean) =
        _uiState.update { it.copy(monthlyReport = enabled) }

    fun onTipsToggle(enabled: Boolean) =
        _uiState.update { it.copy(tips = enabled) }

    fun onAppUpdatesToggle(enabled: Boolean) =
        _uiState.update { it.copy(appUpdates = enabled) }
}
