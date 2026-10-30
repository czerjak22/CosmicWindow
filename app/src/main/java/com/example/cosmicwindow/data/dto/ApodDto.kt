package com.example.cosmicwindow.data.dto

import com.example.cosmicwindow.data.network.LocalDateSerializer
import com.example.cosmicwindow.model.Apod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ApodDto(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val title: String,
    val permalink:String,
    val explanation: String,
    val credit: String,
    val copyright: String? = null,
    val alt:String,
    val url: String,
    val hdurl: String? = null,
    @SerialName("media_type") val mediaType: String,
    @SerialName("basic_html") val basicHtml: String,
)

