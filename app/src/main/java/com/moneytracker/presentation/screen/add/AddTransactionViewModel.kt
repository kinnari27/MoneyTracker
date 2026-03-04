package com.moneytracker.presentation.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.usecase.trasaction.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value, error = null) }
    fun onAmountChange(value: String) = _uiState.update { it.copy(amount = value, error = null) }
    fun onNoteChange(value: String) = _uiState.update { it.copy(note = value) }
    fun onRecurringToggle() = _uiState.update { it.copy(isRecurring = !it.isRecurring) }

    fun onCategorySelect(cat: String) =
        _uiState.update { it.copy(selectedCategory = cat, error = null) }

    // When type changes, reset category so stale expense category isn't carried into investment
    fun onTypeChange(type: TransactionType) =
        _uiState.update { it.copy(type = type, selectedCategory = "", error = null) }

    fun saveTransaction() {
        val state = _uiState.value

        // Validation
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
            val result = addTransactionUseCase(
                Transaction(
                    title = state.title.trim(),
                    amount = amount,
                    category = state.selectedCategory,
                    type = state.type,
                    note = state.note.trim(),
                    date = Date(),
                    isRecurring = state.isRecurring
                )
            )
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

    fun resetSavedFlag() = _uiState.update { it.copy(isSaved = false) }
}