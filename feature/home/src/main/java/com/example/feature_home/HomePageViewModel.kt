package com.example.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.db.FoodItem
import com.example.utility.data.GetCaloriesBurnedProgressUseCase
import com.example.utility.data.GetCaloriesConsumedProgressUseCase
import com.example.utility.data.GetCurrentDayCaloriesConsumed
import com.example.utility.data.GetCurrentDayFoodItemsUseCase
import com.example.utility.data.GetStepGoalProgressUseCase
import com.example.utility.data.PreferencesRepository
import com.example.utility.data.SessionMetricsRepository
import com.example.utility.foreground.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    sessionMetricsRepository: SessionMetricsRepository,
    preferencesRepository: PreferencesRepository,
    getStepGoalProgressUseCase: GetStepGoalProgressUseCase,
    getCaloriesBurnedProgressUseCase: GetCaloriesBurnedProgressUseCase,
    getCaloriesConsumedProgressUseCase: GetCaloriesConsumedProgressUseCase,
    getCurrentDayFoodItemsUseCase: GetCurrentDayFoodItemsUseCase,
    getCurrentDayCaloriesConsumed: GetCurrentDayCaloriesConsumed,
): ViewModel() {
    val sessionState: StateFlow<SessionState> = sessionMetricsRepository.sessionState
    val stepsTaken: StateFlow<Int> = sessionMetricsRepository.stepTaken
    val caloriesBurned = sessionMetricsRepository.caloriesBurned

    val userWeight: StateFlow<Int> = preferencesRepository.weight.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.Eagerly
    )

    val stepGoal: StateFlow<Int> = preferencesRepository.stepGoal.stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.Eagerly
    )

    val stepGoalProgress: StateFlow<Float> = getStepGoalProgressUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f
    )

    val caloriesBurnedProgress: StateFlow<Float> = getCaloriesBurnedProgressUseCase().stateIn(
        scope = viewModelScope,
        initialValue = 0f,
        started = SharingStarted.Eagerly
    )

    val caloriesConsumedProgress: StateFlow<Float> = getCaloriesConsumedProgressUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f,
    )

    val currentDayFoodItems: StateFlow<List<FoodItem>> = getCurrentDayFoodItemsUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    val caloriesConsumed: StateFlow<Int> = getCurrentDayCaloriesConsumed().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )
}