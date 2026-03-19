package com.example.utility

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Mass
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

class HealthConnectService @Inject constructor(
    private val healthConnectClient: HealthConnectClient
) {

    companion object {
        const val TAG = "HealthConnectService"

        val PERMISSIONS =
            setOf(
                HealthPermission.getReadPermission(StepsRecord::class),
                HealthPermission.getWritePermission(StepsRecord::class),
                HealthPermission.getReadPermission(WeightRecord::class),
                HealthPermission.getWritePermission(WeightRecord::class),
            )

        fun isActivityRecognitionGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        }

        val requestPermissionsForHealthConnect = PermissionController.createRequestPermissionResultContract()
    }

    suspend fun hasAllPermissions(
    ): Boolean {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        return granted.containsAll(PERMISSIONS)
    }

    suspend fun writeStepsData(
        startTime: Instant,
        endTime: Instant,
        startZoneOffset: ZoneOffset,
        endZoneOffset: ZoneOffset,
        metadata: Metadata,
        countOfSteps: Long,
        onError: (String) -> Unit = {}
    ): InsertRecordsResponse? {
        return try {
            val stepsRecord = StepsRecord(
                startTime = startTime,
                startZoneOffset = startZoneOffset,
                endTime = endTime,
                endZoneOffset = endZoneOffset,
                count = countOfSteps,
                metadata = metadata
            )
            return healthConnectClient.insertRecords(listOf(stepsRecord))
        } catch (e: Exception) {
            onError(e.message.toString())
            null
        }
    }

    suspend fun readStepsAggregate(
        startTime: Instant,
        endTime: Instant,
        onError: (String) -> Unit
    ): Long? {
        return try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            response[StepsRecord.COUNT_TOTAL]
        } catch (e: Exception) {
            onError(e.message.toString())
            null
        }
    }

    suspend fun writeWeightInput(weightInput: Double): InsertRecordsResponse? {
        val time = ZonedDateTime.now().withNano(0)
        val weightRecord = WeightRecord(
            metadata = Metadata.manualEntry(),
            weight = Mass.pounds(weightInput),
            time = time.toInstant(),
            zoneOffset = time.offset
        )
        val records = listOf(weightRecord)
        return try {
            healthConnectClient.insertRecords(records)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun readExerciseSessions(start: Instant, end: Instant): List<ExerciseSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = ExerciseSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }
}