package com.example.utility

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemSensorProvider @Inject constructor(
    @ApplicationContext private val context: Context
): SensorProvider {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    override fun registerListener(listener: StepSensorListener) {
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun unregisterListener(listener: StepSensorListener) {
        sensorManager.unregisterListener(listener)
    }
}

class SystemSensorProviderFake: SensorProvider {
    override fun registerListener(listener: StepSensorListener) {
        listener.onTotalStepCountChanged
    }

    override fun unregisterListener(listener: StepSensorListener) {

    }
}