package com.example.cosmicwindow.model

import kotlinx.serialization.SerialName
import java.time.LocalDate


data class Apod(
    val date: LocalDate,
    val title: String,
    val permalink: String,
    val explanation: String,
    val credit: String,
    val copyright: String? = null,
    val alt: String,
    val url: String,
    val hdurl: String? = null,
    val mediaType: Mediatype,
    val videoUrl: String?,
) {
    override fun toString(): String {
        return "Apod:$date, $title, $mediaType"
    }
}
