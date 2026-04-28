package com.example.utility.sensor

interface SensorProvider {
    fun registerListener(listener: StepSensorListener)
    fun unregisterListener(listener: StepSensorListener)
}
