package com.moneytracker.presentation.screen.settings.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneytracker.R
import com.moneytracker.presentation.navigation.Routes
import com.moneytracker.ui.theme.Indigo100
import com.moneytracker.ui.theme.Indigo600
import com.moneytracker.ui.theme.Slate100
import com.moneytracker.ui.theme.Slate200
import com.moneytracker.ui.theme.Slate400
import com.moneytracker.ui.theme.Slate50
import com.moneytracker.ui.theme.Slate900
import com.moneytracker.ui.theme.White
import com.moneytracker.ui.theme.moneytrackerTheme

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit = {},
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreenContent(
        uiState = uiState,
        onNavigate = onNavigate,
        onSignOut = {
            viewModel.signOut()
            onSignOut()
        },
        modifier = modifier
    )
}

@Composable
fun ProfileScreenContent(
    uiState: ProfileUiState,
    onNavigate: (String) -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val userName = uiState.userName.ifBlank { "User" }
    val userEmail = uiState.userEmail

    val menuSections = remember {
        listOf(
            "Account" to listOf(
                Triple(R.drawable.ic_user, "Edit Profile", "Update personal info") to Routes.EDIT_PROFILE,
                Triple(R.drawable.ic_bell, "Notifications", "Manage alerts") to Routes.NOTIFICATIONS,
                Triple(R.drawable.ic_currency, "Currency", "Indian Rupee ₹ INR") to Routes.CURRENCY,
            ),
            "Security" to listOf(
                Triple(R.drawable.ic_locked, "Biometric Lock", "Fingerprint / Face ID") to Routes.BIOMETRIC_LOCK
            ) + if (uiState.hasPassword) {
                listOf(Triple(R.drawable.ic_key, "Change Password", "Update your password") to Routes.CHANGE_PASSWORD)
            } else emptyList()
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(cs.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {

        // ── Header — flat Slate900, no gradient ───────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Slate900)
                    .padding(top = 52.dp, bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Avatar — rounded square, not a circle
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Indigo600),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1).uppercase(),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        userName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        userEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate400
                    )

                    // Premium badge — subtle, no pill shape
                    if (uiState.isPremium) {
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Indigo600.copy(alpha = 0.25f))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                "Premium Member",
                                style = MaterialTheme.typography.labelMedium,
                                color = Indigo100,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // ── Stats Card ────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        uiState.totalTransactions.toString() to "Transactions",
                        uiState.totalCategories.toString() to "Categories",
                        uiState.activeMonths.toString() to "Months"
                    ).forEachIndexed { i, (value, label) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                value,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Indigo600
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall,
                                color = cs.onSurfaceVariant
                            )
                        }
                        if (i < 2) VerticalDivider(
                            modifier = Modifier.height(32.dp),
                            thickness = 0.5.dp,
                            color = Slate200
                        )
                    }
                }
            }
        }

        // ── Menu Sections ─────────────────────────────────────────────────────
        menuSections.forEach { (section, items) ->
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Text(
                        text = section.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onSurfaceVariant,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cs.surface),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        items.forEachIndexed { idx, (triple, route) ->
                            val (iconRes, title, subtitle) = triple
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { if (route.isNotEmpty()) onNavigate(route) }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Icon container — rounded square
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Slate100),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painterResource(iconRes),
                                        title,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = cs.onSurface
                                    )
                                    Text(
                                        subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = cs.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    Icons.Default.ChevronRight,
                                    null,
                                    tint = Slate400,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            if (idx < items.size - 1) HorizontalDivider(
                                color = cs.outline,
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }

        // ── Sign Out ──────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cs.errorContainer
                ),
                onClick = { onSignOut() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Sign Out",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = cs.error
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "MoneyTracker v1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = cs.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    moneytrackerTheme {
        ProfileScreenContent(
            uiState = ProfileUiState(
                userName = "Rahul Sharma",
                userEmail = "rahul.sharma@gmail.com",
                isPremium = true,
                totalTransactions = 124,
                totalCategories = 8,
                activeMonths = 6,
                isLoading = false
            ),
            onNavigate = {},
            onSignOut = {}
        )
    }
}