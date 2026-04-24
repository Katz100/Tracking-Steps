package com.example.utility.data

import androidx.datastore.preferences.core.Preferences
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
    fun stepGoalProgress(transform: (Int, Int) -> Float): Flow<Float>
    fun calorieProgress(transform: (Int, Int) -> Float): Flow<Float>
    suspend fun decrementKey(key: Preferences.Key<Int>, decrementValue: Int)
    suspend fun incrementKey(key: Preferences.Key<Int>, incrementValue: Int)
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

    override fun stepGoalProgress(transform: (Int, Int) -> Float): Flow<Float> {
        return combine(dataStore.stepFlow(), stepTaken) {goal, current ->
            transform(current, goal)
        }
    }

    override fun calorieProgress(
        transform: (Int, Int) -> Float,
    ): Flow<Float> {
        return combine(dataStore.calorieFlow(), caloriesBurned) { calorieGoal, caloriesBurnt ->
            transform(caloriesBurnt, calorieGoal)
        }
    }

    override suspend fun decrementKey(
        key: Preferences.Key<Int>,
        decrementValue: Int
    ) {
        dataStore.decrementKey(key, decrementValue)
    }

    override suspend fun incrementKey(
        key: Preferences.Key<Int>,
        incrementValue: Int
    ) {
        dataStore.incrementKey(key, incrementValue)
    }
}