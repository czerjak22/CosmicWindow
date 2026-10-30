package com.example.cosmicwindow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.cosmicwindow.feature.navigation.Navigation
import com.example.cosmicwindow.feature.today.TodayScreen
import com.example.cosmicwindow.ui.components.AppHeader
import com.example.cosmicwindow.ui.components.CurrentDatePicker
import com.example.cosmicwindow.ui.theme.CosmicWindowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(

        )
        setContent {
            CosmicWindowTheme {
                Navigation()
            }
        }
    }
}