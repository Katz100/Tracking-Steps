package com.example.utility.data.repository

import com.example.utility.data.datastore.DataStore
import com.example.utility.data.db.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PreferencesRepository {
    val caloriesConsumedGoal: Flow<Int>
    val weight: Flow<Int>
    val calorieFlow: Flow<Int>
    val stepGoal: Flow<Int>
}

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore,
): PreferencesRepository {
    override val caloriesConsumedGoal: Flow<Int>
        get() = dataStore.caloriesConsumedGoalFlow()

    override val weight: Flow<Int>
        get() = dataStore.weightFlow()

    override val calorieFlow: Flow<Int>
        get() = dataStore.calorieFlow()

    override val stepGoal: Flow<Int>
        get() = dataStore.stepFlow()
}
