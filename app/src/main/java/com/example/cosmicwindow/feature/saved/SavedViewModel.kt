package com.example.cosmicwindow.feature.saved

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SavedViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedState())
    val uiState = _uiState.asStateFlow()

    private final val saveFolderName = "images"


    init {
        loadItems()
    }

    fun loadItems() {

        _uiState.update { it.copy(isLoading = true, error = "") }


    }

    fun removeSavedItem() {

    }

}
