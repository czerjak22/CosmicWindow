package com.example.cosmicwindow.feature.explore

data class ExploreState(
    val error: String?,
    val isLoading: Boolean,
) {
    constructor() : this(
        error = null,
        isLoading = false,
    )
}
