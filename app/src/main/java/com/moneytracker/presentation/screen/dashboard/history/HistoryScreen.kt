package com.moneytracker.presentation.screen.dashboard.history

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneytracker.domain.model.Category
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.presentation.components.TransactionCard
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.moneytrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ─────────────────────────────────────────────────────────────────────────────
// HistoryScreen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onFilterChange: (TransactionFilter) -> Unit,
    onSearchChange: (String) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val grouped = uiState.filteredTransactions.groupBy { transaction ->
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(transaction.date)
    }
    val colorScheme = MaterialTheme.colorScheme
    val extendedColors = AppTheme.colors

    // ── Delete confirmation dialog ───────────────────────────────────────────
    if (uiState.transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = onDismissDelete,
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction?") },
            confirmButton = {
                Button(
                    onClick = onConfirmDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = extendedColors.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = onDismissDelete) { Text("Cancel") }
            }
        )
    }

    // ── Main list ────────────────────────────────────────────────────────────
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {

        // ── Gradient header + search bar ─────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                colorScheme.primary,
                                colorScheme.primary
                            )
                        )
                    )
                    .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                "Search transactions...",
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedBorderColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                            focusedContainerColor = Color.White.copy(alpha = 0.2f),
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            cursorColor = Color.White
                        )
                    )
                }
            }
        }

        // ── Summary cards + filter chips ─────────────────────────────────────
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 4-column summary row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Triple("Income", "₹${uiState.totalIncome.toInt()}", extendedColors.success),
                        Triple("Expense", "₹${uiState.totalExpense.toInt()}", extendedColors.error),
                        Triple(
                            "Invest",
                            "₹${uiState.totalInvestment.toInt()}",
                            extendedColors.accent
                        ),
                        Triple(
                            "Net",
                            "₹${(uiState.totalIncome - uiState.totalExpense).toInt()}",
                            colorScheme.primary
                        )
                    ).forEach { (label, value, color) ->
                        SummaryCard(
                            label = label,
                            value = value,
                            color = color,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Scrollable filter chips — labels never clip or wrap
                ScrollableFilterChips(
                    selectedFilter = uiState.filter,
                    onFilterChange = onFilterChange
                )
            }
        }

        // ── Loading / empty / list ────────────────────────────────────────────
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorScheme.primary)
                }
            }
        } else if (grouped.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions found", color = colorScheme.onSurfaceVariant)
                }
            }
        } else {
            grouped.forEach { (dateString, transactions) ->

                // Date header row
                item {
                    val date = runCatching {
                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).parse(dateString)
                    }.getOrNull()
                    val dayName =
                        date?.let {
                            SimpleDateFormat("EEEE", Locale.getDefault()).format(it)
                        } ?: ""

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = dateString,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = dayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }

                        // Day net badge
                        Surface(
                            shape = RoundedCornerShape(99.dp),
                            color = colorScheme.surfaceVariant
                        ) {
                            val dayNet = transactions.sumOf {
                                when (it.type) {
                                    TransactionType.INCOME -> it.amount
                                    TransactionType.EXPENSE -> -it.amount
                                    TransactionType.INVESTMENT -> 0.0
                                }
                            }
                            Text(
                                text = "${if (dayNet >= 0) "+" else ""}₹${
                                    Math.abs(dayNet).toInt()
                                }",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (dayNet >= 0) extendedColors.success else extendedColors.error,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }
                }

                // Transaction cards for this date
                items(transactions) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onClick = { onEditTransaction(transaction) },
                        onLongClick = { onDeleteTransaction(transaction) },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SummaryCard — compact card for Income / Expense / Investment / Net
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ScrollableFilterChips — horizontally scrollable row; chips never clip/wrap
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ScrollableFilterChips(
    selectedFilter: TransactionFilter,
    onFilterChange: (TransactionFilter) -> Unit,
    scrollState: ScrollState = rememberScrollState()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TransactionFilter.values().forEach { f ->
            val label = when (f) {
                TransactionFilter.ALL -> "All"
                TransactionFilter.INCOME -> "Income"
                TransactionFilter.EXPENSE -> "Expense"
                TransactionFilter.INVESTMENT -> "Investment"
            }
            FilterChip(
                selected = selectedFilter == f,
                onClick = { onFilterChange(f) },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "History — with data")
@Composable
fun HistoryScreenPreview() {
    moneytrackerTheme {
        HistoryScreen(
            uiState = HistoryUiState(
                isLoading = false,
                totalIncome = 50000.0,
                totalExpense = 12500.0,
                totalInvestment = 10000.0,
                filteredTransactions = listOf(
                    Transaction(
                        id = 1,
                        amount = 500.0,
                        category = Category.FOOD,
                        type = TransactionType.EXPENSE,
                        date = Date()
                    ),
                    Transaction(
                        id = 2,
                        amount = 50000.0,
                        category = Category.OTHER,
                        type = TransactionType.INCOME,
                        date = Date()
                    ),
                    Transaction(
                        id = 3,
                        amount = 10000.0,
                        category = Category.OTHER,
                        type = TransactionType.INVESTMENT,
                        date = Date()
                    )
                )
            ),
            onFilterChange = {},
            onSearchChange = {},
            onDeleteTransaction = {},
            onConfirmDelete = {},
            onDismissDelete = {},
            onEditTransaction = {}
        )
    }
}

@Preview(showBackground = true, name = "History — loading")
@Composable
fun HistoryScreenLoadingPreview() {
    moneytrackerTheme {
        HistoryScreen(
            uiState = HistoryUiState(isLoading = true),
            onFilterChange = {},
            onSearchChange = {},
            onDeleteTransaction = {},
            onConfirmDelete = {},
            onDismissDelete = {},
            onEditTransaction = {}
        )
    }
}

@Preview(showBackground = true, name = "History — empty")
@Composable
fun HistoryScreenEmptyPreview() {
    moneytrackerTheme {
        HistoryScreen(
            uiState = HistoryUiState(isLoading = false),
            onFilterChange = {},
            onSearchChange = {},
            onDeleteTransaction = {},
            onConfirmDelete = {},
            onDismissDelete = {},
            onEditTransaction = {}
        )
    }
}

@Preview(showBackground = true, name = "History — expense filter active")
@Composable
fun HistoryScreenFilteredPreview() {
    moneytrackerTheme {
        HistoryScreen(
            uiState = HistoryUiState(
                isLoading = false,
                filter = TransactionFilter.EXPENSE,
                totalIncome = 50000.0,
                totalExpense = 12500.0,
                totalInvestment = 10000.0,
                filteredTransactions = listOf(
                    Transaction(
                        id = 1,
                        amount = 2300.0,
                        category = Category.FOOD,
                        type = TransactionType.EXPENSE,
                        date = Date()
                    ),
                    Transaction(
                        id = 4,
                        amount = 1800.0,
                        category = Category.OTHER,
                        type = TransactionType.EXPENSE,
                        date = Date()
                    )
                )
            ),
            onFilterChange = {},
            onSearchChange = {},
            onDeleteTransaction = {},
            onConfirmDelete = {},
            onDismissDelete = {},
            onEditTransaction = {}
        )
    }
}