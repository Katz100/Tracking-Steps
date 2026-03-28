package com.example.utility.foreground

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

object StepCountProvider {
    private val _currentSteps = MutableStateFlow<Int>(0)
    val currentSteps: StateFlow<Int> = _currentSteps

    private val _currentGoal = MutableStateFlow<Int>(0)
    val currentGoal: StateFlow<Int> = _currentGoal

    private val _completionFlow = MutableSharedFlow<CompletionEvent>()
    val completionFlow: SharedFlow<CompletionEvent> = _completionFlow

    fun updateCurrentSteps(value: Int) {
        _currentSteps.value = value
    }

    fun updateCurrentGoal(value: Int) {
        _currentGoal.value = value
    }

    suspend fun updateCompletionStatus(
        completionState: CompletionState,
        stepsTaken: Int
    ) {
        _completionFlow.emit(CompletionEvent(completionState, stepsTaken))
    }
}

enum class CompletionState {
    COMPLETED,
    ENDED_EARLY,
    NO_CHANGE,
}

data class CompletionEvent (
    val state: CompletionState,
    val steps: Int,
)