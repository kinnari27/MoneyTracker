package com.moneytracker.domain.repository

import com.moneytracker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // ── Observe (reactive) ────────────────────────────────────────────────────
    /** Hot Flow — emits whenever the current user row changes in Room. */
    val observeCurrentUser: Flow<User?>

    // ── One-shot reads ────────────────────────────────────────────────────────
    /** Suspend fetch — use where a one-time value is enough. */
    suspend fun getCurrentUser(): User?

    /** Returns true if at least one account exists (used by SplashViewModel). */
    suspend fun hasAccount(): Boolean

    /** Returns true if there is a user currently marked as logged in. */
    suspend fun isUserLoggedIn(): Boolean

    // ── Auth ──────────────────────────────────────────────────────────────────
    /** Persist a new user + hashed password. Fails if email already registered. */
    suspend fun registerUser(user: User, password: String): Result<Unit>

    /** Verify email + password; returns the matching User or a descriptive error. */
    suspend fun loginUser(email: String, password: String): Result<User>

    /** Sign in with Firebase using Google ID Token. */
    suspend fun signInWithGoogle(idToken: String): Result<User>

    /** Remove the login status for the user (full sign-out for a single-user app). */
    suspend fun signOut()

    // ── Profile updates ───────────────────────────────────────────────────────
    /** Update editable profile fields (name, email, phone, occupation, incomeGoal). */
    suspend fun updateProfile(
        userId: Int,
        name: String,
        email: String,
        phone: String,
        occupation: String,
        incomeGoal: Double,
        budgetLimit: Double
    )

    /** Swap the preferred currency code. */
    suspend fun updateCurrency(userId: Int, code: String)

    /** Update profile picture URL or local image path. */
    suspend fun updateProfilePicture(userId: Int, url: String?)

    /** Sync user details (photo, display name) from active Google/Firebase user. */
    suspend fun syncGoogleProfile(): Result<User>

    // ── Security ──────────────────────────────────────────────────────────────
    /** Replace the stored hashed PIN. Returns failure if the userId is invalid. */
    suspend fun updatePin(userId: Int, newPin: String): Result<Unit>

    /** Toggle biometric authentication on / off for the given user. */
    suspend fun updateBiometric(userId: Int, enabled: Boolean)

    // ── Insert (used internally by registerUser; exposed for DI / testing) ────
    /** Low-level insert — prefer [registerUser] in production code. */
    suspend fun insertUser(user: User)
}
