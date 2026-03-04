package com.moneytracker.domain.usecase.auth

import javax.inject.Inject

class RegisterUserUseCase @Inject constructor() {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        phone: String
    ): Result<Unit> {
        return Result.success(Unit)
    }
}