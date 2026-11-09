package com.example.cosmicwindow.core.navigation

import androidx.annotation.DrawableRes
import com.example.cosmicwindow.R

enum class TopLevelDestination(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
) {
    TODAY(Routes.today, "Today", R.drawable.today_icon),
    EARTH(Routes.earth, "Earth", R.drawable.earth_icon),
    EXPLORE(Routes.explore, "Explore", R.drawable.explore_icon),
    SAVED(Routes.saved, "Saved", R.drawable.bookmark_icon),
}
