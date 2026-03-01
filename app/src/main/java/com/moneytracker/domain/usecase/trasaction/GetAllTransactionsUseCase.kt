package com.moneytracker.domain.usecase.trasaction

import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> =
        repository.getAllTransactions()
}