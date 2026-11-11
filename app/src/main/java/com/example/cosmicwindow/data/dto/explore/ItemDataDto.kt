package com.example.cosmicwindow.data.dto.explore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDataDto(
    @SerialName("nasa_id") val nasaId: String,
    val title: String,
    @SerialName("date_created") val dateCreated: String,
    val description: String,
    val center: String? = null,
)
