package com.example.cosmicwindow.data.dto.explore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDataDto(
    @SerialName("nasa_id") val nasaId: String,
    val title: String,

    @SerialName("date_created")
    val dateCreated: String,

    val description: String?=null,

    @SerialName("description_508")
    val description508: String="",
    val center: String? = null,
)
