package com.moneytracker.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String = "",
    val occupation: String = "",
    val incomeGoal: String = "",
    val biometricEnabled: Boolean = false
)
