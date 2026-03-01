package com.moneytracker.presentation.screen.analytics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moneytracker.R
import com.moneytracker.domain.model.Category
import com.moneytracker.presentation.components.categoryColor
import com.moneytracker.ui.theme.OnSurface
import com.moneytracker.ui.theme.OnSurfaceVariant
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Surface
import com.moneytracker.ui.theme.SurfaceVariant
import com.moneytracker.ui.theme.moneytrackerTheme

data class StatItem(
    val iconRes: Int,
    val label: String,
    val value: String
)

@Composable
fun AnalyticsScreen(
    uiState: AnalyticsUiState,
    onPeriodChange: (AnalyticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    AnalyticsScreenContent(
        uiState = uiState,
        onPeriodChange = onPeriodChange,
        modifier = modifier
    )
}

@Composable
fun AnalyticsScreenContent(
    uiState: AnalyticsUiState,
    onPeriodChange: (AnalyticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                    .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    // Period Toggle
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
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = period.name.lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    color = if (isSelected) Primary else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Stats Grid - Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(
                        StatItem(
                            R.drawable.ic_graph1,
                            "Total Spent",
                            "₹${uiState.totalSpent.toInt()}"
                        ),
                        StatItem(
                            R.drawable.ic_graph2,
                            "Total Saved",
                            "₹${uiState.totalSaved.toInt()}"
                        ),
                    ).forEach { item ->
                        StatCard(
                            iconRes = item.iconRes,
                            label = item.label,
                            value = item.value,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Stats Grid - Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(
                        StatItem(
                            R.drawable.ic_graph3,
                            "Daily Average",
                            "₹${uiState.dailyAverage.toInt()}"
                        ),
                        StatItem(
                            R.drawable.ic_shopping,
                            "Largest",
                            "₹${uiState.largestExpense?.amount?.toInt() ?: 0}"
                        ),
                    ).forEach { item ->
                        StatCard(
                            iconRes = item.iconRes,
                            label = item.label,
                            value = item.value,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Category Breakdown
                if (uiState.categoryTotals.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Spending by Category",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnSurface
                            )
                            Text(
                                "Current Period Breakdown",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceVariant
                            )

                            Spacer(Modifier.height(16.dp))

                            val total = uiState.categoryTotals.values.sum()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(99.dp))
                                    .background(SurfaceVariant)
                            ) {
                                uiState.categoryTotals.entries.take(6).forEach { (cat, amount) ->
                                    val weight = if (total > 0) (amount / total).toFloat() else 0f
                                    if (weight > 0) {
                                        Box(
                                            modifier = Modifier
                                                .weight(weight)
                                                .fillMaxHeight()
                                                .background(categoryColor(cat))
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(20.dp))

                            uiState.categoryTotals.entries.take(6).forEach { (cat, amount) ->
                                val pct = if (total > 0) (amount / total * 100).toInt() else 0
                                CategoryProgressRow(cat, amount.toInt(), pct)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryProgressRow(category: String, amount: Int, percentage: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(categoryColor(category).copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = Category.iconResFor(category)),
                contentDescription = category,
                modifier = Modifier.size(22.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    category,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                Text(
                    "₹$amount",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = OnSurface
                )
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(99.dp)),
                color = categoryColor(category),
                trackColor = SurfaceVariant,
                strokeCap = StrokeCap.Round
            )
        }
        Text(
            "$percentage%",
            style = MaterialTheme.typography.labelMedium,
            color = OnSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatCard(
    iconRes: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceVariant.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = OnSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    moneytrackerTheme {
        AnalyticsScreenContent(
            uiState = AnalyticsUiState(
                isLoading = false,
                totalSpent = 24500.0,
                totalSaved = 15000.0,
                dailyAverage = 800.0,
                categoryTotals = mapOf(
                    Category.FOOD to 5000.0,
                    Category.SHOPPING to 8000.0,
                    Category.TRANSPORT to 2000.0,
                    Category.BILLS to 6000.0
                )
            ),
            onPeriodChange = {}
        )
    }
}
