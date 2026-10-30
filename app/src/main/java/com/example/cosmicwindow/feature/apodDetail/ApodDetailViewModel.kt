package com.example.cosmicwindow.feature.apodDetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.ApodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class ApodDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: ApodRepository
) : ViewModel() {
    private val date = LocalDate.parse(checkNotNull(savedStateHandle.get<String>("date")))

    private val _uiState = MutableStateFlow<ApodDetailState>(ApodDetailState())
    val uiState = _uiState.asStateFlow()

    init {
        loadApod()
    }

    fun loadApod() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            Log.w("debug","$date ez amit megkaptam!")
            repo.getApod(date).fold(
                onSuccess = { newApod ->
                    _uiState.update { it.copy(apod = newApod, isLoading = false) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(error = err.message, isLoading = false) }
                }
            )
        }
    }
}
