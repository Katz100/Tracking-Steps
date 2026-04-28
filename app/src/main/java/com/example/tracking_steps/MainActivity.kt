package com.example.tracking_steps

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.tracking_steps.ui.theme.TrackingStepsTheme
import com.example.utility.service.health_connect.HealthConnectService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import com.example.utility.service.sensor.StepSensorManager
import kotlinx.coroutines.launch
import android.Manifest
import android.app.ComponentCaller
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import com.example.tracking_steps.firebase.GeminiModel
import com.example.tracking_steps.nav.Nav
import com.example.utility.data.repository.FoodRepository
import com.example.utility.service.foreground.StepCountProvider
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var writeStepsService: HealthConnectService

    @Inject
    lateinit var model: GeminiModel

    @Inject
    lateinit var foodRepository: FoodRepository

    val requestPermissions =
        registerForActivityResult(HealthConnectService.requestPermissionsForHealthConnect) { granted ->
            if (granted.containsAll(HealthConnectService.PERMISSIONS)) {
                Timber.i("Permission has been granted for Health Connect")
            } else {
                Timber.i("Permissions have been denied for Health Connect")
            }
        }

    private val activityRecognitionPermissionLauncher =
        registerForActivityResult(StepSensorManager.requestPermissionsForSteps) { granted ->
            if (granted) {
                Timber.i("Permissions have been granted for step sensor")
            } else {
                Timber.i("Permissions have been denied for step sensor")
            }
        }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakeImageIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Timber.e(e, "No app available to handle ACTION_VIDEO_CAPTURE")
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            lifecycleScope.launch {
                val response = model.generateContentFromImage(imageBitmap)
                Timber.d("Response: $response")
                Toast.makeText(
                    this@MainActivity,
                    "Response: ${response.toString()}",
                    Toast.LENGTH_LONG
                ).show()
                StepCountProvider.increaseCaloriesConsumed(response?.calories ?: 0)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel(this)
        activityRecognitionPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        setContent {
            TrackingStepsTheme {
                Nav(
                    onLogFoodClicked = {
                        dispatchTakeImageIntent()
                    },
                )
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
