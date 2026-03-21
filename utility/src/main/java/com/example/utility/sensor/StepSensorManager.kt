package com.example.utility.sensor

import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.utility.activity_checker.ActivityRecognitionChecker
import timber.log.Timber
import javax.inject.Inject

class StepSensorManager @Inject constructor(
    private val sensorProvider: SensorProvider,
    private val activityRecognitionChecker: ActivityRecognitionChecker,
    private val stepSensorListener: StepSensorListener,
): DefaultLifecycleObserver {
    companion object {
        val requestPermissionsForSteps = ActivityResultContracts.RequestPermission()
    }

    var onActiveStepDetected: () -> Unit = {}
    var onTotalStepCountChanged: (Int) -> Unit = {}
    var onRequestActivityRecognitionPermission: () -> Unit = {}

    override fun onResume(owner: LifecycleOwner) {
        if (activityRecognitionChecker.isActivityRecognitionGranted()) {
            Timber.Forest.i("Registering listener for steps")
            registerListener(
                onActiveStepDetected,
                onTotalStepCountChanged,
            )
        } else {
            onRequestActivityRecognitionPermission()
        }
        super.onResume(owner)

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.Forest.i("Unregistering listener for steps")
        unregisterListener()
    }

    fun registerListener(
        onActiveStepDetected: () -> Unit,
        onTotalStepCountChanged: (Int) -> Unit,
    ) {
        stepSensorListener.onTotalStepCountChanged = onTotalStepCountChanged
        stepSensorListener.onActiveStepDetected = onActiveStepDetected
        sensorProvider.registerListener(stepSensorListener)
    }

    fun unregisterListener() {
        sensorProvider.unregisterListener(stepSensorListener)
    }
}