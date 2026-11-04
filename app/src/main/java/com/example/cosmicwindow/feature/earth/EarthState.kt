package com.example.cosmicwindow.feature.earth

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
