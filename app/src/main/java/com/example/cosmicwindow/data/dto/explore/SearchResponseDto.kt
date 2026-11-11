package com.example.cosmicwindow.data.dto.explore

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SearchResponseDto(
    @SerialName("collection") val collection: CollectionDto,
) {
    val items: List<ItemDto> get() = collection.items
    val totalHits: Long get() = collection.metadata?.totalHits ?: 0


    @Serializable
    class CollectionDto(
        val items: List<ItemDto> = emptyList(),
        val metadata: MetadataDto? = null,
    )

    @Serializable
    class MetadataDto(
        @SerialName("total_hits") val totalHits: Long,
    )

}

