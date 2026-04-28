package com.example.utility.data.repository

import androidx.datastore.preferences.core.Preferences
import com.example.utility.data.datastore.DataStore
import com.example.utility.data.db.FoodItem
import com.example.utility.service.foreground.SessionState
import com.example.utility.service.foreground.StepCountProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/*
    TODO: Split repository into two, one for accessing data from StepCountProvider
     and the other for accessing data from data store
 */
interface MetricsRepository {
    val stepTaken: StateFlow<Int>
    val caloriesBurned: StateFlow<Int>
    val sessionState: StateFlow<SessionState>
    val caloriesConsumed: StateFlow<Int>
    val caloriesConsumedGoal: Flow<Int>
    val weight: Flow<Int>
    val calorieFlow: Flow<Int>
    val stepGoal: Flow<Int>
    val currentDayFoodItems: Flow<List<FoodItem>>
    fun caloriesConsumedProgress(transform: (Int, Int) -> Float): Flow<Float>
    fun stepGoalProgress(transform: (Int, Int) -> Float): Flow<Float>
    fun calorieProgress(transform: (Int, Int) -> Float): Flow<Float>
    suspend fun decrementKey(key: Preferences.Key<Int>, decrementValue: Int)
    suspend fun incrementKey(key: Preferences.Key<Int>, incrementValue: Int)
    suspend fun setNewWeight(weight: Int)
}

class MetricsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
    private val foodRepository: FoodRepository,
): MetricsRepository {
    override val caloriesBurned: StateFlow<Int> = StepCountProvider.caloriesBurned
    override val stepTaken: StateFlow<Int> = StepCountProvider.currentSteps
    override val sessionState: StateFlow<SessionState> = StepCountProvider.sessionState
    override val caloriesConsumed: StateFlow<Int> = StepCountProvider.caloriesConsumed
    override val weight: Flow<Int> = dataStore.weightFlow()
    override val calorieFlow: Flow<Int> = dataStore.calorieFlow()
    override val stepGoal: Flow<Int> = dataStore.stepFlow()
    override val caloriesConsumedGoal: Flow<Int> = dataStore.caloriesConsumedGoalFlow()
    override val currentDayFoodItems: Flow<List<FoodItem>> = foodRepository.currentDayFoodItems

    override fun caloriesConsumedProgress(transform: (Int, Int) -> Float): Flow<Float> {
        return combine(dataStore.caloriesConsumedGoalFlow(), currentDayFoodItems) { caloriesGoal, foodItems ->
            val consumed = foodItems.sumOf { foodItem -> foodItem.calories }
            transform(consumed, caloriesGoal)
        }
    }

    override fun stepGoalProgress(transform: (Int, Int) -> Float): Flow<Float> {
        return combine(dataStore.stepFlow(), stepTaken) {goal, current ->
            transform(current, goal)
        }
    }

    override fun calorieProgress(
        transform: (Int, Int) -> Float,
    ): Flow<Float> {
        return combine(dataStore.calorieFlow(), caloriesBurned) { calorieGoal, caloriesBurnt ->
            transform(caloriesBurnt, calorieGoal)
        }
    }

    override suspend fun decrementKey(
        key: Preferences.Key<Int>,
        decrementValue: Int
    ) {
        dataStore.decrementKey(key, decrementValue)
    }

    override suspend fun incrementKey(
        key: Preferences.Key<Int>,
        incrementValue: Int
    ) {
        dataStore.incrementKey(key, incrementValue)
    }

    override suspend fun setNewWeight(weight: Int) {
        dataStore.setNewWeight(weight)
    }
}

class MetricsRepositoryFake: MetricsRepository {
    private val _stepTaken = MutableStateFlow(5000)
    override val stepTaken: StateFlow<Int> = _stepTaken

    private val _caloriesBurned = MutableStateFlow(50)
    override val caloriesBurned: StateFlow<Int> = _caloriesBurned

    private val _sessionState = MutableStateFlow(SessionState.STOPPED)
    override val sessionState: StateFlow<SessionState> = _sessionState

    private val _caloriesConsumed = MutableStateFlow<Int>(200)
    override val caloriesConsumed: StateFlow<Int> = _caloriesConsumed

    override val caloriesConsumedGoal: Flow<Int> = flowOf(2000)
    override val weight: Flow<Int> = flowOf(180)
    override val calorieFlow: Flow<Int> = flowOf(200)
    override val stepGoal: Flow<Int> = flowOf(10_000)
    override val currentDayFoodItems: Flow<List<FoodItem>> = flowOf(listOf(FoodItem(foodName = "Pizza", calories = 300),
        FoodItem(foodName = "Burger", calories = 500)))

    override fun caloriesConsumedProgress(transform: (Int, Int) -> Float): Flow<Float> {
        return combine(caloriesConsumedGoal, currentDayFoodItems) { caloriesGoal, foodItems ->
            val consumed = foodItems.sumOf { foodItem -> foodItem.calories }
            transform(consumed, caloriesGoal)
        }
    }

    override fun stepGoalProgress(transform: (Int, Int) -> Float): Flow<Float> {
        return combine(stepGoal, stepTaken) {goal, current ->
            transform(current, goal)
        }
    }

    override fun calorieProgress(transform: (Int, Int) -> Float): Flow<Float> {
        return combine(calorieFlow, caloriesBurned) { calorieGoal, caloriesBurnt ->
            transform(caloriesBurnt, calorieGoal)
        }
    }

    override suspend fun decrementKey(
        key: Preferences.Key<Int>,
        decrementValue: Int
    ) {
    }

    override suspend fun incrementKey(
        key: Preferences.Key<Int>,
        incrementValue: Int
    ) {
    }

    override suspend fun setNewWeight(weight: Int) {
    }


    fun setSteps(value: Int) {
        _stepTaken.value = value
    }

    fun setCaloriesBurned(value: Int) {
        _caloriesBurned.value = value
    }

    fun setSessionState(value: SessionState) {
        _sessionState.value = value
    }

    fun setCaloriesConsumed(value: Int) {
        _caloriesConsumed.value = value
    }
}
