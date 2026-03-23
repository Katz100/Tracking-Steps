package com.example.utility.foreground

import android.Manifest
import com.example.utility.R
import android.app.ForegroundServiceStartNotAllowedException
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.example.utility.health_connect.HealthConnectService
import com.example.utility.sensor.StepSensorManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class StepTrackingService : Service() {

    private companion object {
        const val CHANNEL_ID = "steps"
        const val NOTIFICATION_ID = 100
        const val ACTION_STOP_SESSION = "com.example.utility.foreground.ACTION_STOP_SESSION"
    }

    @Inject
    lateinit var stepSensorManager: StepSensorManager

    @Inject
    lateinit var healthConnectService: HealthConnectService

    lateinit var pendingIntent: PendingIntent
    lateinit var startTime: Instant
    lateinit var notificationLayout: RemoteViews
    lateinit var notificationLayoutExpanded: RemoteViews

    var currentSteps = -1
    var stepGoal = -1

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onCreate() {
        super.onCreate()

        notificationLayout = RemoteViews(packageName, R.layout.small)
        notificationLayoutExpanded = RemoteViews(packageName, R.layout.large)

        val stopServiceIntent = Intent(this, StepTrackingService::class.java).apply {
            action = ACTION_STOP_SESSION
        }

        val flag = PendingIntent.FLAG_IMMUTABLE

        pendingIntent = PendingIntent.getService(
            this,
            0,
            stopServiceIntent,
            flag
        )

        stepSensorManager.onActiveStepDetected = {
            Timber.i("Active step detected")
        }

        stepSensorManager.onRequestActivityRecognitionPermission = {
            Timber.i("Permissions requested for recognition")
            stopSelf()
        }

        stepSensorManager.onTotalStepCountChanged = {
            currentSteps++
            StepCountProvider.updateCurrentSteps(currentSteps)

            notificationLayoutExpanded.setTextViewText(
                R.id.steps_counter,
                "${currentSteps}/${stepGoal}"
            )

            notificationLayoutExpanded.setProgressBar(
                R.id.progress_indicator,
                stepGoal,
                currentSteps,
                false
            )
            updateNotification(
                this,
                pendingIntent,
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
        /* Maybe should use work manager to write steps/or uncomment below to just write steps for sessions */

//        scope.launch {
//            Timber.i("Writing steps to Health Connect")
//            if (healthConnectService.hasAllPermissions()) {
//                val recordResponse = healthConnectService.writeStepsData(
//                    startTime = startTime,
//                    endTime = Instant.now(),
//                    countOfSteps = currentSteps.toLong()
//                ) {
//                    Timber.e("There was an error writing steps to Health Connect: $it")
//                }
//                if (recordResponse != null) {
//                    Timber.i("Successfully wrote steps to Health Connect")
//                }
//            } else {
//                Timber.i("Unable to write steps to Health Connect due to correct permissions not being granted")
//            }
//        }
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

        if (intent.action == ACTION_STOP_SESSION) {
            Timber.i("User has ended session from notification, stopping service...")
            stopSelf()
            return START_STICKY
        }

        startTime = Instant.now()
        currentSteps = intent.getIntExtra("steps", 0)
        stepGoal = intent.getIntExtra("goal", 0)
        StepCountProvider.updateCurrentGoal(stepGoal)

        try {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step tracking active")
                .setContentText("Tracking steps...")
                .setSmallIcon(R.drawable.ic_launcher_background)
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
        pendingIntent: PendingIntent,
    ) {
        val notificationManager = NotificationManagerCompat.from(context)

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val updatedNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Steps")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .addAction(0, "End Session", pendingIntent)
                .build()
            notificationManager.notify(NOTIFICATION_ID, updatedNotification)
        } else {
            Timber.e("Unable to update notification due to permissions not being granted")
        }
    }
}
