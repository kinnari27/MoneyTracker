package com.moneytracker.presentation.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneytracker.domain.model.Category
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.presentation.components.TransactionCard
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.OnSurfaceVariant
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Success
import com.moneytracker.ui.theme.Surface
import com.moneytracker.ui.theme.SurfaceVariant
import com.moneytracker.ui.theme.moneytrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onFilterChange: (TransactionFilter) -> Unit,
    onSearchChange: (String) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val grouped = uiState.filteredTransactions.groupBy { transaction ->
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        sdf.format(transaction.date)
    }

    LazyColumn(
        modifier       = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                    .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Transactions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = Color.White)

                    // Search Bar
                    OutlinedTextField(
                        value         = uiState.searchQuery,
                        onValueChange = onSearchChange,
                        placeholder   = { Text("Search transactions...", color = Color.White.copy(0.6f)) },
                        leadingIcon   = { Icon(Icons.Default.Search, null, tint = Color.White.copy(0.7f)) },
                        singleLine    = true,
                        shape         = RoundedCornerShape(14.dp),
                        modifier      = Modifier.fillMaxWidth(),
                        colors        = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor     = Color.White.copy(0.3f),
                            focusedBorderColor       = Color.White,
                            unfocusedContainerColor  = Color.White.copy(0.15f),
                            focusedContainerColor    = Color.White.copy(0.2f),
                            unfocusedTextColor       = Color.White,
                            focusedTextColor         = Color.White,
                            cursorColor              = Color.White
                        )
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Summary Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(
                        Triple("Income",  "₹${uiState.totalIncome.toInt()}",  Success),
                        Triple("Expense", "₹${uiState.totalExpense.toInt()}", Error),
                        Triple("Net",     "₹${(uiState.totalIncome - uiState.totalExpense).toInt()}", Primary)
                    ).forEach { (label, value, color) ->
                        Card(
                            modifier  = Modifier.weight(1f),
                            shape     = RoundedCornerShape(16.dp),
                            colors    = CardDefaults.cardColors(containerColor = Surface),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = color)
                                Text(label, style = MaterialTheme.typography.labelSmall, color = OnSurfaceVariant)
                            }
                        }
                    }
                }

                // Filter Chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TransactionFilter.values().forEach { f ->
                        FilterChip(
                            selected = uiState.filter == f,
                            onClick  = { onFilterChange(f) },
                            label    = { Text(f.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            colors   = FilterChipDefaults.filterChipColors(
                                selectedContainerColor  = Primary,
                                selectedLabelColor      = Color.White,
                                containerColor          = Surface
                            )
                        )
                    }
                }
            }
        }

        if (uiState.isLoading) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(32.dp), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }
        } else if (grouped.isEmpty()) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(40.dp), contentAlignment = Alignment.Center
                ) {
                    Text("No transactions found", color = OnSurfaceVariant)
                }
            }
        } else {
            grouped.forEach { (date, transactions) ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(date, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                        Surface(shape = RoundedCornerShape(99.dp), color = SurfaceVariant) {
                            val dayNet = transactions.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
                            Text(
                                text     = "${if (dayNet >= 0) "+" else ""}₹${Math.abs(dayNet).toInt()}",
                                style    = MaterialTheme.typography.labelSmall,
                                color    = OnSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                items(transactions) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    moneytrackerTheme {
        HistoryScreen(
            uiState = HistoryUiState(
                isLoading = false,
                totalIncome = 5000.0,
                totalExpense = 2000.0,
                filteredTransactions = listOf(
                    Transaction(id = 1, title = "Lunch", amount = 500.0, category = Category.FOOD, type = TransactionType.EXPENSE, date = Date()),
                    Transaction(id = 2, title = "Freelance", amount = 5000.0, category = Category.OTHER, type = TransactionType.INCOME, date = Date()),
                    Transaction(id = 3, title = "Uber", amount = 300.0, category = Category.TRANSPORT, type = TransactionType.EXPENSE, date = Date())
                )
            ),
            onFilterChange = {},
            onSearchChange = {},
            onDeleteTransaction = {}
        )
    }
}
