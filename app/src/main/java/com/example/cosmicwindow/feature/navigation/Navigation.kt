package com.example.cosmicwindow.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cosmicwindow.feature.apodDetail.ApodDetailsScreen
import com.example.cosmicwindow.feature.today.TodayScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.today
    ) {
        composable(Routes.today) {
            TodayScreen(
                onApodClick = { apod -> navController.navigate(Routes.apodDetailRoute(apod.date)) }
            )
        }
        composable(
            Routes.apodDetail,
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) {
            ApodDetailsScreen()
        }

    }
}


