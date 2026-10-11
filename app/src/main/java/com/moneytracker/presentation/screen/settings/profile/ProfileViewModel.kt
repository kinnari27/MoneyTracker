package com.moneytracker.presentation.screen.settings.profile

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
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeUser()
        observeTransactions()
    }

    // ── Live user data from Room ──────────────────────────────────────────────
    private fun observeUser() {
        viewModelScope.launch {
            userRepository.observeCurrentUser.collect { user ->
                _uiState.update {
                    it.copy(
                        userName = user?.name ?: "",
                        userEmail = user?.email ?: "",
                        userPhone = user?.phone ?: "",
                        currency = user?.currency ?: "INR",
                        hasPassword = user?.hasPassword ?: false
                    )
                }
            }
        }
    }

    // ── Live transaction stats from Room ──────────────────────────────────────
    private fun observeTransactions() {
        viewModelScope.launch {
            getAllTransactionsUseCase()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { transactions ->
                    val categories = transactions.map { it.category }.distinct().size
                    val months = transactions.map {
                        val c = Calendar.getInstance().also { cal -> cal.time = it.date }
                        "${c.get(Calendar.YEAR)}-${c.get(Calendar.MONTH)}"
                    }.distinct().size

                    val income = transactions.filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }
                    val expense = transactions.filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                    val invested = transactions.filter { it.type == TransactionType.INVESTMENT }
                        .sumOf { it.amount }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalTransactions = transactions.size,
                            totalCategories = categories,
                            activeMonths = months,
                            totalIncome = income,
                            totalExpense = expense,
                            totalInvested = invested
                        )
                    }
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
        }
    }
}
