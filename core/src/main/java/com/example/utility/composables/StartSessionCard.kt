package com.example.utility.composables

import com.example.utility.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StartSessionCard(
    modifier: Modifier = Modifier,
    sessionActive: Boolean,
    onClick: () -> Unit,
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF9800),
            Color(0xFFFFB74D)
        )
    )

    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.walking),
                contentDescription = "Icon",
                modifier = Modifier.size(25.dp)
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = if (sessionActive) "END SESSION"  else "BEGIN SESSION",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}

@Preview
@Composable
fun StartSessionCardPreview() {
    StartSessionCard(
        modifier = Modifier.size(100.dp),
        onClick = {},
        sessionActive = false
    )
}