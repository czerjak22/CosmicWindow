package com.example.cosmicwindow.model

import java.time.LocalDate

data class SearchResultItem(
    val nasaId: String,
    val title: String,
    val dateCreated: LocalDate,
    val thumbnailUrl: String?,
    val originalUrl: String?,
    val description: String,
    val center:String?,
)
