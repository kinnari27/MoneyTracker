package com.moneytracker.presentation.screen.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.usecase.trasaction.DeleteTransactionUseCase
import com.moneytracker.domain.usecase.trasaction.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Int = checkNotNull(savedStateHandle["transactionId"])

    private val _uiState = MutableStateFlow(EditTransactionUiState())
    val uiState: StateFlow<EditTransactionUiState> = _uiState.asStateFlow()

    // Original transaction to preserve date on save
    private var originalDate: Date = Date()

    init {
        loadTransaction()
    }

    // ── Load from Room by ID ──────────────────────────────────────────────────
    private fun loadTransaction() {
        viewModelScope.launch {
            val tx = getTransactionByIdUseCase(transactionId)
            if (tx == null) {
                _uiState.update { it.copy(isLoading = false, notFound = true) }
                return@launch
            }
            originalDate = tx.date
            _uiState.update {
                it.copy(
                    id = tx.id,
                    title = tx.title,
                    amount = tx.amount.toInt().toString(),
                    selectedCategory = tx.category,
                    type = tx.type,
                    note = tx.note,
                    isRecurring = tx.isRecurring,
                    isLoading = false
                )
            }
        }
    }

    fun onTitleChange(v: String) = _uiState.update { it.copy(title = v, error = null) }
    fun onAmountChange(v: String) = _uiState.update { it.copy(amount = v, error = null) }
    fun onNoteChange(v: String) = _uiState.update { it.copy(note = v) }
    fun onRecurringToggle() = _uiState.update { it.copy(isRecurring = !it.isRecurring) }
    fun onCategorySelect(c: String) =
        _uiState.update { it.copy(selectedCategory = c, error = null) }

    fun onTypeChange(t: TransactionType) =
        _uiState.update { it.copy(type = t, selectedCategory = "", error = null) }

    // ── Save update to Room ───────────────────────────────────────────────────
    fun save() {
        val s = _uiState.value
        if (s.title.isBlank()) {
            _uiState.update { it.copy(error = "Please enter a title") }; return
        }
        val amount = s.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(error = "Please enter a valid amount") }; return
        }
        if (s.selectedCategory.isBlank()) {
            _uiState.update { it.copy(error = "Please select a category") }; return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val result = updateTransactionUseCase(
                Transaction(
                    id = s.id,
                    title = s.title.trim(),
                    amount = amount,
                    category = s.selectedCategory,
                    type = s.type,
                    note = s.note.trim(),
                    date = originalDate,     // keep original date
                    isRecurring = s.isRecurring
                )
            )
            result.fold(
                onSuccess = { _uiState.update { it.copy(isSaving = false, isSaved = true) } },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = e.message
                        )
                    }
                }
            )
        }
    }

    // ── Delete from Room ──────────────────────────────────────────────────────
    fun delete() {
        viewModelScope.launch {
            deleteTransactionUseCase(transactionId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
}