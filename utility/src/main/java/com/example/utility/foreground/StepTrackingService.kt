package com.example.utility.foreground

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.utility.sensor.StepSensorManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StepTrackingService: Service() {

    private companion object {
        const val CHANNEL_ID = "steps"
        const val NOTIFICATION_ID = 100
    }

    @Inject
    lateinit var stepSensorManager: StepSensorManager

    var currentSteps = -1
    var stepGoal = -1

    override fun onCreate() {
        super.onCreate()

        stepSensorManager.onActiveStepDetected = {
            Timber.i("Active step detected")
        }

        stepSensorManager.onRequestActivityRecognitionPermission = {
            Timber.i("Permissions requested for recognition")
            stopSelf()
        }

        stepSensorManager.onTotalStepCountChanged = {
            Timber.d("Step count changed: $it")
            if (it > stepGoal) {
                Timber.i("Step goal has been met, ending service...")
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stepSensorManager.unregisterListener()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentSteps = intent?.getIntExtra("current", 0) ?: 0
        stepGoal = intent?.getIntExtra("goal", 0) ?: 0

        try {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step tracking active")
                .setContentText("Tracking steps...")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOngoing(true)
                .build()

            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
            )
            stepSensorManager.registerListener()
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
        }

        return START_STICKY
    }
}
