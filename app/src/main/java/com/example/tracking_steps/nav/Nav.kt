package com.example.tracking_steps.nav

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.feature_home.HomePage
import com.example.utility.composables.ValueStepper
import com.example.utility.foreground.StepTrackingService
import timber.log.Timber

@Composable
fun Nav(
    onLogFoodClicked: () -> Unit,
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(destination.screen::class)
                    } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.screen) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                HomePage(
                    onLogFoodClicked = onLogFoodClicked,
                    onStartWalkClicked = { weight ->
                        val intent =
                            Intent(context, StepTrackingService::class.java).apply {
                                putExtra("steps", 0)
                                putExtra("goal", 0) // todo
                                putExtra("weight", weight)
                            }
                        Timber.d("weight: $weight")
                        context.startForegroundService(intent)
                    }
                )
            }
            composable<Screen.Goals> {
                ValueStepper(
                    modifier = Modifier.fillMaxWidth()
                        .height(100.dp)
                        .padding(16.dp),
                    onPlusIconClicked = {},
                    onSubtractIconClicked = {},
                    value = 2000
                )
            }
            composable<Screen.Settings> {
               Text("Settings")
            }
        }
    }
}