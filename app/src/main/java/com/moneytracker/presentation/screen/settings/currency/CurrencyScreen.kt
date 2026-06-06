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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.presentation.screen.settings.currency.CurrencyItem
import com.moneytracker.presentation.screen.settings.currency.CurrencyUiState
import com.moneytracker.presentation.screen.settings.currency.CurrencyViewModel
import com.moneytracker.ui.theme.Indigo100
import com.moneytracker.ui.theme.Indigo600
import com.moneytracker.ui.theme.Slate400
import com.moneytracker.ui.theme.Slate900
import com.moneytracker.ui.theme.White
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
    val cs = MaterialTheme.colorScheme

    Column(modifier = modifier
        .fillMaxSize()
        .background(cs.background)) {

        // ── Header — flat Slate900 ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Slate900)
                .padding(top = 52.dp, start = 4.dp, end = 16.dp, bottom = 20.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, null, tint = White)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Currency",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    "Select your preferred currency",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate400,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))

                // Search field — styled for dark header
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search currency…", color = Slate400) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            null,
                            tint = Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Slate400.copy(alpha = 0.3f),
                        focusedBorderColor = Indigo600,
                        unfocusedContainerColor = White.copy(alpha = 0.07f),
                        focusedContainerColor = White.copy(alpha = 0.10f),
                        unfocusedTextColor = White,
                        focusedTextColor = White,
                        cursorColor = Indigo600
                    )
                )
            }
        }

        // ── Current selection banner ──────────────────────────────────────────
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Indigo100),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(uiState.selectedCurrency.flag, fontSize = 28.sp)
                Column(Modifier.weight(1f)) {
                    Text(
                        "Current Currency",
                        style = MaterialTheme.typography.labelSmall,
                        color = Indigo600
                    )
                    Text(
                        "${uiState.selectedCurrency.code} — ${uiState.selectedCurrency.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = cs.onSurface
                    )
                }
                Text(
                    uiState.selectedCurrency.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Indigo600
                )
            }
        }

        // ── Currency list ─────────────────────────────────────────────────────
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
                                1.5.dp,
                                Indigo600,
                                RoundedCornerShape(12.dp)
                            )
                            else Modifier
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Indigo100 else cs.surface
                    ),
                    elevation = CardDefaults.cardElevation(0.dp),
                    onClick = { onCurrencySelect(currency) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(currency.flag, fontSize = 24.sp)
                        Column(Modifier.weight(1f)) {
                            Text(
                                currency.code,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Indigo600 else cs.onSurface
                            )
                            Text(
                                currency.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = cs.onSurfaceVariant
                            )
                        }
                        Text(
                            currency.symbol,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Indigo600 else cs.onSurfaceVariant
                        )
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check, null,
                                tint = Indigo600,
                                modifier = Modifier.size(18.dp)
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