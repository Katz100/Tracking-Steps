package com.example.utility

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.response.InsertRecordsResponse
import java.time.Instant
import java.time.ZoneOffset
import javax.inject.Inject

class WriteStepsService @Inject constructor(
    private val healthConnectClient: HealthConnectClient
) {
    suspend fun writeStepsData(
        startTime: Instant,
        endTime: Instant,
        startZoneOffset: ZoneOffset,
        endZoneOffset: ZoneOffset,
        metadata: Metadata,
    ): InsertRecordsResponse {
        val stepsRecord = StepsRecord(
            startTime = startTime,
            startZoneOffset = startZoneOffset,
            endTime = endTime,
            endZoneOffset = endZoneOffset,
            count = 1000,
            metadata = metadata
        )
        return healthConnectClient.insertRecords(listOf(stepsRecord))
    }
}