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
                        // FD fields
                        maturityDate = transaction.maturityDate ?: Date(),
                        interestRate = transaction.interestRate?.toString() ?: "",
                        maturityAmount = transaction.maturityAmount ?: 0.0,
                        // RD fields
                        rdMonthlyAmount = transaction.rdMonthlyAmount?.toString() ?: "",
                        rdMaturityDate = transaction.rdMaturityDate ?: Date(),
                        rdInterestRate = transaction.rdInterestRate?.toString() ?: "",
                        rdMaturityAmount = transaction.rdMaturityAmount ?: 0.0,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Transaction not found") }
            }
        }
    }

    // ── Common field handlers ─────────────────────────────────────────────────

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

    fun onCategorySelect(cat: String) {
        _uiState.update { it.copy(selectedCategory = cat, error = null) }
        calculateMaturity()
    }

    fun onTypeChange(type: TransactionType) {
        _uiState.update { it.copy(type = type, selectedCategory = "", error = null) }
    }

    // ── FD field handlers ─────────────────────────────────────────────────────

    fun onMaturityDateChange(date: Date) {
        _uiState.update { it.copy(maturityDate = date) }
        calculateMaturity()
    }

    fun onInterestRateChange(value: String) {
        _uiState.update { it.copy(interestRate = value) }
        calculateMaturity()
    }

    // ── RD field handlers ─────────────────────────────────────────────────────

    fun onRdMonthlyAmountChange(value: String) {
        _uiState.update { it.copy(rdMonthlyAmount = value, error = null) }
        calculateMaturity()
    }

    fun onRdMaturityDateChange(date: Date) {
        _uiState.update { it.copy(rdMaturityDate = date) }
        calculateMaturity()
    }

    fun onRdInterestRateChange(value: String) {
        _uiState.update { it.copy(rdInterestRate = value) }
        calculateMaturity()
    }

    // ── Maturity calculation ──────────────────────────────────────────────────

    private fun calculateMaturity() {
        val state = _uiState.value

        when (state.selectedCategory) {
            InvestmentCategory.FD -> {
                val principle = state.amount.toDoubleOrNull() ?: return
                val rate = state.interestRate.toDoubleOrNull() ?: return
                if (principle <= 0 || rate <= 0) return

                val diffInMillies = state.maturityDate.time - state.date.time
                val diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
                if (diffInDays <= 0) return

                val years = diffInDays / 365.0
                // Simple interest: A = P(1 + rt)
                val total = principle * (1 + (rate / 100 * years))
                _uiState.update { it.copy(maturityAmount = total) }
            }

            InvestmentCategory.RD -> {
                val monthly = state.rdMonthlyAmount.toDoubleOrNull() ?: return
                val rate = state.rdInterestRate.toDoubleOrNull() ?: return

                // Derive tenure in months from date -> rdMaturityDate
                val diffInMillies = state.rdMaturityDate.time - state.date.time
                val months = (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 30).toInt()

                if (monthly <= 0 || rate <= 0 || months <= 0) return

                // Standard RD maturity formula:
                // M = P*n + P*n*(n+1)*r / (2*12*100)
                val total = monthly * months +
                        (monthly * months * (months + 1) * rate) / (2 * 12 * 100)
                _uiState.update { it.copy(rdMaturityAmount = total) }
            }
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────

    fun saveTransaction() {
        val state = _uiState.value

        // For RD, the "amount" stored is the monthly installment
        val isRd = state.selectedCategory == InvestmentCategory.RD
        val isFd = state.selectedCategory == InvestmentCategory.FD

        val amountToValidate = if (isRd) state.rdMonthlyAmount else state.amount
        val amount = amountToValidate.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(error = "Please enter a valid amount") }
            return
        }
        if (state.selectedCategory.isBlank()) {
            _uiState.update { it.copy(error = "Please select a category") }
            return
        }
        if (isRd) {
            if (state.rdInterestRate.toDoubleOrNull() == null) {
                _uiState.update { it.copy(error = "Please enter a valid interest rate") }
                return
            }
            // Validate that rdMaturityDate is after date (giving at least 1 month)
            val diffInMillies = state.rdMaturityDate.time - state.date.time
            val months = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 30
            if (months <= 0) {
                _uiState.update { it.copy(error = "Maturity date must be at least 1 month from start date") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Derive RD tenure months at save time
            val rdTenureMonths: Int? = if (isRd) {
                val diffInMillies = state.rdMaturityDate.time - state.date.time
                (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 30).toInt()
            } else null

            val transaction = Transaction(
                id = state.id ?: 0,
                amount = amount,
                category = state.selectedCategory,
                type = state.type,
                note = state.note.trim(),
                date = state.date,
                isRecurring = state.isRecurring,
                // FD fields
                maturityDate = if (isFd) state.maturityDate else null,
                interestRate = if (isFd) state.interestRate.toDoubleOrNull() else null,
                maturityAmount = when {
                    isFd -> state.maturityAmount
                    isRd -> state.rdMaturityAmount
                    else -> null
                },
                // RD fields
                rdMonthlyAmount = if (isRd) amount else null,
                rdTenureMonths = rdTenureMonths,
                rdMaturityDate = if (isRd) state.rdMaturityDate else null,
                rdInterestRate = if (isRd) state.rdInterestRate.toDoubleOrNull() else null,
                rdMaturityAmount = if (isRd) state.rdMaturityAmount else null
            )

            val result = if (state.isEditMode) {
                updateTransactionUseCase(transaction)
            } else {
                addTransactionUseCase(transaction)
            }

            result.fold(
                onSuccess = { _uiState.update { AddTransactionUiState(isSaved = true) } },
                onFailure = { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

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