package com.moneytracker.presentation.screen.settings.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.R
import com.moneytracker.ui.theme.OnSurface
import com.moneytracker.ui.theme.OnSurfaceVariant
import com.moneytracker.ui.theme.Outline
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Surface
import com.moneytracker.ui.theme.SurfaceVariant
import com.moneytracker.ui.theme.moneytrackerTheme

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationsScreenContent(
        uiState = uiState,
        onBack = onBack,
        onToggle = { key, enabled ->
            when (key) {
                "all" -> viewModel.onAllNotificationsToggle(enabled)
                "budget" -> viewModel.onBudgetAlertToggle(enabled)
                "large" -> viewModel.onLargeTransactionToggle(enabled)
                "daily" -> viewModel.onDailySummaryToggle(enabled)
                "bill" -> viewModel.onBillReminderToggle(enabled)
                "monthly" -> viewModel.onMonthlyReportToggle(enabled)
                "tips" -> viewModel.onTipsToggle(enabled)
                "updates" -> viewModel.onAppUpdatesToggle(enabled)
            }
        },
        modifier = modifier
    )
}

@Composable
fun NotificationsScreenContent(
    uiState: NotificationsUiState,
    onBack: () -> Unit,
    onToggle: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {

        // ── Header ────────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                    .padding(top = 52.dp, start = 4.dp, end = 20.dp, bottom = 28.dp)
            ) {
                Column {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, null, tint = Color.White)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Notifications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        "Manage your alerts",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(0.7f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // ── Master Toggle ─────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.allNotifications) Primary.copy(0.08f) else SurfaceVariant.copy(
                        0.5f
                    )
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (uiState.allNotifications) Primary.copy(0.15f) else SurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_bell),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(
                            "All Notifications",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )
                        Text(
                            if (uiState.allNotifications) "Enabled" else "Disabled",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (uiState.allNotifications) Primary else OnSurfaceVariant
                        )
                    }
                    Switch(
                        checked = uiState.allNotifications,
                        onCheckedChange = { onToggle("all", it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Primary
                        )
                    )
                }
            }
        }

        // ── Notification Groups ───────────────────────────────────────────────
        val sections = listOf(
            "Spending Alerts" to listOf(
                Triple(
                    "Budget Limit Alert",
                    "Warn when you near budget limit",
                    uiState.budgetAlert
                ) to "budget",
                Triple(
                    "Large Transaction",
                    "Alert for transactions over ₹1000",
                    uiState.largeTransaction
                ) to "large",
                Triple(
                    "Daily Spending Summary",
                    "Get a summary every evening",
                    uiState.dailySummary
                ) to "daily",
            ),
            "Reminders" to listOf(
                Triple(
                    "Bill Payment Reminder",
                    "Remind before due date",
                    uiState.billReminder
                ) to "bill",
                Triple(
                    "Monthly Report",
                    "Summary on 1st of every month",
                    uiState.monthlyReport
                ) to "monthly",
            ),
            "App" to listOf(
                Triple(
                    "Tips & Insights",
                    "Smart money-saving tips",
                    uiState.tips
                ) to "tips",
                Triple(
                    "App Updates",
                    "New features and improvements",
                    uiState.appUpdates
                ) to "updates",
            )
        )

        sections.forEach { (sectionTitle, items) ->
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Text(
                        text = sectionTitle.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        items.forEachIndexed { idx, (triple, key) ->
                            val (title, subtitle, isOn) = triple
                            val enabled = uiState.allNotifications
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (enabled) OnSurface else OnSurfaceVariant
                                    )
                                    Text(
                                        subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = isOn && enabled,
                                    onCheckedChange = { if (enabled) onToggle(key, it) },
                                    enabled = enabled,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Primary
                                    )
                                )
                            }
                            if (idx < items.size - 1) {
                                HorizontalDivider(
                                    color = Outline.copy(0.3f),
                                    thickness = 0.5.dp,
                                    modifier = Modifier.padding(horizontal = 18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    moneytrackerTheme {
        NotificationsScreenContent(
            uiState = NotificationsUiState(
                allNotifications = true,
                budgetAlert = true,
                largeTransaction = false,
                dailySummary = true
            ),
            onBack = {},
            onToggle = { _, _ -> }
        )
    }
}
