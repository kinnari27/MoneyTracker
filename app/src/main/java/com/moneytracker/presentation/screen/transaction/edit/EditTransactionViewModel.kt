package com.moneytracker.presentation.screen.transaction.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.model.InvestmentCategory
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
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
class EditTransactionViewModel @Inject constructor(
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Int = checkNotNull(savedStateHandle["transactionId"])

    private val _uiState = MutableStateFlow(EditTransactionUiState())
    val uiState: StateFlow<EditTransactionUiState> = _uiState.asStateFlow()

    // Preserve original date — never changed in edit mode; also used as RD start date for tenure calc
    private var originalDate: Date = Date()

    init {
        loadTransaction()
    }

    // ── Load from Room ────────────────────────────────────────────────────────

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
                    amount = tx.amount.toInt().toString(),
                    selectedCategory = tx.category,
                    type = tx.type,
                    note = tx.note,
                    // RD is always recurring — enforce on load too
                    isRecurring = if (tx.category == InvestmentCategory.RD) true else tx.isRecurring,
                    // FD fields
                    maturityDate = tx.maturityDate
                        ?: Date(System.currentTimeMillis() + 31536000000L),
                    interestRate = tx.interestRate?.toString() ?: "",
                    maturityAmount = tx.maturityAmount ?: 0.0,
                    // RD fields (rdTenureMonths no longer stored in UI state)
                    rdMonthlyAmount = tx.rdMonthlyAmount?.toString() ?: "",
                    rdMaturityDate = tx.rdMaturityDate
                        ?: Date(System.currentTimeMillis() + 31536000000L),
                    rdInterestRate = tx.rdInterestRate?.toString() ?: "",
                    rdMaturityAmount = tx.rdMaturityAmount ?: 0.0,
                    isLoading = false
                )
            }
        }
    }

    // ── Common field handlers ─────────────────────────────────────────────────

    fun onAmountChange(v: String) {
        _uiState.update { it.copy(amount = v, error = null) }
        calculateMaturity()
    }

    fun onNoteChange(v: String) = _uiState.update { it.copy(note = v) }

    fun onRecurringToggle() {
        // RD is always recurring — ignore toggle attempts
        if (_uiState.value.selectedCategory == InvestmentCategory.RD) return
        _uiState.update { it.copy(isRecurring = !it.isRecurring) }
    }

    fun onCategorySelect(c: String) {
        _uiState.update {
            it.copy(
                selectedCategory = c,
                error = null,
                // Force recurring on when RD is selected
                isRecurring = if (c == InvestmentCategory.RD) true else it.isRecurring
            )
        }
        calculateMaturity()
    }

    fun onTypeChange(t: TransactionType) =
        _uiState.update { it.copy(type = t, selectedCategory = "", error = null) }

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

                val diffInMillies = state.maturityDate.time - originalDate.time
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
                if (monthly <= 0 || rate <= 0) return

                // Derive tenure in months from originalDate → rdMaturityDate
                val diffInMillies = state.rdMaturityDate.time - originalDate.time
                val months = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 30
                if (months <= 0) return

                // Standard RD maturity formula: M = P*n + P*n*(n+1)*r / (2*12*100)
                val total = monthly * months +
                        (monthly * months * (months + 1) * rate) / (2 * 12 * 100)
                _uiState.update { it.copy(rdMaturityAmount = total) }
            }
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────

    fun save() {
        val s = _uiState.value
        val isRd = s.selectedCategory == InvestmentCategory.RD
        val isFd = s.selectedCategory == InvestmentCategory.FD

        val amountToValidate = if (isRd) s.rdMonthlyAmount else s.amount
        val amount = amountToValidate.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            _uiState.update { it.copy(error = "Please enter a valid amount") }
            return
        }
        if (s.selectedCategory.isBlank()) {
            _uiState.update { it.copy(error = "Please select a category") }
            return
        }
        if (isRd) {
            if (s.rdInterestRate.toDoubleOrNull() == null) {
                _uiState.update { it.copy(error = "Please enter a valid interest rate") }
                return
            }
            // Validate that rdMaturityDate is after originalDate (giving at least 1 month)
            val diffInMillies = s.rdMaturityDate.time - originalDate.time
            val months = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 30
            if (months <= 0) {
                _uiState.update { it.copy(error = "Maturity date must be at least 1 month from start date") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            // Derive RD tenure months at save time
            val rdTenureMonths: Int? = if (isRd) {
                val diffInMillies = s.rdMaturityDate.time - originalDate.time
                (TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) / 30).toInt()
            } else null

            val result = updateTransactionUseCase(
                Transaction(
                    id = s.id,
                    amount = amount,
                    category = s.selectedCategory,
                    type = s.type,
                    note = s.note.trim(),
                    date = originalDate,     // keep original date
                    isRecurring = s.isRecurring,
                    // FD fields
                    maturityDate = if (isFd) s.maturityDate else null,
                    interestRate = if (isFd) s.interestRate.toDoubleOrNull() else null,
                    maturityAmount = when {
                        isFd -> s.maturityAmount
                        isRd -> s.rdMaturityAmount
                        else -> null
                    },
                    // RD fields
                    rdMonthlyAmount = if (isRd) amount else null,
                    rdTenureMonths = rdTenureMonths,
                    rdMaturityDate = if (isRd) s.rdMaturityDate else null,
                    rdInterestRate = if (isRd) s.rdInterestRate.toDoubleOrNull() else null,
                    rdMaturityAmount = if (isRd) s.rdMaturityAmount else null
                )
            )

            result.fold(
                onSuccess = { _uiState.update { it.copy(isSaving = false, isSaved = true) } },
                onFailure = { e ->
                    _uiState.update { it.copy(isSaving = false, error = e.message) }
                }
            )
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    fun delete() {
        viewModelScope.launch {
            deleteTransactionUseCase(transactionId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
}