package com.example.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_home.data.MetricsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
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

    fun incrementCalories() {
        viewModelScope.launch {
            metricsRepository.incrementCalories(50)
        }
    }

    fun decrementCalorieGoal() {
        viewModelScope.launch {
            if (calorieGoal.value == 0) return@launch
            metricsRepository.decrementCalories(50)
        }
    }
}