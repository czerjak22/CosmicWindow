package com.example.cosmicwindow.model

import java.time.LocalDate

data class SavedItem(
    val id: Long,
    val imageName: String,
    val title: String,
    val savedDate: LocalDate,
    val description: String,
)
