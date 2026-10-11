package com.moneytracker.presentation.screen.dashboard.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.usecase.transaction.GetAllTransactionsUseCase
import com.moneytracker.domain.usecase.transaction.GetCategoryTotalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getCategoryTotalsUseCase: GetCategoryTotalsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    private var allTransactions: List<Transaction> = emptyList()

    init {
        loadAnalytics()
    }

    fun onPeriodChange(period: AnalyticsPeriod) {
        _uiState.update { it.copy(period = period) }
        calculateStats(allTransactions, period)
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            getAllTransactionsUseCase()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { transactions ->
                    allTransactions = transactions
                    calculateStats(transactions, _uiState.value.period)
                }
        }
    }

    private fun calculateStats(transactions: List<Transaction>, period: AnalyticsPeriod) {
        val (filtered, daysInPeriod) = filterByPeriod(transactions, period)

        val income = filtered.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expense = filtered.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val invested = filtered.filter { it.type == TransactionType.INVESTMENT }.sumOf { it.amount }

        val largest = filtered.filter { it.type == TransactionType.EXPENSE }.maxByOrNull { it.amount }
        val catTotals = getCategoryTotalsUseCase(filtered)

        val investBreakdown = filtered
            .filter { it.type == TransactionType.INVESTMENT }
            .groupBy { it.category }
            .mapValues { (_, list) -> list.sumOf { it.amount } }

        _uiState.update {
            it.copy(
                isLoading = false,
                totalSpent = expense,
                totalIncome = income,
                totalInvested = invested,
                totalSaved = income - expense - invested,
                netSavings = income - expense,
                dailyAverage = if (daysInPeriod > 0) expense / daysInPeriod else 0.0,
                largestExpense = largest,
                categoryTotals = catTotals,
                investmentBreakdown = investBreakdown,
                error = null
            )
        }
    }

    private fun filterByPeriod(
        transactions: List<Transaction>,
        period: AnalyticsPeriod
    ): Pair<List<Transaction>, Int> {
        val cal = Calendar.getInstance()
        val now = cal.timeInMillis

        return when (period) {
            AnalyticsPeriod.WEEKLY -> {
                cal.add(Calendar.DAY_OF_YEAR, -7)
                val threshold = cal.timeInMillis
                val list = transactions.filter { it.date.time in threshold..now }
                Pair(list, 7)
            }
            AnalyticsPeriod.MONTHLY -> {
                cal.add(Calendar.MONTH, -1)
                val threshold = cal.timeInMillis
                val list = transactions.filter { it.date.time in threshold..now }
                Pair(list, 30)
            }
            AnalyticsPeriod.YEARLY -> {
                cal.add(Calendar.YEAR, -1)
                val threshold = cal.timeInMillis
                val list = transactions.filter { it.date.time in threshold..now }
                Pair(list, 365)
            }
        }
    }
}
