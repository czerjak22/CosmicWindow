package com.example.cosmicwindow.data.dto

import com.example.cosmicwindow.data.network.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class EarthDto(
    @SerialName("image") val imageName: String,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val caption: String,
)
