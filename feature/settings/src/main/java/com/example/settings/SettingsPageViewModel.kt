package com.example.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.repository.MetricsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsPageViewModel @Inject constructor(
    private val metricsRepository: MetricsRepository
): ViewModel() {
    val savedWeight = metricsRepository.weight
        .stateIn(
            scope = viewModelScope,
            initialValue = -1,
            started = SharingStarted.Eagerly
        )

    private val _weight = MutableStateFlow<String>("")
    val weight: StateFlow<String> = _weight

    val weightError: StateFlow<InputError> = _weight.map { weightText ->
        val weight = weightText.toIntOrNull()
        when {
            weightText.isBlank() -> InputError.BLANK_TEXT
            weight == null -> InputError.BLANK_TEXT
            weight < 0 -> InputError.NEGATIVE_WEIGHT
            weight > 1000 -> InputError.WEIGHT_TOO_LARGE
            else -> InputError.NO_ERROR
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = InputError.NO_ERROR,
        started = SharingStarted.Eagerly
    )

    fun onWeightChange(value: String) {
        _weight.value = value
    }

    fun save(weight: Int) {
        viewModelScope.launch {
            Timber.i("Saving weight: $weight")
            metricsRepository.setNewWeight(weight)
        }
    }

     fun weightErrorText(error: InputError): String {
        return when (error) {
            InputError.NEGATIVE_WEIGHT -> "Weight can not be negative"
            InputError.BLANK_TEXT -> "Weight can not be blank"
            InputError.WEIGHT_TOO_LARGE -> "Weight can not be above 1000 lbs"
            InputError.NO_ERROR -> ""
        }
    }
}

enum class InputError {
    NEGATIVE_WEIGHT,
    BLANK_TEXT,
    WEIGHT_TOO_LARGE,
    NO_ERROR,
}

