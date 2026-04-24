package com.example.tracking_steps.nav

import android.content.Intent
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.feature_home.HomePage
import com.example.goals.GoalsPage
import com.example.settings.SettingsPage
import com.example.utility.composables.CustomAlertDialog
import com.example.utility.foreground.SessionState
import com.example.utility.foreground.StepTrackingService
import timber.log.Timber
import com.example.utility.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Nav(
    onLogFoodClicked: () -> Unit,
) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Timber.i("Permissions for notifications granted")
        } else {
            Timber.i("Permissions for notification denied")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                val openAlertDialog = remember { mutableStateOf(false) }
                when {
                    openAlertDialog.value -> {
                        CustomAlertDialog(
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                openAlertDialog.value = false
                                val intent = Intent(context, StepTrackingService::class.java)
                                context.stopService(intent)
                            },
                            dialogTitle = "End Session",
                            dialogText = "Are you sure you want to end your walking session?",
                            icon = painterResource(R.drawable.walking)
                        )
                    }
                }

                HomePage(
                    onLogFoodClicked = onLogFoodClicked,
                    onStartWalkClicked = { weight, stepGoal, sessionState ->
                        if (sessionState == SessionState.STOPPED) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            val intent =
                                Intent(context, StepTrackingService::class.java).apply {
                                    putExtra("steps", 0)
                                    putExtra("goal", stepGoal)
                                    putExtra("weight", weight)
                                }
                            context.startForegroundService(intent)
                        } else {
                            openAlertDialog.value = true
                        }
                    }
                )
            }
            composable<Screen.Goals> {
                GoalsPage()
            }
            composable<Screen.Settings> {
                SettingsPage()
            }
        }
    }
}