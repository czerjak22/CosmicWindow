package com.example.cosmicwindow.feature.saved

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.data.repository.SavedRepository
import com.example.cosmicwindow.model.SavedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

sealed interface SavedEvent {
    data object ShowUndoDelete : SavedEvent
}

private data class PendingDelete(
    val item: SavedItem,
    val job: Job,
)

@HiltViewModel
class SavedViewModel
    @Inject
    constructor(
        val savedRepository: SavedRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SavedState())
        val uiState = _uiState.asStateFlow()
        private var loadJob: Job? = null

        companion object {
            val UNDO_TIMEOUT = 3.seconds
        }

        private val _events = Channel<SavedEvent>(Channel.BUFFERED)
        val events = _events.receiveAsFlow()
        private var pendingDelete: PendingDelete? = null

        init {
            loadItems()
        }

        fun loadItems() {
            if (loadJob?.isActive == true) {
                return
            }
            _uiState.update { it.copy(isLoading = true, error = null) }
            loadJob =
                viewModelScope.launch {
                    val folderSize = savedRepository.getImageFolderSizeMb()
                    savedRepository.loadItems().fold(
                        onSuccess = { items ->
                            val newItems = items.map { it.copy(apod = it.apod.copy(isSaved = true)) }
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    items = newItems,
                                    folderSize = folderSize,
                                )
                            }
                        },
                        onFailure = { err ->
                            _uiState.update { it.copy(isLoading = false, error = err.message) }
                        },
                    )
                }
        }

        fun onErrorRetry() {
            loadItems()
        }

        fun onDeleteClicked(savedItem: SavedItem) {
            commitPendingDelete()
            setSaved(savedItem.id, false)

            val job =
                viewModelScope.launch {
                    delay(UNDO_TIMEOUT)
                    pendingDelete = null
                    finalizeDelete(savedItem)
                }
            pendingDelete = PendingDelete(savedItem, job)
            _events.trySend(SavedEvent.ShowUndoDelete)
        }

        fun undoDelete() {
            val pending = pendingDelete ?: return
            pending.job.cancel()
            pendingDelete = null
            setSaved(pending.item.id, true)
        }

        fun commitPendingDelete() {
            val pending = pendingDelete ?: return
            pending.job.cancel()
            pendingDelete = null
            viewModelScope.launch { finalizeDelete(pending.item) }
        }

        private suspend fun finalizeDelete(item: SavedItem) {
            savedRepository.deleteItem(item).fold(
                onSuccess = {
                    Log.w("ViewModel", "Deleted apod ${item.id}")
                    val folderSize = savedRepository.getImageFolderSizeMb()
                    _uiState.update { state ->
                        state.copy(
                            items = state.items.filterNot { it.id == item.id },
                            folderSize = folderSize,
                        )
                    }
                },
                onFailure = { err ->
                    setSaved(item.id, true)
                    _uiState.update { it.copy(error = err.message ?: "error") }
                },
            )
        }

        private fun setSaved(id: Long, saved: Boolean) {
            _uiState.update { state ->
                state.copy(
                    items =
                        state.items.map {
                            if (it.id == id) it.copy(apod = it.apod.copy(isSaved = saved)) else it
                        },
                )
            }
        }
    }
