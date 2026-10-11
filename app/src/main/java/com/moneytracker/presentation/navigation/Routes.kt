package com.moneytracker.presentation.navigation

object Routes {
    // Auth
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val EDIT_TRANSACTION = "edit_transaction/{transactionId}"
    fun editTransaction(id: Int) = "edit_transaction/$id"
    const val EDIT_PROFILE = "edit_profile"
    const val NOTIFICATIONS = "notifications"
    const val CURRENCY = "currency"
    const val THEME = "theme"
    const val BIOMETRIC_LOCK = "biometric_lock"
    const val CHANGE_PASSWORD = "change_password"
}