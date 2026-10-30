package com.example.cosmicwindow.feature.apodDetail

import com.example.cosmicwindow.model.Apod

data class ApodDetailState(
    val apod: Apod?=null,
    val error: String? = null,
    val isLoading: Boolean = true,
){

}