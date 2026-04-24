package com.example.utility.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color,
    trackColor: Color,
    value: Int,
    labelText: String,
) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = progress,
            color = if (progress >= 1.0f) Color.Green else progressColor,
            trackColor = trackColor
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = labelText,
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 1.5.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCircularProgressIndicatorPreview() {
    CustomCircularProgressIndicator(
        modifier = Modifier.size(200.dp),
        progress = 0.5f,
        progressColor = Color.Blue,
        trackColor = Color.Gray,
        value = 2000,
        labelText = "STEPS",
    )
}