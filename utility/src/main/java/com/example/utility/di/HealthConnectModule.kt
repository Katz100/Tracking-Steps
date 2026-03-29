package com.example.utility.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.utility.sensor.SensorProvider
import com.example.utility.sensor.SystemSensorProvider
import com.example.utility.sensor.SystemSensorProviderEmulator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HealthConnectModule {

    const val IS_EMULATOR = false

    @Provides
    @Singleton
    fun provideSensorProvider(
        systemSensorProvider: SystemSensorProvider,
        systemSensorProviderEmulator: SystemSensorProviderEmulator,
    ): SensorProvider {
        return if (IS_EMULATOR) {
            systemSensorProviderEmulator
        } else {
            systemSensorProvider
        }
    }

    @Singleton
    @Provides
    fun provideHealthConnectClient(
        @ApplicationContext context: Context
    ): HealthConnectClient {
        return HealthConnectClient.Companion.getOrCreate(context)
    }
}