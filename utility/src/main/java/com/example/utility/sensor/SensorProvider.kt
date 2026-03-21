package com.example.utility.sensor

import com.example.utility.sensor.StepSensorListener

interface SensorProvider {
    fun registerListener(listener: StepSensorListener)
    fun unregisterListener(listener: StepSensorListener)
}