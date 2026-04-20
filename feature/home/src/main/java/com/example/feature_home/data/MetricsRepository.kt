package com.example.feature_home.data

import com.example.utility.data.DataStore
import com.example.utility.foreground.StepCountProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

// Move to utility
interface MetricsRepository {
    val stepTaken: StateFlow<Int>
    val caloriesBurned: StateFlow<Int>
    val weight: Flow<Int>
    val calorieFlow: Flow<Int>
    fun calorieProgress(transform: (Int, Int) -> Float): Flow<Float>
    suspend fun incrementCalories(incrementValue: Int)
    suspend fun decrementCalories(decrementValue: Int)
}

class MetricsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
): MetricsRepository {
    override val caloriesBurned: StateFlow<Int> = StepCountProvider.caloriesBurned
    override val stepTaken: StateFlow<Int> = StepCountProvider.currentSteps
    override val weight: Flow<Int> = dataStore.weightFlow()
    override val calorieFlow: Flow<Int> = dataStore.calorieFlow()

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
}