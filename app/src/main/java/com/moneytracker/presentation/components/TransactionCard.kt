package com.moneytracker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.moneytracker.domain.model.Transaction
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.domain.model.Category
import com.moneytracker.ui.theme.*

@Composable
fun TransactionCard(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val categoryColor = categoryColor(transaction.category)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        // Use Surface from your Theme for better M3 support
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(44.dp) // Increased slightly for better visual balance
                    .clip(RoundedCornerShape(12.dp))
                    .background(categoryColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = Category.iconResFor(transaction.category)),
                    contentDescription = transaction.category,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Title & Category
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium, // Better hierarchy than bodyMedium
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Prevents text overlap on small screens
                )
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = OnSurfaceVariant
                )
            }

            // Amount
            val isIncome = transaction.type == TransactionType.INCOME
            Text(
                text = formatAmount(transaction),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (isIncome) Success else Error
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────
fun formatAmount(transaction: Transaction): String {
    val prefix = if (transaction.type == TransactionType.INCOME) "+" else "-"
    // Cleanly formats the decimal if present (e.g., 50.0 -> 50)
    val amountText = if (transaction.amount % 1.0 == 0.0) {
        transaction.amount.toInt().toString()
    } else {
        "%.2f".format(transaction.amount)
    }
    return "$prefix ₹$amountText"
}