package com.example.utility.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun Circular(
    current: Int,
    goal: Int,
) {
    val progress = if (goal > 0) {
        (current.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    CircularProgressIndicator(
        progress = { progress }
    )
}

@Preview(showBackground = true)
@Composable
fun CircularPreview() {
    Circular(
        current = 90,
        goal = 100,
    )
}