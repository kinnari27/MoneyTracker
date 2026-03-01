package com.moneytracker.data.mapper

import com.moneytracker.data.local.entity.TransactionEntity
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import java.util.Date

fun TransactionEntity.toDomain() = Transaction(
    id          = id,
    title       = title,
    amount      = amount,
    category    = category,
    type        = TransactionType.valueOf(type.uppercase()),
    note        = note,
    date        = Date(date),
    isRecurring = isRecurring
)

fun Transaction.toEntity() = TransactionEntity(
    id          = id,
    title       = title,
    amount      = amount,
    category    = category,
    type        = type.name.lowercase(),
    note        = note,
    date        = date.time,
    isRecurring = isRecurring
)