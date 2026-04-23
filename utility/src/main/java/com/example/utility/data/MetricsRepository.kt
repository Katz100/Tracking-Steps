package com.example.utility.data

import com.example.utility.data.db.FoodItem
import com.example.utility.foreground.SessionState
import com.example.utility.foreground.StepCountProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

// Move to utility
interface MetricsRepository {
    val stepTaken: StateFlow<Int>
    val caloriesBurned: StateFlow<Int>
    val sessionState: StateFlow<SessionState>
    val caloriesConsumed: StateFlow<Int>
    val caloriesConsumedGoal: Flow<Int>
    val weight: Flow<Int>
    val calorieFlow: Flow<Int>
    val stepGoal: Flow<Int>
    val currentDayFoodItems: Flow<List<FoodItem>>
    fun caloriesConsumedProgress(transform: (Int, Int) -> Float): Flow<Float>
    fun calorieProgress(transform: (Int, Int) -> Float): Flow<Float>
    // TODO: Create one increment/decrement method that can be reused easily
    suspend fun incrementCalories(incrementValue: Int)
    suspend fun decrementCalories(decrementValue: Int)
    suspend fun incrementStepGoal(incrementValue: Int)
    suspend fun decrementStepGoal(decrementValue: Int)
    suspend fun incrementConsumedGoal(incrementValue: Int)
    suspend fun decrementConsumedGoal(decrementValue: Int)
}

class MetricsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
    private val foodRepository: FoodRepository,
): MetricsRepository {
    override val caloriesBurned: StateFlow<Int> = StepCountProvider.caloriesBurned
    override val stepTaken: StateFlow<Int> = StepCountProvider.currentSteps
    override val sessionState: StateFlow<SessionState> = StepCountProvider.sessionState
    override val caloriesConsumed: StateFlow<Int> = StepCountProvider.caloriesConsumed
    override val weight: Flow<Int> = dataStore.weightFlow()
    override val calorieFlow: Flow<Int> = dataStore.calorieFlow()
    override val stepGoal: Flow<Int> = dataStore.stepFlow()
    override val caloriesConsumedGoal: Flow<Int> = dataStore.caloriesConsumedGoalFlow()
    override val currentDayFoodItems: Flow<List<FoodItem>> = foodRepository.currentDayFoodItems

    override fun caloriesConsumedProgress(transform: (Int, Int) -> Float): Flow<Float> {
        return combine(dataStore.caloriesConsumedGoalFlow(), currentDayFoodItems) { caloriesGoal, foodItems ->
            val consumed = foodItems.sumOf { foodItem -> foodItem.calories }
            transform(consumed, caloriesGoal)
        }
    }
    override fun calorieProgress(
        transform: (Int, Int) -> Float,
    ): Flow<Float> {
        return combine(dataStore.calorieFlow(), caloriesBurned) { calorieGoal, caloriesBurnt ->
            transform(caloriesBurnt, calorieGoal)
        }
    }

    override suspend fun incrementCalories(incrementValue: Int) {
        dataStore.incrementCalorieGoal(incrementValue)
    }

    override suspend fun decrementCalories(decrementValue: Int) {
        dataStore.decrementCalorieGoal(decrementValue)
    }

    override suspend fun incrementStepGoal(incrementValue: Int) {
        dataStore.incrementStepGoal(incrementValue)
    }

    override suspend fun decrementStepGoal(decrementValue: Int) {
        dataStore.decrementStepGoal(decrementValue)
    }

    override suspend fun incrementConsumedGoal(incrementValue: Int) {
        dataStore.incrementCaloriesConsumedGoal(incrementValue)
    }

    override suspend fun decrementConsumedGoal(decrementValue: Int) {
        dataStore.decrementCaloriesConsumedGoal(decrementValue)
    }
}