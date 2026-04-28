package com.example.utility.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import javax.inject.Inject

class StepSensorListener @Inject constructor(): SensorEventListener {

    var onActiveStepDetected: () -> Unit = {}
    var onTotalStepCountChanged: (Int) -> Unit = {}

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                onActiveStepDetected()
            }

            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val totalStepsSinceReboot = event.values[0].toInt()
                onTotalStepCountChanged(totalStepsSinceReboot)
            }
        }
    }
}
