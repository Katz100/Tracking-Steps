package com.example.utility.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface PreferencesRepository {
    val caloriesConsumedGoal: Flow<Int>
    val weight: Flow<Int>
    val calorieFlow: Flow<Int>
    val stepGoal: Flow<Int>
}

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
): PreferencesRepository {
    override val caloriesConsumedGoal: Flow<Int>
        get() = dataStore.caloriesConsumedGoalFlow()

    override val weight: Flow<Int>
        get() = dataStore.weightFlow()

    override val calorieFlow: Flow<Int>
        get() = dataStore.calorieFlow()

    override val stepGoal: Flow<Int>
        get() = dataStore.stepFlow()
}

class GetStepGoalProgressUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val sessionMetricsRepository: SessionMetricsRepository,
) {
    operator fun invoke(): Flow<Float> {
        return combine(sessionMetricsRepository.stepTaken, preferencesRepository.stepGoal) { current, goal ->
            calculateProgress(current, goal)
        }
    }
}

class GetCaloriesBurnedProgressUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val sessionMetricsRepository: SessionMetricsRepository,
) {
    operator fun invoke(): Flow<Float> {
        return combine(sessionMetricsRepository.caloriesBurned, preferencesRepository.calorieFlow) { current, goal ->
            calculateProgress(current, goal)
        }
    }
}

class GetCaloriesConsumedProgressUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val foodRepository: FoodRepository,
) {
    operator fun invoke(): Flow<Float> {
        return combine(foodRepository.currentDayFoodItems, preferencesRepository.caloriesConsumedGoal) { foodItems, goal ->
            val current = foodItems.sumOf { foodItem -> foodItem.calories }
            calculateProgress(current, goal)
        }
    }
}



internal fun calculateProgress(current: Int, goal: Int): Float {
    if (goal <= 0) return 0f
    return current.toFloat() / goal.toFloat()
}