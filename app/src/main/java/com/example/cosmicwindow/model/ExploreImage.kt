package com.example.cosmicwindow.model

import java.time.LocalDate

data class ExploreImage(
    val id: String, // PIA25301 (the badge on the tile)
    val title: String,
    val date: LocalDate?,
    val thumbnailUrl: String?,
    val description: String?,
    val keywords: List<String>,
    val badge: BadgeTitle?,
    val isFavorite: Boolean = false,
)
