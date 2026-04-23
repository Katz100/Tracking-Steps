package com.example.utility.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.utility.data.db.FoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    fun insertAll(vararg foodItems: FoodEntity)

    @Delete
    fun deleteFoodItem(foodItem: FoodEntity)

   @Query("""
    SELECT *
    FROM food_item
    WHERE date_added >= strftime('%s','now') * 1000 - 86400000
    ORDER BY date_added DESC
""")
   fun getFoodItemsCreatedToday(): Flow<List<FoodEntity>>

   @Query("SELECT * FROM food_item")
   fun getAllFoodItems(): Flow<List<FoodEntity>>
}