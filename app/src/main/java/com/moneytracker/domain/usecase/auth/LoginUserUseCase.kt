package com.moneytracker.domain.usecase.auth

import javax.inject.Inject

class LoginUserUseCase @Inject constructor() {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {

        return Result.success(Unit)
    }
}