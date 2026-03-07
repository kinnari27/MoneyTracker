package com.moneytracker.presentation.screen.transaction.edit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.domain.model.Category
import com.moneytracker.domain.model.IncomeCategory
import com.moneytracker.domain.model.InvestmentCategory
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.presentation.components.categoryColor
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Success
import com.moneytracker.ui.theme.Warning

// ── Entry point ───────────────────────────────────────────────────────────────
@Composable
fun EditTransactionScreen(
    transactionId: Int,
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    onBack: () -> Unit,
    viewModel: EditTransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) { if (uiState.isSaved) onSaved() }
    LaunchedEffect(uiState.isDeleted) { if (uiState.isDeleted) onDeleted() }

    // Loading spinner while reading from DB
    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    if (uiState.notFound) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Transaction not found", color = AppTheme.colors.textSecondary)
        }
        return
    }

    EditTransactionContent(
        uiState = uiState,
        onTitleChange = viewModel::onTitleChange,
        onAmountChange = viewModel::onAmountChange,
        onCategorySelect = viewModel::onCategorySelect,
        onTypeChange = viewModel::onTypeChange,
        onNoteChange = viewModel::onNoteChange,
        onRecurringToggle = viewModel::onRecurringToggle,
        onSave = viewModel::save,
        onDelete = viewModel::delete,
        onBack = onBack
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────
@Composable
fun EditTransactionContent(
    uiState: EditTransactionUiState,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onNoteChange: (String) -> Unit,
    onRecurringToggle: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.DeleteForever, null, tint = colors.error) },
            title = { Text("Delete Transaction?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "\"${uiState.title}\" (₹${uiState.amount}) will be permanently deleted.",
                    color = colors.textSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false; onDelete() }) {
                    Text("Delete", color = colors.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            },
            containerColor = cs.surface,
            titleContentColor = cs.onSurface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Header ─────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryDark, cs.primary)))
                .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 36.dp)
        ) {
            Column {
                // Top bar: back + delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Edit Entry",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(0.7f)
                        )
                        Text(
                            "Edit Transaction",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.White.copy(0.8f))
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 3-way type toggle
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
                        val typeColor = when (type) {
                            TransactionType.INCOME -> Success
                            TransactionType.EXPENSE -> Error
                            TransactionType.INVESTMENT -> Warning
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { onTypeChange(type) }
                                .padding(vertical = 11.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (type) {
                                    TransactionType.INCOME -> "↑ Income"
                                    TransactionType.EXPENSE -> "↓ Expense"
                                    TransactionType.INVESTMENT -> "⇌ Invest"
                                },
                                color = if (isSelected) typeColor else Color.White.copy(0.8f),
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Amount input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Amount",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(0.6f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                                        "0", style = MaterialTheme.typography.displaySmall.copy(
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
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(0.35f))
                    )
                }
            }
        }

        // ── Form ──────────────────────────────────────────────────────────────
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Title
            EditFormCard {
                EditFormLabel("Title")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = editFieldColors()
                )
            }

            // Category — switches by type, same as Add screen
            AnimatedContent(
                targetState = uiState.type,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                label = "cat_panel"
            ) { type ->
                when (type) {
                    TransactionType.EXPENSE -> EditCategoryGrid(
                        Category.ALL,
                        uiState.selectedCategory,
                        onCategorySelect,
                        { Category.iconResFor(it) },
                        { it },
                        { categoryColor(it) })

                    TransactionType.INCOME -> EditCategoryGrid(
                        IncomeCategory.ALL,
                        uiState.selectedCategory,
                        onCategorySelect,
                        { IncomeCategory.iconResFor(it) },
                        { it },
                        { Success })

                    TransactionType.INVESTMENT -> EditCategoryGrid(
                        InvestmentCategory.ALL,
                        uiState.selectedCategory,
                        onCategorySelect,
                        { InvestmentCategory.iconResFor(it) },
                        { InvestmentCategory.shortLabel(it) },
                        { Warning })
                }
            }

            // Note
            EditFormCard {
                EditFormLabel("Note (optional)")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.note,
                    onValueChange = onNoteChange,
                    placeholder = { Text("Add a note…", color = colors.textSecondary.copy(0.5f)) },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = editFieldColors()
                )
            }

            // Recurring toggle (hidden for investments)
            if (uiState.type != TransactionType.INVESTMENT) {
                EditFormCard {
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
                    colors = CardDefaults.cardColors(containerColor = colors.errorContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            null,
                            modifier = Modifier.size(16.dp),
                            tint = colors.error
                        )
                        Text(
                            uiState.error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Save button
            val canSave =
                uiState.title.isNotBlank() && uiState.amount.isNotBlank() && uiState.selectedCategory.isNotBlank()
            Button(
                onClick = onSave,
                enabled = canSave && !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (uiState.type) {
                        TransactionType.INCOME -> Success
                        TransactionType.EXPENSE -> cs.primary
                        TransactionType.INVESTMENT -> Warning
                    },
                    contentColor = Color.White
                )
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Save Changes",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Delete button
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.5.dp, colors.error.copy(0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.error)
            ) {
                Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Delete Transaction",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ── Local helpers ─────────────────────────────────────────────────────────────

@Composable
private fun EditFormLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelMedium,
        color = AppTheme.colors.textSecondary,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun EditFormCard(content: @Composable ColumnScope.() -> Unit) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surface, contentColor = cs.onSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (colors.isDark) 0.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
private fun editFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = AppTheme.colors.inputBackground,
    focusedContainerColor = AppTheme.colors.inputBackground,
    unfocusedBorderColor = AppTheme.colors.outline,
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    focusedTextColor = MaterialTheme.colorScheme.onSurface,
    cursorColor = MaterialTheme.colorScheme.primary
)

@Composable
private fun EditCategoryGrid(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    iconRes: (String) -> Int,
    label: (String) -> String,
    chipColor: (String) -> Color
) {
    val colors = AppTheme.colors
    EditFormCard {
        EditFormLabel("Category")
        Spacer(Modifier.height(12.dp))
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
                                .background(if (isSelected) color.copy(0.14f) else MaterialTheme.colorScheme.surfaceVariant)
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
                                    painterResource(iconRes(cat)),
                                    cat,
                                    modifier = Modifier.size(22.dp),
                                )
                                Text(
                                    label(cat),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) color else colors.textSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    repeat(4 - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}