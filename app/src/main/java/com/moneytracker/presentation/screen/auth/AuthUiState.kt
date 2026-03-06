package com.moneytracker.presentation.screen.auth

data class AuthUiState(
    // ── Step 1: Basic Info (required) ─────────────────────────────────────────
    val name: String = "",
    val email: String = "",

    // ── Step 2: Personalization (optional) ───────────────────────────────────
    val phone: String = "",
    val occupation: String = "",
    val incomeGoal: String = "",
    val budgetLimit: String = "",
    val currency: String = "INR",

    // ── Step 3: Security (required) ───────────────────────────────────────────
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmVisible: Boolean = false,

    // ── Shared state ─────────────────────────────────────────────────────────
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)