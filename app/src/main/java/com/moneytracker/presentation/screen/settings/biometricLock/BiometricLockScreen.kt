package com.moneytracker.presentation.screen.settings.biometricLock

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.statusBarsPadding
import com.moneytracker.R
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.Indigo100
import com.moneytracker.ui.theme.Indigo200
import com.moneytracker.ui.theme.Indigo600
import com.moneytracker.ui.theme.Invest100
import com.moneytracker.ui.theme.Invest600
import com.moneytracker.ui.theme.Slate100
import com.moneytracker.ui.theme.Slate400
import com.moneytracker.ui.theme.Slate900
import com.moneytracker.ui.theme.White
import com.moneytracker.ui.theme.moneytrackerTheme

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
    val isAnyEnabled = uiState.biometricEnabled || uiState.faceIdEnabled

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
                .padding(top = 16.dp, start = 4.dp, end = 20.dp, bottom = 24.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, "Back", tint = White)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Biometric Lock",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    "Fingerprint / Face ID security",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate400,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── Status card — flat, no pulsing circle ─────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Static rounded-square icon container — no circle, no pulse
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                if (isAnyEnabled) cs.primaryContainer else cs.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Fingerprint",
                            modifier = Modifier.size(48.dp),
                            tint = if (isAnyEnabled) Indigo600 else Slate400
                        )
                    }

                    Text(
                        text = if (isAnyEnabled) "Biometric Active" else "Biometric Disabled",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isAnyEnabled) Indigo600 else cs.onSurfaceVariant
                    )
                    Text(
                        text = if (isAnyEnabled)
                            "Your app is protected. You'll be asked to authenticate on every launch."
                        else
                            "Enable biometric authentication to add an extra layer of security.",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ── Fingerprint toggle ────────────────────────────────────────────
            BiometricToggleCard(
                iconContent = {
                    Icon(
                        Icons.Default.Fingerprint, null,
                        tint = Indigo600,
                        modifier = Modifier.size(22.dp)
                    )
                },
                iconBg = Indigo100,
                title = "Fingerprint Lock",
                subtitle = "Use fingerprint to unlock the app",
                checked = uiState.biometricEnabled,
                onToggle = onBiometricToggle,
                trackColor = Indigo600
            )

            // ── Face ID toggle ────────────────────────────────────────────────
            BiometricToggleCard(
                iconContent = {
                    Icon(
                        painterResource(R.drawable.ic_locked), null,
                        tint = cs.secondary,
                        modifier = Modifier.size(22.dp)
                    )
                },
                iconBg = Indigo200.copy(alpha = 0.4f),
                title = "Face ID",
                subtitle = "Use face recognition to unlock",
                checked = uiState.faceIdEnabled,
                onToggle = onFaceIdToggle,
                trackColor = cs.secondary
            )

            // ── Auto-lock — visible only when any biometric is on ─────────────
            if (isAnyEnabled) {
                Column {
                    Text(
                        text = "AUTO-LOCK AFTER",
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onSurfaceVariant,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cs.surface),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        val options = listOf("Immediately", "1 minute", "5 minutes", "15 minutes")
                        options.forEachIndexed { idx, option ->
                            val isSelected = uiState.autoLockOption == idx
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAutoLockSelect(idx) }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) Indigo600 else cs.onSurface,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check, null,
                                        tint = Indigo600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            if (idx < options.lastIndex) HorizontalDivider(
                                color = cs.outline,
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }

            // ── Security info notice — amber tint, no emoji ───────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Invest100),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Check,
                        null,
                        tint = Invest600,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 1.dp)
                    )
                    Text(
                        "Biometric data stays on your device and is never shared with us.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Invest600,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ── Reusable toggle row ───────────────────────────────────────────────────────
@Composable
private fun BiometricToggleCard(
    iconContent: @Composable () -> Unit,
    iconBg: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit,
    trackColor: Color = Indigo600
) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center,
                content = { iconContent() }
            )
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = cs.onSurface
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    checkedTrackColor = trackColor
                )
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Biometric — Disabled", showBackground = true)
@Composable
private fun BiometricPreviewDisabled() {
    moneytrackerTheme(darkTheme = false) {
        BiometricLockContent(
            uiState = BiometricLockUiState(isLoading = false),
            onBiometricToggle = {},
            onFaceIdToggle = {},
            onAutoLockSelect = {},
            onBack = {}
        )
    }
}

@Preview(name = "Biometric — Enabled Dark", showBackground = true)
@Composable
private fun BiometricPreviewEnabled() {
    moneytrackerTheme(darkTheme = true) {
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