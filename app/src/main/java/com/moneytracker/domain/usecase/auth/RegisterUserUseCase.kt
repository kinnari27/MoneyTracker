package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        phone: String = "",
        occupation: String = "",
        incomeGoal: String = "",
        budgetLimit: String = "",
        currency: String = "INR"
    ): Result<Unit> {
        if (name.isBlank()) return Result.failure(Exception("Name cannot be empty"))
        if (email.isBlank()) return Result.failure(Exception("Email cannot be empty"))
        if (!email.contains("@")) return Result.failure(Exception("Enter a valid email"))
        if (password.length < 6) return Result.failure(Exception("Password must be at least 6 characters"))

        return repo.registerUser(
            user = User(
                name = name.trim(),
                email = email.trim(),
                phone = phone.trim(),
                occupation = occupation.trim(),
                incomeGoal = incomeGoal.toDoubleOrNull() ?: 0.0,
                budgetLimit = budgetLimit.toDoubleOrNull() ?: 0.0,
                currency = currency.trim()
            ),
            password = password
        )
    }
}
