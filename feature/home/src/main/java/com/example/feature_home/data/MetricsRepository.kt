package com.example.feature_home.data

import com.example.utility.data.DataStore
import com.example.utility.foreground.StepCountProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

interface MetricsRepository {
    val stepTaken: StateFlow<Int>
    val caloriesBurned: StateFlow<Int>

    fun weight(
        initialValue: Int,
        coroutineScope: CoroutineScope,
    ): StateFlow<Int>

    fun calorieProgress(
        initialValue: Float,
        coroutineScope: CoroutineScope,
        transform: (Int, Int) -> Float
    ): StateFlow<Float>
}

class MetricsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
): MetricsRepository {
    override val caloriesBurned: StateFlow<Int> = StepCountProvider.caloriesBurned
    override val stepTaken: StateFlow<Int> = StepCountProvider.currentSteps

    override fun weight(
        initialValue: Int,
        coroutineScope: CoroutineScope,
    ): StateFlow<Int> {
        return dataStore.weightFlow().stateIn(
            scope = coroutineScope,
            initialValue = initialValue,
            started = SharingStarted.Eagerly
        )
    }

    override fun calorieProgress(
        initialValue: Float,
        coroutineScope: CoroutineScope,
        transform: (Int, Int) -> Float,
    ): StateFlow<Float> {
        return combine(dataStore.calorieFlow(), caloriesBurned) { calorieGoal, caloriesBurnt ->
            transform(caloriesBurnt, calorieGoal)
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = initialValue
        )
    }
}