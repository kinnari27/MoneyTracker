package com.moneytracker.presentation.screen.settings.theme

import androidx.lifecycle.ViewModel
import com.moneytracker.data.local.ThemePreferences
import com.moneytracker.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = themePreferences.themeMode

    fun onThemeSelect(mode: ThemeMode) {
        themePreferences.setThemeMode(mode)
    }
}
