package com.example.utility.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.utility.data.db.Converters
import com.example.utility.data.db.FoodEntity

@Database(entities = [FoodEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class FoodDatabase: RoomDatabase() {
    abstract fun foodDao(): FoodDao
}