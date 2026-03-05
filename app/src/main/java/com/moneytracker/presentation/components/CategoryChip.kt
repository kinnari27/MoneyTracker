package com.moneytracker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moneytracker.domain.model.Category
import com.moneytracker.ui.theme.AppTheme

// ── Single Category Chip ──────────────────────────────────────────────────────
@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    showLabel: Boolean = true
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    val color = categoryColor(category)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(if (isSelected) color.copy(alpha = 0.15f) else cs.surfaceVariant)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(99.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (showIcon) {
                Image(
                    painter = painterResource(Category.iconResFor(category)),
                    contentDescription = category,
                    modifier = Modifier.size(18.dp),
                )
            }
            if (showLabel) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                    color = if (isSelected) color else colors.textSecondary
                )
            }
        }
    }
}

// ── Grid Category Chip ────────────────────────────────────────────────────────
@Composable
fun CategoryChipGrid(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Category.ALL.chunked(4).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { category ->
                    val isSelected = selectedCategory == category
                    val color = categoryColor(category)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) color.copy(0.15f) else cs.surfaceVariant)
                            .border(
                                2.dp,
                                if (isSelected) color else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onCategorySelect(category) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(Category.iconResFor(category)),
                                    contentDescription = category,
                                    modifier = Modifier.size(22.dp),
                                )
                            }
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) color else colors.textSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
                repeat(4 - row.size) { Spacer(modifier = Modifier.weight(1f)) }
            }
        }
    }
}

// ── Horizontal Scrollable Category Filter Row ─────────────────────────────────
@Composable
fun CategoryFilterRow(
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(99.dp))
                    .background(if (selectedCategory == null) cs.primary.copy(0.15f) else cs.surfaceVariant)
                    .border(
                        2.dp,
                        if (selectedCategory == null) cs.primary else Color.Transparent,
                        RoundedCornerShape(99.dp)
                    )
                    .clickable { onCategorySelect(null) }
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "All",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (selectedCategory == null) FontWeight.ExtraBold else FontWeight.Normal,
                    color = if (selectedCategory == null) cs.primary else colors.textSecondary
                )
            }
        }
        items(Category.ALL) { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelect(category) }
            )
        }
    }
}