package com.example.cosmicwindow.core.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cosmicwindow.core.designsystem.theme.Spacing

@Composable
fun AppNavigationBar(modifier: Modifier = Modifier, navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar(
        modifier =
            modifier.drawBehind {
                val h = Spacing.s16.toPx()
                drawRect(
                    brush =
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f)),
                            startY = -h,
                            endY = 0f,
                        ),
                    topLeft = Offset(0f, -h),
                    size = Size(size.width, h),
                )
            },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        TopLevelDestination.entries.forEach { dest ->
            val selected = currentDestination?.route == dest.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(dest.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painterResource(dest.icon),
                        contentDescription = null,
                    )
                },
                label = { Text(dest.label) },
                alwaysShowLabel = true,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    ),
            )
        }
    }
}
