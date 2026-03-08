package com.moneytracker.presentation.screen.dashboard.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
    onSeeAllClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val greeting = remember { getGreeting() }
    val userName = uiState.userName.ifBlank { "User" }

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // ── Header ──────────────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(PrimaryDark, Primary, PrimaryLight))
                        )
                        .statusBarsPadding()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 80.dp)
                ) {
                    Column {

                        // Greeting row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "$greeting 👋",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.75f)
                                )
                                Text(
                                    userName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        // Balance
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Total Balance",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f),
                                letterSpacing = 0.5.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            AnimatedBalanceText(uiState.totalBalance)
                        }

                        Spacer(Modifier.height(16.dp))

                        // Income / Expense tiles
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

            // ── Budget card ──────────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .offset(y = (-58).dp)
                        .padding(horizontal = 16.dp)
                ) {
                    BudgetProgressCard(
                        spent = uiState.totalExpense,
                        total = uiState.budgetLimit
                    )
                }
            }
            // ── "Recent Transactions" header — pulled up, zero gap ───────────────
            item {
                SectionHeader(
                    title = "Recent Transactions",
                    actionText = "See all",
                    onAction = onSeeAllClick,
                    modifier = Modifier
                        .offset(y = (-40).dp)
                        .padding(horizontal = 16.dp)
                )
            }

            // ── Transactions list ────────────────────────────────────────────────
            when {
                uiState.isLoading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-24).dp)
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary, strokeCap = StrokeCap.Round)
                    }
                }

                uiState.recentTransactions.isEmpty() -> item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-16).dp)
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🧾", fontSize = 32.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No transactions yet",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant
                        )
                        Text(
                            "Add one to start tracking",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }

                else -> items(
                    items = uiState.recentTransactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onClick = { onTransactionClick(transaction) },
                        modifier = Modifier
                            .offset(y = (-34).dp)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 6.dp)
                    )
                }
            }
        }
    }
}

// ── Animated balance ──────────────────────────────────────────────────────────

@Composable
private fun AnimatedBalanceText(balance: Double) {
    var triggered by remember { mutableStateOf(false) }
    val animated by animateFloatAsState(
        targetValue = if (triggered) balance.toFloat() else 0f,
        animationSpec = tween(800),
        label = "balance"
    )
    LaunchedEffect(balance) { triggered = true }
    Text(
        "₹${animated.toInt()}",
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White
    )
}

// ── BalanceTile ───────────────────────────────────────────────────────────────

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
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.18f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(prefix, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun getGreeting(): String = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
    in 0..11  -> "Good Morning"
    in 12..15 -> "Good Afternoon"
    in 16..20 -> "Good Evening"
    else      -> "Good Night"
}

// ── Preview ───────────────────────────────────────────────────────────────────

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
                        amount = 1200.0,
                        category = Category.FOOD,
                        type = TransactionType.EXPENSE,
                        date = Date()
                    ),
                    Transaction(
                        id = 2,
                        amount = 45000.0,
                        category = Category.OTHER,
                        type = TransactionType.INCOME,
                        date = Date()
                    ),
                    Transaction(
                        id = 3,
                        amount = 499.0,
                        category = Category.ENTERTAINMENT,
                        type = TransactionType.EXPENSE,
                        date = Date()
                    )
                )
            ),
            onSeeAllClick = {},
            onTransactionClick = {}
        )
    }
}