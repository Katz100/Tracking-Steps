package com.example.tracking_steps

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.foreground.StepCountProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
     val stepsCounter = StepCountProvider.currentSteps
     val currentGoal = StepCountProvider.currentGoal

     private val _goal = MutableStateFlow<String>("")
     val goal: StateFlow<String> = _goal

     private val _weightTxt = MutableStateFlow<String>("")
     val weightTxt: StateFlow<String> = _weightTxt

     fun onWeightChange(value: String) {
          _weightTxt.value = value
     }

     fun onGoalChange(value: String) {
          _goal.value = value
     }

}