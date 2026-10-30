package com.example.cosmicwindow.feature.navigation

import java.time.LocalDate

object Routes {
    val today = "Today"
    val apodDetail = "apodDetail/{date}"
    fun apodDetailRoute(date: LocalDate) = "apodDetail/$date"
    val earth = "Earth"
    val explore = "Explore"
}