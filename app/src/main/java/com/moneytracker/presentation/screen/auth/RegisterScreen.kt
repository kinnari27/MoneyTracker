package com.moneytracker.presentation.screen.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    val focus = LocalFocusManager.current
    val strength = passwordStrength(uiState.password)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
    ) {
        // ── Gradient header blob ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Brush.verticalGradient(listOf(PrimaryDark, cs.primary)))
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.06f))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(52.dp))

            // ── Logo ──────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text("₹", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                "MoneyTracker",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                "Create your account",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(0.7f)
            )

            Spacer(Modifier.height(32.dp))

            // ── Form card ─────────────────────────────────────────────────────
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
                    defaultElevation = if (colors.isDark) 0.dp else 6.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Get Started",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = cs.onSurface
                        )
                        Text(
                            "Fill in your details below",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textSecondary
                        )
                    }

                    // Full Name
                    AuthField(
                        value = uiState.name,
                        onValueChange = onNameChange,
                        label = "Full Name",
                        placeholder = "Your full name",
                        leadingIcon = Icons.Outlined.Person,
                        imeAction = ImeAction.Next,
                        onImeAction = { focus.moveFocus(FocusDirection.Down) }
                    )

                    // Email
                    AuthField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        label = "Email",
                        placeholder = "Your email address",
                        leadingIcon = Icons.Outlined.Email,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        onImeAction = { focus.moveFocus(FocusDirection.Down) }
                    )

                    // Phone (optional)
                    AuthField(
                        value = uiState.phone,
                        onValueChange = onPhoneChange,
                        label = "Phone (optional)",
                        placeholder = "+91 XXXXX XXXXX",
                        leadingIcon = Icons.Outlined.Phone,
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next,
                        onImeAction = { focus.moveFocus(FocusDirection.Down) }
                    )

                    // Password
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

                    // Password strength bar
                    if (uiState.password.isNotEmpty()) {
                        PasswordStrengthBar(strength = strength)
                    }

                    // Confirm Password
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
                        onImeAction = { focus.clearFocus(); onRegister() }
                    )

                    // Error banner
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

                    // Create account button
                    Button(
                        onClick = onRegister,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = cs.onPrimary
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = cs.onPrimary,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                "Create Account",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Sign in link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
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

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ── Password strength helper ──────────────────────────────────────────────────
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
                .height(6.dp)
                .clip(RoundedCornerShape(99.dp)),
            color = color,
            trackColor = cs.surfaceVariant
        )
        Text(
            "Password strength: ${strength.label}",
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Register — Light", showBackground = true)
@Composable
private fun RegisterPreviewLight() {
    MoneyTrackerTheme(darkTheme = false) {
        RegisterScreenContent(
            uiState = AuthUiState(password = "Hello@123"),
            onNameChange = {}, onEmailChange = {}, onPhoneChange = {},
            onPasswordChange = {}, onConfirmChange = {},
            onTogglePassword = {}, onToggleConfirm = {},
            onRegister = {}, onNavigateToLogin = {}
        )
    }
}

@Preview(name = "Register — Dark + Error", showBackground = true)
@Composable
private fun RegisterPreviewDark() {
    MoneyTrackerTheme(darkTheme = true) {
        RegisterScreenContent(
            uiState = AuthUiState(error = "Passwords don't match"),
            onNameChange = {}, onEmailChange = {}, onPhoneChange = {},
            onPasswordChange = {}, onConfirmChange = {},
            onTogglePassword = {}, onToggleConfirm = {},
            onRegister = {}, onNavigateToLogin = {}
        )
    }
}