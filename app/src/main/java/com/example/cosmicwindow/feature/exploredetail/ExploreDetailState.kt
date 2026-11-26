package com.example.cosmicwindow.feature.exploredetail

import com.example.cosmicwindow.model.SearchResultItem

data class ExploreDetailState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val searchItem: SearchResultItem? = null,
    val pictureLoaded: Boolean = false,
)
