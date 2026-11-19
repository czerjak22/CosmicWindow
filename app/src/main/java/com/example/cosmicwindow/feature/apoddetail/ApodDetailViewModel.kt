package com.example.cosmicwindow.feature.apoddetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.repository.ApodRepository
import com.example.cosmicwindow.data.repository.SavedRepository
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.MediaType
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
    private val _uiState = MutableStateFlow(ApodDetailState())
    val uiState = _uiState.asStateFlow()

    init {
        loadApod()
    }

    fun loadApod() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Log.w("debug", "$date ez amit megkaptam!")

            savedRepository.findItemByCreatedDate(date).fold(
                onSuccess = { item ->
                    if (item == null) {
                        return@fold
                    }
                    checkSaved(item.apod)
                    _uiState.update { it.copy(apod = item.apod.copy(hdUrl = item.imagePath), isLoading = false) }
                    return@launch
                },
                onFailure = { err ->
                    _uiState.update { it.copy(error = err.message ?: "error", isLoading = false) }
                    return@launch
                },
            )
            apodRepository.getApod(date).fold(
                onSuccess = { newApod ->
                    checkSaved(newApod)
                    _uiState.update { it.copy(apod = newApod, isLoading = false) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(error = err.message ?: "error", isLoading = false) }
                },
            )
        }
    }


    fun toggleApodSave() {
        if (_uiState.value.apod == null || _uiState.value.apod!!.mediaType == MediaType.VIDEO) {
            return
        }
        _uiState.update { it.copy(apod = it.apod!!.copy(isSaved = !it.apod.isSaved)) }
        saveOrDeleteApod()
    }

    fun saveOrDeleteApod() {
        val apod = _uiState.value.apod ?: return
        saveJob?.cancel()
        saveJob =
            viewModelScope.launch {
                val willBeSaved = apod.isSaved
                val result =
                    if (willBeSaved) {
                        savedRepository.saveItem(apod)
                    } else {
                        savedRepository.deleteItem(apod)
                    }
                result.fold(
                    onSuccess = {
                        Log.w("ViewModel", " ${if (willBeSaved) "Saving" else "Deleting"} apod")
                        _uiState.update {
                            it.copy(apod = it.apod!!.copy(isSaved = willBeSaved))
                        }
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
            savedRepository.isSaved(apod.postId).fold(
                onSuccess = { savedItem ->
                    _uiState.update { it.copy(apod = apod.copy(isSaved = savedItem)) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(error = err.message ?: "error") }
                },
            )
        }
    }
}
