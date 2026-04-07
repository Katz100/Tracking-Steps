package com.example.tracking_steps.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tracking_steps.data.FoodEntity
import com.example.tracking_steps.firebase.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    fun insertAll(vararg foodItems: FoodEntity)

    @Delete
    fun deleteFoodItem(foodItem: FoodEntity)

   @Query("SELECT * FROM food_item WHERE date(date_added / 1000, 'unixepoch') = date('now')")
   fun getFoodItemsCreatedToday(): Flow<List<FoodEntity>>
}