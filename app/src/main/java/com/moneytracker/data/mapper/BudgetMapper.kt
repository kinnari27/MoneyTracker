package com.moneytracker.data.mapper

import com.moneytracker.data.local.entity.BudgetEntity
import com.moneytracker.domain.model.Budget

fun BudgetEntity.toDomain() = Budget(
    id       = id,
    category = category,
    limit    = limit,
    month    = month,
    year     = year
)

fun Budget.toEntity() = BudgetEntity(
    id       = id,
    category = category,
    limit    = limit,
    month    = month,
    year     = year
)