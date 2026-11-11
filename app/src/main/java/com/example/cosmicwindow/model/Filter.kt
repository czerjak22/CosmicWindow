package com.example.cosmicwindow.model

data class Filter(
    val text: String,
    val icon: Int,
    val isActive: Boolean = false,
    val value: String = text,
)
