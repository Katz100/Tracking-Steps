package com.example.utility

interface SensorProvider {
    fun registerListener(listener: StepSensorListener)
    fun unregisterListener(listener: StepSensorListener)
}