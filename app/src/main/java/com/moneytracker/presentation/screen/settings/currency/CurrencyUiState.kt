package com.moneytracker.presentation.screen.settings.currency

import com.moneytracker.presentation.screen.settings.currency.CurrencyViewModel.Companion.ALL_CURRENCIES

data class CurrencyItem(
    val code: String,
    val name: String,
    val symbol: String,
    val flag: String
)

data class CurrencyUiState(
    val selectedCurrency: CurrencyItem = CurrencyItem("INR", "Indian Rupee", "₹", "🇮🇳"),
    val searchQuery: String = "",
    val allCurrencies: List<CurrencyItem> = ALL_CURRENCIES,
    val filteredCurrencies: List<CurrencyItem> = ALL_CURRENCIES
)