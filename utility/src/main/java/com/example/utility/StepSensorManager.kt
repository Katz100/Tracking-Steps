package com.example.utility

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StepSensorManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stepSensorListener: StepSensorListener,
){
    companion object {
        const val TAG = "StepSensorManager"
    }

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    fun registerListener() {
        sensorManager.registerListener(stepSensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        sensorManager.unregisterListener(stepSensorListener)
    }
}