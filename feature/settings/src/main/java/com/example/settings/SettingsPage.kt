package com.example.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsPage(
    viewModel: SettingsPageViewModel = hiltViewModel()
) {
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current
    val weight = viewModel.weight.collectAsState().value
    val savedWeight = viewModel.savedWeight.collectAsState().value
    val weightError = viewModel.weightError.collectAsState().value

    LaunchedEffect(savedWeight) {
        if (weight.isBlank() && savedWeight != -1) {
            viewModel.onWeightChange(savedWeight.toString())
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = weight,
            onValueChange = viewModel::onWeightChange,
            singleLine = true,
            isError = weightError != InputError.NO_ERROR,
            supportingText = {
                if (weightError != InputError.NO_ERROR) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Invalid weight!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                }
            ),
            trailingIcon = {
                if (weightError != InputError.NO_ERROR)
                    Icon(
                        Icons.Filled.Error,
                        "error",
                        tint = MaterialTheme.colorScheme.error
                    )
            },
        )

        Button(
            onClick = {
                viewModel.save(weight.toIntOrNull() ?: 180)
                Toast.makeText(context, "Settings have been saved", Toast.LENGTH_SHORT).show()
            },
            enabled = weightError == InputError.NO_ERROR
        ) {
            Text("Save")
        }
    }
}
