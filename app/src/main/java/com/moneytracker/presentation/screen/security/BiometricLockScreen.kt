package com.moneytracker.presentation.screen.security

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.R
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.MoneyTrackerTheme
import com.moneytracker.ui.theme.PrimaryDark

// ── Entry point ───────────────────────────────────────────────────────────────
@Composable
fun BiometricLockScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BiometricLockViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BiometricLockContent(
        uiState = uiState,
        onBiometricToggle = viewModel::onBiometricToggle,
        onFaceIdToggle = viewModel::onFaceIdToggle,
        onAutoLockSelect = viewModel::onAutoLockSelect,
        onBack = onBack,
        modifier = modifier
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────
@Composable
fun BiometricLockContent(
    uiState: BiometricLockUiState,
    onBiometricToggle: () -> Unit,
    onFaceIdToggle: () -> Unit,
    onAutoLockSelect: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    // Pulsing fingerprint icon when enabled
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (uiState.biometricEnabled || uiState.faceIdEnabled) 1.08f else 1f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "pulse_scale"
    )

    val isAnyEnabled = uiState.biometricEnabled || uiState.faceIdEnabled

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
                .background(Brush.linearGradient(listOf(PrimaryDark, cs.primary)))
                .padding(top = 52.dp, start = 4.dp, end = 20.dp, bottom = 32.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, "Back", tint = Color.White)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Biometric Lock",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    "Fingerprint / Face ID security",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Status card ───────────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cs.surface,
                    contentColor = cs.onSurface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (colors.isDark) 0.dp else 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                if (isAnyEnabled) cs.primary.copy(0.12f) else cs.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Fingerprint",
                            modifier = Modifier.size(72.dp),
                            tint = if (isAnyEnabled) cs.primary
                            else colors.textSecondary.copy(0.4f)
                        )
                    }
                    Text(
                        text = if (isAnyEnabled) "Biometric Active" else "Biometric Disabled",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isAnyEnabled) cs.primary else colors.textSecondary
                    )
                    Text(
                        text = if (isAnyEnabled)
                            "Your app is protected. You'll be asked to authenticate on every launch."
                        else
                            "Enable biometric authentication to add an extra layer of security.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ── Fingerprint toggle ────────────────────────────────────────────
            BiometricToggleCard(
                iconContent = {
                    Icon(
                        Icons.Default.Fingerprint,
                        null,
                        tint = cs.primary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                title = "Fingerprint Lock",
                subtitle = "Use fingerprint to unlock the app",
                checked = uiState.biometricEnabled,
                onToggle = onBiometricToggle,
                trackColor = cs.primary
            )

            // ── Face ID toggle ────────────────────────────────────────────────
            BiometricToggleCard(
                iconContent = {
                    Icon(
                        painterResource(R.drawable.ic_locked),
                        null,
                        tint = cs.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                title = "Face ID",
                subtitle = "Use face recognition to unlock",
                checked = uiState.faceIdEnabled,
                onToggle = onFaceIdToggle,
                trackColor = cs.secondary
            )

            // ── Auto-lock timing — only shown when any biometric is on ────────
            if (isAnyEnabled) {
                Column {
                    Text(
                        text = "AUTO-LOCK AFTER",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = cs.surface,
                            contentColor = cs.onSurface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (colors.isDark) 0.dp else 1.dp
                        )
                    ) {
                        val options = listOf("Immediately", "1 minute", "5 minutes", "15 minutes")
                        options.forEachIndexed { idx, option ->
                            val isSelected = uiState.autoLockOption == idx
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAutoLockSelect(idx) }
                                    .padding(horizontal = 20.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) cs.primary else cs.onSurface,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        null,
                                        tint = cs.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            if (idx < options.lastIndex) {
                                HorizontalDivider(
                                    color = colors.outline.copy(0.3f),
                                    thickness = 0.5.dp,
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ── Security info pill ────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.warningContainer,
                    contentColor = colors.warning
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text("🔐", fontSize = 16.sp)
                    Text(
                        "Biometric data stays on your device and is never shared with us.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.warning,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ── Reusable toggle row card ──────────────────────────────────────────────────
@Composable
private fun BiometricToggleCard(
    iconContent: @Composable () -> Unit,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit,
    trackColor: Color = MaterialTheme.colorScheme.primary
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surface,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (colors.isDark) 0.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(trackColor.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center,
                content = { iconContent() }
            )
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = trackColor
                )
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Biometric — Light Disabled", showBackground = true)
@Composable
private fun BiometricPreviewLight() {
    MoneyTrackerTheme(darkTheme = false) {
        BiometricLockContent(
            uiState = BiometricLockUiState(isLoading = false),
            onBiometricToggle = {},
            onFaceIdToggle = {},
            onAutoLockSelect = {},
            onBack = {}
        )
    }
}

@Preview(name = "Biometric — Dark Enabled", showBackground = true)
@Composable
private fun BiometricPreviewDark() {
    MoneyTrackerTheme(darkTheme = true) {
        BiometricLockContent(
            uiState = BiometricLockUiState(
                biometricEnabled = true,
                autoLockOption = 1,
                isLoading = false
            ),
            onBiometricToggle = {},
            onFaceIdToggle = {},
            onAutoLockSelect = {},
            onBack = {}
        )
    }
}