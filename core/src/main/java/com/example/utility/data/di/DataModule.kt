package com.example.utility.data.di

import com.example.utility.data.repository.FoodRepository
import com.example.utility.data.repository.FoodRepositoryImpl
import com.example.utility.data.repository.PreferencesRepository
import com.example.utility.data.repository.PreferencesRepositoryImpl
import com.example.utility.data.repository.SessionMetricsRepository
import com.example.utility.data.repository.SessionMetricsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository

    @Singleton
    @Binds
    fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository

    @Singleton
    @Binds
    fun bindSessionMetricsRepository(impl: SessionMetricsRepositoryImpl): SessionMetricsRepository
}