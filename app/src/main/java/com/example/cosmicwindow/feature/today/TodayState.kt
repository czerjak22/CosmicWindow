package com.example.cosmicwindow.feature.today

import com.example.cosmicwindow.model.Apod
import java.time.LocalDate

data class TodayState(
    val apods: List<Apod> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = true,
    val selectedDate: LocalDate = LocalDate.now(),
    val featuredVideoApod: Apod? = null,
)
