package com.example.cosmicwindow.feature.earth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.EarthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@HiltViewModel
class EarthViewModel
@Inject
constructor(
    val earthRepository: EarthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EarthState())
    val uiState = _uiState.asStateFlow()
    private var apiJob: Job? = null
    private var playbackJob: Job? = null

    init {
        loadEarthImages()
    }

    fun loadEarthImages() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        apiJob?.cancel()
        apiJob =
            viewModelScope.launch {
                earthRepository.getLatestEarthPictures().fold(onSuccess = { newImages ->
                    var selected: Int? = null
                    if (newImages.isNotEmpty()) {
                        selected = 0
                    }
                    _uiState.update {
                        it.copy(
                            images = newImages,
                            isLoading = false,
                            error = null,
                            selectedIndex = selected,
                        )
                    }
                    Log.w(
                        "modelView",
                        "Loaded images to state successfully imageSize:${newImages.size} , " +
                            "date:${if (newImages.isNotEmpty()) newImages.first().date else "images are empty"}",
                    )
                }, onFailure = { err ->
                    _uiState.update {
                        it.copy(images = emptyList(), isLoading = false, error = err.message)
                    }
                    Log.w(
                        "modelView",
                        "error: $err",
                    )
                })
            }
    }

    fun updateSelected(newIndex: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedIndex = newIndex,
                )
            }
        }
    }

    fun startPlayback() {
        if (uiState.value.images.size <= 1) return

        playbackJob?.cancel()

        playbackJob = viewModelScope.launch {
            while (isActive) {
                delay(500.milliseconds)
                updateSelected(((uiState.value.selectedIndex ?: 0) + 1) % uiState.value.images.size)
            }
        }
    }

    fun stopPlayback() {
        playbackJob?.cancel()
        playbackJob = null
    }
}
