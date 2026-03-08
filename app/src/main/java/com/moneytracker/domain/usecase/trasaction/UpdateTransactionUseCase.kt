package com.moneytracker.domain.usecase.trasaction

import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        return try {
            require(transaction.amount > 0) { "Amount must be greater than 0" }
            repository.updateTransaction(transaction)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}