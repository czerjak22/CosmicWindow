package com.example.cosmicwindow.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.cosmicwindow.R

enum class TopLevelDestination(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    TODAY(Routes.today, R.string.navigation_today_label, R.drawable.today_icon),
    EARTH(Routes.earth, R.string.navigation_earth_label, R.drawable.earth_icon),
    EXPLORE(Routes.explore, R.string.navigation_explore_label, R.drawable.explore_icon),
    SAVED(Routes.saved, R.string.navigation_saved_label, R.drawable.bookmark_icon),
}
