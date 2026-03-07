package com.moneytracker.presentation.screen.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.moneytrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onBack()
    }

    EditProfileScreenContent(
        uiState = uiState,
        onBack = onBack,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onOccupationChange = viewModel::onOccupationChange,
        onIncomeGoalChange = viewModel::onIncomeGoalChange,
        onSave = viewModel::save,
        modifier = modifier
    )
}

@Composable
fun EditProfileScreenContent(
    uiState: EditProfileUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    onIncomeGoalChange: (Double) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ── Theme-aware color tokens ──────────────────────────────────────────────
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)             // page bg: #F0F4F8 light / #0F172A dark
            .verticalScroll(rememberScrollState())
    ) {

        // ── Header gradient ───────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryDark, cs.primary)))
                .padding(top = 52.dp, bottom = 40.dp)
        ) {
            // Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                // Avatar + camera badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.name.take(1).ifEmpty { "?" },
                            fontSize = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(cs.primary)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    "Edit Profile",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    "Update your personal info",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // ── Form body ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ProfileFormCard(title = "Personal Info") {
                ProfileField(
                    "Full Name",
                    uiState.name,
                    onNameChange,
                    "Rahul Sharma"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    "Email",
                    uiState.email,
                    onEmailChange,
                    "rahul@gmail.com"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    "Phone",
                    uiState.phone,
                    onPhoneChange,
                    "+91 XXXXX XXXXX"
                )
            }

            ProfileFormCard(title = "About") {
                ProfileField(
                    "Occupation",
                    uiState.occupation,
                    onOccupationChange,
                    "e.g. Software Engineer"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    "Monthly Income Goal",
                    uiState.incomeGoal.toString(),
                    { onIncomeGoalChange(it.toDoubleOrNull() ?: 0.0) },
                    "₹ 0"
                )
            }

            // Error banner
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colors.errorContainer  // dark-aware tinted red bg
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = uiState.error,
                        color = colors.error,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Save button
            Button(
                onClick = onSave,
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
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Check,
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Save Changes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Reusable sub-composables ──────────────────────────────────────────────────
@Composable
private fun ProfileFormCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    Column {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = cs.surface,
                contentColor = cs.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (colors.isDark) 0.dp else 2.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                content = content
            )
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = colors.textSecondary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = colors.textSecondary.copy(alpha = 0.5f)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = colors(
                unfocusedContainerColor = colors.surfaceVariant,
                focusedContainerColor = colors.surfaceVariant,
                unfocusedBorderColor = colors.outline,
                focusedBorderColor = cs.primary,
                unfocusedTextColor = cs.onSurface,
                focusedTextColor = cs.onSurface,
                cursorColor = cs.primary,
                focusedLabelColor = cs.primary,
                unfocusedLabelColor = colors.textSecondary,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    moneytrackerTheme {
        EditProfileScreenContent(
            uiState = EditProfileUiState(),
            onBack = {},
            onNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onOccupationChange = {},
            onIncomeGoalChange = {},
            onSave = {}
        )
    }
}
