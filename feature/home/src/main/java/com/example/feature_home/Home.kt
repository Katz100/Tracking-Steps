package com.example.feature_home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.utility.composables.Circular

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onRequestPermissions: () -> Unit,
    onWriteSteps: (Long) -> Unit,
    launchForeground: () -> Unit,
    stopForeground: () -> Unit,
    requestForeground: () -> Unit,
    onGoalChange: (String) -> Unit,
    steps: Int,
    goal: Int,
    goalValue: String,
    weightValue: String,
    onWeightChange: (String) -> Unit,
){
    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(steps.toString())

            Circular(
                steps,
                goal
            )

            Button(onClick = requestForeground) {
                Text("Request foreground")
            }

            TextField(
                value = weightValue,
                onValueChange = {
                    onWeightChange(it)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = {
                    Text("Enter weight")
                }
            )

            TextField(
                value = goalValue,
                onValueChange = {
                    onGoalChange(it)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(onClick = launchForeground) {
                Text("Launch foreground")
            }

            Button(onClick = stopForeground) {
                Text("stop foreground")
            }

            Button(
                modifier = Modifier.padding(bottom = 100.dp, top = 100.dp),
                onClick = onRequestPermissions
            ) {
                Text("Request permission")
            }

            Button(
                onClick = {
                    onWriteSteps(1000)
                }
            ) {
                Text("Write Steps")
            }
        }
    }
}