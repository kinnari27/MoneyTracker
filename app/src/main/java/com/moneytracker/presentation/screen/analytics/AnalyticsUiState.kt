package com.moneytracker.presentation.screen.analytics

import com.moneytracker.domain.model.Transaction

enum class AnalyticsPeriod { WEEKLY, MONTHLY, YEARLY }

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    // Expense
    val totalSpent: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val largestExpense: Transaction? = null,
    val categoryTotals: Map<String, Double> = emptyMap(),
    // Income
    val totalIncome: Double = 0.0,
    val netSavings: Double = 0.0,
    // Investment
    val totalInvested: Double = 0.0,
    val investmentBreakdown: Map<String, Double> = emptyMap(),
    // Meta
    val period: AnalyticsPeriod = AnalyticsPeriod.MONTHLY,
    val error: String? = null,
    val totalSaved: Double = 0.0
)