package com.example.cosmicwindow.data.remote.dto.explore

data class QueryRequestDto(
    val filters: String?,
    val query: String,
    val pageSize: Int,
    val page: Int,
    val mediaType: String,
)
