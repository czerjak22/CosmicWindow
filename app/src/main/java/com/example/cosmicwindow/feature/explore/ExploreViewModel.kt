package com.example.cosmicwindow.feature.explore

import androidx.lifecycle.ViewModel
import com.example.cosmicwindow.R
import com.example.cosmicwindow.model.FilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ExploreViewModel
@Inject
constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ExploreState())
    val uiState = _uiState.asStateFlow()
    private var apiJob: Job? = null

    init {
        loadArchive()
    }

    fun loadArchive() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        val filterList = generateFilters()
        _uiState.update { it.copy(isLoading = false, filters = filterList) }
    }

    fun generateFilters(): List<FilterState> {
        val list = mutableListOf<FilterState>()
        list.add(
            FilterState(
                text = "James Webb",
                icon = R.drawable.today_icon,
            )
        )
        list.add(
            FilterState(
                text = "Mars",
                icon = R.drawable.earth_icon,
            )
        )
        list.add(
            FilterState(
                text = "Moon",
                icon = R.drawable.moon_icon,
            )
        )
        list.add(
            FilterState(
                text = "Nebula",
                icon = R.drawable.nebula_icon,
            )
        )
        return list
    }

    fun toggleLoading() {
        _uiState.update { it.copy(isLoading = !it.isLoading) }
    }

    fun toggleFilter(index: Int) {
        val newFilters = _uiState.value.filters.map { item ->
            when {
                _uiState.value.filters.indexOf(item) == index ->
                    item.copy(isActive = !item.isActive)

                else -> item
            }

        }
        _uiState.update { it.copy(filters = newFilters) }
    }
}
