package com.example.cosmicwindow.feature.explore

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ExploreViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(ExploreState())
        val uiState = _uiState.asStateFlow()
        private var apiJob: Job? = null
    }
