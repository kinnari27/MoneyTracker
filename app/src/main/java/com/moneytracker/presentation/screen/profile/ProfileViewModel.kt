package com.moneytracker.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getAllTransactionsUseCase(),
                userRepository.observeCurrentUser
            ) { transactions, user ->
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
                        user = user,
                        totalTransactions = transactions.size,
                        totalCategories = categories,
                        activeMonths = months
                    )
                }
            }.catch { _uiState.update { it.copy(isLoading = false) } }
                .collect {}
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
        }
    }
}
