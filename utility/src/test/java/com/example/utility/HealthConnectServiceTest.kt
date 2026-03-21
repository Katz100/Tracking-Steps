package com.example.utility

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import com.example.utility.health_connect.HealthConnectService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HealthConnectServiceTest {
    private lateinit var healthConnectClient: HealthConnectClient
    private lateinit var healthConnectService: HealthConnectService

    @Before
    fun setUp() {
        healthConnectClient = mockk<HealthConnectClient>(relaxed = true)
        healthConnectService = HealthConnectService(healthConnectClient)
    }

    @Test
    fun `hasAllPermissions returns true when all permissions are granted`() = runTest {
        coEvery { healthConnectClient.permissionController.getGrantedPermissions() } returns setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class),
            HealthPermission.getReadPermission(WeightRecord::class),
            HealthPermission.getWritePermission(WeightRecord::class),
        )

        assertTrue(healthConnectService.hasAllPermissions())
    }

    @Test
    fun `hasAllPermissions returns false when all permissions are not granted`() = runTest {
        coEvery { healthConnectClient.permissionController.getGrantedPermissions() } returns setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class),
            HealthPermission.getReadPermission(WeightRecord::class),
        )

        assertTrue(!healthConnectService.hasAllPermissions())
    }

}