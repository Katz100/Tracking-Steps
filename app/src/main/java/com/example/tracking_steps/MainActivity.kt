package com.example.tracking_steps

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var writeStepsService: HealthConnectService

    var cancelled = 0

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isStepTrackingAvailable = SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 20

        val isAvailable = HealthConnectClient.getSdkStatus(this)

        // Create the permissions launcher
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

        // Create a set of permissions for required data types
        val PERMISSIONS =
            setOf(
                HealthPermission.getReadPermission(StepsRecord::class),
                HealthPermission.getWritePermission(StepsRecord::class)
            )

        val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                Log.i("MainActivity", "Permissions granted")
            } else {

            }
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
                                    val availability = HealthConnectClient.getSdkStatus(this@MainActivity)
                                    Log.i("MainActivity", "Health Connect availability: $availability")
                                    Log.i("MainActivity", "Permissions to be requested: ${PERMISSIONS}")
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
