package com.example.utility.data.di

import com.example.utility.data.MetricsRepository
import com.example.utility.data.MetricsRepositoryImpl
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