package com.example.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.db.FoodItem
import com.example.utility.data.FoodRepository
import com.example.utility.data.GetCaloriesBurnedProgressUseCase
import com.example.utility.data.GetCaloriesConsumedProgressUseCase
import com.example.utility.data.GetStepGoalProgressUseCase
import com.example.utility.data.PreferencesRepository
import com.example.utility.data.SessionMetricsRepository
import com.example.utility.foreground.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    foodRepository: FoodRepository,
    sessionMetricsRepository: SessionMetricsRepository,
    preferencesRepository: PreferencesRepository,
    getStepGoalProgressUseCase: GetStepGoalProgressUseCase,
    getCaloriesBurnedProgressUseCase: GetCaloriesBurnedProgressUseCase,
    getCaloriesConsumedProgressUseCase: GetCaloriesConsumedProgressUseCase,
): ViewModel() {
    val sessionActive: StateFlow<SessionState> = sessionMetricsRepository.sessionState
    val stepsTaken: StateFlow<Int> = sessionMetricsRepository.stepTaken
    val caloriesBurned = sessionMetricsRepository.caloriesBurned

    val weight: StateFlow<Int> = preferencesRepository.weight.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.Eagerly
    )

    val stepGoal: StateFlow<Int> = preferencesRepository.stepGoal.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.Eagerly
    )

    val stepProgress: StateFlow<Float> = getStepGoalProgressUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f
    )

    val calorieProgress: StateFlow<Float> = getCaloriesBurnedProgressUseCase().stateIn(
        scope = viewModelScope,
        initialValue = 0f,
        started = SharingStarted.Eagerly
    )

    val caloriesConsumedGoal: StateFlow<Float> = getCaloriesConsumedProgressUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f,
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

    private fun calculateTotalCaloriesConsumed(foodItems: List<FoodItem>): Int =
        foodItems.sumOf { foodItem -> foodItem.calories }
}