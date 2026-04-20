package com.example.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.DataStore
import com.example.utility.data.FoodItem
import com.example.utility.data.FoodRepository
import com.example.utility.foreground.StepCountProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    foodRepository: FoodRepository,
    dataStore: DataStore,
): ViewModel() {
    val stepsTaken = StepCountProvider.currentSteps
    val caloriesBurned = StepCountProvider.caloriesBurned
    // val caloriesProgress = StepCountProvider.caloriesProgress

    val weight: StateFlow<Int> = dataStore.weightFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    val calorieProgress: StateFlow<Float> = combine(dataStore.calorieFlow(), caloriesBurned) { calorieGoal, caloriesBurnt ->
        calculateProgressForCalorieGoal(caloriesBurnt, calorieGoal)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0f
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

    // temp
    init {
        viewModelScope.launch {
            dataStore.setNewCalorieGoal(100)
            Timber.i("Set new calorie goal")
        }
    }

    private fun calculateProgressForCalorieGoal(caloriesBurnt: Int, calorieGoal: Int): Float {
        Timber.i("Calculating progress. Calories burnt: $caloriesBurnt, calorie goal: $calorieGoal")
        if (calorieGoal == 0) return 0f
        return caloriesBurnt.toFloat() / calorieGoal.toFloat()
    }

    private fun calculateTotalCaloriesConsumed(foodItems: List<FoodItem>): Int =
        foodItems.sumOf { foodItem -> foodItem.calories }
}