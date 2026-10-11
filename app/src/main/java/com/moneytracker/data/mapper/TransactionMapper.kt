package com.moneytracker.data.mapper

import com.moneytracker.data.local.entity.TransactionEntity
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import java.util.Date

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    amount = amount,
    category = category,
    type = TransactionType.valueOf(type.uppercase()),
    note = note,
    date = Date(date),
    isRecurring = isRecurring,
    maturityDate = maturityDate?.let { Date(it) },
    interestRate = interestRate,
    maturityAmount = maturityAmount,
    rdMonthlyAmount = rdMonthlyAmount,
    rdTenureMonths = rdTenureMonths,
    rdMaturityDate = rdMaturityDate?.let { Date(it) },
    rdInterestRate = rdInterestRate,
    rdMaturityAmount = rdMaturityAmount
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    amount = amount,
    category = category,
    type = type.name.lowercase(),
    note = note,
    date = date.time,
    isRecurring = isRecurring,
    maturityDate = maturityDate?.time,
    interestRate = interestRate,
    maturityAmount = maturityAmount,
    rdMonthlyAmount = rdMonthlyAmount,
    rdTenureMonths = rdTenureMonths,
    rdMaturityDate = rdMaturityDate?.time,
    rdInterestRate = rdInterestRate,
    rdMaturityAmount = rdMaturityAmount
)