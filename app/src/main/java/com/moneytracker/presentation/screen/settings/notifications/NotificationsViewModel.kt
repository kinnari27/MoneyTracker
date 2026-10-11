package com.moneytracker.presentation.screen.settings.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(loadPreferences())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private fun loadPreferences(): NotificationsUiState = NotificationsUiState(
        allNotifications = prefs.getBoolean("all_notifications", true),
        budgetAlert = prefs.getBoolean("budget_alert", true),
        largeTransaction = prefs.getBoolean("large_transaction", true),
        dailySummary = prefs.getBoolean("daily_summary", false),
        billReminder = prefs.getBoolean("bill_reminder", true),
        monthlyReport = prefs.getBoolean("monthly_report", true),
        tips = prefs.getBoolean("tips", false),
        appUpdates = prefs.getBoolean("app_updates", true)
    )

    private fun savePref(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun onAllNotificationsToggle(enabled: Boolean) {
        savePref("all_notifications", enabled)
        _uiState.update { it.copy(allNotifications = enabled) }
    }

    fun onBudgetAlertToggle(enabled: Boolean) {
        savePref("budget_alert", enabled)
        _uiState.update { it.copy(budgetAlert = enabled) }
    }

    fun onLargeTransactionToggle(enabled: Boolean) {
        savePref("large_transaction", enabled)
        _uiState.update { it.copy(largeTransaction = enabled) }
    }

    fun onDailySummaryToggle(enabled: Boolean) {
        savePref("daily_summary", enabled)
        _uiState.update { it.copy(dailySummary = enabled) }
    }

    fun onBillReminderToggle(enabled: Boolean) {
        savePref("bill_reminder", enabled)
        _uiState.update { it.copy(billReminder = enabled) }
    }

    fun onMonthlyReportToggle(enabled: Boolean) {
        savePref("monthly_report", enabled)
        _uiState.update { it.copy(monthlyReport = enabled) }
    }

    fun onTipsToggle(enabled: Boolean) {
        savePref("tips", enabled)
        _uiState.update { it.copy(tips = enabled) }
    }

    fun onAppUpdatesToggle(enabled: Boolean) {
        savePref("app_updates", enabled)
        _uiState.update { it.copy(appUpdates = enabled) }
    }
}
