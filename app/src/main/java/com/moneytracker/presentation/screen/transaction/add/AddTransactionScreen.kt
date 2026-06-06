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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.moneytracker.ui.theme.AppTheme
import com.moneytracker.ui.theme.ExtendedColors
import com.moneytracker.ui.theme.MoneyTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ── Colour per type ───────────────────────────────────────────────────────────
private val TransactionType.accentColor: Color
    @Composable get() = when (this) {
        TransactionType.INCOME -> AppTheme.colors.success
        TransactionType.EXPENSE -> AppTheme.colors.error
        TransactionType.INVESTMENT -> AppTheme.colors.warning
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    uiState: AddTransactionUiState,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onTypeChange: (TransactionType) -> Unit,
    onDateChange: (Date) -> Unit,
    // FD callbacks
    onMaturityDateChange: (Date) -> Unit,
    onInterestRateChange: (String) -> Unit,
    // RD callbacks
    onRdMonthlyAmountChange: (String) -> Unit,
    onRdMaturityDateChange: (Date) -> Unit,
    onRdInterestRateChange: (String) -> Unit,
    // Common
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

    // ── Date Picker States ────────────────────────────────────────────────────
    var showDatePicker by remember { mutableStateOf(false) }
    var showMaturityDatePicker by remember { mutableStateOf(false) }
    var showRdMaturityDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.date.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis <= System.currentTimeMillis()
        }
    )

    val maturityDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.maturityDate.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis > uiState.date.time
        }
    )

    val rdMaturityDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.rdMaturityDate.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                utcTimeMillis > uiState.date.time
        }
    )

    val amountFocusRequester = remember { FocusRequester() }

    // ── Date Picker Dialogs ───────────────────────────────────────────────────
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateChange(Date(it)) }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showMaturityDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showMaturityDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    maturityDatePickerState.selectedDateMillis?.let { onMaturityDateChange(Date(it)) }
                    showMaturityDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showMaturityDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = maturityDatePickerState) }
    }

    if (showRdMaturityDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showRdMaturityDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    rdMaturityDatePickerState.selectedDateMillis?.let {
                        onRdMaturityDateChange(
                            Date(
                                it
                            )
                        )
                    }
                    showRdMaturityDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showRdMaturityDatePicker = false }) { Text("Cancel") }
            }
        ) { DatePicker(state = rdMaturityDatePickerState) }
    }

    // ── Screen ────────────────────────────────────────────────────────────────
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
                .background(Brush.linearGradient(listOf(colors.primary, cs.primary)))
                .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 36.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
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
                                .background(colors.error.copy(alpha = 0.25f))
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete transaction",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

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

                Spacer(Modifier.height(32.dp))

                // ── Amount input — label changes per investment sub-type ───────
                val amountLabel = when (uiState.selectedCategory) {
                    InvestmentCategory.FD -> "Enter Principal Amount"
                    InvestmentCategory.RD -> "Enter Monthly Installment"
                    else -> "Enter Amount"
                }

                // For RD we bind the header input to rdMonthlyAmount, not amount
                val amountValue = if (uiState.selectedCategory == InvestmentCategory.RD)
                    uiState.rdMonthlyAmount else uiState.amount
                val amountOnChange: (String) -> Unit =
                    if (uiState.selectedCategory == InvestmentCategory.RD)
                        onRdMonthlyAmountChange else onAmountChange

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { amountFocusRequester.requestFocus() },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        amountLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(0.6f)
                    )
                    Spacer(Modifier.height(8.dp))

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
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(amountFocusRequester),
                        decorationBox = { inner ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    "₹",
                                    style = MaterialTheme.typography.displaySmall.copy(
                                        color = Color.White.copy(0.5f),
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Box(contentAlignment = Alignment.Center) {
                                    if (amountValue.isEmpty()) {
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
                            }
                        }
                    )

                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(0.35f))
                    )

                    if (uiState.type == TransactionType.INVESTMENT) {
                        Spacer(Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .clip(RoundedCornerShape(99.dp))
                                .background(Color.White.copy(0.12f))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "Tracked as investment, not spending",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(0.8f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
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

            // ── Category panel — switches based on type ───────────────────────
            AnimatedContent(
                targetState = uiState.type,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
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

            // ── FD Specific Fields ────────────────────────────────────────────
            if (uiState.selectedCategory == InvestmentCategory.FD) {
                FormCard {
                    FormLabel("Fixed Deposit Details")
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
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = colors.inputBackground,
                            focusedContainerColor = colors.inputBackground,
                            unfocusedBorderColor = colors.outline,
                            focusedBorderColor = cs.primary
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    // Maturity Date picker
                    DatePickerRow(
                        label = "Maturity Date",
                        date = uiState.maturityDate,
                        tint = colors.warning,
                        inputBackground = colors.inputBackground,
                        onSurface = cs.onSurface,
                        primary = cs.primary,
                        textSecondary = colors.textSecondary,
                        onClick = { showMaturityDatePicker = true }
                    )

                    // Estimated maturity amount
                    if (uiState.maturityAmount > 0) {
                        Spacer(Modifier.height(16.dp))
                        MaturityAmountCard(
                            maturityAmount = uiState.maturityAmount,
                            extraLine = null
                        )
                    }
                }
            }

            // ── RD Specific Fields ────────────────────────────────────────────
            if (uiState.selectedCategory == InvestmentCategory.RD) {
                FormCard {
                    FormLabel("Recurring Deposit Details")
                    Spacer(Modifier.height(12.dp))

                    // Interest Rate
                    OutlinedTextField(
                        value = uiState.rdInterestRate,
                        onValueChange = onRdInterestRateChange,
                        label = { Text("Interest Rate (% p.a.)") },
                        placeholder = { Text("e.g. 6.5") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = colors.inputBackground,
                            focusedContainerColor = colors.inputBackground,
                            unfocusedBorderColor = colors.outline,
                            focusedBorderColor = cs.primary
                        )
                    )

                    Spacer(Modifier.height(14.dp))

                    // Maturity Date picker
                    DatePickerRow(
                        label = "Maturity Date",
                        date = uiState.rdMaturityDate,
                        tint = colors.warning,
                        inputBackground = colors.inputBackground,
                        onSurface = cs.onSurface,
                        primary = cs.primary,
                        textSecondary = colors.textSecondary,
                        onClick = { showRdMaturityDatePicker = true }
                    )

                    // Estimated maturity amount
                    if (uiState.rdMaturityAmount > 0) {
                        Spacer(Modifier.height(16.dp))
                        MaturityAmountCard(
                            maturityAmount = uiState.rdMaturityAmount,
                            extraLine = null
                        )
                    }
                }
            }

            // ── Start Date ────────────────────────────────────────────────────
            FormCard {
                val dateLabel = when (uiState.selectedCategory) {
                    InvestmentCategory.FD, InvestmentCategory.RD -> "Start Date"
                    else -> "Date"
                }
                FormLabel(dateLabel)
                Spacer(Modifier.height(8.dp))
                DatePickerRow(
                    label = null,
                    date = uiState.date,
                    tint = cs.primary,
                    inputBackground = colors.inputBackground,
                    onSurface = cs.onSurface,
                    primary = cs.primary,
                    textSecondary = colors.textSecondary,
                    actionLabel = "Change",
                    onClick = { showDatePicker = true }
                )
            }

            // ── Note ──────────────────────────────────────────────────────────
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

            // ── Recurring toggle — hidden for Expense ─────────────────────────
            if (uiState.type != TransactionType.EXPENSE) {
                FormCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(cs.primary.copy(alpha = 0.10f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Repeat,
                                    contentDescription = null,
                                    tint = cs.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
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

            // ── Error card ────────────────────────────────────────────────────
            AnimatedVisibility(visible = uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colors.errorContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = colors.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = uiState.error ?: "",
                            color = colors.error,
                            style = MaterialTheme.typography.bodySmall,
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

            val buttonColor = when (uiState.type) {
                TransactionType.INCOME -> colors.success
                TransactionType.EXPENSE -> cs.primary
                TransactionType.INVESTMENT -> colors.warning
            }

            if (!canSave && !uiState.isLoading) {
                val hint = when {
                    uiState.selectedCategory == InvestmentCategory.RD &&
                            uiState.rdMonthlyAmount.isBlank() -> "Enter monthly installment to continue"

                    uiState.selectedCategory == InvestmentCategory.RD &&
                            uiState.rdInterestRate.isBlank() -> "Enter interest rate to continue"

                    uiState.amount.isBlank() -> "Enter an amount to continue"
                    uiState.selectedCategory.isBlank() -> "Select a category to continue"
                    else -> null
                }
                if (hint != null) {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
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
                    contentColor = Color.White,
                    disabledContainerColor = buttonColor.copy(alpha = 0.38f),
                    disabledContentColor = Color.White.copy(alpha = 0.6f)
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
                        text = if (uiState.isEditMode) "Update Transaction" else when (uiState.type) {
                            TransactionType.INCOME -> "Save Income"
                            TransactionType.EXPENSE -> "Save Expense"
                            TransactionType.INVESTMENT -> "Save Investment"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // ── Delete section in edit mode ───────────────────────────────────
            if (uiState.isEditMode) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.outline.copy(alpha = 0.5f)
                    )
                    Text(
                        "or",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.outline.copy(alpha = 0.5f)
                    )
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.error),
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Delete Transaction", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ── Reusable date picker row ──────────────────────────────────────────────────

@Composable
private fun DatePickerRow(
    label: String?,
    date: Date,
    tint: Color,
    inputBackground: Color,
    onSurface: Color,
    primary: Color,
    textSecondary: Color,
    actionLabel: String = "Select",
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
                    if (label != null) {
                        Text(
                            label,
                            style = MaterialTheme.typography.labelSmall,
                            color = textSecondary
                        )
                    }
                    Text(
                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = onSurface
                    )
                }
            }
            Text(
                actionLabel,
                style = MaterialTheme.typography.labelSmall,
                color = primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ── Reusable maturity amount card ─────────────────────────────────────────────

@Composable
private fun MaturityAmountCard(
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
    val colors = AppTheme.colors
    FormCard {
        FormLabel("Income Source")
        Spacer(Modifier.height(12.dp))
        CategoryGrid(
            items = IncomeCategory.ALL,
            selected = selected,
            onSelect = onSelect,
            iconRes = { IncomeCategory.iconResFor(it) },
            label = { it },
            chipColor = { colors.success }
        )
    }
}

@Composable
private fun InvestmentCategoryGrid(selected: String, onSelect: (String) -> Unit) {
    val colors = AppTheme.colors
    FormCard {
        FormLabel("Investment Type")
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colors.warning.copy(0.10f))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = colors.warning,
                modifier = Modifier.size(16.dp)
            )
            Text(
                "Tracked separately from your spending.",
                style = MaterialTheme.typography.labelSmall,
                color = colors.warning,
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
            chipColor = { colors.warning }
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
                                1.5.dp,
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
                                modifier = Modifier.size(22.dp)
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
                repeat(4 - row.size) {
                    Box(modifier = Modifier.weight(1f))
                }
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

private val ExtendedColors.surfaceVariant: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceVariant

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Add — Expense Light", showBackground = true)
@Composable
private fun PreviewExpenseLight() {
    MoneyTrackerTheme(darkTheme = false) {
        AddTransactionScreen(
            uiState = AddTransactionUiState(type = TransactionType.EXPENSE),
            onAmountChange = {}, onCategorySelect = {}, onTypeChange = {},
            onDateChange = {}, onMaturityDateChange = {}, onInterestRateChange = {},
            onRdMonthlyAmountChange = {},
            onRdMaturityDateChange = {}, onRdInterestRateChange = {},
            onNoteChange = {}, onRecurringToggle = {},
            onSave = {}, onDelete = {}, onSaveSuccess = {}
        )
    }
}

@Preview(name = "Add — FD Dark", showBackground = true)
@Composable
private fun PreviewFdDark() {
    MoneyTrackerTheme(darkTheme = true) {
        AddTransactionScreen(
            uiState = AddTransactionUiState(
                type = TransactionType.INVESTMENT,
                selectedCategory = InvestmentCategory.FD
            ),
            onAmountChange = {}, onCategorySelect = {}, onTypeChange = {},
            onDateChange = {}, onMaturityDateChange = {}, onInterestRateChange = {},
            onRdMonthlyAmountChange = {},
            onRdMaturityDateChange = {}, onRdInterestRateChange = {},
            onNoteChange = {}, onRecurringToggle = {},
            onSave = {}, onDelete = {}, onSaveSuccess = {}
        )
    }
}

@Preview(name = "Add — RD Light", showBackground = true)
@Composable
private fun PreviewRdLight() {
    MoneyTrackerTheme(darkTheme = false) {
        AddTransactionScreen(
            uiState = AddTransactionUiState(
                type = TransactionType.INVESTMENT,
                selectedCategory = InvestmentCategory.RD
            ),
            onAmountChange = {}, onCategorySelect = {}, onTypeChange = {},
            onDateChange = {}, onMaturityDateChange = {}, onInterestRateChange = {},
            onRdMonthlyAmountChange = {},
            onRdMaturityDateChange = {}, onRdInterestRateChange = {},
            onNoteChange = {}, onRecurringToggle = {},
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
            onAmountChange = {}, onCategorySelect = {}, onTypeChange = {},
            onDateChange = {}, onMaturityDateChange = {}, onInterestRateChange = {},
            onRdMonthlyAmountChange = {},
            onRdMaturityDateChange = {}, onRdInterestRateChange = {},
            onNoteChange = {}, onRecurringToggle = {},
            onSave = {}, onDelete = {}, onSaveSuccess = {}
        )
    }
}
