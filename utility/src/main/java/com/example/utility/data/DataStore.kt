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

    companion object {
        const val USER_WEIGHT = "weight"
        const val CALORIE_GOAL = "calorie_goal"
        const val STEP_GOAL = "step_goal"
        const val CALORIES_CONSUMED_GOAL = "calories_consumed_goal"
    }

    val USER_WEIGHT = intPreferencesKey("weight")

    val CALORIE_GOAL = intPreferencesKey("calorie_goal")

    val STEP_GOAL = intPreferencesKey("step_goal")

    val CALORIES_CONSUMED_GOAL = intPreferencesKey("calories_consumed_goal")

    fun weightFlow(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[USER_WEIGHT] ?: 0
    }

    fun calorieFlow(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CALORIE_GOAL] ?: 0
    }

    fun stepFlow(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[STEP_GOAL] ?: 0
    }

    fun caloriesConsumedGoalFlow(): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[CALORIES_CONSUMED_GOAL] ?: 0
    }

    suspend fun setNewWeight(weight: Int) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[USER_WEIGHT] = weight
            }
        }
    }

    suspend fun incrementKey(key: Preferences.Key<Int>, incrementValue: Int) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[key] = preferences[key]?.plus(incrementValue) ?: incrementValue
            }
        }
    }

    suspend fun decrementKey(key: Preferences.Key<Int>, decrementValue: Int) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[key] = preferences[key]?.minus(decrementValue) ?: 0
            }
        }
    }
}