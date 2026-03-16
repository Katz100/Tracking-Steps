package com.example.tracking_steps

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.tracking_steps.ui.theme.TrackingStepsTheme
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Device
import com.example.feature_home.Home
import com.example.utility.HealthConnectService
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject
import androidx.health.connect.client.records.metadata.Metadata
import androidx.lifecycle.lifecycleScope
import com.example.utility.StepSensorListener
import com.example.utility.StepSensorManager
import kotlinx.coroutines.launch
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var writeStepsService: HealthConnectService

    @Inject
    lateinit var stepSensorListener: StepSensorListener

    @Inject
    lateinit var stepSensorManager: StepSensorManager

    val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
    val requestPermissionForActivity = ActivityResultContracts.RequestPermission()

    val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
        if (granted.containsAll(HealthConnectService.PERMISSIONS)) {
            Log.i("MainActivity", "Permissions granted")
        } else {
            Log.i("MainActivity", "Permissions not granted")
        }
    }

    private val activityRecognitionPermissionLauncher = registerForActivityResult(requestPermissionForActivity) { granted ->
            if (granted) {
                stepSensorManager.registerListener()
            } else {
                Log.i("MainActivity", "Permission denied")
            }
        }

    override fun onResume() {
        super.onResume()
        if (HealthConnectService.isActivityRecognitionGranted(this)) {
            stepSensorManager.registerListener()
        } else {
            activityRecognitionPermissionLauncher.launch(
                Manifest.permission.ACTIVITY_RECOGNITION
            )
        }
    }

    override fun onPause() {
        super.onPause()
        stepSensorManager.unregisterListener()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        stepSensorListener.onStepDetected = {
            Log.i("MainActivity", "A step has been made")
        }

        setContent {
            TrackingStepsTheme {
                Scaffold(
                    modifier = Modifier
                ) { innerPadding ->
                    Home(
                        modifier = Modifier.padding(innerPadding),
                        onRequestPermissions = {
                            lifecycleScope.launch {
                                if (!writeStepsService.hasAllPermissions()) {
                                    requestPermissions.launch(HealthConnectService.PERMISSIONS)
                                } else {
                                    Toast.makeText(this@MainActivity, "Permissions already granted",
                                        Toast.LENGTH_SHORT).show()
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

                                val startOffset = ZoneOffset.systemDefault().rules.getOffset(startTime)
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
                                    Toast.makeText(this@MainActivity, response?.recordIdsList.toString(), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@MainActivity, "Permission for steps must be granted", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
