package com.moneytracker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moneytracker.domain.model.Category
import com.moneytracker.ui.theme.*

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
    val color = categoryColor(category)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(99.dp))
            .background(if (isSelected) color.copy(alpha = 0.15f) else SurfaceVariant)
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
                    painter = painterResource(id = Category.iconResFor(category)),
                    contentDescription = category,
                    modifier = Modifier.size(18.dp),
                )
            }
            if (showLabel) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                    color = if (isSelected) color else OnSurfaceVariant
                )
            }
        }
    }
}

// ── Grid Category Chip (for Add Transaction screen) ───────────────────────────
@Composable
fun CategoryChipGrid(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
                            .background(if (isSelected) color.copy(0.15f) else SurfaceVariant)
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
                                    painter = painterResource(id = Category.iconResFor(category)),
                                    contentDescription = category,
                                    modifier = Modifier.size(22.dp),
                                )
                            }
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) color else OnSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }
                // Fill empty slots in last row
                repeat(4 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ── Horizontal Scrollable Category Filter Row (for History screen) ────────────
@Composable
fun CategoryFilterRow(
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // "All" chip
        item {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(99.dp))
                    .background(if (selectedCategory == null) Primary.copy(0.15f) else SurfaceVariant)
                    .border(
                        2.dp,
                        if (selectedCategory == null) Primary else Color.Transparent,
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
                    color = if (selectedCategory == null) Primary else OnSurfaceVariant
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

