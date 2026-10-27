package com.example.cosmicwindow.feature.today

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodayViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<TodayState>(TodayState(null))
 val uiState = _uiState.asStateFlow();
    fun updateError()
    {
        _uiState.update { it.copy(error = "Baj van") }
    }

}