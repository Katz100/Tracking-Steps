package com.example.utility.foreground

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

object StepCountProvider {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.STOPPED)
    val sessionState: StateFlow<SessionState> = _sessionState

    private val _currentSteps = MutableStateFlow<Int>(0)
    val currentSteps: StateFlow<Int> = _currentSteps

    private val _caloriesConsumed = MutableStateFlow<Int>(0)
    val caloriesConsumed: StateFlow<Int> = _caloriesConsumed

    private val _caloriesBurned = MutableStateFlow<Int>(0)
    val caloriesBurned: StateFlow<Int> = _caloriesBurned

    fun updateCaloriesBurned(value: Int) {
        _caloriesBurned.value = value
    }

    fun updateCurrentSteps(value: Int) {
        _currentSteps.value = value
    }

    fun resetSessionValues() {
        _currentSteps.value = 0
        _caloriesConsumed.value = 0
    }

    fun increaseCaloriesConsumed(value: Int) {
        _caloriesConsumed.value += value
    }

    fun setSessionState(state: SessionState) {
        _sessionState.value = state
    }

}

enum class SessionState {
    PAUSE,
    RESUME,
    STOPPED,
}