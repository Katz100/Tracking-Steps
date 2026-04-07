package com.example.utility.data.di

import com.example.utility.data.FoodRepository
import com.example.utility.data.FoodRepositoryImpl
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
}