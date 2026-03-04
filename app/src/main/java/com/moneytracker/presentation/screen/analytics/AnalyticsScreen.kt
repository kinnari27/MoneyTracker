package com.moneytracker.presentation.screen.analytics

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneytracker.R
import com.moneytracker.domain.model.Category
import com.moneytracker.domain.model.InvestmentCategory
import com.moneytracker.presentation.components.categoryColor
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.MoneyTrackerTheme
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Success
import com.moneytracker.ui.theme.Warning

// Investment category colour — amber/gold tone
private fun investmentColor(category: String): Color = Warning

@Composable
fun AnalyticsScreen(
    uiState: AnalyticsUiState,
    onPeriodChange: (AnalyticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    AppTheme.colors

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {

        // ── Header ─────────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PrimaryDark, cs.primary)))
                    .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 28.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Text(
                            "Overview",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(0.7f)
                        )
                        Text(
                            "Analytics",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    // Period selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(0.15f))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        AnalyticsPeriod.values().forEach { period ->
                            val isSelected = uiState.period == period
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color.White else Color.Transparent)
                                    .clickable { onPeriodChange(period) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = period.name.lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    color = if (isSelected) cs.primary else Color.White.copy(0.8f),
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Top summary: Income / Spent / Invested / Net ───────────────────────
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Row 1: Income + Spent
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        iconRes = R.drawable.ic_stat_saved,
                        label = "Total Income",
                        value = "₹${uiState.totalIncome.toInt()}",
                        color = Success,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        iconRes = R.drawable.ic_stat_spent,
                        label = "Total Spent",
                        value = "₹${uiState.totalSpent.toInt()}",
                        color = Error,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: Invested + Net savings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        iconRes = R.drawable.ic_star,
                        label = "Invested",
                        value = "₹${uiState.totalInvested.toInt()}",
                        color = Warning,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        iconRes = R.drawable.ic_stat_average,
                        label = "Net Savings",
                        value = "₹${uiState.netSavings.toInt()}",
                        color = if (uiState.netSavings >= 0) Success else Error,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: Daily avg + Largest expense
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        iconRes = R.drawable.ic_stat_average,
                        label = "Daily Average",
                        value = "₹${uiState.dailyAverage.toInt()}",
                        color = cs.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        iconRes = R.drawable.ic_stat_largest,
                        label = "Biggest Spend",
                        value = "₹${uiState.largestExpense?.amount?.toInt() ?: 0}",
                        color = Warning,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // ── Money Flow bar (Income → Expense → Investment → Savings) ──────────
        item {
            if (uiState.totalIncome > 0) {
                MoneyFlowCard(uiState = uiState)
            }
        }

        // ── Expense breakdown by category ──────────────────────────────────────
        item {
            if (uiState.categoryTotals.isNotEmpty()) {
                CategoryBreakdownCard(
                    title = "Spending by Category",
                    periodLabel = periodLabel(uiState.period),
                    totals = uiState.categoryTotals,
                    iconRes = { Category.iconResFor(it) },
                    barColor = { categoryColor(it) }
                )
            }
        }

        // ── Investment breakdown by category ───────────────────────────────────
        item {
            if (uiState.investmentBreakdown.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                CategoryBreakdownCard(
                    title = "Investments by Type",
                    periodLabel = periodLabel(uiState.period),
                    totals = uiState.investmentBreakdown,
                    iconRes = { InvestmentCategory.iconResFor(it) },
                    barColor = { investmentColor(it) }
                )
            }
        }

        // ── Largest expense detail card ────────────────────────────────────────
        item {
            uiState.largestExpense?.let { tx ->
                Spacer(Modifier.height(4.dp))
                LargestExpenseCard(tx = tx)
            }
        }
    }
}

// ── Money Flow Card ────────────────────────────────────────────────────────────
@Composable
private fun MoneyFlowCard(uiState: AnalyticsUiState) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    val total = uiState.totalIncome.coerceAtLeast(1.0)

    val expenseFraction = (uiState.totalSpent / total).toFloat().coerceIn(0f, 1f)
    val investFraction = (uiState.totalInvested / total).toFloat().coerceIn(0f, 1f)
    val savingsFraction = (uiState.netSavings.coerceAtLeast(0.0) / total).toFloat().coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surface,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (colors.isDark) 0.dp else 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Money Flow",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = cs.onSurface
            )
            Text(
                "How your income is distributed",
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )

            Spacer(Modifier.height(16.dp))

            // Stacked bar: expense | investment | savings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(99.dp))
            ) {
                if (expenseFraction > 0f)
                    Box(
                        Modifier
                            .weight(expenseFraction)
                            .fillMaxHeight()
                            .background(Error)
                    )
                if (investFraction > 0f)
                    Box(
                        Modifier
                            .weight(investFraction)
                            .fillMaxHeight()
                            .background(Warning)
                    )
                val remaining = (1f - expenseFraction - investFraction).coerceAtLeast(0.01f)
                Box(
                    Modifier
                        .weight(remaining)
                        .fillMaxHeight()
                        .background(Success.copy(alpha = 0.5f))
                )
            }

            Spacer(Modifier.height(14.dp))

            // Legend row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FlowLegendItem("Spent", "${(expenseFraction * 100).toInt()}%", Error)
                FlowLegendItem("Invested", "${(investFraction * 100).toInt()}%", Warning)
                FlowLegendItem("Saved", "${(savingsFraction * 100).toInt()}%", Success)
            }
        }
    }
}

@Composable
private fun RowScope.FlowLegendItem(label: String, pct: String, color: Color) {
    MaterialTheme.colorScheme
    val colors = AppTheme.colors
    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(color)
        )
        Column {
            Text(
                pct,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(label, style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
        }
    }
}

// ── Category Breakdown Card (reused for both Expense and Investment) ───────────
@Composable
private fun CategoryBreakdownCard(
    title: String,
    periodLabel: String,
    totals: Map<String, Double>,
    iconRes: (String) -> Int,
    barColor: (String) -> Color
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    val total = totals.values.sum()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surface,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (colors.isDark) 0.dp else 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = cs.onSurface
            )
            Text(
                periodLabel,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )

            Spacer(Modifier.height(16.dp))

            // Stacked bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(99.dp))
            ) {
                totals.entries.take(6).forEach { (cat, amount) ->
                    val w =
                        if (total > 0) (amount / total).toFloat().coerceAtLeast(0.01f) else 0.01f
                    Box(
                        modifier = Modifier
                            .weight(w)
                            .fillMaxHeight()
                            .background(barColor(cat))
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Per-category rows with animated progress bars
            totals.entries.take(6).forEach { (cat, amount) ->
                val pct = if (total > 0) (amount / total * 100).toInt() else 0

                var played by remember { mutableStateOf(false) }
                val animPct by animateFloatAsState(
                    targetValue = if (played) pct / 100f else 0f,
                    animationSpec = tween(800),
                    label = "bar_$cat"
                )
                LaunchedEffect(Unit) { played = true }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(barColor(cat).copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(iconRes(cat)),
                            contentDescription = cat,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                cat,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = cs.onSurface
                            )
                            Text(
                                "₹${amount.toInt()}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = cs.onSurface
                            )
                        }
                        Spacer(Modifier.height(5.dp))
                        LinearProgressIndicator(
                            progress = { animPct },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(99.dp)),
                            color = barColor(cat),
                            trackColor = cs.surfaceVariant
                        )
                    }
                    Text(
                        "$pct%",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ── Largest Expense Card ───────────────────────────────────────────────────────
@Composable
private fun LargestExpenseCard(tx: com.moneytracker.domain.model.Transaction) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.warningContainer,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Warning.copy(alpha = 0.20f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_stat_largest),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    tint = Warning
                )
            }
            Column(Modifier.weight(1f)) {
                Text(
                    "Biggest Spend",
                    style = MaterialTheme.typography.labelSmall,
                    color = Warning,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    tx.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface
                )
                Text(
                    tx.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary
                )
            }
            Text(
                "₹${tx.amount.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Warning
            )
        }
    }
}

// ── Stat Card ─────────────────────────────────────────────────────────────────
@Composable
private fun StatCard(
    iconRes: Int,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surface,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (colors.isDark) 0.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textSecondary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ── Helpers ────────────────────────────────────────────────────────────────────
private fun periodLabel(period: AnalyticsPeriod) = when (period) {
    AnalyticsPeriod.WEEKLY -> "This week"
    AnalyticsPeriod.MONTHLY -> "This month"
    AnalyticsPeriod.YEARLY -> "This year"
}

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Analytics — Light", showBackground = true)
@Composable
private fun AnalyticsPreviewLight() {
    MoneyTrackerTheme(darkTheme = false) {
        AnalyticsScreen(
            uiState = AnalyticsUiState(
                isLoading = false,
                totalSpent = 32000.0,
                dailyAverage = 1066.0,
                categoryTotals = mapOf(
                    "Food" to 12000.0,
                    "Transport" to 8000.0,
                    "Shopping" to 7000.0,
                    "Bills" to 5000.0
                ),
                totalIncome = 80000.0,
                netSavings = 28000.0,
                totalInvested = 20000.0,
                investmentBreakdown = mapOf("Fixed Deposit" to 12000.0, "Mutual Fund" to 8000.0),
            ),
            onPeriodChange = {}
        )
    }
}

@Preview(name = "Analytics — Dark", showBackground = true)
@Composable
private fun AnalyticsPreviewDark() {
    MoneyTrackerTheme(darkTheme = true) {
        AnalyticsScreen(
            uiState = AnalyticsUiState(
                isLoading = false,
                totalSpent = 32000.0,
                dailyAverage = 1066.0,
                categoryTotals = mapOf(
                    "Food" to 12000.0,
                    "Transport" to 8000.0,
                    "Shopping" to 7000.0
                ),
                totalIncome = 80000.0,
                netSavings = 28000.0,
                totalInvested = 20000.0,
                investmentBreakdown = mapOf(
                    "Fixed Deposit" to 12000.0,
                    "LIC / Insurance" to 8000.0
                ),
            ),
            onPeriodChange = {}
        )
    }
}