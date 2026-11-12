package com.example.cosmicwindow.feature.explore.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.ExploreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExploreDetailViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val exploreRepository: ExploreRepository
) : ViewModel() {

    val id = savedStateHandle.get<String>("nasaId")
    private val _uiState = MutableStateFlow(
        ExploreDetailState()
    )
    val uiState = _uiState.asStateFlow()


    init {
        loadSearchItem()
    }

    fun loadSearchItem() {
        if (id == null) {
            _uiState.update { it.copy(isLoading = false, error = "Something went wrong") }
        }
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            exploreRepository.getItemDetailsById(id!!).fold(
                onSuccess = { result ->
                    _uiState.update { it.copy(searchItem = result, isLoading = false, error = null) }
                },
                onFailure = { err ->
                    if (err is CancellationException) {
                        throw err
                    }
                    _uiState.update { it.copy(isLoading = false, error = err.message) }
                }
            )
        }
    }

    fun pictureLoaded() {
        _uiState.update { it.copy(pictureLoaded = true) }
    }

    fun onErrorRetry() {
        loadSearchItem()
    }

}
