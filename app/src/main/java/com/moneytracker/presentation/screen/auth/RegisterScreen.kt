package com.moneytracker.presentation.screen.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.PrimaryDark

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.resetSuccess()
            onRegisterSuccess()
        }
    }

    RegisterScreenContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onOccupationChange = viewModel::onOccupationChange,
        onIncomeGoalChange = viewModel::onIncomeGoalChange,
        onCurrencyChange = viewModel::onCurrencyChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onTogglePassword = viewModel::togglePasswordVisible,
        onToggleConfirm = viewModel::toggleConfirmVisible,
        onRegister = viewModel::register,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun RegisterScreenContent(
    uiState: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    onIncomeGoalChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onToggleConfirm: () -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 3

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
    ) {
        // Gradient Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Brush.verticalGradient(listOf(PrimaryDark, cs.primary)))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // Logo & Title
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("₹", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "MoneyTracker",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Step Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                when (currentStep) {
                                    0 -> "Basic Info"
                                    1 -> "Personalization"
                                    else -> "Security"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Step ${currentStep + 1} of $totalSteps",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.textSecondary
                            )
                        }
                        if (currentStep > 0) {
                            IconButton(
                                onClick = { currentStep-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = cs.primary)
                            }
                        }
                    }

                    LinearProgressIndicator(
                        progress = { (currentStep + 1).toFloat() / totalSteps },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape),
                        color = cs.primary,
                        trackColor = colors.inputBackground
                    )

                    // Step Content with Animation (No Scroll)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                    ) {
                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "StepTransition"
                        ) { step ->
                            when (step) {
                                0 -> BasicInfoStep(uiState, onNameChange, onEmailChange)
                                1 -> PersonalizationStep(
                                    uiState,
                                    onPhoneChange,
                                    onOccupationChange,
                                    onCurrencyChange
                                )

                                2 -> SecurityStep(
                                    uiState,
                                    onPasswordChange,
                                    onConfirmChange,
                                    onTogglePassword,
                                    onToggleConfirm
                                )
                            }
                        }
                    }

                    // Navigation Actions
                    val isFirstStepValid = uiState.name.isNotBlank() && uiState.email.isNotBlank()

                    Button(
                        onClick = {
                            if (currentStep < totalSteps - 1) currentStep++
                            else onRegister()
                        },
                        enabled = !uiState.isLoading && (currentStep != 0 || isFirstStepValid),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = cs.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(if (currentStep < totalSteps - 1) "Next" else "Register")
                            if (currentStep < totalSteps - 1) {
                                Spacer(Modifier.size(8.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Row(modifier = Modifier.clickable { onNavigateToLogin() }) {
                Text("Already have an account? ", color = colors.textSecondary)
                Text("Login", fontWeight = FontWeight.Bold, color = cs.primary)
            }
        }
    }
}

@Composable
private fun BasicInfoStep(
    uiState: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    val focus = LocalFocusManager.current
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AuthField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = "Full Name",
            placeholder = "Enter your name",
            leadingIcon = Icons.Outlined.Person,
            isRequired = true,
            imeAction = ImeAction.Next,
            onImeAction = { focus.moveFocus(FocusDirection.Down) }
        )
        AuthField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email Address",
            placeholder = "example@mail.com",
            leadingIcon = Icons.Outlined.Email,
            isRequired = true,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
            onImeAction = { focus.clearFocus() }
        )
    }
}

@Composable
private fun PersonalizationStep(
    uiState: AuthUiState,
    onPhoneChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit
) {
    val focus = LocalFocusManager.current
    val currencies = listOf("INR (₹)", "USD ($)", "EUR (€)", "GBP (£)", "JPY (¥)")
    var expanded by remember { mutableStateOf(false) }
    val colors = AppTheme.colors
    val cs = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AuthField(
            value = uiState.phone,
            onValueChange = { if (it.length <= 10) onPhoneChange(it) },
            label = "Phone (Optional)",
            placeholder = "10-digit number",
            leadingIcon = Icons.Outlined.Phone,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            onImeAction = { focus.moveFocus(FocusDirection.Down) }
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Currency",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Box {
                OutlinedTextField(
                    value = uiState.currency,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    shape = RoundedCornerShape(14.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.AccountBalanceWallet,
                            null,
                            tint = colors.textSecondary
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colors.inputBackground,
                        focusedContainerColor = colors.inputBackground,
                        unfocusedBorderColor = colors.outline,
                        focusedBorderColor = cs.primary
                    )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background(cs.surface)
                ) {
                    currencies.forEach { currency ->
                        DropdownMenuItem(
                            text = { Text(currency) },
                            onClick = {
                                onCurrencyChange(currency.split(" ")[0])
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SecurityStep(
    uiState: AuthUiState,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onToggleConfirm: () -> Unit
) {
    val focus = LocalFocusManager.current
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        AuthField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            placeholder = "Min. 6 characters",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            passwordVisible = uiState.passwordVisible,
            onTogglePassword = onTogglePassword,
            imeAction = ImeAction.Next,
            onImeAction = { focus.moveFocus(FocusDirection.Down) }
        )
        AuthField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmChange,
            label = "Confirm Password",
            placeholder = "Re-enter password",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            passwordVisible = uiState.confirmVisible,
            onTogglePassword = onToggleConfirm,
            imeAction = ImeAction.Done,
            onImeAction = { focus.clearFocus() }
        )
    }
}