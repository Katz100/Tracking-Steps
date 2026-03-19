package com.example.utility

import android.content.Context
import android.hardware.SensorManager

interface SensorProvider {
    fun registerListener(listener: StepSensorListener)
    fun unregisterListener()
}