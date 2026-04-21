package com.example.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.MetricsRepository
import com.example.utility.data.FoodItem
import com.example.utility.data.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    foodRepository: FoodRepository,
    metricsRepository: MetricsRepository,
): ViewModel() {
    val stepsTaken = metricsRepository.stepTaken
    val caloriesBurned = metricsRepository.caloriesBurned

    val weight: StateFlow<Int> = metricsRepository.weight.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.Eagerly
    )

    val calorieProgress: StateFlow<Float> = metricsRepository.calorieProgress { caloriesBurnt, calorieGoal ->
        calculateProgressForCalorieGoal(caloriesBurnt, calorieGoal)
    }.stateIn(
        scope = viewModelScope,
        initialValue = 0f,
        started = SharingStarted.Eagerly
    )

    val currentDayFoodItems: StateFlow<List<FoodItem>> = foodRepository.currentDayFoodItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val caloriesConsumed: StateFlow<Int> = currentDayFoodItems
        .map { foodItems -> calculateTotalCaloriesConsumed(foodItems) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    private fun calculateProgressForCalorieGoal(caloriesBurnt: Int, calorieGoal: Int): Float {
        Timber.d("Calculating: Burnt: $caloriesBurnt Goal: $calorieGoal")
        if (calorieGoal == 0) return 0f
        return caloriesBurnt.toFloat() / calorieGoal.toFloat()
    }

    private fun calculateTotalCaloriesConsumed(foodItems: List<FoodItem>): Int =
        foodItems.sumOf { foodItem -> foodItem.calories }
}