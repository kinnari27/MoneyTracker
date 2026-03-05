package com.moneytracker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.moneytracker.ui.theme.CategoryBills
import com.moneytracker.ui.theme.CategoryEducation
import com.moneytracker.ui.theme.CategoryEntertainment
import com.moneytracker.ui.theme.CategoryFood
import com.moneytracker.ui.theme.CategoryHealth
import com.moneytracker.ui.theme.CategoryOther
import com.moneytracker.ui.theme.CategoryShopping
import com.moneytracker.ui.theme.CategoryTransport
import com.moneytracker.ui.theme.OnSurface
import com.moneytracker.ui.theme.Primary

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = OnSurface
        )
        if (actionText != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

fun categoryColor(category: String): Color = when (category) {
    "Food" -> CategoryFood
    "Transport" -> CategoryTransport
    "Shopping" -> CategoryShopping
    "Health" -> CategoryHealth
    "Entertainment" -> CategoryEntertainment
    "Bills" -> CategoryBills
    "Education" -> CategoryEducation
    else -> CategoryOther
}
