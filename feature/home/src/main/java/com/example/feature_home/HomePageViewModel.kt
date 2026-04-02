package com.example.feature_home

import androidx.lifecycle.ViewModel
import com.example.utility.foreground.StepCountProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(): ViewModel() {
    val stepsTaken = StepCountProvider.currentSteps
    val caloriesBurned = StepCountProvider.caloriesBurned
    val caloriesConsumed = StepCountProvider.caloriesConsumed
}