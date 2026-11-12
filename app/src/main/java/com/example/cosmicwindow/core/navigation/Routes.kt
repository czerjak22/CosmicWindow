package com.example.cosmicwindow.core.navigation

import java.time.LocalDate

object Routes {
    val today = "Today"
    val apodDetail = "apodDetail/{date}"

    fun apodDetailRoute(date: LocalDate) = "apodDetail/$date"

    val earth = "Earth"
    val explore = "Explore"
    val exploreDetail = "exploreDetail/{nasaId}"
    fun exploreDetailRoute(nasaId: String) = "exploreDetail/$nasaId"
    val saved = "Saved"
}
