package com.example.utility.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HealthConnectModule {

    @Singleton
    @Provides
    fun provideHealthConnectClient(
        @ApplicationContext context: Context
    ): HealthConnectClient {
        return HealthConnectClient.Companion.getOrCreate(context)
    }
}