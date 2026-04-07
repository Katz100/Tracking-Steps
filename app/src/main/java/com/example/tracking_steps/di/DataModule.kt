package com.example.tracking_steps.di

import com.example.tracking_steps.data.FoodRepository
import com.example.tracking_steps.data.FoodRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Singleton
    @Binds
    fun bindFoodRepository(impl: FoodRepositoryImpl): FoodRepository
}