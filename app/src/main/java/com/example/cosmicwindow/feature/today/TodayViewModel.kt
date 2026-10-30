package com.example.cosmicwindow.feature.today

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.ApodRepository
import com.example.cosmicwindow.model.Apod
import com.example.cosmicwindow.model.Mediatype
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class TodayViewModel @Inject constructor(
    val apodRepository: ApodRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<TodayState>(TodayState())

    val uiState = _uiState.asStateFlow();
    fun updateError() {
        _uiState.update { it.copy(error = "Baj van") }
    }

    init {
        getCurrentApods()
    }

    fun getCurrentApods() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val apods = apodRepository.getApodWeek(_uiState.value.selectedDate).fold(
                onSuccess = { apods ->
                    if (apods.isNotEmpty()) {
                        Log.w("debug", " got ${apods.size} First apod: ${apods.first()}")
                    } else {
                        Log.e("debug", "Apods are empty!")
                    }
                    val videoApod = searchForFirstVideoApod(apods)
                    _uiState.update {
                        it.copy(
                            apods = apods,
                            error = null,
                            featuredVideoApod = videoApod,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }


    fun updateCurretDay(newDate: LocalDate) {
        _uiState.update {
            it.copy(
                selectedDate = newDate,
            )
        }
        Log.w("debug", "Updated selected date to: $newDate")
        getCurrentApods()
    }


    private fun searchForFirstVideoApod(apods: List<Apod>): Apod? {
        return apods.stream().filter { it.mediaType == Mediatype.VIDEO }.findFirst().orElse(null);

    }


}