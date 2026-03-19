package com.example.utility

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class StepSensorManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stepSensorListener: StepSensorListener,
): DefaultLifecycleObserver {
    companion object {
        val requestPermissionsForSteps = ActivityResultContracts.RequestPermission()
    }

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    var onActiveStepDetected: () -> Unit = {}
    var onTotalStepCountChanged: (Int) -> Unit = {}
    var onRequestActivityRecognitionPermission: () -> Unit = {}

    override fun onResume(owner: LifecycleOwner) {
        if (HealthConnectService.isActivityRecognitionGranted(context)) {
            Timber.i("Registering listener for steps")
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
        Timber.i("Unregistering listener for steps")
        unregisterListener()
    }

    fun registerListener(
        onActiveStepDetected: () -> Unit,
        onTotalStepCountChanged: (Int) -> Unit,
    ) {
        stepSensorListener.onTotalStepCountChanged = onTotalStepCountChanged
        stepSensorListener.onActiveStepDetected = onActiveStepDetected
        sensorManager.registerListener(stepSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        sensorManager.unregisterListener(stepSensorListener)
    }
}