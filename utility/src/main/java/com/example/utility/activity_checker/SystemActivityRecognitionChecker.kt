package com.example.utility.activity_checker

import android.content.Context
import com.example.utility.health_connect.HealthConnectService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SystemActivityRecognitionChecker @Inject constructor(
    @ApplicationContext private val context: Context,
): ActivityRecognitionChecker {
    override fun isActivityRecognitionGranted(): Boolean {
        return HealthConnectService.isActivityRecognitionGranted(context)
    }
}

class SystemActivityRecognitionCheckerFake: ActivityRecognitionChecker {
    var isGranted = true
    override fun isActivityRecognitionGranted(): Boolean {
        return isGranted
    }
}
