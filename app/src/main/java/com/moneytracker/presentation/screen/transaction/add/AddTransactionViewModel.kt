package com.moneytracker.presentation.screen.transaction.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.InvestmentCategory
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.usecase.trasaction.AddTransactionUseCase
import com.moneytracker.domain.usecase.trasaction.DeleteTransactionUseCase
import com.moneytracker.domain.usecase.trasaction.GetTransactionByIdUseCase
import com.moneytracker.domain.usecase.trasaction.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        val transactionId: Int? = savedStateHandle.get<Int>("transactionId")
        if (transactionId != null && transactionId != -1) {
            loadTransaction(transactionId)
        }
    }

    private fun loadTransaction(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transaction = getTransactionByIdUseCase(id)
            if (transaction != null) {
                _uiState.update {
                    it.copy(
                        id = transaction.id,
                        amount = transaction.amount.toString(),
                        selectedCategory = transaction.category,
                        type = transaction.type,
                        note = transaction.note,
                        date = transaction.date,
                        isRecurring = transaction.isRecurring,
                        maturityDate = transaction.maturityDate ?: Date(),
                        interestRate = transaction.interestRate?.toString() ?: "",
                        maturityAmount = transaction.maturityAmount ?: 0.0,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Transaction not found") }
            }
        }
    }

    fun onAmountChange(value: String) {
        _uiState.update { it.copy(amount = value, error = null) }
        calculateMaturity()
    }

    fun onNoteChange(value: String) = _uiState.update { it.copy(note = value) }
    fun onRecurringToggle() = _uiState.update { it.copy(isRecurring = !it.isRecurring) }
    fun onDateChange(date: Date) {
        _uiState.update { it.copy(date = date) }
        calculateMaturity()
    }

    fun onMaturityDateChange(date: Date) {
        _uiState.update { it.copy(maturityDate = date) }
        calculateMaturity()
    }

    fun onInterestRateChange(value: String) {
        _uiState.update { it.copy(interestRate = value) }
        calculateMaturity()
    }

    private fun calculateMaturity() {
        val state = _uiState.value
        if (state.selectedCategory != InvestmentCategory.FD) return

        val principle = state.amount.toDoubleOrNull() ?: 0.0
        val rate = state.interestRate.toDoubleOrNull() ?: 0.0
        
        if (principle > 0 && rate > 0) {
            val diffInMillies = state.maturityDate.time - state.date.time
            val diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
            
            if (diffInDays > 0) {
                val years = diffInDays / 365.0
                // Simple interest calculation: A = P(1 + rt)
                val total = principle * (1 + (rate / 100 * years))
                _uiState.update { it.copy(maturityAmount = total) }
            }
        }
    }

    fun onCategorySelect(cat: String) {
        _uiState.update { it.copy(selectedCategory = cat, error = null) }
        calculateMaturity()
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.update { it.copy(type = type, selectedCategory = "", error = null) }
    }

    fun saveTransaction() {
        val state = _uiState.value

        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(error = "Please enter a valid amount") }; return
        }
        if (state.selectedCategory.isBlank()) {
            _uiState.update { it.copy(error = "Please select a category") }; return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val transaction = Transaction(
                id = state.id ?: 0,
                amount = amount,
                category = state.selectedCategory,
                type = state.type,
                note = state.note.trim(),
                date = state.date,
                isRecurring = state.isRecurring,
                maturityDate = if (state.selectedCategory == InvestmentCategory.FD) state.maturityDate else null,
                interestRate = if (state.selectedCategory == InvestmentCategory.FD) state.interestRate.toDoubleOrNull() else null,
                maturityAmount = if (state.selectedCategory == InvestmentCategory.FD) state.maturityAmount else null
            )

            val result = if (state.isEditMode) {
                updateTransactionUseCase(transaction)
            } else {
                addTransactionUseCase(transaction)
            }

            result.fold(
                onSuccess = { _uiState.update { AddTransactionUiState(isSaved = true) } },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
            )
        }
    }

    fun deleteTransaction() {
        val state = _uiState.value
        val id = state.id ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val transaction = Transaction(
                id = id,
                amount = state.amount.toDoubleOrNull() ?: 0.0,
                category = state.selectedCategory,
                type = state.type,
                date = state.date
            )
            deleteTransactionUseCase(transaction)
            _uiState.update { AddTransactionUiState(isSaved = true) }
        }
    }

    fun resetSavedFlag() = _uiState.update { it.copy(isSaved = false) }
}