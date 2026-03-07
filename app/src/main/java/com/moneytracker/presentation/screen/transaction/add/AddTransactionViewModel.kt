package com.moneytracker.presentation.screen.transaction.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                        title = transaction.title,
                        amount = transaction.amount.toString(),
                        selectedCategory = transaction.category,
                        type = transaction.type,
                        note = transaction.note,
                        date = transaction.date,
                        isRecurring = transaction.isRecurring,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Transaction not found") }
            }
        }
    }

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, error = null) }
    fun onAmountChange(value: String) = _uiState.update { it.copy(amount = value, error = null) }
    fun onNoteChange(value: String) = _uiState.update { it.copy(note = value) }
    fun onRecurringToggle() = _uiState.update { it.copy(isRecurring = !it.isRecurring) }

    fun onCategorySelect(cat: String) =
        _uiState.update { it.copy(selectedCategory = cat, error = null) }

    fun onTypeChange(type: TransactionType) =
        _uiState.update { it.copy(type = type, selectedCategory = "", error = null) }

    fun saveTransaction() {
        val state = _uiState.value

        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "Please enter a title") }; return
        }
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
                title = state.title.trim(),
                amount = amount,
                category = state.selectedCategory,
                type = state.type,
                note = state.note.trim(),
                date = state.date,
                isRecurring = state.isRecurring
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
                title = state.title,
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