package com.example.utility.service.sensor

interface SensorProvider {
    fun registerListener(listener: StepSensorListener)
    fun unregisterListener(listener: StepSensorListener)
}
