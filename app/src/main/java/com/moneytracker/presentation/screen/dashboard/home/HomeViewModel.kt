package com.moneytracker.presentation.screen.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.repository.UserRepository
import com.moneytracker.domain.usecase.transaction.GetAllTransactionsUseCase
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
class HomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUser()
        loadTransactions()
    }

    // ── Load user name from Room DB ───────────────────────────────────────────
    private fun loadUser() {
        viewModelScope.launch {
            userRepository.observeCurrentUser.collect { user ->
                _uiState.update {
                    it.copy(
                        userName = user?.name ?: "",
                        userInitial = user?.name?.take(1)?.uppercase() ?: "?",
                        budgetLimit = user?.budgetLimit ?: 0.0,
                        greeting = timeGreeting()
                    )
                }
            }
        }
    }

    // ── Load all transactions from Room DB ────────────────────────────────────
    private fun loadTransactions() {
        viewModelScope.launch {
            getAllTransactionsUseCase()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { transactions ->
                    val income = transactions.filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }
                    val expense = transactions.filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                    val invested = transactions.filter { it.type == TransactionType.INVESTMENT }
                        .sumOf { it.amount }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalBalance = income - expense - invested,
                            totalIncome = income,
                            totalExpense = expense,
                            totalInvested = invested,
                            recentTransactions = transactions.take(5),
                            error = null
                        )
                    }
                }
        }
    }

    private fun timeGreeting(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }
}
