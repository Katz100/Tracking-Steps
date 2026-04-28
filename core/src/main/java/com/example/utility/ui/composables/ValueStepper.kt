package com.example.utility.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.utility.R

@Composable
fun ValueStepper(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    onPlusIconClicked: () -> Unit,
    onSubtractIconClicked: () -> Unit,
    value: Int,
    img: Painter,
    imgText: String
) {
    val haptics = LocalHapticFeedback.current

    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = CircleShape
                    )
                ,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                    onSubtractIconClicked()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Add"
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    text = value.toString(),
                    style = textStyle
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = img,
                        contentDescription = "Img",
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = imgText,
                        style = TextStyle(
                            color = Color.Gray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
                ,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                    onPlusIconClicked()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValueStepperPreview() {
    ValueStepper(
        modifier = Modifier.fillMaxWidth()
            .height(150.dp)
            .padding(16.dp),
        onPlusIconClicked = {},
        value = 200,
        onSubtractIconClicked = {},
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        img = painterResource(R.drawable.calories),
        imgText = "kCal"
    )
}