package com.example.cosmicwindow.feature.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.SavedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
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

        init {
            loadItems()
        }

        fun loadItems() {
            _uiState.update { it.copy(isLoading = true, error = "") }
            viewModelScope.launch {
                savedRepository.loadItems().fold(
                    onSuccess = { items ->
                        _uiState.update { it.copy(isLoading = false, error = "", items = items) }
                    },
                    onFailure = { err ->
                        _uiState.update { it.copy(isLoading = false, error = err.message) }
                    },
                )
            }
        }

        fun removeSavedItem() {
        }
    }
