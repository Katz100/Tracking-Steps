package com.example.utility

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemActivityRecognitionChecker @Inject constructor(
    @ApplicationContext private val context: Context,
): ActivityRecognitionChecker {
    override fun isActivityRecognitionGranted(): Boolean {
        return HealthConnectService.isActivityRecognitionGranted(context)
    }
}