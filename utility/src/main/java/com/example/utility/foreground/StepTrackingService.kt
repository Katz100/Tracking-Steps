package com.example.utility.foreground

import android.Manifest
import android.R
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
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

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            TODO("Not yet implemented")
        }

    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
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
            currentSteps++
            updateNotification(
                this,
                currentSteps,
                stepGoal
            )
            if (currentSteps >= stepGoal) {
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
        if (intent == null) {
            Timber.i("Received intent is null so not continuing foreground service")
            return START_STICKY
        }

        currentSteps = intent.getIntExtra("steps", 0)
        stepGoal = intent.getIntExtra("goal", 0)

        try {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step tracking active")
                .setContentText("Tracking steps...")
                .setSmallIcon(R.drawable.ic_dialog_info)
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
            if (e is ForegroundServiceStartNotAllowedException) {
                Timber.e("Notification not available: $e")
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
        }

        return START_STICKY
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun updateNotification(
        context: Context,
        currentSteps: Int,
        stepGoal: Int,
    ) {
        val notificationManager = NotificationManagerCompat.from(context)

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val updatedNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Steps")
                .setSmallIcon(R.drawable.ic_dialog_info)
                .setContentText("$currentSteps/$stepGoal steps completed")
                .build()
            notificationManager.notify(1, updatedNotification)
        } else {
            Timber.e("Unable to update notification due to permissions not being granted")
        }
    }
}
