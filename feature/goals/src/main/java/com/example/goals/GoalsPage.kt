package com.example.goals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.utility.composables.ValueStepper

@Composable
fun GoalsPage(
    viewModel: GoalsPageViewModel = hiltViewModel()
) {
     val calorieGoal = viewModel.calorieGoal.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ValueStepper(
            modifier = Modifier.fillMaxWidth()
                .height(100.dp)
                .padding(16.dp),
            onPlusIconClicked = viewModel::incrementCalories,
            onSubtractIconClicked = viewModel::decrementCalorieGoal,
            value = calorieGoal
        )
    }
}