package com.example.cosmicwindow.feature.saved

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.SavedRepository
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.SavedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SavedViewModel
    @Inject
    constructor(
        val savedRepository: SavedRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SavedState())
        val uiState = _uiState.asStateFlow()
        private var saveJob: Job? = null
        private var loadJob: Job? = null

        init {
            loadItems()
        }

        fun loadItems() {
            if (loadJob?.isActive == true) {
                return
            }
            _uiState.update { it.copy(isLoading = true, error = null) }
            loadJob =
                viewModelScope.launch {
                    savedRepository.loadItems().fold(
                        onSuccess = { items ->
                            val folderSize = savedRepository.getImageFolderSizeMb()
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    items = items,
                                    folderSize = folderSize,
                                )
                            }
                        },
                        onFailure = { err ->
                            _uiState.update { it.copy(isLoading = false, error = err.message) }
                        },
                    )
                }
        }

        fun onErrorRetry() {
            loadItems()
        }

        fun reevaluateApod(savedItem: SavedItem) {
            if (uiState.value.items.isEmpty()) {
                return
            }

            viewModelScope.launch {
                val newItems =
                    _uiState.value.items.map {
                        when {
                            it.id == savedItem.id
                            -> it.copy(apod = it.apod.copy(isSaved = checkSaved(it.apod)))

                            else -> it
                        }
                    }
                _uiState.update { it.copy(items = newItems) }
            }
        }

        fun toggleItemSave(savedItem: SavedItem) {
            val newItems =
                _uiState.value.items.map {
                    when {
                        it.id == savedItem.id
                        ->
                            it.copy(
                                apod = it.apod.copy(isSaved = !savedItem.apod.isSaved),
                            )

                        else -> it
                    }
                }
            _uiState.update { it.copy(items = newItems) }
            // saveOrDeleteApod(savedItem)
        }

        fun saveOrDeleteApod(savedItem: SavedItem) {
            saveJob?.cancel()
            saveJob =
                viewModelScope.launch {
                    val willBeSaved = !savedItem.apod.isSaved
                    val result =
//                    if (willBeSaved) {
//                           savedRepository.saveItem(savedItem.apod)
//                    } else {
                        savedRepository.deleteItem(savedItem)
                    //   }

                    result.fold(
                        onSuccess = {
                            Log.w("ViewModel", " ${if (willBeSaved) "Saving" else "Deleting"} apod")
                            _uiState.update {
                                it.copy(
                                    items =
                                        it.items.map {
                                            if (it.id ==
                                                savedItem.id
                                            ) {
                                                it.copy(apod = it.apod.copy(isSaved = willBeSaved))
                                            } else {
                                                it
                                            }
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
