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

    fun onTitleChange(value: String)    = _uiState.update { it.copy(title = value, error = null) }
    fun onAmountChange(value: String)   = _uiState.update { it.copy(amount = value, error = null) }
    fun onCategorySelect(cat: String)   = _uiState.update { it.copy(selectedCategory = cat, error = null) }
    fun onNoteChange(value: String)     = _uiState.update { it.copy(note = value) }
    fun onRecurringToggle()             = _uiState.update { it.copy(isRecurring = !it.isRecurring) }
    fun onTypeChange(type: TransactionType) = _uiState.update { it.copy(type = type) }

    fun saveTransaction() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = addTransactionUseCase(
                Transaction(
                    title = state.title,
                    amount = state.amount.toDoubleOrNull() ?: 0.0,
                    category = state.selectedCategory,
                    type = state.type,
                    note = state.note,
                    date = Date(),
                    isRecurring = state.isRecurring
                )
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSaved = true) }
                    resetForm()
                },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    private fun resetForm() {
        _uiState.update { AddTransactionUiState(isSaved = true) }
    }

    fun resetSavedFlag() {
        _uiState.update { it.copy(isSaved = false) }
    }
}