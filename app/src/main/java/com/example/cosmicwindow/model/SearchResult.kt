package com.example.cosmicwindow.model

import java.time.LocalDate

data class SearchResult(
    val searchItems: List<SearchResultItem> = emptyList(),
    val totalFinds: Long,
) {
    override fun toString(): String {
        return "totalFinds:$totalFinds totalList:$searchItems"
    }
}

