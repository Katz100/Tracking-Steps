package com.example.tracking_steps

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    private val _stepsCounter = MutableStateFlow<Int>(0)
    val stepsCounter: StateFlow<Int> = _stepsCounter.asStateFlow()

    fun increaseStepCounter() {
        _stepsCounter.value++
    }

    fun updateSteps(value: Int) {
        _stepsCounter.value = value
    }
}