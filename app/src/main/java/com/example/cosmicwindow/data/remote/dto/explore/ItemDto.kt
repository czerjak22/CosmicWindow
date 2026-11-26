package com.example.cosmicwindow.data.remote.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val data: List<ItemDataDto> = emptyList(),
    val links: List<LinkDto> = emptyList(),
)
