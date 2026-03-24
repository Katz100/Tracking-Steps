package com.example.utility.health_connect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.metadata.Device
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Mass
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

class HealthConnectService @Inject constructor(
    private val healthConnectClient: HealthConnectClient
) {

    companion object {
        val PERMISSIONS =
            setOf(
                HealthPermission.Companion.getReadPermission(StepsRecord::class),
                HealthPermission.Companion.getWritePermission(StepsRecord::class),
                HealthPermission.Companion.getReadPermission(WeightRecord::class),
                HealthPermission.Companion.getWritePermission(WeightRecord::class),
                HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
                HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class),
            )

        fun isActivityRecognitionGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        }

        val requestPermissionsForHealthConnect = PermissionController.Companion.createRequestPermissionResultContract()
    }

    suspend fun hasAllPermissions(
    ): Boolean {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        return granted.containsAll(PERMISSIONS)
    }

    suspend fun writeStepsData(
        startTime: Instant,
        endTime: Instant,
        startZoneOffset: ZoneOffset = ZoneOffset.UTC,
        endZoneOffset: ZoneOffset = ZoneOffset.UTC,
        metadata: Metadata = Metadata.autoRecorded(
            device = Device(type = Device.TYPE_PHONE)
        ),
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
                    metrics = setOf(StepsRecord.Companion.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.Companion.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            response[StepsRecord.Companion.COUNT_TOTAL]
        } catch (e: Exception) {
            onError(e.message.toString())
            null
        }
    }

    suspend fun readCaloriesAggregate(
        startTime: Instant,
        endTime: Instant,
        onError: (String) -> Unit
    ): Energy? {
        return try {
            val response = healthConnectClient.aggregate(
                AggregateRequest(
                    metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                    timeRangeFilter = TimeRangeFilter.Companion.between(startTime, endTime)
                )
            )
            // The result may be null if no data is available in the time range
            response[TotalCaloriesBurnedRecord.ENERGY_TOTAL]
        } catch (e: Exception) {
            onError(e.message.toString())
            null
        }
    }

    suspend fun writeWeightInput(weightInput: Double): InsertRecordsResponse? {
        val time = ZonedDateTime.now().withNano(0)
        val weightRecord = WeightRecord(
            metadata = Metadata.Companion.manualEntry(),
            weight = Mass.Companion.pounds(weightInput),
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

    suspend fun writeCaloriesBurned(
        calories: Double,
        startTime: Instant,
        endTime: Instant,
    ): InsertRecordsResponse? {
        val time = ZonedDateTime.now().withNano(0)

        val caloriesRecord = TotalCaloriesBurnedRecord(
            startTime = startTime,
            endTime = endTime,
            startZoneOffset = time.offset,
            endZoneOffset = null,
            energy = Energy.calories(calories),
            metadata = Metadata.manualEntry()
        )
        val records = listOf(caloriesRecord)
        return try {
            healthConnectClient.insertRecords(records)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun readExerciseSessions(start: Instant, end: Instant): List<ExerciseSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = ExerciseSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.Companion.between(start, end)
        )
        val response = healthConnectClient.readRecords(request)
        return response.records
    }
}
