package com.moneytracker.presentation.screen.settings.changePassword

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.presentation.screen.auth.AuthField
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.MoneyTrackerTheme
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Success
import com.moneytracker.ui.theme.SurfaceVariant

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            // Give some time to see success message
            kotlinx.coroutines.delay(1000)
            onBack()
        }
    }

    ChangePasswordScreenContent(
        uiState = uiState,
        onBack = onBack,
        onCurrentChange = viewModel::onCurrentPasswordChange,
        onNewChange = viewModel::onNewPasswordChange,
        onConfirmChange = viewModel::onConfirmPasswordChange,
        onToggleVisible = viewModel::togglePasswordVisibility,
        onToggleConfirmVisible = viewModel::toggleConfirmVisibility,
        onNext = viewModel::next,
        modifier = modifier
    )
}

@Composable
fun ChangePasswordScreenContent(
    uiState: ChangePasswordUiState,
    onBack: () -> Unit,
    onCurrentChange: (String) -> Unit,
    onNewChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onToggleVisible: () -> Unit,
    onToggleConfirmVisible: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Header ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                .padding(top = 52.dp, start = 4.dp, end = 20.dp, bottom = 32.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, null, tint = Color.White)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Change Password",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    "Update your security credentials",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (!uiState.hasPassword && !uiState.isLoading) {
            Box(Modifier
                .fillMaxSize()
                .padding(32.dp), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.Lock,
                        null,
                        modifier = Modifier.size(64.dp),
                        tint = colors.textSecondary.copy(0.3f)
                    )
                    Text(
                        "You are signed in with Google. You can manage your security settings in your Google Account.",
                        textAlign = TextAlign.Center,
                        color = colors.textSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(onClick = onBack, shape = RoundedCornerShape(12.dp)) {
                        Text("Go Back")
                    }
                }
            }
            return@Column
        }

        // Step indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PasswordStep.values().forEach { step ->
                val isPast = uiState.step.ordinal > step.ordinal
                val isCurrent = uiState.step == step
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(
                            when {
                                isPast -> Primary
                                isCurrent -> Primary.copy(0.5f)
                                else -> SurfaceVariant
                            }
                        )
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Step card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = when (uiState.step) {
                            PasswordStep.CURRENT -> "Verify Identity"
                            PasswordStep.NEW -> "New Password"
                            PasswordStep.CONFIRM -> "Confirm Change"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.onSurface
                    )

                    AnimatedContent(
                        targetState = uiState.step,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "StepContent"
                    ) { step ->
                        when (step) {
                            PasswordStep.CURRENT -> AuthField(
                                value = uiState.currentPassword,
                                onValueChange = onCurrentChange,
                                label = "Current Password",
                                placeholder = "Enter current password",
                                leadingIcon = Icons.Default.Lock,
                                isPassword = true,
                                passwordVisible = uiState.passwordVisible,
                                onTogglePassword = onToggleVisible,
                                imeAction = ImeAction.Done,
                                onImeAction = onNext
                            )

                            PasswordStep.NEW -> AuthField(
                                value = uiState.newPassword,
                                onValueChange = onNewChange,
                                label = "New Password",
                                placeholder = "Min. 4 characters",
                                leadingIcon = Icons.Default.Lock,
                                isPassword = true,
                                passwordVisible = uiState.passwordVisible,
                                onTogglePassword = onToggleVisible,
                                imeAction = ImeAction.Next,
                                onImeAction = onNext
                            )

                            PasswordStep.CONFIRM -> AuthField(
                                value = uiState.confirmPassword,
                                onValueChange = onConfirmChange,
                                label = "Confirm Password",
                                placeholder = "Re-enter new password",
                                leadingIcon = Icons.Default.Lock,
                                isPassword = true,
                                passwordVisible = uiState.confirmVisible,
                                onTogglePassword = onToggleConfirmVisible,
                                imeAction = ImeAction.Done,
                                onImeAction = onNext
                            )
                        }
                    }

                    // Error message
                    AnimatedVisibility(visible = uiState.error != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Error.copy(0.1f))
                                .padding(12.dp)
                        ) {
                            Icon(
                                Icons.Default.ErrorOutline,
                                null,
                                tint = Error,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = uiState.error ?: "",
                                color = Error,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Button(
                        onClick = onNext,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                if (uiState.step == PasswordStep.CONFIRM) "Update Password" else "Continue",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Success state
            AnimatedVisibility(visible = uiState.isComplete) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Success.copy(0.1f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, null, tint = Success)
                        Text(
                            "Password updated successfully!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Success,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    MoneyTrackerTheme {
        ChangePasswordScreenContent(
            uiState = ChangePasswordUiState(
                step = PasswordStep.NEW,
                newPassword = "pass"
            ),
            onBack = {},
            onCurrentChange = {},
            onNewChange = {},
            onConfirmChange = {},
            onToggleVisible = {},
            onToggleConfirmVisible = {},
            onNext = {}
        )
    }
}
