package com.moneytracker.presentation.screen.changePin

enum class PinStep { CURRENT, NEW, CONFIRM }

data class ChangePinUiState(
    val step       : PinStep = PinStep.CURRENT,
    val currentPin : String  = "",
    val newPin     : String  = "",
    val confirmPin : String  = "",
    val error      : String? = null,
    val isComplete : Boolean = false
)