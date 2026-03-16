package com.example.feature_home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onRequestPermissions: () -> Unit,
    onWriteSteps: (Long) -> Unit,
    steps: Int,
){
    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column() {
            Text(steps.toString())

            Button(
                modifier = Modifier.padding(bottom = 100.dp),
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