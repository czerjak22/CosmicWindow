package com.example.cosmicwindow.data.remote.dto.earth

import kotlinx.serialization.Serializable

@Serializable
data class CentroidCoordinates(
    val lat: Double,
    val lon: Double,
)
