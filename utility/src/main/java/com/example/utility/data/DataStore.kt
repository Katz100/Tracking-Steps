package com.example.utility.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val USER_WEIGHT = intPreferencesKey("weight")

    val CALORIE_GOAL = intPreferencesKey("calorie_goal")

    fun weightFlow(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[USER_WEIGHT] ?: 0
    }

    fun calorieFlow(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CALORIE_GOAL] ?: 0
    }

    suspend fun setNewWeight(weight: Int) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[USER_WEIGHT] = weight
            }
        }
    }

    suspend fun setNewCalorieGoal(goal: Int) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[CALORIE_GOAL] = goal
            }
        }
    }
}