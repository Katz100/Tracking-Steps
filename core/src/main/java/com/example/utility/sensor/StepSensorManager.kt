package com.example.utility.sensor

import androidx.activity.result.contract.ActivityResultContracts
import com.example.utility.activity_checker.ActivityRecognitionChecker
import timber.log.Timber
import javax.inject.Inject

class StepSensorManager @Inject constructor(
    private val sensorProvider: SensorProvider,
    private val activityRecognitionChecker: ActivityRecognitionChecker,
    private val stepSensorListener: StepSensorListener,
) {
    companion object {
        val requestPermissionsForSteps = ActivityResultContracts.RequestPermission()
    }

    var onActiveStepDetected: () -> Unit = {}
    var onTotalStepCountChanged: (Int) -> Unit = {}
    var onRequestActivityRecognitionPermission: () -> Unit = {}

    fun registerListener() {
        stepSensorListener.onTotalStepCountChanged = onTotalStepCountChanged
        stepSensorListener.onActiveStepDetected = onActiveStepDetected

        if (activityRecognitionChecker.isActivityRecognitionGranted()) {
            sensorProvider.registerListener(stepSensorListener)
            Timber.i("Permissions have been granted and sensor has been registered")
        } else {
            Timber.i("Attempted to register step sensor when permissions are not granted")
            onRequestActivityRecognitionPermission()
        }
    }

    fun unregisterListener() {
        Timber.i("Unregistering listener for step sensor")
        sensorProvider.unregisterListener(stepSensorListener)
    }
}
