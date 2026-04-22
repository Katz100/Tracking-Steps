package com.example.goals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.utility.composables.ValueStepper

@Composable
fun GoalsPage(
    viewModel: GoalsPageViewModel = hiltViewModel()
) {
    val calorieGoal = viewModel.calorieGoal.collectAsState().value
    val stepGoal = viewModel.stepGoal.collectAsState().value
    val consumedGoal = viewModel.consumedGoal.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Goals",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Set goals for steps or calories",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ValueStepper(
            modifier = Modifier.fillMaxWidth()
                .height(120.dp)
                .padding(16.dp),
            onPlusIconClicked = viewModel::incrementCalories,
            onSubtractIconClicked = viewModel::decrementCalorieGoal,
            value = calorieGoal,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            imgText = "kcal",
            img = painterResource(com.example.utility.R.drawable.calories)
        )

        ValueStepper(
            modifier = Modifier.fillMaxWidth()
                .height(120.dp)
                .padding(16.dp),
            onPlusIconClicked = viewModel::incrementStepGoal,
            onSubtractIconClicked = viewModel::decrementStepGoal,
            value = stepGoal,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            imgText = "steps",
            img = painterResource(com.example.utility.R.drawable.walking)
        )

        ValueStepper(
            modifier = Modifier.fillMaxWidth()
                .height(120.dp)
                .padding(16.dp),
            onPlusIconClicked = viewModel::incrementConsumedGoal,
            onSubtractIconClicked = viewModel::decrementConsumedGoal,
            value = consumedGoal,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            imgText = "kcal consumed",
            img = painterResource(com.example.utility.R.drawable.fork)
        )
    }
}