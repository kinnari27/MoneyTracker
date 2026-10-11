package com.moneytracker.domain.usecase.transaction

import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Int): Transaction? {
        return repository.getTransactionById(id)
    }
}