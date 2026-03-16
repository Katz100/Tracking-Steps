package com.example.tracking_steps

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    private val _stepsCounter = MutableStateFlow<Int>(0)
    val stepsCounter: StateFlow<Int> = _stepsCounter

    fun increaseStepCounter() {
        Log.i("Tag", "Increasing counter")
        _stepsCounter.value++
    }
}