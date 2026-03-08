package com.moneytracker.domain.usecase.trasaction

import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return try {
            require(transaction.amount > 0) { "Amount must be greater than 0" }
            require(transaction.category.isNotBlank()) { "Category must be selected" }
            val id = repository.addTransaction(transaction)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}