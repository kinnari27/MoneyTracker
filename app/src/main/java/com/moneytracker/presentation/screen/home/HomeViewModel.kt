package com.moneytracker.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.repository.UserRepository
import com.moneytracker.domain.usecase.trasaction.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getAllTransactionsUseCase(),
                userRepository.observeCurrentUser
            ) { transactions, user ->
                val income = transactions
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }
                val expense = transactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                        totalBalance = income - expense,
                        totalIncome = income,
                        totalExpense = expense,
                        recentTransactions = transactions.take(5),
                        error = null
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect {}
        }
    }
}