package com.example.utility

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StepSensorManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stepSensorListener: StepSensorListener,
): DefaultLifecycleObserver {
    companion object {
        const val TAG = "StepSensorManager"
    }

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    var onActiveStepDetected: () -> Unit = {}
    var onTotalStepCountChanged: (Int) -> Unit = {}
    var onRequestActivityRecognitionPermission: () -> Unit = {}

    override fun onResume(owner: LifecycleOwner) {
        if (HealthConnectService.isActivityRecognitionGranted(context)) {
            Log.i(TAG, "Registering listener for steps")
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
        Log.i(TAG, "Unregistering listener for steps")
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