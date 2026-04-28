package com.example.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.repository.MetricsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.utility.data.datastore.DataStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsPageViewModel @Inject constructor(
    private val metricsRepository: MetricsRepository
): ViewModel() {
    val calorieGoal: StateFlow<Int> = metricsRepository.calorieFlow
        .stateIn(
            scope = viewModelScope,
            initialValue = 0,
            started = SharingStarted.Eagerly
        )

    val stepGoal: StateFlow<Int> = metricsRepository.stepGoal
        .stateIn(
            scope = viewModelScope,
            initialValue = 0,
            started = SharingStarted.Eagerly
        )

    val consumedGoal: StateFlow<Int> = metricsRepository.caloriesConsumedGoal
        .stateIn(
            scope = viewModelScope,
            initialValue = 0,
            started = SharingStarted.Eagerly
        )

    fun incrementCalories() {
        viewModelScope.launch {
            metricsRepository.incrementKey(intPreferencesKey(DataStore.CALORIE_GOAL), 50)
        }
    }

    fun decrementCalorieGoal() {
        viewModelScope.launch {
            if (calorieGoal.value == 0) return@launch
            metricsRepository.decrementKey(intPreferencesKey(DataStore.CALORIE_GOAL), 50)
        }
    }

    fun incrementStepGoal() {
        viewModelScope.launch {
            metricsRepository.incrementKey(intPreferencesKey(DataStore.STEP_GOAL), 100)
        }
    }

    fun decrementStepGoal() {
        viewModelScope.launch {
            if (stepGoal.value == 0) return@launch
            metricsRepository.decrementKey(intPreferencesKey(DataStore.STEP_GOAL), 100)
        }
    }

    fun incrementConsumedGoal() {
        viewModelScope.launch {
            metricsRepository.incrementKey(intPreferencesKey(DataStore.CALORIES_CONSUMED_GOAL), 100)
        }
    }

    fun decrementConsumedGoal() {
        viewModelScope.launch {
            if (consumedGoal.value == 0) return@launch
            metricsRepository.decrementKey(intPreferencesKey(DataStore.CALORIES_CONSUMED_GOAL), 100)
        }
    }
}