package com.example.cosmicwindow.model

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
    val mediaType: MediaType,
    val videoUrl: String?,
) {
    override fun toString(): String = "Apod:$date, $title, $mediaType"
}
