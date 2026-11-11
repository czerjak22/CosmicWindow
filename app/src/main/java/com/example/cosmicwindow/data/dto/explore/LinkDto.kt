package com.example.cosmicwindow.data.dto.explore

import kotlinx.serialization.Serializable

@Serializable
data class LinkDto(
    val href: String,
    val rel: String? = null,
)
