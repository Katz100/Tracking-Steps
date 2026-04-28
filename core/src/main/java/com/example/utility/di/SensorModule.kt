package com.example.utility.di

import com.example.utility.activity_checker.ActivityRecognitionChecker
import com.example.utility.sensor.SensorProvider
import com.example.utility.activity_checker.SystemActivityRecognitionChecker
import com.example.utility.sensor.SystemSensorProvider
import com.example.utility.sensor.SystemSensorProviderEmulator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SensorModule {
    @Binds
    abstract fun bindActivityChecker(impl: SystemActivityRecognitionChecker): ActivityRecognitionChecker
}