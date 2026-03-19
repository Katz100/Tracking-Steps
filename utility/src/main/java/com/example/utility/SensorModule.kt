package com.example.utility

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SensorModule {
    @Binds
    abstract fun bindSystemSensor(impl: SystemSensorProvider): SensorProvider

    @Binds
    abstract fun bindActivityChecker(impl: SystemActivityRecognitionChecker): ActivityRecognitionChecker
}
