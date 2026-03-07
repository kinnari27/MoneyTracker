package com.moneytracker.presentation.screen.transaction.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneytracker.domain.model.Category
import com.moneytracker.domain.model.IncomeCategory
import com.moneytracker.domain.model.InvestmentCategory
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.presentation.components.categoryColor
import com.moneytracker.ui.theme.AppColors
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.MoneyTrackerTheme
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Success
import com.moneytracker.ui.theme.Warning

// ── Colour per type ───────────────────────────────────────────────────────────
private val TransactionType.accentColor: Color
    @Composable get() = when (this) {
        TransactionType.INCOME -> Success
        TransactionType.EXPENSE -> Error
        TransactionType.INVESTMENT -> Warning
    }

private val TransactionType.label: String
    get() = when (this) {
        TransactionType.INCOME -> "Income"
        TransactionType.EXPENSE -> "Expense"
        TransactionType.INVESTMENT -> "Invest"
    }

private val TransactionType.arrow: String
    get() = when (this) {
        TransactionType.INCOME -> "↑"
        TransactionType.EXPENSE -> "↓"
        TransactionType.INVESTMENT -> "⇌"
    }

// ── Entry point ───────────────────────────────────────────────────────────────
@Composable
fun AddTransactionScreen(
    uiState: AddTransactionUiState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onNoteChange: (String) -> Unit,
    onRecurringToggle: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaveSuccess()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Header ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryDark, cs.primary)))
                .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 36.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            if (uiState.isEditMode) "Edit Entry" else "New Entry",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(0.7f)
                        )
                        Text(
                            if (uiState.isEditMode) "Update Transaction" else "Add Transaction",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    if (uiState.isEditMode) {
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(0.15f))
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // ── 3-way type toggle ─────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(0.15f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TransactionType.values().forEach { type ->
                        val isSelected = uiState.type == type
                        val typeColor = type.accentColor
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { onTypeChange(type) }
                                .padding(vertical = 11.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = type.arrow,
                                    color = if (isSelected) typeColor else Color.White.copy(0.8f),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = type.label,
                                    color = if (isSelected) typeColor else Color.White.copy(0.8f),
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // ── Amount input ──────────────────────────────────────────────
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Enter Amount",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(0.6f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "₹",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White.copy(0.5f)
                        )
                        BasicTextField(
                            value = uiState.amount,
                            onValueChange = onAmountChange,
                            textStyle = MaterialTheme.typography.displaySmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.widthIn(min = 80.dp, max = 240.dp),
                            decorationBox = { inner ->
                                if (uiState.amount.isEmpty()) {
                                    Text(
                                        "0",
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            color = Color.White.copy(0.35f),
                                            fontWeight = FontWeight.ExtraBold,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                                inner()
                            }
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(0.35f))
                    )

                    // Investment: show maturity hint
                    if (uiState.type == TransactionType.INVESTMENT) {
                        Spacer(Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(99.dp))
                                .background(Color.White.copy(0.12f))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "Amount will be tracked as investment, not spending",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // ── Form ──────────────────────────────────────────────────────────────
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Title
            FormCard {
                FormLabel("Title")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    placeholder = {
                        Text(
                            when (uiState.type) {
                                TransactionType.INCOME -> "e.g. Monthly Salary"
                                TransactionType.EXPENSE -> "e.g. Grocery Store"
                                TransactionType.INVESTMENT -> "e.g. HDFC FD — 12 months"
                            },
                            color = colors.textSecondary.copy(0.5f)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colors.inputBackground,
                        focusedContainerColor = colors.inputBackground,
                        unfocusedBorderColor = colors.outline,
                        focusedBorderColor = cs.primary,
                        unfocusedTextColor = cs.onSurface,
                        focusedTextColor = cs.onSurface,
                        cursorColor = cs.primary
                    )
                )
            }

            // ── Category — switches based on type ─────────────────────────────
            AnimatedContent(
                targetState = uiState.type,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(200))
                },
                label = "category_panel"
            ) { type ->
                when (type) {
                    TransactionType.EXPENSE -> ExpenseCategoryGrid(
                        uiState.selectedCategory,
                        onCategorySelect
                    )

                    TransactionType.INVESTMENT -> InvestmentCategoryGrid(
                        uiState.selectedCategory,
                        onCategorySelect
                    )

                    TransactionType.INCOME -> IncomeCategoryGrid(
                        uiState.selectedCategory,
                        onCategorySelect
                    )
                }
            }

            // Date row
            FormCard {
                FormLabel("Date")
                Spacer(Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = colors.inputBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            java.text.SimpleDateFormat(
                                "MMM dd yyyy",
                                java.util.Locale.getDefault()
                            ).format(uiState.date),
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurface
                        )
                        Text(
                            "Change",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Note
            FormCard {
                FormLabel("Note (optional)")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.note,
                    onValueChange = onNoteChange,
                    placeholder = { Text("Add a note…", color = colors.textSecondary.copy(0.5f)) },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colors.inputBackground,
                        focusedContainerColor = colors.inputBackground,
                        unfocusedBorderColor = colors.outline,
                        focusedBorderColor = cs.primary,
                        unfocusedTextColor = cs.onSurface,
                        focusedTextColor = cs.onSurface,
                        cursorColor = cs.primary
                    )
                )
            }

            // Recurring toggle — hide for investments (FD/RD already have fixed tenures)
            if (uiState.type != TransactionType.INVESTMENT) {
                FormCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Recurring",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = cs.onSurface
                            )
                            Text(
                                "Repeat monthly",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary
                            )
                        }
                        Switch(
                            checked = uiState.isRecurring,
                            onCheckedChange = { onRecurringToggle() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = cs.primary
                            )
                        )
                    }
                }
            }

            // Error
            AnimatedVisibility(visible = uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colors.errorContainer,
                        contentColor = colors.error
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = uiState.error ?: "",
                        color = colors.error,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Save button
            val canSave = uiState.amount.isNotBlank()
                    && uiState.selectedCategory.isNotBlank()
                    && uiState.title.isNotBlank()

            val buttonColor = when (uiState.type) {
                TransactionType.INCOME -> Success
                TransactionType.EXPENSE -> cs.primary
                TransactionType.INVESTMENT -> Warning
            }

            Button(
                onClick = onSave,
                enabled = canSave && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = Color.White
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (uiState.isEditMode) "Update Transaction" else {
                            when (uiState.type) {
                                TransactionType.INCOME -> "Save Income"
                                TransactionType.EXPENSE -> "Save Expense"
                                TransactionType.INVESTMENT -> "Save Investment"
                            }
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            if (uiState.isEditMode) {
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Error
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Error)
                ) {
                    Text("Delete Transaction", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ── Category grid panels ──────────────────────────────────────────────────────

@Composable
private fun ExpenseCategoryGrid(selected: String, onSelect: (String) -> Unit) {
    FormCard {
        FormLabel("Category")
        Spacer(Modifier.height(12.dp))
        CategoryGrid(
            items = Category.ALL,
            selected = selected,
            onSelect = onSelect,
            iconRes = { Category.iconResFor(it) },
            label = { it },
            chipColor = { categoryColor(it) }
        )
    }
}

@Composable
private fun IncomeCategoryGrid(selected: String, onSelect: (String) -> Unit) {
    FormCard {
        FormLabel("Income Source")
        Spacer(Modifier.height(12.dp))
        CategoryGrid(
            items = IncomeCategory.ALL,
            selected = selected,
            onSelect = onSelect,
            iconRes = { IncomeCategory.iconResFor(it) },
            label = { it },
            chipColor = { Success }
        )
    }
}

@Composable
private fun InvestmentCategoryGrid(selected: String, onSelect: (String) -> Unit) {
    MaterialTheme.colorScheme
    AppTheme.colors
    FormCard {
        FormLabel("Investment Type")

        // Info pill
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Warning.copy(0.10f))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                "Investment transactions are tracked separately from your spending.",
                style = MaterialTheme.typography.labelSmall,
                color = Warning,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(14.dp))
        CategoryGrid(
            items = InvestmentCategory.ALL,
            selected = selected,
            onSelect = onSelect,
            iconRes = { InvestmentCategory.iconResFor(it) },
            label = { InvestmentCategory.shortLabel(it) },
            chipColor = { Warning }
        )
    }
}

// ── Generic category grid ─────────────────────────────────────────────────────
@Composable
private fun CategoryGrid(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    iconRes: (String) -> Int,
    label: (String) -> String,
    chipColor: (String) -> Color
) {
    val colors = AppTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.chunked(4).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { cat ->
                    val isSelected = selected == cat
                    val color = chipColor(cat)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isSelected) color.copy(alpha = 0.14f)
                                else colors.surfaceVariant
                            )
                            .border(
                                2.dp,
                                if (isSelected) color else Color.Transparent,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { onSelect(cat) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Image(
                                painter = painterResource(iconRes(cat)),
                                contentDescription = cat,
                                modifier = Modifier.size(22.dp),
                            )
                            Text(
                                text = label(cat),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) color else colors.textSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                // fill empty slots in last row
                repeat(4 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

// ── Shared helpers ────────────────────────────────────────────────────────────

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = AppTheme.colors.textSecondary,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun FormCard(content: @Composable ColumnScope.() -> Unit) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surface,
            contentColor = cs.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (colors.isDark) 0.dp else 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

// ── AppColors extension for surfaceVariant ────────────────────────────────────
private val AppColors.surfaceVariant: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceVariant

// ── Previews ──────────────────────────────────────────────────────────────────
@Preview(name = "Add — Expense Light", showBackground = true)
@Composable
private fun PreviewExpenseLight() {
    MoneyTrackerTheme(darkTheme = false) {
        AddTransactionScreen(
            uiState = AddTransactionUiState(type = TransactionType.EXPENSE),
            onTitleChange = {}, onAmountChange = {}, onCategorySelect = {},
            onTypeChange = {}, onNoteChange = {}, onRecurringToggle = {},
            onSave = {}, onDelete = {}, onSaveSuccess = {}
        )
    }
}

@Preview(name = "Add — Investment Dark", showBackground = true)
@Composable
private fun PreviewInvestmentDark() {
    MoneyTrackerTheme(darkTheme = true) {
        AddTransactionScreen(
            uiState = AddTransactionUiState(
                type = TransactionType.INVESTMENT,
                selectedCategory = "Fixed Deposit"
            ),
            onTitleChange = {}, onAmountChange = {}, onCategorySelect = {},
            onTypeChange = {}, onNoteChange = {}, onRecurringToggle = {},
            onSave = {}, onDelete = {}, onSaveSuccess = {}
        )
    }
}

@Preview(name = "Add — Income Light", showBackground = true)
@Composable
private fun PreviewIncomeLight() {
    MoneyTrackerTheme(darkTheme = false) {
        AddTransactionScreen(
            uiState = AddTransactionUiState(
                type = TransactionType.INCOME,
                selectedCategory = "Salary"
            ),
            onTitleChange = {}, onAmountChange = {}, onCategorySelect = {},
            onTypeChange = {}, onNoteChange = {}, onRecurringToggle = {},
            onSave = {}, onDelete = {}, onSaveSuccess = {}
        )
    }
}