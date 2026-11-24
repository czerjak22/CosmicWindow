package com.example.cosmicwindow.feature.earth

import com.example.cosmicwindow.model.Earth
import com.example.cosmicwindow.model.ProjectedPoint
import java.time.LocalDate

data class EarthState(
    val images: List<Earth> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = true,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedIndex: Int? = null,
    val isLocationEnabled: Boolean = false,
    val selectedIndexLocation: ProjectedPoint? = null,
)
