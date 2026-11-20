package com.example.cosmicwindow.model

import java.time.LocalDate

data class SavedItem(
    val id: Long,
    val imageName: String,
    val savedDate: LocalDate,
    val apod: Apod,
    val imagePath: String? = null,
)
