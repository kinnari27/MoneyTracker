package com.moneytracker.presentation.screen.analytics

import com.moneytracker.domain.model.Transaction

enum class AnalyticsPeriod { WEEKLY, MONTHLY, YEARLY }
data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val totalSpent: Double = 0.0,
    val totalSaved: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val largestExpense: Transaction? = null,
    val categoryTotals: Map<String, Double> = emptyMap(),
    val period: AnalyticsPeriod = AnalyticsPeriod.MONTHLY,
    val error: String? = null
)