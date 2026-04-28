package com.example.utility.domain

import com.example.utility.data.db.FoodItem
import com.example.utility.data.repository.FoodRepository
import com.example.utility.data.repository.PreferencesRepository
import com.example.utility.data.repository.SessionMetricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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

class GetCurrentDayFoodItemsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(): Flow<List<FoodItem>> {
        return foodRepository.currentDayFoodItems
    }
}

class GetCurrentDayCaloriesConsumed @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(): Flow<Int> {
        return foodRepository.currentDayFoodItems.map { foodItems ->
            foodItems.sumOf { foodItem -> foodItem.calories }
        }
    }
}

internal fun calculateProgress(current: Int, goal: Int): Float {
    if (goal <= 0) return 0f
    return current.toFloat() / goal.toFloat()
}