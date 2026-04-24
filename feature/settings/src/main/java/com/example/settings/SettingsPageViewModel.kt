package com.example.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utility.data.MetricsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    fun onWeightChange(value: String) {
        _weight.value = value
    }

    init {
        viewModelScope.launch {
            metricsRepository.weight.collect { weight ->
                Timber.i("Collected weight: $weight")
            }
        }
    }

    fun saveWeight(value: Int) {
        viewModelScope.launch {
            Timber.i("Saving weight: $weight")
            metricsRepository.setNewWeight(value)
        }
    }
}