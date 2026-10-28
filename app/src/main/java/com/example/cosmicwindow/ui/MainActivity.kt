package com.example.cosmicwindow.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.cosmicwindow.R
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
        enableEdgeToEdge()
        setContent {
            CosmicWindowTheme {
                var showDatePicker by remember { mutableStateOf(false) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppHeader(
                            modifier = Modifier,
                            leftIcon = R.drawable.header_image,
                            rightButtonIcon = R.drawable.calendar_icon,
                            onClick = { showDatePicker = true },
                        )
                    }
                ) { innerPadding ->

                    if (showDatePicker) {
                        CurrentDatePicker(
                            onDateSelected = { date -> Log.w("debug", "Date selected $date") },
                            onDismiss = { showDatePicker = false }
                        )
                    }
                    TodayScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}