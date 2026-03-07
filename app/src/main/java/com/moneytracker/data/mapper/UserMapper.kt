package com.moneytracker.data.mapper

import com.moneytracker.data.local.entity.UserEntity
import com.moneytracker.domain.model.User

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    phone = phone,
    occupation = occupation,
    incomeGoal = incomeGoal,
    budgetLimit = budgetLimit,
    currency = currency,
    biometricEnabled = biometricEnabled,
    isLoggedIn = isLoggedIn
)

fun User.toEntity(hashedPin: String = ""): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    phone = phone,
    occupation = occupation,
    incomeGoal = incomeGoal,
    budgetLimit = budgetLimit,
    currency = currency,
    biometricEnabled = biometricEnabled,
    isLoggedIn = isLoggedIn,
    pin = hashedPin
)
