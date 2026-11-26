package com.example.cosmicwindow.data.remote.dto.earth

import kotlinx.serialization.Serializable

@Serializable
data class TelescopePosition(
    val x: Double,
    val y: Double,
    val z: Double,
)
