package com.example.cosmicwindow.feature.today.apod

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.ApodRepository
import com.example.cosmicwindow.data.SavedRepository
import com.example.cosmicwindow.model.Apod
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class ApodDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val apodRepository: ApodRepository,
        private val savedRepository: SavedRepository,
    ) : ViewModel() {
        private val date = LocalDate.parse(checkNotNull(savedStateHandle.get<String>("date")))
        private var saveJob: Job? = null
        private val _uiState = MutableStateFlow<ApodDetailState>(ApodDetailState())
        val uiState = _uiState.asStateFlow()

        init {
            loadApod()
        }

        fun loadApod() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                Log.w("debug", "$date ez amit megkaptam!")
                apodRepository.getApod(date).fold(
                    onSuccess = { newApod ->
                        _uiState.update { it.copy(apod = newApod, isLoading = false) }
                        checkSaved(newApod)
                    },
                    onFailure = { err ->
                        _uiState.update { it.copy(error = err.message ?: "error", isLoading = false) }
                    },
                )
            }
        }

        fun savedApod(apod: Apod) {
            if (uiState.value.isSaved) {
                return
            }
            saveJob?.cancel()
            saveJob =
                viewModelScope.launch {
                    Log.w("ViewModel", "$apod to be saved!")
                    savedRepository.saveItem(apod).fold(
                        onSuccess = { newApod ->
                            _uiState.update { it.copy(isSaved = true) }
                        },
                        onFailure = { err ->
                            _uiState.update { it.copy(error = err.message ?: "error") }
                        },
                    )
                }
        }

        fun removeSavedApod(apod: Apod) {
            if (!uiState.value.isSaved) {
                return
            }
            saveJob?.cancel()
            saveJob
            saveJob =
                viewModelScope.launch {
                    Log.w("ViewModel", "$apod to be saved!")
                    savedRepository.deleteItem(apod).fold(
                        onSuccess = { newApod ->
                            _uiState.update { it.copy(isSaved = false) }
                        },
                        onFailure = { err ->
                            _uiState.update { it.copy(error = err.message ?: "error") }
                        },
                    )
                }
        }

        fun checkSaved(apod: Apod) {
            viewModelScope.launch {
                Log.w("ViewModel", "$apod to be saved!")
                savedRepository.findItemByPostId(apod.postId).fold(
                    onSuccess = { savedItem ->
                        _uiState.update { it.copy(isSaved = savedItem != null) }
                    },
                    onFailure = { err ->
                        _uiState.update { it.copy(error = err.message ?: "error") }
                    },
                )
            }
        }
    }
