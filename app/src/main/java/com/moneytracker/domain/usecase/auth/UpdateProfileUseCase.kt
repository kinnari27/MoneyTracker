package com.moneytracker.domain.usecase.auth

import com.moneytracker.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(
        id: Int,
        name: String,
        email: String,
        phone: String,
        occupation: String,
        incomeGoal: Double
    ) = repo.updateProfile(id, name, email, phone, occupation, incomeGoal)
}