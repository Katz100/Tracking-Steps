package com.example.utility

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import javax.inject.Inject

class StepSensorListener @Inject constructor(): SensorEventListener {

    var onStepDetected: () -> Unit = {}

    companion object {
        const val TAG = "StepSensorListener"
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                val stepDetected = it.values[0]
                onStepDetected()
            }
        }
    }
}