package com.example.cosmicwindow.feature.earth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.EarthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EarthViewModel
    @Inject
    constructor(
        val earthRepository: EarthRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<EarthState>(EarthState())
        val uiState = _uiState.asStateFlow()
        private var apiJob: Job? = null

        init {
            loadEarthImages()
        }

        fun loadEarthImages() {
            _uiState.update { it.copy(isLoading = true, error = null) }
            apiJob?.cancel()
            apiJob =
                viewModelScope.launch {
                    earthRepository.getLatestEarthPictures().fold(onSuccess = { newImages ->
                        _uiState.update { it.copy(images = newImages, isLoading = false, error = null) }
                    }, onFailure = { err ->
                        _uiState.update {
                            it.copy(images = emptyList(), isLoading = false, error = err.message)
                        }
                    })
                }
        }
    }
