package com.example.cosmicwindow.feature.explore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmicwindow.R
import com.example.cosmicwindow.data.ExploreRepository
import com.example.cosmicwindow.model.Filter
import com.example.cosmicwindow.model.QueryExplore
import com.example.cosmicwindow.model.SearchResultItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExploreViewModel
@Inject constructor(
    val exploreRepository: ExploreRepository

) : ViewModel() {
    private val _uiState = MutableStateFlow(ExploreState(queryState = QueryExplore(filters = generateFilters())))
    val uiState = _uiState.asStateFlow()
    private var newSearchJob: Job? = null;
    private var loadMoreJob: Job? = null;
    private var lastSearched: QueryExplore? = null;

    val prefetchVariable = 4;

    init {
        watchQueryChanges()
    }


    @OptIn(FlowPreview::class)
    fun watchQueryChanges(): Unit {
        viewModelScope.launch {
            _uiState.map { it.queryState.copy(page = 1) }
                .distinctUntilChanged()
                .debounce(400.milliseconds)
                .collectLatest { newSearch() }
        }
    }

    fun onQueryChanged(text: String) {
        _uiState.update { it.copy(queryState = it.queryState.copy(query = text)) }
    }

    fun newSearch(): Unit {
        val query = _uiState.value.queryState.copy(
            page = 1,
            query = _uiState.value.queryState.query.trim(),
        )

        if (query == lastSearched) return
        lastSearched = query

        _uiState.update {
            it.copy(
                isLoading = true, searchItems = emptyList(), endReached = false, error = null
            )
        }

        loadMoreJob?.cancel()
        newSearchJob?.cancel()
        newSearchJob = viewModelScope.launch {
            exploreRepository.getSearchByQuery(query).fold(onSuccess = { result ->
                Log.w("ViewModel", "returned Search: $result")
                _uiState.update {
                    it.copy(
                        isLoading = false, searchItems = result.searchItems, queryState = query
                    )
                }
            }, onFailure = { err ->
                _uiState.update {
                    if (err is CancellationException) throw err
                    it.copy(isLoading = false, error = err.message)
                }
            })

        }

    }

    fun increasePageNumber() {
        _uiState.update { it.copy(queryState = it.queryState.copy(page = it.queryState.page + 1)) }
    }

    fun onErrorRetry() {
        _uiState.update { it.copy(queryState = QueryExplore(), isLoading = true, error = null) }
        newSearch()
    }


    fun loadMore() {
        if (_uiState.value.isLoading || _uiState.value.endReached) {
            return
        }

        _uiState.update {
            it.copy(isLoading = true)
        }
        increasePageNumber()
        val stateVal = _uiState.value
        val query = stateVal.queryState
        loadMoreJob = viewModelScope.launch {
            exploreRepository.getSearchByQuery(query).fold(onSuccess = { newItems ->
                val existingIds = stateVal.searchItems.mapTo(HashSet()) { it.nasaId }
                val fresh = newItems.searchItems.filter { it.nasaId !in existingIds }
                val newList: List<SearchResultItem> = stateVal.searchItems + fresh
                if (
                    newItems.searchItems.size < query.pageSize || fresh.isEmpty()
                ) {
                    Log.w(
                        "Repository",
                        "End reached true ${newItems.searchItems.size}< ${query.pageSize} || fresh.isEmpty: ${fresh.isEmpty()}"
                    )
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        searchItems = newList,
                        endReached = newItems.searchItems.size < query.pageSize || fresh.isEmpty()
                    )
                }
            }, onFailure = { err ->
                if (err is CancellationException) throw err
                _uiState.update {
                    it.copy(isLoading = false, error = err.message)
                }
            })
        }
    }

    fun generateFilters(): List<Filter> {
        val list = mutableListOf<Filter>()
        list.add(
            Filter(
                text = "James Webb",
                icon = R.drawable.today_icon,
            )
        )
        list.add(
            Filter(
                text = "Mars",
                icon = R.drawable.earth_icon,
            )
        )
        list.add(
            Filter(
                text = "Moon",
                icon = R.drawable.moon_icon,
            )
        )
        list.add(
            Filter(
                text = "Nebula",
                icon = R.drawable.nebula_icon,
            )
        )
        return list
    }

    fun toggleFilter(index: Int) {
        val newFilters = _uiState.value.queryState.filters.map { item ->
            when {
                _uiState.value.queryState.filters.indexOf(item) == index -> item.copy(isActive = !item.isActive)

                else -> item
            }
        }
        _uiState.update { it.copy(queryState = it.queryState.copy(filters = newFilters)) }
    }
}
