package com.example.cosmicwindow.feature.today

import com.example.cosmicwindow.model.Apod
import java.time.LocalDate

data class TodayState(
    val apods: List<Apod>,
    val error: String?,
    val isLoading: Boolean,
    val selectedDate: LocalDate,
    val featuredVideoApod: Apod?,
) {
    constructor() : this(
        apods = listOf<Apod>(),
        error = null,
        selectedDate = LocalDate.now(),
        featuredVideoApod = null,
        isLoading = true,
    )
}
