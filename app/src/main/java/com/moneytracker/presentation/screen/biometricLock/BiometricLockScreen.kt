package com.moneytracker.presentation.screen.biometricLock

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.R
import com.moneytracker.ui.theme.*

@Composable
fun BiometricLockScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BiometricLockViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Pulse animation for fingerprint icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue   = 1f,
        targetValue    = if (uiState.biometricEnabled) 1.08f else 1f,
        animationSpec  = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label          = "pulse_scale"
    )

    Column(modifier = modifier.fillMaxSize()) {

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
                Text("Biometric Lock", style = MaterialTheme.typography.titleLarge,  fontWeight = FontWeight.ExtraBold, color = Color.White, modifier = Modifier.padding(horizontal = 16.dp))
                Text("Fingerprint / Face ID security", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.7f), modifier = Modifier.padding(horizontal = 16.dp))
            }
        }

        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Fingerprint visual ────────────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(24.dp),
                colors    = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier            = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(
                                if (uiState.biometricEnabled) Primary.copy(0.12f)
                                else SurfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Fingerprint,
                            contentDescription = "Fingerprint",
                            modifier           = Modifier.size(72.dp),
                            tint               = if (uiState.biometricEnabled) Primary else OnSurfaceVariant.copy(0.4f)
                        )
                    }
                    Text(
                        text       = if (uiState.biometricEnabled) "Biometric Active" else "Biometric Disabled",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color      = if (uiState.biometricEnabled) Primary else OnSurfaceVariant
                    )
                    Text(
                        text      = if (uiState.biometricEnabled)
                            "Your app is protected with biometrics. You'll be asked to authenticate on every launch."
                        else
                            "Enable biometric authentication to add an extra layer of security to your account.",
                        style     = MaterialTheme.typography.bodySmall,
                        color     = OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ── Toggle ────────────────────────────────────────────────────────
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier              = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier         = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Primary.copy(0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Fingerprint, null, tint = Primary, modifier = Modifier.size(24.dp))
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Fingerprint Lock", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = OnSurface)
                        Text("Use fingerprint to unlock", style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    }
                    Switch(
                        checked         = uiState.biometricEnabled,
                        onCheckedChange = { viewModel.onBiometricToggle() },
                        colors          = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Primary)
                    )
                }
            }

            // Face ID toggle (secondary option)
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier              = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier         = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Secondary.copy(0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter            = painterResource(R.drawable.ic_locked),
                            contentDescription = null,
                            modifier           = Modifier.size(24.dp),
                            tint               = Secondary
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Face ID",              style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = OnSurface)
                        Text("Use face to unlock",   style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
                    }
                    Switch(
                        checked         = uiState.faceIdEnabled,
                        onCheckedChange = { viewModel.onFaceIdToggle() },
                        colors          = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Secondary)
                    )
                }
            }

            // ── Auto-lock timing ──────────────────────────────────────────────
            if (uiState.biometricEnabled || uiState.faceIdEnabled) {
                Column {
                    Text(
                        text          = "AUTO-LOCK AFTER",
                        style         = MaterialTheme.typography.labelSmall,
                        color         = OnSurfaceVariant,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier      = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Card(
                        shape     = RoundedCornerShape(20.dp),
                        colors    = CardDefaults.cardColors(containerColor = Surface),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        listOf("Immediately", "1 minute", "5 minutes", "15 minutes").forEachIndexed { idx, option ->
                            val isSelected = uiState.autoLockOption == idx
                            Row(
                                modifier              = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = { viewModel.onAutoLockSelect(idx) })
                                    .padding(horizontal = 20.dp, vertical = 14.dp),
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    option,
                                    style      = MaterialTheme.typography.bodyMedium,
                                    color      = if (isSelected) Primary else OnSurface,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.ArrowBackIosNew, null, tint = Primary,
                                        modifier = Modifier.size(16.dp))
                                }
                            }
                            if (idx < 3) HorizontalDivider(color = Outline.copy(0.3f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 20.dp))
                        }
                    }
                }
            }

            // Security note
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(16.dp),
                colors    = CardDefaults.cardColors(containerColor = Warning.copy(0.06f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier              = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment     = Alignment.Top
                ) {
                    Text("🔐", fontSize = 16.sp)
                    Text(
                        "Biometric data stays on your device and is never shared with us.",
                        style  = MaterialTheme.typography.bodySmall,
                        color  = Warning,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
