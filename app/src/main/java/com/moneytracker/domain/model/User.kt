package com.moneytracker.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String = "",
    val occupation: String = "",
    val incomeGoal: Double = 0.0,
    val budgetLimit: Double = 0.0,
    val currency: String = "INR",
    val biometricEnabled: Boolean = false,
    val isLoggedIn: Boolean = false
)
