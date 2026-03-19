package com.example.utility

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemSensorProvider @Inject constructor(
    @ApplicationContext private val context: Context
): SensorProvider {
    override fun registerListener(listener: StepSensorListener) {
        TODO("Not yet implemented")
    }

    override fun unregisterListener() {
        TODO("Not yet implemented")
    }
}