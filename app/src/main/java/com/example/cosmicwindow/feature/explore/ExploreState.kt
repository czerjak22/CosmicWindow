package com.example.cosmicwindow.feature.explore

import com.example.cosmicwindow.model.FilterState

data class ExploreState(
    val error: String?,
    val isLoading: Boolean,
    val filters: List<FilterState>,
) {
    constructor() : this(
        error = null,
        isLoading = false,
        filters = emptyList(),
    )
}
