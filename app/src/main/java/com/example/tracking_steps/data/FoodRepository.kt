package com.example.tracking_steps.data

import com.example.tracking_steps.data.db.FoodDao
import com.example.tracking_steps.firebase.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

interface FoodRepository {
    val currentDayFoodItems: Flow<List<FoodItem>>

    fun insertFoodItem(foodItem: FoodItem)
}

class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
): FoodRepository {
    override val currentDayFoodItems: Flow<List<FoodItem>> =
        foodDao.getFoodItemsCreatedToday().map {entities ->
            entities.map { entity -> entity.asDomain()
            }
        }

    override fun insertFoodItem(foodItem: FoodItem) {
        foodDao.insertAll(foodItem.asEntity())
    }

    fun FoodEntity.asDomain() = FoodItem(
        id = this.id,
        foodName = this.foodName!!,
        calories = this.calories!!,
        dateAdded = this.dateAdded,
    )

    fun FoodItem.asEntity() = FoodEntity(
        foodName = this.foodName,
        calories = this.calories,
        dateAdded = Date(),
    )
}