package com.moneytracker

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moneytracker.presentation.components.BottomNavBar
import com.moneytracker.presentation.navigation.NavGraph
import com.moneytracker.presentation.navigation.Routes
import com.moneytracker.ui.theme.moneytrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            moneytrackerTheme {
                val navController = rememberNavController()
                val currentEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry?.destination?.route

                val hideBottomBarRoutes = setOf<String>(
                    Routes.SPLASH,
                    Routes.LOGIN,
                    Routes.REGISTER
                )
                val showBottomBar = currentRoute != null
                        && currentRoute !in hideBottomBarRoutes
                        && !currentRoute.startsWith("edit_transaction/")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}