package com.moneytracker.presentation.screen.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.usecase.trasaction.GetAllTransactionsUseCase
import com.moneytracker.domain.usecase.trasaction.GetCategoryTotalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getCategoryTotalsUseCase: GetCategoryTotalsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init { loadAnalytics() }

    fun onPeriodChange(period: AnalyticsPeriod) {
        _uiState.update { it.copy(period = period) }
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            getAllTransactionsUseCase()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { transactions ->
                    val income   = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                    val expense  = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                    val largest  = transactions.filter { it.type == TransactionType.EXPENSE }.maxByOrNull { it.amount }
                    val catTotals = getCategoryTotalsUseCase(transactions)

                    _uiState.update {
                        it.copy(
                            isLoading      = false,
                            totalSpent     = expense,
                            totalSaved     = income - expense,
                            dailyAverage   = if (transactions.isNotEmpty()) expense / 28 else 0.0,
                            largestExpense = largest,
                            categoryTotals = catTotals
                        )
                    }
                }
        }
    }
}
