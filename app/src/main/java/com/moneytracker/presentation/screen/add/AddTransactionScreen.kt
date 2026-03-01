package com.moneytracker.presentation.screen.add

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.moneytracker.domain.model.TransactionType
import com.moneytracker.presentation.components.categoryColor
import com.moneytracker.ui.theme.*

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
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaveSuccess()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                .padding(top = 52.dp, start = 20.dp, end = 20.dp, bottom = 32.dp)
        ) {
            Column {
                Text(
                    "New Entry",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    "Add Transaction",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(Modifier.height(20.dp))

                // Type Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TransactionType.values().forEach { type ->
                        val isSelected = uiState.type == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color.White else Color.Transparent)
                                .clickable { onTypeChange(type) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (type == TransactionType.EXPENSE) "⬇ Expense" else "⬆ Income",
                                color = if (isSelected) (if (type == TransactionType.EXPENSE) Error else Success) else Color.White.copy(
                                    alpha = 0.8f
                                ),
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Amount Input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Enter Amount",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "₹",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White.copy(alpha = 0.6f)
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
                                        style = MaterialTheme.typography.displaySmall,
                                        color = Color.White.copy(alpha = 0.4f),
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                                inner()
                            }
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.4f))
                    )
                }
            }
        }

        // ── Form Fields ───────────────────────────────────────────────────────
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Title
            FormCard {
                Text(
                    "Title",
                    style = MaterialTheme.typography.labelMedium,
                    color = OnSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    placeholder = { Text("e.g. Grocery Store") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Outline,
                        focusedBorderColor = Primary
                    )
                )
            }

            // Categories
            FormCard {
                Text(
                    "Category",
                    style = MaterialTheme.typography.labelMedium,
                    color = OnSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Category.ALL.chunked(4).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { cat ->
                                val isSelected = uiState.selectedCategory == cat
                                val color = categoryColor(cat)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSelected) color.copy(0.15f) else SurfaceVariant)
                                        .border(
                                            2.dp,
                                            if (isSelected) color else Color.Transparent,
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable { onCategorySelect(cat) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(categoryColor(cat).copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = painterResource(id = Category.iconResFor(cat)),
                                                contentDescription = cat,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Text(
                                            cat,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = OnSurfaceVariant,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            repeat(4 - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }

            // Note
            FormCard {
                Text(
                    "Note (optional)",
                    style = MaterialTheme.typography.labelMedium,
                    color = OnSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.note,
                    onValueChange = onNoteChange,
                    placeholder = { Text("Add a note...") },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Outline,
                        focusedBorderColor = Primary
                    )
                )
            }

            // Recurring Toggle
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
                            color = OnSurface
                        )
                        Text(
                            "Repeat monthly",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }
                    Switch(
                        checked = uiState.isRecurring,
                        onCheckedChange = { onRecurringToggle() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Primary
                        )
                    )
                }
            }

            // Error
            if (uiState.error != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = uiState.error,
                        color = Error,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Save Button
            val canSave =
                uiState.amount.isNotBlank() && uiState.selectedCategory.isNotBlank() && uiState.title.isNotBlank()
            Button(
                onClick = onSave,
                enabled = canSave && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        "Save Transaction",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean,
    modifier: Modifier,
    decorationBox: @Composable ((@Composable () -> Unit)) -> Unit
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        modifier = modifier,
        decorationBox = decorationBox
    )
}

@Composable
private fun FormCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Preview(showBackground = true)
@Composable
fun AddTransactionScreenPreview() {
    moneytrackerTheme {
        AddTransactionScreen(
            uiState = AddTransactionUiState(
                amount = "1500",
                title = "Dinner at Taj",
                selectedCategory = Category.FOOD,
                type = TransactionType.EXPENSE
            ),
            onTitleChange = {},
            onAmountChange = {},
            onCategorySelect = {},
            onTypeChange = {},
            onNoteChange = {},
            onRecurringToggle = {},
            onSave = {},
            onSaveSuccess = {}
        )
    }
}
