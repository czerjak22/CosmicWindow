package com.example.cosmicwindow.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    val items: List<ItemDto> = emptyList(),
    val metadata: MetadataDto? = null,
    val links: List<PageLinkDto> = emptyList(),
)

@Serializable
data class PageLinkDto(
    val rel: String,
    val prompt: String? = null,
    val href: String,
)

@Serializable
data class MetadataDto(
    @SerialName("total_hits") val totalHits: Int = 0,
)
