package com.example.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.FoodItem
import com.example.utility.data.FoodRepository
import com.example.utility.foreground.StepCountProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
): ViewModel() {
    val stepsTaken = StepCountProvider.currentSteps
    val caloriesBurned = StepCountProvider.caloriesBurned
    val caloriesProgress = StepCountProvider.caloriesProgress

    private val _currentDayFoodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val currentDayFoodItems: StateFlow<List<FoodItem>> = _currentDayFoodItems

    private val _caloriesConsumed = MutableStateFlow<Int>(0)
    val caloriesConsumed: StateFlow<Int> = _caloriesConsumed

    init {
        viewModelScope.launch {
            foodRepository.currentDayFoodItems.collect {
                _currentDayFoodItems.value = it
                calculateTotalCaloriesConsumed()
            }
        }
    }

    private fun calculateTotalCaloriesConsumed() {
        var total = 0
        for (item in currentDayFoodItems.value) {
            total += item.calories
        }
        _caloriesConsumed.value = total
    }
}