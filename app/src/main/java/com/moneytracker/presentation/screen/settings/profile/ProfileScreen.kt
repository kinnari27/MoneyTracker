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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.moneytracker.presentation.navigation.Routes
import com.moneytracker.ui.theme.Error
import com.moneytracker.ui.theme.OnSurface
import com.moneytracker.ui.theme.OnSurfaceVariant
import com.moneytracker.ui.theme.Outline
import com.moneytracker.ui.theme.Primary
import com.moneytracker.ui.theme.PrimaryDark
import com.moneytracker.ui.theme.Surface
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
    val userName = uiState.userName.ifBlank { "User" }
    val userEmail = uiState.userEmail

    // Map each menu item to its route
    val menuSections = remember {
        listOf(
            "Account" to listOf(
                Triple(
                    R.drawable.ic_user,
                    "Edit Profile",
                    "Update personal info"
                ) to Routes.EDIT_PROFILE,
                Triple(
                    R.drawable.ic_bell,
                    "Notifications",
                    "Manage alerts"
                ) to Routes.NOTIFICATIONS,
                Triple(R.drawable.ic_currency, "Currency", "Indian Rupee ₹ INR") to Routes.CURRENCY,
            ),
            "Security" to listOf(
                Triple(
                    R.drawable.ic_locked,
                    "Biometric Lock",
                    "Fingerprint / Face ID"
                ) to Routes.BIOMETRIC_LOCK
            ) + if (uiState.hasPassword) {
                listOf(Triple(R.drawable.ic_key, "Change Password", "Update your password") to Routes.CHANGE_PASSWORD)
            } else {
                emptyList()
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {

        // ── Header ────────────────────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(PrimaryDark, Primary)))
                    .padding(top = 52.dp, bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            userName.take(1).uppercase(),
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        userName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        userEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(0.7f)
                    )
                    if (uiState.isPremium) {
                        Spacer(Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(99.dp),
                            color = Color.White.copy(0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Image(
                                    painterResource(R.drawable.ic_star),
                                    null,
                                    modifier = Modifier.size(14.dp),
                                )
                                Text(
                                    "Premium Member",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Stats Card ────────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val stats = listOf(
                        uiState.totalTransactions.toString() to "Transactions",
                        uiState.totalCategories.toString() to "Categories",
                        uiState.activeMonths.toString() to "Months"
                    )
                    stats.forEachIndexed { i, (value, label) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                value,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Primary
                            )
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                        }
                        if (i < stats.size - 1) VerticalDivider(
                            modifier = Modifier.height(36.dp),
                            thickness = 1.dp,
                            color = Outline.copy(0.4f)
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
                        items.forEachIndexed { idx, (triple, route) ->
                            val (iconRes, title, subtitle) = triple
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { if (route.isNotEmpty()) onNavigate(route) }
                                    .padding(horizontal = 18.dp, vertical = 15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Primary.copy(alpha = 0.08f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painterResource(iconRes),
                                        title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OnSurface
                                    )
                                    Text(
                                        subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                                Icon(
                                    Icons.Default.ChevronRight,
                                    null,
                                    tint = OnSurfaceVariant.copy(0.5f)
                                )
                            }
                            if (idx < items.size - 1) HorizontalDivider(
                                color = Outline.copy(0.3f),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(horizontal = 18.dp)
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
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Error.copy(0.08f)),
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
                        fontWeight = FontWeight.Bold,
                        color = Error
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "MoneyTracker v1.0.0",
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant.copy(0.4f)
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
