package com.moneytracker.data.mapper

import com.moneytracker.data.local.entity.UserEntity
import com.moneytracker.domain.model.User

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    phone = phone,
    occupation = occupation,
    incomeGoal = incomeGoal.toDoubleOrNull() ?: 0.0,
    budgetLimit = budgetLimit.toDoubleOrNull() ?: 0.0,
    currency = currency,
    biometricEnabled = biometricEnabled
)

fun User.toEntity(hashedPin: String = ""): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    phone = phone,
    occupation = occupation,
    incomeGoal = incomeGoal.toString(),
    budgetLimit = budgetLimit.toString(),
    currency = currency,
    biometricEnabled = biometricEnabled,
    pin = hashedPin
)
