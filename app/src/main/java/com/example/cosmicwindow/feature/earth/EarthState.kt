package com.example.cosmicwindow.feature.earth

import com.example.cosmicwindow.model.Earth
import java.time.LocalDate

data class EarthState(
    val images: List<Earth>,
    val error: String?,
    val isLoading: Boolean,
    val selectedDate: LocalDate,
) {
    constructor() : this(
        emptyList<Earth>(),
        error = null,
        selectedDate = LocalDate.now(),
        isLoading = true,
    )
}
