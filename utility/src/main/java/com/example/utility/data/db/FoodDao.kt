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

   @Query("SELECT *\n" +
           "FROM food_item\n" +
           "WHERE date_added >= strftime('%s', 'now', 'localtime', 'start of day') * 1000\n" +
           "  AND date_added <  strftime('%s', 'now', 'localtime', 'start of day', '+1 day') * 1000;")
   fun getFoodItemsCreatedToday(): Flow<List<FoodEntity>>
}