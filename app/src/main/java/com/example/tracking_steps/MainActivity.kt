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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.tracking_steps.ui.theme.TrackingStepsTheme
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Device
import com.example.feature_home.Home
import com.example.utility.WriteStepsService
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject
import androidx.health.connect.client.records.metadata.Metadata

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var writeStepsService: WriteStepsService

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val isStepTrackingAvailable =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                    SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 20

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
                Log.i("Tag", "Granted")
            } else {
                Log.i("Tag", "Not Granted")
            }
        }

        suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (granted.containsAll(PERMISSIONS)) {
                Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
                requestPermissions.launch(PERMISSIONS)
            }
        }

        setContent {
            TrackingStepsTheme {

                LaunchedEffect(Unit) {
                    val healthConnectClient = HealthConnectClient.getOrCreate(this@MainActivity)
                    checkPermissionsAndRun(healthConnectClient)
                    val startTime = Instant.now().minusSeconds(3600)
                    val endTime = Instant.now()

                    val startOffset = ZoneOffset.systemDefault().rules.getOffset(startTime)
                    val endOffset = ZoneOffset.systemDefault().rules.getOffset(endTime)

                    val metadata = Metadata.autoRecorded(
                        device = Device(
                            manufacturer = Build.MANUFACTURER,
                            model = Build.MODEL,
                            type = Device.TYPE_PHONE
                        )
                    )

                    val response = writeStepsService.writeStepsData(
                        startTime = startTime,
                        endTime = endTime,
                        startZoneOffset = startOffset,
                        endZoneOffset = endOffset,
                        metadata = metadata
                    )
                    Log.i("Tag", response.toString())
                }

                Home(modifier = Modifier)
            }
        }
    }
}
