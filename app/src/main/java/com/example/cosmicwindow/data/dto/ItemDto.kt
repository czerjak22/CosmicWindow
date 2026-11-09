package com.example.cosmicwindow.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val data: List<ItemDataDto> = emptyList(),
    val links: List<ItemLinkDto> = emptyList(),
)

@Serializable
data class ItemDataDto(
    @SerialName("nasa_id") val nasaId: String,
    val title: String = "",
    @SerialName("date_created") val dateCreated: String? = null,
    val description: String? = null,
    val keywords: List<String> = emptyList(),
    val center: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
)

@kotlinx.serialization.Serializable
data class ItemLinkDto(
    val href: String,
    val rel: String? = null,
    val render: String? = null,
)
