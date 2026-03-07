package com.moneytracker.presentation.screen.settings.editProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.presentation.screen.settings.currency.CurrencyItem
import com.moneytracker.presentation.screen.settings.currency.CurrencyUiState
import com.moneytracker.presentation.screen.settings.currency.CurrencyViewModel
import com.moneytracker.ui.theme.OnSurface
import com.moneytracker.ui.theme.OnSurfaceVariant
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Surface
import com.moneytracker.ui.theme.moneytrackerTheme

@Composable
fun CurrencyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CurrencyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CurrencyScreenContent(
        uiState = uiState,
        onBack = onBack,
        onSearchChange = viewModel::onSearchChange,
        onCurrencySelect = viewModel::onCurrencySelect,
        modifier = modifier
    )
}

@Composable
fun CurrencyScreenContent(
    uiState: CurrencyUiState,
    onBack: () -> Unit,
    onSearchChange: (String) -> Unit,
    onCurrencySelect: (CurrencyItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        // ── Header ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                .padding(top = 52.dp, start = 4.dp, end = 20.dp, bottom = 28.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, null, tint = Color.White)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Currency",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    "Select your preferred currency",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(16.dp))

                // Search
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search currency…", color = Color.White.copy(0.5f)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            null,
                            tint = Color.White.copy(0.7f)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.White.copy(0.3f),
                        focusedBorderColor = Color.White,
                        unfocusedContainerColor = Color.White.copy(0.12f),
                        focusedContainerColor = Color.White.copy(0.18f),
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White
                    )
                )
            }
        }

        // ── Current selection banner ──────────────────────────────────────────
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Primary.copy(0.08f)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    uiState.selectedCurrency.flag,
                    fontSize = 32.sp
                )
                Column(Modifier.weight(1f)) {
                    Text(
                        "Current Currency",
                        style = MaterialTheme.typography.labelSmall,
                        color = Primary
                    )
                    Text(
                        "${uiState.selectedCurrency.code} — ${uiState.selectedCurrency.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface
                    )
                }
                Text(
                    uiState.selectedCurrency.symbol,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Primary
                )
            }
        }

        // ── Currency List ─────────────────────────────────────────────────────
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.filteredCurrencies, key = { it.code }) { currency ->
                val isSelected = currency.code == uiState.selectedCurrency.code
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (isSelected) Modifier.border(
                                2.dp,
                                Primary,
                                RoundedCornerShape(16.dp)
                            ) else Modifier
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Primary.copy(
                            0.06f
                        ) else Surface
                    ),
                    elevation = CardDefaults.cardElevation(if (isSelected) 0.dp else 1.dp),
                    onClick = { onCurrencySelect(currency) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(currency.flag, fontSize = 28.sp)
                        Column(Modifier.weight(1f)) {
                            Text(
                                currency.code,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSelected) Primary else OnSurface
                            )
                            Text(
                                currency.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceVariant
                            )
                        }
                        Text(
                            currency.symbol,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Primary else OnSurfaceVariant
                        )
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyScreenPreview() {
    moneytrackerTheme {
        CurrencyScreenContent(
            uiState = CurrencyUiState(),
            onBack = {},
            onSearchChange = {},
            onCurrencySelect = {}
        )
    }
}
