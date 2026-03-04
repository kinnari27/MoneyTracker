package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.model.User
import com.moneytracker.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        phone: String,
        occupation: String,
        incomeGoal: String,
        budgetLimit: String,
        currency: String
    ): Result<Unit> {
        return try {
            val user = User(
                name = name,
                email = email,
                phone = phone,
                occupation = occupation,
                incomeGoal = incomeGoal,
                budgetLimit = budgetLimit,
                currency = currency,
                biometricEnabled = false
            )
            userRepository.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
