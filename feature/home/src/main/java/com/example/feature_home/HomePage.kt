package com.example.feature_home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.utility.R
import com.example.utility.ui.composables.CustomCircularProgressIndicator
import com.example.utility.ui.composables.LogFoodCard
import com.example.utility.ui.composables.ProgressCard
import com.example.utility.ui.composables.StartSessionCard
import com.example.utility.service.foreground.SessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    onLogFoodClicked: () -> Unit = {},
    onStartWalkClicked: (Int, Int, SessionState) -> Unit = {a, b, c ->},
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val stepsTaken = viewModel.stepsTaken.collectAsState().value
    val caloriesBurned = viewModel.caloriesBurned.collectAsState().value
    val caloriesConsumed = viewModel.caloriesConsumed.collectAsState().value
    val calorieProgress = viewModel.caloriesBurnedProgress.collectAsState().value
    val currentDayFoodItems = viewModel.currentDayFoodItems.collectAsState().value
    val weight = viewModel.userWeight.collectAsState().value
    val stepGoal = viewModel.stepGoal.collectAsState().value
    val stepProgress = viewModel.stepGoalProgress.collectAsState().value
    val sessionActive = viewModel.sessionState.collectAsState().value
    val caloriesConsumedGoal = viewModel.caloriesConsumedProgress.collectAsState().value


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { CenterAlignedTopAppBar(
            title = {
                Text("Activity Overview")
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            CustomCircularProgressIndicator(
                modifier = Modifier.size(220.dp),
                progress = stepProgress,
                progressColor = colorResource(R.color.accent),
                trackColor = colorResource(R.color.trackColor),
                value = stepsTaken,
                labelText = "STEPS"
            )

            // Replace icon with some calorie icon
            ProgressCard(
                modifier = Modifier.size(width = 300.dp, height = 200.dp),
                value = caloriesBurned,
                titleText = "BURNED",
                titleTextStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                ),
                valueTextStyle = TextStyle(
                    color = colorResource(R.color.accent),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 36.sp
                ),
                icon = painterResource(R.drawable.walking),
                iconTint = colorResource(R.color.accent),
                progress = calorieProgress,
                trackColor = colorResource(R.color.trackColor),
                progressColor = colorResource(R.color.accent)
            )
            ProgressCard(
                modifier = Modifier.size(width = 300.dp, height = 200.dp),
                value = caloriesConsumed,
                titleText = "CALORIES CONSUMED",
                titleTextStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                ),
                valueTextStyle = TextStyle(
                    color = colorResource(R.color.lightGreenColor),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 36.sp,
                ),
                icon = painterResource(R.drawable.fork),
                iconTint = colorResource(R.color.lightGreenColor),
                progress = caloriesConsumedGoal,
                trackColor = colorResource(R.color.trackColor),
                progressColor = colorResource(R.color.accent)
            )

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
                    onClick = {
                        onStartWalkClicked(weight, stepGoal, sessionActive)
                    },
                    sessionActive = ((sessionActive == SessionState.RESUME) || (sessionActive == SessionState.PAUSE))
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
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage()
}