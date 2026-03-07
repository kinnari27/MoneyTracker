package com.moneytracker.presentation.screen.dashboard.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.usecase.trasaction.DeleteTransactionUseCase
import com.moneytracker.domain.usecase.trasaction.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            getAllTransactionsUseCase()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { transactions ->
                    val income = transactions.filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }
                    val expense = transactions.filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                    val investment = transactions.filter { it.type == TransactionType.INVESTMENT }
                        .sumOf { it.amount }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allTransactions = transactions,
                            totalIncome = income,
                            totalExpense = expense,
                            totalInvestment = investment,
                            filteredTransactions = applyFilter(
                                transactions,
                                it.filter,
                                it.searchQuery
                            )
                        )
                    }
                }
        }
    }

    fun onFilterChange(filter: TransactionFilter) {
        _uiState.update {
            it.copy(
                filter = filter,
                filteredTransactions = applyFilter(it.allTransactions, filter, it.searchQuery)
            )
        }
    }

    fun onSearchChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredTransactions = applyFilter(it.allTransactions, it.filter, query)
            )
        }
    }

    fun showDeleteConfirmation(transaction: Transaction) {
        _uiState.update { it.copy(transactionToDelete = transaction) }
    }

    fun hideDeleteConfirmation() {
        _uiState.update { it.copy(transactionToDelete = null) }
    }

    fun deleteTransaction() {
        val transaction = _uiState.value.transactionToDelete ?: return
        viewModelScope.launch {
            deleteTransactionUseCase(transaction)
            hideDeleteConfirmation()
        }
    }

    private fun applyFilter(
        transactions: List<Transaction>,
        filter: TransactionFilter,
        query: String
    ): List<Transaction> = transactions.filter { t ->
        val matchType = when (filter) {
            TransactionFilter.ALL -> true
            TransactionFilter.INCOME -> t.type == TransactionType.INCOME
            TransactionFilter.EXPENSE -> t.type == TransactionType.EXPENSE
            TransactionFilter.INVESTMENT -> t.type == TransactionType.INVESTMENT
        }
        val matchQuery = query.isBlank() ||
                t.title.contains(query, ignoreCase = true) ||
                t.category.contains(query, ignoreCase = true)
        matchType && matchQuery
    }
}