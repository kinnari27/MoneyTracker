package com.moneytracker.presentation.screen.settings.editProfile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.presentation.components.ProfileAvatar
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.Indigo600
import com.moneytracker.ui.theme.Slate100
import com.moneytracker.ui.theme.Slate400
import com.moneytracker.ui.theme.Slate900
import com.moneytracker.ui.theme.White
import com.moneytracker.ui.theme.moneytrackerTheme

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onProfileImageSelected(context, it) }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onBack()
    }

    EditProfileScreenContent(
        uiState = uiState,
        onBack = onBack,
        onPickPhoto = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onSyncGoogle = viewModel::syncWithGoogle,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onOccupationChange = viewModel::onOccupationChange,
        onIncomeGoalChange = viewModel::onIncomeGoalChange,
        onBudgetLimitChange = viewModel::onBudgetLimitChange,
        onSave = viewModel::save,
        modifier = modifier
    )
}

@Composable
fun EditProfileScreenContent(
    uiState: EditProfileUiState,
    onBack: () -> Unit,
    onPickPhoto: () -> Unit,
    onSyncGoogle: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOccupationChange: (String) -> Unit,
    onIncomeGoalChange: (Double) -> Unit,
    onBudgetLimitChange: (Double) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Header — dynamic background matching theme ───────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.colors.headerBackground)
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 28.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            ) {
                Icon(Icons.Default.ArrowBackIosNew, "Back", tint = White)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                // Avatar — rounded square with camera badge
                Box(
                    modifier = Modifier.clickable { onPickPhoto() },
                    contentAlignment = Alignment.BottomEnd
                ) {
                    ProfileAvatar(
                        name = uiState.name,
                        imageUrl = uiState.profilePictureUrl,
                        size = 84.dp,
                        shape = RoundedCornerShape(22.dp),
                        onClick = onPickPhoto
                    )
                    // Camera badge — small rounded square
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(cs.primary)
                            .border(2.dp, AppTheme.colors.headerBackground, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Change photo",
                            tint = White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                if (uiState.isGoogleConnected) {
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onSyncGoogle,
                        enabled = !uiState.isSyncingGoogle,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = White
                        ),
                        border = BorderStroke(1.dp, Slate400.copy(alpha = 0.4f)),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        if (uiState.isSyncingGoogle) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                color = White,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Syncing...",
                                style = MaterialTheme.typography.labelSmall,
                                color = White
                            )
                        } else {
                            Icon(
                                Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Sync Google Photo",
                                style = MaterialTheme.typography.labelSmall,
                                color = White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))
                Text(
                    "Edit Profile",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Update your personal info",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate400
                )
            }
        }

        // ── Form body ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ProfileFormCard(title = "Personal Info") {
                ProfileField("Full Name", uiState.name, onNameChange, "Rahul Sharma")
                Spacer(Modifier.height(12.dp))
                ProfileField("Email", uiState.email, onEmailChange, "rahul@gmail.com")
                Spacer(Modifier.height(12.dp))
                ProfileField("Phone", uiState.phone, onPhoneChange, "+91 XXXXX XXXXX")
            }

            ProfileFormCard(title = "Financial & Professional") {
                ProfileField(
                    "Occupation", uiState.occupation,
                    onOccupationChange, "e.g. Software Engineer"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    "Monthly Income Goal",
                    uiState.incomeGoal.toString(),
                    { onIncomeGoalChange(it.toDoubleOrNull() ?: 0.0) },
                    "₹ 0"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    "Monthly Budget Limit",
                    uiState.budgetLimit.toString(),
                    { onBudgetLimitChange(it.toDoubleOrNull() ?: 0.0) },
                    "₹ 0"
                )
            }

            // Error banner
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cs.errorContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = uiState.error,
                        color = cs.error,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Save button
            Button(
                onClick = onSave,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Indigo600,
                    contentColor = White
                )
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        color = White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Save Changes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Form section card ─────────────────────────────────────────────────────────
@Composable
private fun ProfileFormCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Column {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = cs.onSurfaceVariant,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = cs.surface,
                contentColor = cs.onSurface
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

// ── Single field ──────────────────────────────────────────────────────────────
@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val cs = MaterialTheme.colorScheme
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = cs.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder, color = cs.onSurfaceVariant.copy(alpha = 0.5f))
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = colors(
                unfocusedContainerColor = AppTheme.colors.inputBackground,
                focusedContainerColor = AppTheme.colors.inputBackground,
                unfocusedBorderColor = AppTheme.colors.outline,
                focusedBorderColor = cs.primary,
                unfocusedTextColor = cs.onSurface,
                focusedTextColor = cs.onSurface,
                unfocusedPlaceholderColor = cs.onSurfaceVariant.copy(alpha = 0.5f),
                focusedPlaceholderColor = cs.onSurfaceVariant.copy(alpha = 0.5f),
                cursorColor = cs.primary,
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
            onPickPhoto = {},
            onSyncGoogle = {},
            onNameChange = {},
            onEmailChange = {},
            onPhoneChange = {},
            onOccupationChange = {},
            onIncomeGoalChange = {},
            onBudgetLimitChange = {},
            onSave = {}
        )
    }
}