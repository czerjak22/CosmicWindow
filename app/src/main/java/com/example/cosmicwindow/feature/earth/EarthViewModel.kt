package com.example.cosmicwindow.feature.earth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.core.utils.LocationUtils
import com.example.cosmicwindow.data.repository.EarthRepository
import com.example.cosmicwindow.model.ProjectedPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class EarthViewModel
    @Inject
    constructor(
        val earthRepository: EarthRepository,
        val locationUtils: LocationUtils,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(EarthState())
        val uiState = _uiState.asStateFlow()
        private var apiJob: Job? = null
        private var playbackJob: Job? = null
        private var isLocationProjectionEnabled: Boolean = false
        private var userLocation: Pair<Double, Double>? = null

        init {
            loadEarthImages()
        }

        fun enableLocationProjection() {
            viewModelScope.launch {
                isLocationProjectionEnabled = true
                userLocation = locationUtils.getLocation()
            }
        }

        fun projectLocationOntoImage(): ProjectedPoint? {
            if (!isLocationProjectionEnabled ||
                uiState.value.images.isEmpty() ||
                uiState.value.selectedIndex == null ||
                userLocation == null
            ) {
                return null
            }

            val earth = uiState.value.images[uiState.value.selectedIndex ?: 0]
            val centroid = earth.centroidCoordinates
            val telescopePos = earth.telescopePosition

            val phi0 = Math.toRadians(centroid.lat)
            val lambda0 = Math.toRadians(centroid.lon)
            val phi = Math.toRadians(userLocation!!.first)
            val lambda = Math.toRadians(userLocation!!.second)
            val dLambda = lambda - lambda0

            val cosC = sin(phi0) * sin(phi) + cos(phi0) * cos(phi) * cos(dLambda)
            if (cosC <= 0.2) {
                return ProjectedPoint(isVisible = false, u = 0f, v = 0f)
            }

            var x = cos(phi) * sin(dLambda)
            var y = cos(phi0) * sin(phi) - sin(phi0) * cos(phi) * cos(dLambda)

            val earthRadiusKm = 6371.0
            val distKm =
                sqrt(
                    telescopePos.x * telescopePos.x +
                        telescopePos.y * telescopePos.y +
                        telescopePos.z * telescopePos.z,
                )
            val perspectiveScale = (distKm - earthRadiusKm) / (distKm - earthRadiusKm * cosC)
            x *= perspectiveScale
            y *= perspectiveScale

            val angularRadiusRad = asin(earthRadiusKm / distKm)
            val fovRad = Math.toRadians(0.7)
            val earthRadiusFraction = (angularRadiusRad / fovRad).toFloat()
            // val earthRadiusFraction = 0.394

            val u = (0.5 + x * earthRadiusFraction).toFloat()
            val v = (0.5 - y * earthRadiusFraction).toFloat()

            return ProjectedPoint(isVisible = true, u = u, v = v)
        }

        fun loadEarthImages() {
            _uiState.update { it.copy(isLoading = true, error = null) }
            apiJob?.cancel()
            apiJob =
                viewModelScope.launch {
                    earthRepository.getLatestEarthPictures().fold(onSuccess = { newImages ->
                        var selected: Int? = null
                        if (newImages.isNotEmpty()) {
                            selected = 0
                        }
                        _uiState.update {
                            it.copy(
                                images = newImages,
                                isLoading = false,
                                error = null,
                                selectedIndex = selected,
                            )
                        }
                        Log.w(
                            "ViewModel",
                            "Loaded images to state successfully imageSize:${newImages.size} , " +
                                "date:${if (newImages.isNotEmpty()) newImages.first().date else "images are empty"}",
                        )
                    }, onFailure = { err ->
                        Log.w("ViewModel", "error: $err")
                        _uiState.update {
                            it.copy(images = emptyList(), isLoading = false, error = err.message)
                        }
                    })
                }
        }

        fun updateSelected(newIndex: Int) {
            if (uiState.value.locationPoints.isEmpty() && isLocationProjectionEnabled) {
                enableLocationProjection()
            }
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        selectedIndex = newIndex,
                    )
                }
            }
        }

        fun startPlayback() {
            if (uiState.value.images.size <= 1) return

            playbackJob?.cancel()

            playbackJob =
                viewModelScope.launch {
                    while (isActive) {
                        delay(500.milliseconds)
                        updateSelected(((uiState.value.selectedIndex ?: 0) + 1) % uiState.value.images.size)
                    }
                }
        }

        fun stopPlayback() {
            playbackJob?.cancel()
            playbackJob = null
        }
    }
