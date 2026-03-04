package com.moneytracker.presentation.navigation

object Routes {
    // Auth
    const val SPLASH   = "splash"
    const val LOGIN    = "login"
    const val REGISTER = "register"

    // Transaction edit — parametrised
    const val EDIT_TRANSACTION = "edit_transaction/{transactionId}"
    fun editTransaction(id: Int) = "edit_transaction/$id"

    // Settings
    const val EDIT_PROFILE   = "edit_profile"
    const val NOTIFICATIONS  = "notifications"
    const val CURRENCY       = "currency"
    const val BIOMETRIC_LOCK = "biometric_lock"
    const val CHANGE_PIN     = "change_pin"
}