package com.example.utility.data

import com.example.utility.data.db.FoodDao
import com.example.utility.data.db.FoodEntity
import com.example.utility.data.db.FoodItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

interface FoodRepository {
    val currentDayFoodItems: Flow<List<FoodItem>>
    val allFoodItems: Flow<List<FoodItem>>

    suspend fun insertFoodItem(foodItem: FoodItem)

}

class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
): FoodRepository {
    override val currentDayFoodItems: Flow<List<FoodItem>> =
        foodDao.getFoodItemsCreatedToday().map {entities ->
            Timber.i("Entities: $entities")
            entities.map { entity ->
                Timber.i("Entity: $entity")
                entity.asDomain()
            }
        }

    override val allFoodItems: Flow<List<FoodItem>> =
        foodDao.getAllFoodItems().map { entities ->
            entities.map { entity ->
                entity.asDomain()
            }
        }

    override suspend fun insertFoodItem(foodItem: FoodItem) {
        withContext(Dispatchers.IO) {
            foodDao.insertAll(foodItem.asEntity())
        }
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