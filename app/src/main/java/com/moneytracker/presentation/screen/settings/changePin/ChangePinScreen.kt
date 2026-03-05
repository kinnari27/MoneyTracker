package com.moneytracker.presentation.screen.settings.security

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.presentation.screen.settings.changePin.ChangePinUiState
import com.moneytracker.presentation.screen.settings.changePin.ChangePinViewModel
import com.moneytracker.presentation.screen.settings.changePin.PinStep
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.OnSurface
import com.moneytracker.ui.theme.OnSurfaceVariant
import com.moneytracker.ui.theme.Outline
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Success
import com.moneytracker.ui.theme.SurfaceVariant
import com.moneytracker.ui.theme.moneytrackerTheme

@Composable
fun ChangePinScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChangePinViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) onBack()
    }

    ChangePinScreenContent(
        uiState = uiState,
        onBack = onBack,
        onDigit = viewModel::onDigit,
        onDelete = viewModel::onDelete,
        modifier = modifier
    )
}

@Composable
fun ChangePinScreenContent(
    uiState: ChangePinUiState,
    onBack: () -> Unit,
    onDigit: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                Text(
                    "Change PIN",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    when (uiState.step) {
                        PinStep.CURRENT -> "Enter your current PIN"
                        PinStep.NEW -> "Enter your new PIN"
                        PinStep.CONFIRM -> "Confirm your new PIN"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Step indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(PinStep.CURRENT, PinStep.NEW, PinStep.CONFIRM).forEach { step ->
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
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            // Step title
            Text(
                text = when (uiState.step) {
                    PinStep.CURRENT -> "Current PIN"
                    PinStep.NEW -> "New PIN"
                    PinStep.CONFIRM -> "Confirm PIN"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = OnSurface,
                textAlign = TextAlign.Center
            )

            // PIN dots
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val currentPin = when (uiState.step) {
                    PinStep.CURRENT -> uiState.currentPin
                    PinStep.NEW -> uiState.newPin
                    PinStep.CONFIRM -> uiState.confirmPin
                }
                repeat(4) { i ->
                    val filled = i < currentPin.length
                    val isError = uiState.error != null
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isError -> Error
                                    filled -> Primary
                                    else -> SurfaceVariant
                                }
                            )
                            .then(
                                if (!filled) Modifier.border(
                                    2.dp,
                                    Outline,
                                    CircleShape
                                ) else Modifier
                            )
                    )
                }
            }

            // Error message
            AnimatedVisibility(
                visible = uiState.error != null,
                enter = fadeIn(tween(200)),
                exit = fadeOut(tween(200))
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = Error,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            // Numpad
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("", "0", "⌫")
                ).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        row.forEach { key ->
                            when (key) {
                                "" -> Spacer(Modifier.size(72.dp))
                                "⌫" -> Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .clickable { onDelete() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Backspace,
                                        null,
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                else -> Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .background(SurfaceVariant)
                                        .clickable { onDigit(key) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                }
                            }
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
                        Text("✅")
                        Text(
                            "PIN changed successfully!",
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
fun ChangePinScreenPreview() {
    moneytrackerTheme {
        ChangePinScreenContent(
            uiState = ChangePinUiState(
                step = PinStep.NEW,
                newPin = "12"
            ),
            onBack = {},
            onDigit = {},
            onDelete = {}
        )
    }
}
