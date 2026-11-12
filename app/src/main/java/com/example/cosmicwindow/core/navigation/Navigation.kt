package com.example.cosmicwindow.core.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cosmicwindow.feature.today.apod.ApodDetailsScreen
import com.example.cosmicwindow.feature.earth.EarthPage
import com.example.cosmicwindow.feature.explore.ExplorePage
import com.example.cosmicwindow.feature.explore.detail.ExploreDetailScreen
import com.example.cosmicwindow.feature.today.TodayScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val topLevelRoutes = remember { TopLevelDestination.entries.map { it.route } }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in topLevelRoutes
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppNavigationBar(
                    navController = navController,
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            modifier =
                modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            navController = navController,
            startDestination = Routes.explore,
        ) {
            composable(Routes.today) {
                TodayScreen(
                    onApodClick = { apod -> navController.navigate(Routes.apodDetailRoute(apod.date)) },
                )
            }
            composable(
                Routes.apodDetail,
                arguments = listOf(navArgument("date") { type = NavType.StringType }),
            ) {
                ApodDetailsScreen(
                    onClickBack = { navController.popBackStack() },
                )
            }
            composable(Routes.earth) {
                EarthPage()
            }
            composable(Routes.explore) {
                ExplorePage(
                    onItemClick = { nasaId -> navController.navigate(Routes.exploreDetailRoute(nasaId)) }
                )
            }
            composable(
                Routes.exploreDetail,
                arguments = listOf(navArgument("nasaId") { type = NavType.StringType }),
            ) {
                ExploreDetailScreen(
                    onClickBack = { navController.popBackStack() },
                )
            }
            composable(Routes.saved) {
                Text("Saved page")
            }
        }
    }
}
