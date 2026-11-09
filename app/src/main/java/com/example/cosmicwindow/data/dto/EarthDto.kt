package com.example.cosmicwindow.data.dto

import com.example.cosmicwindow.data.network.LocaleDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class EarthDto(
    @SerialName("image") val imageName: String,
    @Serializable(LocaleDateTimeSerializer::class)
    val date: LocalDateTime,
    val caption: String,
)
