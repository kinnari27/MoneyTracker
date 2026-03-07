package com.moneytracker.presentation.screen.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.MoneyTrackerTheme
import com.moneytracker.ui.theme.PrimaryDark

// ── Entry point ───────────────────────────────────────────────────────────────
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
        onBudgetLimitChange = viewModel::onBudgetLimitChange,
        onCurrencyChange = viewModel::onCurrencyChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onTogglePassword = viewModel::togglePasswordVisible,
        onToggleConfirm = viewModel::toggleConfirmVisible,
        onRegister = viewModel::register,
        onNavigateToLogin = onNavigateToLogin
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────
@Composable
fun RegisterScreenContent(
    uiState: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    onIncomeGoalChange: (String) -> Unit,
    onBudgetLimitChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onToggleConfirm: () -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    initialStep: Int = 0,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    var currentStep by remember { mutableIntStateOf(initialStep) }
    val totalSteps = 3
    val scrollState = rememberScrollState()

    // Step-level "Next" gate — only step 0 requires fields before proceeding
    val canProceedStep0 = uiState.name.isNotBlank() && uiState.email.isNotBlank()
            && uiState.email.contains("@")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
    ) {
        // ── Gradient header ───────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Brush.verticalGradient(listOf(PrimaryDark, cs.primary)))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // ── Logo ──────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.20f)),
                contentAlignment = Alignment.Center
            ) {
                Text("₹", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "MoneyTracker",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                "Create your account",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(0.70f)
            )

            Spacer(Modifier.height(20.dp))

            // ── Multi-step card ───────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cs.surface,
                    contentColor = cs.onSurface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (colors.isDark) 0.dp else 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // ── Step header row ───────────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = when (currentStep) {
                                    0 -> "Basic Info"
                                    1 -> "Personalization"
                                    else -> "Security"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = cs.onSurface
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
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = cs.primary
                                )
                            }
                        }
                    }

                    // ── Step dots + progress bar ──────────────────────────────
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(totalSteps) { idx ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(4.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                idx < currentStep -> cs.primary           // completed
                                                idx == currentStep -> cs.primary            // active
                                                else -> colors.inputBackground // upcoming
                                            }
                                        )
                                )
                            }
                        }
                        // Step subtitles
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("Required", "Optional", "Security").forEachIndexed { idx, lbl ->
                                Text(
                                    lbl,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (idx <= currentStep) cs.primary else colors.textSecondary,
                                    fontWeight = if (idx == currentStep) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // ── Animated step content ─────────────────────────────────
                    // Removed fixed height to allow content to expand naturally
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AnimatedContent(
                            targetState = currentStep,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "StepTransition"
                        ) { step ->
                            when (step) {
                                0 -> Step1BasicInfo(
                                    uiState = uiState,
                                    onNameChange = onNameChange,
                                    onEmailChange = onEmailChange
                                )

                                1 -> Step2Personalization(
                                    uiState = uiState,
                                    onPhoneChange = onPhoneChange,
                                    onOccupationChange = onOccupationChange,
                                    onIncomeGoalChange = onIncomeGoalChange,
                                    onBudgetLimitChange = onBudgetLimitChange,
                                    onCurrencyChange = onCurrencyChange
                                )

                                else -> Step3Security(
                                    uiState = uiState,
                                    onPasswordChange = onPasswordChange,
                                    onConfirmChange = onConfirmChange,
                                    onTogglePassword = onTogglePassword,
                                    onToggleConfirm = onToggleConfirm
                                )
                            }
                        }
                    }

                    // ── Error banner ──────────────────────────────────────────
                    AnimatedVisibility(
                        visible = uiState.error != null,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut()
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = colors.errorContainer,
                                contentColor = colors.error
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ErrorOutline,
                                    null,
                                    modifier = Modifier.size(16.dp),
                                    tint = colors.error
                                )
                                Text(
                                    uiState.error ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.error,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // ── Next / Register button ────────────────────────────────
                    Button(
                        onClick = {
                            if (currentStep < totalSteps - 1) currentStep++
                            else onRegister()
                        },
                        enabled = !uiState.isLoading && (currentStep != 0 || canProceedStep0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = cs.onPrimary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (currentStep < totalSteps - 1) "Next" else "Create Account",
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            if (currentStep < totalSteps - 1) {
                                Spacer(Modifier.width(6.dp))
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

            // ── Login link ────────────────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary
                )
                Text(
                    "Sign In",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = cs.primary,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            // Extra padding for scroll
            Spacer(Modifier.height(40.dp))
        }
    }
}

// ══════════════════════════════════════════════════════════════════════════════
//  STEP 1 — Basic Info (Name + Email are REQUIRED)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun Step1BasicInfo(
    uiState: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    val focus = LocalFocusManager.current
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        AuthField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = "Full Name",
            placeholder = "Enter your full name",
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
        // Hint label
        Text(
            "* Required fields",
            style = MaterialTheme.typography.labelSmall,
            color = AppTheme.colors.textSecondary
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
//  STEP 2 — Personalization (all optional)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun Step2Personalization(
    uiState: AuthUiState,
    onPhoneChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    onIncomeGoalChange: (String) -> Unit,
    onBudgetLimitChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit
) {
    val focus = LocalFocusManager.current
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    val currencies = listOf("INR", "USD", "EUR", "GBP", "JPY", "AED", "AUD", "CAD")
    val currencySymbols = mapOf(
        "INR" to "₹", "USD" to "$", "EUR" to "€", "GBP" to "£",
        "JPY" to "¥", "AED" to "د.إ", "AUD" to "A$", "CAD" to "C$"
    )
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Phone
        AuthField(
            value = uiState.phone,
            onValueChange = { if (it.length <= 10) onPhoneChange(it) },
            label = "Phone Number",
            placeholder = "10-digit mobile number",
            leadingIcon = Icons.Outlined.Phone,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
            onImeAction = { focus.moveFocus(FocusDirection.Down) }
        )

        // Occupation
        AuthField(
            value = uiState.occupation,
            onValueChange = onOccupationChange,
            label = "Occupation",
            placeholder = "e.g. Software Engineer",
            leadingIcon = Icons.Default.Work,
            imeAction = ImeAction.Next,
            onImeAction = { focus.moveFocus(FocusDirection.Down) }
        )

        // Income Goal + Budget Limit (side by side)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Monthly Income Goal",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = uiState.incomeGoal,
                    onValueChange = onIncomeGoalChange,
                    placeholder = {
                        Text(
                            "e.g. 50000",
                            color = colors.textSecondary.copy(0.5f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onNext = { focus.moveFocus(FocusDirection.Right) }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colors.inputBackground,
                        focusedContainerColor = colors.inputBackground,
                        unfocusedBorderColor = colors.outline,
                        focusedBorderColor = cs.primary,
                        unfocusedTextColor = cs.onSurface,
                        focusedTextColor = cs.onSurface,
                        cursorColor = cs.primary
                    )
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Monthly Budget Limit",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = uiState.budgetLimit,
                    onValueChange = onBudgetLimitChange,
                    placeholder = {
                        Text(
                            "e.g. 20000",
                            color = colors.textSecondary.copy(0.5f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = { focus.clearFocus() }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colors.inputBackground,
                        focusedContainerColor = colors.inputBackground,
                        unfocusedBorderColor = colors.outline,
                        focusedBorderColor = cs.primary,
                        unfocusedTextColor = cs.onSurface,
                        focusedTextColor = cs.onSurface,
                        cursorColor = cs.primary
                    )
                )
            }
        }

        // Currency picker
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "Preferred Currency",
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary,
                fontWeight = FontWeight.SemiBold
            )
            Box {
                OutlinedTextField(
                    value = "${uiState.currency}  ${currencySymbols[uiState.currency] ?: ""}",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    leadingIcon = {
                        Icon(Icons.Outlined.AccountBalanceWallet, null, tint = colors.textSecondary)
                    },
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, null, tint = colors.textSecondary)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colors.inputBackground,
                        focusedContainerColor = colors.inputBackground,
                        unfocusedBorderColor = colors.outline,
                        focusedBorderColor = cs.primary,
                        unfocusedTextColor = cs.onSurface,
                        focusedTextColor = cs.onSurface
                    )
                )
                // Transparent overlay to detect clicks on the whole field
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { expanded = true }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(cs.surface)
                ) {
                    currencies.forEach { code ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "$code  ${currencySymbols[code] ?: ""}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = cs.onSurface
                                )
                            },
                            onClick = {
                                onCurrencyChange(code)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Text(
            "All fields on this step are optional — you can update them later in Settings.",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary
        )
    }
}

// ══════════════════════════════════════════════════════════════════════════════
//  STEP 3 — Security (Password required)
// ══════════════════════════════════════════════════════════════════════════════
@Composable
private fun Step3Security(
    uiState: AuthUiState,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    onToggleConfirm: () -> Unit
) {
    val focus = LocalFocusManager.current
    val colors = AppTheme.colors

    // Live password strength
    val strength = passwordStrength(uiState.password)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        AuthField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Password",
            placeholder = "Min. 6 characters",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            passwordVisible = uiState.passwordVisible,
            onTogglePassword = onTogglePassword,
            isRequired = true,
            imeAction = ImeAction.Next,
            onImeAction = { focus.moveFocus(FocusDirection.Down) }
        )

        // Strength bar — only shown while typing
        if (uiState.password.isNotEmpty()) {
            PasswordStrengthBar(strength = strength)
        }

        AuthField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmChange,
            label = "Confirm Password",
            placeholder = "Re-enter password",
            leadingIcon = Icons.Outlined.Lock,
            isPassword = true,
            passwordVisible = uiState.confirmVisible,
            onTogglePassword = onToggleConfirm,
            isRequired = true,
            imeAction = ImeAction.Done,
            onImeAction = { focus.clearFocus() }
        )

        // Match indicator
        if (uiState.confirmPassword.isNotEmpty()) {
            val matches = uiState.password == uiState.confirmPassword
            Text(
                text = if (matches) "✓ Passwords match" else "✗ Passwords don't match",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (matches) colors.success else colors.error
            )
        }
    }
}

// ── Password strength helpers ─────────────────────────────────────────────────
enum class PasswordStrength(val label: String, val fraction: Float) {
    WEAK("Weak", 0.33f),
    MEDIUM("Medium", 0.66f),
    STRONG("Strong", 1.00f)
}

fun passwordStrength(password: String): PasswordStrength {
    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    return when {
        score >= 3 -> PasswordStrength.STRONG
        score == 2 -> PasswordStrength.MEDIUM
        else -> PasswordStrength.WEAK
    }
}

@Composable
private fun PasswordStrengthBar(strength: PasswordStrength) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    val color = when (strength) {
        PasswordStrength.WEAK -> colors.error
        PasswordStrength.MEDIUM -> colors.warning
        PasswordStrength.STRONG -> colors.success
    }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        LinearProgressIndicator(
            progress = { strength.fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(CircleShape),
            color = color,
            trackColor = cs.surfaceVariant
        )
        Text(
            "Strength: ${strength.label}",
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Step 1: Basic Info", showBackground = true)
@Composable
private fun RegisterPreviewStep1() {
    MoneyTrackerTheme {
        RegisterScreenContent(
            uiState = AuthUiState(name = "Rahul Sharma", email = "rahul@example.com"),
            onNameChange = {}, onEmailChange = {},
            onPhoneChange = {}, onOccupationChange = {},
            onIncomeGoalChange = {}, onBudgetLimitChange = {},
            onCurrencyChange = {}, onPasswordChange = {},
            onConfirmChange = {}, onTogglePassword = {},
            onToggleConfirm = {}, onRegister = {},
            onNavigateToLogin = {},
            initialStep = 0
        )
    }
}

@Preview(name = "Step 2: Personalization", showBackground = true)
@Composable
private fun RegisterPreviewStep2() {
    MoneyTrackerTheme {
        RegisterScreenContent(
            uiState = AuthUiState(
                name = "Rahul Sharma",
                email = "rahul@example.com",
                phone = "9876543210",
                occupation = "Software Engineer"
            ),
            onNameChange = {}, onEmailChange = {},
            onPhoneChange = {}, onOccupationChange = {},
            onIncomeGoalChange = {}, onBudgetLimitChange = {},
            onCurrencyChange = {}, onPasswordChange = {},
            onConfirmChange = {}, onTogglePassword = {},
            onToggleConfirm = {}, onRegister = {},
            onNavigateToLogin = {},
            initialStep = 1
        )
    }
}

@Preview(name = "Step 3: Security", showBackground = true)
@Composable
private fun RegisterPreviewStep3() {
    MoneyTrackerTheme {
        RegisterScreenContent(
            uiState = AuthUiState(
                name = "Rahul Sharma",
                email = "rahul@example.com",
                password = "Password@123"
            ),
            onNameChange = {}, onEmailChange = {},
            onPhoneChange = {}, onOccupationChange = {},
            onIncomeGoalChange = {}, onBudgetLimitChange = {},
            onCurrencyChange = {}, onPasswordChange = {},
            onConfirmChange = {}, onTogglePassword = {},
            onToggleConfirm = {}, onRegister = {},
            onNavigateToLogin = {},
            initialStep = 2
        )
    }
}
