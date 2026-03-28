package com.example.tracking_steps

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.tracking_steps.ui.theme.TrackingStepsTheme
import androidx.health.connect.client.records.metadata.Device
import com.example.feature_home.Home
import com.example.utility.health_connect.HealthConnectService
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject
import androidx.health.connect.client.records.metadata.Metadata
import androidx.lifecycle.lifecycleScope
import com.example.utility.sensor.StepSensorManager
import kotlinx.coroutines.launch
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.utility.foreground.StepCountProvider
import com.example.utility.foreground.StepTrackingService
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var writeStepsService: HealthConnectService

    @Inject
    lateinit var dataStore: DataStore

    val viewModel: MainViewModel by viewModels()

    val requestPermissions = registerForActivityResult(HealthConnectService.requestPermissionsForHealthConnect) { granted ->
        if (granted.containsAll(HealthConnectService.PERMISSIONS)) {
            Timber.i("Permission has been granted for Health Connect")
        } else {
            Timber.i("Permissions have been denied for Health Connect")
        }
    }

    private val activityRecognitionPermissionLauncher = registerForActivityResult(StepSensorManager.requestPermissionsForSteps) { granted ->
            if (granted) {
                Timber.i("Permissions have been granted for step sensor")
            } else {
                Timber.i("Permissions have been denied for step sensor")
            }
        }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Timber.i("Permissions for notifications granted")
        } else {
            Timber.i("Permissions for notification denied")
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel.onSessionCompleted = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "I completed $it steps!")
                type = "text/plain"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        createNotificationChannel(this)
        activityRecognitionPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)

        setContent {

            val stepsCounter = viewModel.stepsCounter.collectAsStateWithLifecycle().value
            val goal = viewModel.goal.collectAsStateWithLifecycle().value
            val currentGoal = viewModel.currentGoal.collectAsStateWithLifecycle().value
            val weightTxt = viewModel.weightTxt.collectAsStateWithLifecycle().value

            LaunchedEffect(Unit) {
                dataStore.weightFlow().collect {
                    Timber.i("Collecting ${it.toString()}")
                }
            }

            TrackingStepsTheme {
                Scaffold(
                    modifier = Modifier
                ) { innerPadding ->
                    Home(
                        modifier = Modifier.padding(innerPadding),
                        steps = stepsCounter,
                        onRequestPermissions = {
                            lifecycleScope.launch {
                                if (!writeStepsService.hasAllPermissions()) {
                                    requestPermissions.launch(HealthConnectService.PERMISSIONS)
                                } else {
                                    Toast.makeText(
                                        this@MainActivity, "Permissions already granted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        onWriteSteps = { steps ->
                            lifecycleScope.launch {
                                val metadata = Metadata.autoRecorded(
                                    device = Device(
                                        manufacturer = Build.MANUFACTURER,
                                        model = Build.MODEL,
                                        type = Device.TYPE_PHONE
                                    )
                                )

                                val startTime = Instant.now().minusSeconds(3600)
                                val endTime = Instant.now()
                                Timber.d(endTime.toString())

                                val startOffset =
                                    ZoneOffset.systemDefault().rules.getOffset(startTime)
                                val endOffset = ZoneOffset.systemDefault().rules.getOffset(endTime)

                                if (writeStepsService.hasAllPermissions()) {
                                    val response = writeStepsService.writeStepsData(
                                        startTime = startTime,
                                        endTime = endTime,
                                        startZoneOffset = startOffset,
                                        endZoneOffset = endOffset,
                                        metadata = metadata,
                                        countOfSteps = steps
                                    )
                                    Toast.makeText(
                                        this@MainActivity,
                                        response?.recordIdsList.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Permission for steps must be granted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        goal = currentGoal,
                        launchForeground = {
                            val intent = Intent(this@MainActivity, StepTrackingService::class.java).apply {
                                putExtra("steps", 0)
                                putExtra("goal", goal.toIntOrNull() ?: 100)
                                putExtra("weight", weightTxt.toIntOrNull() ?: 180)
                            }
                            lifecycleScope.launch {
                                dataStore.setNewWeight(weightTxt.toIntOrNull() ?: 180)
                            }
                            startForegroundService(intent)
                        },
                        stopForeground = {
                            stopService(Intent(this@MainActivity, StepTrackingService::class.java))
                        },
                        requestForeground = {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        },
                        onGoalChange = { viewModel.onGoalChange(it) },
                        goalValue = goal,
                        weightValue = weightTxt,
                        onWeightChange = { viewModel.onWeightChange(it) }
                    )
                }
            }
        }
    }
}

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    val channel = NotificationChannel(
        "steps",
        "Steps",
        NotificationManager.IMPORTANCE_LOW // not DEFAULT
    ).apply { description = "Steps running in background" }
    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(channel)
}
