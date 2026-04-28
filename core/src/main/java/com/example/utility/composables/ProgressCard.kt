package com.example.utility.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.utility.R

@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    value: Int,
    titleText: String,
    titleTextStyle: TextStyle,
    valueTextStyle: TextStyle,
    icon: Painter,
    iconTint: Color,
    progress: Float,
    trackColor: Color,
    progressColor: Color,
) {
    val goalCompleted = progress >= 1.0f

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = titleText,
                        style = titleTextStyle
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "%,d".format(value),
                        style = valueTextStyle
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (goalCompleted) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF4CAF50).copy(alpha = 0.15f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Goal completed",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        Icon(
                            painter = icon,
                            contentDescription = "Icon",
                            tint = iconTint,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 25.dp)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(trackColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (goalCompleted) Color.Green else progressColor)
                )
            }
        }
    }
}

@Preview
@Composable
fun ProgressCardPreview() {
    ProgressCard(
        value = 2000,
        modifier = Modifier.size(width = 300.dp, height = 200.dp),
        titleText = "BURNED",
        titleTextStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp
        ),
        valueTextStyle = TextStyle(
            color = colorResource(R.color.accent),
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 36.sp
        ),
        icon = painterResource(R.drawable.walking),
        iconTint = colorResource(R.color.accent),
        progress = 1.9f,
        trackColor = colorResource(R.color.trackColor),
        progressColor = colorResource(R.color.accent)
    )
}