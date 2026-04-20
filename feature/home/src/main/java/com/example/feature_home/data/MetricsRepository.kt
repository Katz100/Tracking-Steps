package com.example.feature_home.data

import com.example.utility.data.DataStore
import com.example.utility.foreground.StepCountProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface MetricsRepository {
    val stepTaken: StateFlow<Int>
    val caloriesBurned: StateFlow<Int>
    val weight: Flow<Int>
    fun calorieProgress(transform: (Int, Int) -> Float): Flow<Float>
}

class MetricsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
): MetricsRepository {
    override val caloriesBurned: StateFlow<Int> = StepCountProvider.caloriesBurned
    override val stepTaken: StateFlow<Int> = StepCountProvider.currentSteps
    override val weight: Flow<Int> = dataStore.weightFlow()

    override fun calorieProgress(
        transform: (Int, Int) -> Float,
    ): Flow<Float> {
        return combine(dataStore.calorieFlow(), caloriesBurned) { calorieGoal, caloriesBurnt ->
            transform(caloriesBurnt, calorieGoal)
        }
    }
}