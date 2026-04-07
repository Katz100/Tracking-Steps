package com.example.tracking_steps.di

import android.content.Context
import androidx.room.Room
import com.example.tracking_steps.data.Converters
import com.example.tracking_steps.data.FoodRepository
import com.example.tracking_steps.data.FoodRepositoryImpl
import com.example.tracking_steps.data.db.FoodDao
import com.example.tracking_steps.data.db.FoodDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideRoomInstance(
        @ApplicationContext context: Context,
    ): FoodDatabase {
        val db = Room.databaseBuilder(
            context,
            FoodDatabase::class.java, "food"
        )
            .build()
        return db
    }

    @Provides
    fun provideFoodDao(db: FoodDatabase): FoodDao {
        return db.foodDao()
    }
}