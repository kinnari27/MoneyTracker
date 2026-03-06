package com.moneytracker.presentation.screen.dashboard.home

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneytracker.domain.model.Category
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.presentation.components.BudgetProgressCard
import com.moneytracker.presentation.components.SectionHeader
import com.moneytracker.presentation.components.TransactionCard
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.OnSurfaceVariant
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.PrimaryLight
import com.moneytracker.ui.theme.Success
import com.moneytracker.ui.theme.moneytrackerTheme
import java.util.Calendar
import java.util.Date

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAddClick: () -> Unit,
    onSeeAllClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val greeting = remember { getGreeting() }
    val userName = uiState.userName.ifBlank { "User" }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(PrimaryDark, Primary, PrimaryLight)
                        )
                    )
                    .statusBarsPadding()
                    .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 80.dp)
            ) {
                Column {
                    // Greeting
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "$greeting 👋",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                userName,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                uiState.userInitial,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Balance
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Total Balance",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            "₹${uiState.totalBalance.toInt()}",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Income / Expense Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BalanceTile(
                            label = "Income",
                            value = "₹${uiState.totalIncome.toInt()}",
                            color = Success,
                            prefix = "↑",
                            modifier = Modifier.weight(1f)
                        )
                        BalanceTile(
                            label = "Expense",
                            value = "₹${uiState.totalExpense.toInt()}",
                            color = Error,
                            prefix = "↓",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // ── Pull-up Cards ─────────────────────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .offset(y = (-36).dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BudgetProgressCard(
                    spent = uiState.totalExpense,
                    total = uiState.budgetLimit
                )
                SectionHeader(
                    title = "Recent Transactions",
                    actionText = "See all",
                    onAction = onSeeAllClick
                )
            }
        }

        // ── Transactions List ─────────────────────────────────────────────────
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Primary) }
            }
        } else if (uiState.recentTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions yet", color = OnSurfaceVariant)
                }
            }
        } else {
            items(uiState.recentTransactions) { transaction ->
                TransactionCard(
                    transaction = transaction,
                    onClick = { onTransactionClick(transaction) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

private fun getGreeting(): String {
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..15 -> "Good Afternoon"
        in 16..20 -> "Good Evening"
        else -> "Good Night"
    }
}

@Composable
private fun BalanceTile(
    label: String,
    value: String,
    color: Color,
    prefix: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        prefix,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    moneytrackerTheme {
        HomeScreen(
            uiState = HomeUiState(
                isLoading = false,
                userName = "Rahul Sharma",
                userInitial = "R",
                totalBalance = 24500.0,
                totalIncome = 45000.0,
                totalExpense = 20500.0,
                recentTransactions = listOf(
                    Transaction(
                        id = 1,
                        title = "Groceries",
                        amount = 1200.0,
                        category = Category.FOOD,
                        type = TransactionType.EXPENSE,
                        date = Date()
                    ),
                    Transaction(
                        id = 2,
                        title = "Salary",
                        amount = 45000.0,
                        category = Category.OTHER,
                        type = TransactionType.INCOME,
                        date = Date()
                    ),
                    Transaction(
                        id = 3,
                        title = "Netflix Subscription",
                        amount = 499.0,
                        category = Category.ENTERTAINMENT,
                        type = TransactionType.EXPENSE,
                        date = Date()
                    )
                )
            ),
            onAddClick = {},
            onSeeAllClick = {},
            onTransactionClick = {}
        )
    }
}
