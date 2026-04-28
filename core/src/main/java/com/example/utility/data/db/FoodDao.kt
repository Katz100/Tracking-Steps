package com.example.utility.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.utility.data.db.FoodEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface FoodDao {
    @Insert
    fun insertAll(vararg foodItems: FoodEntity)

    @Delete
    fun deleteFoodItem(foodItem: FoodEntity)

   @Query("""
    SELECT *
    FROM food_item
    WHERE date_added >= :start AND date_added < :end
    ORDER BY date_added DESC
""")
   fun getFoodItemsCreatedToday(
       start: Long = Date.UTC(
           Date().year,
           Date().month,
           Date().date ,
           Date().hours,
           Date().minutes,
           Date().seconds
       ),
       end: Long = Date.UTC(
           Date().year,
           Date().month,
           Date().date + 1,
           Date().hours,
           Date().minutes,
           Date().seconds
       )
   ): Flow<List<FoodEntity>>

   @Query("SELECT * FROM food_item")
   fun getAllFoodItems(): Flow<List<FoodEntity>>
}