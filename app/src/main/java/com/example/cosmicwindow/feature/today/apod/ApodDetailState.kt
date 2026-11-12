package com.example.cosmicwindow.feature.today.apod

import com.example.cosmicwindow.model.Apod

data class ApodDetailState(
    val apod: Apod? = null,
    val error: String? = null,
    val isLoading: Boolean = true,
)
