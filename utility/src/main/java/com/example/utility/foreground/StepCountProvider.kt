package com.example.utility.foreground

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object StepCountProvider {
    private val _currentSteps = MutableStateFlow<Int>(0)
    val currentSteps: StateFlow<Int> = _currentSteps

    private val _currentGoal = MutableStateFlow<Int>(1)
    val currentGoal: StateFlow<Int> = _currentGoal

    private val _caloriesConsumed = MutableStateFlow<Int>(0)
    val caloriesConsumed: StateFlow<Int> = _caloriesConsumed

    private val _caloriesBurned = MutableStateFlow<Int>(0)
    val caloriesBurned: StateFlow<Int> = _caloriesBurned

    private val _caloriesProgress = MutableStateFlow<Float>(0f)
    val caloriesProgress: StateFlow<Float> = _caloriesProgress

    fun updateCaloriesProgress(value: Float) {
        _caloriesProgress.value = value
    }

    fun updateCaloriesBurned(value: Int) {
        _caloriesBurned.value = value
    }

    fun updateCurrentSteps(value: Int) {
        _currentSteps.value = value
    }

    fun updateCurrentGoal(value: Int) {
        _currentGoal.value = value
    }

    fun resetSessionValues() {
        _currentSteps.value = 0
        _currentGoal.value = 1
        _caloriesConsumed.value = 0
    }

    fun increaseCaloriesConsumed(value: Int) {
        _caloriesConsumed.value += value
    }
}