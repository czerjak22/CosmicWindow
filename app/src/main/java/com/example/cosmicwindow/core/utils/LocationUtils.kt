package com.example.cosmicwindow.core.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationUtils
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val client =
            LocationServices.getFusedLocationProviderClient(context)

        @SuppressLint("MissingPermission")
        suspend fun getLocation(): Pair<Double, Double>? =
            try {
                val location =
                    client
                        .getCurrentLocation(
                            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                            CancellationTokenSource().token,
                        ).await()

                location?.let {
                    Pair(it.latitude, it.longitude)
                }
            } catch (e: Exception) {
                null
            }
    }
