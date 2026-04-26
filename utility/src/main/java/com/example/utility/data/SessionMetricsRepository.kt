package com.example.utility.data

import com.example.utility.foreground.SessionState
import com.example.utility.foreground.StepCountProvider
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface SessionMetricsRepository {
    val stepTaken: StateFlow<Int>
    val caloriesBurned: StateFlow<Int>
    val sessionState: StateFlow<SessionState>
}

class SessionMetricsRepositoryImpl @Inject constructor(): SessionMetricsRepository {
    override val stepTaken: StateFlow<Int>
        get() = StepCountProvider.currentSteps
    override val caloriesBurned: StateFlow<Int>
        get() = StepCountProvider.caloriesBurned
    override val sessionState: StateFlow<SessionState>
        get() = StepCountProvider.sessionState
}