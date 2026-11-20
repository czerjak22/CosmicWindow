package com.example.cosmicwindow.feature.saved

import com.example.cosmicwindow.model.SavedItem

data class SavedState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val folderSize: Float = 0f,
    val items: List<SavedItem> = emptyList(),
)
