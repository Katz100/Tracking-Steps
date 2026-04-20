package com.example.feature_home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.utility.composables.CaloriesBurnedCard
import com.example.utility.composables.CaloriesConsumedCard
import com.example.utility.composables.LogFoodCard
import com.example.utility.composables.StartSessionCard
import com.example.utility.composables.StepsTakenCard

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onLogFoodClicked: () -> Unit = {},
    onStartWalkClicked: (Int) -> Unit = {},
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val stepsTaken = viewModel.stepsTaken.collectAsState().value
    val caloriesBurned = viewModel.caloriesBurned.collectAsState().value
    val caloriesConsumed = viewModel.caloriesConsumed.collectAsState().value
    val calorieProgress = viewModel.calorieProgress.collectAsState().value
    val currentDayFoodItems = viewModel.currentDayFoodItems.collectAsState().value
    val weight = viewModel.weight.collectAsState().value

    LaunchedEffect(calorieProgress) {
        Log.d("TAG", "Calorie progress: ${calorieProgress}")
    }
    Column(
        modifier = modifier.fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepsTakenCard(
            modifier = Modifier.size(width = 300.dp, height = 100.dp)
                .padding(top = 20.dp, bottom = 20.dp),
            stepsTaken = stepsTaken,
        )
        CaloriesBurnedCard(
            modifier = Modifier.size(width = 300.dp, height = 200.dp),
            calories = caloriesBurned,
            progress = calorieProgress
        )
        Spacer(modifier = Modifier.height(30.dp))
        CaloriesConsumedCard(
            modifier = Modifier.size(width = 300.dp, height = 150.dp),
            calories = caloriesConsumed
        )
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.width(300.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            LogFoodCard(
                modifier = Modifier.size(width = 120.dp, height = 100.dp),
                onClick = onLogFoodClicked
            )
            Spacer(modifier = Modifier.weight(1f))
            StartSessionCard(
                modifier = Modifier.size(width = 120.dp, height = 100.dp),
                onClick = { onStartWalkClicked(weight) }
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
        ) {
            items(currentDayFoodItems, key = { it.id.toString() }) { foodItem ->
                Text("Food name: ${foodItem.foodName} Calories: ${foodItem.calories}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage()
}