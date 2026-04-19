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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    foodRepository: FoodRepository,
    dataStore: DataStore,
): ViewModel() {
    val stepsTaken = StepCountProvider.currentSteps
    val caloriesBurned = StepCountProvider.caloriesBurned
    val caloriesProgress = StepCountProvider.caloriesProgress

    val weight = dataStore.weightFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )

    val currentDayFoodItems = foodRepository.currentDayFoodItems.stateIn(
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

    private fun calculateTotalCaloriesConsumed(foodItems: List<FoodItem>): Int =
        foodItems.sumOf { foodItem -> foodItem.calories }
}