package com.example.cosmicwindow.feature.earth

import com.example.cosmicwindow.model.Apod
import java.time.LocalDate

data class EarthState(
    val error: String?,
    val isLoading: Boolean,
    val selectedDate: LocalDate,

    ) {
    constructor() : this(
        error = null,
        selectedDate = LocalDate.now(),
        isLoading = true,
    )
}
