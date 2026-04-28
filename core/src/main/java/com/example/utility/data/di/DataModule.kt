package com.example.utility.data.di

import com.example.utility.data.FoodRepository
import com.example.utility.data.FoodRepositoryImpl
import com.example.utility.data.PreferencesRepository
import com.example.utility.data.PreferencesRepositoryImpl
import com.example.utility.data.SessionMetricsRepository
import com.example.utility.data.SessionMetricsRepositoryImpl
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