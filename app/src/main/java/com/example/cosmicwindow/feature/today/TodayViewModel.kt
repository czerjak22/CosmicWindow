package com.example.cosmicwindow.feature.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.repository.ApodRepository
import com.example.cosmicwindow.data.repository.SavedRepository
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodayViewModel
    @Inject
    constructor(
        val apodRepository: ApodRepository,
        val savedRepository: SavedRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<TodayState>(TodayState())
        private var loadApodsJob: Job? = null
        private var saveJob: Job? = null
        val uiState = _uiState.asStateFlow()

        fun updateError() {
            _uiState.update { it.copy(error = "Baj van") }
        }

        init {
            getCurrentApods()
        }

        fun getCurrentApods() {
            loadApodsJob?.cancel()
            loadApodsJob =
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, error = null) }

                    apodRepository.getApodWeek(_uiState.value.selectedDate).fold(
                        onSuccess = { apods ->
                            val first = apods.firstOrNull()
                            val newApods =
                                when {
                                    first == null -> {
                                        Log.e("debug", "Apods are empty!")
                                        apods
                                    }

                                    first.mediaType == MediaType.VIDEO -> apods
                                    else ->
                                        listOf(first.copy(isSaved = checkSaved(first))) + apods.drop(1)
                                }

                            val videoApod = searchForFirstVideoApod(newApods)
                            _uiState.update {
                                it.copy(
                                    apods = newApods,
                                    error = null,
                                    featuredVideoApod = videoApod,
                                    isLoading = false,
                                )
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                it.copy(
                                    error = error.message,
                                    isLoading = false,
                                    apods = emptyList(),
                                    featuredVideoApod = null,
                                )
                            }
                        },
                    )
                }
        }

        fun reevaluateTodayApod() {
            if (uiState.value.apods.isEmpty()) {
                return
            }

            viewModelScope.launch {
                val newApods =
                    _uiState.value.apods.map {
                        when {
                            it.postId ==
                                _uiState.value.apods
                                    .first()
                                    .postId
                            -> it.copy(isSaved = checkSaved(it))

                            else -> it
                        }
                    }
                _uiState.update { it.copy(apods = newApods) }
            }
        }

        fun updateCurrentDay(newDate: LocalDate) {
            _uiState.update {
                it.copy(
                    selectedDate = newDate,
                    apods = emptyList(),
                    featuredVideoApod = null,
                )
            }
            Log.w("debug", "Updated selected date to: $newDate")
            getCurrentApods()
        }

        private fun searchForFirstVideoApod(apods: List<Apod>): Apod? =
            apods.firstOrNull {
                it.mediaType == MediaType.VIDEO && it.videoUrl != null
            }

        fun toggleApodSave(apod: Apod) {
            if (apod.mediaType == MediaType.VIDEO) return
            val newApods =
                _uiState.value.apods.map {
                    when {
                        it.postId ==
                            _uiState.value.apods
                                .first()
                                .postId
                        -> it.copy(isSaved = !apod.isSaved)

                        else -> it
                    }
                }
            _uiState.update { it.copy(apods = newApods) }
            saveOrDeleteApod(apod)
        }

        fun saveOrDeleteApod(apod: Apod) {
            saveJob?.cancel()
            saveJob =
                viewModelScope.launch {
                    val willBeSaved = !apod.isSaved
                    val result =
                        if (willBeSaved) {
                            savedRepository.saveItem(apod)
                        } else {
                            savedRepository.deleteItem(apod)
                        }

                    result.fold(
                        onSuccess = {
                            Log.w("ViewModel", " ${if (willBeSaved) "Saving" else "Deleting"} apod")
                            _uiState.update { state ->
                                state.copy(
                                    apods =
                                        state.apods.map {
                                            if (it.postId == apod.postId) it.copy(isSaved = willBeSaved) else it
                                        },
                                )
                            }
                        },
                        onFailure = { err ->
                            _uiState.update { it.copy(error = err.message ?: "error") }
                        },
                    )
                }
        }

        private suspend fun checkSaved(apod: Apod): Boolean {
            Log.w("ViewModel", "$apod to be saved!")
            savedRepository.isSaved(apod.postId).fold(
                onSuccess = { isSaved ->
                    return isSaved
                },
                onFailure = { err ->
                    _uiState.update { it.copy(error = err.message ?: "error") }
                    return false
                },
            )
        }
    }
