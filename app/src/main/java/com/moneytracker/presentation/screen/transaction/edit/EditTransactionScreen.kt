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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.domain.model.Category
import com.moneytracker.domain.model.IncomeCategory
import com.moneytracker.domain.model.InvestmentCategory
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.presentation.components.categoryColor
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.MoneyTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    LaunchedEffect(uiState.isSaved) { if (uiState.isSaved) onSaved() }
    LaunchedEffect(uiState.isDeleted) { if (uiState.isDeleted) onDeleted() }

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
        onAmountChange = viewModel::onAmountChange,
        onCategorySelect = viewModel::onCategorySelect,
        onTypeChange = viewModel::onTypeChange,
        onNoteChange = viewModel::onNoteChange,
        onRecurringToggle = viewModel::onRecurringToggle,
        // FD
        onMaturityDateChange = viewModel::onMaturityDateChange,
        onInterestRateChange = viewModel::onInterestRateChange,
        // RD
        onRdMonthlyAmountChange = viewModel::onRdMonthlyAmountChange,
        onRdMaturityDateChange = viewModel::onRdMaturityDateChange,
        onRdInterestRateChange = viewModel::onRdInterestRateChange,
        onSave = viewModel::save,
        onDelete = viewModel::delete,
        onBack = onBack
    )
}

// ── Stateless content ─────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionContent(
    uiState: EditTransactionUiState,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onNoteChange: (String) -> Unit,
    onRecurringToggle: () -> Unit,
    // FD
    onMaturityDateChange: (Date) -> Unit,
    onInterestRateChange: (String) -> Unit,
    // RD
    onRdMonthlyAmountChange: (String) -> Unit,
    onRdMaturityDateChange: (Date) -> Unit,
    onRdInterestRateChange: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val colors = AppTheme.colors

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showFdMaturityDatePicker by remember { mutableStateOf(false) }
    var showRdMaturityDatePicker by remember { mutableStateOf(false) }

    // ── FD maturity date picker state ─────────────────────────────────────────
    val fdMaturityDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.maturityDate.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis > System.currentTimeMillis()
        }
    )

    // ── RD maturity date picker state ─────────────────────────────────────────
    val rdMaturityDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.rdMaturityDate.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis > System.currentTimeMillis()
        }
    )

    // ── Delete confirmation dialog ────────────────────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.DeleteForever, null, tint = colors.error) },
            title = { Text("Delete Transaction?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This ${uiState.type.name.lowercase()} (₹${uiState.amount}) will be permanently deleted.",
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

    // ── FD maturity date picker dialog ────────────────────────────────────────
    if (showFdMaturityDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showFdMaturityDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    fdMaturityDatePickerState.selectedDateMillis?.let {
                        onMaturityDateChange(Date(it))
                    }
                    showFdMaturityDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showFdMaturityDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = fdMaturityDatePickerState) }
    }

    // ── RD maturity date picker dialog ────────────────────────────────────────
    if (showRdMaturityDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showRdMaturityDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    rdMaturityDatePickerState.selectedDateMillis?.let {
                        onRdMaturityDateChange(Date(it))
                    }
                    showRdMaturityDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showRdMaturityDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = rdMaturityDatePickerState) }
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
                .background(Brush.linearGradient(AppTheme.colors.headerGradient))
                .statusBarsPadding()
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 36.dp)
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
                            TransactionType.INCOME -> colors.success
                            TransactionType.EXPENSE -> colors.error
                            TransactionType.INVESTMENT -> colors.warning
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

                // Amount input — label + binding changes for RD
                val amountLabel = when (uiState.selectedCategory) {
                    InvestmentCategory.FD -> "Principal Amount"
                    InvestmentCategory.RD -> "Monthly Installment"
                    else -> "Amount"
                }
                val amountValue = if (uiState.selectedCategory == InvestmentCategory.RD)
                    uiState.rdMonthlyAmount else uiState.amount
                val amountOnChange: (String) -> Unit =
                    if (uiState.selectedCategory == InvestmentCategory.RD)
                        onRdMonthlyAmountChange else onAmountChange

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        amountLabel,
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
                            value = amountValue,
                            onValueChange = amountOnChange,
                            textStyle = MaterialTheme.typography.displaySmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.widthIn(min = 80.dp, max = 240.dp),
                            decorationBox = { inner ->
                                if (amountValue.isEmpty()) {
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

            // Category — switches by type
            AnimatedContent(
                targetState = uiState.type,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                label = "cat_panel"
            ) { type ->
                when (type) {
                    TransactionType.EXPENSE -> EditCategoryGrid(
                        Category.ALL, uiState.selectedCategory, onCategorySelect,
                        { Category.iconResFor(it) }, { it }, { categoryColor(it) }
                    )

                    TransactionType.INCOME -> EditCategoryGrid(
                        IncomeCategory.ALL, uiState.selectedCategory, onCategorySelect,
                        { IncomeCategory.iconResFor(it) }, { it }, { colors.success }
                    )

                    TransactionType.INVESTMENT -> EditCategoryGrid(
                        InvestmentCategory.ALL, uiState.selectedCategory, onCategorySelect,
                        { InvestmentCategory.iconResFor(it) },
                        { InvestmentCategory.shortLabel(it) },
                        { colors.warning }
                    )
                }
            }

            // ── FD Specific Fields ────────────────────────────────────────────
            if (uiState.selectedCategory == InvestmentCategory.FD) {
                EditFormCard {
                    EditFormLabel("Fixed Deposit Details")
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.interestRate,
                        onValueChange = onInterestRateChange,
                        label = { Text("Interest Rate (% p.a.)") },
                        placeholder = { Text("e.g. 7.5") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = editFieldColors()
                    )

                    Spacer(Modifier.height(14.dp))

                    EditDatePickerRow(
                        label = "Maturity Date",
                        date = uiState.maturityDate,
                        tint = colors.warning,
                        primary = cs.primary,
                        textSecondary = colors.textSecondary,
                        inputBackground = colors.inputBackground,
                        onSurface = cs.onSurface,
                        onClick = { showFdMaturityDatePicker = true }
                    )

                    if (uiState.maturityAmount > 0) {
                        Spacer(Modifier.height(16.dp))
                        EditMaturityAmountCard(
                            maturityAmount = uiState.maturityAmount,
                            extraLine = null
                        )
                    }
                }
            }

            // ── RD Specific Fields ────────────────────────────────────────────
            if (uiState.selectedCategory == InvestmentCategory.RD) {
                EditFormCard {
                    EditFormLabel("Recurring Deposit Details")
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.rdInterestRate,
                        onValueChange = onRdInterestRateChange,
                        label = { Text("Interest Rate (% p.a.)") },
                        placeholder = { Text("e.g. 6.5") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = editFieldColors()
                    )

                    Spacer(Modifier.height(14.dp))

                    EditDatePickerRow(
                        label = "Maturity Date",
                        date = uiState.rdMaturityDate,
                        tint = colors.warning,
                        primary = cs.primary,
                        textSecondary = colors.textSecondary,
                        inputBackground = colors.inputBackground,
                        onSurface = cs.onSurface,
                        onClick = { showRdMaturityDatePicker = true }
                    )

                    if (uiState.rdMaturityAmount > 0) {
                        Spacer(Modifier.height(16.dp))
                        EditMaturityAmountCard(
                            maturityAmount = uiState.rdMaturityAmount,
                            extraLine = null
                        )
                    }
                }
            }

            // ── Note ──────────────────────────────────────────────────────────
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

            // ── Recurring toggle ──────────────────────────────────────────────
            // Hidden for Expense. Locked ON (disabled) when RD is selected.
            if (uiState.type != TransactionType.EXPENSE) {
                val isRd = uiState.selectedCategory == InvestmentCategory.RD
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
                                // Explain to the user why it's locked when RD
                                if (isRd) "Always on for Recurring Deposit"
                                else "Repeat monthly",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textSecondary
                            )
                        }
                        Switch(
                            checked = uiState.isRecurring,
                            onCheckedChange = { if (!isRd) onRecurringToggle() },
                            // Visually disable the switch for RD so user can't toggle it off
                            enabled = !isRd,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = cs.primary,
                                // Keep a distinct amber tint when locked on for RD
                                disabledCheckedThumbColor = Color.White,
                                disabledCheckedTrackColor = colors.warning
                            )
                        )
                    }
                }
            }

            // ── Error ──────────────────────────────────────────────────────────
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

            // ── Save button ───────────────────────────────────────────────────
            val canSave = when (uiState.selectedCategory) {
                InvestmentCategory.RD ->
                    uiState.rdMonthlyAmount.isNotBlank() &&
                            uiState.rdInterestRate.isNotBlank() &&
                            uiState.selectedCategory.isNotBlank()

                else ->
                    uiState.amount.isNotBlank() && uiState.selectedCategory.isNotBlank()
            }

            Button(
                onClick = onSave,
                enabled = canSave && !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (uiState.type) {
                        TransactionType.INCOME -> colors.success
                        TransactionType.EXPENSE -> cs.primary
                        TransactionType.INVESTMENT -> colors.warning
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

            // ── Delete button ─────────────────────────────────────────────────
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

// ── Reusable date row ─────────────────────────────────────────────────────────

@Composable
private fun EditDatePickerRow(
    label: String,
    date: Date,
    tint: Color,
    primary: Color,
    textSecondary: Color,
    inputBackground: Color,
    onSurface: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = inputBackground,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelSmall,
                        color = textSecondary
                    )
                    Text(
                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = onSurface
                    )
                }
            }
            Text(
                "Select",
                style = MaterialTheme.typography.labelSmall,
                color = primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ── Reusable maturity amount card ─────────────────────────────────────────────

@Composable
private fun EditMaturityAmountCard(
    maturityAmount: Double,
    extraLine: String?
) {
    val colors = AppTheme.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.success.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.TrendingUp,
                contentDescription = null,
                tint = colors.success,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    "Estimated Maturity Value",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.success
                )
                Text(
                    "₹${String.format("%.2f", maturityAmount)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.success
                )
                if (extraLine != null) {
                    Text(
                        extraLine,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.success.copy(alpha = 0.7f)
                    )
                }
            }
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
                                .background(
                                    if (isSelected) color.copy(0.14f)
                                    else MaterialTheme.colorScheme.surfaceVariant
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
                                    painterResource(iconRes(cat)),
                                    cat,
                                    modifier = Modifier.size(22.dp)
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

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Edit — Expense Light", showBackground = true)
@Composable
private fun PreviewEditExpenseLight() {
    MoneyTrackerTheme(darkTheme = false) {
        EditTransactionContent(
            uiState = EditTransactionUiState(
                id = 1,
                amount = "1250",
                selectedCategory = "Food",
                type = TransactionType.EXPENSE,
                note = "Dinner with friends",
                isLoading = false
            ),
            onAmountChange = {}, onCategorySelect = {}, onTypeChange = {},
            onNoteChange = {}, onRecurringToggle = {},
            onMaturityDateChange = {}, onInterestRateChange = {},
            onRdMonthlyAmountChange = {}, onRdMaturityDateChange = {},
            onRdInterestRateChange = {}, onSave = {}, onDelete = {}, onBack = {}
        )
    }
}

@Preview(name = "Edit — FD Dark", showBackground = true)
@Composable
private fun PreviewEditFdDark() {
    MoneyTrackerTheme(darkTheme = true) {
        EditTransactionContent(
            uiState = EditTransactionUiState(
                id = 2,
                amount = "50000",
                selectedCategory = InvestmentCategory.FD,
                type = TransactionType.INVESTMENT,
                interestRate = "7.5",
                maturityAmount = 53750.0,
                isLoading = false
            ),
            onAmountChange = {}, onCategorySelect = {}, onTypeChange = {},
            onNoteChange = {}, onRecurringToggle = {},
            onMaturityDateChange = {}, onInterestRateChange = {},
            onRdMonthlyAmountChange = {}, onRdMaturityDateChange = {},
            onRdInterestRateChange = {}, onSave = {}, onDelete = {}, onBack = {}
        )
    }
}

@Preview(name = "Edit — RD Light", showBackground = true)
@Composable
private fun PreviewEditRdLight() {
    MoneyTrackerTheme(darkTheme = false) {
        EditTransactionContent(
            uiState = EditTransactionUiState(
                id = 3,
                rdMonthlyAmount = "5000",
                selectedCategory = InvestmentCategory.RD,
                type = TransactionType.INVESTMENT,
                rdInterestRate = "6.5",
                rdMaturityAmount = 62100.0,
                isRecurring = true,
                isLoading = false
            ),
            onAmountChange = {}, onCategorySelect = {}, onTypeChange = {},
            onNoteChange = {}, onRecurringToggle = {},
            onMaturityDateChange = {}, onInterestRateChange = {},
            onRdMonthlyAmountChange = {}, onRdMaturityDateChange = {},
            onRdInterestRateChange = {}, onSave = {}, onDelete = {}, onBack = {}
        )
    }
}
