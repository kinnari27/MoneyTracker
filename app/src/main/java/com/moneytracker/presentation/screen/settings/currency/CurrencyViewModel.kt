package com.moneytracker.presentation.screen.settings.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneytracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        loadUserCurrency()
    }

    private fun loadUserCurrency() {
        viewModelScope.launch {
            userRepository.observeCurrentUser.collect { user ->
                val code = user?.currency ?: "INR"
                val item = ALL_CURRENCIES.find { it.code.equals(code, ignoreCase = true) }
                    ?: ALL_CURRENCIES.first()
                _uiState.update { it.copy(selectedCurrency = item) }
            }
        }
    }

    fun onCurrencySelect(currency: CurrencyItem) {
        _uiState.update { it.copy(selectedCurrency = currency) }
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                userRepository.updateCurrency(user.id, currency.code)
            }
        }
    }

    fun onSearchChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredCurrencies = if (query.isBlank()) ALL_CURRENCIES
                else ALL_CURRENCIES.filter { c ->
                    c.name.contains(query, true) || c.code.contains(query, true)
                }
            )
        }
    }

    companion object {
        val ALL_CURRENCIES = listOf(
            CurrencyItem("INR", "Indian Rupee", "₹", "🇮🇳"),
            CurrencyItem("USD", "US Dollar", "$", "🇺🇸"),
            CurrencyItem("EUR", "Euro", "€", "🇪🇺"),
            CurrencyItem("GBP", "British Pound", "£", "🇬🇧"),
            CurrencyItem("JPY", "Japanese Yen", "¥", "🇯🇵"),
            CurrencyItem("AUD", "Australian Dollar", "A$", "🇦🇺"),
            CurrencyItem("CAD", "Canadian Dollar", "C$", "🇨🇦"),
            CurrencyItem("SGD", "Singapore Dollar", "S$", "🇸🇬"),
            CurrencyItem("AED", "UAE Dirham", "د.إ", "🇦🇪"),
            CurrencyItem("CHF", "Swiss Franc", "Fr", "🇨🇭"),
            CurrencyItem("CNY", "Chinese Yuan", "¥", "🇨🇳"),
            CurrencyItem("SAR", "Saudi Riyal", "﷼", "🇸🇦"),
            CurrencyItem("MYR", "Malaysian Ringgit", "RM", "🇲🇾"),
            CurrencyItem("THB", "Thai Baht", "฿", "🇹🇭"),
            CurrencyItem("NZD", "New Zealand Dollar", "NZ$", "🇳🇿"),
        )
    }
}