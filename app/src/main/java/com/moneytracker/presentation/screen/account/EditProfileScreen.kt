package com.moneytracker.presentation.screen.account

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.ui.theme.*

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        // ── Header ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                .padding(top = 52.dp, bottom = 40.dp)
        ) {
            // Back button
            IconButton(
                onClick  = onBack,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            ) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
            }

            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                // Avatar with camera overlay
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(0.2f))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = uiState.name.take(1).ifEmpty { "?" },
                            fontSize   = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Primary)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text("Edit Profile", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text("Update your personal info", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.7f))
            }
        }

        // ── Form ──────────────────────────────────────────────────────────────
        Column(
            modifier            = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ProfileFormCard(title = "Personal Info") {
                ProfileField(
                    label         = "Full Name",
                    value         = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder   = "Enter your name"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    label         = "Email",
                    value         = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder   = "Enter your email"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    label         = "Phone",
                    value         = uiState.phone,
                    onValueChange = viewModel::onPhoneChange,
                    placeholder   = "+91 XXXXX XXXXX"
                )
            }

            ProfileFormCard(title = "About") {
                ProfileField(
                    label         = "Occupation",
                    value         = uiState.occupation,
                    onValueChange = viewModel::onOccupationChange,
                    placeholder   = "e.g. Software Engineer"
                )
                Spacer(Modifier.height(12.dp))
                ProfileField(
                    label         = "Monthly Income Goal",
                    value         = uiState.incomeGoal,
                    onValueChange = viewModel::onIncomeGoalChange,
                    placeholder   = "₹ 0"
                )
            }

            // Error
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Error.copy(0.08f)),
                    shape  = RoundedCornerShape(14.dp)
                ) {
                    Text(uiState.error!!, color = Error, modifier = Modifier.padding(14.dp), style = MaterialTheme.typography.bodySmall)
                }
            }

            // Save
            Button(
                onClick  = viewModel::save,
                enabled  = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(18.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Save Changes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileFormCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text          = title.uppercase(),
            style         = MaterialTheme.typography.labelSmall,
            color         = OnSurfaceVariant,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier      = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = Surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), content = content)
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
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = OnSurfaceVariant, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = OnSurfaceVariant.copy(0.5f)) },
            singleLine    = true,
            shape         = RoundedCornerShape(14.dp),
            modifier      = Modifier.fillMaxWidth(),
            colors        = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Outline,
                focusedBorderColor   = Primary,
                focusedLabelColor    = Primary
            )
        )
    }
}