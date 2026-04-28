package com.example.utility.service.di

import com.example.utility.service.activity_checker.ActivityRecognitionChecker
import com.example.utility.service.activity_checker.SystemActivityRecognitionChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SensorModule {
    @Binds
    abstract fun bindActivityChecker(impl: SystemActivityRecognitionChecker): ActivityRecognitionChecker
}