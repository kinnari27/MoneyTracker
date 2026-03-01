package com.moneytracker.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.usecase.trasaction.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            getAllTransactionsUseCase()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { transactions ->
                    val categories = transactions
                        .map { it.category }
                        .distinct().size
                    val months = transactions
                        .map {
                            val cal = Calendar.getInstance()
                            cal.time = it.date
                            "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH)}"
                        }
                        .distinct()
                        .size

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalTransactions = transactions.size,
                            totalCategories = categories,
                            activeMonths = months
                        )
                    }
                }
        }
    }
}