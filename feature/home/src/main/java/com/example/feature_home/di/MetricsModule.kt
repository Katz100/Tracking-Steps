package com.example.feature_home.di

import com.example.feature_home.data.MetricsRepository
import com.example.feature_home.data.MetricsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MetricsModule {
    @Binds
    abstract fun bindMetricsRepository(impl: MetricsRepositoryImpl): MetricsRepository
}