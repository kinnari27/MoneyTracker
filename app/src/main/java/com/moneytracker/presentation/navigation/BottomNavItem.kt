package com.moneytracker.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route         : String,
    val label         : String,
    val selectedIcon  : ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(
        route          = "home",
        label          = "Home",
        selectedIcon   = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    object History : BottomNavItem(
        route          = "history",
        label          = "History",
        selectedIcon   = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    )
    object Add : BottomNavItem(
        route          = "add",
        label          = "Add",
        selectedIcon   = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    )
    object Analytics : BottomNavItem(
        route          = "analytics",
        label          = "Stats",
        selectedIcon   = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart
    )
    object Profile : BottomNavItem(
        route          = "profile",
        label          = "Profile",
        selectedIcon   = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}