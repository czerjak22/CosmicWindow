package com.example.cosmicwindow.feature.explore

import com.example.cosmicwindow.model.QueryExplore
import com.example.cosmicwindow.model.SearchResultItem

data class ExploreState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val searchItems: List<SearchResultItem> = emptyList(),
    val queryState: QueryExplore = QueryExplore(),
    val endReached: Boolean = false,
)
