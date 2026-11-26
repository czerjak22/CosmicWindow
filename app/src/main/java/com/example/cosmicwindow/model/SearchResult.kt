package com.example.cosmicwindow.model

data class SearchResult(
    val searchItems: List<SearchResultItem> = emptyList(),
    val totalFinds: Long,
) {
    override fun toString(): String = "totalFinds:$totalFinds totalList:$searchItems"
}
