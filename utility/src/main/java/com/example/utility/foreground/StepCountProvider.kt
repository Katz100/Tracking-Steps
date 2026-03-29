package com.example.utility.foreground

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object StepCountProvider {
    private val _currentSteps = MutableStateFlow<Int>(0)
    val currentSteps: StateFlow<Int> = _currentSteps

    private val _currentGoal = MutableStateFlow<Int>(1)
    val currentGoal: StateFlow<Int> = _currentGoal

    fun updateCurrentSteps(value: Int) {
        _currentSteps.value = value
    }

    fun updateCurrentGoal(value: Int) {
        _currentGoal.value = value
    }

    fun resetSessionValues() {
        _currentSteps.value = 0
        _currentGoal.value = 1
    }
}